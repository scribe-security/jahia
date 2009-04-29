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
package org.jahia.taglibs.uicomponents.loginform;

import org.apache.log4j.Logger;
import org.jahia.data.JahiaData;
import org.jahia.taglibs.AbstractJahiaTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Class LogUserName : return the username field for the login Area
 *
 * @author Werner Assek
 * @jsp:tag name="logUserName" body-content="empty"
 * description="Display a username field for the login area."
 * <p/>
 * <p><b>Example :</b>
 * <p> &lt;content:logUserName
 * &nbsp;&nbsp; labelBundle=\"jahiatemplates.JahiaLabels\" <br>
 * &nbsp;&nbsp; labelKey=\"login.username\" <br>
 * &nbsp;&nbsp; styleClass=\"cssClass\" /&gt; <br>
 * <br>
 */
@SuppressWarnings("serial")
public class LogUserNameInputTag extends AbstractJahiaTag {

    private static final transient Logger logger = Logger.getLogger(LogUserNameInputTag.class);

    private String labelKey;
    public int tabIndex = 1;
    private boolean displayLabel = true;
    private String labelCssClassName;
    private int size = 8;

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void setDisplayLabel(boolean displayLabel) {
        this.displayLabel = displayLabel;
    }

    public void setLabelCssClassName(String labelCssClassName) {
        this.labelCssClassName = labelCssClassName;
    }

    public int doEndTag() throws JspException {
        final LogAreaTag logAreaTag = (LogAreaTag) findAncestorWithClass(this, LogAreaTag.class);
        if (logAreaTag == null) {
            logger.error("login-username tag must be used as a child of tag login-area");
            tabIndex = 1;
            displayLabel = true;
            labelCssClassName = null;
            labelKey = null;
            size = 8;
            return EVAL_PAGE;
        }
        final JspWriter out = pageContext.getOut();
        final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        final JahiaData jData = (JahiaData) request.getAttribute("org.jahia.data.JahiaData");
        if (!jData.gui().isLogged()) {
            try {
                final String label = getMessage(labelKey, "Username");

                String css = "";
                if (cssClassName != null && !cssClassName.equals("")) {
                    css = " class=\"" + cssClassName + "\"";
                }
                final StringBuffer buff = new StringBuffer();

                if (displayLabel) {
                    buff.append("<label for=\"username\" class=\"").append(labelCssClassName).append("\">").
                            append(label).append(":</label>\n");
                }
                buff.append("<input type=\"text\" name=\"username\" tabindex=\"");
                buff.append(tabIndex);
                buff.append("\" value=\"username\" size=\"");
                buff.append(size);
                buff.append("\" onfocus=\"this.value=''\" ");
                buff.append(css);
                buff.append(" />\n");

                out.print(buff.toString());

            } catch (IOException ioe) {
                logger.error("IO exception while trying to display login content login", ioe);
            }
        }
        tabIndex = 1;
        displayLabel = true;
        labelCssClassName = null;
        labelKey = null;
        size = 8;
        return EVAL_PAGE;
    }
}
