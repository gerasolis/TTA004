package mx.prisma.editor.bs;

import mx.prisma.bs.AnalisisEnum.CU_Actores;
import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.bs.AnalisisEnum.CU_Entidades;
import mx.prisma.bs.AnalisisEnum.CU_Glosario;
import mx.prisma.bs.AnalisisEnum.CU_Mensajes;
import mx.prisma.bs.AnalisisEnum.CU_Pantallas;
import mx.prisma.bs.AnalisisEnum.CU_ReglasNegocio;
import mx.prisma.editor.dao.CasoUsoDAO;
import mx.prisma.editor.dao.ElementoDAO;
import mx.prisma.editor.dao.EstadoElementoDAO;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.EstadoElemento;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.util.PRISMAException;

public class ElementoBs {
	
	private final static int ID_EDICION = 1;
	private final static int ID_REVISION = 2;
	private final static int ID_PENDIENTECORRECCION = 3;
	private final static int ID_PORLIBERAR = 4;
	private final static int ID_LIBERADO = 5;
	private final static int ID_PRECONFIGURADO = 6;
	private final static int ID_CONFIGURADO = 7;
	
	public enum Estado {
	    EDICION, REVISION, PENDIENTECORRECCION, PORLIBERAR, LIBERADO, PRECONFIGURADO, CONFIGURADO
	}
	
	public static EstadoElemento consultarEstadoElemento(Estado estado) throws Exception{
		EstadoElemento estadoElemento = null;
		switch(estado) {
		case EDICION:
			 estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_EDICION);
			break;
		case LIBERADO:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_LIBERADO);
			break;
		case PENDIENTECORRECCION:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_PENDIENTECORRECCION);
			break;
		case PORLIBERAR:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_PORLIBERAR);
			break;
		case REVISION:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_REVISION);
			break;
		case PRECONFIGURADO:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_PRECONFIGURADO);
			break;
		case CONFIGURADO:
			estadoElemento = new EstadoElementoDAO().consultarEstadoElemento(ID_CONFIGURADO);
		default:
			break;
		}
		if(estadoElemento == null) {
			throw new PRISMAException("No se puede consultar el estado del elemento", "MSG13");
		}
		return estadoElemento;
	}
	
	public static int getIdEstado(Estado estado) {
		switch(estado) {
		case EDICION:
			return ID_EDICION;
		case LIBERADO:
			return ID_LIBERADO;
		case PENDIENTECORRECCION:
			return ID_PENDIENTECORRECCION;
		case PORLIBERAR:
			return ID_PORLIBERAR;
		case REVISION:
			return ID_REVISION;
		case PRECONFIGURADO:
			return ID_PRECONFIGURADO;
		case CONFIGURADO:
			return ID_CONFIGURADO;
		default:
			return 0;
		}
	}
	
	public static void verificarEstado(Elemento elemento, CU_CasosUso casoUsoAnalisis) {
		switch(casoUsoAnalisis) {
		case MODIFICARCASOUSO5_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PENDIENTECORRECCION)) {
					throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
			}
			
		case ELIMINARCASOUSO5_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PENDIENTECORRECCION)) {
				throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
		}			break;
		case REVISARCASOUSO5_5:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.REVISION)) {
				throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
		}
			break;
		case TERMINARCASOUSO5_6:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PENDIENTECORRECCION)) {
				throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
		}
			break;
		case LIBERARCASOUSO4_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PORLIBERAR) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.LIBERADO)
					&& elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.CONFIGURADO) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PRECONFIGURADO)) {
				throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
		}
			break;
		case CONFIGURARPRUEBA5_7:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.LIBERADO) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PRECONFIGURADO) && elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.CONFIGURADO)) {
				throw new PRISMAException("El estado del caso de uso es inválido.", "MSG13");
			}
			break;
		default:
			break;
		
		}
	}

	public static void verificarEstado(Elemento elemento,
			CU_ReglasNegocio reglaNegocioAnalisis) {
		switch(reglaNegocioAnalisis) {
		case MODIFICARREGLANEGOCIO8_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la regla de negocio es inválido.", "MSG13");
			}
			break;
		case ELIMINARREGLANEGOCIO8_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la regla de negocio es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}
	
	public static void verificarEstado(Elemento elemento,
			CU_Mensajes mensajeAnalisis) {
		switch(mensajeAnalisis) {
		case MODIFICARMENSAJE9_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la regla de negocio es inválido.", "MSG13");
			}
			break;
		case ELIMINARMENSAJE9_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la regla de negocio es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}
	
	public static void verificarEstado(Elemento elemento,
			CU_Pantallas pantallaAnalisis) {
		switch(pantallaAnalisis) {
		case MODIFICARPANTALLA6_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la pantalla es inválido.", "MSG13");
			}
			break;
		case ELIMINARPANTALLA6_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado de la pantalla es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}

	public static void verificarEstado(Actor elemento, CU_Actores actorAnalisis) {
		switch(actorAnalisis) {
		case MODIFICARACTOR7_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del actor es inválido.", "MSG13");
			}
			break;
		case ELIMINARACTOR7_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del actor es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}
	
	public static void verificarEstado(TerminoGlosario elemento, CU_Glosario glosarioAnalisis) {
		switch(glosarioAnalisis) {
		case MODIFICARTERMINO10_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del término es inválido.", "MSG13");
			}
			break;
		case ELIMINARTERMINO10_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del término es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}
	
	public static void verificarEstado(Entidad elemento, CU_Entidades entidadAnalisis) {
		switch(entidadAnalisis) {
		case MODIFICARENTIDAD11_2:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del término es inválido.", "MSG13");
			}
			break;
		case ELIMINARENTIDAD11_3:
			if (elemento.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.EDICION)) {
				throw new PRISMAException("El estado del término es inválido.", "MSG13");
			}
			break;
		default:
			break;
		}
		
	}
	
	public static void modificarEstadoElemento(Elemento elemento, Estado estado) throws Exception {
		elemento.setEstadoElemento(ElementoBs
				.consultarEstadoElemento(estado));
		new ElementoDAO().modificarElemento(elemento);
	}
}

