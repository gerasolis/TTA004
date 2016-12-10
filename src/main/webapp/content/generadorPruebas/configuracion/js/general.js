$(document).ready(function() {
	$('#gestion').DataTable();
	contextPath = $("#rutaContexto").val();

} );

function siguiente() {
	$('form').get(0).setAttribute("action", contextPath + "/configuracion-general!configurar");
	$('form').get(0).submit();
}

function guardarSalir() {
	$('form').get(0).setAttribute("action", contextPath + "/configuracion-general!guardar");
	$('form').get(0).submit();
}

function probarConexion() {
	rutaProbarConexion = contextPath + '/configuracion-general!probarConexion';
	var url = $('#url').val();
	var driver = $('#driver').val();
	var usuario = $('#usuario').val();
	var contrasenia = $("#contrasenia").val();
		
	$.ajax({
		dataType : 'json',
		url : rutaProbarConexion,
		type: "POST",
		data : {
			url : url,
			driver : driver,
			usuario : usuario,
			contrasenia : contrasenia
		},
		success : function(data) {
			mostrarResultadoConexion(data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

function mostrarResultadoConexion(data) {
	alert(data);
}