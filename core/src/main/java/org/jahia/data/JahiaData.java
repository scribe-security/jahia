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

//
//  JahiaData
//  EV      30.10.2000
//  EV      23.12.2000  settings is not anymore in JahiaData, but in ProcessingContext
//
//  params()
//  fields()
//  containers()
//  gui()
//
//

package org.jahia.data;

import org.jahia.exceptions.JahiaException;
import org.jahia.params.ProcessingContext;

public class JahiaData {

    public static final String JAHIA_DATA = "org.jahia.data.JahiaData";

    private ProcessingContext jParams;

    /**
     * constructor
     * Build a JahiaData and optionally can force to create or not the fields set or containers set.
     * This is used in particular situation where fields and container sets are not required,
     * i.e. : when some engines are called from JahiaAdministration Servlet
     *
     * @param jParams
     * @param doBuildData
     * @author NK
     */
    public JahiaData(ProcessingContext jParams, boolean doBuildData) throws JahiaException {
        this.jParams = jParams;
    }


    /**
     * constructor
     * EV    30.10.2000
     * EV    18.11.2000  added jSettings in parameters
     */
    public JahiaData(ProcessingContext jParams) throws JahiaException {
        this(jParams, true);
    }


    /**
     * @return the current processing context.
     */
    public ProcessingContext getProcessingContext() {
        return jParams;
    }


} // end JahiaData

