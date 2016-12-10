var contextPath = "prisma";
$(document).ready(function() {
	contextPath = $("#rutaContexto").val();
	verificarObservaciones('resumen');
	verificarObservaciones('trayectoria');
	verificarObservaciones('puntosExt');
	
} );

function verificarObservaciones(seccion) {
	if (seccion == "resumen") {
		if (document.getElementById("esCorrectoResumen2").checked == true) {
			document.getElementById("divObservacionesResumen").style.display = '';
		} else {
			document.getElementById("divObservacionesResumen").style.display = 'none';
		}
		
	}
	
	if (seccion == "trayectoria") {
		if (document.getElementById("esCorrectoTrayectoria2").checked == true) {
			document.getElementById("divObservacionesTrayectoria").style.display = '';
		} else {
			document.getElementById("divObservacionesTrayectoria").style.display = 'none';
		}
	}
	
	if (seccion == "puntosExt") {
		if(document.getElementById("esCorrectoPuntosExt2")) {
			if (document.getElementById("esCorrectoPuntosExt2").checked == true) {
				document.getElementById("divObservacionesPuntosExt").style.display = '';
			} else {
				document.getElementById("divObservacionesPuntosExt").style.display = 'none';
			}
		}
		
	}
}

