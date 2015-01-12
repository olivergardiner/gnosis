<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
<portlet:resourceURL var="iconURL" id="icon"/>
<portlet:resourceURL var="pptxURL" id="pptx"/>
<portlet:resourceURL var="docxURL" id="docx"/>
<portlet:resourceURL var="actionURL" id="action"/>

<script type="text/javascript">
var jsonDataURL = "<%= jsonDataURL %>";
var iconURL = "<%= iconURL %>";
var pptxURL = "<%= pptxURL %>";
var docxURL = "<%= docxURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;">
			<div style="display: table-row;" id="operating-model" class="om-layer">
			</div>
		</div>
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;">
			<div style="display: table-row;" id="business-applications-primary" class="ba-layer">
			</div>
		</div>
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;">
			<div style="display: table-row;" id="business-applications-support" class="ba-layer">
			</div>
		</div>
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;">
			<div style="display: table-row;" id="common-services" class="cs-layer">
			</div>
		</div>
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;">
			<div style="display: table-row;" id="infrastructure" class="inf-layer">
			</div>
		</div>
		<br/>
		<div>Download: <button id="pptx-download" style="display: none">PPTX</button><button id="docx-download" style="display: none">DOCX</button></div>
	</div>
	<br />
</div>
