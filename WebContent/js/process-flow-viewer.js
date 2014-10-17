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
	}).show();
		
	$("#docx-download").button().click(function () {
		downloadURL(unescapeHTML(docxURL));
	}).show();
		
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
	}
	
	function showFlow(element) {
		var flowId = $("#process-flow-select").val();
		$("#flow-diagram").empty();
		
		var processElements = [];
		for (var i = 0; i < processes.processFlows.length; i++) {
			if (processes.processFlows[i].name == flowId) {
				var flow = processes.processFlows[i];
				for (var j = 0; j < flow.instances.length; j++) {
					processElements.push({
						instance: flow.instances[j],
						process: findProcess(flow.instances[j].process)
					});
				}
			}
		}
		
		layoutProcessElements(processElements);
		
		drawFlow(processElements);
	}
	
	function drawFlow(flow) {
		var unitWidth = PROCESS_FULL_WIDTH / resolution;

		var $table = $("<div/>", {
			"class": "flow-table",
			"style": "width: " + PROCESS_FULL_WIDTH + "px;"
		});
		for (var i = 0; i < flow.length; i++) {
			var process = flow[i];
			var $row = $("<div/>", {
				"class": "flow-row"
			});
			$table.append($row);
			var start = unitWidth * process.start;
			var width = unitWidth * (process.end - process.start);		
			
			var $pre = $("<div/>", {
				"class": "flow-cell",
				"style": "width: " + start + "px;"
			});
			var $process = $("<div/>", {
				"class": "flow-cell flow-process",
				"style": "width: " + width + "px;"
			});
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
			for (var j = 0; j < parentProcess.instance.parents.length; j++) {
				var p = parentProcess.instance.parents[j];
				if (parentProcess.instance.process = p) {
					if (childProcess.start <= parentProcess.start) {
						changed = true;
						childProcess.start = parentProcess.start + 1;
					}
					if (childProcess.end - childProcess.start < processDuration2(childProcess, parentProcess)) {
						changed = true;
						childProcess.end = childProcess.start + processDuration2(childProcess, parentProcess);
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