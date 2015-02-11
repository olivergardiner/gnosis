$("#apps-upload-form").uploadFile({
	url: actionURL + "&amp;action=uploadApplicationsAction",
	fileName:"file",
	multiple:true,
	showStatusAfterSuccess:false,
	showAbort:false,
	showDone:false,
	uploadButtonClass:"ajax-file-upload-green"
});

$("#apps-upload").show();