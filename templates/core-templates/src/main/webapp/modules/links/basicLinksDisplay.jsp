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
<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<%@ include file="../../common/declarations.jspf" %>
<template:containerList name="basicLink${param.id}" id="links" displayActionMenu="false">
    <ul class="${param.cssClassName}">
        <template:container id="linkContainer" displayActionMenu="false">
            <li>
                <ui:actionMenu contentObjectName="linkContainer" namePostFix="link" labelKey="link.update">
                    <template:field name="link" maxChar="20"/>
                </ui:actionMenu>
            </li>
        </template:container>
        <c:if test="${requestScope.currentRequest.editMode}">
            <li><ui:actionMenu contentObjectName="links" namePostFix="links" labelKey="links.add"/></li>
        </c:if>
    </ul>
</template:containerList>
