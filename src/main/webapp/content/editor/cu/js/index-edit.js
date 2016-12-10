var contextPath = "prisma";
$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	$('table.tablaGestion').DataTable();
	try {
		token.cargarListasToken();
	} catch (err) {
		alert("No existen elementos para referenciar con el token.");
	}
	ocultarTokens();
	var json = $("#jsonPrecondiciones").val();
	if (json !== "" && json != null) {
		var parsedJson = JSON.parse(json);
		$
				.each(
						parsedJson,
						function(i, item) {
							var paso = [
									item.redaccion,
									"<center><a onclick='dataTableCDT.deleteRow(tablaPrecondiciones,this);'><img class='icon' src='"
											+ contextPath
											+ "/resources/images/icons/eliminar.png' title='Eliminar Precondición'></img></a></center>" ];
							dataTableCDT.addRow("tablaPrecondiciones",paso);
						});
	}
	json = $("#jsonPostcondiciones").val();
	if (json !== "") {
		var parsedJson = JSON.parse(json);
		$
				.each(
						parsedJson,
						function(i, item) {
							var paso = [
									item.redaccion,
									"<center><a onclick='dataTableCDT.deleteRow(tablaPostcondiciones,this);'><img class='icon' src='"
											+ window.contextPath
											+ "/resources/images/icons/eliminar.png' title='Eliminar Postcondición'></img></a></center>" ];
							dataTableCDT.addRow("tablaPostcondiciones",paso);
						});
	}
} );

function ocultarTokens() {
	$("div[id^='at-view']").each(function(index, value) {
		var id = $(this).attr('style', 'none');
	});
}
function registrarPrecondicion(){
	var varRedaccion = document.forms["frmPrecondicionName"]["precondicion.redaccion"].value;
	var campoErrorRedaccion = document.getElementById("errorRedaccion");
	 
    if (esValidaPostPrecondicion("tablaPrecondiciones", varRedaccion)) {
    	var row = [
					varRedaccion,
					"<center>" +
							"<a onclick='dataTableCDT.deleteRow(tablaPrecondiciones,this);'>" +
							"<img class='icon'  id='icon' src='" +
					window.contextPath + "/resources/images/icons/eliminar.png' title='Eliminar Precondición'></img></a></center>" ];
    	dataTableCDT.addRow("tablaPrecondiciones", row);
    	document.getElementById("precondicionInput").value = "";
    	$('#precondDialog').dialog('close');
    } else {
    	return false;
    }
};
  
function cancelarRegistrarPrecondicion() {
	document.getElementById("precondicionInput").value = "";
	$('#precondDialog').dialog('close');
};

function registrarPostcondicion(){
	var varRedaccion = document.forms["frmPostcondicionName"]["postcondicion.redaccion"].value;
	var campoErrorRedaccion = document.getElementById("errorRedaccion");
	 
    if (esValidaPostPrecondicion("tablaPostcondiciones", varRedaccion)) {
    	var obj = new PostPrecondicion(varRedaccion, false);
    	var postprecondicion = JSON.stringify(obj);
    	var row = [
					varRedaccion,
					"<center><a onclick='dataTableCDT.deleteRow(tablaPostcondiciones,this);'>"
							+ "<img class='icon'  id='icon' src='" + window.contextPath
							+ "/resources/images/icons/eliminar.png' title='Eliminar Postcondición'></img></a></center>" ];
    	dataTableCDT.addRow("tablaPostcondiciones", row);
    	document.getElementById("postcondicionInput").value = "";
    	$('#postcondDialog').dialog('close');
    } else {
    	return false;
    }
};
  
function cancelarRegistrarPostcondicion() {
	document.getElementById("postcondicionInput").value = "";
	$('#postcondDialog').dialog('close');
};

/*
 * Agrega un mensaje en la pantalla
 */
function agregarMensaje(mensaje) {
	alert(mensaje);
};

/*
 * Verifica que la redacción sea válida
 */
function esValidaPostPrecondicion(idTabla, redaccion) {
	if(vaciaONula(redaccion)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}

	if (dataTableCDT.exist(idTabla, redaccion, 0, "", "Mensaje")) {
    	agregarMensaje("Este elemento ya está en el caso de uso.");
    	return false;
    } 
	return true;
}

/*
 * Verifica que el punto de extensión sea válido
 */
function esValidoPtoExtension(idTabla, varIdCUDestino, varCausa, varRegion) {
	if(vaciaONula(varCausa) || vaciaONula(varRegion)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}

	return true;
}

function prepararEnvio() {
	try {
		tablaPrecondicionesToJson("tablaPrecondiciones");
		tablaPostcondicionesToJson("tablaPostcondiciones");
		//$('#mensajeConfirmacion').dialog('open');
		return true;
	} catch(err) {
		alert("Ha ocurrido un error.");
		return false;
	}
}

function tablaPrecondicionesToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arreglo = [];
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		arreglo.push(new PostPrecondicion(table.fnGetData(i, 0), "true"));
	}
	var json = JSON.stringify(arreglo);
	document.getElementById("jsonPrecondiciones").value = json;
}

function tablaPostcondicionesToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arreglo = [];
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		arreglo.push(new PostPrecondicion(table.fnGetData(i, 0), "false"));
	}
	var json = JSON.stringify(arreglo);
	document.getElementById("jsonPostcondiciones").value = json;
}

function enviarComentarios(){
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if(vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmCU").submit();

	}
function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeConfirmacion').dialog('close');
}