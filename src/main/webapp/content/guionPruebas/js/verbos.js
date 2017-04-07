var contextPath = "prisma";
$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	verificarObservaciones('verbo');
	
} );

function verificarObservaciones(seccion) {
	if (seccion == "verbo") {
		if (document.getElementById("esCorrectoVerbo2").checked == true) {
			document.getElementById("divObservacionesVerbo").style.display = '';
		} else {
			document.getElementById("divObservacionesVerbo").style.display = 'none';
		}
		
	}
}

function verificarObservaciones(seccion) {
	if (seccion == "verbo") {
		if (document.getElementById("esCorrectoVerbo2").checked == true) {
			document.getElementById("divObservacionesVerbo").style.display = '';
		} else {
			document.getElementById("divObservacionesVerbo").style.display = 'none';
		}
		
	}
}
