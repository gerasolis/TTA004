package mx.prisma.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.mifmif.common.regex.Generex;

public class GeneradorCadenasUtil {
	public static String generarCadena(String expReg) {
		Generex generex = new Generex(expReg);
		String cadena = generex.random();
		return cadena;
	}
	
	public static String generarCadenaInvalida(Integer longitud, boolean numero) {
		if(longitud == null) {
			longitud = 100;
		} 
		longitud++; 
		String caracter;
		if(numero) {
			caracter = "1";
		} else {
			caracter = "a";
		}
		String expReg = "(" + caracter + "){" + longitud + "}";
		Generex generex = new Generex(expReg);
		String cadena = generex.random();
		return cadena;
	}
	
	public static String generarEnteroAleatorio(int limite) {
		int numero = PRISMARandomUtil.generarRandomEntero(1, limite);
		return numero + "";
	}
	
	public static String generarCadenaAleatoria(int longitud, boolean obligatorio) {
		String expReg = "[a-zA-Z]";
		if(obligatorio) {
			expReg = expReg.concat("+");
		} else {
			expReg = expReg.concat("*");
		}
		Generex generex = new Generex(expReg);
		String cadena = generex.random();
		if(cadena.length() > longitud) {
			cadena = cadena.substring(0, longitud);
		}
		return cadena;
	}

	public static String generarFechaAleatoria() {
		int anio = PRISMARandomUtil.generarRandomEntero(1990, 2020);
		int mes = PRISMARandomUtil.generarRandomEntero(1, 12);
		
		Calendar c = new GregorianCalendar(anio, mes, 1);
		int maxDias = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int dia = PRISMARandomUtil.generarRandomEntero(1, maxDias);
		
		String diaCadena = dia + "";
		String mesCadena = mes + "";
		
		if(diaCadena.length() == 1) {
			diaCadena = "0" + dia;
		}
		
		if(mesCadena.length() == 1) {
			mesCadena = "0" + mes;
		}
		
		String fecha = diaCadena + "/" + mesCadena + "/" + anio;
		return fecha;
	}

	public static String generarFlotanteAleatorio(Integer limite) {
		float numero = PRISMARandomUtil.generarRandomFlotante(1, limite);
		return numero + "";
	}
}
