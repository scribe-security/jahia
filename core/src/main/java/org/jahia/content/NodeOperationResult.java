/**
 * 
 * This file is part of Jahia: An integrated WCM, DMS and Portal Solution
 * Copyright (C) 2002-2009 Jahia Limited. All rights reserved.
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
 * in Jahia's FLOSS exception. You should have recieved a copy of the text
 * describing the FLOSS exception, and it is also available here:
 * http://www.jahia.com/license"
 * 
 * Commercial and Supported Versions of the program
 * Alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms contained in a separate written agreement
 * between you and Jahia Limited. If you are unsure which license is appropriate
 * for your use, please contact the sales department at sales@jahia.com.
 */

package org.jahia.content;

import org.jahia.engines.EngineMessage;

import java.io.Serializable;

/**
 * <p>Title: This class contains the result of an operation on a node.</p>
 * <p>Description: This class is usually used in conjunction with the
 * TreeOperationResult. The latter contains classes of this type (or of a
 * child class), that describes a specific result for a node.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Serge Huber
 * @version 1.0
 */

public class NodeOperationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ObjectKey nodeKey;
    protected String languageCode;
    protected String comment;
    protected EngineMessage msg;
    protected int objectId;
    protected String objectType;
    public boolean blocker = false;


    /**
     * Constructor for the result.
     *
     * @param nodeKey      a valid ObjectKey instance that represents the node for
     *                     which we are creating the result.
     * @param languageCode the language for which this result is given
     * @param comment      the text describing the result, or a resource bundle key
     *                     to use for internationalization.
     *                     recognized by Jahia
     * @see ObjectKey
     */
    public NodeOperationResult(final ObjectKey nodeKey, final String languageCode, final String comment) {
        this(nodeKey, languageCode, comment, null);
    }

    public NodeOperationResult(final ObjectKey nodeKey, final String languageCode, final String comment, final EngineMessage msg) {
        this(nodeKey != null ? nodeKey.getType() : null, nodeKey != null ? nodeKey.getIdInType() : 0, languageCode, comment, msg);
        this.nodeKey = nodeKey;
    }

    public NodeOperationResult(final String objectType, final int objectId, final String languageCode, final String comment, final EngineMessage msg) {
        super();
        this.objectType = objectType;
        this.objectId = objectId;
        this.languageCode = languageCode;
        this.comment = comment;
        this.msg = msg;
        if (objectType != null) {
            try {
                this.nodeKey = ObjectKey.getInstance(objectType + ObjectKey.KEY_SEPARATOR + objectId);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
    }

    public boolean isBlocker() {
        return blocker;
    }

    public ObjectKey getNodeKey() {
        return nodeKey;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getComment() {
        return comment;
    }

    public EngineMessage getMsg() {
        return msg;
    }

    public String getObjectType() {
        return objectType;
    }

    public int getObjectID() {
        return objectId;
    }

    public void setBlocker(boolean blocker) {
        this.blocker = blocker;
    }
}