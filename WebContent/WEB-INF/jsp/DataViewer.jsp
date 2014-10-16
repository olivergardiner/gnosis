<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<portlet:defineObjects/>

<portlet:resourceURL var="jsonDataURL" id="jsonData"/>
<portlet:resourceURL var="modelDetailURL" id="modelDetail"/>

<script type="text/javascript">
var jsonDataURL = "<%= jsonDataURL %>";
var modelDetailURL = "<%= modelDetailURL %>";
var changeNodeCallback;

function changeNode(path) {
	changeNodeCallback(path);
}
</script>

<div>
	<div class="portlet-section-body">
		<table>
			<tr>
				<td style="vertical-align: top;">
					<div id="jstree"></div>
				</td>
				<td>
					<div id="detail-panel"></div>
				<td>
			</tr>
		</table>
	</div>
	<br />
</div>
