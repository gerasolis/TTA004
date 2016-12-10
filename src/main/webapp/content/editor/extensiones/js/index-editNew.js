var contextPath = "prisma";

$(document).ready(function() {
	window.scrollTo(0,0);
	
	try {
		token.cargarListasToken();
	} catch (err) {
		alert("No existen elementos para referenciar con el token.");
	}
	
} );
