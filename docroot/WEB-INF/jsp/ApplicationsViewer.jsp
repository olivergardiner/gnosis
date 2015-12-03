<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<portlet:defineObjects/>

<portlet:resourceURL var="applicationsListJsonDataURL" id="applicationsListJsonData"/>
<portlet:resourceURL var="valueChainJsonDataURL" id="valueChainJsonData"/>
<portlet:resourceURL var="actionURL" id="action"/>
<portlet:resourceURL var="pptxURL" id="pptx"/>
<portlet:resourceURL var="xlsxURL" id="xlsx"/>
<portlet:resourceURL var="iconURL" id="icon"/>

<script type="text/javascript">
var applicationsListJsonDataURL = "<%= applicationsListJsonDataURL %>";
var valueChainJsonDataURL = "<%= valueChainJsonDataURL %>";
var iconURL = "<%= iconURL %>";
var pptxURL = "<%= pptxURL %>";
var xlsxURL = "<%= xlsxURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header">
	</div>
	<br />
	<div class="portlet-section-body">
		<div id="app-list" style="display: none;">
			<table id="apps-table" class="display compact">
				<thead><tr><th class="dt-head-left">Name</th><th class="dt-head-left">Description</th></tr></thead>
				<tbody id="apps" style="cursor: pointer;">
				</tbody>
			</table>
		</div>
	</div>
	<br />
	<div>Download: <button id="pptx-download" style="display: none">PPTX</button><button id="xlsx-download" style="display: none">XLSX</button></div>
</div>

<div id="application-detail" title="Application detail" style="display: none;">
	<input id="app-id" type="hidden"></input>
	<div class="ui-widget-content ui-corner-all">
		<div style="display: table;">
			<div style="display: table-row; background: #eeeeee;">
				<div style="display: table-cell; width: 100%;"><div class="gnosis-title"><span>Name and description</span></div></div>
				<div style="display: table-cell; padding-top: 2px; padding-bottom: 2px; padding-left: 2px;"><button id="edit-app-basic">Edit</button></div>
			</div>
		</div>
		<br>
		<!-- <div style="display: table;">
			<div style="display: table-row;">
				<div style="display:table-cell; padding: 0px 2px;  white-space: pre; width: 100%"></div>
				<div style="display:table-cell; padding: 0px 2px;  white-space: pre;"><button id="edit-app-basic">Edit</button></div>
			</div>
		</div> -->
		<br>
		<div style="display: table;">
			<div style="display: table-row;">
				<div class="gnosis-label" style="display:table-cell; padding: 0px 2px;  white-space: pre; min-width: 150px;">Application name:</div>
				<div style="display:table-cell; padding: 0px 2px; width: 100%;">
					<div id="app-name" class="gnosis-text ui-widget-content ui-corner-all"></div>
				</div>
			</div>
			<div style="display: table-row;">
				<div class="gnosis-label" style="display:table-cell; padding: 0px 2px;  white-space: pre;">Description:</div>
				<div style="display:table-cell; padding: 0px 2px; width: 100%;">
					<div id="app-description" class="gnosis-text ui-widget-content ui-corner-all"></div>
				</div>
			</div>
		</div>
	</div>
	<br>
	<!-- <div class="ui-widget-content ui-corner-all">
		<div style="display: table;">
			<div style="display: table-row; background: #eeeeee;">
				<div style="display: table-cell; width: 100%;"><div class="gnosis-title"><span>Logical Applications</span></div></div>
				<div style="display: table-cell; padding-top: 2px; padding-bottom: 2px; padding-left: 2px;"><button id="add-capability">Add</button></div>
			</div>
		</div>
		<div id="capabilities"></div>
	</div>
	<br> -->
	<div class="ui-widget-content ui-corner-all">
		<div style="display: table;">
			<div style="display: table-row; background: #eeeeee;">
				<div style="display: table-cell; width: 100%;"><div class="gnosis-title"><span>Ecosystems</span></div></div>
				<div style="display: table-cell; padding-top: 2px; padding-bottom: 2px; padding-left: 2px;"><button id="add-ecosystem">Add</button></div>
			</div>
		</div>
		<div id="ecosystems"></div>
		<!-- <div style="background: #eeeeee;"><button id="add-capability" style="margin-right: 0px;">Add</button></div> -->
	</div>
	<br>
	<div class="ui-widget-content ui-corner-all">
		<div style="display: table;">
			<div style="display: table-row; background: #eeeeee;">
				<div style="display: table-cell; width: 100%;"><div class="gnosis-title"><span>Lifecycle</span></div></div>
				<div style="display: table-cell;"><button id="new-lifecycle">Stage</button></div>
				<div style="display: table-cell;"><button id="new-milestone">Milestone</button></div>
				<div style="display: table-cell;"><button id="new-migration">Migration</button></div>
				<div style="display: table-cell;"><button id="edit-lifecycle">Edit</button></div>
				<div style="display: table-cell; padding-top: 2px; padding-bottom: 2px; padding-left: 2px;"><button id="remove-lifecycle">Remove</button></div>
			</div>
		</div>
		<br>
		<table>
			<tr>
				<td width="15%"><span style="display: inline-block;" class="lc-candidate"></span><span> Candidate</span></td>
				<td width="14%"><span style="display: inline-block;" class="lc-emerging"></span><span> Emerging</span></td>
				<td width="14%"><span style="display: inline-block;" class="lc-mainstream"></span><span> Mainstream</span></td>
				<td width="14%"><span style="display: inline-block;" class="lc-heritage"></span><span> Heritage</span></td>
				<td width="14%"><span style="display: inline-block;" class="lc-sunset"></span><span> Sunset</span></td>
				<td width="14%"><span style="display: inline-block;" class="lc-retire"></span><span> Retire</span></td>
				<td width="15%"><span style="display: inline-block;" class="lc-remove"></span><span> Remove</span></td>
			</tr>
		</table>
		<div style="display: table;">
			<div style="display: table-row;">
				<div style="display: table-cell; vertical-align: middle;"><div id="initial-stage" style="cursor: pointer;"></div></div>
				<div style="display: table-cell; vertical-align: middle;"><div id="timeline-earlier" class="left-arrow" style="margin-left: 20px;"></div></div>
				<div id="timeline-slider" style="display: table-cell; margin-left: 30px; margin-right: 30px; padding-top: 30px; overflow: hidden;">
					<div id="timeline" style="overflow: auto; padding-right: 15px;"></div>
				</div>
				<div style="display: table-cell; vertical-align: middle;"><div id="timeline-later" class="right-arrow"></div></div>
			</div>
		</div>
		<div style="display: table;">
			<div style="display: table-row; background: #eeeeee; height: 30px;">
				<div style="display: table-cell; vertical-align:middle"><div id="detail-label" class="gnosis-label" style="width: 100px; padding-left: 10px; vertical-align: middle;">Lifecycle detail: </div></div>
				<div style="display: table-cell; width: 100%; vertical-align: middle;"><div id="lifecycle-detail" style="vertical-align: middle;"></div></div>
			</div>
		</div>
	</div>
</div>

<div id="edit-app-basic-form" title="Application name and description" style="display: none;">
	<br>
	<div style="display: table;">
		<div style="display: table-row;">
			<div style="display: table-cell; min-width: 150px; vertical-align: top;" class="gnosis-label">Application name:</div>
			<div style="display: table-cell; width: 100%;"><input id="app-name-editor" type="text" class="ui-widget ui-corner-all" style="width: 100%;"></input></div>
		</div>
		<br>
		<div style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Description:</div>
			<div style="display: table-cell; vertical-align: top;"><div id="app-description-editor"></div></div>
		</div>
	</div>
</div>

<div id="ecosystem-form" title="" style="display: none;">
	<div id="value-chain-jstree"></div>
</div>

<div id="edit-lifecycle-form" title="Lifecycle stage" style="display: none;">
	<br>
	<div style="display: table;">
		<div style="display: table-row;">
			<div style="display: table-cell; min-width: 150px; vertical-align: top;" class="gnosis-label">Lifecycle stage:</div>
			<div style="display: table-cell; width: 100%;">
				<select id="lifecycle-stage" class="ui-widget-content ui-corner-all">
					<option value="candidate">Candidate</option>
					<option value="emerging">Emerging</option>
					<option value="mainstream">Mainstream</option>
					<option value="heritage">Heritage</option>
					<option value="sunset">Sunset</option>
					<option value="retire">Retire</option>
					<option value="remove">Remove</option>
				</select>
			</div>
		</div>
		<br>
		<div id="lifecycle-initial-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Initial state:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="checkbox" id="lifecycle-initial-state" value="initial"></input></div>
		</div>
		<br>
		<div id="lifecycle-date-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Date:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="text" id="lifecycle-date" class="ui-widget-content ui-corner-all"></input></div>
		</div>
	</div>
</div>

<div id="edit-milestone-form" title="Investment milestone" style="display: none;">
	<br>
	<div style="display: table;">
		<div style="display: table-row;">
			<div style="display: table-cell; min-width: 150px; vertical-align: top;" class="gnosis-label">Description:</div>
			<div style="display: table-cell; width: 100%;"><textarea id="milestone-description" class="ui-widget-content ui-corner-all"></textarea></div>
		</div>
		<br>
		<div id="milestone-date-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Date:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="text" id="milestone-date" class="ui-widget-content ui-corner-all"></input></div>
		</div>
		<br>
		<div id="milestone-capital-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Capital:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="text" id="milestone-capital"></input></div>
		</div>
		<div id="milestone-runrate-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Run rate:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="text" id="milestone-runrate"></input></div>
		</div>
	</div>
</div>

<div id="edit-migration-form" title="Application migration" style="display: none;">
	<br>
	<div style="display: table;">
		<div id="migration-date-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Date:</div>
			<div style="display: table-cell; vertical-align: top;"><input type="text" id="migration-date" class="ui-widget-content ui-corner-all"></input></div>
		</div>
		<br>
		<div id="migration-target-row" style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;" class="gnosis-label">Target:</div>
			<div style="display: table-cell; vertical-align: top;"><select name="target" id="migration-target"></select></div>
		</div>
	</div>
</div>

<div id="confirm-lifecycle-remove-form" title="Remove lifecycle stage" style="display: none;">
<br><br><br>
<h4>Confirm removal of lifecycle stage?</h4>
</div>

<div id="wait-form" title="Please wait..." style="display: none;">
<div class="please-wait"></div>
</div>
