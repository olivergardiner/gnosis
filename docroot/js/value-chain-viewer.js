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

$('#jstree').jstree({
	'core': {
		'data': getJsonData,
		'multiple': false,
		'check_callback': true,
		'themes': {
			'variant': 'small',
			'responsive': false
		}
	},
	'dnd': {
		'copy': false,
		'is_draggable': function (nodes) {
			return (nodes[0].type == 'activity' || nodes[0].type == 'ecosystem' || nodes[0].type == 'capability') ? true : false;
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
	'contextmenu': {
		'show_at_node': false,
		'items': contextMenu
	},
	'plugins': ['dnd', 'types', 'contextmenu']
}).on('select_node.jstree', function(e, data) {
	showNode(data.node);
}).on('move_node.jstree', moveNode).on('copy_node.jstree', copyNode);

$('#description-editor').jqte({
});

$('#edit-node-form').dialog({
    autoOpen: false,
    height: 500,
    width: 700,
    modal: true,
    buttons: {
    	"Save": function() {
    		var tree = $.jstree.reference('#jstree');
    		var name = $('#name-editor').val();
    		var description = $('#edit-node-form div.jqte_editor').html();

    		var node = tree.get_node($("#edit-node-form").data("node"));
    		
    		node.text = name;
    		node.data.description = description;
    		
    		updateNode(node);
    		
    		tree.refresh();
    		
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

$('#capability-jstree').jstree({
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
	var tree = $.jstree.reference('#capability-jstree');
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

$('#capability-form').dialog({
	title: "Add a capability",
    autoOpen: false,
    height: 500,
    width: 600,
    modal: true,
    buttons: { "Add": applyAddCapability, "Close": closeCapabilityForm }
});

function closeCapabilityForm() {
	$('#capability-form').dialog("close");
}

function applyAddCapability() {
	var tree = $.jstree.reference('#jstree');
	var capabilityTree = $.jstree.reference('#capability-jstree');
	var node = tree.get_node($("#capability-form").data("node"));
	var capabilityNode = capabilityTree.get_selected(true)[0];
	
	var newNodeId = addChildItem(node, "capability", 'New capability instance', capabilityNode.data.id);
	var newNode = tree.get_node(newNodeId);
	newNode.data.description = "";
	
	$('#capability-form').dialog( "close" );

	repoAction("createCapabilityAction", {
		ecosystemId: node.data.id,
		capabilityId: newNode.data.id,
		name: newNode.text,
		description: newNode.description
	});

	//$("#edit-node-form").data("node", newNode.id);
	//$('#name-editor').val(newNode.label);
	//$('#description-editor').jqteVal(newNode.data.description);
	
	//$('#edit-node-form').dialog("open");
}
	
function showNode(node){
	var type = node.type;
	
	if (type == 'root' || type == 'primary' || type == 'support' || type == 'trash') {
		$('#tree-panel').show();
		$('#detail-panel').hide();
	} else {
		$('#tree-panel').hide();
		
		$('#detail-name').text(node.text);
		$('#detail-description').html(unescapeHTML(node.data.description));
		
		if (type == 'capability') {
			$('#detail-capability-name').text(node.data.name);
			$('#detail-capability').show();
		} else {
			$('#detail-capability').hide();
		}
		
		$('#detail-panel').show();
	}
}

function showValueChain() {
	var tree = $.jstree.reference('#jstree');
	var json = tree.get_json();

	$("#primary-value-chain").empty();
	$("#support-value-chain").empty();
	
	var primaryActivites = json[0];
	var width = 100 / primaryActivites.children.length;
	for (var i = 0; i < primaryActivites.children.length; i++) {
		var activity = primaryActivites.children[i];
		$("#primary-value-chain").append('<div style="padding: 2px; display: table-cell; width: ' + width.toFixed(0) + '%;"><div class="primary-activity"><div class="gnosis-middle">' + activity.text + '<div></div></div>');
	}
	
	var supportActivites = json[1];
	for (var i = 0; i < supportActivites.children.length; i++) {
		var activity = supportActivites.children[i];
		$("#support-value-chain").append('<div style="display: table-row"><div style="padding: 2px; display: table-cell; width: 100%;"><div class="support-activity"><div class="gnosis-middle">' + activity.text + '</div></div></div></div>');
	}
}

function contextMenu(node, callback) {
	var path = $.jstree.reference('#jstree').get_path(node, false, true);
	
	if (path.length > 1 && path[0] == 'trash') {
		return {};
	} else if (node.type == 'primary') {
		return {
			'add-primary': {'label': 'New Primary Activity', 'action': addActivity }
		};
	} else if (node.type == 'support') {
		return {
			'add-support': {'label': 'New Support Activity', 'action': addActivity }
		};
	} else if (node.type == 'activity') {
		return {
			'add-ecosystem': {'label': 'New Ecosystem', 'action': addEcosystem },
			'delete': {'label': 'Delete', 'action': deleteItem },
			'edit': {'label': 'Edit', 'action': editNode }
		};
	} else if (node.type == 'ecosystem') {
		return {
			'add-capability': {'label': 'Include Capability', 'action': addCapability },
			'delete': {'label': 'Delete', 'action': deleteItem },
			'edit': {'label': 'Edit', 'action': editNode }
		};
	} else if (node.type == 'capability') {
		return {
			'delete': {'label': 'Delete', 'action': deleteItem },
			'edit': {'label': 'Edit', 'action': editNode }
		};
	} else if (node.type == 'trash') {
		return {
			'empty': {'label': 'Empty Recycle Bin', 'action': emptyTrash }
		};
	}
}
	
function renameNode(menu) {
	var tree = $.jstree.reference('#jstree')
	var node = tree.get_node(menu.reference[0]);
	
	tree.edit(node);
};

function editNode(menu) {
	var tree = $.jstree.reference('#jstree')
	var node = tree.get_node(menu.reference[0]);
	$("#edit-node-form").data("node", node.id);
	$('#name-editor').val(node.text);
	$('#description-editor').jqteVal(node.data.description);
	if (node.type == 'capability') {
		$('#capability-name').text(node.data.name);
		$('#capability-row').css("display", "table-row");
	} else {
		$('#capability-row').css("display", "none");
	}
	
	$('#edit-node-form').dialog("open");
};

function addActivity(menu) {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_node(menu.reference[0]);
	
	var newNodeId = addChildItem(node, "activity", 'New activity');
	var newNode = tree.get_node(newNodeId);
	newNode.data.description = "";
	
	repoAction("createActivityAction", {
		type: node.type,
		activityId: newNode.data.id,
		name: newNode.text,
		description: newNode.description
	});

	//$("#edit-node-form").data("node", newNode.id);
	//$('#name-editor').val(newNode.label);
	//$('#description-editor').jqteVal(newNode.data.description);
	
	//$('#edit-node-form').dialog("open");
}

function addEcosystem(menu) {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_node(menu.reference[0]);
	
	var newNodeId = addChildItem(node, "ecosystem", 'New ecosystem');
	var newNode = tree.get_node(newNodeId);
	newNode.data.description = "";
	
	repoAction("createEcosystemAction", {
		activityId: node.data.id,
		ecosystemId: newNode.data.id,
		name: newNode.text,
		description: newNode.description
	});

	//$("#edit-node-form").data("node", newNode.id);
	//$('#name-editor').val(newNode.label);
	//$('#description-editor').jqteVal(newNode.data.description);
	
	//$('#edit-node-form').dialog("open");
}

function addCapability(menu) {
	$("#capability-form").data("node", menu.reference[0]);
	
	$('#capability-form').dialog( "open" );
}

function addChildItem(node, type, label, id) {
	var nodeData = {
		'text': label,
		'type': type,
		'data': {
			'type': type,
			'id': uuid.v4()
		}
	}
	
	if (id != undefined) {
		nodeData.data.id = id;
	}
	
	return $.jstree.reference('#jstree').create_node(node, nodeData);
}

function deleteItem(menu) {
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
	
	$.jstree.reference('#jstree').move_node(node, $.jstree.reference('#jstree').get_node('trash'));
}

function emptyTrash(menu) {
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	
	while (node.children.length > 0) {
		var deleteNode = $.jstree.reference('#jstree').get_node(node.children[0]);
		$.jstree.reference('#jstree').delete_node(deleteNode);
	}

	repoAction("emptyTrashAction");
}

function moveNode(eventObject, data) {
	var node = data.node;
	var parent = $.jstree.reference('#jstree').get_node(data.parent);
	var oldParent = $.jstree.reference('#jstree').get_node(data.old_parent);
	
	var data = {
		id: node.data.id,
		from: oldParent.type,
		to: parent.type,
		position: data.position
	};
	
	if (oldParent.data != null){
		data.fromId = oldParent.data.id;
	}
	
	if (parent.data != null){
		data.toId = parent.data.id;
	}
	
	repoAction("moveValueChainAction", data);

	showValueChain();
}

function copyNode(eventObject, data) {
	data.node.data = data.original.data;
	data.node.data.id = uuid.v4();

	// Not currently permitted - need to decide whether to implement this as it would require the ids to be set for the whole of the copied tree
}

function updateNode(node) {
	var tree =$.jstree.reference('#jstree');
	var data = {
		type: node.type,
		name: node.text,
		description: node.data.description
	};
	
	if (node.type == "activity") {
		data.activityId = node.data.id;
	} else if (node.type == "ecosystem") {
		var activity = tree.get_parent(node);
		var activityNode = tree.get_node(activity);
		data.activityId = activityNode.data.id;
		data.ecosystemId = node.data.id;		
	} else if (node.type == "capability") {
		var ecosystem = tree.get_parent(node);
		var ecosystemNode = tree.get_node(ecosystem);
		var activity = tree.get_parent(ecosystemNode);
		var activityNode = tree.get_node(activity);
		data.activityId = activityNode.data.id;
		data.ecosystemId = ecosystemNode.data.id;
		data.capabilityId = node.data.id;		
	}
	
	repoAction("updateValueChainAction", data);

	showValueChain();
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

function getJsonData(obj, callback) {
	$.ajax({
        async : true,
        type : "GET",
        url : jsonDataURL,
        dataType : "json",
        success : function(response) {
    		callback.call(this, response);
        },
        error : function(jqXhr, status, reason) {
        	alert("Unable to retrieve value chain data\n" + status + ": " + reason);
        }
	});
}

function unescapeHTML(html) {
   return $("<div />").html(html).text();
}

function escapeHTML(html) {
   return $("<div />").text(html).html();
}

function sanitiseData(source) {
	var data = {
		'text': source.text,
		'type': source.type,
		'data': source.data
	};
	if (typeof source.children != 'undefined') {
		data.children = [];
		for (var i = 0; i < source.children.length; i++) {
			var child = source.children[i];
			if (child.type != 'dependency') {
				data.children.push(sanitiseData(child));
			}
		}
	}
	return data;
}