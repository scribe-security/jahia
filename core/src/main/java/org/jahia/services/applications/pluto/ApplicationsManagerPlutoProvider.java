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
package org.jahia.services.applications.pluto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletWindow;
import org.apache.pluto.descriptors.portlet.ExpirationCacheDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.driver.AttributeKeys;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.core.PortletWindowImpl;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.url.PortalURL;
import org.apache.pluto.internal.impl.PortletContextImpl;
import org.jahia.data.applications.ApplicationBean;
import org.jahia.data.applications.EntryPointDefinition;
import org.jahia.data.applications.EntryPointInstance;
import org.jahia.data.applications.PortletEntryPointDefinition;
import org.jahia.exceptions.JahiaException;
import org.jahia.exceptions.JahiaInitializationException;
import org.jahia.params.ParamBean;
import org.jahia.services.applications.ApplicationsManagerProvider;
import org.springframework.web.context.ServletContextAware;

/**
 * Created by IntelliJ IDEA.
 * User: Serge Huber
 * Date: 15 juil. 2008
 * Time: 15:54:24
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationsManagerPlutoProvider implements ApplicationsManagerProvider, ServletContextAware {

    private PortletContainer portletContainer;
    private ServletContext servletContext;

    public ApplicationsManagerPlutoProvider() {

    }

    public EntryPointInstance createEntryPointInstance(EntryPointDefinition entryPointDefinition) throws JahiaException {        
        final EntryPointInstance instance = new EntryPointInstance(null, entryPointDefinition.getContext(), entryPointDefinition.getName());
        if (entryPointDefinition instanceof PortletEntryPointDefinition) {
            PortletEntryPointDefinition portletEntryPointDefinition = (PortletEntryPointDefinition) entryPointDefinition;            
            final ExpirationCacheDD expirationCacheDD = portletEntryPointDefinition.getPortletDefinition().getExpirationCacheDD();
            if(expirationCacheDD!=null){
                instance.setExpirationTime(expirationCacheDD.getExpirationTime());
                instance.setCacheScope(expirationCacheDD.getScope());
            }
        }
        return instance;
    }

    public PortletWindow getPortletWindow(EntryPointInstance entryPointInstance, String windowID, ParamBean jParams) {

        JahiaContextRequest jahiaContextRequest = new JahiaContextRequest(jParams, jParams.getRealRequest());

        PortalRequestContext portalContext = new PortalRequestContext(jParams.getContext(), jahiaContextRequest, jParams.getResponse());

        PortletWindowConfig windowConfig =
            PortletWindowConfig.fromId(entryPointInstance.getContextName() + "."+entryPointInstance.getDefName() + "!" + windowID);
        windowConfig.setContextPath(entryPointInstance.getContextName());
        // Retrieve the current portal URL.
        PortalRequestContext portalEnv = PortalRequestContext.getContext(
                (HttpServletRequest) jahiaContextRequest);
        PortalURL portalURL = portalEnv.getRequestedPortalURL();

        // Create the portlet window to render.
        PortletWindow window = new PortletWindowImpl(windowConfig, portalURL);

        return window;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public List getAppEntryPointDefinitions(ApplicationBean appBean) throws JahiaException {
        List result = new ArrayList();
        Iterator portletContextIterator = getPortletContainer().getOptionalContainerServices().getPortletRegistryService().getRegisteredPortletApplications();
        while (portletContextIterator.hasNext()) {
            PortletContextImpl portletContext = (PortletContextImpl) portletContextIterator.next();
            if (portletContext.getApplicationId().equals(appBean.getContext())) {
                List<PortletDD> portletList = portletContext.getPortletApplicationDefinition().getPortlets();
                for (PortletDD portlet: portletList) {
                    PortletEntryPointDefinition portletEntryPointDefinition = new PortletEntryPointDefinition(appBean.getID(), appBean.getContext(), portlet);
                    result.add(portletEntryPointDefinition);
                }
            }
        }
        return result;
    }

    public void start() throws JahiaInitializationException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    private PortletContainer getPortletContainer() {
        // Retrieve the portlet container from servlet context.
        portletContainer = (PortletContainer)
                servletContext.getAttribute(AttributeKeys.PORTLET_CONTAINER);
        return portletContainer;
    }
}
