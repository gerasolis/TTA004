var contextPath = "error";

$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	
	
	cargarImagenPantalla();
	
	var json = $("#jsonAccionesTabla").val();
	var jsonImg = $("#jsonImagenesAcciones").val(); 
	if (json !== "") {

		
		var parsedJson = JSON.parse(json);
		var parsedJsonImg = JSON.parse(jsonImg);
		if(parsedJson.length > 0) {
			document.getElementById("seccion-acciones").style.display = '';
			$
			.each(
					parsedJson,
					function(i, item) {
				    	if(parsedJsonImg[i] != null && parsedJsonImg[i] != "") {
				    		document.getElementById("accion" + item.id).src = parsedJsonImg[i];
				    		document.getElementById("marco-accion" + item.id).style.display = '';
				    	} 
				    		
						
						
					});
		}
		
		 
	}
});

function cargarImagenPantalla() {
	var imgPantalla = document.getElementById("pantallaB64").value;
	if(imgPantalla != "") {
		document.getElementById("pantalla").src = imgPantalla;
		document.getElementById("marco-pantalla").style.display = '';
	}
}
