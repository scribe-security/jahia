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
<%@ tag body-content="empty" description="Renders the link to the path selection engine (as a popup window)." %>
<%@ attribute name="fieldId" required="true" type="java.lang.String"
              description="The input field name and ID to synchronize the selected path value with" %>
<%@ attribute name="useUrl" required="false" type="java.lang.Boolean"
              description="If set to true the selected folder URL will be used in the field value; otherwise the path of the selected folder will be used (default)." %>
<%@ attribute name="rootPath" required="false" type="java.lang.String"
              description="The path to start with. So the selection will be available for subfolders of the specified root directory" %>
<%@ attribute name="startPath" required="false" type="java.lang.String"
              description="The path of the directory that will be expanded by default" %>
<%@ attribute name="filters" required="false" type="java.lang.String"
              description="Comma-separated list of filter patterns to be applied on the displayed resources If both filters and mimeTypes are specified they are applied one after another." %>
<%@ attribute name="mimeTypes" required="false" type="java.lang.String"
              description="Comma-separated list of MIME types for files to be displayed. If both filters and mimeTypes are specified they are applied one after another." %>
<%@ attribute name="onSelect" required="false" type="java.lang.String"
              description="The JavaScript function to be called after a location is selected. The selected path will be passed as an argument to this function. If the function returns true, the value will be also set into the field value. Otherwise nothing will be done by this tag." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="fieldIdHash"><%= Math.abs(jspContext.getAttribute("fieldId").hashCode()) %>
</c:set>
<c:set var="useUrl" value="${not empty useUrl ? useUrl : 'false'}"/>
&nbsp;<a href="#select"
onclick="javascript:{var fileSelector = window.open('${pageContext.request.contextPath}/engines/webdav/filePicker.jsp?callback=setSelectedFile${fieldIdHash}&amp;rootPath=${rootPath}&amp;startPath=${startPath}&amp;filters=${filters}&amp;mimeTypes=${mimeTypes}', '<%="fileSelector" + session.getId().replaceAll("[^a-zA-Z0-9]", "_")%>', 'resizable,height=800,width=800'); fileSelector.focus(); return false;}"
title='<fmt:message key="selectors.fileSelector.selectFile"/>'><fmt:message key="selectors.select"/></a>
<script type="text/javascript">
    function setSelectedFile${fieldIdHash}(path, url) {
    <c:if test="${not empty onSelect}">
        if ((${onSelect})(path, url)) {
            document.getElementById('${fieldId}').value = ${useUrl} ? url : path;
        }
    </c:if>
    <c:if test="${empty onSelect}">
        document.getElementById('${fieldId}').value = ${useUrl} ? url : path;
    </c:if>
    }
</script>