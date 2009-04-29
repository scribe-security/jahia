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
package org.jahia.taglibs;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;
import org.jahia.ajax.gwt.client.messages.Messages;
import org.jahia.data.JahiaData;
import org.jahia.data.beans.JahiaBean;
import org.jahia.data.beans.TemplatePathResolverBean;
import org.jahia.exceptions.JahiaException;
import org.jahia.params.ProcessingContext;
import org.jahia.utils.i18n.ResourceBundleMarker;
import org.jahia.taglibs.utility.Utils;
import org.jahia.utils.i18n.JahiaResourceBundle;
import org.jahia.registries.ServicesRegistry;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.*;

/**
 * This abstract Tag is the starting point for implementing any knew tags. In contains common attributes that should be
 * used in the implementation of the derived tags. For instance, the 'xhtmlCompliantHtml' is used to know if the tag
 * should render XHTML compliant html or simple basic html.</br>
 * The same is true regarding the 'resourceBundle' attribute. Instead of having to set the name of the resource bundle
 * file for all Jahia tags, it is much more convenient to set it once, at the beginning of the template, and then simply
 * fetching this set values.
 *
 * @author Xavier Lawrence
 */
@SuppressWarnings("serial")
public class AbstractJahiaTag extends BodyTagSupport {

    public static final String PARENT_TAG_REQUEST_ATTRIBUTE = "parentContainerTag";
    public static final String PARENT_BUNDLE_REQUEST_ATTRIBUTE = "parentBundleTag";
    public static final String TEMPLATE_PATH = "/templates/";
    public static final String COMMON_TAG_BUNDLE1 = "CommonTag";

    private static final transient Logger logger = Logger.getLogger(AbstractJahiaTag.class);

    /**
     * Name of the resourceBundle all tags derived from this class will use.
     */
    private String resourceBundle;


    /**
     * If set to 'true' the output generated by the tag will be XHTML compliant, otherwise it will be
     * HTML compliant
     */
    protected boolean xhtmlCompliantHtml;

    /**
     * The languageCode attribute keeps track of the current language
     */
    protected String languageCode;

    /**
     * The CSS class the surrounding div or span element will have
     */
    protected String cssClassName;

    public String getResourceBundle() {
        if (resourceBundle == null || "".equals(resourceBundle)) {
            try {
                resourceBundle = ServicesRegistry.getInstance()
                        .getJahiaTemplateManagerService().getTemplatePackage(
                                getProcessingContext().getSite()
                                        .getTemplatePackageName())
                        .getResourceBundleName();
            } catch (Exception e) {
                logger.warn(
                        "Unable to retrieve resource bundle name for current template set. Cause: "
                                + e.getMessage(), e);
            }
        }
        return resourceBundle;
    }

    public void setResourceBundle(String resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public boolean isXhtmlCompliantHtml() {
        return xhtmlCompliantHtml;
    }

    public void setXhtmlCompliantHtml(boolean xhtmlCompliantHtml) {
        this.xhtmlCompliantHtml = xhtmlCompliantHtml;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCssClassName() {
        return cssClassName;
    }

    public void setCssClassName(String cssClassName) {
        this.cssClassName = cssClassName;
    }

    protected final Tag findAncestorWithClass(final Tag tag,
                                              final Class<?> aClass,
                                              final ServletRequest request) {
        if (tag == null || aClass == null ||
                (!Tag.class.isAssignableFrom(aClass) && !(aClass.isInterface()))) {
            return null;
        }

        Tag result = TagSupport.findAncestorWithClass(this, aClass);
        if (result == null) {
            Stack<Tag> stack = (Stack<Tag>) pageContext.getRequest().getAttribute(PARENT_TAG_REQUEST_ATTRIBUTE);
            if (stack != null) {
                Tag[] tags = stack.toArray(new Tag[stack.size()]);
                int pos = stack.indexOf(this);
                for (int i = pos; i >= 0; i--) {
                    Tag ancestor = tags[i];
                    if (aClass.isInstance(ancestor) && ancestor != this) {
                        return ancestor;
                    }
                }
            }
        }
        return result;
    }

    public void pushTag() {
        Stack<Tag> stack = (Stack<Tag>) pageContext.getRequest().getAttribute(PARENT_TAG_REQUEST_ATTRIBUTE);
        if (stack == null) {
            stack = new Stack<Tag>();
            pageContext.getRequest().setAttribute(PARENT_TAG_REQUEST_ATTRIBUTE, stack);
        }
        stack.push(this);
    }

    public void popTag() {
        Stack<Tag> stack = (Stack<Tag>) pageContext.getRequest().getAttribute(PARENT_TAG_REQUEST_ATTRIBUTE);
        stack.pop();
    }

    public static String resolveIncludeFullPath(final PageContext pageContext,
                                                final String fileName) {
        final TemplatePathResolverBean templatePath = Utils.getJahiaBean(pageContext).getIncludes().getTemplatePath();
        return templatePath.lookup(fileName);
    }

    protected String getMessage(final String key, final String defaultValue) {
        String message = defaultValue;
        if (key != null) {
            try {
                message = retrieveResourceBundle().getString(key);
            } catch (MissingResourceException e) {
                // use default value
            }
        }
        return message;
    }

    protected String getMessage(final String key) {
        return getMessage(key, "???" + key + "???");
    }

    /**
     * Retrieve the parent resource bundle if any and if the current one is null.
     * This has to be called in subtags of TemplateTag (any tag within a template should do actually).
     */
    protected ResourceBundle retrieveResourceBundle() {
        ResourceBundle bundle = null;
        final LocalizationContext localizationCtx = BundleSupport.getLocalizationContext(pageContext);
        if (localizationCtx!=null) {
            bundle = localizationCtx.getResourceBundle();
        }
        if (bundle == null) {
            bundle = new JahiaResourceBundle(resourceBundle,
                    getProcessingContext().getLocale(), getProcessingContext()
                            .getSite().getTemplatePackageName()); 
        }
        return bundle;
    }

    /**
     * Returns an {@link JahiaBean} instance with current Jahia data.
     *
     * @return an {@link JahiaBean} instance with current Jahia data
     */
    protected JahiaBean getJahiaBean() {
        return getJahiaBean(false);
    }

    /**
     * Returns an {@link JahiaBean} instance with current Jahia data.
     *
     * @param createIfNotFound will create the bean if it is not found
     * @return an {@link JahiaBean} instance with current Jahia data
     */
    protected JahiaBean getJahiaBean(boolean createIfNotFound) {
        return Utils.getJahiaBean(pageContext, createIfNotFound);
    }

    /**
     * Returns current {@link ProcessingContext} instance.
     *
     * @return current {@link ProcessingContext} instance
     */
    protected ProcessingContext getProcessingContext() {
        return Utils.getProcessingContext(pageContext);
    }

    /**
     * Returns current {@link JahiaData} instance.
     *
     * @return current {@link JahiaData} instance
     */
    protected JahiaData getJahiaData() {
        return (JahiaData) pageContext.getAttribute("org.jahia.data.JahiaData",
                PageContext.REQUEST_SCOPE);
    }

    /**
     * @return jahia_gwt_dictionary as map
     */
    protected Map<String, String> getJahiaGwtDictionary() {
        Map<String, String> dictionaryMap = (Map<String, String>) pageContext.getAttribute("org.jahia.ajax.gwt.dictionary", PageContext.REQUEST_SCOPE);
        if (dictionaryMap == null) {
            dictionaryMap = new HashMap<String, String>();
            updateJahiaGwtDictionary(dictionaryMap);
        }
        return dictionaryMap;
    }

    /**
     * Update jahia_gwt_dictionary
     *
     * @param dictionaryMap
     */
    protected void updateJahiaGwtDictionary(Map<String, String> dictionaryMap) {
        pageContext.setAttribute("org.jahia.ajax.gwt.dictionary", dictionaryMap, PageContext.REQUEST_SCOPE);
    }

    /**
     * Generate jahia_gwt_dictionary
     *
     * @return
     */
    protected String generateJahiaGwtDictionary() {
        Map<String, String> dictionaryMap = getJahiaGwtDictionary();
        StringBuffer s = new StringBuffer();
        s.append("var " + Messages.DICTIONARY_NAME + " = {\n");
        if (dictionaryMap != null) {
            Iterator<String> keys = dictionaryMap.keySet().iterator();
            while (keys.hasNext()) {
                String name = keys.next().toString();
                Object value = dictionaryMap.get(name);
                if (value != null) {
                    s.append("\"").append(name).append("\"").append(":\"").append(value.toString()).append("\"");
                    if (keys.hasNext()) {
                        s.append(",");
                    }
                    s.append("\n");
                }
            }
        }

        s.append("};\n");
        return s.toString();
    }

    /**
     * Add a message into jahia_gwt_dictionary
     *
     * @param aliasName
     * @param message
     */
    protected void addGwtDictionaryMessage(String aliasName, String message) {
        if (aliasName != null) {
            Map<String, String> dictionaryMap = getJahiaGwtDictionary();
            dictionaryMap.put(aliasName, message);
            updateJahiaGwtDictionary(dictionaryMap);
        }
    }

    protected String resolveIncludeFullPath(final String fileName) {
        return resolveIncludeFullPath(pageContext, fileName);
    }

    protected String extractDefaultValue(final String value) {
        if (value.startsWith("<jahia-resource")) {
            final int keyIndex = value.indexOf("default-value=");
            final String tmp = value.substring(keyIndex + 15);
            int whiteSpaceIndex = tmp.indexOf("\"");
            if (whiteSpaceIndex < 0) {
                whiteSpaceIndex = tmp.indexOf("'");
            }
            return tmp.substring(0, whiteSpaceIndex);

        } else {
            return value;
        }
    }

    protected String getValueFromResourceBundleMarker(final String value) {
        final ResourceBundleMarker marker = ResourceBundleMarker.parseMarkerValue(value);
        if (marker == null) return value;
        try {
            return marker.getValue(getProcessingContext().getLocale());
        } catch (JahiaException je) {
            return marker.getDefaultValue();
        }
    }

    /**
     * Simply utility method in order to extract the key value from a String
     * made of a resource bundle marker.<br/> <p/> i.e: <br/> Extracts the value
     * 'files' from the String below: <br/> <jahia-resource id="DEFAULT_V3_LANG"
     * key="files" default-value="files"/>
     */
    protected String extractKey(final String value) {
        if (value != null && value.startsWith("<jahia-resource")) {
            final int keyIndex = value.indexOf("key=");
            final String tmp = value.substring(keyIndex + 5);
            int whiteSpaceIndex = tmp.indexOf("\"");
            if (whiteSpaceIndex < 0) {
                whiteSpaceIndex = tmp.indexOf("'");
            }
            return tmp.substring(0, whiteSpaceIndex);

        } else {
            return value;
        }
    }

    protected void resetState() {
        cssClassName = null;
        languageCode = null;
        resourceBundle = null;
        xhtmlCompliantHtml = false;
    }

    protected boolean isLogged() {
        final JahiaBean jBean = (JahiaBean) pageContext.getAttribute("jahia", PageContext.REQUEST_SCOPE);
        return jBean.getRequestInfo().isLogged();
    }

    @Override
    public void release() {
        resetState();
        super.release();
    }

}
