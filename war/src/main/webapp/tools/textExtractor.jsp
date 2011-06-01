<%@ page contentType="text/html; charset=UTF-8" language="java"%> 
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

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="tools.css" type="text/css" />
<title>Jahia Text Extraction Service</title>
</head>
<body>
<h1>Jahia Text Extraction Service</h1>
<form id="extraction" action="${pageContext.request.contextPath}/cms/text-extract" enctype="multipart/form-data" method="post">
<p>
<label for="file">Choose a file to upload:&nbsp;</label><input name="file" id="file" type="file" />
</p>
<p><input type="submit" value="Extract content" /></p>
</form>
<c:if test="${extracted}">
<hr/>
<h2>Content extracted in ${extractionTime} ms</h2>
<fieldset>
    <legend><strong>Metadata</strong></legend>
    <c:forEach items="${metadata}" var="item">
        <p>
            <strong><c:out value="${item.key}"/>:&nbsp;</strong>
            <c:out value="${item.value}"/>
        </p>
    </c:forEach>
</fieldset>
<fieldset>
    <legend><strong>Content (${fn:length(content)} characters)</strong></legend>
    <p><c:out value="${content}"/></p>
</fieldset>
</c:if>
</body>
</html>