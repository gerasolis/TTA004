$(document).ready(function() {
	var cardinalidad = document.getElementById("cardinalidad");
	var cardinalidadTexto = cardinalidad.options[cardinalidad.selectedIndex].text;
	
	if (cardinalidadTexto == 'Otro' || cardinalidadTexto == 'Otra') {
		document.getElementById("otro").style.display = '';
	} else {
		document.getElementById("otro").style.display = 'none';
	}
});

function verificarOtro() {
	var cardinalidad = document.getElementById("cardinalidad");
	var cardinalidadTexto = cardinalidad.options[cardinalidad.selectedIndex].text;

	if (cardinalidadTexto == 'Otro' || cardinalidadTexto == 'Otra') {
		document.getElementById("otro").style.display = '';
	} else {
		document.getElementById("otro").style.display = 'none';
	}
}