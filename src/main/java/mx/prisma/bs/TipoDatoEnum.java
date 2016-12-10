package mx.prisma.bs;

public class TipoDatoEnum {
	private static String cadena = "Cadena";
	private static String flotante = "Flotante";
	private static String entero = "Entero";
	private static String booleano = "Booleano";
	private static String fecha = "Fecha";
	private static String archivo = "Archivo";
	private static String otro = "Otro";
	
	public enum tipoDato {
		CADENA, FLOTANTE, ENTERO, BOOLEANO, FECHA, ARCHIVO, OTRO
	}
	
	public static TipoDatoEnum.tipoDato getTipoDato(mx.prisma.editor.model.TipoDato td) {
		String nombreTipoDato = td.getNombre();
		if(nombreTipoDato.equals(cadena)) {
			return tipoDato.CADENA;
		}
		if(nombreTipoDato.equals(flotante)) {
			return tipoDato.FLOTANTE;
		}
		if(nombreTipoDato.equals(entero)) {
			return tipoDato.ENTERO;
		}
		if(nombreTipoDato.equals(booleano)) {
			return tipoDato.BOOLEANO;
		}
		if(nombreTipoDato.equals(fecha)) {
			return tipoDato.FECHA;
		}
		if(nombreTipoDato.equals(archivo)) {
			return tipoDato.ARCHIVO;
		}
		if(nombreTipoDato.equals(otro)) {
			return tipoDato.OTRO;
		}
		return null;
	}
	
	
}
