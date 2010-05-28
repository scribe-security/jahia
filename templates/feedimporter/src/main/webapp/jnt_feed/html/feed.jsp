<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<template:addResources type="css" resources="feed.css"/>

<c:set var="currentList" value="${currentNode.nodes}" scope="request"/>
<c:forEach items="${currentList}" var="subchild" varStatus="status" begin="${begin}" end="${end}">
    <div class="feed feed-box-style${(status.index mod 2)+1}">
        <template:module node="${subchild}" template="default"/>
    </div>

    <form action="${url.base}${currentNode.path}.getfeed.do" method="post">
        <input type="submit" name="submit" value="Refresh feed"/>
    </form>
</c:forEach>
