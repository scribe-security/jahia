<%@page language = "java" %>
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

<%@page import="org.jahia.bin.*" %>
<%@page import = "java.util.*" %>
<%@page import = "org.jahia.data.JahiaData" %>
<%@taglib uri="http://www.jahia.org/tags/internalLib" prefix="internal" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<utility:setBundle basename="JahiaInternalResources" useUILocale="true"/><jsp:useBean id="URL" class="java.lang.String" scope="request"/><% // http files path. %>
<%
String groupName = (String)request.getAttribute("groupName");
List groupMembership = (List)request.getAttribute("groupMembership");
JahiaData jData = (JahiaData)request.getAttribute("org.jahia.data.JahiaData");
int stretcherToOpen   = 1; %>
<!-- Adiministration page position -->
<div id="topTitle">
  <h1>Jahia</h1>
  <h2 class="edit"><fmt:message key="org.jahia.admin.viewGroupMemberships.label"/></h2>
</div>
<div id="main">
  <table style="width: 100%;" class="dex-TabPanel" cellpadding="0" cellspacing="0">
    <tbody>
      <tr>
        <td style="vertical-align: top;" align="left">
          <%@include file="/admin/include/tab_menu.inc" %>
        </td>
      </tr>
      <tr>
        <td style="vertical-align: top;" align="left" height="100%">
        <div class="dex-TabPanelBottom">
        <div class="tabContent">
            <jsp:include page="/admin/include/left_menu.jsp">
                <jsp:param name="mode" value="site"/>
            </jsp:include>
          <div id="content" class="fit">
            <div class="head">
              <div class="object-title">
                <fmt:message key="org.jahia.admin.membershipsForGroup.label"/>&nbsp;: <%= groupName %>
              </div>
            </div><!-- View group membership on other sites --><!-- For future version : <script language="javascript" src="../search_options.js"></script> -->
            <table border="0" cellspacing="0" class="evenOddTable" style="width:100%">
              <thead>
                <tr>
                  <th>
                    <fmt:message key="label.username"/>
                  </th>
                  <th style="text-align:right">
                    <fmt:message key="org.jahia.admin.homeSite.label"/>&nbsp;&nbsp;
                  </th>
                </tr>
              </thead>
              <tbody>
                <%
                Iterator it = groupMembership.iterator();
                if (!it.hasNext()) { %>
                <tr>
                  <td colspan="2">
                    <fmt:message key="org.jahia.admin.noMembershipFromOtherSites.label"/>
                  </td>
                </tr><%
                } else {
                String lineClass="oddLine";
                while (it.hasNext()) {
                String groupMembers = (String)it.next();
                String memberSite = (String)it.next();
                if ("oddLine".equals(lineClass)) {
                lineClass="evenLine";
                } else {
                lineClass="oddLine";
                } %>
                <tr class="<%=lineClass%>">
                  <td>
                    <%= groupMembers %>
                  </td>
                  <td style="text-align:right">
                    <%= memberSite %>&nbsp;&nbsp;
                  </td>
                </tr>
                <%
                }
                } %>
              </tbody>
            </table>
          </div>
        </div>
        </td>
      </tr>
    </tbody>
  </table>
</div>
<div id="actionBar">
  <span class="dex-PushButton">
    <span class="first-child">
      <a class="ico-back" href='<%=JahiaAdministration.composeActionURL(request,response,"groups","&sub=display")%>'><fmt:message key="label.backToGroupList"/></a>
    </span>
  </span>
</div>
</div>