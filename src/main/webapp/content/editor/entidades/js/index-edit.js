var contextPath = "prisma";

$(document).ready(
		function() {
			window.scrollTo(0, 0);
			contextPath = $("#rutaContexto").val();
			$('table.tablaGestion').DataTable();
			ocultarColumnas("tablaAtributo");
			var json = $("#jsonAtributosTabla").val();
			if (json !== "") {
				var parsedJson = JSON.parse(json);
				$.each(parsedJson, function(i, item) {
					if (item.unidadTamanio != null) {
						abreviatura = item.unidadTamanio.abreviatura;
					} else {
						abreviatura = null;
					}

					var atributo = construirFila(item.nombre, item.descripcion,
							item.tipoDato.nombre, item.otroTipoDato,
							item.longitud, item.formatoArchivo,
							item.tamanioArchivo, abreviatura, item.obligatorio,
							item.id);
					dataTableCDT.addRow("tablaAtributo", atributo);
				});
			}
		});

function verificarRegistroModificacion() {
	var indexFilaAtributo = document.getElementById("filaAtributo").value;
	if (indexFilaAtributo == -1) {
		registrarAtributo();
	} else {
		modificarAtributo();
	}
}

function registrarAtributo() {
	var nombre = document.getElementById("atributo.nombre").value;
	var descripcion = document.getElementById("atributo.descripcion").value;
	var tipoDatoSelect = document.getElementById("atributo.tipoDato");
	var otroTipoDato = document.getElementById("atributo.otroTipoDato").value;
	var longitud = document.getElementById("atributo.longitud").value;
	var formatoArchivo = document.getElementById("atributo.formatoArchivo").value;
	var tamanioArchivo = document.getElementById("atributo.tamanioArchivo").value;
	var unidadTamanioSelect = document.getElementById("atributo.unidadTamanio");
	var obligatorio = document.getElementById("atributo.obligatorio").checked;
	var tipoDato = tipoDatoSelect.options[tipoDatoSelect.selectedIndex].text;
	var unidadTamanio = unidadTamanioSelect.options[unidadTamanioSelect.selectedIndex].text;

	if (esValidoAtributo("tablaAtributo", nombre, descripcion, tipoDatoSelect,
			otroTipoDato, longitud, formatoArchivo, tamanioArchivo,
			unidadTamanioSelect)) {
		var row = construirFila(nombre, descripcion, tipoDato, otroTipoDato,
				longitud, formatoArchivo, tamanioArchivo, unidadTamanio,
				obligatorio, 0);
		dataTableCDT.addRow("tablaAtributo", row);
		limpiarCamposEmergente();
		$('#atributoDialog').dialog('close');
	} else {
		return false;
	}
}

function modificarAtributo() {
	var nombre = document.getElementById("atributo.nombre").value;
	var descripcion = document.getElementById("atributo.descripcion").value;
	var tipoDatoSelect = document.getElementById("atributo.tipoDato");
	var otroTipoDato = document.getElementById("atributo.otroTipoDato").value;
	var longitud = document.getElementById("atributo.longitud").value;
	var formatoArchivo = document.getElementById("atributo.formatoArchivo").value;
	var tamanioArchivo = document.getElementById("atributo.tamanioArchivo").value;
	var unidadTamanioSelect = document.getElementById("atributo.unidadTamanio");
	var obligatorio = document.getElementById("atributo.obligatorio").checked;
	var tipoDato = tipoDatoSelect.options[tipoDatoSelect.selectedIndex].text;
	var unidadTamanio = unidadTamanioSelect.options[unidadTamanioSelect.selectedIndex].text;
	var indexFilaAtributo = document.getElementById("filaAtributo").value;

	if (esValidoAtributo("tablaAtributo", nombre, descripcion, tipoDatoSelect,
			otroTipoDato, longitud, formatoArchivo, tamanioArchivo,
			unidadTamanioSelect, indexFilaAtributo)) {
		var rowSelData = $("#tablaAtributo").DataTable().row(indexFilaAtributo)
				.data();
		var rowNewData = construirFila(nombre, descripcion, tipoDato,
				otroTipoDato, longitud, formatoArchivo, tamanioArchivo,
				unidadTamanio, obligatorio, 0);
		rowSelData[0] = rowNewData[0];
		rowSelData[1] = rowNewData[1];
		rowSelData[2] = rowNewData[2];
		rowSelData[3] = rowNewData[3];
		rowSelData[4] = rowNewData[4];
		rowSelData[5] = rowNewData[5];
		rowSelData[6] = rowNewData[6];
		rowSelData[7] = rowNewData[7];
		rowSelData[8] = rowNewData[8];
		rowSelData[9] = rowNewData[9];
		dataTableCDT.editRow("tablaAtributo", indexFilaAtributo, rowSelData);
		limpiarCamposEmergente();
		$('#atributoDialog').dialog('close');
	} else {
		return false;
	}
}

function construirFila(nombre, descripcion, tipoDato, otroTipoDato, longitud,
		formatoArchivo, tamanioArchivo, unidadTamanio, obligatorio, id) {
	var tipoDatoAux;
	if (obligatorio == true) {
		obligatorio = "Sí";
	} else {
		obligatorio = "No";
	}

	if (tipoDato == 'Otro') {
		tipoDatoAux = otroTipoDato;
	} else {
		tipoDatoAux = tipoDato;
	}
	var row = [
			nombre,
			tipoDatoAux,
			obligatorio,
			tipoDato,
			otroTipoDato,
			descripcion,
			longitud,
			formatoArchivo,
			tamanioArchivo,
			unidadTamanio,
			id,
			"<center>"
					+ "<a button='true' onclick='solicitarModificacionAtributo(this);'>"
					+ "<img class='icon'  id='icon' src='"
					+ window.contextPath
					+ "/resources/images/icons/editar.png' title='Modificar Atributo'/></a>"
					+ "<a onclick='dataTableCDT.deleteRow(tablaAtributo, this);' button='true'>"
					+ "<img class='icon'  id='icon' src='"
					+ window.contextPath
					+ "/resources/images/icons/eliminar.png' title='Eliminar Atributo'/></a>"
					+ "</center>" ];
	return row;
}

function limpiarCamposEmergente() {
	document.getElementById("atributo.nombre").value = null;
	document.getElementById("atributo.descripcion").value = null;
	document.getElementById("atributo.tipoDato").selectedIndex = 0;
	document.getElementById("atributo.otroTipoDato").value = null;
	document.getElementById("atributo.longitud").value = null;
	document.getElementById("atributo.formatoArchivo").value = null;
	document.getElementById("atributo.tamanioArchivo").value = null;
	document.getElementById("atributo.unidadTamanio").selectedIndex = 0;
	document.getElementById("atributo.obligatorio").checked = false;

	document.getElementById("trOtro").style.display = 'none';
	document.getElementById("trLongitud").style.display = 'none';
	document.getElementById("trFormatoArchivo").style.display = 'none';
	document.getElementById("trTamanioArchivo").style.display = 'none';
}

function cancelarRegistrarAtributo() {
	limpiarCamposEmergente();
	$('#atributoDialog').dialog('close');
}

function agregarMensaje(mensaje) {
	alert(mensaje);
};

function esValidoAtributo(idTabla, nombre, descripcion, tipoDato, otroTipoDato,
		longitud, formatoArchivo, tamanioArchivo, unidadTamanio, indexRow) {

	var tipoDatoTexto = tipoDato.options[tipoDato.selectedIndex].text;

	/*
	 * Inicia la validación del nombre y descripción, los cuales se deben
	 * validar independientemente del tipo de dato seleccionado.
	 */

	if (vaciaONula(nombre) || vaciaONula(descripcion)
			|| tipoDato.selectedIndex == 0) {
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

	if (tipoDatoTexto == 'Booleano' || tipoDatoTexto == 'Fecha'
			|| tipoDatoTexto == 'Archivo' || tipoDatoTexto == 'Otro') {
		if (tipoDatoTexto == 'Archivo') {
			if (vaciaONula(tamanioArchivo) || vaciaONula(formatoArchivo)
					|| unidadTamanio.selectedIndex == 0) {
				agregarMensaje("Agregue todos los campos obligatorios.");
				return false;
			}
			if (!esFloat(tamanioArchivo) && !esEntero(tamanioArchivo)) {
				agregarMensaje("Ingrese un tamaño válido.");
				return false;
			}
			if (tipoDato == 'Otro') {
				if (vacioONula(otroTipoDato)) {
					agregarMensaje("Agregue todos los campos obligatorios.");
					return false;
				}
			}
		}
	} else {
		
		if (vaciaONula(longitud)) {
			agregarMensaje("Agregue todos los campos obligatorios.");
			return false;
		}
		
		if (longitud > 1999) {
			agregarMensaje("Ingrese una longitud menor a 1999.");
			return false;
		}
		
		if (!esEntero(longitud)) {
			agregarMensaje("Ingrese una longitud válida.");
			return false;
		}
	}

	if (dataTableCDT.exist(idTabla, nombre, 0, "", "Atributo", indexRow)) {
		agregarMensaje("Este atributo ya está en la entidad.");
		return false;
	}

	return true;
}

function ocultarColumnas(tabla) {
	var dataTable = $("#" + tabla).dataTable();
	dataTable.api().column(3).visible(false);
	dataTable.api().column(4).visible(false);
	dataTable.api().column(5).visible(false);
	dataTable.api().column(6).visible(false);
	dataTable.api().column(7).visible(false);
	dataTable.api().column(8).visible(false);
	dataTable.api().column(9).visible(false);
	dataTable.api().column(10).visible(false);

}

function esEntero(str) {
	var i;
	try {
		if (isNaN(str)) {
			return false
		} 
		i = parseInt(str)
		
		if (i < 0 || i > 2147483647) {
			return false;
		}
		return true;
	} catch (err) {
		return false;
	}
}

function esFloat(str) {
	return /^(\.(\d+)([1-9]))|(\d+)(\.)(\d+)([1-9])$/.test(str);
}

function disablefromTipoDato() {
	document.getElementById("atributo.longitud").value = null;
	document.getElementById("atributo.formatoArchivo").value = null;
	document.getElementById("atributo.tamanioArchivo").value = null;
	document.getElementById("atributo.unidadTamanio").selectedIndex = 0;
	document.getElementById("atributo.otroTipoDato").value = null;

	document.getElementById("trTextoAyudaFormato").style.display = 'none';
	var tipoDato = document.getElementById("atributo.tipoDato");
	var tipoDatoTexto = tipoDato.options[tipoDato.selectedIndex].text;

	if (tipoDatoTexto == 'Booleano' || tipoDatoTexto == 'Fecha'
			|| tipoDatoTexto == 'Archivo' || tipoDatoTexto == 'Seleccione'
			|| tipoDatoTexto == 'Otro') {
		document.getElementById("trLongitud").style.display = 'none';
		if (tipoDatoTexto == 'Archivo') {
			document.getElementById("trFormatoArchivo").style.display = '';
			document.getElementById("trTamanioArchivo").style.display = '';
			document.getElementById("trTextoAyudaFormato").style.display = '';
		} else {
			document.getElementById("trFormatoArchivo").style.display = 'none';
			document.getElementById("trTamanioArchivo").style.display = 'none';
			document.getElementById("trTextoAyudaFormato").style.display = 'none';
		}
		if (tipoDatoTexto == 'Otro') {
			document.getElementById("trOtro").style.display = '';
		} else {
			document.getElementById("trOtro").style.display = 'none';
		}
	} else {
		document.getElementById("trLongitud").style.display = '';
		document.getElementById("trOtro").style.display = 'none';
		document.getElementById("trFormatoArchivo").style.display = 'none';
		document.getElementById("trTamanioArchivo").style.display = 'none';
	}
}

function prepararEnvio() {
	try {
		tablaToJson("tablaAtributo");
		//$('#mensajeConfirmacion').dialog('open');
		return true;
	} catch (err) {
		alert("Ocurrió un error: " + err);
		return false;
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloAtributos = [];

	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		var nombre = table.fnGetData(i, 0);
		var obligatorio = table.fnGetData(i, 2);
		var tipoDato = table.fnGetData(i, 3);
		var otroTipoDato = table.fnGetData(i, 4);

		var descripcion = table.fnGetData(i, 5);
		var longitud = table.fnGetData(i, 6);
		var formatoArchivo = table.fnGetData(i, 7);
		var tamanioArchivo = table.fnGetData(i, 8);
		var unidadTamanio = table.fnGetData(i, 9);
		var id = table.fnGetData(i, 10);

		if (obligatorio == 'Sí') {
			obligatorio = true;
		} else {
			obligatorio = false;
		}

		arregloAtributos.push(new Atributo(nombre, descripcion, obligatorio,
				longitud, tipoDato, otroTipoDato, formatoArchivo,
				tamanioArchivo, unidadTamanio, id));
	}
	var jsonAtributos = JSON.stringify(arregloAtributos);
	document.getElementById("jsonAtributosTabla").value = jsonAtributos;
}

function solicitarModificacionAtributo(registro) {

	var row = $("#tablaAtributo").DataTable().row($(registro).parents('tr'));

	document.getElementById("filaAtributo").value = row.index();

	var cells = row.data();

	document.getElementById("atributo.tipoDato").value = cells[3];
	disablefromTipoDato();

	document.getElementById("atributo.nombre").value = cells[0];
	document.getElementById("atributo.descripcion").value = cells[5];
	document.getElementById("atributo.otroTipoDato").value = cells[4];
	document.getElementById("atributo.longitud").value = cells[6];
	document.getElementById("atributo.formatoArchivo").value = cells[7];
	document.getElementById("atributo.tamanioArchivo").value = cells[8];
	document.getElementById("atributo.unidadTamanio").value = cells[9];
	document.getElementById("idAtributo").value = cells[10];
	if (cells[2] == "No") {
		document.getElementById("atributo.obligatorio").checked = false;
	} else {
		document.getElementById("atributo.obligatorio").checked = true;
	}

	$('#atributoDialog').dialog('open');
}
function solicitarRegistroAtributo() {
	document.getElementById("filaAtributo").value = -1;
	$('#atributoDialog').dialog('open');
}

function enviarComentarios() {
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if (vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmEntidad").submit();

}

function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeConfirmacion').dialog('close');
}