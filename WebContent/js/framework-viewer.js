(function($) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    //alert(versions);

    function showFramework(response) {
		var width = 100 / 4;
    	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Value Chain</div></div></div>');
    	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Organisation</div></div></div>');
    	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Process Flows</div></div></div>');
    	$("#operating-model").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="om-domain"><div class="gnosis-middle">Business Information</div></div></div>');

    	var primaryBusinessApplicationDomains = [];
    	for (var i=0; i < response.valueChain.primaryActivities.length; i++) {
    		var activity = response.valueChain.primaryActivities[i];
        	for (var j=0; j < response.businessApplications.length; j++) {
        		if (response.businessApplications[j].valueChain != undefined && response.businessApplications[j].valueChain == activity.id) {
        			primaryBusinessApplicationDomains.push(response.businessApplications[j]);
        		}
        	}
    	}
    	
    	width = 100 / primaryBusinessApplicationDomains.length;
    	for (var i = 0; i < primaryBusinessApplicationDomains.length; i++) {
        	$("#business-applications-primary").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="ba-domain"><div class="gnosis-middle">' + primaryBusinessApplicationDomains[i].name + '</div></div></div>');
    	}
    	
    	var supportBusinessApplicationDomains = [];
    	for (var i=0; i < response.valueChain.supportActivities.length; i++) {
    		var activity = response.valueChain.supportActivities[i];
        	for (var j=0; j < response.businessApplications.length; j++) {
        		if (response.businessApplications[j].valueChain != undefined && response.businessApplications[j].valueChain == activity.id) {
        			supportBusinessApplicationDomains.push(response.businessApplications[j]);
        		}
        	}
    	}
    	
    	for (var j=0; j < response.businessApplications.length; j++) {
    		if (response.businessApplications[j].valueChain == undefined) {
    			supportBusinessApplicationDomains.push(response.businessApplications[j]);
    		}
    	}
    	
    	width = 100 / supportBusinessApplicationDomains.length;
    	for (var i = 0; i < supportBusinessApplicationDomains.length; i++) {
        	$("#business-applications-support").append('<div style="padding: 5px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="ba-domain"><div class="gnosis-middle">' + supportBusinessApplicationDomains[i].name + '</div></div></div>');
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
		
	$("#docx-download").button().click(function () {
		downloadURL(unescapeHTML(docxURL));
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
})($);