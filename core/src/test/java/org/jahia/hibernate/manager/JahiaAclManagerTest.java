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
 package org.jahia.hibernate.manager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.jahia.hibernate.model.JahiaAcl;
import org.jahia.hibernate.model.JahiaAclEntry;
import org.jahia.hibernate.model.JahiaAclEntryPK;
import org.jahia.services.acl.ACLInfo;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * JahiaAclManager Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>01/05/2005</pre>
 */
public class JahiaAclManagerTest extends TestCase {
// ------------------------------ FIELDS ------------------------------

    protected ApplicationContext ctx = null;
    protected final Log log = LogFactory.getLog(getClass());
    protected JahiaAclManager manager = null;
// -------------------------- STATIC METHODS --------------------------

    public static Test suite() {
        return new TestSuite(JahiaAclManagerTest.class);
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public JahiaAclManagerTest(String name) {
        super(name);
        String[] paths = {"spring/applicationContext*.xml"};
        log.debug("initialize test");
        ctx = new ClassPathXmlApplicationContext(paths);
    }

// -------------------------- OTHER METHODS --------------------------

    public void setUp() throws Exception {
        super.setUp();
        manager = (JahiaAclManager) ctx.getBean(JahiaAclManager.class.getName());
        assertNotNull(manager);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetAllAcls() throws Exception {
        testSaveAcl();
        List list = manager.getAllAcls();
        assertNotNull(list);
        assertTrue(list.size()>0);
    }

    public void testSaveAcl() throws Exception {
        JahiaAcl aclParent = new JahiaAcl();
        aclParent.setParent(manager.findJahiaAclById("2"));
        aclParent.setInheritance(new Integer(ACLInfo.INHERITANCE));
        manager.saveAcl(aclParent);
        assertTrue(aclParent.getId()!=null);
        JahiaAcl acl = new JahiaAcl();
        acl.setParent(aclParent);
        acl.setInheritance(new Integer(ACLInfo.INHERITANCE));
        manager.saveAcl(acl);
        assertTrue(acl.getId()!=null);
        Integer id = acl.getId();
        acl = null;
        assertNull(acl);
        acl = manager.findJahiaAclById(id.toString());
        assertNotNull(acl.getParent());
        JahiaAcl subChild = new JahiaAcl();
        subChild.setParent(acl);
        subChild.setInheritance(new Integer(ACLInfo.INHERITANCE));
        manager.saveAcl(subChild);
        assertTrue(subChild.getId()!=null);
        Integer subChildId = subChild.getId();
        acl = null;
        assertNull(acl);
        acl = manager.findJahiaAclById(subChildId.toString());
        assertNotNull(acl.getParent().getParent());
    }

    public void testUpdate() throws Exception {
        JahiaAcl acl = new JahiaAcl();
        acl.setParent(null);
        acl.setInheritance(new Integer(ACLInfo.INHERITANCE));
        JahiaAclEntry entry = new JahiaAclEntry(new JahiaAclEntryPK(acl, new Integer(ACLInfo.USER_TYPE_ENTRY),"guest:1"),0,0);
        Map userEntries = new HashMap(1);
        userEntries.put(entry.getComp_id().getTarget(),entry);
        acl.setUserEntries(userEntries);
        manager.saveAcl(acl);
        assertTrue(acl.getId()!=null);
        Integer id = acl.getId();
        entry = new JahiaAclEntry(new JahiaAclEntryPK(acl, new Integer(ACLInfo.USER_TYPE_ENTRY),"cedric:1"),0,0);
        acl.getUserEntries().put(entry.getComp_id().getTarget(),entry);
        ((JahiaAclEntry)acl.getUserEntries().get("guest:1")).setEntryState(1);
        manager.update(acl);
        acl = null;
        assertNull(acl);
        acl = manager.findJahiaAclById(id.toString());
        assertNotNull(acl);
        assertNotNull(acl.getUserEntries());
        assertTrue(acl.getUserEntries().size()==2);
        assertTrue(((JahiaAclEntry)acl.getUserEntries().get("guest:1")).getEntryState()==1);
    }

    public void testFindAllContainerAclsIdInSite() throws Exception {
        List list = manager.findAllContainerAclsIdInSite(1);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}

