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
package org.jahia.ajax.gwt.client.module;

import com.google.gwt.user.client.ui.*;
import com.allen_sauer.gwt.log.client.Log;

import java.util.*;

import org.jahia.ajax.gwt.client.core.JahiaModule;
import org.jahia.ajax.gwt.client.core.JahiaType;
import org.jahia.ajax.gwt.client.data.config.GWTJahiaPageContext;
import org.jahia.ajax.gwt.client.widget.opensearch.JahiaOpenSearchTriPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OpenSearchJahiaModule extends JahiaModule {


    public String getJahiaModuleType() {
       return JahiaType.OPENSEARCH;
   }

   public void onModuleLoad(GWTJahiaPageContext page, List<RootPanel> rootPanels) {
       try {
           for (RootPanel rootPanel : rootPanels) {
               rootPanel.add(new JahiaOpenSearchTriPanel(rootPanel,this));
           }
       } catch (Exception e) {
           Log.error(e.getMessage(), e);
       }
   }    
}