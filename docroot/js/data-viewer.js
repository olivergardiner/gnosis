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
	'types': {
		'#': {
		},
		'package': {
			//'icon': "/Gnosis2/icons/technology-domain.ico"
		},
		'Entity': {
			//'icon': "/Gnosis2/icons/technology-domain.ico"
		},
		'ReferenceEntity': {
			//'icon': "/Gnosis2/icons/capability.ico"
		},
		'ComplexEntity': {
			//'icon': "/Gnosis2/icons/trash.ico"
		}
	},
	'plugins': ['dnd', 'types']
}).on('select_node.jstree', function(e, data) {
	showNode(data.node);
});

changeNodeCallback = function(resourcePath) {
	$.jstree.reference('#jstree').deselect_all();
	$("#detail-panel").load(modelDetailURL + "&path=" + resourcePath);
}

function showNode(node){
	var resourcePath = node.data.path;
	if (node.type == "package") {
		resourcePath += "/" + node.text + "/package-frame.html";
	} else {
		resourcePath += "/" + node.text + ".html"
	}
	
	$("#detail-panel").load(modelDetailURL + "&path=" + resourcePath);
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
        	alert("Unable to retrieve data\n" + status + ": " + reason);
        }
	});
}