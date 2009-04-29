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
 package org.jahia.hibernate.model.indexingjob;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.search.RemovableDocumentImpl;
import org.jahia.registries.ServicesRegistry;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hollis
 * Date: 28 juil. 2005
 * Time: 12:53:53
 * To change this template use File | Settings | File Templates.
 *
 * @hibernate.subclass discriminator-value="org.jahia.hibernate.model.indexingjob.JahiaRemoveFromIndexJob"
 */
public class JahiaRemoveFromIndexJob extends JahiaIndexingJob {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(JahiaRemoveFromIndexJob.class);

    private Integer siteId = new Integer(0);
    private String keyFieldName = "";
    private String keyFieldValue = "";

    public JahiaRemoveFromIndexJob(){
        super();
        this.setClassName(getClass().getName());
    }

    public JahiaRemoveFromIndexJob(int siteId, String keyFieldName,
                                   String keyFieldValue, long date)
    {
        this();
        this.setSiteId(new Integer(siteId));
        if ( keyFieldName != null ){
            this.setKeyFieldName(keyFieldName);
        }
        if ( keyFieldValue != null ){
            this.setKeyFieldValue(keyFieldValue);
        }
        this.setDate(new Long(date));
    }

    /**
     * @hibernate.property column="siteid_indexingjob"
     * @return
     */
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * @hibernate.property column="keyname_indexingjob"
     * @return
     */
    public String getKeyFieldName() {
        return keyFieldName;
    }

    public void setKeyFieldName(String keyFieldName) {
        this.keyFieldName = keyFieldName;
    }

    /**
     * @hibernate.property column="keyvalue_indexingjob"
     * @return
     */
    public String getKeyFieldValue() {
        return keyFieldValue;
    }

    public void setKeyFieldValue(String keyFieldValue) {
        this.keyFieldValue = keyFieldValue;
    }

    /**
     * by default, return true
     *
     * @return true if the job is executable. If false, the job should be discarded
     */
    public boolean isValid(){
        return true;
    }

    public void execute(JahiaUser user){
        try {
            ServicesRegistry.getInstance().getJahiaSearchService()
                .removeFromSearchEngine(siteId.intValue(), keyFieldName,
                        keyFieldValue, user, false, false, null);
        } catch ( Exception t){
            logger.debug("Exception occured when performing indexation job",t);
        }
    }

    public void prepareBatchIndexation(List toRemove, List toAdd, JahiaUser user){
        RemovableDocumentImpl doc =
                new RemovableDocumentImpl(keyFieldName, keyFieldValue);
        toRemove.add(doc);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj != null && this.getClass() == obj.getClass()) {
            final JahiaRemoveFromIndexJob castOther = (JahiaRemoveFromIndexJob) obj;
            return new EqualsBuilder()
                .append(this.getId(), castOther.getId())
                .isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getId())
                .toHashCode();
    }

    public String toString(){
        return new StringBuffer(getClass().getName())
                .append("id="+getId())
                .toString();
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
    public Object clone() throws CloneNotSupportedException {
        JahiaRemoveFromIndexJob job = new JahiaRemoveFromIndexJob();
        job.setId(this.getId());
        job.setDate(this.getDate());
        job.setIndexImmediately(this.getIndexImmediately());
        job.setRuleId(this.getRuleId());
        job.setScheduledFromTime1(this.getScheduledFromTime1());
        job.setScheduledToTime1(this.getScheduledToTime1());
        job.setScheduledFromTime2(this.getScheduledFromTime2());
        job.setScheduledToTime2(this.getScheduledToTime2());
        job.setScheduledFromTime3(this.getScheduledFromTime3());
        job.setScheduledToTime3(this.getScheduledToTime3());
        job.setEnabledIndexingServers(this.getEnabledIndexingServers());
        job.setSiteId(this.getSiteId());
        job.setKeyFieldName(this.getKeyFieldName());
        job.setKeyFieldValue(this.getKeyFieldValue());
        job.setClassName(this.getClassName());
        return job;
    }

}
