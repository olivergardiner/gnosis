<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
<portlet:resourceURL var="processJsonDataURL" id="processJsonData"/>
<portlet:resourceURL var="iconURL" id="icon"/>
<portlet:resourceURL var="pptxURL" id="pptx"/>
<portlet:resourceURL var="docxURL" id="docx"/>
<portlet:resourceURL var="actionURL" id="action"/>

<script type="text/javascript">
var jsonDataURL = "<%= jsonDataURL %>";
var processJsonDataURL = "<%= processJsonDataURL %>";
var iconURL = "<%= iconURL %>";
var pptxURL = "<%= pptxURL %>";
var docxURL = "<%= docxURL %>";
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">
		<div id="toolbar" style="display: none; width: 100%">
			<div style="display: table-row;">
				<div class="gnosis-left">
					<select id="process-flow-select">
						<option value="null">Select a process flow...</option>
					</select>
				</div>
				<div class="gnosis-spacer">&nbsp;</div>
				<div class="gnosis-right">Download: <button id="pptx-download">PPTX</button><button id="docx-download">DOCX</button></div>
			</div>
		</div>
		<div id="flow-diagram"></div>
		<div style="width: 95%; margin-left: auto; margin-right: auto;">
			<button id="edit-button" style="display: none;">Edit</button>
		</div>
		<br/>
		<div id="flow-editor" style="display: none; width: 95%; margin-left: auto; margin-right: auto;">
			<div id="flow-tree"></div>
		</div>
		<br/>
	</div>
	<br />
</div>

<div id="edit-process-instance" style="display: none;" title="Process instance">
	<div style="display: table; table-layout: fixed;">
		<div style="display: table-row;">
			<div style="display: table-cell; width: 150px;">Process duration</div>
			<div style="display: table-cell; width: *">
				<select id="process-duration-select">
					<option value="default">Default</option>
					<option value="short">Short</option>
					<option value="long">Long</option>
					<option value="always">Always</option>
				</select>
			</div>
		</div>
		<br/>
		<div style="display: table-row;" id="process-selector-row">
			<div style="display: table-cell;">Process</div>
			<div style="display: table-cell;">
				<div id="process-selector-tree"></div>
			</div>
		</div>
	</div>
</div>

<div id="edit-parent-dependency" style="display: none;" title="Parent process dependency">
	<div style="display: table; table-layout: fixed;">
		<div style="display: table-row;">
			<div style="display: table-cell; width: 150px;">Parent process</div>
			<div style="display: table-cell;">
				<div id="parent-selector-tree"></div>
			</div>
		</div>
	</div>
</div>

<div id="edit-predecessor-dependency" style="display: none;" title="Predecessor process dependency">
	<div style="display: table; table-layout: fixed;">
		<div style="display: table-row;">
			<div style="display: table-cell; width: 150px;">Dependency is contiguous</div>
			<div style="display: table-cell; width: *">
				<label for="contiguous">Contiguous</label>
				<input type="checkbox" id="contiguous">
			</div>
		</div>
		<br/>
		<div style="display: table-row;" id="predecessor-selector-row">
			<div style="display: table-cell;">Preceding process</div>
			<div style="display: table-cell;">
				<div id="predecessor-selector-tree"></div>
			</div>
		</div>
	</div>
</div>
