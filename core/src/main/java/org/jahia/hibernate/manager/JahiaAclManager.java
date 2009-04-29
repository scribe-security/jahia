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
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.jahia.hibernate.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jahia.hibernate.dao.JahiaAclDAO;
import org.jahia.hibernate.dao.JahiaContainerDAO;
import org.jahia.hibernate.dao.JahiaContainerListDAO;
import org.jahia.hibernate.model.JahiaAcl;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.cache.Cache;
import org.jahia.services.cache.CacheService;
import org.jahia.exceptions.JahiaInitializationException;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rincevent
 * Date: 28 déc. 2004
 * Time: 16:41:35
 * To change this template use File | Settings | File Templates.
 */
public class JahiaAclManager {
// ------------------------------ FIELDS ------------------------------

    private JahiaAclDAO dao = null;
    private JahiaContainerDAO containerDAO = null;
    private JahiaContainerListDAO containerListDAO = null;
    private Log log = LogFactory.getLog(JahiaAclManager.class);
    private Cache groupEntriesCache = null;
    public static final String JAHIA_ACL_GROUP_ENTRIES = "JAHIA_ACL_GROUP_ENTRIES";
// --------------------- GETTER / SETTER METHODS ---------------------

    public List<JahiaAcl> getAllAcls() {
        return dao.getAcls();
    }

    public List<JahiaAcl> getChildAcls(int parentId) {
        return dao.getChildAcls(new Integer(parentId));
    }
    
    public List<JahiaAcl> getChildAclsOnPage(List<Integer> parentAclIds, int pageId) {
        return dao.getChildAclsOnPage(parentAclIds, pageId);
    }     
    
    public void setJahiaAclDAO(JahiaAclDAO dao) {
        this.dao = dao;
    }

    public void setJahiaContainerDAO(JahiaContainerDAO containerDAO) {
        this.containerDAO = containerDAO;
    }

    public void setJahiaContainerListDAO(JahiaContainerListDAO containerListDAO) {
        this.containerListDAO = containerListDAO;
    }

// -------------------------- OTHER METHODS --------------------------

    public List<Integer> findAllContainerAclsIdInSite(int siteId) {
        Integer integer = new Integer(siteId);
        List<Integer> retVal = null;
        try {
            retVal = containerDAO.getAllContainerAclIdsFromSite(integer);
            retVal.addAll(containerListDAO.getAclContainerListIdsInSite(integer));
        } catch (ObjectRetrievalFailureException e) {
            log.warn("Cannot find acls for site " + siteId, e);
        }
        return retVal;
    }

    public JahiaAcl findJahiaAclById(String id) {
        if(id!=null) {
            try {
                Integer integer = new Integer(id);
                if(integer.intValue()>0){
                    return dao.findAclById(integer);
                }
            } catch (NumberFormatException e) {
                log.warn("Try to find an acl with passing a  non number string "+id,e);
            }
        }
        throw new ObjectRetrievalFailureException(JahiaAcl.class,id);
    }

    public void preloadACLs(Cache mACLCache) {
        List<JahiaAcl> acls = getAllAcls();
        for (Iterator<JahiaAcl> it = acls.iterator(); it.hasNext();) {        
            JahiaAcl acl = (JahiaAcl) it.next();
            mACLCache.put(acl.getId(), acl);
        }
    }

    public void remove(String id) {
        dao.removeAcl(new Integer(id));
        flushCache();
    }

    public void removeAclGroupEntries(String name) {
        dao.removeGroupAclEntries(name);
        flushCache();
    }

    public void removeAclUserEntries(String name) {
        dao.removeUserAclEntries(name);
        flushCache();
    }

    public boolean isGroupUsedInAclEntries(String groupName) {
        Cache cache = checkCache();
        if(cache!=null) {
            if(cache.containsKey(groupName)){
                Boolean result = (Boolean)cache.get(groupName);
                if (result != null) {
                    return result.booleanValue();
                }
            }
            boolean groupUsedInAclEntries = dao.isGroupUsedInAclEntries(groupName);
            if(groupUsedInAclEntries) cache.put(groupName,Boolean.TRUE);
            else cache.put(groupName,Boolean.FALSE);
            return  groupUsedInAclEntries;
        }
        return dao.isGroupUsedInAclEntries(groupName);
    }

    public Collection<String> findAllTarget(int type) {
        return dao.findAllTarget(type);
    }

    public void saveAcl(JahiaAcl acl) {
        if(acl.getId()!=null)  {
            flushCache();
        }
        dao.saveAcl(acl);
    }

    public void flushCache() {
        ServicesRegistry instance = ServicesRegistry.getInstance();
        if(instance!=null) {
            CacheService cacheService = instance.getCacheService();
            if(cacheService!=null) {
                Cache cache = cacheService.getCache(JahiaAcl.JAHIA_ACL_PERMISSIONS_CACHE);
                if(cache!=null)
                    cache.flush(true);
                cache = cacheService.getCache(JAHIA_ACL_GROUP_ENTRIES);
                if(cache!=null)
                    cache.flush(true);
            }
        }
    }

    public Cache checkCache() {
        if (groupEntriesCache == null) {
            ServicesRegistry instance = ServicesRegistry.getInstance();
            if (instance != null) {
                CacheService cacheService = instance.getCacheService();
                if (cacheService != null) {
                    try {
                        groupEntriesCache = cacheService.createCacheInstance(JAHIA_ACL_GROUP_ENTRIES);
                    } catch (JahiaInitializationException e) {
                        log.error(e);
                    }
                }
            }
        }
        return groupEntriesCache;
    }

    public void update(JahiaAcl jahiaACL) {
        dao.updateAcl(jahiaACL);
        flushCache();
    }
}

