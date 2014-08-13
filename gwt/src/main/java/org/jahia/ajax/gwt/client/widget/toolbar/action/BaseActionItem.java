/**
 * ==========================================================================================
 * =                   JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION                       =
 * ==========================================================================================
 *
 *     Copyright (C) 2002-2014 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOSE THE GPL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     "This program is free software; you can redistribute it and/or
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
 *     As a special exception to the terms and conditions of version 2.0 of
 *     the GPL (or any later version), you may redistribute this Program in connection
 *     with Free/Libre and Open Source Software ("FLOSS") applications as described
 *     in Jahia's FLOSS exception. You should have received a copy of the text
 *     describing the FLOSS exception, also available here:
 *     http://www.jahia.com/license"
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ======================================================================================
 *
 *     IF YOU DECIDE TO CHOOSE THE JSEL LICENSE, YOU MUST COMPLY WITH THE FOLLOWING TERMS:
 *
 *     Alternatively, commercial and supported versions of the program - also known as
 *     Enterprise Distributions - must be used in accordance with the terms and conditions
 *     contained in a separate written agreement between you and Jahia Solutions Group SA.
 *
 *     If you are unsure which license is appropriate for your use,
 *     please contact the sales department at sales@jahia.com.
 *
 *
 * ==========================================================================================
 * =                                   ABOUT JAHIA                                          =
 * ==========================================================================================
 *
 *     Rooted in Open Source CMS, Jahia’s Digital Industrialization paradigm is about
 *     streamlining Enterprise digital projects across channels to truly control
 *     time-to-market and TCO, project after project.
 *     Putting an end to “the Tunnel effect”, the Jahia Studio enables IT and
 *     marketing teams to collaboratively and iteratively build cutting-edge
 *     online business solutions.
 *     These, in turn, are securely and easily deployed as modules and apps,
 *     reusable across any digital projects, thanks to the Jahia Private App Store Software.
 *     Each solution provided by Jahia stems from this overarching vision:
 *     Digital Factory, Workspace Factory, Portal Factory and eCommerce Factory.
 *     Founded in 2002 and headquartered in Geneva, Switzerland,
 *     Jahia Solutions Group has its North American headquarters in Washington DC,
 *     with offices in Chicago, Toronto and throughout Europe.
 *     Jahia counts hundreds of global brands and governmental organizations
 *     among its loyal customers, in more than 20 countries across the globe.
 *
 *     For more information, please visit http://www.jahia.com
 */
package org.jahia.ajax.gwt.client.widget.toolbar.action;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;

import org.jahia.ajax.gwt.client.data.GWTJahiaProperty;
import org.jahia.ajax.gwt.client.data.node.GWTBitSet;
import org.jahia.ajax.gwt.client.data.node.GWTJahiaNode;
import org.jahia.ajax.gwt.client.data.toolbar.GWTJahiaToolbarItem;
import org.jahia.ajax.gwt.client.util.Constants;
import org.jahia.ajax.gwt.client.util.Formatter;
import org.jahia.ajax.gwt.client.util.icons.ToolbarIconProvider;
import org.jahia.ajax.gwt.client.util.security.PermissionsUtils;
import org.jahia.ajax.gwt.client.widget.Linker;

import java.util.Map;

/**
 * User: jahia
 * Date: 4 avr. 2008
 * Time: 13:32:32
 */
@SuppressWarnings("serial")
public abstract class BaseActionItem implements ActionItem {
    private GWTJahiaToolbarItem gwtToolbarItem;

    private transient Component textToolitem = null;
    private transient MenuItem menuItem = null;
    private transient MenuItem contextMenuItem = null;
    protected transient Linker linker;

    public BaseActionItem() {
    }

    public Component getCustomItem() {
        return null;
    }

    /**
     * Get text toolitem
     *
     * @return
     */
    public Component getTextToolItem() {
        if (textToolitem != null) {
            return textToolitem;
        }
        textToolitem = createNewToolItem();
        final GWTJahiaToolbarItem gwtJahiaToolbarItem = getGwtToolbarItem();
        int layout = gwtJahiaToolbarItem.getLayout();

        // set properties that are specific to a ToggleToolItem
        if (textToolitem instanceof ToggleButton) {
            ((ToggleButton) textToolitem).toggle(gwtJahiaToolbarItem.isSelected());
        }

        // set properties that are specific to a Button
        if (textToolitem instanceof Button) {
            if (layout == Constants.LAYOUT_BUTTON_LABEL || layout == Constants.LAYOUT_ONLY_LABEL) {
                if (gwtJahiaToolbarItem.isDisplayTitle()) {
                    ((Button) textToolitem).setText(gwtJahiaToolbarItem.getTitle());
                }
            }
            if (layout == Constants.LAYOUT_BUTTON_LABEL || layout == Constants.LAYOUT_BUTTON) {
                if (gwtJahiaToolbarItem.getIcon() != null) {
                    ((Button) textToolitem).setIcon(ToolbarIconProvider.getInstance().getIcon(gwtJahiaToolbarItem.getIcon()));
                }
            }

            // add listener
            SelectionListener<ButtonEvent> listener = getSelectListener();
            if (listener != null) {
                ((Button) textToolitem).addSelectionListener(listener);
            }
        }

        // description
        String description = gwtJahiaToolbarItem.getDescription();
        if (gwtJahiaToolbarItem.getDescription() != null && description.length() > 0 && textToolitem.getToolTip() == null && !gwtJahiaToolbarItem.getTitle().equals(description)) {
            textToolitem.setToolTip(description);
        }

        return textToolitem;
    }


    public MenuItem getMenuItem() {
        if (menuItem != null) {
            return menuItem;
        }
        menuItem = createMenuItem();
        initMenuItem(menuItem);
        return menuItem;

    }


    public MenuItem getContextMenuItem() {
        if (contextMenuItem != null) {
            return contextMenuItem;
        }
        contextMenuItem = createMenuItem();
        initMenuItem(contextMenuItem);
        return contextMenuItem;
    }

    private void initMenuItem(final MenuItem menuItem) {
        GWTJahiaToolbarItem toolbarItem = getGwtToolbarItem();

        if (toolbarItem.getIcon() != null) {
            menuItem.setIcon(ToolbarIconProvider.getInstance().getIcon(toolbarItem.getIcon()));
        }

        // selection
        menuItem.setText(toolbarItem.getTitle());
        if (!toolbarItem.getTitle().equals(toolbarItem.getDescription())) {
            menuItem.setToolTip(toolbarItem.getDescription());
        }
        SelectionListener<MenuEvent> listener = getSelectListener();
        menuItem.addSelectionListener(listener);
    }

    public void setSubMenu(Menu menu) {
        if (isTextToolItem()) {
            Button button = (Button) getTextToolItem();
            button.setMenu(menu);
        }

        if (isMenuItem()) {
            MenuItem mi = getMenuItem();
            mi.setSubMenu(menu);
        }

        if (isContextMenuItem()) {
            MenuItem mi = getContextMenuItem();
            mi.setSubMenu(menu);
        }
    }

    public boolean isTextToolItem() {
        return textToolitem != null;
    }

    public boolean isMenuItem() {
        return menuItem != null;
    }

    public boolean isContextMenuItem() {
        return contextMenuItem != null;
    }

    public void setTextToolitem(Component textToolitem) {
        this.textToolitem = textToolitem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setContextMenuItem(MenuItem contextMenuItem) {
        this.contextMenuItem = contextMenuItem;
    }

    public void setEnabled(boolean enabled) {
        if (isTextToolItem()) {
            if (gwtToolbarItem.isHideWhenDisabled()) {
                getTextToolItem().setVisible(enabled);
            } else {
                Formatter.setButtonEnabled(getTextToolItem(), enabled);
            }
        }
        if (isMenuItem()) {
            if (gwtToolbarItem.isHideWhenDisabled()) {
                getMenuItem().setVisible(enabled);
            } else {
                Formatter.setMenuItemEnabled(getMenuItem(), enabled);
            }
        }
        if (isContextMenuItem()) {
            getContextMenuItem().setVisible(enabled);
        }
    }


    /**
     * Executed when the item is clicked
     *
     * @return
     */

    private <T extends ComponentEvent> SelectionListener<T> getSelectListener() {
        return new SelectionListener<T>() {
            public void componentSelected(T event) {
                onComponentSelection();
            }
        };
    }

    /**
     * Create a new componet
     *
     * @return
     */
    public Component createNewToolItem() {
        return new Button();
    }

    /**
     * Create a menuItem
     *
     * @return
     */
    public MenuItem createMenuItem() {
        return new MenuItem();
    }

    /**
     * Get the corresponding gwt item
     * @return
     */
    public GWTJahiaToolbarItem getGwtToolbarItem() {
        return gwtToolbarItem;
    }

    /**
     * Init the action item.
     * @param gwtToolbarItem
     * @param linker
     */
    public void init(GWTJahiaToolbarItem gwtToolbarItem, Linker linker) {
        this.linker = linker;
        this.gwtToolbarItem = gwtToolbarItem;
    }

    /**
     * Het property value
     *
     * @param gwtToolbarItem
     * @param propertyName
     * @return
     */
    public String getPropertyValue(GWTJahiaToolbarItem gwtToolbarItem, String propertyName) {
        Map<String, GWTJahiaProperty> properties = gwtToolbarItem.getProperties();
        GWTJahiaProperty property = properties != null ? properties.get(propertyName) : null;
        return property != null ? property.getValue() : null;
    }

    /**
     * Called when the action component is selected. Override this method to provide custom behaviour
     */
    public  void onComponentSelection(){

    }

    /**
     *  Called when there is a new liker selection. Override this method to provide custom behaviour
     */
    public  void handleNewLinkerSelection(){

    }

    public  void handleNewMainNodeLoaded(GWTJahiaNode node){

    }

    public boolean hasPermission(GWTJahiaNode node) {
        if (gwtToolbarItem.getRequiredPermission() != null) {
            return PermissionsUtils.isPermitted(gwtToolbarItem.getRequiredPermission(), node);
        }
        return true;
    }

    public boolean hasPermission(GWTBitSet permissions) {
        if (gwtToolbarItem.getRequiredPermission() != null) {
            return PermissionsUtils.isPermitted(gwtToolbarItem.getRequiredPermission(), permissions);
        }
        return true;
    }

    protected void updateTitle(String title) {
        if (textToolitem != null) {
            ((Button) textToolitem).setText(title);
        }
        if (menuItem != null) {
            menuItem.setText(title);
            if (menuItem.getParentMenu() != null) {
                menuItem.getParentMenu().recalculate();
            }
        }
        if (contextMenuItem != null) {
            contextMenuItem.setText(title);
            if (menuItem !=null && menuItem.getParentMenu() != null) {
                menuItem.getParentMenu().recalculate();
            }
        }
    }

}
