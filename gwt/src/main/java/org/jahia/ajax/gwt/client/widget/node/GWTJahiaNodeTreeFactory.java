/**
 * ==========================================================================================
 * =                        DIGITAL FACTORY v7.0 - Community Distribution                   =
 * ==========================================================================================
 *
 *     Rooted in Open Source CMS, Jahia's Digital Industrialization paradigm is about
 *     streamlining Enterprise digital projects across channels to truly control
 *     time-to-market and TCO, project after project.
 *     Putting an end to "the Tunnel effect", the Jahia Studio enables IT and
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
 *
 * JAHIA'S DUAL LICENSING - IMPORTANT INFORMATION
 * ============================================
 *
 *     Copyright (C) 2002-2014 Jahia Solutions Group SA. All rights reserved.
 *
 *     THIS FILE IS AVAILABLE UNDER TWO DIFFERENT LICENSES:
 *     1/GPL OR 2/JSEL
 *
 *     1/ GPL
 *     ==========================================================
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
 *     describing the FLOSS exception, and it is also available here:
 *     http://www.jahia.com/license"
 *
 *     2/ JSEL - Commercial and Supported Versions of the program
 *     ==========================================================
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
package org.jahia.ajax.gwt.client.widget.node;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.jahia.ajax.gwt.client.core.BaseAsyncCallback;
import org.jahia.ajax.gwt.client.core.JahiaGWTParameters;
import org.jahia.ajax.gwt.client.data.node.GWTJahiaNode;
import org.jahia.ajax.gwt.client.service.content.JahiaContentManagementService;
import org.jahia.ajax.gwt.client.util.Collator;
import org.jahia.ajax.gwt.client.widget.edit.sidepanel.PathStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tree factory for loading {@link GWTJahiaNode} items.
 * @author toto
 */
public class GWTJahiaNodeTreeFactory {
    protected String repository;
    protected List<String> paths;
    protected List<String> nodeTypes;
    protected List<String> filters;
    protected List<String> fields = GWTJahiaNode.DEFAULT_FIELDS;
    protected List<String> mimeTypes;
    private PathStorage pathStorage;
    protected TreeLoader<GWTJahiaNode> loader;
    protected TreeStore<GWTJahiaNode> store;
    private boolean checkSubchilds = false;
    private List<String> hiddenTypes = new ArrayList<String>();
    private String hiddenRegex;
    private boolean displayHiddenTypes = false;
    private boolean showOnlyNodesWithTemplates = false;

    public GWTJahiaNodeTreeFactory(final List<String> paths) {
        this(paths, GWTJahiaNode.DEFAULT_FIELDS);
    }

    public GWTJahiaNodeTreeFactory(final List<String> paths, List<String> fields) {
        this.paths = paths;
        this.fields = fields;
        this.repository = paths.toString();
        pathStorage = new PathStorage(repository);
        repository = repository.replace("$siteKey", JahiaGWTParameters.getSiteKey());
    }

    public GWTJahiaNodeTreeFactory(final List<String> paths, boolean checkSubchilds) {
        this(paths);
        this.checkSubchilds = checkSubchilds;
    }

    public GWTJahiaNodeTreeFactory(TreeLoader<GWTJahiaNode> loader, String repository) {
        pathStorage = new PathStorage(repository);
        this.loader = loader;
        this.repository = repository;
    }

    public TreeLoader<GWTJahiaNode> getLoader() {
        if (loader == null) {
            loader = new GWTJahiaNodeTreeLoader(new GWTJahiaNodeProxy());
        }
        return loader;
    }


    public TreeStore<GWTJahiaNode> getStore() {
        if (store == null) {
            store = new TreeStore<GWTJahiaNode>(getLoader());
            store.setStoreSorter(new StoreSorter<GWTJahiaNode>(new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof String && o2 instanceof String) {
                        String s1 = (String) o1;
                        String s2 = (String) o2;
                        return Collator.getInstance().localeCompare(s1, s2);
                    } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
                        return ((Comparable) o1).compareTo(o2);
                    }
                    return 0;
                }
            }));
        }
        return store;
    }

    public GWTJahiaNodeTreeGrid getTreeGrid(ColumnModel cm) {
        GWTJahiaNodeTreeGrid grid = new GWTJahiaNodeTreeGrid(getStore(), cm);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        pathStorage.addStorageListener(grid);

        return grid;
    }

    public GWTJahiaNodeTreePanel getTreePanel() {
        GWTJahiaNodeTreePanel panel = new GWTJahiaNodeTreePanel(getStore());
        panel.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        panel.setAutoSelect(false);
        pathStorage.addStorageListener(panel);

        return panel;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public void setNodeTypes(List<String> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public void setFields(List<String> fields) {
        this.fields = new ArrayList<String>(fields);
        this.fields.add(GWTJahiaNode.LOCKS_INFO);
        this.fields.add(GWTJahiaNode.PERMISSIONS);
        this.fields.add(GWTJahiaNode.CHILDREN_INFO);
        this.fields.add(GWTJahiaNode.ICON);
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public List<String> getSelectedPath() {
        return pathStorage.getSelectedPath();
    }

    public void setSelectedPath(String selectedPath) {
        pathStorage.setSelectedPath(selectedPath);
    }

    public void setSelectedPath(List<String> selectedPath) {
        pathStorage.setSelectedPath(selectedPath);
    }

    public List<String> getOpenPath() {
        return pathStorage.getOpenPath();
    }

    public void setOpenPath(String openPath) {
        pathStorage.setOpenPath(openPath);
    }

    public void setOpenPath(List<String> openPath) {
        pathStorage.setOpenPath(openPath);
    }

    public void setDisplayHiddenTypes(boolean displayHiddenTypes) {
        this.displayHiddenTypes = displayHiddenTypes;
    }

    public void setHiddenRegex(String hiddenRegex) {
        this.hiddenRegex = hiddenRegex;
    }

    public void setHiddenTypes(List<String> hiddenTypes) {
        this.hiddenTypes = hiddenTypes;
    }

    public void setShowOnlyNodesWithTemplates(boolean showOnlyNodesWithTemplates) {
        this.showOnlyNodesWithTemplates = showOnlyNodesWithTemplates;
    }

    /**
     * {@link RpcProxy} implementation for {@link GWTJahiaNode} objects.
     */
    class GWTJahiaNodeProxy extends RpcProxy<List<GWTJahiaNode>> {

        @Override
        protected void load(Object currentPage, final AsyncCallback<List<GWTJahiaNode>> listAsyncCallback) {
            if (currentPage == null) {
                JahiaContentManagementService.App.getInstance()
                        .getRoot(paths, nodeTypes, mimeTypes, filters, fields, pathStorage.getSelectedPath(), pathStorage.getOpenPath(), checkSubchilds,
                                displayHiddenTypes, hiddenTypes, hiddenRegex, false, listAsyncCallback);
            } else {
                GWTJahiaNode gwtJahiaNode = (GWTJahiaNode) currentPage;
                if (gwtJahiaNode.isExpandOnLoad()) {
                    List<GWTJahiaNode> list = new ArrayList<GWTJahiaNode>();
                    for (ModelData modelData : gwtJahiaNode.getChildren()) {
                        list.add((GWTJahiaNode) modelData);
                    }
                    listAsyncCallback.onSuccess(list);
                } else {
                    JahiaContentManagementService.App.getInstance()
                            .lsLoad(gwtJahiaNode.getPath(), nodeTypes, mimeTypes, filters, fields, checkSubchilds, -1, -1, displayHiddenTypes,
                                    hiddenTypes, hiddenRegex, false, false, new BaseAsyncCallback<PagingLoadResult<GWTJahiaNode>>() {
                                public void onSuccess(PagingLoadResult<GWTJahiaNode> result) {
                                    listAsyncCallback.onSuccess(result.getData());
                                }

                                @Override
                                public void onApplicationFailure(Throwable caught) {
                                    listAsyncCallback.onFailure(caught);
                                }

                            });
                }
            }
        }

    }


    /**
     * Implementatoin of the {@link BaseTreeLoader} for {@link GWTJahiaNode} objects.
     */
    public class GWTJahiaNodeTreeLoader extends BaseTreeLoader<GWTJahiaNode> {
        GWTJahiaNodeTreeLoader(DataProxy proxy) {
            super(proxy);
        }

        @Override
        public boolean hasChildren(GWTJahiaNode parent) {
            return parent.hasChildren();
        }
    }

    /**
     * Dedicated {@link TreeGrid} implementation for displaying {@link GWTJahiaNode} objects.
     */
    public class GWTJahiaNodeTreeGrid extends TreeGrid<GWTJahiaNode> {
        public GWTJahiaNodeTreeGrid(TreeStore store, ColumnModel cm) {
            super(store, cm);
        }

        @Override
        protected void onRender(Element target, int index) {
            super.onRender(target, index);
        }

        @Override
        protected void onAfterRenderView() {
            // init (loading) is done when the grid is displayed, not while we create the grid
            //super.onAfterRenderView();
        }

        protected void onDataChanged(TreeStoreEvent<GWTJahiaNode> mTreeStoreEvent) {
            super.onDataChanged(mTreeStoreEvent);
            final GWTJahiaNode p = mTreeStoreEvent.getParent();
            Scheduler.get().scheduleDeferred(new Command() {
                public void execute() {
                    if (p == null) {
                        expandChildren(treeStore.getRootItems());
                    } else {
                        if (treeStore.getChildren(p) != null) {
                            expandChildren(treeStore.getChildren(p));
                        }
                    }
                }
            });
        }

        private void expandChildren(List<GWTJahiaNode> children) {
            for (GWTJahiaNode child : children) {
                if (child.isExpandOnLoad()) {
                    setExpanded(child, true);
                }
                if (child.isSelectedOnLoad()) {
                    getSelectionModel().select(true, child);
                }
            }
        }

    }

    /**
     * {@link TreePanel} implementation for {@link GWTJahiaNode} objects.
     */
    public class GWTJahiaNodeTreePanel extends TreePanel<GWTJahiaNode> {
        GWTJahiaNodeTreePanel(TreeStore store) {
            super(store);
            setAutoSelect(false);
        }

        protected void onDataChanged(TreeStoreEvent<GWTJahiaNode> mTreeStoreEvent) {
            super.onDataChanged(mTreeStoreEvent);
            final GWTJahiaNode p = mTreeStoreEvent.getParent();
            Scheduler.get().scheduleDeferred(new Command() {
                public void execute() {
                    if (p == null) {
                        expandChildren(store.getRootItems());
                    } else {
                        expandChildren(store.getChildren(p));
                    }
                }
            });
        }

        private void expandChildren(List<GWTJahiaNode> children) {
            for (GWTJahiaNode child : children) {
                if (child.isExpandOnLoad()) {
                    setExpanded(child, true);
                }
                if (child.isSelectedOnLoad()) {
                    getSelectionModel().select(true, child);
                }
            }
        }

    }

}
