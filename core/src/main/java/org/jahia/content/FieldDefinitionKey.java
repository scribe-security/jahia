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
 package org.jahia.content;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Khue Nguyen
 * @version 1.0
 */
public class FieldDefinitionKey extends ContentDefinitionKey {

    private static final long serialVersionUID = 4684719533255642932L;
    
    public static final String FIELD_TYPE = "FieldDefinition";

    static {
        ObjectKey.registerType(FIELD_TYPE, FieldDefinitionKey.class);
    }

    /**
     * Protected constructor to use this class also as a factory by calling
     * the getChildInstance method
     */
    protected FieldDefinitionKey() {}

    public FieldDefinitionKey(int fieldDefID) {
        super(FIELD_TYPE, Integer.toString(fieldDefID));
    }

    public FieldDefinitionKey(int fieldDefID, String objectKey) {
        super(FIELD_TYPE, Integer.toString(fieldDefID), objectKey);
    }

    public int getFieldID() {
        return getIdInType();
    }

    /**
     * @deprecated This method should not be called directly, but rather it
     * should be replace by a call to the constructor with the proper IDInType.
     * This has been deprecated because the new getChildInstance() is much
     * faster
     * @param IDInType the IDInType
     * @return the ObjectKey corresponding to the ID for this class type
     */
    public static ObjectKey getChildInstance(String IDInType) {
        return new FieldDefinitionKey(Integer.parseInt(IDInType));
    }

    public ObjectKey getChildInstance(String IDInType, String objectKey) {
        return new FieldDefinitionKey(Integer.parseInt(IDInType), objectKey);
    }
}
