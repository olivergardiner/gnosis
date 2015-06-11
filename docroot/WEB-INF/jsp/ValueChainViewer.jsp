<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
<portlet:resourceURL var="frameworkJsonDataURL" id="frameworkJsonData"/>
<portlet:resourceURL var="iconURL" id="icon"/>
<portlet:resourceURL var="actionURL" id="action"/>

<script type="text/javascript">
var jsonDataURL = "<%= jsonDataURL %>";
var frameworkJsonDataURL = "<%= frameworkJsonDataURL %>";
var iconURL = "<%= iconURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">

		<div style="display: table; table-layout: fixed;">
			<div style="display: table-row;">
				<div style="display: table-cell;" class="ui-widget ui-widget-content ui-corner-all tree-panel">
					<div id="jstree"></div>
				</div>
				<div style="display: table-cell; width: 100%;">
					<div id="tree-panel" class="ui-widget ui-widget-content ui-corner-all info-panel">
<p>Select an element of the value chain from the tree on the right to view or edit</p>
<p></p>
					</div>
					<div id="detail-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<div id="detail-capability" style="display: none;"><h4><span id="detail-capability-name"></span></h4></div>
						<div><h4><span id="detail-name"></span></h4></div>
						<div id="detail-description"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="edit-node-form" title="Value chain detail" style="display: none;">
	<br>
	<div style="display: table;">
		<div style="display: table-row;">
			<div style="display: table-cell; min-width: 150px;"></div>
			<div style="display: table-cell; width: 100%;"></div>
		</div>
		<div style="display: none;" id="capability-row">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Logical Application:</div>
			<div style="display: table-cell; width: 100%;"><span id="capability-name" class="ui-widget ui-corner-all" style="width: 100%;"></span></div>
		</div>
		<br>
		<div style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Name:</div>
			<div style="display: table-cell; width: 100%;"><input id="name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input></div>
		</div>
		<br>
		<div style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Description:</div>
			<div style="display: table-cell; vertical-align: top;"><div id="description-editor"></div></div>
		</div>
	</div>
</div>

<div id="capability-form" title="" style="display: none;">
	<div id="capability-jstree"></div>
</div>

<div id="wait-form" title="Please wait..." style="display: none;">
	<div class="please-wait"></div>
</div>
