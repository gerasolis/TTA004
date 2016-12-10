$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	
	if(document.getElementById("pruebaGenerada").value == "true") {
		window.location.href = contextPath + "/configuracion-caso-uso!descargarPrueba";
		document.getElementById("pruebaGenerada").value = "false";
	}
	$('#gestion').DataTable().column(0).visible(false); 
	
});

function confirmarEliminacion(urlEliminar) {
	$('#confirmarEliminacionDialog').dialog('close');
	window.location.href = urlEliminar;
}

function cancelarConfirmarEliminacion() {
	$('#confirmarEliminacionDialog').dialog('close');
}

function cerrarMensajeReferencias() {
	$('#mensajeReferenciasDialog').dialog('close');
}

function confirmarTermino(urlTerminar) {
	$('#mensajeTerminarDialog').dialog('close');
	window.location.href = urlTerminar;
}

function cancelarConfirmarTermino() {
	$('#mensajeTerminarDialog').dialog('close');
}

function verificarEliminacionElemento(idElemento) {
	rutaVerificarReferencias = contextPath
			+ '/cu!verificarElementosReferencias';
	$.ajax({
		dataType : 'json',
		url : rutaVerificarReferencias,
		type : "POST",
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
	var urlEliminar = contextPath + "/cu/" + id + "?_method=delete";
	while (elementosReferencias.firstChild) {
		elementosReferencias.removeChild(elementosReferencias.firstChild);
	}
	if (json != "") {
		$.each(json, function(i, item) {
			var elemento = document.createElement("li");
			elemento.appendChild(document.createTextNode(item));
			elementos.appendChild(elemento);
		});
		document.getElementById("elementosReferencias").appendChild(elementos);

		$('#mensajeReferenciasDialog').dialog('open');
	} else {
		document.getElementById("btnConfirmarEliminacion").onclick = function() {
			confirmarEliminacion(urlEliminar);
		};
		$('#confirmarEliminacionDialog').dialog('open');
	}
}

function verificarTerminarCasoUso(idElemento) {
	mostrarMensajeCargando();
	rutaTerminar = contextPath + '/cu!verificarTermino';
	$.ajax({
		dataType : 'json',
		url : rutaTerminar,
		type : "POST",
		data : {
			idSel : idElemento
		},
		success : function(data) {
			ocultarMensajeCargando();
			mostrarMensajeRestriccion(data, idElemento);
		},
		error : function(err) {
			ocultarMensajeCargando();
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});

	return false;

}

function mostrarMensajeRestriccion(json, id) {
	var elementos = document.createElement("ul");
	var restriccionesTermino = document.getElementById("restriccionesTermino");
	var urlTerminar = contextPath + "/cu!terminar?idSel=" + id;
	while (restriccionesTermino.firstChild) {
		restriccionesTermino.removeChild(restriccionesTermino.firstChild);
	}
	if (json != "") {
		$.each(json, function(i, item) {
			var elemento = document.createElement("li");
			elemento.appendChild(document.createTextNode(item));
			elementos.appendChild(elemento);
		});
		document.getElementById("restriccionesTermino").innerHTML = "Algunos elementos utilizados en la descripción del caso de uso" +
				" y en la trayectoria no coinciden, ¿desea continuar?";
		document.getElementById("restriccionesTermino").appendChild(elementos);
	} else {
		document.getElementById("restriccionesTermino").innerHTML = "¿Está seguro de que quiere terminar el Caso de uso?";
	}
	document.getElementById("btnConfirmarTermino").onclick = function() {
		confirmarTermino(urlTerminar);
	};
	$('#mensajeTerminarDialog').dialog('open');
}

function mostrarMensajeCargando() {
	$("#modal").css("display", "block");
}

function ocultarMensajeCargando() {
	$("#modal").css("display", "");
}