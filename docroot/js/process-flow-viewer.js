(function($) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    //alert(versions);
    
    var processes;
	var processElements = [];
    var resolution = 15;
    var PROCESS_DURATION = 5;
    var PROCESS_FULL_WIDTH = 800;

	$("#pptx-download").button().click(function () {
		downloadURL(unescapeHTML(pptxURL));
	});
		
	$("#docx-download").button().click(function () {
		downloadURL(unescapeHTML(docxURL));
	});
	
	$("#edit-flow").button().click(openTree);
	
	$("#new-flow").button().click(addFlow);
	
	$("#copy-flow").button().click(copyFlow);
	
	$("#delete-flow").button().click(deleteFlow);
	
	$("#edit-process-instance").dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        modal: true,
        buttons: {
        	"Save": function() {
        		if (saveProcessInstance()) {
    				$(this).dialog( "close" );
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

	$("#edit-parent-dependency").dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        modal: true,
        buttons: {
        	"Save": function() {
        		if (saveParent()) {
    				$(this).dialog( "close" );
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
	
	$("#edit-predecessor-dependency").dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        modal: true,
        buttons: {
        	"Save": function() {
        		if (savePredecessor()) {
    				$(this).dialog( "close" );
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
	
	$("#contiguous").button();
	
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
		$('#process-flow-select').selectric("destroy");
		$('#process-flow-select').empty();
		
		$('#process-flow-select').append('<option value="null">Select a process flow...</option>');
		for (var i = 0; i < flows.length; i++) {
    		$("#process-flow-select").append('<option value="' + flows[i].id + '">' +flows[i].name + '</option>')
    	}

    	$('#process-flow-select').selectric({
    		expandToItemText: true,
    		maxHeight: 400,
    		onChange: showFlow
    	});
    	
    	$("#toolbar-top").css("display", "table");
	}
	
	function showFlow(element) {
		var flowId = $("#process-flow-select").val();
		$("#flow-diagram").empty();
		
		var flowName = "";
		processElements = [];
		for (var i = 0; i < processes.processFlows.length; i++) {
			if (processes.processFlows[i].id == flowId) {
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
			$("#toolbar-bottom").show();
		} else {
			$("#toolbar-bottom").hide();
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
		$('#flow-tree').empty();
		
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
					flowId: flowId,
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
						processId: predecessorProcess.id,
						contiguous: flow.instance.predecessors[j].contiguous
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
		}).on('move_node.jstree', moveInstance).on("rename_node.jstree", applyRenameFlow);	
	}
	
	function contextMenu(node, callback) {
		var path = $.jstree.reference('#flow-tree').get_path(node, false, true);
		
		if (path.length > 1 && path[0] == 'trash') {
			return {};
		} else if (node.type == 'flow') {
			return {
				'rename': {'label': 'Rename', 'action': renameFlow },
				'add-instance': {'label': 'New Process Instance', 'action': addInstance }
			};
		} else if (node.type == 'instance') {
			return {
				'edit': {'label': 'Edit', 'action': editInstance },
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
				'edit': {'label': 'Edit', 'action': editPredecessor },
				'delete': {'label': 'Delete', 'action': deleteItem }
			};
		}
	}
	
	function refresh(response) {
		var flowId = $("#process-flow-select").val();

		processes = response;
    	buildFlowSelect(processes.processFlows);

    	$("#process-flow-select").val(flowId);
    	$("#process-flow-select").selectric("refresh");

    	showFlow();	
	}
		
	function addFlow() {
		var data = {
			flowId: uuid.v4()
		};
		
		repoAction("addFlowAction", data, refresh);
	}

	function copyFlow() {
		var flowId = $("#process-flow-select").val();
		var data = {
			flowId: flowId,
			copyId: uuid.v4()
		};
		
		repoAction("copyFlowAction", data, refresh);
	}

	function deleteFlow() {
		var flowId = $("#process-flow-select").val();
		var data = {
			flowId: flowId
		};
		
		repoAction("deleteFlowAction", data, refresh);
	}

	function renameFlow(menu) {
		var $tree = $.jstree.reference('#flow-tree')
		var node = $tree.get_node(menu.reference.context);
		
		$tree.edit(node);
	}

	function applyRenameFlow(eventObject, eventData) {
		var node = eventData.node;
		var flowId = $("#process-flow-select").val();
		
		var data = {
			flowId: flowId,
			instanceId: node.data.processId,
			name: eventData.text
		};
		
		// Done
		repoAction("renameFlowAction", data)
		
		$("#process-flow-select option[value='" + flowId + "']").text(eventData.text);
		$("#process-flow-select").selectric("refesh");
	}
	
	function moveInstance(eventObject, data) {
		var node = data.node;
		var flowId = $("#process-flow-select").val();
		
		var data = {
			flowId: node.data.flowId,
			instanceId: node.data.processId,
			position: data.position
		};
		
		// Done
		repoAction("moveInstanceAction", data, refresh);
	}
	
	function addInstance(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		$("#process-selector-row").show();
		
		$("#edit-process-instance").data("mode", "add");		
		$("#edit-process-instance").dialog("open");		
	}
	
	function editInstance(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		
		$("#process-selector-row").hide();
		
		$("#process-duration-select").val(node.data.duration);
		$("#process-duration-select").selectric("refesh");
		
		$("#edit-process-instance").data("mode", "edit");		
		$("#edit-process-instance").data("instanceId", node.data.processId);		
		$("#edit-process-instance").dialog("open");		
	}
	
	function saveProcessInstance() {
		var flowId = $("#process-flow-select").val();
		var instanceId = $("#edit-process-instance").data("instanceId");
		
		if ($("#edit-process-instance").data("mode") == "add") {
			var $tree = $.jstree.reference('#process-selector-tree');
			var selected = $tree.get_node($tree.get_selected()[0]);
			instanceId = selected.data.id;
		}
		
		var data = {
			flowId: flowId,
			instanceId: instanceId,
			mode:  $("#edit-process-instance").data("mode"),
			duration: $("#process-duration-select").val()
		};
			
		// Done
		repoAction("updateInstanceAction", data, refresh);
		
		return true;
	}

	function addParent(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		buildFlowProcessTree("parent-selector-tree");
		
		$("#edit-parent-dependency").data("instanceId", node.data.processId);		
		$("#edit-parent-dependency").dialog("open");		
	}

	function saveParent() {
		var flowId = $("#process-flow-select").val();
		var instanceId = $("#edit-parent-dependency").data("instanceId");
		
		var $tree = $.jstree.reference('#parent-selector-tree');
		var selected = $tree.get_node($tree.get_selected()[0]);
		var parentId = selected.data.processId;
		
		var data = {
			flowId: flowId,
			instanceId: instanceId,
			parentId: parentId
		};
			
		// Done
		repoAction("addParentAction", data, refresh);
		
		return true;
	}

	function addPredecessor(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		buildFlowProcessTree("predecessor-selector-tree");
		$("#predecessor-selector-row").show();
		
		$("#contiguous").prop("checked", true);
		$("#contiguous").button("refresh");
		
		$("#edit-predecessor-dependency").data("instanceId", node.data.processId);		
		$("#edit-predecessor-dependency").dialog("open");		
	}

	function editPredecessor(menu) {
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#flow-tree').get_parent(node);
		
		$("#predecessor-selector-row").hide();
		
		$("#contiguous").prop("checked", node.data.contiguous);
		$("#contiguous").button("refresh");
		
		$("#edit-predecessor-dependency").data("instanceId", node.data.processId);		
		$("#edit-predecessor-dependency").dialog("open");		
	}
	
	function savePredecessor() {
		var flowId = $("#process-flow-select").val();
		var instanceId = $("#edit-predecessor-dependency").data("instanceId");
		
		var $tree = $.jstree.reference('#predecessor-selector-tree');
		var selected = $tree.get_node($tree.get_selected()[0]);
		var predecessorId = selected.data.processId;
		
		var data = {
			flowId: flowId,
			instanceId: instanceId,
			predecessorId: predecessorId,
			contiguous: $("#contiguous").prop("checked") ? "true" : "false"
		};
			
		// Done
		repoAction("updatePredecessorAction", data, refresh);
		
		return true;
	}

	function deleteItem(menu) {
		var flowId = $("#process-flow-select").val();
		var node = $.jstree.reference('#flow-tree').get_node(menu.reference.context);
		var parentNode = $.jstree.reference('#flow-tree').get_node($.jstree.reference('#flow-tree').get_parent(node));
		
		var data = {
			flowId: flowId,
			type: node.type
		};
		switch (node.type) {
		case "flow":
			break;
		case "instance":
			data.instanceId = node.data.processId;
			break;
		case "parent":
		case "predecessor":
			data.instanceId = parentNode.data.processId;
			data.dependencyId = node.data.processId;
			break;
		}
				
		// Done
		repoAction("deleteItemAction", data, refresh);
	}
	
	function buildFlowProcessTree(target) {
		$('#' + target).jstree("destroy");
		
		var tree = [];
		
		for (var i = 0; i < processElements.length; i++) {
			var flow = processElements[i];
			var node = {
				text: flow.process.name,
				type: "instance",
				data: {
					processId: flow.process.id
				},
				children: []
			};
			
			tree.push(node);
		}

		$('#' + target).jstree({
			'core': {
				'data': tree,
				'multiple': false,
				'check_callback': true,
				'themes': {
					'variant': 'small',
					'responsive': false
				}
			},
			'types': {
				'#': {
					'valid_children': ['instance']
				},
				'instance': {
					'valid_children': [],
					'icon': iconURL + "&amp;icon=process.ico"
				}
			},
			'plugins': ['types']
		});	
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
        			callback.call(this, response);
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