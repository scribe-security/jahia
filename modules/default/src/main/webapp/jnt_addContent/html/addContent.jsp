<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="uiComponents" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<template:addResources type="javascript" resources="jquery.fancybox.js"/>
<template:addResources type="css" resources="jquery.fancybox.css"/>
<c:set var="target" value="${uiComponents:getBindedComponent(currentNode, renderContext, 'j:bindedComponent')}"/>

<c:set var="types" value="${jcr:getContributeTypes(target, null, currentNode.properties['j:type'])}"/>

<c:forEach items="${types}" var="nodeType" varStatus="status">
    <a href="#add${currentNode.identifier}-${status.index}"  id="addButton${currentNode.identifier}-${status.index}">
        <fmt:message key="add"/> ${jcr:label(nodeType, renderContext.mainResourceLocale)}
    </a>

    <div style="display:none;">
        <div id="add${currentNode.identifier}-${status.index}"
             style="width:800px;">
            <template:module node="${target}" view="contribute.add">
                <template:param name="resourceNodeType" value="${nodeType.name}"/>
            </template:module>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#addButton${currentNode.identifier}-${status.index}").fancybox({
                'onComplete':function() {
                    $(".newContentCkeditorContribute${target.identifier}${fn:replace(nodeType.name,':','_')}").each(function() { $(this).ckeditor(); alert('ckeditorInstance: ' + $(this).data('ckeditorInstance')); alert('wcagCompliant: ' + wcagCompliant);})
                },

                'onCleanup':function() {
                    $(".newContentCkeditorContribute${target.identifier}${fn:replace(nodeType.name,':','_')}").each(function
                            () {
                        if ($(this).data('ckeditorInstance')) {
                            $(this).data('ckeditorInstance').destroy()
                        }
                    });
                }
            })
        });
    </script>
</c:forEach>
