/**
 * This file is part of Jahia: An integrated WCM, DMS and Portal Solution
 * Copyright (C) 2002-2011 Jahia Solutions Group SA. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL (or any later version), you may redistribute this Program in connection
 * with Free/Libre and Open Source Software ("FLOSS") applications as described
 * in Jahia's FLOSS exception. You should have received a copy of the text
 * describing the FLOSS exception, and it is also available here:
 * http://www.jahia.com/license
 *
 * Commercial and Supported Versions of the program
 * Alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms contained in a separate written agreement
 * between you and Jahia Solutions Group SA. If you are unsure which license is appropriate
 * for your use, please contact the sales department at sales@jahia.com.
 */

package org.jahia.services.content;

import org.apache.jackrabbit.core.security.JahiaPrivilegeRegistry;
import org.jahia.exceptions.JahiaException;
import org.jahia.exceptions.JahiaInitializationException;
import org.jahia.services.JahiaAfterInitializationService;
import org.jahia.services.JahiaService;
import org.jahia.services.content.nodetypes.NodeTypeRegistry;
import org.jahia.services.content.interceptor.PropertyInterceptor;
import org.jahia.services.content.interceptor.InterceptorChain;
import org.jahia.services.usermanager.JahiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a Jahia service, which manages the delegation of JCR store related deployment
 * and export functions to the right <code>JCRStoreProvider</code>.
 *
 * @author toto
 */
public class JCRStoreService extends JahiaService implements JahiaAfterInitializationService {
    
    private static Logger logger = LoggerFactory.getLogger(JCRStoreService.class);

    private Map<String, String> decorators = new HashMap<String, String>();

    private InterceptorChain interceptorChain;

    static private JCRStoreService instance = null;
    private JCRSessionFactory sessionFactory;

    private Map<String,List<DefaultEventListener>> listeners;

    protected JCRStoreService() {
    }

    public static JCRStoreService getInstance() {
        if (instance == null) {
            synchronized (JCRStoreService.class) {
                if (instance == null) {
                    instance = new JCRStoreService();
                }
            }
        }
        return instance;
    }

    public void setSessionFactory(JCRSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public JCRSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void start() throws JahiaInitializationException {
        try {
            NamespaceRegistry nsRegistry = sessionFactory.getNamespaceRegistry();
            NodeTypeRegistry ntRegistry = NodeTypeRegistry.getInstance();
            Set<String> prefixes = ImmutableSet.copyOf(nsRegistry.getPrefixes());
            for (Map.Entry<String, String> namespaceEntry : ntRegistry.getNamespaces().entrySet()) {
                if (!prefixes.contains(namespaceEntry.getKey())) {
                    nsRegistry
                            .registerNamespace(namespaceEntry.getKey(), namespaceEntry.getValue());
                }
            }

            initObservers(listeners);
        } catch (Exception e) {
            logger.error("Repository init error", e);
        }
    }

    public Map<String, String> getDecorators() {
        return decorators;
    }

    public void setDecorators(Map<String, String> decorators) {
        this.decorators = decorators;
    }

    public void setListeners(Map<String, List<DefaultEventListener>> listeners) {
        this.listeners = listeners;
    }

    public void setInterceptors(List<PropertyInterceptor> interceptors) {
        interceptorChain = new InterceptorChain();
        interceptorChain.setInterceptors(interceptors);
    }

    public InterceptorChain getInterceptorChain() {
        return interceptorChain;
    }

    public Map<String, List<DefaultEventListener>> getListeners() {
        return listeners;
    }

    private void initObservers(Map<String, List<DefaultEventListener>> listeners) throws RepositoryException {
        if (listeners != null) {
            for (String ws : listeners.keySet()) {
                List<DefaultEventListener> l = listeners.get(ws);

                // This session must not be released
                final Session session = getSessionFactory().getSystemSession(null,ws);
                final Workspace workspace = session.getWorkspace();

                ObservationManager observationManager = workspace.getObservationManager();
                for (DefaultEventListener listener : l) {
                	if (listener.getEventTypes() > 0) {
	                    listener.setWorkspace(ws);
	                    observationManager.addEventListener(listener, listener.getEventTypes(), listener.getPath(), listener.isDeep(), listener.getUuids(), listener.getNodeTypes(), false);
                	} else {
						logger.info("Skipping listener {} as it has no event types configured.",
						        listener.getClass().getName());
                	}
                }
            }
        }
    }

    public void stop() throws JahiaException {
    }

    public void deployDefinitions(String systemId) {
        for (JCRStoreProvider provider : sessionFactory.getProviders().values()) {
            if (provider.canRegisterCustomNodeTypes()) {
                provider.deployDefinitions(systemId);
            }
        }
    }

    public void deployExternalUser(JahiaUser jahiaUser) throws RepositoryException {
        JCRStoreProvider provider = sessionFactory.getMountPoints().get("/");
        provider.deployExternalUser(jahiaUser);
    }

    public JCRNodeWrapper getUserFolder(JahiaUser user) throws RepositoryException {
        return sessionFactory.getMountPoints().get("/").getUserFolder(user);
    }

    public List<JCRNodeWrapper> getImportDropBoxes(String site, JahiaUser user) {
        List<JCRNodeWrapper> r = new ArrayList<JCRNodeWrapper>();
        for (JCRStoreProvider storeProvider : sessionFactory.getMountPoints().values()) {
            try {
                r.addAll(storeProvider.getImportDropBoxes(site, user));
            } catch (RepositoryException e) {
                logger.warn("Error when querying repository", e);
            }
        }
        return r;
    }

    public JCRNodeWrapper decorate(JCRNodeWrapper w) {
        try {
            for (String type : decorators.keySet()) {
                if (w.isNodeType(type)) {
                    String className = decorators.get(type);
                    try {
                        return (JCRNodeWrapper) Class.forName(className).getConstructor(JCRNodeWrapper.class).newInstance(w);
                    } catch (Exception e) {
                        logger.error("Cannot decorate node", e);
                    }
                }
            }
        } catch (RepositoryException e) {
            logger.error("Error while decorating node", e);
        }
        return w;
    }

    public void initAfterAllServicesAreStarted() throws JahiaInitializationException {
        try {
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Object>() {
                public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    JahiaPrivilegeRegistry.init(session);
                    return null;
                }
            });
        } catch (RepositoryException e) {
            throw new JahiaInitializationException("Cannot register permissions",e);
        }
    }
}
