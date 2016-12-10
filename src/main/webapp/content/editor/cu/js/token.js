var token = function() {

	/* Tipos de lista
	 * 		Tipo A: TK.nombre
	 * 				Actores
	 * 				Entidades
	 * 				Términos del glosario
	 * 		Tipo B: TK.num:nombre
	 * 				Reglas de negocio
	 * 				Mensajes
	 * 		Tipo C: TK.entidad:nombre
	 * 				Atributo
	 * 		Tipo D: TK.modulo.numero:nombre
	 * 				Casos de uso
	 * 				Pantallas
	 * 		Tipo E: TK.CUMODULO.numero:clave
	 * 				Trayectorias
	 * 		Tipo F: TK.CUMODULO:claveTrayectoria.numero
	 * 				Pasos
	 * 		Tipo G: TK.IUM.numero:nombrePantalla:nombreAccion
	 * 				Ejemplo: ACC.IUSF.7:Registrar_incendio:Aceptar
	 * 				Acciones
	 */
	var at_configRN;
	var at_configENT;
	var at_configCU;
	var at_configIU;
	var at_configMSG;
	var at_configACT;
	var at_configGLS;
	var at_configATR;
	var at_configP;
	var at_configTRAY;
	var at_configACC;

	cargarListasToken = function() {

		// Actores
		var json = $("#jsonActores").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configACT = cargaLista("A", "ACT", listaElementos);
		}

		// Entidades
		json = $("#jsonEntidades").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configENT = cargaLista("A", "ENT", listaElementos);
		}

		// Términos del glosario
		json = $("#jsonTerminosGls").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configGLS = cargaLista("A", "GLS", listaElementos);
		}

		// Mensajes
		json = $("#jsonMensajes").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configMSG = cargaLista("B", "MSG", listaElementos);
		}

		// Reglas de negocio
		json = $("#jsonReglasNegocio").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configRN = cargaLista("B", "RN", listaElementos);
		}

		// Atributos
		json = $("#jsonAtributos").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configATR = cargaLista("C", "ATR", listaElementos);
		}

		// Casos de uso
		json = $("#jsonCasosUsoProyecto").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configCU = cargaLista("D", "CU", listaElementos);
		}

		// Pantallas
		json = $("#jsonPantallas").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configIU = cargaLista("D", "IU", listaElementos);
		}

		// Trayectorias
		json = $("#jsonTrayectorias").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configTRAY = cargaLista("E", "TRAY", listaElementos);
		}

		// Pasos
		json = $("#jsonPasos").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configP = cargaLista("F", "P", listaElementos);
		}

		// Acciones
		json = $("#jsonAcciones").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configACC = cargaLista("G", "ACC", listaElementos);
		}

		/*
		 * Se configuran los tokens que estará disponibles en los textAreas.
		 */
		// textArea de Actores
		$inputor = $('#actorInput').atwho(at_configACT);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		
		// textArea de Entradas
		$inputor = $('#entradaInput').atwho(at_configATR).atwho(at_configGLS);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		// textArea de Salidas
		$inputor = $('#salidaInput').atwho(at_configATR).atwho(at_configMSG)
				.atwho(at_configGLS);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		// textArea de Reglas de negocio
		$inputor = $('#inputor').atwho(at_configRN);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		// textArea de Precondiciones
		$inputor = $('#precondicionInput').atwho(at_configRN).atwho(
				at_configENT).atwho(at_configMSG).atwho(at_configACT).atwho(
				at_configATR).atwho(at_configGLS).atwho(at_configCU).atwho(
				at_configIU).atwho(at_configTRAY).atwho(at_configP).atwho(
				at_configACC);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		// textArea de Postcondiciones
		$inputor = $('#postcondicionInput').atwho(at_configRN).atwho(
				at_configENT).atwho(at_configMSG).atwho(at_configACT).atwho(
				at_configATR).atwho(at_configGLS).atwho(at_configCU).atwho(
				at_configIU).atwho(at_configTRAY).atwho(at_configP).atwho(
				at_configACC);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		// textArea de región de la trayectoria
		$inputor = $('#ptosExtensionInput').atwho(at_configP);

	}

	function cargaLista(tipo, token, listaObjetos) {
		var separador1 = "·";
		var separador2 = ":";
		if (tipo == "A") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"nombre" : value.nombre,
					'name' : remplazarEspaciosGuion(value.nombre)
				};
			});
			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>"
						+ "<span class=\"listaElementoInteres\">${nombre}</span></li>",
				limit : 200
			}
		} else if (tipo == "B") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"numero" : value.numero,
					"nombre" : value.nombre,
					'name' : value.numero + separador2 + ""
							+ remplazarEspaciosGuion(value.nombre)
				};
			});

			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>"
						+ "<span class=\"listaElementoInteres\">${numero}</span><span class=\"listaToken\">"  
						+ separador2 + " </span><span class=\"listaElementoInteres\">${nombre}</span></li>",
				limit : 200
			}
		} else if (tipo == "C") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"nombre" : value.nombre,//remplazarEspaciosGuionBlanco(value.nombre),
					"nombreEntidad" : value.entidad.nombre, //remplazarEspaciosGuionBlanco(value.entidad.nombre),
					'name' : remplazarEspaciosGuion(value.entidad.nombre) + separador2
							+ remplazarEspaciosGuion(value.nombre)
				};
			});
			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>"
						+ "<span class=\"listaNombre\">${nombreEntidad}"
						+ separador2 + "</span><span class=\"listaElementoInteres\">${nombre}</span></li>",
				limit : 200
			}
		} else if (tipo == "D") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"claveModulo" : value.modulo.clave,
					"numero" : value.numero,
					"nombre" : value.nombre,
					'name' : value.modulo.clave
					+ separador1 + value.numero + separador2
					+ remplazarEspaciosGuion(value.nombre)
				};
			});
			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>"
						+ "<span class=\"listaNombre\">${claveModulo}" + separador1
						+ "<span class=\"listaElementoInteres\">${numero}</span><span class=\"listaToken\">" 
						+ separador2 + "</span> </span><span class=\"listaElementoInteres\">${nombre}</span></li>",
				limit : 200
			}
		} else if (tipo == "E") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"claveCU" : value.casoUso.clave,
					"numeroCU" : value.casoUso.numero,
					"nombreCU" : value.casoUso.nombre,
					"clave" : value.clave,
					'name' : remplazarEspaciosGuion(value.casoUso.clave) 
					+ separador1 + value.casoUso.numero + separador2
					+ remplazarEspaciosGuion(value.casoUso.nombre)
					+ separador2 + remplazarEspaciosGuion(value.clave)
				};
				 // TRAY.CUMODULO.NUM:NOMBRECU:CLAVETRAY
			});
			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>"
						+ "<span class=\"listaNombre\">${claveCU}" + separador1 +"${numeroCU}"
						+ separador2 + " ${nombreCU}" + separador2 
						+ " </span><span class=\"listaElementoInteres\">${clave}</span></li>",
				limit : 200
			}
		} else if (tipo == "F") {
			var lista = $.map(listaObjetos, function(value, i) {
				var realiza = "";
				var verbo = "";
				if(value.realizaActor == true) {
		    		realiza = "El actor";
		    	} else if(value.realizaActor == false) {
		    		realiza = "El sistema";
		    	}
				if(value.verbo.nombre == 'Otro') {
					verbo = value.otroVerbo;
				} else {
					verbo = value.verbo.nombre;
				}
				return {
					'id' : i,
					"claveCU" : value.trayectoria.casoUso.clave,
					"numeroCU" : value.trayectoria.casoUso.numero,
					"claveTray": value.trayectoria.clave,
					"numero" : value.numero,
					"nombre" : value.trayectoria.casoUso.nombre,
					"redaccion" : realiza + " " + verbo.toLowerCase() + " " + value.redaccion,
					'name' : value.trayectoria.casoUso.clave
					+ separador1 + value.trayectoria.casoUso.numero + separador2
					+ remplazarEspaciosGuion(value.trayectoria.casoUso.nombre) + separador2 
					+ remplazarEspaciosGuion(value.trayectoria.clave) + separador1
					+ value.numero
				};
			});
			var at_config = {
					at : token + separador1,
					data : lista,
					displayTpl : "<li title='${redaccion}'><span class=\"listaToken\">" + token
					+ separador1 + "</span>" 
					+ "<span class=\"listaNombre\">${claveCU}" + separador1 + "${numeroCU}"
					+ separador2 + "${nombre}" + separador2 + "${claveTray}" 
					+ separador1 +"</span><span class=\"listaElementoInteres\">${numero}</span></li>",
					limit : 200
				}
		} else if (tipo == "G") {
			var lista = $.map(listaObjetos, function(value, i) {
				return {
					'id' : i,
					"claveIU" : value.pantalla.clave,
					"numeroIU" : value.pantalla.numero,
					"nombreIU": value.pantalla.nombre,
					"nombre" : value.nombre,
					'name' : remplazarEspaciosGuion(value.pantalla.clave) 
					+ separador1 + value.pantalla.numero
					+ separador2 + remplazarEspaciosGuion(value.pantalla.nombre) 
					+ separador2 + remplazarEspaciosGuion(value.nombre)
				};
			});
			var at_config = {
				at : token + separador1,
				data : lista,
				displayTpl : "<li><span class=\"listaToken\">" + token
						+ separador1 + "</span>" 
						+ "<span class=\"listaNombre\">${claveIU}" + separador1 + "${numeroIU}"
						+ separador2 + " ${nombreIU}" + separador2 
						+ " </span><span class=\"listaElementoInteres\">${nombre}</span></li>",
				limit : 200
			}
		}


		return at_config;
	}

	function remplazarEspaciosGuion(cadenaConEsp) {
		cadenaSinEsp = cadenaConEsp.replace(/\s/g, "_");
		return cadenaSinEsp;
	}

	function remplazarEspaciosGuionBlanco(cadenaConEsp) {
		cadenaSinEsp = cadenaConEsp.replace(/\s/g,
				"<font color=\"white\">_</font>");
		return cadenaSinEsp;
	}

	return {
		cargarListasToken : cargarListasToken,
	};
}();
