/**
 * 
 */
var contextPath = "prisma";
$(document)
.ready(
		function() {
			window.scrollTo(0, 0);
			contextPath = $("#rutaContexto").val();
			$('table.tablaGestion').DataTable();					
			var json = $("#jsonEntradasTabla").val();
			if (json !== "") {
				var parsedJson = JSON.parse(json);
				$.each(parsedJson, function(i, item) {
					var id = item.id;
					//document.getElementById("checkbox-" + id).checked = true;
				});
			}

			$("input[type=file]").on('change',function(){
				var id = $(this).attr('id')
				guardarCadena(id);
			});
		});



function agregarMensaje(mensaje) {
	alert(mensaje);
};

function prepararEnvio() {
	try {
		tablaToJson("gestion");
		return true;
	} catch (err) {
		alert("Ha ocurrido un error." + err);
	}
}

function tablaToJson(idTable) {
	var table = $("#" + idTable).dataTable();
	var arregloColaboradores = [];
	var seleccionado;
	var cadena="";
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {	    
		var entrada = table.fnGetData(i, 1);
		var id = entrada.split(" ");
		//seleccionado = document.getElementById("checkbox-" + id[0]).checked;
		//seleccionado2 = document.getElementById("checkbox2-" + id[0]).checked;
		seleccionado = $('input[id=checkbox-'+id[0]+']').filter(':checked').val();
		seleccionado2 = $('input[id=checkbox2-'+id[0]+']').filter(':checked').val();
		aleatorioCorrectoPrueba = $('input[id=aleatorioCorrectoPrueba-'+id[0]+']').filter(':checked').val();
		aleatorioCorrectoGuion = $('input[id=aleatorioCorrectoGuion-'+id[0]+']').filter(':checked').val();
		
		if (seleccionado != null && seleccionado2 == null ) {
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó prueba");
		    arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,1,0,0,0));
		}else if(seleccionado != null && seleccionado2 != null){	
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó ambas");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,1,1,0,0));
		}else if(seleccionado == null && seleccionado2 != null){
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,1,0,0));
		}
		if(aleatorioCorrectoPrueba != null && aleatorioCorrectoGuion == null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,1,0));
		}else if(aleatorioCorrectoPrueba == null && aleatorioCorrectoGuion != null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,0,1));
		}else if(aleatorioCorrectoPrueba != null && aleatorioCorrectoGuion != null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,1,1));
		}
	}
	var jsonColaboradores = JSON.stringify(arregloColaboradores);
	document.getElementById("jsonEntradasTabla").value = jsonColaboradores;
}

function guardarCadena(id){
	var fileInput = $('#'+id);
	var input = fileInput.get(0);
	var reader = new FileReader();
    if (input.files.length) {
        var textFile = input.files[0];
        reader.readAsText(textFile);
        
        reader.onload = function (evt) {
        	cadena = evt.target.result;
        	var palabrita = id.split("-");
            $('#palabras-'+palabrita[1]).attr('value',cadena);
            $('#palabras-'+palabrita[1]).attr('value',cadena);     
        }
    } 
}

function subirArchivo(id){
   $("#" + id).click();
    guardarCadena(id);
}

