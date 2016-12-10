var contextPath = "prisma";


$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	$('table.tablaGestion').DataTable();
	
	var verbo = document.getElementById("paso.verbo");
	var verboTexto = verbo.options[verbo.selectedIndex].text;
	
	if (verboTexto == 'Otro' || verboTexto == 'Otra') {
		document.getElementById("otroVerbo").style.display = '';
	} else {
		document.getElementById("otroVerbo").style.display = 'none';
	}
	
	verificarAlternativaPrincipal();
	ocultarColumnas("tablaPaso");
	
	try {
		token.cargarListasToken();
	} catch (err) {
		alert("No existen elementos para referenciar con el token.");
	}
	
	cambiarElementosAlternativaPrincipal();
	var json = $("#jsonPasosTabla").val();
	
	if (json !== "") {
		var parsedJson = JSON.parse(json);
		$
				.each(
						parsedJson,
						function(i, item) {
							var row = construirFila(item.numero, item.realizaActor, item.redaccion, 
									item.verbo.nombre, item.otroVerbo);
							dataTableCDT.addRow("tablaPaso", row); 
						}); 
	}
} );

function cambiarElementosAlternativaPrincipal() {
	var select = document.getElementById("idAlternativaPrincipal");
	var varAlternativaPrincipal = select.options[select.selectedIndex].text;
	 
	if(varAlternativaPrincipal == "Principal"){
		//Si es una trayectoria principal
		document.getElementById("filaCondicion").style.display = 'none';
	    document.getElementById("model.idCondicion").value = "";
	    document.getElementById("model.finCasoUso").checked = true;
	    document.getElementById("model.finCasoUso").onclick = function() { 
           return false; 
        };
	} else if(varAlternativaPrincipal == "Alternativa") {
		//Si es una trayectoria alternativa
		document.getElementById("filaCondicion").style.display = '';
		document.getElementById("model.finCasoUso").checked = false;
		document.getElementById("model.finCasoUso").disabled = false;
	    document.getElementById("model.finCasoUso").onclick = function() { 
	           return true; 
	        };
	} else if(varAlternativaPrincipal == "Seleccione"){
		document.getElementById("filaCondicion").style.display = 'none';
		document.getElementById("model.finCasoUso").checked = false;
	    document.getElementById("model.finCasoUso").onclick = function() { 
	           return true; 
	        }
	}
}

function verificarRegistroModificacion() {
	var indexFilaPaso = document.getElementById("filaPaso").value;
	if(indexFilaPaso == -1) {
		registrarPaso();
	} else {
		modificarPaso();
	}
}

function registrarPaso(){
	var realiza = document.forms["frmPasoName"]["paso.realizaActor"].value;
	var redaccion = document.forms["frmPasoName"]["paso.redaccion"].value;
	var verbo = document.forms["frmPasoName"]["paso.verbo"].value;
	var otroVerbo = document.forms["frmPasoName"]["paso.otroVerbo"].value;
	var noPaso = calcularNumeroPaso();
    if (esValidoPaso("tablaPaso", realiza, verbo, otroVerbo ,redaccion)) {
    	var row = construirFila(noPaso, realiza, redaccion, verbo, otroVerbo);
    	dataTableCDT.addRow("tablaPaso", row);
    	limpiarCamposEmergente();
    	//Se cierra la emergente
    	$('#pasoDialog').dialog('close');
    } else {
    	return false;
    }
}

function modificarPaso(){
	var realiza = document.forms["frmPasoName"]["paso.realizaActor"].value;
	var redaccion = document.forms["frmPasoName"]["paso.redaccion"].value;
	var verbo = document.forms["frmPasoName"]["paso.verbo"].value;
	var otroVerbo = document.forms["frmPasoName"]["paso.otroVerbo"].value;
	var noPaso = 0;
    if (esValidoPaso("tablaPaso", realiza, verbo, otroVerbo ,redaccion)) {
    	var indexFilaPaso = document.getElementById("filaPaso").value;
    	var rowSelData = $("#tablaPaso").DataTable().row(indexFilaPaso).data();
    	var rowNewData = construirFila(noPaso, realiza, redaccion, verbo, otroVerbo);
		rowSelData[1] = rowNewData[1];
		rowSelData[2] = rowNewData[2];
		rowSelData[3] = rowNewData[3];
		rowSelData[4] = rowNewData[4];
		rowSelData[5] = rowNewData[5];
		dataTableCDT.editRow("tablaPaso", indexFilaPaso, rowSelData);
    	limpiarCamposEmergente();
    	$('#pasoDialog').dialog('close');
    } else {
    	return false;
    }
}

function construirFila(noPaso, realiza, redaccion, verbo, otroVerbo) {
	var verboAux;
	var realizaImg;
	//Se agrega la imagen referente a quien realiza el paso
	if(realiza == "Actor" || realiza == true) {
		var realizaActor = true;
		realizaImg = "<img src='" + window.contextPath + 
		"/resources/images/icons/actor.png' title='Actor' style='vertical-align: middle;'/>";
	} else if(realiza == "Sistema" || realiza == false) {
		realizaActor = false;
		realizaImg = "<img src='" + window.contextPath + 
		"/resources/images/icons/uc.png' title='Sistema' style='vertical-align: middle;'/>";
	}
	
	//Se construye la fila 
	if (verbo == 'Otro') {
		verboAux = otroVerbo;
	} else {
		verboAux = verbo;
	}
	var row = [
	           	noPaso,
	            realizaImg + " " + verboAux + " " +redaccion,
	            realizaActor,
	            verbo, 
	            otroVerbo,
	            redaccion,
				"<center>" +
					"<a onclick='dataTableCDT.moveRow(tablaPaso, this, \"up\");' button='true'>" +
					"<img class='icon'  id='icon' src='" + window.contextPath + 
					"/resources/images/icons/flechaArriba.png' title='Subir Paso'/></a>" +
					"<a onclick='dataTableCDT.moveRow(tablaPaso, this, \"down\");' button='true'>" +
					"<img class='icon'  id='icon' src='" + window.contextPath + 
					"/resources/images/icons/flechaAbajo.png' title='Bajar Paso'/></a>" +
					"<a button='true' onclick='solicitarModificacionPaso(this);'>" +
					"<img class='icon'  id='icon' src='" + window.contextPath + 
					"/resources/images/icons/editar.png' title='Modificar Paso'/></a>" +
					"<a onclick='dataTableCDT.deleteRowPasos(tablaPaso, this);' button='true'>" +
					"<img class='icon'  id='icon' src='" + window.contextPath + 
					"/resources/images/icons/eliminar.png' title='Eliminar Paso'/></a>" +
				"</center>" ];
	return row;
}
  
function limpiarCamposEmergente() {
	document.getElementById("inputor").value = "";
	document.getElementById("realiza").selectedIndex = 0;
	document.getElementById("paso.verbo").selectedIndex = 0;
}

function cancelarRegistrarPaso() {
	limpiarCamposEmergente();
	$('#pasoDialog').dialog('close');
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
function esValidoPaso(idTabla, realiza, verbo, otroVerbo, redaccion) {
	if(vaciaONula(redaccion) || realiza == -1 || verbo == -1) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	} 
	
	if (verbo == 'Otro') {
		if (vaciaONula(otroVerbo)) {
			agregarMensaje("Agregue todos los campos obligatorios.");
			return false;			
		}
	}
	if(redaccion.length > 999) {
		agregarMensaje("Ingrese menos de 999 caracteres.");
		return false;
	} 
 
	return true;
}

function prepararEnvio() {
	try {
		tablaToJson("tablaPaso");
		return true;
	} catch(err) {
		alert("Ha ocurrido un error.");
		return false;
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloPasos = [];
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		arregloPasos.push(new Paso(table.fnGetData(i, 0), table.fnGetData(i, 2), 
						table.fnGetData(i, 3), table.fnGetData(i, 4), table.fnGetData(i, 5)));
	}
	var jsonPasos = JSON.stringify(arregloPasos);
	document.getElementById("jsonPasosTabla").value = jsonPasos;
}

function calcularNumeroPaso() {
	return $("#tablaPaso").dataTable().fnSettings().fnRecordsTotal() + 1;
}

function ocultarColumnas(tabla) {
	var dataTable = $("#" + tabla).dataTable();
	dataTable.api().column(2).visible(false);
	dataTable.api().column(3).visible(false);
	dataTable.api().column(4).visible(false);
	dataTable.api().column(5).visible(false);

}

function verificarAlternativaPrincipal() {
	var existeTPrincipal = document.getElementById("existeTPrincipal").value;
	var select = document.getElementById("idAlternativaPrincipal");
	if(existeTPrincipal == "true") {
		select.selectedIndex = 2;
		select.disabled = true;
		document.getElementById("textoAyudaPA").innerHTML = "Solamente puede registrar Trayectorias alternativas, debido a que ya existe una Trayectoria principal.";
	} 
}

function verificarOtro() {
	var verbo = document.getElementById("paso.verbo");
	var verboTexto = verbo.options[verbo.selectedIndex].text;

	if (verboTexto == 'Otro' || verboTexto == 'Otra') {
		document.getElementById("otroVerbo").style.display = '';
	} else {
		document.getElementById("paso.otroVerbo").value = "";
		document.getElementById("otroVerbo").style.display = 'none';
	}
	
}


function solicitarModificacionPaso(registro) {
	var row = $("#tablaPaso").DataTable().row($(registro).parents('tr'));
	document.getElementById("filaPaso").value = row.index();
	var cells = row.data();
	var realizaActor = cells[2];
	if(realizaActor) {
		document.getElementById("realiza").value = "Actor";
	} else {
		document.getElementById("realiza").value = "Sistema";
	}
	document.getElementById("paso.verbo").value = cells[3];
	document.getElementById("paso.otroVerbo").value = cells[4];
	document.getElementById("inputor").value = cells[5];
	verificarOtro();
	$('#pasoDialog').dialog('open');
}

function solicitarRegistroPaso() {
	document.getElementById("filaPaso").value = -1;
	$('#pasoDialog').dialog('open');
}
