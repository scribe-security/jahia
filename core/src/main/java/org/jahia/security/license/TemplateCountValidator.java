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
 package org.jahia.security.license;

import java.util.Iterator;
import org.jahia.registries.ServicesRegistry;
import org.jahia.services.sites.JahiaSite;
import org.jahia.exceptions.JahiaException;
import org.jahia.resourcebundle.ResourceMessage;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Jahia Ltd</p>
 * @author Serge Huber
 * @version 1.0
 */

public class TemplateCountValidator extends AbstractValidator {

    private static org.apache.log4j.Logger logger =
        org.apache.log4j.Logger.getLogger(TemplateCountValidator.class);

    public TemplateCountValidator(String name, String value, License license) {
        super(name, value, license);
    }
    public boolean assertEquals(String value) {

        int maxTemplates = Integer.parseInt(value);

        // Check if the number of users is not exceeding the fixed limit
        try {

            // get all the list of site
            Iterator enumeration = ServicesRegistry.getInstance()
                                                .getJahiaSitesService()
                                                .getSites();
            JahiaSite aSite = null;
            int nbItems = 0;
            while( enumeration.hasNext() ){
                aSite = (JahiaSite)enumeration.next();

                nbItems = ServicesRegistry.getInstance().
                                           getJahiaPageTemplateService().
                                           getNbPageTemplates (aSite.getID());


                if (nbItems > maxTemplates) {
                    errorMessage = new ResourceMessage("org.jahia.security.license.TemplateCountValidator.invalidTemplateCount.label", new Integer(nbItems), new Integer(maxTemplates), new Integer(aSite.getID()));
                    return false;
                }
            }

        } catch (JahiaException ex) {
            logger.error("Error while checking template limit", ex);
            errorMessage = new ResourceMessage("org.jahia.security.license.TemplateCountValidator.errorInTemplateCountCheck.label");
            return false;
        }
        return true;
    }

    public boolean assertInRange(String fromValue, String toValue) {

        int minTemplates = Integer.parseInt(fromValue);
        int maxTemplates = Integer.parseInt(toValue);

        // Check if the number of users is not exceeding the fixed limit
        try {

            // get all the list of site
            Iterator enumeration = ServicesRegistry.getInstance()
                                                .getJahiaSitesService()
                                                .getSites();
            JahiaSite aSite = null;
            int nbItems = 0;
            while( enumeration.hasNext() ){
                aSite = (JahiaSite)enumeration.next();

                nbItems = ServicesRegistry.getInstance().
                                           getJahiaPageTemplateService().
                                           getNbPageTemplates (aSite.getID());
                if ((nbItems > maxTemplates) || (nbItems < minTemplates)) {
                    errorMessage = new ResourceMessage("org.jahia.security.license.TemplateCountValidator.templateCountNotInRange.label", new Integer(nbItems), new Integer(minTemplates), new Integer(maxTemplates), new Integer(aSite.getID()));
                    return false;
                }
            }

        } catch (JahiaException ex) {
            logger.error("Error while checking template limit", ex);
            errorMessage = new ResourceMessage("org.jahia.security.license.TemplateCountValidator.errorInTemplateCountCheck.label");
            return false;
        }
        return true;
    }

}