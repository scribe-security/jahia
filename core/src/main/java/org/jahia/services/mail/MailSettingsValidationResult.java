/**
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *                                 http://www.jahia.com
 *
 *     Copyright (C) 2002-2016 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ==================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     This program is free software; you can redistribute it and/or
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
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ===================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 */
package org.jahia.services.mail;

/**
 * Mail settings validation result.
 * 
 * @author Sergiy Shyrkov
 */
public class MailSettingsValidationResult {
    public static final MailSettingsValidationResult SUCCESSFULL = new MailSettingsValidationResult();

    private Object[] args;

    private String messageKey;

    private String property;

    private boolean success = true;

    
    private MailSettingsValidationResult() {
        super();
    }

    public MailSettingsValidationResult(String property, String messageKey) {
        this(property, messageKey, null);
    }

    public MailSettingsValidationResult(String property,
            String messageKey, Object[] args) {
        super();
        this.success = false;
        this.property = property;
        this.messageKey = messageKey;
        this.args = args;
    }

    /**
     * Returns the args.
     * 
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Returns the messageKey.
     * 
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Returns the property.
     * 
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Returns the success.
     * 
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

}
