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
package org.jahia.taglibs.query;

import org.apache.jackrabbit.spi.commons.query.jsr283.qom.Constraint;
import org.apache.jackrabbit.spi.commons.query.jsr283.qom.DescendantNode;
import javax.servlet.jsp.JspException;

/**
 * Tag used to create a DescendantNode Constraint
 *
 * User: hollis
 * Date: 7 nov. 2007
 * Time: 15:33:24
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class DescendantNodeTag extends ConstraintTag  {

    private DescendantNode descendantNode;
    private String selectorName;
    private String path;

    public DescendantNodeTag(){
    }

    public int doEndTag() throws JspException {
        int eval = super.doEndTag();
        descendantNode = null;
        selectorName = null;
        path = null;
        return eval;
    }

    public String getSelectorName() {
        return selectorName;
    }

    public void setSelectorName(String selectorName) {
        this.selectorName = selectorName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public DescendantNode getDescendantNode() {
        return descendantNode;
    }

    public void setDescendantNode(DescendantNode descendantNode) {
        this.descendantNode = descendantNode;
    }

    public Constraint getConstraint() throws Exception {
        if ( descendantNode != null ){
            return descendantNode;
        }
        if ("".equals(path.trim())){
            return null;
        }
        if (selectorName==null || "".equals(selectorName.trim())){
            descendantNode = this.getQueryFactory().descendantNode(path);
        } else {
            descendantNode = this.getQueryFactory().descendantNode(selectorName.trim(),path);
        }
        return descendantNode;
    }

}