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
		<div style="display: table; table-layout: fixed;">
			<div style="display: table-row;">
				<div style="display: table-cell;" class="ui-widget ui-widget-content ui-corner-all tree-panel">
					<div id="jstree"></div>
				</div>
				<div style="display: table-cell; width: 100%;">
					<div id="tree-panel" class="ui-widget ui-widget-content ui-corner-all info-panel">
<p>Select a Technology Domain or Logical Application from the tree on the right to view or edit</p>
<p></p>
					</div>
					<div id="domain-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<div style="display: table; table-layout: fixed;">
							<div style="display: table-row;">
								<div style="display: table-cell;" class="info-header"><h4>Domain</h4></div>
								<div style="display: table-cell;"><h4><span id="domain-title"></span></h4></div>
							</div>
						</div>
						<div id="domain-description"></div>
					</div>
					<div id="capability-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<div style="display: table; table-layout: fixed;">
							<div style="display: table-row;">
								<div style="display: table-cell;" class="info-header"><h4>Domain</h4></div>
								<div style="display: table-cell;"><h4><span id="capability-domain-title"></span></h4></div>
							</div>
							<div style="display: table-row;">
								<div style="display: table-cell;" class="info-header"><h4>Logical Application</h4></div>
								<div style="display: table-cell;"><h4><span id="capability-title"></span></h4></div>
							</div>
						</div>
						<div id="capability-description"></div>
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
			<div class="gnosis-text">Domain name:</div>
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
<div id="edit-capability-form" style="display: none;">
	<form id="capability-form">
		<input id="capability-id" name="capability-id" type="hidden"></input>
	</form>
	<table>
		<tr><td style="width: 150px; vertical-align: top;">
			<div class="gnosis-text">Logical Application name:</div>
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
