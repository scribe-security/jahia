<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%--

    This file is part of Jahia: An integrated WCM, DMS and Portal Solution
    Copyright (C) 2002-2010 Jahia Solutions Group SA. All rights reserved.

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

    As a special exception to the terms and conditions of version 2.0 of
    the GPL (or any later version), you may redistribute this Program in connection
    with Free/Libre and Open Source Software ("FLOSS") applications as described
    in Jahia's FLOSS exception. You should have received a copy of the text
    describing the FLOSS exception, and it is also available here:
    http://www.jahia.com/license

    Commercial and Supported Versions of the program
    Alternatively, commercial and supported versions of the program may be used
    in accordance with the terms contained in a separate written agreement
    between you and Jahia Solutions Group SA. If you are unsure which license is appropriate
    for your use, please contact the sales department at sales@jahia.com.

--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<template:include view="hidden.header"/>
<c:set var="isEmpty" value="true"/>
<c:choose>
    <c:when test="${moduleMap.liveOnly eq 'true' && !renderContext.liveMode}">
        <c:if test="${renderContext.editModeConfigName eq 'studiomode'}">
            <div class="area-liveOnly">
                <fmt:message key="label.content.creation.only.live"/>
            </div>
        </c:if>
        <c:if test="${!(renderContext.editModeConfigName eq 'studiomode')}">
            <template:addResources type="javascript" resources="jquery.js"/>
            <div id="liveList${currentNode.identifier}"></div>
            <script type="text/javascript">
                $('#liveList${currentNode.identifier}').load('<c:url value="${url.baseLive}${currentNode.path}.html.ajax"/>');
            </script>
        </c:if>
    </c:when>
    <c:otherwise>
        <c:forEach items="${moduleMap.currentList}" var="subchild" begin="${moduleMap.begin}" end="${moduleMap.end}">
            <template:module node="${subchild}" view="${moduleMap.subNodesView}" editable="${moduleMap.editable}"/>
            <c:set var="isEmpty" value="false"/>
        </c:forEach>
        <c:if test="${not omitFormatting}"><div class="clear"></div></c:if>
        <c:if test="${moduleMap.editable and renderContext.editMode}">
            <template:module path="*"/>
        </c:if>
        <c:if test="${not empty moduleMap.emptyListMessage and renderContext.editMode and isEmpty}">
            ${moduleMap.emptyListMessage}
        </c:if>
        <template:include view="hidden.footer"/>
    </c:otherwise>
</c:choose>
