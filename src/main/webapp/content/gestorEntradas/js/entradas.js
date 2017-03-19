var contextPath = "prisma";
$(document)
.ready(
		function() {
			window.scrollTo(0, 0);
			contextPath = $("#rutaContexto").val();
			$('table.tablaGestion').DataTable();					
			var json = $("#jsonColaboradoresTabla").val();
			if (json !== "") {
				var parsedJson = JSON.parse(json);
				$.each(parsedJson, function(i, item) {
					var curp = item.curp;
					document.getElementById("checkbox-" + curp).checked = true;
				});
			}
		});

function agregarMensaje(mensaje) {
	alert(mensaje);
};

function prepararEnvio() {
	try {
		tablaToJson("tablaColaboradores");
		return true;
	} catch (err) {
		alert("Ha ocurrido un error." + err);
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloColaboradores = [];
	var seleccionado;

	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {
		var curp = table.fnGetData(i, 1);
		seleccionado = document.getElementById("checkbox-" + curp).checked;
		if (seleccionado == true) {
			arregloColaboradores.push(new Colaborador(curp));
		}
	}
	var jsonColaboradores = JSON.stringify(arregloColaboradores);
	document.getElementById("jsonColaboradoresTabla").value = jsonColaboradores;
}
