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
// $Id$
//
//  ManageServer
//
//  31.03.2001  AK  added in jahia.
//

package org.jahia.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;
import org.jahia.bin.Jahia;
import org.jahia.bin.JahiaAdministration;
import org.jahia.data.JahiaData;
import org.jahia.exceptions.JahiaException;
import org.jahia.params.ProcessingContext;
import org.jahia.registries.ServicesRegistry;
import org.jahia.utils.i18n.JahiaResourceBundle;
import org.jahia.services.mail.MailService;
import org.jahia.services.mail.MailServiceImpl;
import org.jahia.services.mail.MailSettings;
import org.jahia.services.mail.MailSettingsValidationResult;
import org.jahia.utils.properties.PropertiesManager;
import org.jahia.admin.AbstractAdministrationModule;


/**
 * desc:  This class is used by the administration to manage the
 * server settings of a jahia portal, like the mail notification service (when
 * jahia or a user generate error(s), or like the java server home disk path,
 * mail server, etc.
 *
 * Copyright:    Copyright (c) 2002
 * Company:      Jahia Ltd
 *
 * @author Alexandre Kraft
 * @version 1.0
 */
public class ManageServer extends AbstractAdministrationModule {

    private static final String CLASS_NAME  =  JahiaAdministration.CLASS_NAME;
    private static final String JSP_PATH    =  JahiaAdministration.JSP_PATH;

    private static final transient Logger logger = Logger.getLogger(ManageServer.class);

    /**
     * This method is used like a dispatcher for user requests.
     * @author  Alexandre Kraft
     *
     * @param   request       Servlet request.
     * @param   response      Servlet response.
     */
    public void service( HttpServletRequest    request,
                                        HttpServletResponse   response )
    throws Exception
    {
        String operation =  request.getParameter("sub");

        if(operation.equals("display")) {
            displaySettings( request, response, request.getSession() );
        } else if(operation.equals("process")) {
            processSettings( request, response, request.getSession() );
        }
    } // userRequestDispatcher



    /**
     * Display the server settings page, using doRedirect().
     * @author  Alexandre Kraft
     *
     * @param   request       Servlet request.
     * @param   response      Servlet response.
     * @param   session       Servlet session for the current user.
     */
    private void displaySettings( HttpServletRequest    request,
                                  HttpServletResponse   response,
                                  HttpSession           session )
    throws IOException, ServletException
    {
        // retrieve previous form values...
        MailSettings cfg = "process".equals(request.getParameter("sub")) ? (MailSettings) session
                .getAttribute(CLASS_NAME + "jahiaMailSettings")
                : null;
        if (cfg == null) {
            cfg = ServicesRegistry.getInstance().getMailService().getSettings();
            session.setAttribute(CLASS_NAME + "jahiaMailSettings", cfg);
        }

        // set request attributes...
        request.setAttribute("jahiaMailSettings", cfg);

        JahiaAdministration.doRedirect( request, response, session, JSP_PATH + "config_server.jsp" );
    } // end displaySettings



    /**
     * Process and check the validity of the server settings page. If they are
     * not valid, display the server settings page to the user.
     * @author  Alexandre Kraft
     *
     * @param   request       Servlet request.
     * @param   response      Servlet response.
     * @param   session       Servlet session for the current user.
     */
    private void processSettings( HttpServletRequest   request,
                                  HttpServletResponse  response,
                                  HttpSession          session )
    throws IOException, ServletException
    {
        // get form values...
        MailSettings cfg = new MailSettings();
        RequestUtils.populate(cfg, request);
        session.setAttribute(CLASS_NAME + "jahiaMailSettings", cfg);
        JahiaData jData = (JahiaData) request.getAttribute("org.jahia.data.JahiaData");
        ProcessingContext jParams = null;
        if (jData != null) {
            jParams = jData.getProcessingContext();
        }

        MailSettingsValidationResult result = MailServiceImpl.validateSettings(cfg, true);
        if (!result.isSuccess()) {
            session.setAttribute(CLASS_NAME + "jahiaDisplayMessage",
                    JahiaResourceBundle.getJahiaInternalResource(
                            result.getMessageKey(), jParams.getLocale()));
        } else {
            storeSettings(cfg, jParams, request);
        }
        displaySettings( request, response, session );
    } // end processSettings



    /**
     * Store new settings for the mail server.
     * 
     * @param cfg
     *            mail settings
     * @param jParams
     *            processing context
     * @param request
     *            current request
     */
    private void storeSettings(MailSettings cfg, ProcessingContext jParams,
            HttpServletRequest request) throws IOException, ServletException {
        // set new values in the properties manager...
        PropertiesManager properties = new PropertiesManager( Jahia.getJahiaPropertiesFileName() );
        properties.setProperty("mail_service_activated", cfg.isServiceActivated() ? "true" : "false");
        properties.setProperty("mail_server", cfg.getHost());
        properties.setProperty("mail_administrator", cfg.getTo());
        properties.setProperty("mail_from", cfg.getFrom());
        properties.setProperty("mail_paranoia", cfg.getNotificationLevel());

        // write in the jahia properties file...
        properties.storeProperties();
        org.jahia.settings.SettingsBean.getInstance().mail_service_activated = cfg.isServiceActivated();
        org.jahia.settings.SettingsBean.getInstance().mail_server = cfg.getHost();
        org.jahia.settings.SettingsBean.getInstance().mail_administrator = cfg.getTo();
        org.jahia.settings.SettingsBean.getInstance().mail_from = cfg.getFrom();
        org.jahia.settings.SettingsBean.getInstance().mail_paranoia = cfg.getNotificationLevel();
        
        // restart the mail service
        MailService mailSrv = ServicesRegistry.getInstance().getMailService();
        try {
            mailSrv.stop();
            mailSrv.start();
            request.setAttribute("jahiaDisplayInfo", JahiaResourceBundle
                    .getJahiaInternalResource(
                            "org.jahia.admin.warningMsg.changSaved.label",
                            jParams.getLocale()));
        } catch (JahiaException e) {
            logger
                    .error(
                            "Unable to restart Mail Service."
                                    + " New mail settings will be taken into consideration after server restart",
                            e);
            request
                    .setAttribute(
                            "jahiaDisplayMessage",
                            JahiaResourceBundle
                                    .getJahiaInternalResource(
                                            "org.jahia.admin.JahiaDisplayMessage.restartJahiaAfterChange.label",
                                            jParams.getLocale()));
        }
    }
}
