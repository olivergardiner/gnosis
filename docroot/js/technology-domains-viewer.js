(function($, uuid) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    versions += "\njstree: " + $.jstree.version;
    //versions += "\njqte: " + $.jqte.version; // Can't check version of jQTE
    //alert(versions);
    
    var valueChain;

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
				return (node.data.type == 'root') ? false : true;
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
				'icon': iconURL + "&amp;icon=technology-domain.ico"
			},
			'capability': {
				'valid_children': [],
				'icon': iconURL + "&amp;icon=capability.ico"
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
	}).on('select_node.jstree', function(e, data) {
		showNode(data.node);
	}).on('move_node.jstree', moveNode).on('copy_node.jstree', copyNode);
	
	$('#tree-panel').show();

	$.ajax({
        async : true,
        type : "GET",
        url : valueChainJsonDataURL,
        dataType : "json",
        success : function(response) {
        	valueChain = response;
        	for (var i = 0; i < response[0].children.length; i++) {
        		$("#domain-value-chain-editor").append('<option value="' + response[0].children[i].data.id + '">' + response[0].children[i].text + '</option>')
        	}

        	for (var i = 0; i < response[1].children.length; i++) {
        		$("#domain-value-chain-editor").append('<option value="' + response[1].children[i].data.id + '">' + response[1].children[i].text + '</option>')
        	}

        	$('#domain-value-chain-editor').selectric({
        		expandToItemText: true,
        		maxHeight: 120
        	});
        },
        error : function(jqXhr, status, reason) {
        	alert("Unable to retrieve value chain data\n" + status + ": " + reason);
        }
	});

	
	$('#edit-domain-form').dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        modal: true,
        buttons: {
        	"Save": applyDomainEdit,
	    	"Cancel": function() {
				$(this).dialog( "close" );
	        }
        },
        open: function() {
        },
        close: function() {
        }
	});

	$('#edit-capability-form').dialog({
        autoOpen: false,
        height: 600,
        width: 980,
        modal: true,
        buttons: {
        	"Save": applyCapabilityEdit,
	    	"Cancel": function() {
				$(this).dialog( "close" );
	        }
        },
        open: function() {
        },
        close: function() {
        }
	});

	$('#domain-description-editor').jqte({
	});
		
	$('#capability-description-editor').jqte({
	});
	
	function contextMenu(node, callback) {
		var path = $.jstree.reference('#jstree').get_path(node, false, true);
		
		if (path.length > 1 && path[0] == 'trash') {
			return {};
		} else if (node.type == 'root') {
			return {
				'add-domain': {'label': 'New Technology Domain', 'action': addDomain }
			};
		} else if (node.type == 'technology-domain') {
			return {
				'add-capability': {'label': 'New Capability', 'action': addCapability },
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editDomain }
			};
		} else if (node.type == 'capability') {
			return {
				'delete': {'label': 'Delete', 'action': deleteItem },
				'edit': {'label': 'Edit', 'action': editCapability }
			};
		} else if (node.type == 'trash') {
			return {
				'empty': {'label': 'Empty Recycle Bin', 'action': emptyTrash }
			};
		}
	}
	
	function showNode(node){
		var type = node.type;
		
		if (type == 'root' || type == 'trash') {
			$('#tree-panel').show();
			$('#domain-panel').hide();
			$('#capability-panel').hide();
		} else if (type == 'technology-domain') {
			$('#tree-panel').hide();
			$('#capability-panel').hide();
			
			$('#domain-title').text(node.text);
			$('#domain-description').html(unescapeHTML(node.data.description));
			$('#domain-value-chain').text(getValueChainName(node.data.valueChain));
			$('#domain-panel').show();
		} else if (type == 'capability') {
			$('#tree-panel').hide();
			$('#domain-panel').hide();
			
			var domainNodeId = $.jstree.reference('#jstree').get_parent(node);
			var domainNode = $.jstree.reference('#jstree').get_node(domainNodeId);
			
			$('#capability-domain-title').text(domainNode.text);
			$('#capability-title').text(node.text);
			$('#capability-description').html(unescapeHTML(node.data.description));
			//showCrumbTrail(buildCrumbTrail(node, []));
			$('#capability-panel').show();
		}
	}
	
	function getValueChainName(id) {
		for (var i=0; i < valueChain[0].children.length; i++) {
			if (valueChain[0].children[i].data.id == id) {
				return valueChain[0].children[i].text;
			}
		}

		for (var i=0; i < valueChain[1].children.length; i++) {
			if (valueChain[0].children[i].data.id == id) {
				return valueChain[0].children[i].text;
			}
		}
		
		return '';
	}
	
	function showCrumbTrail(trail) {
		$('#gnosis2-crumb-trail').empty();
				
		for (var i = 0; i < trail.length; i++) {
			var node = trail[i];
			$('#gnosis2-crumb-trail').append('<div><img src="' + iconURL + "&amp;icon=" + node.type + '.ico" style="margin-left: ' + i * 20 + 'px; margin-right: auto; vertical-align: middle;"><span class="hierarchy-element" style="vertical-align: middle;">&nbsp;&nbsp;' + node.text + '</span></div>');
		}
	}
	
	function buildCrumbTrail(node, trail) {
		var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
		var parentNode = $.jstree.reference('#jstree').get_node(parentNodeId);

		if (parentNode.type == 'root' || parentNode.type == 'trash'|| parentNode.type == '#') {
			return trail;
		} else {
			trail.unshift(parentNode);
			
			return buildCrumbTrail(parentNode, trail);
		}
	}

	function addDomain(menu) {
		var tree = $.jstree.reference('#jstree');
		var node = tree.get_node(menu.reference.context);
		var newNode = tree.get_node(addChildItem(node, 'technology-domain', 'New technology domain'));

        $.ajax({
            url: unescapeHTML(actionURL) + "&action=createDomainAction",
            type: "POST",
            data: {
				domainId: newNode.data.id,
				domainName: newNode.text,
				domainDescription: newNode.data.description
        	},
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
	}
	
	function addCapability(menu) {
		var tree = $.jstree.reference('#jstree');
		var node = tree.get_node(menu.reference.context);
		var newNode = tree.get_node(addChildItem(node, 'capability', 'New capability'));

        $.ajax({
            url: unescapeHTML(actionURL) + "&action=createCapabilityAction",
            type: "POST",
            data: {
				domainId: node.data.id,
				capabilityId: newNode.data.id,
				capabilityName: newNode.text,
				capabilityDescription: newNode.data.description
        	},
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
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

	function editDomain(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		$('#domain-id').val(node.id);
		$('#domain-name-editor').val(node.text);
		$('#domain-name-editor').attr('disabled', false);
		$('#edit-domain-form').dialog('option', 'title', node.text);
		$('#domain-description-editor').jqteVal(unescapeHTML(node.data.description));
		if (node.data.isLOB) {
			$('#domain-value-chain-editor').val(node.data.valueChain).selectric("refresh");
			$("#value-chain").css("display", "table-row");
		} else {
			$('#domain-value-chain-editor').val('');
			$("#value-chain").css("display", "none");
		}
		$('#edit-domain-form').dialog("open");
	}

	function applyDomainEdit() {
		var node = $.jstree.reference('#jstree').get_node($('#domain-id').val());
		
		$.jstree.reference('#jstree').rename_node(node, $('#domain-name-editor').val());
		var html = $('#edit-domain-form div.jqte_editor').html();
		node.data.description = escapeHTML(html);
		node.data.valueChain = $('#domain-value-chain-editor').val();
		
		showNode(node);
		
        $.ajax({
            url: unescapeHTML(actionURL) + "&action=updateDomainAction",
            type: "POST",
            data: {
				domainId: node.data.id,
				domainName: $('#domain-name-editor').val(),
				domainDescription: node.data.description,
				valueChain: node.data.valueChain
        	},
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
		
		$(this).dialog( "close" );
	}

	function editCapability(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		$('#capability-id').val(node.id);
		$('#capability-name-editor').val(node.text);
		$('#capability-name-editor').attr('disabled', false);
		$('#edit-capability-form').dialog('option', 'title', node.text);
		$('#capability-description-editor').jqteVal(unescapeHTML(node.data.description));
		$('#edit-capability-form').dialog("open");
	}

	function applyCapabilityEdit() {
		var node = $.jstree.reference('#jstree').get_node($('#capability-id').val());
		
		$.jstree.reference('#jstree').rename_node(node, $('#capability-name-editor').val());
		var html = $('#edit-capability-form div.jqte_editor').html();
		node.data.description = escapeHTML(html);

		showNode(node);
		
        $.ajax({
            url: unescapeHTML(actionURL) + "&action=updateCapabilityAction",
            type: "POST",
            data: {
				capabilityId: node.data.id,
				capabilityName: $('#capability-name-editor').val(),
				capabilityDescription: node.data.description
        	},
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
		
		$(this).dialog( "close" );
	}

	function deleteItem(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
		showNode($.jstree.reference('#jstree').get_node(parentNodeId));
		
		$.jstree.reference('#jstree').move_node(node, $.jstree.reference('#jstree').get_node('trash'));
	}

	function emptyTrash(menu) {
		var node = $.jstree.reference('#jstree').get_node(menu.reference.context);
		
		var i = 0;
		while (i < node.children.length) {
			var deleteNode = $.jstree.reference('#jstree').get_node(node.children[i]);
			$.jstree.reference('#jstree').delete_node(deleteNode);
			i++;
		}

        $.ajax({
            url: unescapeHTML(actionURL + "&action=emptyTrashAction"),
            type: "POST",
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
	}

	function moveNode(eventObject, data) {
		var node = data.node;
		var parentNode = $.jstree.reference('#jstree').get_node(data.parent);
		var parentId = data.parent;
		if (parentNode.type == 'technology-domain') {
			parentId = parentNode.data.id;
		}
		var postURL;
		var postData;
		
		if (node.type == 'technology-domain') {
			postURL = actionURL + "&action=moveDomainAction";
			postData = {
				parentId: parentId,
				position: data.position,
				domainId: node.data.id
			};
		} else {
			postURL = actionURL + "&action=moveCapabilityAction";
			postData = {
				parentId: parentId,
				position: data.position,
				capabilityId: node.data.id
			};
		}
		
        $.ajax({
            url: unescapeHTML(postURL),
            type: "POST",
            data: postData,
            success: function (data) {
                //$("#form_output").html(data);
            },
            error: function (jXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
	}
	
	function copyNode(eventObject, data) {
		data.node.data = data.original.data;
		data.node.data.id = uuid.v4();

		// Not currently permitted - need to decide whether to implement this asit would require the ids to be set for the whole of the copied tree
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