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
package org.jahia.ajax.gwt.client.data;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Data object that represents a key in the resource bundle in multiple languages.
 * 
 * @author Sergiy Shyrkov
 */
public class GWTResourceBundleEntry extends BaseModelData implements
        Comparable<GWTResourceBundleEntry> {

    private static final long serialVersionUID = 5279030130987007606L;

    public GWTResourceBundleEntry() {
        super();
        setKey("<empty>");
        BaseModelData data = new BaseModelData();
        data.setAllowNestedValues(false);
        setValues(data);
    }

    public GWTResourceBundleEntry(String key) {
        this();
        setKey(key);
    }

    public int compareTo(GWTResourceBundleEntry o) {
        return getKey().compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass()
                && getKey().equals(((GWTResourceBundleEntry) obj).getKey());
    }

    public String getKey() {
        return get("key");
    }

    public String getValue(String language) {
        return getValues().get(language);
    }

    public BaseModelData getValues() {
        return get("values");
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    public void setKey(String key) {
        set("key", key);
    }

    public void setValue(String language, String value) {
        getValues().set(language, value);
    }

    public void setValues(BaseModelData values) {
        set("values", values);
    }
}
