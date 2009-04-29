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

<%@ include file="../../declarations.jspf" %>
<template:containerList name="${boxID}_portletContainer" id="portlets"
                       actionMenuNamePostFix="portlets" actionMenuNameLabelKey="portlets.add">
    <template:container id="portletsContainer" actionMenuNamePostFix="portlet" actionMenuNameLabelKey="portlet.update">
        <template:field name='portlet' var="portlet" display="false"/>
        <c:if test="${!empty portlet.field.object}">
            <c:set var="portletWindowBean" value="${portlet.field.object}" />
            <ui:portletModes name="portletWindowBean"/>
            <br/>
            <c:out escapeXml="false" value="${portlet.field.value}"/>
        </c:if>
    </template:container>
</template:containerList>
