/**
 * This file is part of Jahia, next-generation open source CMS:
 * Jahia's next-generation, open source CMS stems from a widely acknowledged vision
 * of enterprise application convergence - web, search, document, social and portal -
 * unified by the simplicity of web content management.
 *
 * For more information, please visit http://www.jahia.com.
 *
 * Copyright (C) 2002-2012 Jahia Solutions Group SA. All rights reserved.
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
 * Commercial and Supported Versions of the program (dual licensing):
 * alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms and conditions contained in a separate
 * written agreement between you and Jahia Solutions Group SA.
 *
 * If you are unsure which license is appropriate for your use,
 * please contact the sales department at sales@jahia.com.
 */

package org.jahia.ajax.gwt.helper;

import org.apache.commons.collections.functors.NOPTransformer;
import org.apache.commons.collections.map.LazySortedMap;
import org.apache.commons.collections.map.TransformedSortedMap;
import org.jahia.ajax.gwt.client.data.GWTRenderResult;
import org.jahia.ajax.gwt.client.service.GWTJahiaServiceException;
import org.jahia.bin.Render;
import org.jahia.services.SpringContextSingleton;
import org.jahia.services.channels.Channel;
import org.jahia.services.channels.ChannelService;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRSiteNode;
import org.jahia.services.content.nodetypes.ConstraintsHelper;
import org.jahia.services.content.nodetypes.ExtendedNodeType;
import org.jahia.services.render.*;
import org.jahia.services.uicomponents.bean.editmode.EditConfiguration;
import org.jahia.utils.i18n.JahiaResourceBundle;

import javax.jcr.AccessDeniedException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;import java.util.*;

/**
 * User: toto
 * Date: Sep 28, 2009
 * Time: 2:54:41 PM
 */
public class TemplateHelper {
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TemplateHelper.class);

    private RenderService renderService;
    private ChannelService channelService;
    public static final int LIVE = 0;
    public static final int PREVIEW = 1;
    public static final int EDIT = 2;

    public void setRenderService(RenderService renderService) {
        this.renderService = renderService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    /**
     * Get rendered content
     *
     * @param path
     * @param template
     * @param configuration
     * @param contextParams
     * @param editMode
     * @param configName
     * @param request
     * @param response
     * @param currentUserSession
     * @param uiLocale
     * @param channelIdentifier
     * @param channelVariant
     */
    public GWTRenderResult getRenderedContent(String path, String template, String configuration,
                                              final Map<String, List<String>> contextParams, boolean editMode, String configName,
                                              HttpServletRequest request, HttpServletResponse response,
                                              JCRSessionWrapper currentUserSession,
                                              Locale uiLocale, String channelIdentifier, String channelVariant) throws GWTJahiaServiceException {
        GWTRenderResult result = null;
        try {
            JCRNodeWrapper node = currentUserSession.getNode(path);

            Resource r = new Resource(node, "html", template, configuration);
            request.setAttribute("mode", "edit");

            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getParameter(String name) {
                    if (contextParams != null && contextParams.containsKey(name)) {
                        return contextParams.get(name).get(0);
                    }
                    return super.getParameter(name);
                }

                @Override
                public Map getParameterMap() {
                    Map r = new HashMap(super.getParameterMap());
                    if (contextParams != null) {
                        for (Map.Entry<String, List<String>> entry : contextParams.entrySet()) {
                            r.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
                        }
                    }
                    return r;
                }

                @Override
                public Enumeration getParameterNames() {
                    return new Vector(getParameterMap().keySet()).elements();
                }

                @Override
                public String[] getParameterValues(String name) {
                    if (contextParams != null && contextParams.containsKey(name)) {
                        List<String> list = contextParams.get(name);
                        return list.toArray(new String[list.size()]);
                    }
                    return super.getParameterValues(name);
                }
            };

            RenderContext renderContext = new RenderContext(request, response, currentUserSession.getUser());
            renderContext.setEditMode(editMode);
            renderContext.setEditModeConfigName(configName);
            renderContext.setMainResource(r);

            EditConfiguration editConfiguration = null;
            if (configName != null) {
                editConfiguration= (EditConfiguration) SpringContextSingleton.getBean(configName);
            }
            String permission = null;
            if (editConfiguration != null) {
                permission = editConfiguration.getRequiredPermission();
                renderContext.setServletPath(editConfiguration.getDefaultUrlMapping());
            } else {
                renderContext.setServletPath(Render.getRenderServletPath());
            }

            if (permission != null) {
                if (!node.hasPermission(permission)) {
                    throw new GWTJahiaServiceException(JahiaResourceBundle.getJahiaInternalResource("label.gwt.error.access.denied",uiLocale));
                }
            }

            if (contextParams != null) {
                for (Map.Entry<String, List<String>> entry : contextParams.entrySet()) {
                    r.getModuleParams().put(entry.getKey(), entry.getValue().get(0));
                }
            }

            JCRSiteNode site = node.getResolveSite();
            renderContext.setSite(site);
            if (channelIdentifier != null) {
                Channel activeChannel = channelService.getChannel(channelIdentifier);
                if (activeChannel != null) {
                    renderContext.setChannel(activeChannel);
                }
            }

            String res = renderService.render(r, renderContext);
            Map<String, Map<String,Map<String,String>>> map = (Map<String, Map<String,Map<String,String>>>) renderContext.getRequest().getAttribute("staticAssets");

            if (channelIdentifier != null && !channelIdentifier.equals("generic")) {
                Map<String,Map<String,String>> css  = map.get("css");
                SortedMap<String,Map<String,String>> cssWithParam  = new TreeMap<String, Map<String, String>>();
                for (Map.Entry<String, Map<String, String>> entry : css.entrySet()) {
                    String k = entry.getKey() + "?channel="+channelIdentifier+(channelVariant!=null?"&variant="+channelVariant:"");
                    cssWithParam.put(k, entry.getValue());
                }
                map.put("css",cssWithParam);
            }


            String constraints = ConstraintsHelper.getConstraints(node);
            if (constraints == null) {
                constraints = "";
            }
            Map<String, List<String>> m = new HashMap<String, List<String>>();
            if (map != null) {
                  for (Map.Entry<String, Map<String,Map<String,String>>> entry : map.entrySet()) {
                    m.put(entry.getKey(), new ArrayList<String>(entry.getValue().keySet()));
                }
            }
            result = new GWTRenderResult(res, m, constraints, node.getDisplayableName());
        } catch (PathNotFoundException e) {
            throw new GWTJahiaServiceException(MessageFormat.format(JahiaResourceBundle.getJahiaInternalResource("label.gwt.error.not.found.for.user",uiLocale), path, currentUserSession.getUser().getName()));
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new GWTJahiaServiceException(MessageFormat.format(JahiaResourceBundle.getJahiaInternalResource("label.gwt.error.repository.exception.on.path",uiLocale), path));
        } catch (RenderException e) {
            if(e.getCause() instanceof AccessDeniedException ) {
                throw new GWTJahiaServiceException(JahiaResourceBundle.getJahiaInternalResource("label.gwt.error.access.denied",uiLocale));
            } else {
                logger.error(e.getMessage(), e);
                throw new GWTJahiaServiceException(MessageFormat.format(JahiaResourceBundle.getJahiaInternalResource("label.gwt.error.render.exception",uiLocale), e.getMessage()));
            }
        }
        return result;
    }

}
