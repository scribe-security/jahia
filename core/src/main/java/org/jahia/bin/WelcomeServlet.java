/**
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *     Copyright (C) 2002-2014 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     "This program is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation; either version 2
 *     of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 *     As a special exception to the terms and conditions of version 2.0 of
 *     the GPL (or any later version), you may redistribute this Program in connection
 *     with Free/Libre and Open Source Software ("FLOSS") applications as described
 *     in Jahia's FLOSS exception. You should have received a copy of the text
 *     describing the FLOSS exception, also available here:
 *     http://www.jahia.com/license"
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 *
 *
 * ==========================================================================================
 * =                                   ABOUT JAHIA                                          =
 * ==========================================================================================
 *
 *     Rooted in Open Source CMS, Jahia’s Digital Industrialization paradigm is about
 *     streamlining Enterprise digital projects across channels to truly control
 *     time-to-market and TCO, project after project.
 *     Putting an end to “the Tunnel effect”, the Jahia Studio enables IT and
 *     marketing teams to collaboratively and iteratively build cutting-edge
 *     online business solutions.
 *     These, in turn, are securely and easily deployed as modules and apps,
 *     reusable across any digital projects, thanks to the Jahia Private App Store Software.
 *     Each solution provided by Jahia stems from this overarching vision:
 *     Digital Factory, Workspace Factory, Portal Factory and eCommerce Factory.
 *     Founded in 2002 and headquartered in Geneva, Switzerland,
 *     Jahia Solutions Group has its North American headquarters in Washington DC,
 *     with offices in Chicago, Toronto and throughout Europe.
 *     Jahia counts hundreds of global brands and governmental organizations
 *     among its loyal customers, in more than 20 countries across the globe.
 *
 *     For more information, please visit http://www.jahia.com
 */
package org.jahia.bin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.AccessDeniedException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.iterators.EnumerationIterator;
import org.apache.commons.lang.StringUtils;
import org.jahia.api.Constants;
import org.jahia.bin.errors.DefaultErrorHandler;
import org.jahia.bin.errors.ErrorHandler;
import org.jahia.exceptions.JahiaException;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.SpringContextSingleton;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.preferences.user.UserPreferencesHelper;
import org.jahia.services.sites.JahiaSite;
import org.jahia.services.sites.JahiaSitesService;
import org.jahia.services.uicomponents.bean.editmode.EditConfiguration;
import org.jahia.services.usermanager.JahiaGroupManagerService;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.jahia.settings.SettingsBean;
import org.jahia.utils.Url;
import org.jahia.utils.WebUtils;
import org.slf4j.Logger;

/**
 * Servlet for the first entry point in Jahia portal that performs a client-side redirect
 * to the home page of the appropriate site.
 * User: toto
 * Date: Apr 26, 2010
 * Time: 5:49:14 PM
 */
public class WelcomeServlet extends HttpServlet {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = -2055161334153523152L;
    
    private static final transient Logger logger = org.slf4j.LoggerFactory.getLogger(WelcomeServlet.class);
    
    private static final String DEFAULT_LOCALE = Locale.ENGLISH.toString();
    private static final String DASHBOARD_HOME = ".projects.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getRequestURI().endsWith("/start")) {
                userRedirect(request, response, getServletContext());
            } else {
                defaultRedirect(request, response, getServletContext());
            }
        } catch (Exception e) {
            List<ErrorHandler> handlers = ServicesRegistry.getInstance()
                    .getJahiaTemplateManagerService().getErrorHandler();
            for (ErrorHandler handler : handlers) {
                if (handler.handle(e, request, response)) {
                    return;
                }
            }
            DefaultErrorHandler.getInstance().handle(e, request, response);
        }
    }

    protected void userRedirect(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws Exception {
        JahiaUser user = (JahiaUser) request.getSession().getAttribute(Constants.SESSION_USER);
        JCRUserNode userNode = user != null ? JahiaUserManagerService.getInstance().lookupUserByPath(user.getLocalPath()) : null;
        if (!JahiaUserManagerService.isGuest(user) && userNode.isMemberOfGroup(null, JahiaGroupManagerService.PRIVILEGED_GROUPNAME)) {
            JCRSiteNode site = resolveSite(request, Constants.LIVE_WORKSPACE,
                    JCRContentUtils.getSystemSitePath());
            String language = resolveLanguage(request, site, userNode);
            redirect(request.getContextPath() + "/cms/dashboard/default/"+ language + user.getLocalPath() +
                     DASHBOARD_HOME, response);
        } else {
            throw new AccessDeniedException();
        }
    }

    protected void redirect(String url, HttpServletResponse response) throws IOException {
        String targetUrl = response.encodeRedirectURL(url);
        String jsessionIdParameterName = SettingsBean.getInstance().getJsessionIdParameterName();
        if (targetUrl.contains(";" + jsessionIdParameterName)) {
            if (targetUrl.contains("?")) {
                targetUrl = StringUtils.substringBefore(targetUrl, ";" + jsessionIdParameterName + "=") + "?"
                        + StringUtils.substringAfter(targetUrl, "?");
            } else {
                targetUrl = StringUtils.substringBefore(targetUrl, ";" + jsessionIdParameterName + "=");
            }
        }
        WebUtils.setNoCacheHeaders(response);
        response.sendRedirect(targetUrl);
    }

    protected void defaultRedirect(HttpServletRequest request, HttpServletResponse response,
            ServletContext context) throws Exception {
        request.getSession(true);
        JahiaSite defaultSite = JahiaSitesService.getInstance().getDefaultSite();
        String defaultSitePath = defaultSite != null ? defaultSite.getJCRLocalPath() : null;
        final JCRSiteNode site = resolveSite(request, Constants.LIVE_WORKSPACE, defaultSitePath);
        JahiaUser user = (JahiaUser) request.getSession().getAttribute(Constants.SESSION_USER);
        JCRUserNode userNode = user != null ? JahiaUserManagerService.getInstance().lookupUserByPath(user.getLocalPath()) : null;
        String redirect = null;
        String pathInfo = request.getPathInfo();
        String language = resolveLanguage(request, site, userNode);

        String defaultLocation = null;
        String mapping = null;

        if (pathInfo != null && (pathInfo.endsWith("mode") || pathInfo.endsWith("mode/"))) {
            String mode = pathInfo.endsWith("/") ? StringUtils.substringBetween(pathInfo, "/", "/") : StringUtils.substringAfter(pathInfo, "/");
            if (SpringContextSingleton.getInstance().getContext().containsBean(mode)) {
                EditConfiguration editConfiguration =  (EditConfiguration) SpringContextSingleton.getInstance().getContext().getBean(mode);
                defaultLocation = editConfiguration.getDefaultLocation();
                mapping = editConfiguration.getDefaultUrlMapping();
            }
        }

        if (site == null && (defaultLocation == null || defaultLocation.contains("$defaultSiteHome"))) {
            userRedirect(request, response, context);
        } else {
            if (defaultLocation != null) {
                if (site != null && defaultLocation.contains("$defaultSiteHome")) {
                    JCRNodeWrapper home = site.getHome();
                    if (home == null) {
                        home = resolveSite(request, Constants.EDIT_WORKSPACE, defaultSitePath).getHome();
                    }
                    defaultLocation = defaultLocation.replace("$defaultSiteHome",home.getPath());
                }

                redirect = request.getContextPath() + mapping + "/" + language +defaultLocation;
            } else {
                JCRNodeWrapper home = site.getHome();
                if (home != null) {
                    redirect = request.getContextPath() + "/cms/render/"
                            + Constants.LIVE_WORKSPACE + "/" + language + home.getPath() + ".html";
                } else if (!SettingsBean.getInstance().isDistantPublicationServerMode()) {
                    JCRSiteNode defSite = null;
                    try {
                        defSite = (JCRSiteNode) JCRStoreService.getInstance().getSessionFactory()
                                .getCurrentUserSession().getNode(site.getPath());
                    } catch (PathNotFoundException e) {
                        if (!Url.isLocalhost(request.getServerName())
                                && defaultSite != null
                                && !site.getSiteKey().equals(
                                        defaultSite.getSiteKey())
                                && (!SettingsBean.getInstance()  // the check in this parenthesis is added to prevent immediate servername change in the url, which leads to the side effect with an automatic login on default site after logout on other site 
                                        .isUrlRewriteUseAbsoluteUrls()
                                        || site.getServerName().equals(
                                                defaultSite.getServerName()) || Url
                                            .isLocalhost(defaultSite
                                                    .getServerName()))) {
                            JCRSiteNode defaultSiteNode = (JCRSiteNode) JCRStoreService
                                    .getInstance()
                                    .getSessionFactory()
                                    .getCurrentUserSession(
                                            Constants.LIVE_WORKSPACE)
                                    .getNode(defaultSitePath);
                            if (defaultSiteNode.getHome() != null) {
                                redirect = request.getContextPath()
                                        + "/cms/render/"
                                        + Constants.LIVE_WORKSPACE + "/"
                                        + language
                                        + defaultSiteNode.getHome().getPath() + ".html";
                            }
                        }
                    }
                    if (redirect == null && defSite != null && defSite.getHome() != null) {
                        if (defSite.getHome().hasPermission("editModeAccess")) {
                            redirect = request.getContextPath() + "/cms/edit/"
                                    + Constants.EDIT_WORKSPACE + "/" + language
                                    + defSite.getHome().getPath() + ".html";
                        } else if (defSite.getHome().hasPermission("contributeModeAccess")) {
                            redirect = request.getContextPath() + "/cms/contribute/"
                                    + Constants.EDIT_WORKSPACE + "/" + language
                                    + defSite.getHome().getPath() + ".html";
                        } 
                    } 
                } 
            }
            if (redirect == null) {
                redirect(request.getContextPath() + "/start", response);
                return;
            }
            redirect(redirect, response);
        }
    }

    protected JCRSiteNode resolveSite(HttpServletRequest request, String workspace, String fallbackSitePath) throws JahiaException, RepositoryException {
        JahiaSitesService siteService = JahiaSitesService.getInstance();
        JahiaSite resolvedSite = !Url.isLocalhost(request.getServerName()) ? siteService.getSiteByServerName(request.getServerName()) : null;
        String sitePath = resolvedSite == null ? fallbackSitePath : resolvedSite.getJCRLocalPath(); 

        return sitePath != null ? (JCRSiteNode) JCRStoreService.getInstance().getSessionFactory()
                .getCurrentUserSession(workspace).getNode(sitePath) : null;
    }
    
    protected String resolveLanguage(HttpServletRequest request, final JCRSiteNode site, JCRUserNode user)
            throws JahiaException {
        final List<Locale> newLocaleList = new ArrayList<Locale>();
        List<Locale> siteLanguages = Collections.emptyList();
        try {
            if (site != null) {
                siteLanguages = site.getLanguagesAsLocales();
            }
        } catch (Exception t) {
            logger.debug("Exception while getting language settings as locales", t);
        }

        Locale preferredLocale = UserPreferencesHelper.getPreferredLocale(user);
        if (preferredLocale != null) {
            addLocale(site, newLocaleList, preferredLocale);
        }

        // retrieve the browser locales
        for (@SuppressWarnings("unchecked")
        Iterator<Locale> browserLocales = new EnumerationIterator(request.getLocales()); browserLocales
                .hasNext();) {
            final Locale curLocale = browserLocales.next();
            if (siteLanguages.contains(curLocale)) {
                addLocale(site, newLocaleList, curLocale);
            } else if (!StringUtils.isEmpty(curLocale.getCountry())) {
                final Locale langOnlyLocale = new Locale(curLocale.getLanguage());
                if (siteLanguages.contains(langOnlyLocale)) {
                    addLocale(site, newLocaleList, langOnlyLocale);
                }
            }
        }

        String language = DEFAULT_LOCALE;
        if (!newLocaleList.isEmpty()) {
            language = newLocaleList.get(0).toString();
        } else if (site!=null){
            language = site.getDefaultLanguage();
        } else if (!StringUtils.isEmpty(SettingsBean.getInstance().getDefaultLanguageCode())) {
            language = SettingsBean.getInstance().getDefaultLanguageCode();
        }
        return language;
    }

    private void addLocale(final JCRSiteNode site, final List<Locale> newLocaleList, final Locale curLocale) {
        try {
            JCRTemplate.getInstance().doExecuteWithSystemSession(null,
                    Constants.LIVE_WORKSPACE,curLocale,new JCRCallback<Object>() {
                public Object doInJCR(JCRSessionWrapper session) throws RepositoryException {
                    try {
                        if(site!=null) {
                            JCRSiteNode nodeByIdentifier = (JCRSiteNode) session.getNodeByIdentifier(site.getIdentifier());
                            JCRNodeWrapper home = nodeByIdentifier.getHome();
                            if (home!=null && !newLocaleList.contains(curLocale)) {
                                newLocaleList.add(curLocale);
                            }
                        }
                    } catch (RepositoryException e) {
                        logger.debug("This site does not have a published home in language "+curLocale,e);
                    }
                    return null;
                }
            });
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
        }
    }
}