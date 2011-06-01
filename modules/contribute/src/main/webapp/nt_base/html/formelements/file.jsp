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
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="propertyDefinition" type="org.jahia.services.content.nodetypes.ExtendedPropertyDefinition"--%>
<%--@elvariable id="type" type="org.jahia.services.content.nodetypes.ExtendedNodeType"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="selectorType" type="org.jahia.services.content.nodetypes.SelectorType"--%>
<template:addResources type="javascript" resources="jquery.js,jquery.jeditable.js"/>
<template:addResources type="javascript" resources="jquery.jeditable.ajaxupload.js"/>
<template:addResources type="javascript" resources="jquery.ajaxfileupload.js"/>
<template:addResources type="javascript" resources="jquery.defer.js"/>
<label for="file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}">${jcr:labelInNodeType(propertyDefinition,renderContext.mainResourceLocale,type)}</label>
<input type="hidden" name="${propertyDefinition.name}" id="${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}"/>
<fmt:message key="label.select.file" var="fileLabel"/>
<c:url value="${url.files}" var="previewPath"/>
<c:set var="onSelect">function(uuid, path, title) {
            $('#${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}').val(uuid);
            $('#display${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}').html('<img src="${previewPath}'+path+'"/>');
            return false;
        }</c:set>
<c:set var="onClose">$.defer( 200, function() {
            $.fancybox({
                'content':$('.FormContribute'),
                'height':600,
                'width':600,
                'autoScale':false,
                'autoDimensions':false,
                'onComplete':function() {
                    $("#treepreview").remove();
                    $(".newContentCkeditorContribute${currentNode.identifier}${fn:replace(resourceNodeType,':','_')}").each(function() { $(this).ckeditor(); $(this).data('ckeditorInstance').checkWCAGCompliance=wcagCompliant; })
                },

                'onCleanup':function() {
                    $("#treepreview").remove();
                    $(".newContentCketempditorContribute${currentNode.identifier}${fn:replace(resourceNodeType,':','_')}").each(function() { if ($(this).data('ckeditorInstance')) { $(this).data('ckeditorInstance').destroy()  } });
                }
             }
            );
        })</c:set>
<c:set var="fancyboxOptions">{
            onStart: function() {
                $(".newContentCkeditorContribute${currentNode.identifier}${fn:replace(resourceNodeType,':','_')}").each(function() { if ($(this).data('ckeditorInstance')) { $(this).data('ckeditorInstance').destroy()  } });
                $('#addNewContent').append($('.FormContribute'))
            }
        }</c:set>
<c:if test="${propertyDefinition.selectorOptions.type == 'image'}">
<ui:fileSelector fieldId="${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}"
                 displayFieldId="file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}" valueType="identifier"
        label="${fileLabel}"
        nodeTypes="nt:folder,jmix:image,jnt:virtualsite"
        selectableNodeTypes="jmix:image"
        onSelect="${onSelect}"
        onClose="${onClose}"
        fancyboxOptions="${fancyboxOptions}" treeviewOptions="{preview:true,previewPath:'${previewPath}'}"/>
</c:if>
<c:if test="${propertyDefinition.selectorOptions.type != 'image'}">
<ui:fileSelector fieldId="${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}"
                 displayFieldId="file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}" valueType="identifier"
        label="${fileLabel}"
        onSelect="${onSelect}"
        onClose="${onClose}"
        fancyboxOptions="${fancyboxOptions}" treeviewOptions="{preview:true,previewPath:'${previewPath}'}"/>
</c:if>
<span><fmt:message key="label.or"/></span>
<div id="file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}" jcr:id="${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}">
    <span><fmt:message key="add.file"/></span>
</div>
<div id="display${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}" jcr:id="${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}">
</div>
<template:addResources>
<script>
    $(document).ready(function() {
        $("#file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}").editable('<c:url value="${url.base}${param['path'] == null ? renderContext.mainResource.node.path : param['path']}"><c:param name="jcrContributePost" value="true"/></c:url>', {
            type : 'ajaxupload',
            onblur : 'ignore',
            submit : 'OK',
            cancel : 'Cancel',
            submitdata : {'jcrContributePost':'true'},
            tooltip : 'Click to edit',
            callback : function (data, status,original) {
                var id = $(original).attr('jcr:id');
                $("#"+id).val(data.uuids[0]);
                $("#display${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}").html("<img src='"+data.urls[0]+"'/>");
                $("#file${scriptTypeName}${fn:replace(propertyDefinition.name,':','_')}").html('<span><fmt:message key="add.file"/></span>');
            }
        });
    });
</script>
</template:addResources>