package mx.prisma.bs;

public class EstadoProyectoEnum {
	public enum EstadoProyecto {
		NEGOCIACION, INICIADO, TERMINADO
	}
	public static int consultarIdEstadoProyecto(EstadoProyectoEnum.EstadoProyecto estado) {
		switch(estado) {
		case NEGOCIACION:
			return 1;
		case INICIADO:
			return 2;
		case TERMINADO:
			return 3;
		default:
			return -1;
		
		}
	}
}
