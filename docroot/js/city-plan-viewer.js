function showEcosystems(response) {
	var width ;
	
	var table = $('<div style="display: table; width: 95%; margin-left: auto; margin-right: auto; table-layout: fixed;"></div>');
	var primaryRow = $('<div style="display: table-row;" class="ba-layer"></div>');
	table.append(primaryRow);
	$("#value-chain-primary").append(table);

	width = 100 / response.valueChain.primaryActivities.length;
	for (var i = 0; i < response.valueChain.primaryActivities.length; i++) {
    	primaryRow.append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="ba-domain"><div class="gnosis-middle">' + response.valueChain.primaryActivities[i].name + '</div></div></div>');
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
    success : showEcosystems,
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