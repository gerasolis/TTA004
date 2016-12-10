var contextPath = "prisma";

$(document)
		.ready(
				function() {
					contextPath = $("#rutaContexto").val();
					// Se oculta el botón de editar de la redacción
					document.getElementById("botonEditar").style.display = 'none';

					cargarParametros();
					try {
						token.cargarListasToken();
					} catch (err) {
						console.log("No se puede cargar el token.");
					}

					// Fin de la creación de la tabla de parámetros
				});

/*$(window).load(function(){
	var cambioRedaccion = document.getElementById("cambioRedaccion").value;
	if(cambioRedaccion == "true") {
		document.getElementById("cambioRedaccion").value = "false";
		alert("Escriba la descripción de cada parámetro.");
	}
})*/

function habilitarEdicionRedaccion() {
	document.getElementById("inputorreadOnly").readOnly = false;
	document.getElementById("inputorreadOnly").id = "inputor";
	document.getElementById("cambioRedaccion").value = true;
	seccionParametros.style.display = 'none';
	document.getElementById("botonEditar").style.display = 'none';
	token.cargarListasToken();
}

function mostrarCamposParametrosX() {
	
	
	
	var seccionParametros = document.getElementById("seccionParametros");
	var parametrizado = document.getElementById("idParametrizado");
	var form = document.getElementById("frmParametros");

	// Se indica que la redacción ha cambiado
	document.getElementById("cambioRedaccion").value = true;
	// PENDIENTE verificar si contiene "PARAM." para no enviar la peticion
	// siempre
	form.submit();
}

function prepararEnvio() {
	try {
		tablaToJson("parametros");
		//$('#mensajeComentarios').dialog('open');
		return true;
	} catch (err) {
		alert("Ha ocurrido un error.");
		console.log("Ocurrió un error: " + err);
		return false;
		
	}
}

function verificarEsParametrizado() {
	var redaccion = document.getElementById("inputor").value;
	if(/PARAM·[a-zA-Z0-9]+(\s|\.\s|,\s|$|\.$)/m.test(redaccion)) {
		verificarParametros();
	} else {
		limpiarParametros();
	}
	
}

function verificarParametros() {
	rutaVerificarParametros = contextPath + '/mensajes!verificarParametros';
	var redaccion = document.getElementById("inputor").value;
	$.ajax({
		dataType : 'json',
		url : rutaVerificarParametros,
		type: "POST",
		data : {
			redaccionMensaje : redaccion
		},
		success : function(data) {
			mostrarCamposParametros(data);
			
		},
		error : function(err) {
			alert("Ha ocurrido un error.");
			console.log("AJAX error in request: " + JSON.stringify(err, null, 2));
		}
	});
}



function tablaToJson(idTable) {
	var tabla;
	try {
		tabla = document.getElementById("parametros");
		if(tabla) {
			var arregloParametros = [];
			var tam = tabla.rows.length;
			for (var i = 0; i < tam; i++) {
				var nombre = tabla.rows[i].cells[0].innerHTML;
				var descripcion = document.getElementById("idDescripcionParametro" + i).value;
				arregloParametros.push(new Parametro(nombre, descripcion));
			}

			var jsonParametros = JSON.stringify(arregloParametros);
			document.getElementById("jsonParametros").value = jsonParametros;
		}
	} catch (err) {
		console.log("Sin parámetros.");
	}	
}


function agregarFila(fila) {
	var tabla = document.getElementById("parametros");
	// Se obtiene el total de filas
	var totalFilas = tabla.rows.length;

	// Se crea un <tr> vacío y se agrega en la última posición
	var row = tabla.insertRow(totalFilas);

	// Se agregan las celdas vacías en la primer y última posición
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);

	// Add some text to the new cells:
	cell1.className = "label obligatorio";

	cell1.innerHTML = fila[0];
	cell2.innerHTML = fila[1];
}

function cerrarEmergente() {
	$('#mensajeConfirmacion').dialog('close');
}

function abrirEmergente() {
	$('#mensajeConfirmacion').dialog('open');
}

/*
 * Agrega un mensaje en la pantalla
 */
function agregarMensaje(mensaje) {
	alert(mensaje);
};

function enviarComentarios(){
	var redaccionDialogo = document.getElementById("comentarioDialogo").value;
	if(vaciaONula(redaccionDialogo)) {
		agregarMensaje("Agregue todos los campos obligatorios.");
		return false;
	}
	document.getElementById("comentario").value = redaccionDialogo;
	document.getElementById("frmMsj").submit();

	}
function cancelarRegistroComentarios() {
	document.getElementById("comentario").value = "";
	$('#mensajeComentarios').dialog('close');
}

function mostrarCamposParametros(json) {
	limpiarParametros();
	if(json != null && json.length > 0) {
		
		$
				.each(
						json,
						function(i, item) {
							var parametro = [
									item.nombre,
									"<textarea rows='2' class='inputFormularioExtraGrande ui-widget' id='idDescripcionParametro"
											+ i
											+ "'"
											+ "maxlength='500'>"
											+ item.descripcion
											+ "</textarea> " ];
							agregarFila(parametro);
						});
		
		document.getElementById("seccionParametros").style.display = '';
	}
}

function limpiarParametros() {
	document.getElementById("parametros").innerHTML = "";
	document.getElementById("seccionParametros").style.display = 'none';
}

function cargarParametros() {
	// Se construye la tabla de los parámetros
	var json = $("#jsonParametros").val();
	if (json !== "" && json !== "[]") {
		var parsedJson = JSON.parse(json);

		$
				.each(
						parsedJson,
						function(i, item) {
							var parametro = [
									item.nombre,
									"<textarea rows='2' class='inputFormularioExtraGrande ui-widget' id='idDescripcionParametro"
											+ i
											+ "'"
											+ "maxlength='500'>"
											+ item.descripcion
											+ "</textarea> " ];
							agregarFila(parametro);
						});
		// Se hace visible la sección de parámetros
		document.getElementById("seccionParametros").style.display = '';
	} else {
		document.getElementById("seccionParametros").style.display = 'none';
	}
}