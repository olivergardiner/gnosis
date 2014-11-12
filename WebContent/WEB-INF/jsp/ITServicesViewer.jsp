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
		<div id="toolbar-top" style="display: none; width: 100%">
			<div style="display: table-row;">
				<div class="gnosis-left">
					<button id="new-flow">New</button>
				</div>
				<div class="gnosis-spacer">&nbsp;</div>
				<div class="gnosis-right">Download: <button id="pptx-download">PPTX</button><button id="docx-download">DOCX</button></div>
			</div>
			<div style="display: table-row;">
				<div class="gnosis-left">
					<select id="process-flow-select"></select>
				</div>
			</div>
		</div>
		<div style="display: table; table-layout: fixed;">
			<div style="display: table-row;">
				<div style="display: table-cell;" class="tree-panel">
					<div id="jstree" class="services-tree"></div>
				</div>
				<div style="display: table-cell; width: 100%;">
					<div id="root-panel" class="ui-widget ui-widget-content ui-corner-all info-panel">
						<h3>IT Services</h3>
						<p>SVG picture goes here...</p>
					</div>
					<div id="service-panel" class="ui-widget ui-widget-content ui-corner-all info-panel" style="display: none;">
						<div id="crumb-trail"></div>
						<h3><span id="service-title"></span></h3>
						<h4>Description</h4>
						<div id="service-description"></div>
						<div id="dependencies">
							<h4>Dependencies</h4>
							<div id="dependency-list"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br />
</div>

<div id="edit-form" style="display: none;">
	<input id="element-id" type="hidden"></input>
	<div style="display: table; table-layout: fixed;">
		<div style="display: table-row;">
			<div style="display: table-cell; min-width: 120px; max-width: 120px;">
				<div>Name:</div>
			</div>
			<div style="display: table-cell;">
				<input id="name-editor" type="text" class="ui-widget ui-corner-all" style="width: 650px;"></input>
			</div>
		</div>
		<br/>
		<div style="display: table-row;">
			<div style="display: table-cell; vertical-align: top;">
				<div style="margin-top: 30px;">Description:</div>
			</div>
			<div style="display: table-cell; vertical-align: top;">
				<div id="description-editor"></div>
			</div>
		</div>
	</div>
</div>

<div id="dependency-form" style="display: none;">
	<input id="dependency-id" type="hidden"></input>
	<table>
		<tr><td style="width: 120px; vertical-align: top;">
			<div style="margin-top: 30px;">Reason:</div>
		</td><td style="vertical-align: top;">
			<div id="dependency-reason-editor"></div>
		</td></tr>
		<tr><td style="vertical-align: top;">
			<div>Dependency:</div>
		</td><td style="vertical-align: top;">
			<div id="dependency-tree" class="services-tree"></div>
		</td></tr>
	</table>
</div>

