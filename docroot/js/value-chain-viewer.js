(function($, uuid) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    versions += "\njstree: " + $.jstree.version;
    //versions += "\njqte: " + $.jqte.version; // Can't check version of jQTE
    //alert(versions);

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
			'is_draggable': function (node) {
				return (node.type == 'primary-activity' || node.type == 'support-activity') ? true : false;
			}
		},
		'types': {
			'#': {
				'valid_children': ['primary', 'support', 'trash']
			},
			'primary': {
				'valid_children': ['primary-activity']
			},
			'support': {
				'valid_children': ['support-activity']
			},
			'primary-activity': {
				'valid_children': [],
				'icon': iconURL + "&amp;icon=technology-domain.ico"
			},
			'support-activity': {
				'valid_children': [],
				'icon': iconURL + "&amp;icon=technology-domain.ico"
			},
			'trash': {
				'icon': iconURL + "&amp;icon=trash.ico"
			}
		},
		'contextmenu': {
			'show_at_node': false,
			'items': contextMenu
		},
		'plugins': ['dnd', 'types', 'contextmenu']
	}).on('move_node.jstree', moveNode).on('copy_node.jstree', copyNode).on("rename_node.jstree", updateNode).on("ready.jstree", showValueChain);

	$("#edit-button").button().click(openTree).show();
	
	function openTree() {
		$("#value-chain-tree").css("display", "table-row");
		$("#edit-button").button("option", "label", "Close").click(closeTree);
	}
	
	function closeTree() {
		$("#value-chain-tree").css("display", "none");
		$("#edit-button").button("option", "label", "Edit").click(openTree);
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
				'add-primary': {'label': 'New Primary Activity', 'action': addValueChain }
			};
		} else if (node.type == 'support') {
			return {
				'add-support': {'label': 'New Support Activity', 'action': addValueChain }
			};
		} else if (node.type == 'primary-activity') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem },
				'rename': {'label': 'Rename', 'action': renameNode }
			};
		} else if (node.type == 'support-activity') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem },
				'rename': {'label': 'Rename', 'action': renameNode }
			};
		} else if (node.type == 'trash') {
			return {
				'empty': {'label': 'Empty Recycle Bin', 'action': emptyTrash }
			};
		}
	}
		
	function renameNode(menu) {
		var tree = $.jstree.reference('#jstree')
		var node = tree.get_node(menu.reference.context);
		
		tree.edit(node);
	};
	
	function addValueChain(menu) {
		var tree = $.jstree.reference('#jstree');
		var node = tree.get_node(menu.reference.context);
		
		var newNode = tree.get_node(addChildItem(node, node.type + "-activity", 'New value chain activity'));
		
		var data = {
			valueChainId: newNode.data.id,
			name: newNode.text,
			type: node.type + "-activity"
		};
		
		tree.edit(newNode);

		//repoAction("addValueChainAction", data);
	}
	
	function addChildItem(node, type, label) {
		return $.jstree.reference('#jstree').create_node(node, {
			'text': label,
			'type': type,
			'data': {
				'type': type,
				'id': uuid.v4()
			}
		});
	}

	function deleteItem(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
		
		$.jstree.reference('#jstree').move_node(node, $.jstree.reference('#jstree').get_node('trash'));
	}

	function emptyTrash(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
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
			valueChainId: node.data.id,
			from: oldParent.type,
			to: parent.type,
			position: data.position
		};
		
		repoAction("moveValueChainAction", data);

		showValueChain();
	}
	
	function copyNode(eventObject, data) {
		data.node.data = data.original.data;
		data.node.data.id = uuid.v4();

		// Not currently permitted - need to decide whether to implement this as it would require the ids to be set for the whole of the copied tree
	}

	function updateNode(eventObject, data) {
		var node = data.node;
	
		var data = {
			valueChainId: node.data.id,
			type: node.type,
			name: data.text
		};
		
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
            	alert("Unable to retrieve framework data\n" + status + ": " + reason);
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

})($, uuid);