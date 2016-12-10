$(document).ready(function() {
	$('#gestion').DataTable().column(0).visible(false);
	contextPath = $("#rutaContexto").val();
} );

function confirmarEliminacion(urlEliminar) {
	$('#confirmarEliminacionDialog').dialog('close');
	window.location.href = urlEliminar;
}

function cancelarConfirmarEliminacion() {
	$('#confirmarEliminacionDialog').dialog('close');
}

function verificarEliminacionElemento(idElemento) {
	rutaVerificarReferencias = contextPath + '/pantallas!verificarElementosReferencias';
	$.ajax({
		dataType : 'json',
		url : rutaVerificarReferencias,
		type: "POST",
		data : {
			idSel : idElemento
		},
		success : function(data) {
			mostrarMensajeEliminacion(data, idElemento);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
	
	return false;
	
}

function mostrarMensajeEliminacion(json, id) {
	var elementos = document.createElement("ul");
	var elementosReferencias = document.getElementById("elementosReferencias");
	var urlEliminar = contextPath + "/pantallas/" +id+ "?_method=delete";
	while (elementosReferencias.firstChild) {
		elementosReferencias.removeChild(elementosReferencias.firstChild);
	}
	if (json != "") {
		$
				.each(
						json,
						function(i, item) {
							var elemento = document.createElement("li");
							elemento.appendChild(document.createTextNode(item));
							elementos.appendChild(elemento);
						});
		document.getElementById("elementosReferencias").appendChild(elementos);
		
		$('#mensajeReferenciasDialog').dialog('open');
	} else {	
		document.getElementById("btnConfirmarEliminacion").onclick = function(){ confirmarEliminacion(urlEliminar);};
		$('#confirmarEliminacionDialog').dialog('open');
	}
}
function cerrarMensajeReferencias() {
	$('#mensajeReferenciasDialog').dialog('close');
}