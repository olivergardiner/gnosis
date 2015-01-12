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
var valueChainJsonDataURL = "<%= valueChainJsonDataURL %>";
var iconURL = "<%= iconURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">
		<%-- <input id="framework-layer" type="hidden" value="<%= layer %>"/> --%>
		<!-- <button id="framework-save">Save</button> -->
		<table>
			<tr>
				<td style="width: 350px; vertical-align: top;">
					<div id="jstree"></div>
				</td>
				<td style="width: auto; vertical-align: top;">
					<div id="tree-panel"
						class="ui-widget ui-widget-content ui-corner-all aurora-panel">
<p>Select a Technology Domain or Capability from the tree on the right to view or edit</p>
<p></p>
					</div>
					<div id="domain-panel"
						class="ui-widget ui-widget-content ui-corner-all aurora-panel"
						style="display: none;">
						<!-- <div id="crumb-trail"></div> -->
						<h3>
							<span id="domain-title"></span>
						</h3>
						<div id="domain-value-chain"></div>
						<div id="domain-description"></div>
					</div>
					<div id="capability-panel"
						class="ui-widget ui-widget-content ui-corner-all aurora-panel"
						style="display: none;">
						<div id="gnosis2-crumb-trail"></div>
						<h3>
							<span id="capability-domain-title"></span>
						</h3>
						<h4>
							<span id="capability-title"></span>
						</h4>
						<div id="capability-description"></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<br />
</div>

<div id="edit-domain-form" style="display: none;">
	<form id="domain-form">
		<input id="domain-id" name="domain-id" type="hidden"></input>
	</form>
	<table>
		<tr><td style="width: 120px; vertical-align: top;">
			<div class="gnosis-text">Domain name:</div>
		</td><td>
			<input id="domain-name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input>
		</td></tr>
		<tr><td style="vertical-align: top;">
			<div style="margin-top: 30px;" class="gnosis-text">Description:</div>
		</td><td style="vertical-align: top;">
			<div id="domain-description-editor"></div>
		</td></tr>
		<tr id="value-chain"><td style="vertical-align: top;">
			<div style="margin-top: 30px;" class="gnosis-text">Value chain:</div>
		</td><td style="vertical-align: top;">
			<select id="domain-value-chain-editor">
			</select>
		</td></tr>
	</table>
</div>
<div id="edit-capability-form" style="display: none;">
	<form id="capability-form">
		<input id="capability-id" name="capability-id" type="hidden"></input>
	</form>
	<table>
		<tr><td style="width: 150px; vertical-align: top;">
			<div class="gnosis-text">Capability name:</div>
		</td><td>
			<input id="capability-name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input>
		</td></tr>
		<tr><td style="vertical-align: top;">
			<div style="margin-top: 30px;" class="gnosis-text">Description:</div>
		</td><td style="vertical-align: top;">
			<div id="capability-description-editor"></div>
		</td></tr>
	</table>
</div>
