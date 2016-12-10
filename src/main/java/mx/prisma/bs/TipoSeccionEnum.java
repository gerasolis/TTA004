package mx.prisma.bs;

public class TipoSeccionEnum {
	public enum TipoSeccionENUM {
		RESUMEN, TRAYECTORIA, PUNTOSEXTENSION
	}
	
	public static String getNombre(TipoSeccionENUM seccion) {
		switch(seccion) {
		case RESUMEN:
			return "General";
		case PUNTOSEXTENSION:
			return "Puntos de extensión";
		case TRAYECTORIA:
			return "Trayectorias";
		default:
			return null;		
		}
	}
}
