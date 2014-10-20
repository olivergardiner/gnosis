<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
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
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto;">
			<div style="display: table-row;" id="primary-value-chain">
			</div>
		</div>
		<div style="display: table; width: 95%; margin-left: auto; margin-right: auto;" id="support-value-chain"></div>
		<br/>
	</div>
	<div style="display: table; width: 95%; margin-left: auto; margin-right: auto;">
		<div style="display: table-row;">
			<div style="display: table-cell; padding: 2px;">
				<button id="edit-button" style="display: none;">Edit</button>
			</div>
		</div>
		<div id="value-chain-tree" style="display: none;">
			<div style="display: table-cell; padding: 2px; width: 110px;">Use the tree to edit the Value Chain</div>
			<div style="display: table-cell; padding: 2px; width: 10px;"></div>
			<div style="display: table-cell; padding: 2px;">
				<div id="jstree"></div>
			</div>
		</div>
	</div>
	<br />
</div>

<div id="wait-form" title="Please wait..." style="display: none;">
	<div class="please-wait"></div>
</div>
