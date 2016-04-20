<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>

<%@ page contentType="text/html" isELIgnored="false" %> 

<portlet:defineObjects/> 
<liferay-theme:defineObjects/>
<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<%
String layer_cfg = portletPreferences.getValue("layer", "BusinessApplications");
%>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
    <aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

    <%-- <aui:input name="preferences--layer--" type="text" value="<%= layer_cfg %>" /> --%>

	<aui:select name="preferences--layer--" label="Layer">
		<aui:option label="Business Applications" value="BusinessApplications" selected="<%= layer_cfg.equals(\"BusinessApplications\") %>"></aui:option>
		<aui:option label="Common Services" value="CommonServices" selected="<%= layer_cfg.equals(\"CommonServices\") %>"></aui:option>
		<aui:option label="Infrastructure" value="Infrastructure" selected="<%= layer_cfg.equals(\"Infrastructure\") %>"></aui:option>
	</aui:select>

    <aui:button-row>
       <aui:button type="submit" />
    </aui:button-row>
</aui:form>