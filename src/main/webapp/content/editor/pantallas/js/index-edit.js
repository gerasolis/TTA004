var contextPath = "error";

$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	$('#tablaAccion').DataTable();
	ocultarColumnas("tablaAccion");
	cargarCatalogos();
	
	cargarImagenPantalla();
	
	var json = $("#jsonAccionesTabla").val();
	var jsonImg = $("#jsonImagenesAcciones").val(); 
	if (json !== "") {
		var parsedJson = JSON.parse(json);
		var parsedJsonImg = JSON.parse(jsonImg);
		$
				.each(
						parsedJson,
						function(i, item) {
							var accion = construirFila(parsedJsonImg[i], item.nombre, item.descripcion, item.tipoAccion.id, 
									item.pantallaDestino.id, item.id);
							dataTableCDT.addRow("tablaAccion", accion); 
						}); 
	}
});


function ocultarColumnas(tabla) {
	var dataTable = $("#" + tabla).dataTable();
	dataTable.api().column(2).visible(false);
	dataTable.api().column(3).visible(false);
	dataTable.api().column(4).visible(false);
	dataTable.api().column(5).visible(false);
	dataTable.api().column(6).visible(false);
}

function mostrarPrevisualizacion(inputFile, nombre) {
	document.getElementById("fila-" + nombre).style.display = 'none';
	var idImg = nombre.replace(/\s/g, "_");
	if (inputFile.files && inputFile.files[0]) {
        var reader = new FileReader();
        reader.readAsDataURL(inputFile.files[0])
        reader.onload = function (e) {
            $('#' + idImg).attr('src', reader.result); 
	    	
        }
        document.getElementById("marco-" + idImg).style.display = '';
    }
}

function verificarRegistroModificacion() {
	var indexFilaAccion = document.getElementById("filaAccion").value;
	if(indexFilaAccion == -1) {
		registrarAccion();
	} else {
		modificarAccion();
	}
}

function registrarAccion() {
	
	var nombre = document.getElementById("accion.nombre").value;
	var descripcion = document.getElementById("accion.descripcion").value;
	var imagen = document.getElementById("accion.imagen");
	var selectTipoAccion = document.getElementById("accion.tipoAccion");
	var selectPantallaDestino = document.getElementById("accion.pantallaDestino");
	var tipoAccion = selectTipoAccion.options[selectTipoAccion.selectedIndex].value;
	var idPantallaDestino = selectPantallaDestino.options[selectPantallaDestino.selectedIndex].value;

	if (esValidaAccion("tablaAccion", nombre, descripcion, imagen, selectTipoAccion, selectPantallaDestino)) {
		var row = construirFila("", nombre, descripcion, tipoAccion, idPantallaDestino, 0);
		dataTableCDT.addRow("tablaAccion", row);
		actualizarImagenAccion(imagen);
		limpiarCamposEmergente();
		$('#accionDialog').dialog('close');
	} else {
		return false;
	}
}

function modificarAccion() {
	
	var nombre = document.getElementById("accion.nombre").value;
	var descripcion = document.getElementById("accion.descripcion").value;
	var imagen = document.getElementById("accion.imagen");
	var selectTipoAccion = document.getElementById("accion.tipoAccion");
	var selectPantallaDestino = document.getElementById("accion.pantallaDestino");
	var tipoAccion = selectTipoAccion.options[selectTipoAccion.selectedIndex].value;
	var idPantallaDestino = selectPantallaDestino.options[selectPantallaDestino.selectedIndex].value;
	var srcAccion = document.getElementById("src-accion").value;

	if (esValidaAccion("tablaAccion", nombre, descripcion, imagen, selectTipoAccion, selectPantallaDestino)) {
		var indexFilaAccion = document.getElementById("filaAccion").value;
		
		var rowSelData = $("#tablaAccion").DataTable().row(indexFilaAccion).data();
		rowNewData = construirFila(srcAccion, nombre, descripcion, tipoAccion, idPantallaDestino, 0);
		rowSelData[0] = rowNewData[0];
		rowSelData[1] = rowNewData[1];
		rowSelData[2] = rowNewData[2];
		rowSelData[3] = rowNewData[3];
		rowSelData[4] = rowNewData[4];
		rowSelData[5] = rowNewData[5];
		dataTableCDT.editRow("tablaAccion", indexFilaAccion, rowSelData);
		actualizarImagenAccion(imagen, indexFilaAccion);
		limpiarCamposEmergente();
		$('#accionDialog').dialog('close');
	} else {
		return false;
	}
}

function actualizarImagenAccion(inputFile, indexFilaAccion) {
	if(indexFilaAccion == null) { 
		indexFilaAccion = "max";
	}
	if (inputFile.files && inputFile.files[0]) {
        var reader = new FileReader();
        reader.readAsDataURL(inputFile.files[0])
        reader.onload = function (e) {
        	if(reader.result != "") {
        		dataTableCDT.insertarValorCelda("tablaAccion", "max", 2, reader.result);
        		img = "<center><img src = '" + reader.result + "'/></center>";
        		dataTableCDT.insertarValorCelda("tablaAccion", indexFilaAccion, 0, img);
        	}     	
        }
    } 
}

function obtenerImagenTextoPantalla(inputFile) {
	if (inputFile.files && inputFile.files[0]) {
        var reader = new FileReader();
        reader.readAsDataURL(inputFile.files[0]);
        reader.onload = function (e) {
            document.getElementById("src-pantalla").value = reader.result;
        }
    }
}

function construirFila(srcAccion, nombre, descripcion, tipoAccion, idPantallaDestino, id) {
	var row = [
				"Sin imagen",
				nombre,
				"",
				descripcion, 
				tipoAccion,
				idPantallaDestino,
				id,
				"<center>"
						+ "<a onclick='solicitarModificacionAccion(this);' button='true'>"
						+ "<img class='icon'  id='icon' src='"
						+ window.contextPath
						+ "/resources/images/icons/editar.png' title='Modificar Acción'/></a>"
						+ "<a onclick='dataTableCDT.deleteRow(tablaAccion, this);' button='true'>"
						+ "<img class='icon'  id='icon' src='"
						+ window.contextPath
						+ "/resources/images/icons/eliminar.png' title='Eliminar Acción'/></a>"
						+ "</center>" ];
	
	if(srcAccion != "" && srcAccion != null) {
    	row[0] = "<center><img src = '" + srcAccion + "'/></center>";
		row[2] = srcAccion;
    }
	return row;
}

function limpiarCamposEmergente() {
	document.getElementById("accion.nombre").value = null;
	document.getElementById("accion.descripcion").value = null;
	document.getElementById("accion.imagen").value = null;
	document.getElementById("accion.tipoAccion").selectedIndex = 0;
	document.getElementById("accion.pantallaDestino").selectedIndex = 0;
	document.getElementById("marco-accion").style.display = 'none';
	document.getElementById("fila-accion").style.display = '';
	document.getElementById("accion").src = "";
	document.getElementById("src-accion").value = "";
}

function cancelarRegistrarAccion() {
	limpiarCamposEmergente();
	$('#accionDialog').dialog('close');
}

function agregarMensaje(mensaje) {
	alert(mensaje);
};

function esValidaAccion(idTabla, nombre, descripcion, imagen, tipoAccion, pantallaDestino) {
	/*
	 * Inicia la validación del nombre y descripción, los cuales se deben validar independientemente del tipo de dato seleccionado.
	 */

	if (vaciaONula(nombre) || vaciaONula(descripcion) || tipoAccion.selectedIndex == 0 || pantallaDestino.selectedIndex == 0) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}

	if (nombre.length > 200) {
		agregarMensaje("Ingrese menos de 45 caracteres.");
		return false;
	}

	if (nombre.indexOf("_") > -1) {
		agregarMensaje("El nombre no puede contener guiones bajos.");
		return false;
	}

	if (nombre.indexOf(":") > -1) {
		agregarMensaje("El nombre no puede contener dos puntos.");
		return false;
	}

	if (nombre.indexOf("·") > -1) {
		agregarMensaje("El nombre no puede punto medio.");
		return false;
	}

	if (descripcion.length > 999) {
		agregarMensaje("Ingrese menos de 999 caracteres.");
		return false;
	}

	/*
	 * Finaliza la validación del nombre y descripción
	 */

	var tamMaximo = 2000000;
	if(imagen.files.length == 1) {
		var img = imagen.files[0];
		if(img.size > tamMaximo) {
			agregarMensaje("Ingrese una imagen con tamaño máximo de " + tamMaximo + " bytes");
		}
	}

	return true; 
}

function prepararEnvio() {
	try {
		tablaToJson("tablaAccion");
//		$('#mensajeConfirmacion').dialog('open');
		return true;
	} catch (err) {
		alert("Ocurrió un error: " + err);
		return false;
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloAcciones = [];
	var arregloImagenesAcciones = [];
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		var nombre = table.fnGetData(i, 1);
		var imagenCadena = table.fnGetData(i, 2);
		var descripcion = table.fnGetData(i, 3);
		var idTipoAccion = table.fnGetData(i, 4);
		var idPantallaDestino = table.fnGetData(i, 5);
		var id = table.fnGetData(i, 6);
		
		var imagen = [];
		var pantallaDestino = new Pantalla(null, null, null, null, idPantallaDestino);
		var tipoAccion = new TipoAccion(idTipoAccion);
		arregloAcciones.push(new Accion(nombre, imagen, descripcion, tipoAccion,
				pantallaDestino, id));
		
		arregloImagenesAcciones[arregloImagenesAcciones.length] = imagenCadena;
	}
	var jsonAcciones = JSON.stringify(arregloAcciones);
	var jsonImagenesAcciones = JSON.stringify(arregloImagenesAcciones);
	document.getElementById("jsonAccionesTabla").value = jsonAcciones;
	document.getElementById("jsonImagenesAcciones").value = jsonImagenesAcciones;
}

function cargarCatalogos() {
	var selectPantallasDestino = document.getElementById("accion.pantallaDestino");
	var json = document.getElementById("jsonPantallasDestino").value;
	agregarListaSelect(selectPantallasDestino, json);
}

function agregarListaSelect(select, cadena) {
	var json = JSON.parse(cadena);
	if (json !== "") {		
		select.options.length = 0;
		var option = document.createElement("option");
		option.text = "Seleccione";
		option.index = -1;
		option.value = -1;
		select.add(option);
		$
				.each(
						json,
						function(i, item) {
							option = document.createElement("option");
							option.text = item.clave + item.numero + ' ' + item.nombre;
							option.index = i + 1;
							option.value = item.id;
							select.add(option);
						});
	}
}

function solicitarModificacionAccion(registro) {
	var row = $("#tablaAccion").DataTable().row($(registro).parents('tr'));
	document.getElementById("filaAccion").value = row.index();
	var rowData = row.data();
	
	document.getElementById("accion.nombre").value = rowData[1];
	if(rowData[2] != null && rowData[2] != "") {
		//si tiene imagen la accion
		document.getElementById("accion").src = rowData[2];
		document.getElementById("marco-accion").style.display = '';
		document.getElementById("fila-accion").style.display = 'none';
		document.getElementById("src-accion").value = rowData[2];
	}	
	document.getElementById("accion.descripcion").value = rowData[3];
	document.getElementById("accion.tipoAccion").value = rowData[4];
	document.getElementById("accion.pantallaDestino").value = rowData[5];
	$('#accionDialog').dialog('open');
}

function solicitarRegistroAccion() {
	document.getElementById("filaAccion").value = -1;
	$('#accionDialog').dialog('open');
}

function eliminarImagen(idImg, idFileUpload) {
	document.getElementById("src-" + idImg).value = "";
	document.getElementById(idImg).src = "";
	document.getElementById("marco-" + idImg).style.display = 'none';
	document.getElementById(idFileUpload).value = null;
	document.getElementById("fila-" + idImg).style.display = '';

}

function cargarImagenPantalla() {
	var imgPantalla = document.getElementById("src-pantalla").value;
	if(imgPantalla != "") {
		document.getElementById("pantalla").src = imgPantalla;
		document.getElementById("fila-pantalla").style.display = 'none';
		document.getElementById("marco-pantalla").style.display = '';
	}
}

function enviarComentarios(){
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if(vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmPantalla").submit();

}

function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeConfirmacion').dialog('close');
}

function verificarEliminacionAccion(idElemento, registro) {
	
	rutaVerificarReferencias = contextPath + '/pantallas!verificarElementosReferenciasAccion';
		
	$.ajax({
		dataType : 'json',
		url : rutaVerificarReferencias,
		type: "POST",
		data : {
			idAccion : idElemento
		},
		success : function(data) {
			mostrarMensajeEliminacion(data, registro);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
	return false;
}

function mostrarMensajeEliminacion(json, registro) {
	var elementos = document.createElement("ul");
	var elementosReferencias = document.getElementById("elementosReferencias");
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
		dataTableCDT.deleteRow(tablaAccion, registro);
	}
}

function cancelarConfirmarEliminacion() {
	$('#confirmarEliminacionDialog').dialog('close');
}

function cerrarMensajeReferencias() {
	$('#mensajeReferenciasDialog').dialog('close');
}