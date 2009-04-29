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
package org.jahia.ajax.gwt.client.widget.opensearch;

import org.jahia.ajax.gwt.client.widget.opensearch.JahiaOpenSearchTriPanel;
import org.jahia.ajax.gwt.client.data.opensearch.GWTJahiaOpenSearchEngine;
import org.jahia.ajax.gwt.client.data.opensearch.GWTJahiaOpenSearchEngineGroup;
import org.jahia.ajax.gwt.client.widget.tripanel.TopBar;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolItem;

/**
 * Created by IntelliJ IDEA.
 * User: hollis
 * Date: 25 sept. 2008
 * Time: 10:08:21
 * To change this template use File | Settings | File Templates.
 */
public class OpenSearchToolBar extends TopBar {

    private JahiaOpenSearchTriPanel openSearchTriPanel;
    private ToolBar toolbar;
    private TextField searchField;

    private String viewType;
    private ComboBox<ViewTypeModel> viewTypeSelector;
    private CheckBox rssSearchesOnly;
    private TextToolItem openSearchEngineStandAloneWindowButton;

    public OpenSearchToolBar(JahiaOpenSearchTriPanel openSearchTriPanel) {
        this.openSearchTriPanel = openSearchTriPanel;
        this.viewType = this.openSearchTriPanel.getViewType();
        buildGUI();
    }

    public Component getComponent() {
        return toolbar;
    }

    public void handleNewSelection(Object leftTreeSelection, Object topTableSelection) {
        //@todo;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getSearchTerms(){
        return (String)searchField.getValue();    
    }

    private void buildGUI(){
        toolbar = new ToolBar() ;
        toolbar.setHeight(28);

        // refresh button
        TextToolItem refreshItem = new TextToolItem();
        refreshItem.setIconStyle("gwt-pdisplay-icons-refresh");
        refreshItem.addSelectionListener(new SelectionListener<ComponentEvent>() {
            public void componentSelected(ComponentEvent event) {
                getLinker().refreshTable();
            }
        });

        searchField = new TextField();
        ToolItem toolItem = new AdapterToolItem(searchField);
        toolbar.add(toolItem);

        TextToolItem searchButton = new TextToolItem("search");
        toolbar.add(searchButton);
        searchButton.addSelectionListener(new SelectionListener<ComponentEvent>() {
            public void componentSelected(ComponentEvent event) {
                onSearchClick(event);
            }
        });

        toolItem = new FillToolItem();
        toolItem.setWidth("30px");
        toolbar.add(new SeparatorToolItem());
        openSearchEngineStandAloneWindowButton = new TextToolItem("open");
        openSearchEngineStandAloneWindowButton.setToolTip("Open selected search engine in popup");
        toolbar.add(openSearchEngineStandAloneWindowButton);
        openSearchEngineStandAloneWindowButton.addSelectionListener(new SelectionListener<ComponentEvent>() {
            public void componentSelected(ComponentEvent event) {
                onOpenSearchEngineStandAloneWindowClick(event);
            }
        });
        
        toolItem = new FillToolItem();
        toolbar.add(toolItem);

        /*
        viewTypeSelector = new ComboBox<ViewTypeModel>();
        viewTypeSelector.setStore(this.getViewTypes());
        viewTypeSelector.setDisplayField("display");
        //viewTypeSelector.setValue(getViewTypeModel(this.getViewType(),"Tab View"));
        viewTypeSelector.addListener(Events.Change, new Listener() {
            public void handleEvent(BaseEvent event) {
                ViewTypeModel data = viewTypeSelector.getValue();
                if (data != null){
                    onViewTypeChange(data.getValue());
                    viewTypeSelector.setValue(data);
                }
            }
        });

        toolItem = new AdapterToolItem(viewTypeSelector);
        toolbar.add(toolItem);
        toolbar.add(new SeparatorToolItem());
        */

        /*
        rssSearchesOnly = new CheckBox();
        rssSearchesOnly.setValue(this.openSearchTriPanel.isRssSearchMode());
        rssSearchesOnly.addListener(Events.Change, new Listener() {
            public void handleEvent(BaseEvent event) {
                onRssSearchOnlyChange();
            }
        });

        toolItem = new AdapterToolItem(rssSearchesOnly);
        toolbar.add(toolItem);
        toolItem = new TextToolItem("RSS searches only");
        toolItem.setEnableState(false);
        toolbar.add(toolItem);
        */
    }


    private void onSearchClick(ComponentEvent ce){
        if (searchField.getValue() != null){
            openSearchTriPanel.search(searchField.getValue().toString());
        }
    }

    /**
     *
     * @param searchGroup
     * @param selectedSearchEngine
     */
    public void onGroupSelectChange(GWTJahiaOpenSearchEngineGroup searchGroup,
                                    GWTJahiaOpenSearchEngine selectedSearchEngine) {
        onSearchEngineSelected(selectedSearchEngine);
    }

    public void onSearchEngineSelected(GWTJahiaOpenSearchEngine searchEngine) {
        if (searchEngine == null && openSearchEngineStandAloneWindowButton != null){
            openSearchEngineStandAloneWindowButton.disable();
        } else {
            openSearchEngineStandAloneWindowButton.enable();
        }
    }

    public void onSearchEngineChecked(GWTJahiaOpenSearchEngine searchEngine) {
        // do nothing
    }

    private void onOpenSearchEngineStandAloneWindowClick(ComponentEvent ce){
        openSearchTriPanel.onOpenSearchEngineStandAloneWindowClick(ce);
    }

    private void onRssSearchOnlyChange(){
        openSearchTriPanel.setRssSearchMode(this.rssSearchesOnly.getValue());
    }

    private void onViewTypeChange(String viewType){
        this.viewType = viewType;
        openSearchTriPanel.setViewType(viewType);
    }

    private ListStore<ViewTypeModel> getViewTypes(){
        ListStore<ViewTypeModel> datas = new ListStore<ViewTypeModel>();
        //datas.add(getViewTypeModel(OpenSearchLayoutView.TAB_VIEW,"Tab View"));
        datas.add(getViewTypeModel(OpenSearchLayoutView.HORIZONTAL_VIEW,"Column View"));
        return datas;
    }

    public ViewTypeModel getViewTypeModel(String type, String typeLabel){
        return new ViewTypeModel(type,typeLabel);
    }

    private class ViewTypeModel extends BaseModel {

        public ViewTypeModel() {
            super();
        }
        public ViewTypeModel(String type, String typeLabel){
            super();
            set("value",type);
            set("display",typeLabel);
        }

        public String getValue() {
            return get("value");
        }

        public String toString() {
          return getValue();
        }
    }

}
