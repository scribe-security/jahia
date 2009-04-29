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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.io.Serializable;

/**
 * @author Hibernate CodeGenerator
 */
public class JahiaCtnListPK extends CachedPK implements Serializable,Cloneable {

    /**
     * identifier field
     */
    private Integer id;

    /**
     * identifier field
     */
    private Integer versionId;

    /**
     * identifier field
     */
    private Integer workflowState;

    /**
     * full constructor
     */
    public JahiaCtnListPK(Integer id, Integer versionId, Integer workflowState) {
        this.id = id;
        this.versionId = versionId;
        this.workflowState = workflowState;
    }

    /**
     * default constructor
     */
    public JahiaCtnListPK() {
    }

    /**
     * @hibernate.property column="id_jahia_ctn_lists"
     * length="11"
     */
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        updated();
        this.id = id;
    }

    /**
     * @hibernate.property column="version_id"
     * length="20"
     */
    public Integer getVersionId() {
        return this.versionId;
    }

    public void setVersionId(Integer versionId) {
        updated();
        this.versionId = versionId;
    }

    /**
     * @hibernate.property column="workflow_state"
     * length="11"
     */
    public Integer getWorkflowState() {
        return this.workflowState;
    }

    public void setWorkflowState(Integer workflowState) {
        updated();
        this.workflowState = workflowState;
    }

    public String effectiveToString() {
        return new StringBuffer(getClass().getName())
                .append("id="+getId())
                .append("versionId="+getVersionId())
                .append("workflowState="+getWorkflowState())
                .toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj != null && this.getClass() == obj.getClass()) {
            final JahiaCtnListPK castOther = (JahiaCtnListPK) obj;
            return new EqualsBuilder()
                .append(this.getId(), castOther.getId())
                .append(this.getVersionId(), castOther.getVersionId())
                .append(this.getWorkflowState(), castOther.getWorkflowState())
                .isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getVersionId())
                .append(getWorkflowState())
                .toHashCode();
    }

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
    protected Object clone() throws CloneNotSupportedException {
        JahiaCtnListPK pk = new JahiaCtnListPK(this.getId(),
                this.getVersionId(), this.getWorkflowState());
        return pk;    //To change body of overridden methods use File | Settings | File Templates.
    }
}
