package org.jahia.bundles.securityfilter.core;

import org.jahia.services.securityfilter.ScopeDefinition;
import org.jahia.bundles.securityfilter.core.apply.AutoApply;
import org.jahia.bundles.securityfilter.core.constraint.Constraint;
import org.jahia.bundles.securityfilter.core.grant.Grant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

public class ScopeDefinitionImpl implements ScopeDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ScopeDefinitionImpl.class);

    private String pid;
    private String scopeName;
    private String description;
    private Collection<AutoApply> apply;
    private Collection<Constraint> constraints;
    private Collection<Grant> grants;
    private Map<String, String> metadata;

    public ScopeDefinitionImpl(String pid, String scopeName, String description, Collection<AutoApply> apply, Collection<Constraint> constraints, Collection<Grant> grants, Map<String, String> metadata) {
        this.pid = pid;
        this.scopeName = scopeName;
        this.description = description;
        this.apply = apply;
        this.constraints = constraints;
        this.grants = grants;
        this.metadata = metadata;
    }

    public String getPid() {
        return pid;
    }

    public String getScopeName() {
        return scopeName;
    }

    public String getDescription() {
        return description;
    }

    public Collection<AutoApply> getApply() {
        return apply;
    }

    public Collection<Constraint> getConstraints() {
        return constraints;
    }

    public Collection<Grant> getGrants() {
        return grants;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public boolean shouldAutoApply(HttpServletRequest request) {
        for (AutoApply autoApply : apply) {
            if (autoApply.shouldApply(request)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(HttpServletRequest request) {
        for (Constraint constraint : constraints) {
            if (!constraint.isValid(request)) {
                return false;
            }
        }

        return true;
    }

    public boolean isGrantAccess(Map<String, Object> query) {
        for (Grant grant : grants) {
            if (grant.matches(query)) {
                logger.debug("Access granted for {} by scope {} (grant {})", query, scopeName, grant);
                return true;
            }
        }
        return false;
    }

}
