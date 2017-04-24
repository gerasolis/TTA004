var contextPath = "prisma";
$(document)
.ready(
		function() {
			window.scrollTo(0, 0);
			contextPath = $("#rutaContexto").val();
			$('table.tablaGestion').DataTable();					
			var json = $("#jsonGuionesTabla").val();
			if (json !== "") {
				var parsedJson = JSON.parse(json);
				$.each(parsedJson, function(i, item) {
					var id = item.id;
				});
			}
		});

function agregarMensaje(mensaje) {
	alert(mensaje);
};

function prepararEnvio() {
	try {
		tablaToJson("tablaGuionesPrueba");
		return true;
	} catch (err) {
		alert("Ha ocurrido un error." + err);
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloGuiones = [];
	var seleccionado;

	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		console.log("Entra al for");
		var id = table.fnGetData(i, 1);
		console.log(id);
		seleccionado = document.getElementById("checkbox-" + id).checked;
		console.log(seleccionado);
		if (seleccionado == true) {
			arregloGuiones.push(new CasoUso(id));
		}
	}
	var jsonGuiones = JSON.stringify(arregloGuiones);
	document.getElementById("jsonGuionesTabla").value = jsonGuiones;
}
