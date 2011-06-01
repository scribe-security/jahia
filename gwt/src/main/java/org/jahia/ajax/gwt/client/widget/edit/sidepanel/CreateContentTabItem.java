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

package org.jahia.ajax.gwt.client.widget.edit.sidepanel;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import org.jahia.ajax.gwt.client.core.BaseAsyncCallback;
import org.jahia.ajax.gwt.client.data.definition.GWTJahiaNodeType;
import org.jahia.ajax.gwt.client.data.toolbar.GWTSidePanelTab;
import org.jahia.ajax.gwt.client.messages.Messages;
import org.jahia.ajax.gwt.client.service.definition.JahiaContentDefinitionService;
import org.jahia.ajax.gwt.client.widget.Linker;
import org.jahia.ajax.gwt.client.widget.edit.ContentTypeTree;
import org.jahia.ajax.gwt.client.widget.edit.EditLinker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Side panel tab that allows creation of new content items using drag and drop.
 * User: toto
 * Date: Dec 21, 2009
 * Time: 3:14:11 PM
 */
class CreateContentTabItem extends SidePanelTabItem {

    private transient ContentTypeTree contentTypeTree;
    private transient CreateGridDragSource gridDragSource;
    private boolean displayStudioElement = false;
    private String baseType = null;

    public TabItem create(GWTSidePanelTab config) {
        super.create(config);
        tab.setLayout(new FitLayout());

        contentTypeTree = new ContentTypeTree(null);

        refresh(Linker.REFRESH_DEFINITIONS);

        tab.add(contentTypeTree);
        gridDragSource = new CreateGridDragSource(contentTypeTree.getTreeGrid());
        return tab;
    }

    @Override
    public void initWithLinker(EditLinker linker) {
        super.initWithLinker(linker);
//        contentTypeTree.setLinker(linker);
        gridDragSource.addDNDListener(linker.getDndListener());
    }

    public void setDisplayStudioElement(boolean displayStudioElement) {
        this.displayStudioElement = displayStudioElement;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    @Override
    public void refresh(int flag) {
        if ((flag & Linker.REFRESH_DEFINITIONS) != 0) {
            contentTypeTree.getStore().removeAll();

            JahiaContentDefinitionService.App.getInstance()
                    .getSubNodetypes(baseType != null ? Arrays.asList(baseType.split(" ")) : null, true, displayStudioElement, new BaseAsyncCallback<Map<GWTJahiaNodeType, List<GWTJahiaNodeType>>>() {
                        public void onApplicationFailure(Throwable caught) {
                            MessageBox.alert(Messages.get("label.error", "Error"),
                                    "Unable to load content definitions. Cause: " + caught.getLocalizedMessage(), null);
                        }

                        public void onSuccess(Map<GWTJahiaNodeType, List<GWTJahiaNodeType>> result) {
                            contentTypeTree.filldataStore(result);
                        }
                    });
        }
    }
}
