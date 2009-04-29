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
 package org.jahia.services.importexport;

import org.jahia.content.ContentObject;
import org.jahia.content.ObjectKey;
import org.jahia.exceptions.JahiaException;
import org.jahia.params.ProcessingContext;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.lock.LockKey;
import org.jahia.services.scheduler.BackgroundJob;
import org.jahia.services.sites.JahiaSite;
import org.jahia.services.sites.SiteLanguageSettings;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.version.EntryLoadRequest;
import org.jahia.services.version.JahiaSaveVersion;
import org.jahia.services.version.StateModificationContext;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 25 oct. 2005 - 16:34:07
 *
 * @author toto
 * @version $Id$
 */
public class CopyJob extends BackgroundJob {
    public static final String COPYPASTE_TYPE = "copypaste";
    public static final String PICKERCOPY_TYPE = "pickercopy";

    public static final String SITESOURCE = "sitesource";
    public static final String DEST = "dest";
    public static final String SOURCE = "source";
    public static final String VERSION = "version";
    public static final String VERSION_CURRENT = "current";
    public static final String LINK = "link";
    public static final String VERSION_COMPLETE = "complete";

    public void executeJahiaJob(JobExecutionContext jobExecutionContext, ProcessingContext context) throws Exception {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        ContentObject source = ContentObject.getContentObjectInstance(ObjectKey.getInstance((String) jobDataMap.get(SOURCE)));
        ContentObject dest = ContentObject.getContentObjectInstance(ObjectKey.getInstance((String) jobDataMap.get(DEST)));
        String link = (String) jobDataMap.get(LINK);
        String version = (String) jobDataMap.get(VERSION);

        List<ImportAction> actions = new ArrayList<ImportAction>();
        ExtendedImportResult result = new ExtendedImportResult();

        EntryLoadRequest loadrequest = EntryLoadRequest.STAGED;

        if (VERSION_CURRENT.equals(version)) {
            loadrequest = EntryLoadRequest.CURRENT;
        }

        ContentObject imported = ServicesRegistry.getInstance().getImportExportService().copy(source, dest, context, loadrequest, link, actions, result);

        if (imported != null) {
            LockKey lock = LockKey.composeLockKey(LockKey.IMPORT_ACTION + "_" + imported.getObjectKey().getType(), imported.getID(), imported.getID());
            ((Set)jobDataMap.get(JOB_LOCKS)).add(lock);
        }

        jobDataMap.put(ACTIONS, actions);
        jobDataMap.put(RESULT, result);
    }

    private Set getSiteLanguages(JahiaSite site) throws JahiaException {
        Set languages = new HashSet();
        List v = site.getLanguageSettings(true);
        for (Iterator iterator = v.iterator(); iterator.hasNext();) {
            SiteLanguageSettings sls = (SiteLanguageSettings) iterator.next();
            languages.add(sls.getCode());
        }

        return languages;
    }


    private void activateAll(ContentObject o, Set languageCodes, boolean versioningActive, JahiaSaveVersion saveVersion, JahiaUser user, ProcessingContext jParams, StateModificationContext stateModifContext) throws JahiaException {
        List l = o.getChilds(null,null);
        for (Iterator iterator = l.iterator(); iterator.hasNext();) {
            ContentObject child = (ContentObject) iterator.next();
            activateAll(child, languageCodes, versioningActive, saveVersion, user, jParams, stateModifContext);
        }
        o.activate(languageCodes, versioningActive, saveVersion, user, jParams, stateModifContext);
    }
}
/**
 *$Log $
 */