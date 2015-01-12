(function($, uuid) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    versions += "\njstree: " + $.jstree.version;
    //versions += "\njqte: " + $.jqte.version; // Can't check version of jQTE
    //alert(versions);
    
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
			'is_draggable': function (node) {
				return (node.data.type == 'root') ? false : true;
			}
		},
		'types': {
			'#': {
				'valid_children': ['root', 'trash']
			},
			'root': {
				'valid_children': ['tower']
			},
			'tower': {
				'valid_children': ['service-group', 'dependency'],
				'icon': iconURL + "&amp;icon=tower.ico"
			},
			'service-group': {
				'valid_children': ['service-sub-group', 'dependency'],
				'icon': iconURL + "&amp;icon=service-group.ico"
			},
			'service-sub-group': {
				'valid_children': ['service-sub-group', 'service-element', 'dependency'],
				'icon': iconURL + "&amp;icon=service-sub-group.ico"
			},
			'service-element': {
				'valid_children': ['dependency'],
				'icon': iconURL + "&amp;icon=service-element.ico"
			},
			'dependency': {
				'max_depth': 0,
				'icon': iconURL + "&amp;icon=dependency.ico"
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
	}).on('copy_node.jstree', copy).on('select_node.jstree', function(e, data) {
		showNode(data.node);
	});
	
	$('#root-panel').show();

	$('#dependency-list').accordion();
	
	$('#edit-form').dialog({
		'minWidth': 800,
		'modal': true,
		'autoOpen': false,
		'buttons': [
		            { 'text': 'Cancel', 'click': function() { $(this).dialog( "close" ); } },
		            { 'text': 'Ok', 'click': applyEdit }
		            ]
	});

	$('#dependency-form').dialog({
		'minWidth': 800,
		'modal': true,
		'autoOpen': false,
		'buttons': [
		            { 'text': 'Cancel', 'click': function() { $(this).dialog( "close" ); } },
		            { 'text': 'Ok', 'click': applyDependency }
		            ]
	});
	
	$('#description-editor').jqte({
	});
	
	$('#dependency-reason-editor').jqte({
	});
	
	function contextMenu(node, callback) {
		var path = $.jstree.reference('#jstree').get_path(node, false, true);
		
		if (path.length > 1 && path[0] == 'trash') {
			return {};
		} else if (node.type == 'root') {
			return {
				'add-tower': {'label': 'New Tower', 'action': addTower }
			};
		} else if (node.type == 'tower') {
			return {
				'add-service-group': {'label': 'New Service Group', 'action': addServiceGroup },
				'add-dependency': {'label': 'New Dependency', 'action': addDependency },
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editItem }
			};
		} else if (node.type == 'service-group') {
			return {
				'add-service-sub-group': {'label': 'New Service Sub-group', 'action': addServiceSubGroup },
				'add-dependency': {'label': 'New Dependency', 'action': addDependency },
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editItem }
			};
		} else if (node.type == 'service-sub-group') {
			return {
				'add-service-sub-group': {'label': 'New Service Sub-group', 'action': addServiceSubGroup },
				'add-service-element': {'label': 'New Service Element', 'action': addServiceElement },
				'add-dependency': {'label': 'New Dependency', 'action': addDependency },
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editItem }
			};
		} else if (node.type == 'service-element') {
			return {
				'add-dependency': {'label': 'New Dependency', 'action': addDependency },
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editItem }
			};
		} else if (node.type == 'dependency') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editItem }
			};
		} else if (node.type == 'trash') {
			return {
				'empty': {'label': 'Empty Recycle Bin', 'action': emptyTrash }
			};
		}
	}
	
	function showNode(node){
		var type = node.data.type;
		var targetNode = node;
		var fragment = null;
		
		if (type == 'dependency') {
			var targetNodeId = $.jstree.reference('#jstree').get_parent(node);
			targetNode = $.jstree.reference('#jstree').get_node(targetNodeId);
			type = targetNode.data.type;
			fragment = node.data.id;
		}
		
		$('div.info-panel').hide();
		if (type == 'root') {
			$('#root-panel').show();
		} else if (type == 'trash') {
			$('#root-panel').show();
		} else {
			$('#service-title').text(targetNode.text);
			$('#service-description').html(unescapeHTML(targetNode.data.description));
			showCrumbTrail(buildCrumbTrail(targetNode, []));
			showDependencies(targetNode, fragment);
			$('#service-panel').show();
		}
	}
	
	function showDependencies(node, fragment) {
		$('#dependency-list').accordion('destroy');
		$('#dependency-list').empty();
		
		var empty = true;
		for (var i = 0; i < node.children.length; i++) {
			var child = $.jstree.reference('#jstree').get_node(node.children[i]);
			if (child.type == 'dependency') {
				empty = false;
				$('#dependency-list').append('<h3>' + child.text + '</h3>');
				$('#dependency-list').append('<div>' + unescapeHTML(child.data.description) + '</div>');
			}
		}
		
		if (empty) {
			$('#dependencies').hide();
		} else {
			$('#dependencies').show();
		}
		
		$('#dependency-list').accordion({
	    	collapsible: true,
	    	expandable: true,
	    	heightStyle: "content",
	    	active: false
	    });
	}
	
	function showCrumbTrail(trail) {
		$('#crumb-trail').empty();
		
		for (var i = 0; i < trail.length; i++) {
			var node = trail[i];
			$('#crumb-trail').append('<div><img src="' + iconURL + '&amp;icon=' + node.type + '.ico" style="margin-left: ' + i * 20 + 'px; margin-right: auto; vertical-align: middle;"><span class="hierarchy-element" style="vertical-align: middle;">&nbsp;&nbsp;' + node.text + '</span></div>');
		}
	}
	
	function buildCrumbTrail(node, trail) {
		var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
		var parentNode = $.jstree.reference('#jstree').get_node(parentNodeId);

		if (parentNode.type == 'root' || parentNode.type == 'trash') {
			return trail;
		} else {
			trail.unshift(parentNode);
			
			return buildCrumbTrail(parentNode, trail);
		}
	}

	function emptyTrash(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		var i = 0;
		while (i < node.children.length) {
			var deleteNode = $.jstree.reference('#jstree').get_node(node.children[i]);
			$.jstree.reference('#jstree').delete_node(deleteNode);
			i++;
		}
	}

	function addTower(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		addChildItem(node, 'tower', 'New tower');
	}
	
	function addServiceGroup(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		addChildItem(node, 'service-group', 'New service group');
	}
	
	function addServiceSubGroup(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		addChildItem(node, 'service-sub-group', 'New service-sub-group');
	}
	
	function addServiceElement(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		addChildItem(node, 'service-element', 'New service element');
	}
	
	function addDependency(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		var data = $.jstree.reference('#jstree').get_json(null, {
			'no_state' : true,
			'no_id' : false,
			'no_data' : false,
			'no_children' : false,
			'flat' : false
		});

		var new_json = sanitiseData(data[0]);

		var dependencyTree = $.jstree.reference('#dependency-tree');
		if (typeof dependencyTree !== 'undefined') {
			dependencyTree.destroy();
		}
		
		$('#dependency-tree').jstree({
			'core': {
				'data': new_json,
				'multiple': false,
				'check_callback': true,
				'themes': {
					'variant': 'small',
					'responsive': false
				}
			},
			'types': {
				'root': {
					'valid_children': ['tower']
				},
				'tower': {
					'valid_children': ['service-group', 'dependency'],
					'icon': "icons/tower.ico"
				},
				'service-group': {
					'valid_children': ['service-sub-group', 'dependency'],
					'icon': "icons/service-group.ico"
				},
				'service-sub-group': {
					'valid_children': ['service-sub-group', 'service-element', 'dependency'],
					'icon': "icons/service-sub-group.ico"
				},
				'service-element': {
					'valid_children': ['dependency'],
					'icon': "icons/service-element.ico"
				}
			},
			'plugins': ['types']
		});
		
		$('#dependency-id').val(node.id);
		$('#dependency-reason-editor').jqteVal('');
		$('#dependency-form').dialog('option', 'title', 'Inbound dependency for: ' + node.text);
		$('#dependency-form').dialog("open");
	}
	
	function applyDependency() {
		var node = $.jstree.reference('#jstree').get_node($('#dependency-id').val());
		var selected = $.jstree.reference('#dependency-tree').get_selected(true);

		if (selected != null && selected.length == 1) {
			var dependency = addChildItem(node, 'dependency', selected[0].text);
			var dependencyNode = $.jstree.reference('#jstree').get_node(dependency);
			var html = $('#dependency-form div.jqte_editor').html();
			dependencyNode.data.description = escapeHTML(html);
			dependencyNode.data.target = selected[0].data.id;
			
			showNode(node);
			
			$(this).dialog( "close" );
		} else {
			alert("Please select an inbound dependency");
		}
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

	function editItem(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		$('#element-id').val(node.id);
		$('#name-editor').val(node.text);
		if (node.type == 'dependency') {
			$('#name-editor').attr('disabled', true);
			$('#edit-form').dialog('option', 'title', 'Inbound dependency');
		} else {
			$('#name-editor').attr('disabled', false);
			$('#edit-form').dialog('option', 'title', node.text);
		}
		$('#description-editor').jqteVal(unescapeHTML(node.data.description));
		$('#edit-form').dialog("open");
	}

	function applyEdit() {
		var node = $.jstree.reference('#jstree').get_node($('#element-id').val());
		
		$.jstree.reference('#jstree').rename_node(node, $('#name-editor').val());
		var htmlNode = $('#description-editor');
		var html = $('#edit-form div.jqte_editor').html();
		node.data.description = escapeHTML(html);

		if (node.type != 'dependency') {
			var rootNode = $.jstree.reference('#jstree').get_node('#');
			var auroraId = rootNode.children[0];
			updateDependencies($.jstree.reference('#jstree').get_node(auroraId), node, true);
		}

		showNode(node);
		
		$(this).dialog( "close" );
	}

	function deleteItem(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		if (node.type != 'dependency') {
			var rootNode = $.jstree.reference('#jstree').get_node('#');
			var auroraId = rootNode.children[0];
			updateDependencies($.jstree.reference('#jstree').get_node(auroraId), node, false);
		}

		var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
		showNode($.jstree.reference('#jstree').get_node(parentNodeId));
		
		$.jstree.reference('#jstree').move_node(node, $.jstree.reference('#jstree').get_node('trash'));
	}

	function copy(eventObject, data) {
		data.node.data = data.original.data;
		data.node.data.id = uuid.v4();
	}
	
	function updateDependencies(node, changeNode, change) {
		if (typeof node.children != 'undefined') {
			for (var i = 0; i < node.children.length; i++) {
				var childNode = $.jstree.reference('#jstree').get_node(node.children[i]);
				if (childNode.type == 'dependency') {
					if (childNode.data.target == changeNode.data.id) {
						if (change) {
							$.jstree.reference('#jstree').rename_node(childNode, changeNode.text);
						} else {
							$.jstree.reference('#jstree').move_node(childNode, $.jstree.reference('#jstree').get_node('trash'));
						}
					}
				} else {
					updateDependencies(childNode, changeNode, change);
				}
			}
		}
	}

	function save() {
		var data = $.jstree.reference('#jstree').get_json(null, {
			'no_state' : false,
			'no_id' : false,
			'no_data' : false,
			'no_children' : false,
			'flat' : false
		});
		
		var jsonString = "var json = " + JSON.stringify(data);
	
		$.ajax({
		    url: "aurora.php",
	        type: "POST",
		    contentType: "application/javascript",
		    data: jsonString,
		    success: function() {
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
            	alert("Unable to retrieve services data\n" + status + ": " + reason);
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