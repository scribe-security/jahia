<%--

    Jahia Enterprise Edition v6

    Copyright (C) 2002-2009 Jahia Solutions Group. All rights reserved.

    Jahia delivers the first Open Source Web Content Integration Software by combining Enterprise Web Content Management
    with Document Management and Portal features.

    The Jahia Enterprise Edition is delivered ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
    IMPLIED.

    Jahia Enterprise Edition must be used in accordance with the terms contained in a separate license agreement between
    you and Jahia (Jahia Sustainable Enterprise License - JSEL).

    If you are unsure which license is appropriate for your use, please contact the sales department at sales@jahia.com.

--%>
<%@ page language="java" %>
<%@ page import="org.jahia.params.ProcessingContext" %>
<%@ page import="org.jahia.services.lock.LockKey" %>
<%@ page import="org.jahia.services.lock.LockPrerequisites" %>
<%@ page import="org.jahia.services.lock.LockPrerequisitesResult" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="internal" uri="http://www.jahia.org/tags/internalLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<utility:setBundle basename="JahiaInternalResources"/>
<jsp:useBean id="jspSource" class="java.lang.String" scope="request"/>

<%
final ProcessingContext jParams = (ProcessingContext) request.getAttribute("org.jahia.params.ParamBean");
Map engineMap = (Map) request.getAttribute("org.jahia.engines.EngineHashMap");
if (engineMap == null) {
  engineMap = (Map) session.getAttribute("jahia_session_engineMap");
}
String engineName = (String) engineMap.get("engineName");
if (engineName == null) {
  engineName = "unknown";
}
final String theScreen = (String) engineMap.get("screen");

final String noApply = (String) engineMap.get("noApply");
final boolean showButtons = request.getAttribute("DisableButtons") == null;
LockKey lockKey = (LockKey) engineMap.get("LockKey");
final LockPrerequisitesResult results;
if (lockKey != null) {
  results = LockPrerequisites.getInstance().getLockPrerequisitesResult(lockKey);
} else {
  results = null;
}
LockKey engineLockKey = (LockKey)engineMap.get("lock");
%>
<!-- actionBar (start) -->
<div id="actionBar">
  <% if (showButtons) { %>
    <% if ("locks".equals(engineName)) { %>
      <span class="dex-PushButton">
        <span class="first-child">
          <a href="javascript:sendFormApply();" class="ico-ok" title="<fmt:message key="org.jahia.altApplyWithoutClose.label"/>" onclick="setWaitingCursor(1);">
            <fmt:message key="org.jahia.button.ok"/></a>
        </span>
      </span>
    <% } else if (!engineMap.containsKey("errorMessage") || (engineMap.get("errorMessage") == Boolean.FALSE)) { %>
      <% if (results == null) { %>
        <span class="dex-PushButton">
          <span class="first-child">
            <a href="javascript:sendFormSave();" class="ico-ok" title="<fmt:message key="org.jahia.altApplyAndClose.label"/>" onclick="setWaitingCursor(1);">
              <fmt:message key="org.jahia.button.ok"/></a>
          </span>
        </span>
      <% } %>
    <% } %>
    <% if (!"logs".equals(theScreen) && !"import".equals(theScreen) && !"workflow".equals(engineName) && !"deletecontainer".equals(engineName) && !"".equals(noApply)) { %>
      <% if (results != null) { %>
    <!--
    <span class="dex-PushButton">
          <span class="first-child">
            <fmt:message key="org.jahia.button.apply"/>
          </span>
        </span>
     -->
      <% } else { %>
        <span class="dex-PushButton">
          <span class="first-child">
            <a href="javascript:sendFormApply();" class="ico-apply" title="<fmt:message key="org.jahia.altApplyWithoutClose.label"/>" onclick="setWaitingCursor(1);">
              <fmt:message key="org.jahia.button.apply"/></a>
          </span>
        </span>
      <% } %>
    <% } %>
  <% } %>
  <span class="dex-PushButton">
    <span class="first-child">
      <a href="javascript:window.close();" class="ico-cancel" title="<fmt:message key="org.jahia.altCloseWithoutSave.label"/>">
        <fmt:message key="org.jahia.button.cancel"/></a>
    </span>
  </span>
</div>
<!-- actionBar (end) -->