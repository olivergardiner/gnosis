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