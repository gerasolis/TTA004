var token = function() {
	var at_configPARAM;
	cargarListasToken = function() {
		// Parámetros
		json = $("#jsonParametrosGuardados").val();
		if (json !== "" && json != null) {
			var listaElementos = JSON.parse(json);
			at_configPARAM = cargaLista("PARAM", listaElementos);
		}

		/*
		 * Se configuran los tokens que estará disponibles en los textAreas.
		 */
		// textArea de Redacción
		$inputor = $('#inputor').atwho(at_configPARAM);
		$inputor.caret('pos', 60);
		$inputor.focus().atwho('run');
		
	}

	function cargaLista(token, listaObjetos) {
		var separador1 = "·";
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
		return at_config;
	}

	function remplazarEspaciosGuion(cadenaConEsp) {
		cadenaSinEsp = cadenaConEsp.replace(/\s/g, "_");
		return cadenaSinEsp;
	}

	return {
		cargarListasToken : cargarListasToken,
	};
}();
