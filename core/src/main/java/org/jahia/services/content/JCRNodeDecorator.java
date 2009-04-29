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
package org.jahia.services.content;

import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.webdav.UsageEntry;
import org.jahia.data.fields.JahiaField;
import org.jahia.data.containers.JahiaContainer;
import org.jahia.data.files.JahiaFileField;
import org.jahia.params.ParamBean;
import org.jahia.params.ProcessingContext;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.version.VersionException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import java.util.*;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: toto
 * Date: Dec 4, 2008
 * Time: 10:21:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class JCRNodeDecorator implements JCRNodeWrapper {
    private JCRNodeWrapper node;

    public JCRNodeDecorator(JCRNodeWrapper node) {
        this.node = node;
    }

    public Node getRealNode() {
        return node.getRealNode();
    }

    public int getTransactionStatus() {
        return node.getTransactionStatus();
    }

    public JahiaUser getUser() {
        return node.getUser();
    }

    public boolean isValid() {
        return node.isValid();
    }

    public Map<String, List<String[]>> getAclEntries() {
        return node.getAclEntries();
    }

    public Map<String, List<String>> getAvailablePermissions() {
        return node.getAvailablePermissions();
    }

    public boolean isWriteable() {
        return node.isWriteable();
    }

    public boolean hasPermission(String perm) {
        return node.hasPermission(perm);
    }

    public Set comparePermsWithField(JahiaField theField, JahiaContainer theContainer) {
        return node.comparePermsWithField(theField, theContainer);
    }

    public void alignPermsWithField(JahiaField theField, Set users) {
        node.alignPermsWithField(theField, users);
    }

    public boolean changePermissions(String user, String perm) {
        return node.changePermissions(user, perm);        
    }

    public boolean changePermissions(String user, Map<String,String> perm) {
        return node.changePermissions(user, perm);
    }

    public boolean revokePermissions(String user) {
        return node.revokePermissions(user);
    }

    public boolean revokeAllPermissions() {
        return node.revokeAllPermissions();
    }

    public boolean getAclInheritanceBreak() {
        return node.getAclInheritanceBreak();
    }

    public boolean setAclInheritanceBreak(boolean inheritance) {
        return node.setAclInheritanceBreak(inheritance);
    }

    public JCRNodeWrapper uploadFile(String name, InputStream is, String contentType) throws RepositoryException {
        return node.uploadFile(name, is, contentType);
    }

    public JCRNodeWrapper createCollection(String name) throws RepositoryException {
        return node.createCollection(name);
    }

    public JahiaFileField getJahiaFileField() {
        return node.getJahiaFileField();
    }

    public String getStorageName() {
        return node.getStorageName();
    }

    public Exception getException() {
        return node.getException();
    }

    public String getAbsoluteUrl(ParamBean jParams) {
        return node.getAbsoluteUrl(jParams);
    }

    public String getUrl() {
        return node.getUrl();
    }

    public String getAbsoluteWebdavUrl(ParamBean jParams) {
        return node.getAbsoluteWebdavUrl(jParams);
    }

    public String getWebdavUrl() {
        return node.getWebdavUrl();
    }

    public List<String> getThumbnails() {
        return node.getThumbnails();
    }

    public String getThumbnailUrl(String name) {
        return node.getThumbnailUrl(name);
    }

    public List<JCRNodeWrapper> getChildren() {
        return node.getChildren();
    }

    public boolean isVisible() {
        return node.isVisible();
    }

    public Map<String, String> getPropertiesAsString() {
        return node.getPropertiesAsString();
    }

    public String getPrimaryNodeTypeName() {
        return node.getPrimaryNodeTypeName();
    }

    public List<String> getNodeTypes() {
        return node.getNodeTypes();
    }

    public boolean isCollection() {
        return node.isCollection();
    }

    public boolean isFile() {
        return node.isFile();
    }

    public boolean isPortlet() {
        return node.isPortlet();
    }

    public Date getLastModifiedAsDate() {
        return node.getLastModifiedAsDate();
    }

    public Date getContentLastModifiedAsDate() {
        return node.getContentLastModifiedAsDate();
    }

    public Date getCreationDateAsDate() {
        return node.getCreationDateAsDate();
    }

    public String getCreationUser() {
        return node.getCreationUser();
    }

    public String getModificationUser() {
        return node.getModificationUser();
    }

    public String getPropertyAsString(String name) {
        return node.getPropertyAsString(name);
    }

    public String getPropertyAsString(String namespace, String name) {
        return node.getPropertyAsString(namespace, name);
    }

    public void setProperty(String namespace, String name, String value) throws RepositoryException {
        node.setProperty(namespace, name, value);
    }

    public boolean renameFile(String newName) {
        return node.renameFile(newName);
    }

    public boolean moveFile(String dest) throws RepositoryException {
        return node.moveFile(dest);
    }

    public boolean moveFile(String dest, String name) throws RepositoryException {
        return node.moveFile(dest, name);
    }

    public boolean copyFile(String dest) throws RepositoryException {
        return node.copyFile(dest);
    }

    public boolean copyFile(String dest, String name) throws RepositoryException {
        return node.copyFile(dest, name);
    }

    public boolean copyFile(JCRNodeWrapper dest, String name) throws RepositoryException {
        return node.copyFile(dest, name);
    }

    public int deleteFile() {
        return node.deleteFile();
    }

    public boolean lockAsSystemAndStoreToken() {
        return node.lockAsSystemAndStoreToken();
    }

    public boolean lockAndStoreToken() {
        return node.lockAndStoreToken();
    }

    public boolean forceUnlock() {
        return node.forceUnlock();
    }

    public String getLockOwner() {
        return node.getLockOwner();
    }

    public void versionFile() {
        node.versionFile();
    }

    public boolean isVersioned() {
        return node.isVersioned();
    }

    public void checkpoint() {
        node.checkpoint();
    }

    public List<String> getVersions() {
        return node.getVersions();
    }

    public JCRNodeWrapper getVersion(String name) {
        return node.getVersion(name);
    }

    public JCRStoreProvider getJCRProvider() {
        return node.getJCRProvider();
    }

    public JCRStoreProvider getProvider() {
        return node.getProvider();
    }

    public JCRFileContent getFileContent() {
        return node.getFileContent();
    }

    public List<UsageEntry> findUsages() {
        return node.findUsages();
    }

    public List<UsageEntry> findUsages(boolean onlyLocked) {
        return node.findUsages(onlyLocked);
    }

    public List<UsageEntry> findUsages(ProcessingContext context, boolean onlyLocked) {
        return node.findUsages(context, onlyLocked);
    }

    public JCRNodeWrapper addNode(String s) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        return node.addNode(s);
    }

    public JCRNodeWrapper addNode(String s, String s1) throws ItemExistsException, PathNotFoundException, NoSuchNodeTypeException, LockException, VersionException, ConstraintViolationException, RepositoryException {
        return node.addNode(s, s1);
    }

    public void orderBefore(String s, String s1) throws UnsupportedRepositoryOperationException, VersionException, ConstraintViolationException, ItemNotFoundException, LockException, RepositoryException {
        node.orderBefore(s, s1);
    }

    public Property setProperty(String s, Value value) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, value);
    }

    public Property setProperty(String s, Value value, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, value, i);
    }

    public Property setProperty(String s, Value[] values) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, values);
    }

    public Property setProperty(String s, Value[] values, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, values, i);
    }

    public Property setProperty(String s, String[] strings) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, strings);
    }

    public Property setProperty(String s, String[] strings, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, strings, i);
    }

    public Property setProperty(String s, String s1) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, s1);
    }

    public Property setProperty(String s, String s1, int i) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, s1, i);
    }

    public Property setProperty(String s, InputStream inputStream) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, inputStream);
    }

    public Property setProperty(String s, boolean b) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, b);
    }

    public Property setProperty(String s, double v) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, v);
    }

    public Property setProperty(String s, long l) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, l);
    }

    public Property setProperty(String s, Calendar calendar) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return node.setProperty(s, calendar);
    }

    public Property setProperty(String s, Node node) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
        return this.node.setProperty(s, node);
    }

    public Node getNode(String s) throws PathNotFoundException, RepositoryException {
        return node.getNode(s);
    }

    public NodeIterator getNodes() throws RepositoryException {
        return node.getNodes();
    }

    public NodeIterator getNodes(String s) throws RepositoryException {
        return node.getNodes(s);
    }

    public Property getProperty(String s) throws PathNotFoundException, RepositoryException {
        return node.getProperty(s);
    }

    public PropertyIterator getProperties() throws RepositoryException {
        return node.getProperties();
    }

    public PropertyIterator getProperties(String s) throws RepositoryException {
        return node.getProperties(s);
    }

    public Item getPrimaryItem() throws ItemNotFoundException, RepositoryException {
        return node.getPrimaryItem();
    }

    public String getUUID() throws UnsupportedRepositoryOperationException, RepositoryException {
        return node.getUUID();
    }

    public int getIndex() throws RepositoryException {
        return node.getIndex();
    }

    public PropertyIterator getReferences() throws RepositoryException {
        return node.getReferences();
    }

    public boolean hasNode(String s) throws RepositoryException {
        return node.hasNode(s);
    }

    public boolean hasProperty(String s) throws RepositoryException {
        return node.hasProperty(s);
    }

    public boolean hasNodes() throws RepositoryException {
        return node.hasNodes();
    }

    public boolean hasProperties() throws RepositoryException {
        return node.hasProperties();
    }

    public NodeType getPrimaryNodeType() throws RepositoryException {
        return node.getPrimaryNodeType();
    }

    public NodeType[] getMixinNodeTypes() throws RepositoryException {
        return node.getMixinNodeTypes();
    }

    public boolean isNodeType(String s) throws RepositoryException {
        return node.isNodeType(s);
    }

    public void addMixin(String s) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        node.addMixin(s);
    }

    public void removeMixin(String s) throws NoSuchNodeTypeException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        node.removeMixin(s);
    }

    public boolean canAddMixin(String s) throws NoSuchNodeTypeException, RepositoryException {
        return node.canAddMixin(s);
    }

    public NodeDefinition getDefinition() throws RepositoryException {
        return node.getDefinition();
    }

    public Version checkin() throws VersionException, UnsupportedRepositoryOperationException, InvalidItemStateException, LockException, RepositoryException {
        return node.checkin();
    }

    public void checkout() throws UnsupportedRepositoryOperationException, LockException, RepositoryException {
        node.checkout();
    }

    public void doneMerge(Version version) throws VersionException, InvalidItemStateException, UnsupportedRepositoryOperationException, RepositoryException {
        node.doneMerge(version);
    }

    public void cancelMerge(Version version) throws VersionException, InvalidItemStateException, UnsupportedRepositoryOperationException, RepositoryException {
        node.cancelMerge(version);
    }

    public void update(String s) throws NoSuchWorkspaceException, AccessDeniedException, LockException, InvalidItemStateException, RepositoryException {
        node.update(s);
    }

    public NodeIterator merge(String s, boolean b) throws NoSuchWorkspaceException, AccessDeniedException, MergeException, LockException, InvalidItemStateException, RepositoryException {
        return node.merge(s, b);
    }

    public String getCorrespondingNodePath(String s) throws ItemNotFoundException, NoSuchWorkspaceException, AccessDeniedException, RepositoryException {
        return node.getCorrespondingNodePath(s);
    }

    public boolean isCheckedOut() throws RepositoryException {
        return node.isCheckedOut();
    }

    public void restore(String s, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
        node.restore(s, b);
    }

    public void restore(Version version, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, RepositoryException {
        node.restore(version, b);
    }

    public void restore(Version version, String s, boolean b) throws PathNotFoundException, ItemExistsException, VersionException, ConstraintViolationException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
        node.restore(version, s, b);
    }

    public void restoreByLabel(String s, boolean b) throws VersionException, ItemExistsException, UnsupportedRepositoryOperationException, LockException, InvalidItemStateException, RepositoryException {
        node.restoreByLabel(s, b);
    }

    public VersionHistory getVersionHistory() throws UnsupportedRepositoryOperationException, RepositoryException {
        return node.getVersionHistory();
    }

    public Version getBaseVersion() throws UnsupportedRepositoryOperationException, RepositoryException {
        return node.getBaseVersion();
    }

    public Lock lock(boolean b, boolean b1) throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, InvalidItemStateException, RepositoryException {
        return node.lock(b, b1);
    }

    public Lock getLock() throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, RepositoryException {
        return node.getLock();
    }

    public void unlock() throws UnsupportedRepositoryOperationException, LockException, AccessDeniedException, InvalidItemStateException, RepositoryException {
        node.unlock();
    }

    public boolean holdsLock() throws RepositoryException {
        return node.holdsLock();
    }

    public boolean isLocked() {
        return node.isLocked();
    }

    public boolean isLockable() {
        return node.isLockable();
    }

    public String getPath() {
        return node.getPath();
    }

    public String getName() {
        return node.getName();
    }

    public Item getAncestor(int i) throws ItemNotFoundException, AccessDeniedException, RepositoryException {
        return node.getAncestor(i);
    }

    public Node getParent() throws ItemNotFoundException, AccessDeniedException, RepositoryException {
        return node.getParent();
    }

    public int getDepth() throws RepositoryException {
        return node.getDepth();
    }

    public Session getSession() throws RepositoryException {
        return node.getSession();
    }

    public boolean isNode() {
        return node.isNode();
    }

    public boolean isNew() {
        return node.isNew();
    }

    public boolean isModified() {
        return node.isModified();
    }

    public boolean isSame(Item item) throws RepositoryException {
        return node.isSame(item);
    }

    public void accept(ItemVisitor itemVisitor) throws RepositoryException {
        node.accept(itemVisitor);
    }

    public void save() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, ReferentialIntegrityException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {
        node.save();
    }

    public void saveSession() throws AccessDeniedException, ItemExistsException, ConstraintViolationException, InvalidItemStateException, ReferentialIntegrityException, VersionException, LockException, NoSuchNodeTypeException, RepositoryException {
        node.saveSession();
    }

    public void refresh(boolean b) throws InvalidItemStateException, RepositoryException {
        node.refresh(b);
    }

    public void remove() throws VersionException, LockException, ConstraintViolationException, RepositoryException {
        node.remove();
    }
}
