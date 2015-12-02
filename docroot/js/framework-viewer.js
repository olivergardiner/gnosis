function showFramework(response) {
	var width = 100 / 4;
	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Value Chain</div></div></div>');
	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Organisation</div></div></div>');
	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Process Flows</div></div></div>');
	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Business Information</div></div></div>');

	var l = response.businessApplications.length;
	var n = 0;
	while (l > 0) {
		var businessAppsTable = $('<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;"></div>');
		var businessAppsRow = $('<div style="display: table-row;" class="ba-layer"></div>');
		businessAppsTable.append(businessAppsRow);
		$("#business-applications").append(businessAppsTable);
		var ll = (l > 6) ? 6 : l;
		width = 100 / ll;
		for (var i = 0; i < ll; i++) {
	    	businessAppsRow.append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="ba-domain"><div class="gnosis-middle">' + response.businessApplications[n].name + '</div></div></div>');
	    	n++;
		}
		l -= ll;
	}
	
	width = 100 / response.commonServices.length;
	for (var i = 0; i < response.commonServices.length; i++) {
    	$("#common-services").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="cs-domain"><div class="gnosis-middle">' + response.commonServices[i].name + '</div></div></div>');
	}

	width = 100 / response.infrastructure.length;
	for (var i = 0; i < response.infrastructure.length; i++) {
    	$("#infrastructure").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="inf-domain"><div class="gnosis-middle">' + response.infrastructure[i].name + '</div></div></div>');
	}
};

$("#pptx-download").button().click(function () {
	downloadURL(unescapeHTML(pptxURL));
}).show();

$("#vdx-download").button().click(function () {
	downloadURL(unescapeHTML(vdxURL));
}).show();
	
$.ajax({
    async : true,
    type : "GET",
    url : jsonDataURL,
    dataType : "json",
    success : showFramework,
    error : function(jqXhr, status, reason) {
    	alert("Unable to retrieve framework data\n" + status + ": " + reason);
    }
});

function downloadURL(url) {
    var hiddenIFrameID = 'hiddenDownloader',
        iframe = document.getElementById(hiddenIFrameID);
    if (iframe === null) {
        iframe = document.createElement('iframe');
        iframe.id = hiddenIFrameID;
        iframe.style.display = 'none';
        document.body.appendChild(iframe);
    }
    iframe.src = url;
};	

function unescapeHTML(html) {
   return $("<div />").html(html).text();
}

function escapeHTML(html) {
   return $("<div />").text(html).html();
}