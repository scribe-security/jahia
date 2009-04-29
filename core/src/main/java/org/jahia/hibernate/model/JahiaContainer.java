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
 package org.jahia.hibernate.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @hibernate.class table="jahia_ctn_entries" batch-size="50" lazy="true" 
 * @hibernate.cache usage="nonstrict-read-write"
 */
@Entity
@Table (name = "jahia_ctn_entries")
public class JahiaContainer implements Serializable, Cloneable {
// ------------------------------ FIELDS ------------------------------

    /**
     * Used for projection, refers to comp_id.id
     */
    private Integer ctnId;

    /**
     * nullable persistent field
     */
    private Integer listid;

    /**
     * nullable persistent field
     */
    private Integer pageid;

    /**
     * nullable persistent field
     */
    private Integer rank;
    /**
     * nullable persistent field
     */
    private JahiaCtnDef ctndef;

    /**
     * nullable persistent field
     */
    private Integer siteId;

    /**
     * persistent field
     */

    private Integer jahiaAclId;

    /**
     * identifier field
     */
    private org.jahia.hibernate.model.JahiaCtnEntryPK comp_id;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * persistent field
     */

//    private Set jahiaCtnLists;
    /**
     * default constructor
     */
    public JahiaContainer() {
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * @hibernate.id generator-class="assigned"
     */
    public org.jahia.hibernate.model.JahiaCtnEntryPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(org.jahia.hibernate.model.JahiaCtnEntryPK comp_id) {
        this.comp_id = comp_id;
    }

    /**
     * @hibernate.property name="ctnId"
     * column="id_jahia_ctn_entries" unique="false" 
     * update="false" insert="false"
     */
    public Integer getCtnId() {
        return this.ctnId;
    }

    public void setCtnId(Integer ctnId) {
        this.ctnId = ctnId;
    }

    /**
     * @hibernate.many-to-one
     * @hibernate.column name="ctndefid_jahia_ctn_entries"
     */
    public JahiaCtnDef getCtndef() {
        return this.ctndef;
    }

    public void setCtndef(JahiaCtnDef ctndef) {
        this.ctndef = ctndef;
    }

    /**
     * @hibernate.property name="jahiaAclId"
     * column="rights_jahia_ctn_entries"
     */
    public Integer getJahiaAclId() {
        return this.jahiaAclId;
    }

    public void setJahiaAclId(Integer jahiaAclId) {
        this.jahiaAclId = jahiaAclId;
    }

    /**
     * @hibernate.property column="listid_jahia_ctn_entries"
     * length="11"
     */
    public Integer getListid() {
        return this.listid;
    }

    public void setListid(Integer listid) {
        this.listid = listid;
    }

    /**
     * @hibernate.property column="pageid_jahia_ctn_entries"
     * length="11"
     */
    public Integer getPageid() {
        return this.pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    /**
     * @hibernate.property column="rank_jahia_ctn_entries"
     * length="11"
     */
    public Integer getRank() {
        return this.rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * @hibernate.property column="jahiaid_jahia_ctn_entries"
     * length="11"
     */
    public Integer getSiteId() {
        return this.siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

// ------------------------ CANONICAL METHODS ------------------------

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object <tt>x</tt>, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be <tt>true</tt>, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be <tt>true</tt>, this is not an absolute requirement.
     * <p/>
     * By convention, the returned object should be obtained by calling
     * <tt>super.clone</tt>.  If a class and all of its superclasses (except
     * <tt>Object</tt>) obey this convention, it will be the case that
     * <tt>x.clone().getClass() == x.getClass()</tt>.
     * <p/>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by <tt>super.clone</tt> before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by <tt>super.clone</tt>
     * need to be modified.
     * <p/>
     * The method <tt>clone</tt> for class <tt>Object</tt> performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface <tt>Cloneable</tt>, then a
     * <tt>CloneNotSupportedException</tt> is thrown. Note that all arrays
     * are considered to implement the interface <tt>Cloneable</tt>.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     * <p/>
     * The class <tt>Object</tt> does not itself implement the interface
     * <tt>Cloneable</tt>, so calling the <tt>clone</tt> method on an object
     * whose class is <tt>Object</tt> will result in throwing an
     * exception at run time.
     *
     * @return a clone of this instance.
     *
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the <code>Cloneable</code> interface. Subclasses
     *                                    that override the <code>clone</code> method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    public Object clone() throws CloneNotSupportedException {
        JahiaContainer container = new JahiaContainer();
        container.setComp_id((JahiaCtnEntryPK) this.getComp_id().clone());
        container.setCtndef(this.getCtndef());
        container.setJahiaAclId(this.getJahiaAclId());
        container.setListid(this.getListid());
        container.setPageid(this.getPageid());
        container.setRank(this.getRank());
        container.setSiteId(this.getSiteId());
        return container;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        
        if (obj != null && this.getClass() == obj.getClass()) {
            final JahiaContainer castOther = (JahiaContainer) obj;
            return new EqualsBuilder()
                .append(this.getComp_id(), castOther.getComp_id())
                .isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getComp_id())
                .toHashCode();
    }

    public String toString() {
        return new StringBuffer(getClass().getName())
                .append("comp_id="+getComp_id())
                .toString();
    }
}

