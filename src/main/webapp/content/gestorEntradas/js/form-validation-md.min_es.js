$(document).ready(function() { 


	var e=function(){
		var e=$("#frmActor"),
		r=$(".alert-danger",e),
		i=$(".alert-success",e);
		e.validate({
			errorElement:"span",
			errorClass:"help-block help-block-error",
			focusInvalid:!1,
			ignore:"",
			messages:{
				
				"checkboxes1[]":{
					required:"Please check some options",
					minlength:jQuery.validator.format("At least {0} items must be selected")
				}
			},
			rules:{
				"nombreHTML-":{
					minlength:2,
					required:!0,
					
				},
				
			},
			
		})
	}
});