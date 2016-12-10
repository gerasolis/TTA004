$(document).ready(function() {
	var cardinalidad = document.getElementById("cardinalidad");
	var cardinalidadTexto = cardinalidad.options[cardinalidad.selectedIndex].text;
	
	if (cardinalidadTexto == 'Otro' || cardinalidadTexto == 'Otra') {
		document.getElementById("otro").style.display = '';
	} else {
		document.getElementById("otro").style.display = 'none';
	}
});

function verificarOtro() {
	var cardinalidad = document.getElementById("cardinalidad");
	var cardinalidadTexto = cardinalidad.options[cardinalidad.selectedIndex].text;

	if (cardinalidadTexto == 'Otro' || cardinalidadTexto == 'Otra') {
		document.getElementById("otro").style.display = '';
	} else {
		document.getElementById("otro").style.display = 'none';
	}
}

function prepararEnvio() {
	$('#mensajeConfirmacion').dialog('open');
}

function enviarComentarios(){
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if(vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmActor").submit();

}

function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeConfirmacion').dialog('close');
}