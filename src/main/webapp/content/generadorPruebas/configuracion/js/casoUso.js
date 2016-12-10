$(document).ready(function() {
	$("#modal").css("display", "none");
	contextPath = $("#rutaContexto").val();
	var jsonAcciones = document.getElementById("jsonAcciones");
	
	agregarCamposEntradasSeccion();
	agregarCamposAccionesSeccion();
	agregarCamposReglasNegocioSeccion();
	agregarCamposParametrosSeccion();
	agregarCamposPantallasSeccion();
	
});

function agregarCamposEntradasSeccion() {
	var json = document.getElementById("jsonEntradas").value;
	if (json !== "" && json !== "[]") {
		var parsedJson = JSON.parse(json);
		$("#tablaEntradas").append("<tr>"
				+ "<td> <!-- --> </td>"
				+ "<td class='ui-widget'><center>Name</center></td>"
				+ "<td class='ui-widget'><center>Value</center></td>"
			+"</tr>");
		
		$.each(
				parsedJson,
				function(i, item) {
					if(item.valores != null){
						valor = item.valores[0].valor;
						idValor = item.valores[0].id;
						}
					else if(item.valoresEntradaTrayectoria != '') {
						valor = item.valoresEntradaTrayectoria[0].valor;
						idValor = item.valoresEntradaTrayectoria[0].id;
					} else {
						valor = "";
						idValor = 0;
					}
					
					inputEtiqueta = "<input type='text' class='inputFormulario ui-widget' id='input-etiqueta-entrada-" + item.id  + "' value='" + nullToEmpty(item.nombreHTML) + "'>";
					inputValor = "<input type='text' class='inputFormulario ui-widget' id='input-valor-entrada-" + item.id  + "' value='" + valor + "'>";
					nombreAtributo = null;
					nombreTermino = null;
					if(item.atributo != null) {
						labelEntrada = item.atributo.nombre;
						nombreAtributo = item.atributo.nombre;
					}
					if(item.terminoGlosario != null) {
						labelEntrada = item.terminoGlosario.nombre;
						nombreTermino = item.terminoGlosario.nombre;
					}
					 
					$("#tablaEntradas").append("<tr>"
									+ "<td class='label obligatorio'>" + labelEntrada + "</td>"
									+ "<td>" + inputEtiqueta + "</td>"
									+ "<td>" + inputValor + "</td>"
									+ "<td class='hide'>" + item.id + "</td>"
									+ "<td class='hide'>" + idValor + "</td>"
									+ "<td class='hide'>" + item.nombreHTML + "</td>"
									+ "<td class='hide'>" + nombreAtributo + "</td>"
									+ "<td class='hide'>" + nombreTermino + "</td>"
								+"</tr>");
					
		});
		
	} else {
		$("#formularioEntradas").hide();
	}
}

function agregarCamposAccionesSeccion() {
	var json = document.getElementById("jsonAcciones").value;
	var jsonImg = document.getElementById("jsonImagenesPantallasAcciones").value;
	if (json !== "" && json !== "[]" && jsonImg !== "" && jsonImg !== "[]") {
		var parsedJson = JSON.parse(json);
		var parsedJsonImg = JSON.parse(jsonImg);
		
		$.each(
				parsedJson,
				function(i, item) {
					idTablaPantalla =  item.pantalla.id;
					tablaPantalla = $("#tabla-acciones-" + idTablaPantalla);
					if (tablaPantalla.size() == 0) {
						$("#seccionURL").append("<div class='subtituloFormulario'><a class='referencia' href='#' onclick=\"mostrarPantalla('" + 
								parsedJsonImg[i] +"');\">Pantalla " + item.pantalla.clave + item.pantalla.numero + " " + item.pantalla.nombre +"</a></div>");
						$("#seccionURL").append("<input type='hidden' value='" + parsedJsonImg[i] + "' id='img-pantalla-" + item.pantalla.id + "'/>");
						$("#seccionURL").append("<table id='tabla-acciones-" + idTablaPantalla + "'> <!--  --> </table>");
						
						$("#tabla-acciones-" + idTablaPantalla).append("<tr>"
								+ "<td> <!-- --> </td>"
								+ "<td class='ui-widget'><center>URL</center></td>"
								+ "<td class='ui-widget'><center>Método</center></td>"
							+"</tr>");
						$("#seccionURL").append("</br>");
						
					} 

					inputURL = "<input type='text' class='inputFormularioGrande ui-widget' id='input-url-accion-" + item.id  + "' value='" + nullToEmpty(item.urlDestino) + "'>";
					inputMetodo = " <select id='input-metodo-accion-" + item.id  + "'> "
									  + "<option value='GET'>GET</option>"
									  + "<option value='POST'>POST</option>"
									+ "</select> ";
					
					label = item.tipoAccion.nombre + " " + item.nombre;
					
					if(item.pantallaDestino != null) {
						clavePantallaDestino = item.pantallaDestino.clave;
						numeroPantallaDestino = item.pantallaDestino.numero;
						nombrePantallaDestino = item.pantallaDestino.nombre;
						pantallaDestino = clavePantallaDestino + numeroPantallaDestino + " " + nombrePantallaDestino;
						$("#tabla-acciones-" + idTablaPantalla).append("<tr>"
								+ "<td class='label obligatorio'>" + label + "</td>"
								+ "<td>" + inputURL + "</td>"
								+ "<td>" + inputMetodo + "</td>"
								+ "<td class='hide'>" + item.id + "</td>"
								+ "<td class='hide'>" + item.nombre + "</td>"
								+ "<td class='hide'>" + clavePantallaDestino + "</td>"
								+ "<td class='hide'>" + numeroPantallaDestino + "</td>"
								+ "<td class='hide'>" + nombrePantallaDestino + "</td>"
								+ "<td class='hide'>" + item.pantalla.id + "</td>"
								+ "<td class='hide'>" + item.pantalla.clave + "</td>"
								+ "<td class='hide'>" + item.pantalla.numero + "</td>"
								+ "<td class='hide'>" + item.pantalla.nombre + "</td>"
								+ "<td class='hide'>" + item.tipoAccion.nombre + "</td>"
								+ "<td class='textoAyuda'>Dirige a la pantalla " + pantallaDestino + "</td>"
							+"</tr>");
					} else {
						clavePantallaDestino = "";
						numeroPantallaDestino = "";
						nombrePantallaDestino = "";
						pantallaDestino = "";
						$("#tabla-acciones-" + idTablaPantalla).append("<tr>"
								+ "<td class='label obligatorio'>" + label + "</td>"
								+ "<td>" + inputURL + "</td>"
								+ "<td>" + inputMetodo + "</td>"
								+ "<td class='hide'>" + item.id + "</td>"
								+ "<td class='hide'>" + item.nombre + "</td>"
								+ "<td class='hide'>" + clavePantallaDestino + "</td>"
								+ "<td class='hide'>" + numeroPantallaDestino + "</td>"
								+ "<td class='hide'>" + nombrePantallaDestino + "</td>"
								+ "<td class='hide'>" + item.pantalla.id + "</td>"
								+ "<td class='hide'>" + item.pantalla.clave + "</td>"
								+ "<td class='hide'>" + item.pantalla.numero + "</td>"
								+ "<td class='hide'>" + item.pantalla.nombre + "</td>"
								+ "<td class='hide'>" + item.tipoAccion.nombre + "</td>"
							+"</tr>");
					}
					
				    
					
					
					document.getElementById("input-metodo-accion-" + item.id).value = nullToEmpty(item.metodo);

		});
		
	} else {
		$("#formularioAcciones").hide();
	}
}

function agregarCamposReglasNegocioSeccion() {
	var json = document.getElementById("jsonReferenciasReglasNegocio").value;
	if (json !== "" && json !== "[]") {
		var parsedJson = JSON.parse(json);
		
		$.each(
				parsedJson,
				function(i, item) {
					var valor = "";
					var idQuery = 0;

					if(item.queries != null) {
						var query;
						$.each(item.queries, function(i, query){
							if(query.referenciaParametro.id == item.id) {
								valor = query.query; 
								idQuery = query.id;
							}
						});
					}
					
					var claveRN = item.elementoDestino.clave;
					var numeroRN = item.elementoDestino.numero;
					var nombreRN = item.elementoDestino.nombre;
					
					reglaNegocio = claveRN + numeroRN + " " + nombreRN;
					
					var paso = item.paso;
					var realiza = paso.realizaActor;
					var realizaImg = "";
					if(realiza == "Actor" || realiza == true || realiza == "true") {
						var realizaActor = true;
						realizaImg = "<img src='" + window.contextPath + 
						"/resources/images/icons/actor.png' title='Actor' style='vertical-align: middle;'/>";
					} else if(realiza == "Sistema" || realiza == false || realiza == "false") {
						realizaActor = false;
						realizaImg = "<img src='" + window.contextPath + 
						"/resources/images/icons/uc.png' title='Sistema' style='vertical-align: middle;'/>";
					}
					
					var nombreVerbo = paso.verbo.nombre;
					var otroVerbo = paso.otroVerbo;
					var verboAux = "";
					if (nombreVerbo == 'Otro') {
						verboAux = otroVerbo;
					} else {
						verboAux = nombreVerbo;
					}
					
					
					idTablaRN = item.elementoDestino.id;
					tablaRN = $("#tabla-queries-" + idTablaRN);
					if (tablaRN.size() == 0) {
						$("#seccionRN").append("<div class='subtituloFormulario'>" + reglaNegocio +"</div>");
						$("#seccionRN").append("<div class='textoAyuda'>Paso: " + realizaImg + " " + verboAux + " " +  item.paso.redaccion +"</div>");
						$("#seccionRN").append("<table class='seccion' id='tabla-queries-" + idTablaRN + "'> <!--  --> </table>");
						$("#seccionRN").append("</br>");
						
					}
					
					inputQuery = "<input type='text' class='ui-widget inputFormularioGrande' id='input-query-rn-" + item.id  + "' value='" + valor + "'>";
					 
					$("#tabla-queries-" + idTablaRN).append("<tr>"
									+ "<td class='label obligatorio'>Query " + reglaNegocio + "</td>"
									+ "<td>" + inputQuery + "</td>"
									+ "<td class='hide'>" + item.id + "</td>"
									+ "<td class='hide'>" + idQuery + "</td>"
									+ "<td class='hide'>" + item.elementoDestino.clave + "</td>"
									+ "<td class='hide'>" + item.elementoDestino.numero + "</td>"
									+ "<td class='hide'>" + item.elementoDestino.nombre + "</td>"
									+ "<td class='hide'>" + item.elementoDestino.id + "</td>"
									+ "<td class='hide'>" + paso.realizaActor + "</td>"
									+ "<td class='hide'>" + nombreVerbo + "</td>"
									+ "<td class='hide'>" + paso.otroVerbo + "</td>"
									+ "<td class='hide'>" + paso.redaccion + "</td>"									
								+"</tr>"); 
					
		});
		
	} else {
		$("#formularioReglasNegocio").hide();
	}
}


function agregarCamposParametrosSeccion() {
	var json = document.getElementById("jsonReferenciasParametrosMensaje").value;
	if (json !== "" && json !== "[]") {
		var parsedJson = JSON.parse(json);
		
		$.each(
				parsedJson,
				function(i, item) {
					var paso = item.paso;
					var realiza = paso.realizaActor;
					var realizaImg = "";
					if(realiza == "Actor" || realiza == true) {
						var realizaActor = true;
						realizaImg = "<img src='" + window.contextPath + 
						"/resources/images/icons/actor.png' title='Actor' style='vertical-align: middle;'/>";
					} else if(realiza == "Sistema" || realiza == false) {
						realizaActor = false;
						realizaImg = "<img src='" + window.contextPath + 
						"/resources/images/icons/uc.png' title='Sistema' style='vertical-align: middle;'/>";
					}
					
					var nombreVerbo = paso.verbo.nombre;
					var otroVerbo = paso.otroVerbo;
					var verboAux = "";
					if (nombreVerbo == 'Otro') {
						verboAux = otroVerbo;
					} else {
						verboAux = nombreVerbo;
					}
					
					idTablaMensaje =  item.elementoDestino.id;
					tablaMensaje = $("#tabla-parametros-" + idTablaMensaje);
					if (tablaMensaje.size() == 0) {
						$("#seccionParametros").append("<div class='subtituloFormulario'>Mensaje " + item.elementoDestino.clave + item.elementoDestino.numero + " " + item.elementoDestino.nombre +"</div>");
						$("#seccionParametros").append("<div class='textoAyuda'>Redacción: " + item.elementoDestino.redaccion +"</div>");
						$("#seccionParametros").append("<div class='textoAyuda'>Paso: " + realizaImg + " " + verboAux + " " +  item.paso.redaccion +"</div>");
						$("#seccionParametros").append("<table class='seccion' id='tabla-parametros-" + idTablaMensaje + "'> <!--  --> </table>");
						$("#seccionParametros").append("</br>");
						
					}
					
					
					$.each(item.elementoDestino.parametros, function(j, mensajeParametro) {
						
						var valor = "";
						var idValor = 0;
						if(item.valoresMensajeParametro != null) {
							var valorMensajeParametro;
							$.each(item.valoresMensajeParametro, function(i, valorMensajeParametro){
								if(valorMensajeParametro.mensajeParametro.id == mensajeParametro.id) {
									valor = valorMensajeParametro.valor; 
									idValor = valorMensajeParametro.id;
								}
							});
						}
						parametro = mensajeParametro.parametro;
						inputParametro = "<input type='text' class='inputFormulario ui-widget' id='input-parametro-mensaje-" + parametro.id  + "' value='" + nullToEmpty(valor) + "'>";

						label = parametro.nombre;
						$("#tabla-parametros-" + idTablaMensaje).append("<tr>"
								+ "<td class='label'>" + parametro.nombre + "</td>"
								+ "<td>" + inputParametro + "</td>"
								+ "<td class='hide'>" + item.id + "</td>"
								+ "<td class='hide'>" + item.elementoDestino.clave + "</td>"
								+ "<td class='hide'>" + item.elementoDestino.numero + "</td>"
								+ "<td class='hide'>" + item.elementoDestino.nombre + "</td>"
								+ "<td class='hide'>" + item.elementoDestino.id + "</td>"
								+ "<td class='hide'>" + mensajeParametro.id + "</td>"
								+ "<td class='hide'>" + parametro.id + "</td>"
								+ "<td class='hide'>" + parametro.nombre + "</td>"
								+ "<td class='hide'>" + idValor + "</td>"
								+ "<td class='hide'>" + paso.realizaActor + "</td>"
								+ "<td class='hide'>" + nombreVerbo + "</td>"
								+ "<td class='hide'>" + paso.otroVerbo + "</td>"
								+ "<td class='hide'>" + paso.redaccion + "</td>"
								+ "<td class='hide'>" + item.elementoDestino.redaccion + "</td>"
							+"</tr>");
					});
					

		});
		
	} else {
		$("#formularioParametros").hide();
	}
}

function agregarCamposPantallasSeccion() {
	var json = document.getElementById("jsonPantallas").value;
	if (json !== "" && json !== "[]") {
		var parsedJson = JSON.parse(json);
		
		$.each(
				parsedJson,
				function(i, item) {
					if(item.valoresPantallaTrayectoria != null && item.valoresPantallaTrayectoria[0]!=null) {
						patron = item.valoresPantallaTrayectoria[0].patron;
						clave = item.valoresPantallaTrayectoria[0].id;
						inputPatron = "<textarea class='ui-widget inputFormularioGrande' id='input-patron-pantalla-" + item.id  + "' >" + patron + "</textarea>";
						anexo = "<td class='hide'>" + clave + "</td>";
					}else{
						inputPatron = "<textarea class='ui-widget inputFormularioGrande' id='input-patron-pantalla-" + item.id  + "' >" + item.patron + "</textarea>";
						anexo = "<td class='hide'>" + item.clave + "</td>";
					} 
				   
					
					label = item.clave + item.numero + " " + item.nombre;
					 
					$("#tablaPantallas").append("<tr>"
									+ "<td class='label obligatorio'>" + label + "</td>"
									+ "<td>" + inputPatron + "</td>"
									+ "<td class='hide'>" + item.id + "</td>"
									+ anexo
									+ "<td class='hide'>" + item.numero + "</td>"
									+ "<td class='hide'>" + item.nombre + "</td>"
								+"</tr>");
				
					
					
	});
		
	} else {
		$("#formularioPantallas").hide();
	}
}

function prepararEnvio() {
	try {
		
		tablaEntradasToJson();
		tablaAccionesToJson();
		tablaReglasNegocioToJson();
		tablaReferenciasParametrosMensajeToJson();
		tablaPantallasToJson();
		return true;
	} catch (err) {
		alert("Ocurrió un error: " + err);
		return false;
	}
}

/*function camposDesconocidos(){
	var desconocido = document.getElementById("entrada");
	var arregloDesconocido = [];
	var nRegistros = desconocido.length;
	alert(nRegistros);
	for (var i = 1; i < nRegistros; i++) {
		arregloDesconocido
	}
	//var valorDesconocido = desconocido.value;
}*/
function tablaEntradasToJson() {
	var tabla = document.getElementById("tablaEntradas");
	
	var arregloEntradas = [];

	var nRegistros = tabla.rows.length;
	
	for (var i = 1; i < nRegistros; i++) {
		
	    var etiqueta = tabla.rows[i].cells[1].childNodes[0].value;
	    var valor = tabla.rows[i].cells[2].childNodes[0].value;
	    var id = tabla.rows[i].cells[3].innerHTML;
	    var idValor = tabla.rows[i].cells[4].innerHTML;
	    var nombreHTML = tabla.rows[i].cells[5].innerHTML;
		var nombreAtributo = tabla.rows[i].cells[6].innerHTML;
		var nombreTermino = tabla.rows[i].cells[7].innerHTML;
		
		var atributo = null;
		var termino = null;
		
		if(nombreAtributo != "null") {
			atributo = new Atributo(nombreAtributo);
		}
		
		if(nombreTermino != "null") {
			termino = new TerminoGlosario(nombreTermino);
		}
	    
	    var valoresEntrada = [];
	    valoresEntrada.push(new ValorEntrada(valor, true, idValor)); 
	    arregloEntradas.push(new Entrada(id, etiqueta, valoresEntrada, atributo, termino));
	}

	
	var jsonEntradas = JSON.stringify(arregloEntradas);
	document.getElementById("jsonEntradas").value = jsonEntradas;
}

function tablaAccionesToJson() {
	var tablas = $("[id^='tabla-acciones-']");
		
	
	
	var arregloAcciones = [];
	var arregloImagenesPantalla = [];
	
	$.each( tablas, function(i, tabla) {

		var nRegistros = tabla.rows.length;
		
		for (var i = 1; i < nRegistros; i++) {
			
		    var url = tabla.rows[i].cells[1].childNodes[0].value;
		    
		    var id = tabla.rows[i].cells[3].innerHTML;
		    var metodo = document.getElementById("input-metodo-accion-" + id).value;
		    var nombreAccion = tabla.rows[i].cells[4].innerHTML;
		    var clavePantallaDestino = tabla.rows[i].cells[5].innerHTML;
		    var numeroPantallaDestino = tabla.rows[i].cells[6].innerHTML;
		    var nombrePantallaDestino = tabla.rows[i].cells[7].innerHTML;
		    
		    var idPantalla = tabla.rows[i].cells[8].innerHTML;
		    var clavePantalla = tabla.rows[i].cells[9].innerHTML;
		    var numeroPantalla = tabla.rows[i].cells[10].innerHTML;
		    var nombrePantalla = tabla.rows[i].cells[11].innerHTML;
		    var imagenPantalla = document.getElementById("img-pantalla-" + idPantalla).value;
		    
		    var nombreTipoAccion = tabla.rows[i].cells[12].innerHTML;
		    
		    var tipoAccion = new TipoAccion(null, nombreTipoAccion);
		    
		    var pantalla = new Pantalla(null, numeroPantalla, nombrePantalla, clavePantalla, idPantalla);
		    arregloImagenesPantalla.push(imagenPantalla);
		    
		    var pantallaDestino;
		    if(clavePantallaDestino != "") {
		    	pantallaDestino = new Pantalla(null, numeroPantallaDestino, nombrePantallaDestino, clavePantallaDestino);
		    } else {
		    	pantallaDestino = null;
		    }
		    arregloAcciones.push(new Accion(nombreAccion, null, null, tipoAccion, pantallaDestino, id, url, metodo, pantalla));
		    
		}
	});
	

	
	var jsonAcciones = JSON.stringify(arregloAcciones);
	document.getElementById("jsonAcciones").value = jsonAcciones;
	
	var jsonImgAcciones = JSON.stringify(arregloImagenesPantalla);
	document.getElementById("jsonImagenesPantallasAcciones").value = jsonImgAcciones;
}

function tablaReglasNegocioToJson() {
	var tablas = $("[id^='tabla-queries-']");
	var arregloReferenciasReglasNegocio = [];
	$.each( tablas, function(i, tabla) {		
	
		var nRegistros = tabla.rows.length;
		
		for (var i = 0; i < nRegistros; i++) {
			
		    var query = tabla.rows[i].cells[1].childNodes[0].value;
		    var idReferenciaParametro = tabla.rows[i].cells[2].innerHTML;
		    var idQuery = tabla.rows[i].cells[3].innerHTML;
		    var claveRN = tabla.rows[i].cells[4].innerHTML;
		    var numeroRN = tabla.rows[i].cells[5].innerHTML;
		    var nombreRN = tabla.rows[i].cells[6].innerHTML;
		    var idRN = tabla.rows[i].cells[7].innerHTML;
		    var realizaActor = tabla.rows[i].cells[8].innerHTML; 
		    var nombreVerbo = tabla.rows[i].cells[9].innerHTML;
		    var otroVerbo = tabla.rows[i].cells[10].innerHTML;
		    var redaccion = tabla.rows[i].cells[11].innerHTML;
		     
		    var paso = new Paso(null, realizaActor, nombreVerbo, otroVerbo, redaccion);
		    
		    var queries = [];
		    queries.push(new Query(idQuery, query, new ReferenciaParametro(idReferenciaParametro)));
		    var elementoDestino = new ReglaNegocio(numeroRN, nombreRN, claveRN, idRN);
		    elementoDestino.type = "reglaNegocio";
		    arregloReferenciasReglasNegocio.push(new ReferenciaParametro(idReferenciaParametro, queries, null, elementoDestino, paso));
		}
	});
	var jsonReferenciasReglasNegocio = JSON.stringify(arregloReferenciasReglasNegocio);
	document.getElementById("jsonReferenciasReglasNegocio").value = jsonReferenciasReglasNegocio;
}

function tablaReferenciasParametrosMensajeToJson() {
	var tablas = $("[id^='tabla-parametros-']");
		
	
	
	var arregloReferenciasMensajes = [];
	
	var mensaje;
	$.each( tablas, function(indice, tabla) {
		var nRegistros = tabla.rows.length;
		var arregloMensajeParametro = [];
		var arregloValoresMensajeParametro = [];
		var mensajeParametros = [];
		var paso;
		for (var i = 0; i < nRegistros; i++) {
			
			var valor = tabla.rows[i].cells[1].childNodes[0].value;
		    var idReferenciaParametro = tabla.rows[i].cells[2].innerHTML;
		    var claveMSG = tabla.rows[i].cells[3].innerHTML;
		    var numeroMSG = tabla.rows[i].cells[4].innerHTML;
		    var nombreMSG = tabla.rows[i].cells[5].innerHTML;
		    var idMSG = tabla.rows[i].cells[6].innerHTML;
		    var idMensajeParametro = tabla.rows[i].cells[7].innerHTML;
		    var idParametro = tabla.rows[i].cells[8].innerHTML;
		    var nombreParametro = tabla.rows[i].cells[9].innerHTML;
		    var idValor = tabla.rows[i].cells[10].innerHTML;
		    
		    var realizaActor = tabla.rows[i].cells[11].innerHTML; 
		    var nombreVerbo = tabla.rows[i].cells[12].innerHTML;
		    var otroVerbo = tabla.rows[i].cells[13].innerHTML;
		    var redaccion = tabla.rows[i].cells[14].innerHTML;
		    
		    var redaccionMSG = tabla.rows[i].cells[15].innerHTML;
		    
		    paso = new Paso(null, realizaActor, nombreVerbo, otroVerbo, redaccion);
		    
		    arregloValoresMensajeParametro.push(new ValorMensajeParametro(idValor, valor, idMensajeParametro));
		    parametro = new Parametro(nombreParametro, null, idParametro);

		    mensajeParametros.push(new MensajeParametro(idMensajeParametro, parametro));
		    mensaje = new Mensaje(numeroMSG, nombreMSG, claveMSG, idMSG, redaccionMSG);
		    mensaje.type = "mensaje";
		    mensaje.parametros = mensajeParametros;
		    arregloMensajeParametro.push(new MensajeParametro(idMensajeParametro, parametro));
		}
		arregloReferenciasMensajes.push(new ReferenciaParametro(idReferenciaParametro, null, arregloValoresMensajeParametro, mensaje, paso));
	});
	

	
	var jsonReferenciasParametrosMensaje = JSON.stringify(arregloReferenciasMensajes);
	document.getElementById("jsonReferenciasParametrosMensaje").value = jsonReferenciasParametrosMensaje;
}

function tablaPantallasToJson() {
	var tabla = document.getElementById("tablaPantallas");
	
	var arregloPantallas = [];

	var nRegistros = tabla.rows.length;
	
	for (var i = 0; i < nRegistros; i++) {
	    var patron = tabla.rows[i].cells[1].childNodes[0].value;
	    var id = tabla.rows[i].cells[2].innerHTML;
	    var clave = tabla.rows[i].cells[3].innerHTML;
	    var numero = tabla.rows[i].cells[4].innerHTML;
	    var nombre = tabla.rows[i].cells[5].innerHTML;
 
	    arregloPantallas.push(new Pantalla(null, numero, nombre, clave, id, patron));
	}

	
	var jsonPantallas = JSON.stringify(arregloPantallas);
	document.getElementById("jsonPantallas").value = jsonPantallas;
}

function quitarEspacios(cadenaConEsp) {
	return cadenaConEsp.replace(/\s/g, "_");
}

function agregarEspacios(cadenaSinEsp) {
	return cadenaSinEsp.replace(/_/g, " ");
}

function nullToEmpty (cadena) {
	if(cadena == null) {
		return "";
	} else {
		return cadena;
	}
}

function aceptar() {
	$('form').get(0).setAttribute("action", contextPath + "/configuracion-caso-uso!configurar");
	if(prepararEnvio()) {
		$('form').get(0).submit();		
		
	}
}

function guardarSalir() {
	$('form').get(0).setAttribute("action", contextPath + "/configuracion-caso-uso!guardar");
	if(prepararEnvio()) {
		$('form').get(0).submit();
	}
}

function guardarSalirTrayectoria(){

	$('form').get(0).setAttribute("action", contextPath + "/configuracion-trayectoria!guardar");

	prepararEnvio(); 

	$('form').get(0).submit();


	}

function mostrarPantalla(imagen) {
	if(imagen != null && imagen != "" && imagen != "null") {
		document.getElementById("pantalla").src = imagen;
	} else {
		document.getElementById("pantalla").src = contextPath + "/resources/images/icons/sinImagen.png";
	}
	
	
	$('#pantallaDialog').dialog('open');
	$('#pantallaDialog').focus();
}

function cerrarPantalla(imagen) {
	document.getElementById("pantalla").src = "";
	$('#pantallaDialog').dialog('close');
} 