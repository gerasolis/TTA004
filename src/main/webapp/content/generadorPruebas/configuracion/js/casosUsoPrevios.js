$(document).ready(function() {
	$('#gestion').DataTable({
		"ordering": false
		});
	contextPath = $("#rutaContexto").val();
});

function abrirConfiguracion(idCu) {
	$('#configuracionDialog').dialog('open');
	return false;
}

function cancelarConfirmarConfiguracion() {
	$('#configuracionDialog').dialog('close');
}