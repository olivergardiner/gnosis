(function($) {
	var versions = "Script loaded with jquery: " + $.fn.jquery;
    versions += "\njquery-ui: " + $.ui.version;
    versions += "\njstree: " + $.jstree.version;
    //versions += "\njqte: " + $.jqte.version; // Can't check version of jQTE
    //alert(versions);

    $("#dm-upload-form").uploadFile({
    	url: actionURL + "&amp;action=uploadModelAction",
    	fileName:"file",
    	multiple:true,
    	showStatusAfterSuccess:false,
    	showAbort:false,
    	showDone:false,
    	uploadButtonClass:"ajax-file-upload-green"
    });

    $("#dm-upload").show();
})($);