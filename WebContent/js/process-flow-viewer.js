(function($) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    //alert(versions);
    
    var processes;
    var resolution = 15;
    var PROCESS_DURATION = 5;
    var PROCESS_FULL_WIDTH = 800;

	$("#pptx-download").button().click(function () {
		downloadURL(unescapeHTML(pptxURL));
	});
		
	$("#docx-download").button().click(function () {
		downloadURL(unescapeHTML(docxURL));
	});
	
	$("#edit-button").button().click(openTree);
	
	$("#edit-process-instance").dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        modal: true,
        buttons: {
        	"Save": selectProcess,
	    	"Cancel": function() {
				$(this).dialog( "close" );
	        }
        },
        open: function() {
        },
        close: function() {
        }
	});
	
	$('#process-selector-tree').jstree({
		'core': {
			'data': getProcessJsonData,
			'multiple': false,
			'check_callback': true,
			'themes': {
				'variant': 'small',
				'responsive': false
			}
		},
		'types': {
			'#': {
			},
			'activity': {
			},
			'domain': {
				'icon': iconURL + "&amp;icon=process-domain.ico"
			},
			'process': {
				'icon': iconURL + "&amp;icon=process.ico"
			}
		},
		'plugins': ['types']
	});

	$.ajax({
        async : true,
        type : "GET",
        url : jsonDataURL,
        dataType : "json",
        success : function(response) {
        	processes = response;
        	buildFlowSelect(processes.processFlows);
        },
        error : function(jqXhr, status, reason) {
        	alert("Unable to retrieve framework data\n" + status + ": " + reason);
        }
	});
	
	function buildFlowSelect(flows) {
    	for (var i = 0; i < flows.length; i++) {
    		$("#process-flow-select").append('<option value="' + flows[i].name + '">' +flows[i].name + '</option>')
    	}

    	$('#process-flow-select').selectric({
    		expandToItemText: true,
    		maxHeight: 200,
    		onChange: showFlow
    	});
    	
    	$("#toolbar").css("display", "table");
	}
	
	function showFlow(element) {
		var flowId = $("#process-flow-select").val();
		$("#flow-diagram").empty();
		
		var flowName = "";
		var processElements = [];
		for (var i = 0; i < processes.processFlows.length; i++) {
			if (processes.processFlows[i].name == flowId) {
				var flow = processes.processFlows[i];
				flowName = flow.name;
				for (var j = 0; j < flow.instances.length; j++) {
					processElements.push({
						start: 0,
						end: 0,
						instance: flow.instances[j],
						process: findProcess(flow.instances[j].process)
					});
				}
			}
		}
		
		layoutProcessElements(processElements);
		
		drawFlow(processElements);
		
		buildTree(processElements, flowId, flowName);
		
		closeTree();

		if (flowId != 'null') {
			$("#edit-button").button().show();
		} else {
			$("#edit-button").button().hide();
		}
	}
	
	function openTree() {
		$("#flow-editor").show();
		$("#edit-button").button("option", "label", "Close").click(closeTree);
	}
	
	function closeTree() {
		$("#flow-editor").hide();
		$("#edit-button").button("option", "label", "Edit").click(openTree);
	}
	
	function buildTree(flows, flowId, flowName) {
		$('#flow-tree').jstree("destroy");
		
		var tree = [{
			text: flowName,
			type: "flow",
			data: {
				flowId: flowId
			},
			children: []
		}];
		
		for (var i = 0; i < flows.length; i++) {
			var flow = flows[i];
			var node = {
				text: flow.process.name,
				type: "instance",
				data: {
					flowId: flow.instance.id,
					processId: flow.process.id,
					duration: flow.instance.duration
				},
				children: []
			};
			
			for (var j = 0; j < flow.instance.parents.length; j++) {
				var parentProcess = findProcess(flow.instance.parents[j]);
				node.children.push({
					text: parentProcess.name,
					type: "parent",
					data: {
						processId: parentProcess.id
					}
				});
			}
			
			for (var j = 0; j < flow.instance.predecessors.length; j++) {
				var predecessorProcess = findProcess(flow.instance.predecessors[j].predecessor);
				node.children.push({
					text: predecessorProcess.name,
					type: "predecessor",
					data: {
						processId: predecessorProcess.id
					}
				});
			}
			
			tree[0].children.push(node);
		}

		$('#flow-tree').jstree({
			'core': {
				'data': tree,
				'multiple': false,
				'check_callback': true,
				'themes': {
					'variant': 'small',
					'responsive': false
				}
			},
			'dnd': {
				'copy': false,
				'is_draggable': function (node) {
					return (node.type != 'flow') ? true : false;
				}
			},
			'types': {
				'#': {
					'valid_children': ['flow']
				},
				'flow': {
					'valid_children': ['instance'],
					'icon': iconURL + "&amp;icon=technology-domain.ico"
				},
				'instance': {
					'valid_children': ['parent', 'predecessor'],
					'icon': iconURL + "&amp;icon=process.ico"
				},
				'parent': {
					'valid_children': [],
					'icon': iconURL + "&amp;icon=parent-process.ico"
				},
				'predecessor': {
					'valid_children': [],
					'icon': iconURL + "&amp;icon=predecessor-process.ico"
				}
			},
			'contextmenu': {
				'show_at_node': false,
				'items': contextMenu
			},
			'plugins': ['dnd', 'types', 'contextmenu']
		});	
	}
	
	function contextMenu(node, callback) {
		var path = $.jstree.reference('#flow-tree').get_path(node, false, true);
		
		if (path.length > 1 && path[0] == 'trash') {
			return {};
		} else if (node.type == 'flow') {
			return {
				'add-instance': {'label': 'New Process Instance', 'action': addInstance }
			};
		} else if (node.type == 'instance') {
			return {
				'add-parent': {'label': 'New Parent Dependency', 'action': addParent },
				'add-predecessor': {'label': 'New Predecessor Dependency', 'action': addPredecessor },
				'delete': {'label': 'Delete', 'action': deleteItem }
			};
		} else if (node.type == 'parent') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem }
			};
		} else if (node.type == 'predecessor') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem }
			};
		}
	}
		
	function addInstance(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		$("#edit-process-instance").dialog("open");		
	}
	
	function selectProcess() {
		
	}

	function addParent(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		$("#edit-parent-dependency").dialog("open");		
	}

	function addPredecessor(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		$("#edit-predecessor-dependency").dialog("open");		
	}
	
	function deleteItem(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
	}

	
	function drawFlow(flows) {
		var unitWidth = PROCESS_FULL_WIDTH / resolution;
		
		for (var i = 0; i < flows.length; i++) {
			var flow = flows[i];

			var $table = $("<div/>", {
				"class": "flow-table",
				"style": "width: " + PROCESS_FULL_WIDTH + "px;"
			});
			
			$("#flow-diagram").append($table);
			
			var $row = $("<div/>", {
				"class": "flow-row"
			});
			
			$table.append($row);
			
			var start = unitWidth * flow.start;
			var width = unitWidth * (flow.end - flow.start);		
			
			var $pre = $("<div/>", {
				"class": "flow-cell",
				"style": "width: " + start.toFixed(0) + "px;"
			});
			
			var $process = $("<div/>", {
				"class": "flow-cell flow-process",
				"style": "width: " + width.toFixed(0) + "px;"
			});

			var $processLabel = $("<div/>", {
				"class": "flow-label"
			});
			$processLabel.text(flow.process.name);
			$process.append($processLabel);
			
			var $post = $("<div/>", {
				"class": "flow-cell",
				"style": "width: auto;"
			});
			
			$row.append($pre).append($process).append($post);
		}
	}

	function findProcess(processId) {
		for (var i = 0; i < processes.processDomains.length; i++) {
			var domain = processes.processDomains[i];
			for (var j = 0; j < domain.processes.length; j++) {
				if (domain.processes[j].id == processId) {
					return domain.processes[j];
				}
			}
		}
	}
	
	function layoutProcessElements(processElements) {
    	var changed = true;
    	var maxEnd = 0;
    	var layoutCount = 0;
    	while (changed && layoutCount < 20) {
    		changed = false;
    		for (var i = 0; i < processElements.length; i++) {
    			var process = processElements[i];
        		if (process.instance.parents.length == 0 && process.instance.predecessors.length == 0) {
        			process.start = 0;
        			process.end = maxEnd;
        		}	
    			if (alignChildProcesses(processElements, process)) {
    	        	changed = true;
    			}
    			if (alignPredecessors(processElements, process)) {
    				changed = true;
    			}
    			
    			if (process.end > maxEnd) {
    				maxEnd = process.end;
    			}
        	}
        	layoutCount++;
    	}
    	
    	resolution = maxEnd;
	}

	function alignPredecessors(areaProcesses, followingProcess) {
		var changed = false;

		for (var i = 0; i < areaProcesses.length; i++) {
			var predecessor = areaProcesses[i];
			for (var j = 0; j < followingProcess.instance.predecessors.length; j++) {
				var p = followingProcess.instance.predecessors[j];
				if (predecessor.instance.process == p.predecessor) {
					var contiguous = true;
					if (p.contiguous != undefined) {
						contiguous = p.contiguous;
					}
					var offset = (contiguous) ? 0 : 2;
					if (predecessor.end + offset > followingProcess.start) {
						changed = true;
						followingProcess.start = predecessor.end + offset;
					}
					if (followingProcess.end - followingProcess.start < processDuration(followingProcess)) {
						changed = true;
						followingProcess.end = followingProcess.start + processDuration(followingProcess);
					}
				}
			}
		}

		return changed;
	}
	
	function alignChildProcesses(areaProcesses, parentProcess) {
		var changed = false;
		for (var i = 0; i < areaProcesses.length; i++) {
			var childProcess = areaProcesses[i];
			for (var j = 0; j < childProcess.instance.parents.length; j++) {
				var p = childProcess.instance.parents[j];
				if (parentProcess.instance.process == p) {
					if (childProcess.start <= parentProcess.start) {
						changed = true;
						areaProcesses[i].start = parentProcess.start + 1;
					}
					if (childProcess.end - childProcess.start < processDuration2(childProcess, parentProcess)) {
						changed = true;
						areaProcesses[i].end = childProcess.start + processDuration2(childProcess, parentProcess);
					}
					if (childProcess.end >= parentProcess.end) {
						changed = true;
						parentProcess.end = childProcess.end + 1;
					}
				}
			}
		}
		
		return changed;
	}
	
	function processDuration(process) {
		var duration = PROCESS_DURATION;
		if (process.instance.duration != null) {
			switch (process.instance.duration) {
			case "shorter":
				duration--;
			case "short":
				duration--;
				break;
			case "longer":
				duration++;
			case "long":
				duration++;
				break;
			default:
				break;
			}
		}
		
		return duration;
	}
	
	function processDuration2(process, parentProcess) {
		var duration = processDuration(process);
		if (process.instance.duration != undefined && process.instance.duration == "always") {
				duration = parentProcess.end - process.start - 1;
				if (duration < PROCESS_DURATION) {
					duration = PROCESS_DURATION;
				}
		}
		
		return duration;
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
        			callback.call(response);
        		}
            },
            error: function (jXHR, textStatus, errorThrown) {
        		$("#wait-form").dialog("close");
                alert(errorThrown);
            }
        });
	}
	
	function getProcessJsonData(obj, callback) {
		$.ajax({
            async : true,
            type : "GET",
            url : processJsonDataURL,
            dataType : "json",
            success : function(response) {
        		callback.call(this, response);
            },
            error : function(jqXhr, status, reason) {
            	alert("Unable to retrieve framework data\n" + status + ": " + reason);
            }
		});
	}
	
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