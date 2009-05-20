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
package org.jahia.ajax.gwt.templates.components.actionmenus.server.helper;

import org.apache.log4j.Logger;
import org.jahia.ajax.gwt.aclmanagement.server.ACLHelper;
import org.jahia.ajax.gwt.client.data.acl.GWTJahiaNodeACE;
import org.jahia.ajax.gwt.client.data.acl.GWTJahiaNodeACL;
import org.jahia.ajax.gwt.client.data.actionmenu.acldiff.GWTJahiaAclDiffDetails;
import org.jahia.ajax.gwt.client.data.actionmenu.acldiff.GWTJahiaAclDiffState;
import org.jahia.ajax.gwt.utils.JahiaObjectCreator;
import org.jahia.content.ContentObject;
import org.jahia.exceptions.JahiaException;
import org.jahia.params.ProcessingContext;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.acl.JahiaBaseACL;
import org.jahia.services.containers.ContentContainer;
import org.jahia.services.containers.ContentContainerList;
import org.jahia.services.lock.LockPrerequisites;
import org.jahia.services.pages.ContentPage;
import org.jahia.services.preferences.user.UserPreferencesHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for rendering ACL difference information.
 *
 * @author rfelden
 * @version 28 f�vr. 2008 - 16:03:35
 */
public class AclDiffHelper {

    private final static List<String> PERMISSIONS = new ArrayList<String>() ;
    static {
        PERMISSIONS.add("read") ;
        PERMISSIONS.add("write") ;
        PERMISSIONS.add("admin") ;
    }
    private final static Logger logger = Logger.getLogger(AclDiffHelper.class) ;

    /**
     * Check if there is an ACL break between the given object and its parent.
     *
     * @param therequest the current request
     * @param jParams the processing context
     * @param objectKey the current object key
     * @return the acl diff state (or null if no particular state)
     */
    public static GWTJahiaAclDiffState getAclDiffState(HttpServletRequest therequest, ProcessingContext jParams, String objectKey) {
        Boolean aclDifferenceParam = UserPreferencesHelper.isDisplayAclDiffState(jParams.getUser());
        try {
            ContentObject obj = JahiaObjectCreator.getContentObjectFromString(objectKey) ;

            // check but should never be null
            if (obj == null) {
                logger.warn("Content object for key " + objectKey + " should not be null") ;
                return null ;
            }

            // only check for write rights, no admi rights required to display state / popup
            if (aclDifferenceParam) {
                aclDifferenceParam = obj.checkWriteAccess(jParams.getUser()) && ServicesRegistry.getInstance().getJahiaACLManagerService().getSiteActionPermission(LockPrerequisites.RIGHTS, jParams.getUser(), JahiaBaseACL.READ_RIGHTS, jParams.getSiteID()) > 0 ;
            }

            if (aclDifferenceParam && !objectKey.equals("ContentPage_" + jParams.getSite().getHomePageID()) && (!obj.isAclSameAsParent() &&  (!obj.getACL().getACL().getEntries().isEmpty() || obj.getACL().getInheritance() == 1 ))) {
                return new GWTJahiaAclDiffState(objectKey) ;
            }
        } catch (final JahiaException je) {
            logger.error(je, je);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null ;
    }

    public static GWTJahiaAclDiffDetails getAclDiffDetails(ProcessingContext jParams, String objectKey) {
        if (objectKey != null && objectKey.length() > 0) {
            try {
                ContentObject obj = JahiaObjectCreator.getContentObjectFromString(objectKey) ;

                // check but should never be null
                if (obj == null) {
                    logger.warn("Content object for key " + objectKey + " should not be null") ;
                    return null ;
                }

                GWTJahiaNodeACL acls = ACLHelper.getGWTJahiaNodeACL(obj.getACL(), jParams) ;
                Map<String, String> rights = new HashMap<String, String>() ;
                Map<String, String> inheritedRights = new HashMap<String, String>() ;
                for (GWTJahiaNodeACE ace: acls.getAce()) {
                    String principal = ace.getPrincipal() ;
                    StringBuilder permBuf = new StringBuilder() ;
                    StringBuilder inhPermBuf = new StringBuilder() ;
                    // build (rwa / rw- / r-- / ---) strings for local and inherited permissions
                    String inhFrom = ace.getInheritedFrom() ;
                    Map<String, String> inheritedPermissions = ace.getInheritedPermissions() ;
                    if (inhFrom == null || inheritedPermissions == null) {
                        inhPermBuf.append("   ") ;
                    } else {
                        for (String perm: PERMISSIONS) {
                            if (inheritedPermissions.containsKey(perm)) {
                                if (inheritedPermissions.get(perm).equalsIgnoreCase("grant")) {
                                    inhPermBuf.append(perm.substring(0, 1)) ;
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(principal +  " can " + perm + " (inh)") ;
                                    }
                                } else {
                                    inhPermBuf.append("-") ;
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(principal +  " cannot " + perm + " (inh)") ;
                                    }
                                }
                            } else {
                                inhPermBuf.append(" ") ;
                                if (logger.isDebugEnabled()) {
                                    logger.debug(principal +  " cannot " + perm + " (inh / not found)") ;
                                }
                            }
                        }
                    }
                    Map<String, String> permissions = ace.getPermissions() ;
                    if (ace.isInherited() || permissions == null) {
                        permBuf.append("   ") ;
                    } else {
                        for (String perm: PERMISSIONS) {
                            if (permissions.containsKey(perm)) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug(permissions.get(perm));
                                }
                                if (permissions.get(perm).equalsIgnoreCase("grant")) {
                                    permBuf.append(perm.substring(0, 1)) ;
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(principal +  " can " + perm) ;
                                    }
                                } else {
                                    permBuf.append("-") ;
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(principal +  " cannot " + perm) ;
                                    }
                                }
                            } else {
                                // this case should never occur
                                permBuf.append(" ") ;
                                if (logger.isDebugEnabled()) {
                                    logger.debug(principal +  " cannot " + perm + " (not found)") ;
                                }
                            }
                        }
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug(principal + " : " + permBuf.toString()) ;
                        logger.debug(principal + " : " + inhPermBuf.toString()) ;
                    }
                    rights.put(principal, permBuf.toString()) ;
                    inheritedRights.put(principal, inhPermBuf.toString()) ;
                }

                String url = null ;
                if (obj.checkAdminAccess(jParams.getUser())) { // user can open engine only if admin access is allowed
                    if (obj instanceof ContentContainerList) {
                        url = ActionMenuServiceHelper.drawContainerListPropertiesLauncher(jParams, (ContentContainerList) obj, false, 0, "rightsMgmt");
                    } else if (obj instanceof ContentContainer) {
                        url = ActionMenuServiceHelper.drawUpdateContainerLauncher(jParams, (ContentContainer) obj, false, 0, "rightsMgmt");
                    } else if (obj instanceof ContentPage) {
                        url = ActionMenuServiceHelper.drawPagePropertiesLauncher(jParams, false, obj.getID(), "rightsMgmt");
                    }
                }
                return new GWTJahiaAclDiffDetails(url, rights, inheritedRights) ;
            } catch (final JahiaException je) {
                logger.error(je, je);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null ;
    }



}
