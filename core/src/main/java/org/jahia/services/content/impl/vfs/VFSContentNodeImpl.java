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
package org.jahia.services.content.impl.vfs;


import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.io.IOUtils;
import org.jahia.services.content.nodetypes.NodeTypeRegistry;
import org.jahia.services.content.PropertyIteratorImpl;
import org.jahia.api.Constants;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.*;
import javax.jcr.version.VersionException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: toto
 * Date: Jul 24, 2008
 * Time: 3:54:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class VFSContentNodeImpl extends VFSItemImpl implements Node {
    private VFSSessionImpl session;
    private FileContent content;

    public VFSContentNodeImpl(VFSSessionImpl session, FileContent content) {
        this.session = session;
        this.content = content;
    }

    public Node addNode(String s) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        return null;
    }

    public Node addNode(String s, String s1) throws ItemExistsException, PathNotFoundException, NoSuchNodeTypeException, LockException, VersionException, ConstraintViolationException, RepositoryException {
        return null;
    }

    public void orderBefore(String s, String s1) throws UnsupportedRepositoryOperationException, VersionException, ConstraintViolationException, ItemNotFoundException, LockException, RepositoryException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, Value value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, Value value, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, Value[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, Value[] values, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, String[] strings) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, String[] strings, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, String s1) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        if (s.equals(Constants.JCR_MIMETYPE)) {
//            try {
//                content.setAttribute();
//            } catch (FileSystemException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
            return null;
        }
        throw new ConstraintViolationException("Unknown type");
    }

    public Property setProperty(String s, String s1, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, InputStream inputStream) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        if (s.equals(Constants.JCR_DATA)) {
            try {
                OutputStream outputStream = content.getOutputStream();
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                return new DataPropertyImpl();
            } catch (IOException e) {
                throw new RepositoryException("Cannot write to stream", e);
            }
        } 
        throw new ConstraintViolationException("Unknown type");
    }

    public Property setProperty(String s, boolean b) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, double v) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, long l) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Property setProperty(String s, Calendar calendar) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        if (s.equals(Constants.JCR_LASTMODIFIED)) {
            try {
                content.setLastModifiedTime(calendar.getTime().getTime());
            } catch (FileSystemException e) {
                
            }
            return null;
        }
        throw new ConstraintViolationException("Unknown type");
    }

    public Property setProperty(String s, Node node) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Node getNode(String s) throws PathNotFoundException, RepositoryException {
        return null;
    }

    public NodeIterator getNodes() throws RepositoryException {
        return new VFSNodeIteratorImpl(session,new ArrayList().iterator(),0);
    }

    public NodeIterator getNodes(String s) throws RepositoryException {
        return new VFSNodeIteratorImpl(session,new ArrayList().iterator(),0);
    }

    public Property getProperty(String s) throws PathNotFoundException, RepositoryException {
        if (s.equals(Constants.JCR_DATA)) {
            return new VFSPropertyImpl() {
                public long getLength() throws ValueFormatException, RepositoryException {
                    try {
                        return content.getSize();
                    } catch (FileSystemException e) {
                        return -1L;
                    }
                }

                public InputStream getStream() throws ValueFormatException, RepositoryException {
                    try {
                        return content.getInputStream();
                    } catch (FileSystemException e) {
                        return null;
                    }
                }

                public String getName() throws RepositoryException {
                    return Constants.JCR_DATA;
                }

                public PropertyDefinition getDefinition() throws RepositoryException {
                    return NodeTypeRegistry.getInstance().getNodeType(Constants.NT_RESOURCE).getPropertyDefinition(Constants.JCR_DATA);
                }
            };
        } else if (s.equals(Constants.JCR_MIMETYPE)) {
            return new VFSPropertyImpl() {
                public String getString() throws ValueFormatException, RepositoryException {
                    try {
                        String s1 = content.getContentInfo().getContentType();
                        if (s1 == null) {
                            return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("."+content.getFile().getName().getExtension());
                        }
                        return s1;
                    } catch (FileSystemException e) {
                        return null;
                    }
                }

                public String getName() throws RepositoryException {
                    return Constants.JCR_MIMETYPE;
                }

                public PropertyDefinition getDefinition() throws RepositoryException {
                    return NodeTypeRegistry.getInstance().getNodeType("mix:mimeType").getPropertyDefinition(Constants.JCR_MIMETYPE);
                }

            };
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PropertyIterator getProperties() throws RepositoryException {
        List l = new ArrayList();
        l.add(getProperty(Constants.JCR_DATA));
        l.add(getProperty(Constants.JCR_MIMETYPE));

        return new PropertyIteratorImpl(l.iterator(), l.size());
    }

    public PropertyIterator getProperties(String s) throws RepositoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Item getPrimaryItem() throws ItemNotFoundException, RepositoryException {
        return null;
    }

    public String getUUID() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }

    public int getIndex() throws RepositoryException {
        return 0;
    }

    public PropertyIterator getReferences() throws RepositoryException {
        return new PropertyIteratorImpl(new ArrayList().iterator(),0);
    }

    public boolean hasNode(String s) throws RepositoryException {
        return false;
    }

    public boolean hasProperty(String s) throws RepositoryException {
        return s.equals("jcr:data") || s.equals("jcr:mimeType");
    }

    public boolean hasNodes() throws RepositoryException {
        return false;
    }

    public boolean hasProperties() throws RepositoryException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NodeType getPrimaryNodeType() throws RepositoryException {
        return NodeTypeRegistry.getInstance().getNodeType("jnt:resource");
    }

    public NodeType[] getMixinNodeTypes() throws RepositoryException {
        return new NodeType[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isNodeType(String s) throws RepositoryException {
        return getPrimaryNodeType().isNodeType(s);
    }

    public void addMixin(String s) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, LockException, RepositoryException {
    }

    public void removeMixin(String s) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, LockException, RepositoryException {
    }

    public boolean canAddMixin(String s) throws NoSuchNodeTypeException, RepositoryException {
        return false;
    }

    public NodeDefinition getDefinition() throws RepositoryException {
        return null;
    }

    public Version checkin() throws VersionException, UnsupportedRepositoryOperationException, InvalidItemStateException, LockException, RepositoryException {
        return null;
    }

    public void checkout() throws UnsupportedRepositoryOperationException, LockException, RepositoryException {
    }

    public void doneMerge(Version version) throws VersionException, InvalidItemStateException, UnsupportedRepositoryOperationException, RepositoryException {
    }

    public void cancelMerge(Version version) throws VersionException, InvalidItemStateException, UnsupportedRepositoryOperationException, RepositoryException {
    }

    public void update(String s) throws NoSuchWorkspaceException, AccessDeniedException, LockException, InvalidItemStateException, RepositoryException {
    }

    public NodeIterator merge(String s, boolean b) throws NoSuchWorkspaceException, AccessDeniedException, MergeException, LockException, InvalidItemStateException, RepositoryException {
        return null;
    }

    public String getCorrespondingNodePath(String s) throws ItemNotFoundException, NoSuchWorkspaceException, AccessDeniedException, RepositoryException {
        return null;
    }

    public boolean isCheckedOut() throws RepositoryException {
        return false;
    }

    public void restore(String s, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
    }

    public void restore(Version version, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, RepositoryException {
    }

    public void restore(Version version, String s, boolean b) throws PathNotFoundException, ItemExistsException, VersionException, ConstraintViolationException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
    }

    public void restoreByLabel(String s, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
    }

    public VersionHistory getVersionHistory() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }

    public Version getBaseVersion() throws UnsupportedRepositoryOperationException, RepositoryException {
        return null;
    }

    public Lock lock(boolean b, boolean b1) throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, InvalidItemStateException, RepositoryException {
        return null;
    }

    public Lock getLock() throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, RepositoryException {
        return null;
    }

    public void unlock() throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, InvalidItemStateException, RepositoryException {
    }

    public boolean holdsLock() throws RepositoryException {
        return false;
    }

    public boolean isLocked() throws RepositoryException {
        return false;
    }

    public String getPath() throws RepositoryException {
        String s = content.getFile().getName().getPath().substring(((VFSRepositoryImpl)session.getRepository()).getRootPath().length());
        if (!s.startsWith("/")) {
            s = "/"+s;
        }
        return s+"/"+Constants.JCR_CONTENT;
    }

    public String getName() throws RepositoryException {
        return Constants.JCR_CONTENT;
    }

    public Node getParent() throws ItemNotFoundException, AccessDeniedException, RepositoryException {
        return new VFSNodeImpl(content.getFile(), session);
    }

    class DataPropertyImpl extends VFSPropertyImpl {
        public InputStream getStream() throws ValueFormatException, RepositoryException {
            try {
                return content.getInputStream();
            } catch (FileSystemException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            throw new RepositoryException();
        }
    }
}
