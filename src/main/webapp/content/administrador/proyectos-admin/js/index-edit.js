$(document).ready(function() {
	$('#fechaInicio').datepicker({
		  dateFormat: "dd/mm/yy"
	});
	$('#fechaTermino').datepicker({
		  dateFormat: "dd/mm/yy"
	});
	$('#fechaInicioProgramada').datepicker({
		  dateFormat: "dd/mm/yy"
	});
	$('#fechaTerminoProgramada').datepicker({
		  dateFormat: "dd/mm/yy"
	});
	contextPath = $("#rutaContexto").val();

} );