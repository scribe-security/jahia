/**
 * Jahia Enterprise Edition v6
 *
 * Copyright (C) 2002-2009 Jahia Solutions Group. All rights reserved.
 *
 * Jahia delivers the first Open Source Web Content Integration Software by combining Enterprise Web Content Management
 * with Document Management and Portal features.
 *
 * The Jahia Enterprise Edition is delivered ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED.
 *
 * Jahia Enterprise Edition must be used in accordance with the terms contained in a separate license agreement between
 * you and Jahia (Jahia Sustainable Enterprise License - JSEL).
 *
 * If you are unsure which license is appropriate for your use, please contact the sales department at sales@jahia.com.
 */
/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.jahia.hibernate.manager;

import org.apache.commons.collections.FastArrayList;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.jahia.hibernate.dao.JahiaAuditLogDAO;
import org.jahia.hibernate.dao.JahiaContainerDAO;
import org.jahia.hibernate.dao.JahiaContainerListDAO;
import org.jahia.hibernate.dao.JahiaFieldsDataDAO;
import org.jahia.hibernate.model.JahiaAuditLog;
import org.jahia.params.ProcessingContext;
import org.jahia.services.audit.LoggingEventListener;
import org.jahia.utils.JahiaObjectTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rincevent
 * Date: 15 avr. 2005
 * Time: 11:53:22
 * To change this template use File | Settings | File Templates.
 */
public class JahiaAuditLogManager {
    private Log log = LogFactory.getLog(getClass());
    
    private JahiaAuditLogDAO dao = null;
    private JahiaContainerDAO containerDAO = null;
    private JahiaFieldsDataDAO fieldsDAO = null;
    private JahiaContainerListDAO listDAO = null;
    private static final String START_TIME = "starttime";

    public void setJahiaAuditLogDAO(JahiaAuditLogDAO dao) {
        this.dao = dao;
    }

    public void setJahiaContainerDAO(JahiaContainerDAO containerDAO) {
        this.containerDAO = containerDAO;
    }

    public void setJahiaFieldsDataDAO(JahiaFieldsDataDAO fieldsDAO) {
        this.fieldsDAO = fieldsDAO;
    }

    public void setJahiaContainerListDAO(JahiaContainerListDAO listDAO) {
        this.listDAO = listDAO;
    }

    public boolean insertAuditLog(int entryID, Long time, String userNameStr, String objTypeStr, String objIDStr,
                                  String parentObjIDStr, String parentObjTypeStr, String siteKey, String operationStr,
                                  String contentStr, long startTime) {
        JahiaAuditLog auditLog = new JahiaAuditLog(null, time, userNameStr, new Integer(objTypeStr),
                                              new Integer(objIDStr), new Integer(parentObjTypeStr),
                                              new Integer(parentObjIDStr), operationStr, siteKey, contentStr);
        try {
            auditLog.setEventType(START_TIME);
            Long in = new Long(startTime);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(in);
            objectOutputStream.close();
            auditLog.setEventInformation(Hibernate.createBlob(baos.toByteArray()));
        } catch (IOException e) {
            log.error("Error serializing object", e);
        }
        dao.save(auditLog);
        return false;
    }
 
    public List getAllChildren(int objectType, int objectID, List parents) {
        List fullChildrenList = parents == null ? new FastArrayList(103)
                : parents;
        List tempChildrenList = getChildrenList(objectType, objectID);
        for (Iterator it = tempChildrenList.iterator(); it.hasNext();) {
            Integer[] newChild = (Integer[]) it.next();
            fullChildrenList.add(newChild);
            Integer newObjType = newChild[0];
            Integer newObjID = newChild[1];
            getAllChildren(newObjType.intValue(), newObjID.intValue(),
                    fullChildrenList);
        }
        if (fullChildrenList instanceof FastArrayList) {
            ((FastArrayList)fullChildrenList).setFast(true);
        }
        return fullChildrenList;
    }

    private List getChildrenList(int objectType, int objectID) {
        List list = null;
        FastArrayList retList = new FastArrayList(103);
        Integer integer;
        switch (objectType) {

            // no Children...
            case LoggingEventListener.FIELD_TYPE:
                break;

                // Children can be CONTAINER...
            case LoggingEventListener.CONTAINERLIST_TYPE:
                list = containerDAO.getAllContainerIdsFromList(new Integer(objectID));
                integer = new Integer(JahiaObjectTool.CONTAINER_TYPE);
                fillListWithArrayOfIntegers(list, integer, retList);
                break;

                // Children can be FIELD or CONTAINERLIST...
            case LoggingEventListener.CONTAINER_TYPE:
                list = fieldsDAO.findNonDeletedFieldsIdInContainer(new Integer(objectID));
                // child found in fields...
                if (!list.isEmpty()) {
                    integer = new Integer(JahiaObjectTool.FIELD_TYPE);
                    fillListWithArrayOfIntegers(list, integer, retList);
                    // no child found in fields... look in container lists...
                } else {
                    list = listDAO.getNonDeletedContainerListIdsInContainer(new Integer(objectID));
                    integer = new Integer(JahiaObjectTool.CONTAINERLIST_TYPE);
                    fillListWithArrayOfIntegers(list, integer, retList);
                }
                break;

                // Children can be PAGE, FIELD or CONTAINERLIST...
            case LoggingEventListener.PAGE_TYPE:
                list = fieldsDAO.findNonDeletedFieldsIdInPage(new Integer(objectID));
                // child found in fields...

                integer = new Integer(JahiaObjectTool.FIELD_TYPE);
                fillListWithArrayOfIntegers(list, integer, retList);

                list = listDAO.getNonDeletedContainerListIdsInPage(new Integer(objectID));
                integer = new Integer(JahiaObjectTool.CONTAINERLIST_TYPE);
                fillListWithArrayOfIntegers(list, integer, retList);
                break;
        }
        retList.setFast(true);
        return retList;
    }

    private void fillListWithArrayOfIntegers(List list, Integer integer, List retList) {
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Integer id = (object instanceof Integer ? (Integer) object
                    : (Integer) ((Object[]) object)[0]);

            Integer[] child = new Integer[2];
            child[0] = integer;
            child[1] = id;
            if (!retList.contains(child)) {
                retList.add(child);
            }
        }
    }

    public List getLogs(int objectType, int objectID, List childrenObjectList, ProcessingContext processingContext) {
        List list = dao.getLogs(new Integer(objectType), new Integer(objectID), childrenObjectList);
        return fillList(list, processingContext);
    }

    private List fillList(List list, ProcessingContext processingContext) {
        FastArrayList retList = new FastArrayList(list.size());
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(3, 3, processingContext.getCurrentLocale());
        for (Iterator it = list.iterator(); it.hasNext();) {
            JahiaAuditLog auditLog = (JahiaAuditLog) it.next();
            String time;
            Long myTime = auditLog.getTime();
            java.util.Date myDate = new java.util.Date(myTime.longValue());
            time = dateTimeInstance.format(myDate);
            FastHashMap map = new FastHashMap(7);
            map.put("timeStr", time);
            map.put("time",myTime);
            map.put("username", auditLog.getUsername());
            map.put("operation", auditLog.getOperation());
            map.put("objecttype", auditLog.getObjecttype().toString());
            map.put("objectid", auditLog.getObjectid().toString());
            map.put("sitekey", auditLog.getSite());
            map.put("objectname", auditLog.getContent());
            map.put("parentid", auditLog.getParentid().toString());
            try {
                map.put("parenttype", JahiaObjectTool.getInstance().getObjectTypeName(auditLog.getParenttype().intValue()));
                // deactivated the parentname resolution, as it is *really* slow !
                //map.put("parentname", JahiaObjectTool.getInstance().getObjectName(log.getParenttype().intValue(), log.getParentid().intValue(), processingContext));
                map.put("parentname", auditLog.getParentid());
            } catch (Exception e) {
                map.put("parentname", auditLog.getParentid());
            }
            try {
                Blob eventInformation = auditLog.getEventInformation();
                if (eventInformation != null) {
                    ObjectInputStream is = new ObjectInputStream(eventInformation.getBinaryStream());
                    Object o = is.readObject();
                    if (auditLog.getEventType().equals(START_TIME))
                        map.put(START_TIME, o);
                }
            } catch (Exception e) {
                log.error("Error getting serialized object", e);
            }
            map.put("id", auditLog.getId());
            map.setFast(true);
            retList.add(map);
        }
        Collections.sort(retList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map map1 = (Map) o1;
                Map map2 = (Map) o2;
                //first order by site
                /*
                String site1 = (String) map1.get("sitekey");
                String site2 = (String) map2.get("sitekey");
                int result = (site1.compareTo(site2));
                if(result!=0) return result;
                */
                Long start1 = (Long) map1.get(START_TIME);
                Long start2 = (Long) map2.get(START_TIME);
                if (start1 != null && start2 != null && !start1.equals(start2)) {
                    return -(start1.compareTo(start2));
                } else {
                    Long id1 = (Long) map1.get("time");
                    Long id2 = (Long) map2.get("time");
                    return -(id1.compareTo(id2));
                }
            }
        });
        retList.setFast(true);
//        Collections.sort(retList, new Comparator() {
//            public int compare(Object o1, Object o2) {
//                Map map1 = (Map) o1;
//                Map map2 = (Map) o2;
//                //first order by site
//                String site1 = (String) map1.get("sitekey");
//                String site2 = (String) map1.get("sitekey");
//                return (site1.compareTo(site2));
//            }
//        });
        return retList;
    }

    public List getLogs(long fromDate, ProcessingContext processingContext) {
        List list = dao.getLogs(fromDate);
        return fillList(list, processingContext);
    }

    public int flushLogs(int objectType, int objectID, List childrenObjectList) {
        return dao.flushLogs(new Integer(objectType), new Integer(objectID), childrenObjectList);
    }

    public void flushLogs(String oldestEntryTime) {
        dao.flushLogs(new Long(oldestEntryTime));
    }

    public void flushSiteLogs(String siteKey) {
        dao.flushSiteLogs(siteKey);
    }

    public int enforceMaxLogs(int maxLogs) {
        return dao.enforceMaxLogs(maxLogs);
    }

    public int deleteAllLogs(){
        return dao.deleteAllLogs();
    }

    public void deleteOldestRow() {
        dao.deleteOldestRow();
    }

    public List<Object[]> executeCriteria(DetachedCriteria criteria, int maxResultSet){
        return dao.executeCriteria(criteria, maxResultSet);
    }

    public List executeNamedQuery(String queryName, Map parameters) {
        return dao.executeNamedQuery(queryName, parameters);
    }
}
