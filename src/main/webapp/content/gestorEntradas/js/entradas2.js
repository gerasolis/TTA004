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
				});
			}

			$("input[type=file]").on('change',function(){
				var id = $(this).attr('id');
				var uploadFiles = id.split("-");
				$("#upload-"+uploadFiles[1]).css("background","no-repeat url('/prisma/resources/images/icons/checked.png')");
				guardarCadena(id);
				
			});		
			
			$("#frmActor").submit(function (event) {
				var k=0;
			    var form = $(this);
			    event.preventDefault();
			    prepararEnvio(k,form);
			   
			    
			});

		});
	


function agregarMensaje(mensaje) {
	alert(mensaje);
};

function prepararEnvio(k,form) {
	try {
		
		tablaToJson("gestion",k,form);
		//return true;
	} catch (err) {
		alert("Ha ocurrido un error." + err);
	}
}





function tablaToJson(idTable,k,form) {
	var table = $("#" + idTable).dataTable();
	var arregloColaboradores = [];
	var arregloEntradas = [];
	var seleccionado;
	var cadena="";
	
	
	for (var i = 0; i < table.fnSettings().fnRecordsTotal(); i++) {	    
		var entrada = table.fnGetData(i, 1);
		var id = entrada.split(" ");
		$('#errorHTML-'+id[0]).empty();
		$('#errorEntrada-'+id[0]).empty();
	
		
		nombreHTML = $('input[id=nombreHTML-'+id[0]+']').val();
		
		if(nombreHTML==""){
			$('#errorHTML-'+id[0]).append('<p align="center">Complete el campo</p>');
			k++;
		}
		seleccionado = $('input[id=checkbox-'+id[0]+']').filter(':checked').val();
		seleccionado2 = $('input[id=checkbox2-'+id[0]+']').filter(':checked').val();
		aleatorioCorrectoPrueba = $('input[id=aleatorioCorrectoPrueba-'+id[0]+']').filter(':checked').val();
		aleatorioCorrectoGuion = $('input[id=aleatorioCorrectoGuion-'+id[0]+']').filter(':checked').val();

		if (seleccionado != null && seleccionado2 == null ) {
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó prueba");
		    arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,1,0,0,0));
		    //arregloEntradas.push(new Entrada(id[0],nombreHTML));
		}else if(seleccionado != null && seleccionado2 != null){	
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó ambas");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,1,1,0,0));
			//arregloEntradas.push(new Entrada(id[0],nombreHTML));
		}else if(seleccionado == null && seleccionado2 != null){
			var cadena = $('#palabras-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,1,0,0));
			//arregloEntradas.push(new Entrada(id[0],nombreHTML));
		}
		if(aleatorioCorrectoPrueba != null && aleatorioCorrectoGuion == null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,1,0));
			//arregloEntradas.push(new Entrada(id[0],nombreHTML));
		}else if(aleatorioCorrectoPrueba == null && aleatorioCorrectoGuion != null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,0,1));
			//arregloEntradas.push(new Entrada(id[0],nombreHTML,));
		}else if(aleatorioCorrectoPrueba != null && aleatorioCorrectoGuion != null){
			var cadena = $('#palabrasAleatorias-'+id[0]).val();
			console.log("se seleccionó guión");
			arregloColaboradores.push(new ValorEntrada(id[0],cadena,1,0,0,1,1));
			//arregloEntradas.push(new Entrada(id[0],nombreHTML));
		}
		else if(seleccionado == null && seleccionado2 == null && aleatorioCorrectoPrueba == null && aleatorioCorrectoGuion == null){
			$("#errorEntrada-"+id[0]).append('<p class="instrucciones">Inserte los valores de la entrada '+id[1]+'</p>');
			k++;
			//return false;
		}
		arregloEntradas.push(new Entrada(id[0],nombreHTML));

	}
	var jsonColaboradores = JSON.stringify(arregloColaboradores);
	var jsonEntradas2_1 = JSON.stringify(arregloEntradas);
	document.getElementById("jsonEntradasTabla").value = jsonColaboradores;
	document.getElementById("jsonEntradasTabla2_1").value = jsonEntradas2_1;
	 if(k==0){
	    	form[0].submit();
	    }

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

