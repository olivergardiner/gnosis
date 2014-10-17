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
		<br/>
	</div>
	<br />
</div>
