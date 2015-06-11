var table = $("#apps-table").dataTable({
	"ajax": applicationsListJsonDataURL,
	//"data": [['Id', 'Name', 'Description']],
    "paging": false,
    "autoWidth": false,
    "lengthChange": false,
    "info": false,
    "searching": true,
    "ordering": true,
    "dom": '<"toolbar"f<"filter">rtip>',
    "scrollY": 400,
    //"scrollX": false,
    "jQueryUI": false,
    "order": [[ 0, "asc" ]],
    "columns": [
        { "width": "30%", "data": "name" },
		{ "width": "70%", "orderable": false, "data": "description" }
    ]
});

var eventData;

$("#pptx-download").button().click(function () {
	var tags = $("div.filter").select2("data");
	var filter = "";
	if (tags.length != 0) {
		filter = "&capabilities=";
		var separator = "";
		for (var i = 0; i < tags.length; i++) {
			filter += separator + tags[i].id;
			separator = ",";
		}
	}

	var url = unescapeHTML(pptxURL) + filter
	downloadURL(url);
}).show();

$("#xlsx-download").button().click(function () {
	downloadURL(unescapeHTML(xlsxURL));
}).show();

$("div.toolbar").prepend('<button id="delete-application">Delete</button>');
$("#delete-application").button().click(function() {
	deleteSelectedApplication();
});

$("div.toolbar").prepend('<button id="edit-application">Edit</button>');
$("#edit-application").button().click(function() {
	editSelectedApplication();
});

$("div.toolbar").prepend('<button id="new-application">New</button>');
$("#new-application").button().click(function() {
	newApplication();
});

$("div.toolbar").prepend('<button id="filter-application">Filter</button>');
$("#filter-application").button().click(function() {
	//$("#capability-form").dialog("option", "title", "Add a capability filter");
	$("#capability-form").data("type", "filter");
	$("#capability-form").dialog("open");
});

$("div.filter").select2({
	"width": "100%",
	"tags": []
}).on("select2-removed", function(event) {
	table.api().draw();
});

$.fn.dataTableExt.afnFiltering.push(function(settings, data, index) {
	var tags = $("div.filter").select2("data");
	if (tags.length == 0) {
		return true;
	}
	
	var app = table.api().row(index).data();
	for (var i = 0; i< app.capabilities.length; i++) {
		var cap = app.capabilities[i];
		for (var j = 0; j < tags.length; j++) {
			if (tags[j].id == cap) {
				return true;
			}
		}
	}

	return false;
});

//table.api().ajax.url(applicationsListJsonDataURL).load();

$("#app-list tbody").click(function(event) {
	event.preventDefault();
	var row = $(event.target.parentNode);
	
    if (row.hasClass('selected')) {
        row.removeClass('selected');
    } else {
        table.$('tr.selected').removeClass('selected');
        row.addClass('selected');
    }
});
	
$("#app-list tbody").dblclick(function(event) {
	event.preventDefault();
	var row = $(event.target.parentNode);
    table.$('tr.selected').removeClass('selected');
    row.addClass('selected');
    
    editSelectedApplication()
});

$("#app-list").show();

$('#app-description-editor').jqte({
});

$("#application-detail").dialog({
    autoOpen: false,
    height: 600,
    width: 980,
    modal: true,
    buttons: {
    	"Close": function() {
			$(this).dialog( "close" );
        }
    },
    open: function() {
    },
    close: function() {
    }
});

$('#edit-app-basic-form').dialog({
    autoOpen: false,
    height: 500,
    width: 800,
    modal: true,
    buttons: {
    	"Save": function() {
    		var name = $('#app-name-editor').val();
    		var description = $('#edit-app-basic-form div.jqte_editor').html();
    		
    		repoUpdateApplicationBasic($("#app-id").val(), name, description);

    		$("#app-name").text(name);
    		$("#app-description").html(description);
    		
            var selected = table.$('tr.selected');
            if (selected.length !== 0) {
        		var data = table.fnGetData(selected[0]);
        		data.name = name;
        		data.description = description;
            	table.api().row(selected[0]).data(data);
            }
    		
			$(this).dialog( "close" );
        },
    	"Cancel": function() {
			$(this).dialog( "close" );
        }
    },
    open: function() {
    },
    close: function() {
    }
});

$("#edit-app-basic").button().click(function( event ) {
    event.preventDefault();
	$('#app-name-editor').val($("#app-name").text());
	$('#app-description-editor').jqteVal($("#app-description").html());
    $("#edit-app-basic-form").dialog("open")
});

$("#initial-stage").click(function() {
	showStage($("#initial-stage").attr("data-lifecycle"));
});

$("#timeline-earlier").on("click", function() {
	showTimeline(parseInt($("#timeline").attr("data-start-year")) - 1);
});

$("#timeline-later").on("click", function() {
	showTimeline(parseInt($("#timeline").attr("data-start-year")) + 1);
});
	
$("#capabilities").select2({
	"width": "100%",
	"tags": []
})./*on("change", function(event) {
	// According to the documentation, this should work - but the event does not get fired for add...
	if (event.removed != undefined) {
		repoDeleteCapability($("#app-id").val(), event.removed.id);
	}
	if (event.added != undefined) {
		repoAddCapability($("#app-id").val(), event.added.id);
	}
}).*/on("select2-removing", function(event) {
	repoDeleteCapability($("#app-id").val(), event.val);
});

$("#ecosystems").select2({
	"width": "100%",
	"tags": []
})./*on("change", function(event) {
	// According to the documentation, this should work - but the event does not get fired for add...
	if (event.removed != undefined) {
		repoDeleteCapability($("#app-id").val(), event.removed.id);
	}
	if (event.added != undefined) {
		repoAddCapability($("#app-id").val(), event.added.id);
	}
}).*/on("select2-removing", function(event) {
	var id = event.val.split("/");
	repoDeleteEcosystem($("#app-id").val(), id[0], id[1]);
});

$("ul.select2-choices").click(function() {
	
});

$('#jstree').jstree({
	'core': {
		'data': getFrameworkJsonData,
		'multiple': false,
		'check_callback': true,
		'themes': {
			'variant': 'small',
			'responsive': false
		}
	},
	'types': {
		'#': {
			'valid_children': ['root', 'trash']
		},
		'root': {
			'valid_children': ['technology-domain']
		},
		'technology-domain': {
			'valid_children': ['capability'],
			'icon': iconURL + "&icon=technology-domain.ico"
		},
		'capability': {
			'valid_children': [],
			'icon': iconURL + "&icon=capability.ico"
		},
		'trash': {
			'icon': iconURL + "&icon=trash.ico"
		}
	},
	'plugins': [ 'types' ]
}).dblclick(function() {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_selected(true);
	
	if (node.length > 0) {
		if (node[0].data == undefined || node[0].type != "capability") {
			if (tree.is_open(node[0].id)) {
				tree.close_node(node[0].id);
			} else {
				tree.open_node(node[0].id);
			}
		} else {
			applyAddCapability();
		}
	}
});

$('#value-chain-jstree').jstree({
	'core': {
		'data': getValueChainJsonData,
		'multiple': false,
		'check_callback': true,
		'themes': {
			'variant': 'small',
			'responsive': false
		}
	},
	'types': {
		'#': {
			'valid_children': ['primary', 'support', 'trash']
		},
		'primary': {
			'valid_children': ['activity']
		},
		'support': {
			'valid_children': ['activity']
		},
		'activity': {
			'valid_children': ['ecosystem'],
			'icon': iconURL + "&icon=technology-domain.ico"
		},
		'ecosystem': {
			'valid_children': ['capability'],
			'icon': iconURL + "&icon=technology-domain.ico"
		},
		'capability': {
			'valid_children': [],
			'icon': iconURL + "&icon=technology-domain.ico"
		},
		'trash': {
			'icon': iconURL + "&icon=trash.ico"
		}
	},
	'plugins': [ 'types' ]
}).dblclick(function() {
	var tree = $.jstree.reference('#value-chain-jstree');
	var node = tree.get_selected(true);
	
	if (node.length > 0) {
		if (node[0].data == undefined || node[0].type != "capability") {
			if (tree.is_open(node[0].id)) {
				tree.close_node(node[0].id);
			} else {
				tree.open_node(node[0].id);
			}
		} else {
			applyAddEcosystem();
		}
	}
});

$('#capability-form').dialog({
	title: "Add a Logical Application",
    autoOpen: false,
    height: 500,
    width: 600,
    modal: true,
    buttons: { "Add": applyAddCapability, "Close": closeCapabilityForm }
});

$("#add-capability").button().click(function() {
	//$("#capability-form").dialog("option", "title", "Add a capability");
	$("#capability-form").data("type", "classify");
	$("#capability-form").dialog("open");
});

$('#ecosystem-form').dialog({
	title: "Add an Ecosystem",
    autoOpen: false,
    height: 500,
    width: 600,
    modal: true,
    buttons: { "Add": applyAddEcosystem, "Close": closeEcosystemForm }
});

$("#add-ecosystem").button().click(function() {
	//$("#capability-form").dialog("option", "title", "Add a capability");
	$("#ecosystem-form").data("type", "classify");
	$("#ecosystem-form").dialog("open");
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

function closeCapabilityForm() {
	$('#capability-form').dialog("close");
}

function closeEcosystemForm() {
	$('#ecosystem-form').dialog("close");
}

function applyAddCapability() {
	if ($("#capability-form").data("type") == "classify") {
		addCapability();
	} else {
		addCapabilityFilter();
		table.api().draw();
	}
	
	$('#capability-form').dialog( "close" );
}
	
function applyAddEcosystem() {
	if ($("#ecosystem-form").data("type") == "classify") {
		addEcosystem();
	} else {
		//addCapabilityFilter();
		//table.api().draw();
	}
	
	$('#ecosystem-form').dialog( "close" );
}
	
$('#edit-lifecycle-form').dialog({
    autoOpen: false,
    height: 500,
    width: 600,
    modal: true,
    buttons: {
    	"Save": function() {
            var selected = table.$('tr.selected');
            if (selected.length !== 0) {
        		var data = table.fnGetData(selected[0]);
        		if (checkLifecycle(data)) {
        			var date = $("#lifecycle-date").datepicker("getDate");
        			var dateString = "";
        	        if (date != undefined) {
            			date.setHours(5);
            			dateString = date.toISOString();
        	        }
        			repoAction("updateLifecycleAction", {
        				applicationId: data.applicationId,
        				mode: $("#lifecycle-detail").attr("data-mode"),
        				stage: $("#lifecycle-stage").val(),
        				date: dateString
        			}, function(response) {
            			updateApplicationList(response);
            			var year = parseInt($("#timeline").attr("data-start-year"));
            			buildTimeline(year, response);
        			});
        			
    				$(this).dialog("close");
        		}
            }
        },
    	"Cancel": function() {
			$(this).dialog( "close" );
        }
    },
    open: function() {
    },
    close: function() {
    }
});

$('#edit-milestone-form').dialog({
    autoOpen: false,
    height: 500,
    width: 600,
    modal: true,
    buttons: {
    	"Save": function() {
            var selected = table.$('tr.selected');
            if (selected.length !== 0) {
        		var data = table.fnGetData(selected[0]);
        		if (checkLifecycle(data)) {
        			var date = $("#milestone-date").datepicker("getDate");
        			var dateString = "";
        	        if (date != undefined) {
            			date.setHours(5);
            			dateString = date.toISOString();
        	        }
        			repoAction("updateInvestmentAction", {
        				applicationId: data.applicationId,
        				mode: $("#lifecycle-detail").attr("data-mode"),
        				index: $("#lifecycle-detail").attr("data-milestone-index"),
        				description: $("#milestone-description").val(),
        				date: dateString,
        				capital: $("#milestone-capital").val(),
        				runrate: $("#milestone-runrate").val()
        			}, function(response) {
            			updateApplicationList(response);
            			var year = parseInt($("#timeline").attr("data-start-year"));
            			buildTimeline(year, response);
        			});
        			
    				$(this).dialog("close");
        		}
            }
        },
    	"Cancel": function() {
			$(this).dialog( "close" );
        }
    },
    open: function() {
    },
    close: function() {
    }
});

//$("#lifecycle-stage").selectmenu({
//	width: 140
//});

$("#new-lifecycle").button().click(function( event ) {
    event.preventDefault();
    $("#lifecycle-detail").attr("data-mode", "new");
    $("#lifecycle-stage").val("candidate");
	$("#lifecycle-initial-state").prop("checked", false);
	$("#lifecycle-initial-state").prop("disabled", false);
	$("#lifecycle-stage").prop("disabled", false);
	$("#lifecycle-date-row").show();
	$("#lifecycle-initial-row").show();
    $("#lifecycle-date").datepicker("setDate", "");
    $('#edit-lifecycle-form').dialog("open");
});

$("#new-milestone").button().click(function( event ) {
    event.preventDefault();
    $("#lifecycle-detail").attr("data-mode", "new");
    $("#lifecycle-detail").attr("data-milestone-index", undefined);
    $("#milestone-description").val("");
    $("#milestone-date").datepicker("setDate", "");
    $("#milestone-capital").val("");
    $("#milestone-runrate").val("");
    $('#edit-milestone-form').dialog("open");
});

$("#edit-lifecycle").button().click(function( event ) {
    event.preventDefault();
    if ($("#lifecycle-detail").attr("data-type") == "investment") {
	    $("#lifecycle-detail").attr("data-mode", "edit");
	    $("#milestone-description").val($("#lifecycle-detail").attr("data-description"));
	    var formattedDate = "";
        var date = new Date($("#lifecycle-detail").attr("data-date"));
        if (date != undefined) {
	        formattedDate = formatDate(date, {
	        	shortMonths: true,
	        	ordinals: false,
	        	monthFirst: false
	        });
        }
	    $("#milestone-date").datepicker("setDate", formattedDate);
	    $("#edit-milestone-form").dialog("open");
    } else {
	    $("#lifecycle-detail").attr("data-mode", "edit");
	    $("#lifecycle-stage").val($("#lifecycle-detail").attr("data-stage"));
	    var formattedDate = "";
	    if ($("#lifecycle-detail").attr("data-date") != "") {
	        var date = new Date($("#lifecycle-detail").attr("data-date"));
	        if (date != undefined) {
		        formattedDate = formatDate(date, {
		        	shortMonths: true,
		        	ordinals: false,
		        	monthFirst: false
		        });
	        }
			$("#lifecycle-initial-state").prop("checked", false);
			$("#lifecycle-initial-state").prop("disabled", true);
			$("#lifecycle-stage").prop("disabled", true);
			$("#lifecycle-date-row").show();
			$("#lifecycle-initial-row").hide();
		} else {
			$("#lifecycle-initial-state").prop("checked", true);
			$("#lifecycle-initial-state").prop("disabled", true);
			$("#lifecycle-stage").prop("disabled", false);
			$("#lifecycle-date-row").hide();
			$("#lifecycle-initial-row").show();
		}
	    $("#lifecycle-date").datepicker("setDate", formattedDate);
	    $("#edit-lifecycle-form").dialog("open");
    }
});

$("#remove-lifecycle").button().click(function( event ) {
    event.preventDefault();
    $("#confirm-lifecycle-remove-form").dialog("open");
});

$("#confirm-lifecycle-remove-form").dialog({
    autoOpen: false,
    height: 300,
    width: 400,
    modal: true,
    buttons: {
    	"Remove": function() {
            var selected = table.$('tr.selected');
            if (selected.length !== 0) {
        		var data = table.fnGetData(selected[0]);
        		
        		var update = {
	    				applicationId: data.applicationId,
	    		};
        		
        	    if ($("#lifecycle-detail").attr("data-type") == "investment") {
	    			update.milestone = $("#lifecycle-detail").attr("data-milestone-index")
        	    } else {
        	    	update.stage = $("#lifecycle-detail").attr("data-stage")
        	    }
        	    
    			repoAction("removeLifecycleAction", update, function(response) {
        			updateApplicationList(response);
        			var year = parseInt($("#timeline").attr("data-start-year"));
        			buildTimeline(year, response);
    			});
            }
            
			$(this).dialog("close");
        },
    	"Cancel": function() {
			$(this).dialog( "close" );
        }
    },
    open: function() {
    },
    close: function() {
    }
});

$("#wait-form").dialog({
    autoOpen: false,
    height: 200,
    width: 200,
    modal: true,
    resizeable: false,
    draggable: true,
    buttons: {},
    open: function() {
    },
    close: function() {
    }
});

$("#lifecycle-date").datepicker({
    changeMonth: true,
    changeYear: true,
    dateFormat: "d M yy"
});

$("#milestone-date").datepicker({
    changeMonth: true,
    changeYear: true,
    dateFormat: "d M yy"
});

$("#lifecycle-initial-state").change(function() {
	if ($("#lifecycle-initial-state").prop("checked")) {
		$("#lifecycle-date-row").hide();
	} else {
		$("#lifecycle-date-row").show();
 	}
});

function checkLifecycle(data) {
	return true;
}

function addCapability() {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_selected(true);
	
	if (node.length > 0) {
		
		_addCapability(node[0].data.id, node[0].text);
	}
}

function _addCapability(capability, text) {
	var data = $("#capabilities").select2("data");
	
	var found = false;
	for (var i = 0; i < data.length; i++) {
		if (data[i].id == capability) {
			found = true;
		}
	}
	
	if (!found) {
		data.push({
    		id: capability,
    		text: text
    	});
		
		$("#capabilities").select2("data", data);
		repoAddCapability($("#app-id").val(), capability);
	}
}

function addEcosystem() {
	var tree = $.jstree.reference('#value-chain-jstree');
	var node = tree.get_selected(true);
	
	if (node.length > 0) {
		
		var parentNode = tree.get_node(tree.get_parent(node[0]));
		
		_addEcosystem(parentNode.data.id, node[0].data.id, node[0].text);		
	}
}

function _addEcosystem(ecosystem, capability, text) {
	var data = $("#ecosystems").select2("data");
	
	var id = ecosystem + "/" + capability;

	var found = false;
	for (var i = 0; i < data.length; i++) {
		if (data[i].id == id) {
			found = true;
		}
	}
	
	if (!found) {
		data.push({
    		id: id,
    		text: text
    	});
				
		$("#ecosystems").select2("data", data);
		repoAddEcosystem($("#app-id").val(), ecosystem, capability);

		// It would be nice to also add the corresponding Logical Application automatically
		var capabilityName = getCapabilityName(capability);
		_addCapability(capability, capabilityName);
	}
}

function addCapabilityFilter() {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_selected(true);
	
	if (node.length > 0) {
		var data = $("div.filter").select2("data");
		
		var found = false;
		for (var i = 0; i < data.length; i++) {
			if (data[i].id == node[0].data.id) {
				found = true;
			}
		}
		
		if (!found) {
			data.push({
	    		id: node[0].data.id,
	    		text: node[0].text
	    	});
			
			$("div.filter").select2("data", data);
		}
	}
}

function getFrameworkJsonData(obj, callback) {
	$.ajax({
        async : true,
        type : "GET",
        url : frameworkJsonDataURL,
        dataType : "json",
        success : function(response) {
    		callback.call(this, response.tree);
        },
        error : function(jqXhr, status, reason) {
        	alert("Unable to retrieve framework data\n" + status + ": " + reason);
        }
	});
}

function getValueChainJsonData(obj, callback) {
	$.ajax({
        async : true,
        type : "GET",
        url : valueChainJsonDataURL,
        dataType : "json",
        success : function(response) {
    		callback.call(this, response);
        },
        error : function(jqXhr, status, reason) {
        	alert("Unable to retrieve value chain data\n" + status + ": " + reason);
        }
	});
}

function getCapabilityName(capabilityId) {
	var tree = $.jstree.reference('#jstree');
	var json = tree.get_json();
	
	for (var i = 0; i < json.length; i++) {
		var layer = json[i];
		for (var j = 0; j < layer.children.length; j++) {
			var domain = layer.children[j];
			for (var k = 0; k < domain.children.length; k++) {
				if (domain.children[k].data.id == capabilityId) {
					return domain.children[k].text;
				}
			}
		}
	}
	
	return undefined;
}

function getCapabilityInstanceName(ecosystemId, capabilityId) {
	var tree = $.jstree.reference('#value-chain-jstree');
	var json = tree.get_json();
	
	for (var i = 0; i < json.length; i++) {
		var layer = json[i];
		for (var j = 0; j < layer.children.length; j++) {
			var domain = layer.children[j];
			for (var k = 0; k < domain.children.length; k++) {
				var ecosystem = domain.children[k];
				if (ecosystem.data.id == ecosystemId) {
					for (var l = 0; l < ecosystem.children.length; l++) {
						if (ecosystem.children[l].data.id == capabilityId) {
							return ecosystem.children[l].text;
						}
					}
				}
			}
		}
	}
	
	return undefined;
}

function editSelectedApplication() {
    var selected = table.$('tr.selected');
    if (selected.length !== 0) {
		var data = table.fnGetData(selected[0]);
		
		$("#app-id").val(data.applicationId);

    	$("#app-name").text(data.name);
    	if (data.description != "") {
    		$("#app-description").html(data.description);
    	} else {
	    	$("#app-description").html("&nbsp;");
	    }
    	
    	var capabilities = [];
    	for (var i = 0; i < data.capabilities.length; i++) {
    		var capabilityId = data.capabilities[i];
    		var capabilityName = getCapabilityName(capabilityId);
    		
    		if (capabilityName != undefined) {
		    	capabilities.push({
		    		id: capabilityId,
		    		text: capabilityName
		    	});
    		}
    	}
    	
    	$("#capabilities").select2("data", capabilities);
    	
    	var ecosystems = [];
    	if (data.ecosystems != undefined) {
	    	for (var i = 0; i < data.ecosystems.length; i++) {
	    		var ecosystem = data.ecosystems[i];
	    		var ecosystemId = ecosystem.ecosystemId;
	    		for (var j = 0; j < ecosystem.capabilities.length; j++) {
	    	   		var capabilityId = ecosystem.capabilities[j];
	    			var capabilityName = getCapabilityInstanceName(ecosystemId, capabilityId);
	    		
		    		if (capabilityName != undefined) {
				    	ecosystems.push({
				    		id: ecosystemId + "/" + capabilityId,
				    		text: capabilityName
				    	});
		    		}
	    		}
	     	}
    	}

    	$("#ecosystems").select2("data", ecosystems);
    	
    	buildTimeline(new Date().getFullYear(), data);
    	
        $("#application-detail").dialog("open");
    }
}

function buildTimeline(year, data) {
	$("#lifecycle-detail").empty();
	eventData = [];
	var id = 0;
	$("#initial-stage").removeClass("lc-unknown lc-candidate lc-emerging lc-mainstream lc-heritage lc-retire lc-remove");
	$("#initial-stage").addClass("lc-unknown");
	$("#initial-stage").attr("data-lifecycle", "unknown");
	if (data.lifecycle != undefined) {
    	for (var i = 0; i < data.lifecycle.length; i++) {
    		if (data.lifecycle[i].date != undefined) {
	    		eventData.push({
	    			id: id++,
	    			type: "stage",
	    			name: data.lifecycle[i].stage,
	    			on: new Date(data.lifecycle[i].date),
	    			eventClass: "gt-lc-" + data.lifecycle[i].stage
	    		});
    		} else {
    			$("#initial-stage").removeClass("lc-unknown");
    			$("#initial-stage").addClass("lc-" + data.lifecycle[i].stage);
    			$("#initial-stage").attr("data-lifecycle", data.lifecycle[i].stage);
    		}
    	}
	}
	
	if (data.milestones != undefined) {
    	for (var i = 0; i < data.milestones.length; i++) {
    		eventData.push({
    			id: id++,
    			type: "investment",
    			name: data.milestones[i].description,
    			index: data.milestones[i].index,
    			on: new Date(data.milestones[i].date),
    			eventClass: "gt-lc-milestone"
    		});
		}
	}
	
	showStage($("#initial-stage").attr("data-lifecycle"));

	showTimeline(year);
}

function deleteSelectedApplication() {
    var selected = table.$('tr.selected');
    if (selected.length !== 0) {
		var data = table.fnGetData(selected[0]);
		repoAction("removeApplicationAction", {
			applicationId: data.applicationId
		});
		
		table.api().row(selected).remove().draw();
   }
}

function newApplication() {
	var id = uuid.v4();
	
	repoAction("addApplicationAction", {
		applicationId: id,
		applicationName: "New application"
	});
	
    table.$('tr.selected').removeClass('selected');
    
	var row = table.api().row.add({
		applicationId: id,
		name: "New application",
		description: "",
		capabilities: [],
		lifecycle: [{stage: "unknown"}]
	}).draw().node();
	
    $(row).addClass('selected');
	
    editSelectedApplication();
}

function showTimeline(year) {
	$("#timeline").empty();
	
	$("#timeline").attr("data-start-year", year);
	
	var numYears = 3;
	
	$("#timeline").jqtimeline({
		events: eventData,
		gap: 66 / numYears,
		startYear: year,
		numYears: numYears,
		click: function(e, event) {
			if (event.type == "stage") {
    			showStage(event.name, new Date(event.on));
			} else if (event.type == "investment") {
				showMilestone(event.name, new Date(event.on), event.index)
			}
		}
	});
}

function showStage(name, date) {
	$("#lifecycle-detail").empty();
	var html;
	$("#lifecycle-detail").attr("data-type", "stage");
	$("#lifecycle-detail").attr("data-stage", name);
	html = '<span class="lc-' + name + '" style="display: inline-block; vertical-align: middle;"></span> <span class="event-name" style="vertical-align: middle;">' + toUpperCamelCase(name);
	if (date != undefined) {
		$("#lifecycle-detail").attr("data-date", date.toISOString());
		html += '</span><span class="event-date" style="vertical-align: middle;"> on ' + formatDate(date) + '</span>';
	} else {
		$("#lifecycle-detail").attr("data-date", "");
		html += ' - initial lifecycle stage</span>';
	}
	$("#lifecycle-detail").html(html);
}

function showMilestone(name, date, index) {
	$("#lifecycle-detail").empty();
	var html;
	$("#lifecycle-detail").attr("data-type", "investment");
	$("#lifecycle-detail").attr("data-description", name);
	$("#lifecycle-detail").attr("data-milestone-index", index);
	$("#lifecycle-detail").attr("data-date", date.toISOString());
	html = '<span class="lc-milestone" style="display: inline-block; vertical-align: middle;"></span><span class="event-date" style="vertical-align: middle;"> ' + formatDate(date) + ': ' + name + '</span>';
	$("#lifecycle-detail").html(html);
}

function updateApplicationList(data) {
    var selected = table.$('tr.selected');
    if (selected.length !== 0) {
    	table.api().row(selected[0]).data(data);
    }
}

function repoUpdateApplicationBasic(id, name, description) {
	repoAction("updateApplicationBasicAction", {
		applicationId: id,
		applicationName: name,
		applicationDescription: description
	});
}

function repoAddCapability(id, capability) {
	repoAction("addCapabilityAction", {
		applicationId: id,
		capability: capability
	});
}

function repoDeleteCapability(id, capability) {
	repoAction("removeCapabilityAction", {
		applicationId: id,
		capability: capability
	});
}

function repoAddEcosystem(id, ecosystem, capability) {
	repoAction("addEcosystemAction", {
		applicationId: id,
		ecosystem: ecosystem,
		capability: capability
	});
}

function repoDeleteEcosystem(id, ecosystem, capability) {
	repoAction("removeEcosystemAction", {
		applicationId: id,
		ecosystem: ecosystem,
		capability: capability
	});
}

function repoAction(action, data, callback) {
	$("#wait-form").dialog("open");
	$.ajax({
        url: unescapeHTML(actionURL) + "&action=" + action,
        type: "POST",
        async: false,
        data: data,
        dataType: "json",
        success: function(response) {
    		$("#wait-form").dialog("close");
    		if (callback != undefined) {
    			callback.call(this, response);
    		}
        },
        error: function (jXHR, textStatus, errorThrown) {
    		$("#wait-form").dialog("close");
            alert(errorThrown);
        }
    });
}

function toUpperCamelCase(str) {
	return str.substring(0,1).toUpperCase() + str.substring(1);
}

var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
var shortMonths = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

function formatDate(date, opts) {
	var _opts = {
		shortMonths: false,
		ordinals: true,
		monthFirst: true
	};
	
	if (opts != undefined) {
		if (opts.shortMonths != undefined) {
			_opts.shortMonths = opts.shortMonths;
		}
		if (opts.ordinals != undefined) {
			_opts.ordinals = opts.ordinals;
		}
		if (opts.monthFirst != undefined) {
			_opts.monthFirst = opts.monthFirst;
		}
	}
	
	var month;
	if (_opts.shortMonths) {
		month = shortMonths[date.getMonth()];
	} else {
		month = months[date.getMonth()];
	}

	var day = "";
	day += date.getDate();
	
	if (_opts.ordinals) {
		var index = date.getDate() % 10;
		if (index == 1) {
			day += 'st';
		} else if (index == 2) {
			day += 'nd';
		} else if (index == 3) {
			day += 'rd';
		} else {
			day += 'th';
		}
	}
	
	var dateString;
	if (_opts.monthFirst) {
		dateString = month + " " + day + " ";
	} else {
		dateString = day + " " + month + " ";
	}
	
	dateString += date.getFullYear();
	
	return dateString;
}

function unescapeHTML(html) {
   return $("<div />").html(html).text();
}

function escapeHTML(html) {
   return $("<div />").text(html).html();
}