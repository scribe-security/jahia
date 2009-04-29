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
package org.jahia.services.content.nodetypes;

import org.apache.log4j.Logger;
import org.jahia.services.content.JCRStoreService;

import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.RepositoryException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: toto
 * Date: 4 janv. 2008
 * Time: 15:08:56
 * To change this template use File | Settings | File Templates.
 */
public class NodeTypeRegistry implements NodeTypeManager {
    public static final String SYSTEM = "system";
    private static Logger logger = Logger.getLogger(NodeTypeRegistry.class);

    private List<ExtendedNodeType> nodeTypesList = new ArrayList<ExtendedNodeType>();
    private Map<Name, ExtendedNodeType> nodetypes = new HashMap<Name, ExtendedNodeType>();

    private Map<String,String> namespaces = new HashMap<String,String>();

    private Map<String,List<File>> files = new HashMap<String,List<File>>();

    private static NodeTypeRegistry instance;

    public synchronized static NodeTypeRegistry getInstance() {
        if (instance == null) {
            instance = new NodeTypeRegistry();
        }
        return instance;
    }

    private NodeTypeRegistry() {
        try {
            String cnddir = org.jahia.settings.SettingsBean.getInstance().getJahiaEtcDiskPath() + "/repository/nodetypes";
            try {
                File f = new File(cnddir);
                SortedSet<File> cndfiles = new TreeSet<File>(Arrays.asList(f.listFiles()));
                for (File file : cndfiles) {
                    addDefinitionsFile(file, SYSTEM + "-" + file.getName().split("-")[1], false);
                }
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void addDefinitionsFile(File file, String systemId, boolean redeploy) throws ParseException, IOException {
        String ext = file.getName().substring(file.getName().lastIndexOf('.'));
        if (ext.equalsIgnoreCase(".cnd")) {
            JahiaCndReader r = new JahiaCndReader(new FileReader(file),file.getPath(), systemId, this);
            r.parse();
        } else if (ext.equalsIgnoreCase(".grp")) {
            JahiaGroupingFileReader r = new JahiaGroupingFileReader(new FileReader(file), file.getName(),systemId, this);
            r.parse();            
        }
        if (redeploy) {
            JCRStoreService.getInstance().deployDefinitions(systemId);
        }
    }

    public List<File> getFiles(String type) {
        return files.get(type);
    }

    public ExtendedNodeType getNodeType(String name) throws NoSuchNodeTypeException {
        ExtendedNodeType res = nodetypes.get(new Name(name, namespaces));
        if (res == null) {
            throw new NoSuchNodeTypeException(name);
        }
        return res;
    }

    public NodeTypeIterator getAllNodeTypes() {
        return new JahiaNodeTypeIterator(nodeTypesList.iterator(),nodeTypesList.size());
    }

    public NodeTypeIterator getNodeTypes(String systemId) {
        List<ExtendedNodeType> l = new ArrayList<ExtendedNodeType>();
        for (ExtendedNodeType nt : nodeTypesList) {
            if (nt.getSystemId().equals(systemId)) {
                l.add(nt);
            }
        }
        return new JahiaNodeTypeIterator(l.iterator(),l.size());
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public NodeTypeIterator getPrimaryNodeTypes() throws RepositoryException {
        List<ExtendedNodeType> res = new ArrayList<ExtendedNodeType>();
        for (Iterator<ExtendedNodeType> iterator = nodetypes.values().iterator(); iterator.hasNext();) {
            ExtendedNodeType nt = iterator.next();
            if (!nt.isMixin()) {
                res.add(nt);
            }
        }
        return new JahiaNodeTypeIterator(res.iterator(), res.size());
    }

    public NodeTypeIterator getMixinNodeTypes() throws RepositoryException {
        List<ExtendedNodeType> res = new ArrayList<ExtendedNodeType>();
        for (Iterator<ExtendedNodeType> iterator = nodetypes.values().iterator(); iterator.hasNext();) {
            ExtendedNodeType nt = iterator.next();
            if (nt.isMixin()) {
                res.add(nt);
            }
        }
        return new JahiaNodeTypeIterator(res.iterator(), res.size());
    }

    public void addNodeType(Name name, ExtendedNodeType nodeType) {
        nodeTypesList.add(nodeType);
        nodetypes.put(name, nodeType);
    }

    public void unregisterNodeType(Name name) {
        ExtendedNodeType nt = nodetypes.remove(name);
        nodeTypesList.remove(nt);
    }

    public void unregisterNodeTypes(String systemId) {
        for (Name n : new HashSet<Name>(nodetypes.keySet())) {
            ExtendedNodeType nt = nodetypes.get(n);
            if (systemId.equals(nt.getSystemId())) {
                unregisterNodeType(n);
            }
        }
    }

    class JahiaNodeTypeIterator implements NodeTypeIterator {
        private long size;
        private long pos=0;
        private Iterator<ExtendedNodeType> iterator;

        JahiaNodeTypeIterator(Iterator<ExtendedNodeType> it, long size) {
            this.iterator = it;
            this.size = size;
        }

        public NodeType nextNodeType() {
            pos += 1;
            return iterator.next();
        }

        public void skip(long l) {
            if ((pos + l + 1) > size) {
                throw new NoSuchElementException("Tried to skip past " + l +
                        " elements, which with current pos (" + pos +
                        ") brings us past total size=" + size);
            }
            for (int i=0; i < l; i++) {
                next();
            }
        }

        public long getSize() {
            return size;
        }

        public long getPosition() {
            return pos;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            pos += 1;
            return iterator.next();
        }

        public void remove() {
            iterator.remove();
            size -= 1;
        }
    }

}
