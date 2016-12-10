var contextPath = "prisma";


$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	try {
		mostrarCamposTipoRN();
		cargarListasElementos();
		seleccionarOpcionListas();
	} catch (err) {
		console.log(err);
	}
} );

function seleccionarOpcionListas() {
	var select = document.getElementById("idTipoRN");
	var tipoRN = select.options[select.selectedIndex].text;
	if(tipoRN == "Comparación de atributos") {
		idEntidad1 = document.getElementById("idEntidad1").value; 
		idAtributo1 = document.getElementById("idAtributo1").value;
		idEntidad2 = document.getElementById("idEntidad2").value;
		idAtributo2 = document.getElementById("idAtributo2").value;
		idOperador = document.getElementById("idOperador").value;
		
		document.getElementById("entidad1").value = idEntidad1;
		document.getElementById("atributo1").value = idAtributo1;
		document.getElementById("entidad2").value = idEntidad2;
		document.getElementById("atributo2").value = idAtributo2;
		document.getElementById("operador").value = idOperador;
	} else if(tipoRN == "Unicidad de parámetros"){
		idEntidadUnicidad = document.getElementById("idEntidadUnicidad").value;
		idAtributoUnicidad = document.getElementById("idAtributoUnicidad").value;
		document.getElementById("entidadUnicidad").value = idEntidadUnicidad;
		document.getElementById("atributoUnicidad").value = idAtributoUnicidad;
	} else if(tipoRN == "Formato correcto"){
		idEntidadFormato = document.getElementById("idEntidadFormato").value;
		idAtributoFormato = document.getElementById("idAtributoFormato").value;
		document.getElementById("entidadFormato").value = idEntidadFormato;
		document.getElementById("atributoFormato").value = idAtributoFormato;		
	}
}

function cargarListasElementos() {
	var select = document.getElementById("idTipoRN");
	var tipoRN = select.options[select.selectedIndex].text;
	jsonEntidades = document.getElementById("jsonEntidades").value;
	jsonAtributos = document.getElementById("jsonAtributos").value;
	jsonOperadores = document.getElementById("jsonOperadores").value;
	jsonEntidades2 = document.getElementById("jsonEntidades2").value;
	jsonAtributos2 = document.getElementById("jsonAtributos2").value;
	
	if(tipoRN == "Comparación de atributos") {
		agregarListaSelect(document.getElementById("entidad1"), jsonEntidades);
		agregarListaSelect(document.getElementById("atributo1"), jsonAtributos);
		agregarListaSelect(document.getElementById("entidad2"), jsonEntidades2);
		agregarListaSelect(document.getElementById("atributo2"), jsonAtributos2);
		agregarListaSelectOperador(document.getElementById("operador"), jsonOperadores);
	} else if(tipoRN == "Unicidad de parámetros"){
		agregarListaSelect(document.getElementById("entidadUnicidad"), jsonEntidades);
		agregarListaSelect(document.getElementById("atributoUnicidad"), jsonAtributos);
	} else if(tipoRN == "Formato correcto"){
		agregarListaSelect(document.getElementById("entidadFormato"), jsonEntidades);
		agregarListaSelect(document.getElementById("atributoFormato"), jsonAtributos);		
	}
}

function mostrarCamposTipoRN() {
	var select = document.getElementById("idTipoRN");
	var tipoRN = select.options[select.selectedIndex].text;
	//Se ocultan los todos los campos
	document.getElementById("filaEntidadFormato").className = "oculto";
	document.getElementById("filaAtributoFormato").className = "oculto";
	document.getElementById("filaExpresionRegular").className = "oculto";
	
	document.getElementById("filaEntidadUnicidad").className = "oculto";
	document.getElementById("filaAtributoUnicidad").className = "oculto";
	
	document.getElementById("filaOperador").className = "oculto";
	document.getElementById("filaEntidad1").className = "oculto";
	document.getElementById("filaAtributo1").className = "oculto";
	document.getElementById("filaEntidad2").className = "oculto";
	document.getElementById("filaAtributo2").className = "oculto";
	
	document.getElementById("filaTextoAyudaInterF").className = "oculto";
	document.getElementById("filaTextoAyudaTipoRN").className = "oculto";
	
	limpiarCampos();
	var instrucciones;
	if(tipoRN == "Verificación de catálogos"){
		document.getElementById("instrucciones").innerHTML = "Indica que el sistema deberá verificar la existencia de los catálogos para realizar alguna operación.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Comparación de atributos") {
		document.getElementById("instrucciones").innerHTML = "Indica restricciones entre los valores de algunos atributos, solamente se permite hacer comparaciones " +
																"entre atributos numéricos o entre atributos de tipo cadena.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
		document.getElementById("filaOperador").className = "";
		document.getElementById("filaEntidad1").className = "";
		document.getElementById("filaAtributo1").className = "";
		document.getElementById("filaEntidad2").className = "";
		document.getElementById("filaAtributo2").className = "";
	} else if(tipoRN == "Unicidad de parámetros"){
		document.getElementById("instrucciones").innerHTML = "Permite indicar los atributos que hacen única una entidad dentro del sistema.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
		document.getElementById("filaEntidadUnicidad").className = "";
		document.getElementById("filaAtributoUnicidad").className = "";
	} else if(tipoRN == "Datos obligatorios"){
		document.getElementById("instrucciones").innerHTML = "Indica que todos los datos marcados como obligatorios deberán ser ingresados por el usuario.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Longitud correcta"){
		document.getElementById("instrucciones").innerHTML = "Indica que la longitud máxima de los atributos no puede ser rebasada.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Tipo de dato correcto"){
		document.getElementById("instrucciones").innerHTML = "Indica que todos los campos que ingrese el usuario deberán cumplir con el tipo de dato indicado.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Formato de archivos"){
		document.getElementById("instrucciones").innerHTML = "Indica que los archivos proporcionados por el usuario deberán cumplir con el formato especificado.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Tamaño de archivos"){
		document.getElementById("instrucciones").innerHTML = "Indica que los archivos que proporcione el usuario no podrán rebasar el tamaño máximo especificado.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
	} else if(tipoRN == "Formato correcto"){
		document.getElementById("instrucciones").innerHTML = "Indica que los datos proporcionados deben cumplir con la expresión regular indicada.";
		document.getElementById("filaTextoAyudaTipoRN").className = "";
		document.getElementById("filaEntidadFormato").className = "";
		document.getElementById("filaAtributoFormato").className = "";
		document.getElementById("filaExpresionRegular").className = "";
	} else if(tipoRN == "Otro"){
	} 
	
}

function cargarCamposTipoRN() {
	var select = document.getElementById("idTipoRN");
	var tipoRN = select.options[select.selectedIndex].text;
	
	if(tipoRN == "Comparación de atributos") {
		cargarEntidades("entidad1");
	} else if(tipoRN == "Unicidad de parámetros"){
		cargarEntidades("entidadUnicidad");
	} else if(tipoRN == "Formato correcto"){
		cargarEntidades("entidadFormato");		
	} 
	
}
//UNICIDAD DE PARÁMETROS, COMPARACIÓN DE ATRIBUTOS
function cargarEntidades(idSelect) {
	var idTipoRN = document.getElementById("idTipoRN").value;
	var select = document.getElementById(idSelect);
	rutaCargarEntidades = contextPath + '/reglas-negocio!cargarEntidades';
	$.ajax({
		dataType : 'json',
		url : rutaCargarEntidades,
		type: "POST",
		data : {
			idTipoRN : idTipoRN
		},
		success : function(data) {
			agregarListaSelect(select, data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

//UNICIDAD DE PARÁMETROS, COMPARACIÓN DE ATRIBUTOS
function cargarAtributos(select, idSelectAtributos) {
	limpiarCamposDependientes(select.id);
	var idEntidad = select.value;
	rutaCargarAtributos = contextPath + '/reglas-negocio!cargarAtributos';
	$.ajax({
		dataType : 'json',
		url : rutaCargarAtributos,
		type: "POST",
		data : {
			idEntidad : idEntidad,
		},
		success : function(data) {
			agregarListaSelect(document.getElementById(idSelectAtributos), data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}


//COMPARACIÓN DE ATRIBUTOS
function cargarOperadores(select) {
	var idTipoRN = document.getElementById("idTipoRN").value;
	var idAtributo = select.value;
	rutaCargarOperadores = contextPath + '/reglas-negocio!cargarOperadores';
	$.ajax({
		dataType : 'json',
		url : rutaCargarOperadores,
		type: "POST",
		data : {
			idAtributo : idAtributo
		},
		success : function(data) {
			agregarListaSelectOperador(document.getElementById("operador"), data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

//COMPARACIÓN DE ATRIBUTOS
function cargarEntidadesDependientes(select, idSelectEntidades) {
	var idAtributo = select.value;
	rutaCargarEntidades = contextPath + '/reglas-negocio!cargarEntidadesDependientes';
	$.ajax({
		dataType : 'json',
		url : rutaCargarEntidades,
		type: "POST",
		data : {
			idAtributo : idAtributo
		},
		success : function(data) {
			agregarListaSelect(document.getElementById(idSelectEntidades), data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

//COMPARACIÓN DE ATRIBUTOS
function cargarAtributosDependientes(select, idSelectAtributos) {
	var idEntidad = select.value;
	var idAtributo = document.getElementById("atributo1").value;
	rutaCargarAtributos = contextPath + '/reglas-negocio!cargarAtributosDependientes';
	$.ajax({
		dataType : 'json',
		url : rutaCargarAtributos,
		type: "POST",
		data : {
			idAtributo : idAtributo,
			idEntidad : idEntidad
		},
		success : function(data) {
			agregarListaSelect(document.getElementById(idSelectAtributos), data);
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}

function agregarListaSelect(select, json) {
	if (json !== "") {
		if(typeof(json) == "string") {
			json = JSON.parse(json);
		}
	
		var opcSeleccionada = select.value;
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
							option.text = item.nombre;
							option.index = i;
							option.value = item.id;
							select.add(option);
						});
		select.value = opcSeleccionada;
	}
	
} 

function agregarListaSelectOperador(select, json) {
	if (json !== "") {	
		if(typeof(json) == "string") {
			json = JSON.parse(json);
		}
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
							option.text = item.simbolo;
							option.index = i;
							option.value = item.id;
							select.add(option);
						});
	}
} 

function registrarPaso(){
	var numero = calcularNumeroPaso();
	var realiza = document.forms["frmPasoName"]["paso.realizaActor"].value;
	var redaccion = document.forms["frmPasoName"]["paso.redaccion"].value;
	var verbo = document.forms["frmPasoName"]["paso.verbo"].value;
	
	var up = "up";
    if (esValidoPaso("tablaPaso", realiza, verbo, redaccion)) {
    	var realizaImg;
    	//Se agrega la imagen referente a quien realiza el paso
    	if(realiza == "Actor") {
    		var realizaActor = true;
    		realizaImg = "<img src='" + window.contextPath + 
			"/resources/images/icons/actor.png' title='Actor' style='vertical-align: middle;'/>";
    	} else if(realiza == "Sistema") {
    		realizaActor = false;
    		realizaImg = "<img src='" + window.contextPath + 
			"/resources/images/icons/uc.png' title='Sistema' style='vertical-align: middle;'/>";
    	}
    	//Se construye la fila 
    	var row = [
    	            numero,
    	            realizaImg + " " + verbo + " " +redaccion,
    	            realizaActor,
    	            verbo, 
    	            redaccion,
					"<center>" +
						"<a onclick='dataTableCDT.moveRow(tablaPaso, this, \"up\");' button='true'>" +
						"<img class='icon'  id='icon' src='" + window.contextPath + 
						"/resources/images/icons/flechaArriba.png' title='Subir Paso'/></a>" +
						"<a onclick='dataTableCDT.moveRow(tablaPaso, this, \"down\");' button='true'>" +
						"<img class='icon'  id='icon' src='" + window.contextPath + 
						"/resources/images/icons/flechaAbajo.png' title='Bajar Paso'/></a>" +
						"<a button='true'>" +
						"<img class='icon'  id='icon' src='" + window.contextPath + 
						"/resources/images/icons/editar.png' title='Modificar Paso'/></a>" +
						"<a onclick='dataTableCDT.deleteRowPasos(tablaPaso, this);' button='true'>" +
						"<img class='icon'  id='icon' src='" + window.contextPath + 
						"/resources/images/icons/eliminar.png' title='Eliminar Paso'/></a>" +
					"</center>" ];
    	dataTableCDT.addRow("tablaPaso", row);
    	
    	//Se limpian los campos
    	document.getElementById("inputor").value = "";
    	document.getElementById("realiza").selectedIndex = 0;
    	document.getElementById("verbo").selectedIndex = 0;
    	
    	//Se cierra la emergente
    	$('#pasoDialog').dialog('close');
    } else {
    	return false;
    }
};
  
function cancelarRegistrarPaso() {
	//Se limpian los campos
	document.getElementById("inputor").value = "";
	document.getElementById("realiza").selectedIndex = 0;
	document.getElementById("verbo").selectedIndex = 0;
	
	//Se cierra la emergente
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
function esValidoPaso(idTabla, realiza, verbo, redaccion) {
	if(vaciaONula(redaccion) && realiza != -1 && verbo != -1) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	} 
	if(redaccion.length > 999) {
		agregarMensaje("Ingrese menos de 999 caracteres.");
		return false;
	} 
 
	return true;
}

function calcularNumeroPaso() {
	return $("#tablaPaso").dataTable().fnSettings().fnRecordsTotal() + 1;
}

function ocultarColumnas(tabla) {
	var dataTable = $("#" + tabla).dataTable();
	dataTable.api().column(2).visible(false);
	dataTable.api().column(3).visible(false);
	dataTable.api().column(4).visible(false);
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

function bloquearOpcion(select) {
	if(select.id == "atributo1") {
		var elemento = document.getElementById("entidad2");
		if(elemento.value != -1) {
			//Aun no han seleccionado opcion
			
		} 
	} else if(select.id == "atributo2") {
		
	}
}

function limpiarCampos() {
	//Se selecciona la primer opción de los elementos
	document.getElementById("entidadUnicidad").selectedIndex = 0;
	document.getElementById("atributoUnicidad").selectedIndex = 0;

	document.getElementById("operador").selectedIndex = 0;
	document.getElementById("entidad1").selectedIndex = 0;
	document.getElementById("atributo1").selectedIndex = 0;
	document.getElementById("entidad2").selectedIndex = 0;
	document.getElementById("atributo2").selectedIndex = 0;
}

function limpiarCamposDependientes(idSelect) {
	if(idSelect == "entidad1") {
		document.getElementById("operador").selectedIndex = 0;
		document.getElementById("atributo1").selectedIndex = 0;
		document.getElementById("entidad2").selectedIndex = 0;
		document.getElementById("atributo2").selectedIndex = 0;
	}
}

function prepararEnvio() {
//	$('#mensajeConfirmacion').dialog('open');
}