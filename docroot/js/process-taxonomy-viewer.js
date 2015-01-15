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
			'valid_children': ['activity', 'trash']
		},
		'activity': {
			'valid_children': ['domain']
		},
		'domain': {
			'valid_children': ['process'],
			'icon': iconURL + "&icon=process-domain.ico"
		},
		'process': {
			'valid_children': [],
			'icon': iconURL + "&icon=process.ico"
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

$('#tree-panel').show();

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

$('#edit-process-form').dialog({
    autoOpen: false,
    height: 600,
    width: 980,
    modal: true,
    buttons: {
    	"Save": applyProcessEdit,
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
	
$('#process-description-editor').jqte({
});

function contextMenu(node, callback) {
	var path = $.jstree.reference('#jstree').get_path(node, false, true);
	
	if (path.length > 1 && path[0] == 'trash') {
		return {};
	} else if (node.type == 'activity') {
		return {
			'add-domain': {'label': 'New Process Domain', 'action': addDomain }
		};
	} else if (node.type == 'domain') {
		return {
			'add-process': {'label': 'New Process', 'action': addProcess },
			'delete': {'label': 'Delete', 'action': deleteItem },
			'edit': {'label': 'Edit', 'action': editDomain }
		};
	} else if (node.type == 'process') {
		return {
			'delete': {'label': 'Delete', 'action': deleteItem },
			'edit': {'label': 'Edit', 'action': editProcess }
		};
	} else if (node.type == 'trash') {
		return {
			'empty': {'label': 'Empty Recycle Bin', 'action': emptyTrash }
		};
	}
}

function showNode(node){
	var type = node.type;
	
	if (type == 'activity' || type == 'trash') {
		$('#tree-panel').show();
		$('#domain-panel').hide();
		$('#process-panel').hide();
	} else if (type == 'domain') {
		$('#tree-panel').hide();
		$('#process-panel').hide();
		
		$('#domain-title').text(node.text);
		$('#domain-description').html(unescapeHTML(node.data.description));
		$('#domain-panel').show();
	} else if (type == 'process') {
		$('#tree-panel').hide();
		$('#domain-panel').hide();
		
		var domainNodeId = $.jstree.reference('#jstree').get_parent(node);
		var domainNode = $.jstree.reference('#jstree').get_node(domainNodeId);
		
		$('#process-domain-title').text(domainNode.text);
		$('#process-title').text(node.text);
		$('#process-description').html(unescapeHTML(node.data.description));
		$('#process-panel').show();
	}
}

function addDomain(menu) {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_node(menu.reference[0]);
	var newNode = tree.get_node(addChildItem(node, 'domain', 'New process domain'));

    $.ajax({
        url: unescapeHTML(actionURL) + "&action=createDomainAction",
        type: "POST",
        data: {
			domainId: newNode.data.id,
			valueChain: node.data.id,
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

function addProcess(menu) {
	var tree = $.jstree.reference('#jstree');
	var node = tree.get_node(menu.reference[0]);
	var newNode = tree.get_node(addChildItem(node, 'process', 'New process'));

    $.ajax({
        url: unescapeHTML(actionURL) + "&action=createProcessAction",
        type: "POST",
        data: {
			domainId: node.data.id,
			processId: newNode.data.id,
			processName: newNode.text,
			processDescription: newNode.data.description
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
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	
	$('#domain-id').val(node.id);
	$('#domain-name-editor').val(node.text);
	$('#edit-domain-form').dialog('option', 'title', node.text);
	$('#domain-description-editor').jqteVal(unescapeHTML(node.data.description));
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
			domainDescription: node.data.description
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

function editProcess(menu) {
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	
	$('#process-id').val(node.id);
	$('#process-name-editor').val(node.text);
	$('#process-name-editor').attr('disabled', false);
	$('#edit-process-form').dialog('option', 'title', node.text);
	$('#process-description-editor').jqteVal(unescapeHTML(node.data.description));
	$('#edit-process-form').dialog("open");
}

function applyProcessEdit() {
	var node = $.jstree.reference('#jstree').get_node($('#process-id').val());
	
	$.jstree.reference('#jstree').rename_node(node, $('#process-name-editor').val());
	var html = $('#edit-process-form div.jqte_editor').html();
	node.data.description = escapeHTML(html);

	showNode(node);
	
    $.ajax({
        url: unescapeHTML(actionURL) + "&action=updateProcessAction",
        type: "POST",
        data: {
			processId: node.data.id,
			processName: $('#process-name-editor').val(),
			processDescription: node.data.description
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
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	
	var parentNodeId = $.jstree.reference('#jstree').get_parent(node);
	showNode($.jstree.reference('#jstree').get_node(parentNodeId));
	
	$.jstree.reference('#jstree').move_node(node, $.jstree.reference('#jstree').get_node('trash'));
}

function emptyTrash(menu) {
	var node = $.jstree.reference('#jstree').get_node(menu.reference[0]);
	
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
	var beforeNode = $.jstree.reference('#jstree').get_node(parentNode.children[data.position + 1]);
	var parentId = data.parent;
	if (parentNode.type == 'activity' || parentNode.type == 'domain') {
		parentId = parentNode.data.id;
	}
	
	var postURL = actionURL;
	
	var postData = {
		parentId: parentId,
		position: data.position
	};
	
	if (beforeNode != false) {
		postData.before = beforeNode.data.id;
	}
	
	if (node.type == 'domain') {
		postURL += "&action=moveDomainAction";
		postData.domainId = node.data.id;
	} else {
		postURL += "&action=moveProcessAction";
		postData.processId = node.data.id;
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