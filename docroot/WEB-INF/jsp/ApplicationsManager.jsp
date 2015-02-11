<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page contentType="text/html" isELIgnored="false" %> 

<portlet:defineObjects/>

<portlet:resourceURL var="actionURL" id="action"/>

<script type="text/javascript">
var actionURL = "<%= actionURL %>";
</script>

<div>
	<div class="portlet-section-header"></div>
	<br />
	<div class="portlet-section-body">
		<div id="apps-upload" style="display:none;">
			<div>To update the Applications list, simply upload an edited applications spreadsheet</div>
			<div id="apps-upload-form">Upload Applications</div>
		</div>
	</div>
	<br />
</div>
