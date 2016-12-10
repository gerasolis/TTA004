function prepararEnvio() {
	//$('#mensajeConfirmacion').dialog('open');
}

function enviarComentarios(){
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if(vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmTermino").submit();

}

function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeConfirmacion').dialog('close');
}

function agregarMensaje(mensaje) {
	alert(mensaje);
};