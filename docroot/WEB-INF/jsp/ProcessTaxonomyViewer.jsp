<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
<portlet:resourceURL var="valueChainJsonDataURL" id="valueChainJsonData"/>
<portlet:resourceURL var="iconURL" id="icon"/>
<portlet:resourceURL var="actionURL" id="action"/>

<script type="text/javascript">
var jsonDataURL = "<%= jsonDataURL %>";
var iconURL = "<%= iconURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">
		<div style="display: table; table-layout: fixed;">
			<div style="display: table-row;">
				<div style="display: table-cell;" class="tree-panel">
					<div id="jstree" class="process-tree"></div>
				</div>
				<div style="display: table-cell; width: 100%;">
					<div id="tree-panel" class="ui-widget ui-widget-content ui-corner-all info-panel">
						<h3>Process taxonomy</h3>
						<p></p>
					</div>
					<div id="domain-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<!-- <div id="crumb-trail"></div> -->
						<h3><span id="domain-title"></span></h3>
						<h4>Description</h4>
						<div id="domain-description"></div>
					</div>
					<div id="process-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<!-- <div id="crumb-trail"></div> -->
						<h3><span id="process-domain-title"></span></h3>
						<h4><span id="process-title"></span></h4>
						<div id="process-description"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br />
</div>

<div id="edit-domain-form" style="display: none;">
	<form id="domain-form">
		<input id="domain-id" name="domain-id" type="hidden"></input>
	</form>
	<table>
		<tr><td style="width: 120px; vertical-align: top;">
			<div class="gnosis-text">Process domain name:</div>
		</td><td>
			<input id="domain-name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input>
		</td></tr>
		<tr><td style="vertical-align: top;">
			<div style="margin-top: 30px;" class="gnosis-text">Description:</div>
		</td><td style="vertical-align: top;">
			<div id="domain-description-editor"></div>
		</td></tr>
	</table>
</div>

<div id="edit-process-form" style="display: none;">
	<form id="process-form">
		<input id="process-id" name="process-id" type="hidden"></input>
	</form>
	<table>
		<tr><td style="width: 150px; vertical-align: top;">
			<div class="gnosis-text">Capability name:</div>
		</td><td>
			<input id="process-name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input>
		</td></tr>
		<tr><td style="vertical-align: top;">
			<div style="margin-top: 30px;" class="gnosis-text">Description:</div>
		</td><td style="vertical-align: top;">
			<div id="process-description-editor"></div>
		</td></tr>
	</table>
</div>
