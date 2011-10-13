package org.jahia.services.importexport.validation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.jahia.api.Constants;
import org.xml.sax.Attributes;

/**
 * Validator that gets the list of all sites and sites properties from the xml import file.
 */
public class SitesValidator implements ImportValidator {

    private Map<String, Properties> sitesProperties;

    public SitesValidator() {
        sitesProperties = new LinkedHashMap<String, Properties>();
    }

    public ValidationResult getResult() {
        return new SitesValidatorResult(sitesProperties);
    }

    public void validate(String decodedLocalName, String decodedQName, String currentPath, Attributes atts) {

        String pt = atts.getValue(Constants.JCR_PRIMARYTYPE);

        if (pt != null && pt.equals(Constants.JAHIANT_VIRTUALSITE)) {
            Properties properties = new Properties();
            sitesProperties.put(decodedQName, properties);
        }
    }

    public Map<String, Properties> getSitesProperties() {
        return sitesProperties;
    }
}
