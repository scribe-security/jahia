/**
 * This file is part of Jahia: An integrated WCM, DMS and Portal Solution
 * Copyright (C) 2002-2010 Jahia Solutions Group SA. All rights reserved.
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
 * Commercial and Supported Versions of the program
 * Alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms contained in a separate written agreement
 * between you and Jahia Solutions Group SA. If you are unsure which license is appropriate
 * for your use, please contact the sales department at sales@jahia.com.
 */

package org.jahia.bin;

import java.util.Set;

/**
 * Base class for controllers, exposing repository nodes via JCR Query API.
 * 
 * @author Sergiy Shyrkov
 */
public abstract class BaseFindController extends JahiaController {

    public static class PropertyFilter {
        
        public static final PropertyFilter EMPTY = new PropertyFilter();

        private Set<String> excludes;

        private Set<String> includes;

        public Set<String> getExcludes() {
            return excludes;
        }

        public Set<String> getIncludes() {
            return includes;
        }

        public void setExcludes(Set<String> excludes) {
            this.excludes = excludes != null && excludes.size() == 0 ? null : excludes;
        }

        public void setIncludes(Set<String> includes) {
            this.includes = includes != null && includes.size() == 0 ? null : includes;
        }
    }

    protected int defaultLimit = 20;

    protected int hardLimit = 100;

    /**
     * The default number of results this controller will return if no limit was specified as a request parameter.
     * 
     * @param defaultLimit
     *            default number of results this controller will return if no limit was specified as a request parameter
     */
    public void setDefaultLimit(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    /**
     * Sets the "hard" limit, i.e. the maximum number of results this controller will return no matter what limit was specified as a request
     * parameter. This is done to prevent denial of service attacks or just limit the number of results to some "reasonable" amount.
     * 
     * @param hardLimit
     *            the maximum number of results this controller will return
     */
    public void setHardLimit(int hardLimit) {
        this.hardLimit = hardLimit;
    }
}
