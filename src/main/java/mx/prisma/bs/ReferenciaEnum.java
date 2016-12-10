package mx.prisma.bs;

import mx.prisma.admin.model.EstadoProyecto;
import mx.prisma.admin.model.Rol;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Cardinalidad;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.EstadoElemento;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Operador;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Parametro;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.TipoAccion;
import mx.prisma.editor.model.TipoComparacion;
import mx.prisma.editor.model.TipoDato;
import mx.prisma.editor.model.TipoParametro;
import mx.prisma.editor.model.TipoReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.UnidadTamanio;
import mx.prisma.editor.model.Verbo;

public class ReferenciaEnum {
	
	private static String ACCION = "Acción";
	private static String ACTOR = "Actor";
	private static String ATRIBUTO = "Atributo";
	private static String CASOUSO = "Caso de uso";
	private static String ENTIDAD = "Entidad";
	private static String MENSAJE = "Mensaje";
	private static String PANTALLA = "Pantalla";
	private static String PASO = "Paso";
	private static String REGLANEGOCIO = "Regla de negocio";
	private static String TERMINO = "Término del glosario";
	private static String TRAYECTORIA = "Trayectoria";







	
	public enum TipoReferencia {
	    ACTOR, ENTIDAD, CASOUSO, PANTALLA, PASO, ATRIBUTO,
	    MENSAJE, REGLANEGOCIO, TERMINOGLS, ACCION, TRAYECTORIA, PARAMETRO
	}
	
	public enum TipoSeccion {
		ACTORES, ENTRADAS, SALIDAS, REGLAS, POSTPRECONDICIONES, REGLASNEGOCIOS, PASOS, EXTENSIONES, PARAMETROS
	}
	
	public enum TipoRelacion {
		/* Actores */
		ACTOR_ACTORES, ACTOR_POSTPRECONDICIONES, ACTOR_PASOS,
			
		/* Entidades */
		ENTIDAD_POSTPRECONDICIONES, ENTIDAD_PASOS,
		
		/* Casos de uso */
		CASOUSO_POSTPRECONDICIONES, CASOUSO_PASOS,
		
		/* Pantalla */
		PANTALLA_PASOS, PANTALLA_POSTPRECONDICIONES,
		
		/* Mensajes */
		MENSAJE_SALIDAS, MENSAJE_POSTPRECONDICIONES, MENSAJE_PASOS,
		
		/* Reglas de negocio */
		REGLANEGOCIO_REGLASNEGOCIOS, REGLANEGOCIO_POSTPRECONDICIONES, REGLANEGOCIO_PASOS,
		
		/* Término del glosario */		
		TERMINOGLS_ENTRADAS, TERMINOGLS_SALIDAS, TERMINOGLS_POSTPRECONDICIONES, TERMINOGLS_PASOS,

		/* Atributos */		
		ATRIBUTO_ENTRADAS, ATRIBUTO_SALIDAS, ATRIBUTO_POSTPRECONDICIONES, ATRIBUTO_PASOS,
		
		/* Acciones */
		ACCION_POSTPRECONDICIONES, ACCION_PASOS,
		
		/* Trayectorias */
		TRAYECTORIA_POSTPRECONDICIONES, TRAYECTORIA_PASOS,
		
		/* Pasos */
		PASO_PASOS, PASO_POSTPRECONDICIONES, PASO_EXTENSIONES
	}
	
	public enum TipoCatalogo {
		VERBO, TIPOPARAMETRO, TIPODATO, TIPOACCION, CARDINALIDAD,
		
		UNIDADTAMANIO, OPERADOR, TIPOCOMPARACION, TIPOREGLANEGOCIO,
		
		PARAMETRO, ESTADOELEMENTO, ESTADOPROYECTO, ROL
	}

	
	
	public static TipoReferencia getTipoReferencia(String tokenReferencia){
		if (tokenReferencia.equals("ACT")){
			return TipoReferencia.ACTOR;
		}
		if (tokenReferencia.equals("ATR")){
			return TipoReferencia.ATRIBUTO;
		}
		if (tokenReferencia.equals("ENT")){
			return TipoReferencia.ENTIDAD;
		}
		if (tokenReferencia.equals("CU")){
			return TipoReferencia.CASOUSO;
		}
		if (tokenReferencia.equals("IU")){
			return TipoReferencia.PANTALLA;
		}
		if (tokenReferencia.equals("MSG")){
			return TipoReferencia.MENSAJE;
		}
		if (tokenReferencia.equals("RN")){
			return TipoReferencia.REGLANEGOCIO;
		}
		if (tokenReferencia.equals("GLS")){
			return TipoReferencia.TERMINOGLS;
		}
		if (tokenReferencia.equals("ACC")){
			return TipoReferencia.ACCION;
		}
		if (tokenReferencia.equals("TRAY")){
			return TipoReferencia.TRAYECTORIA;
		}
		if (tokenReferencia.equals("P")){
			return TipoReferencia.PASO;
		}
		if (tokenReferencia.equals("PARAM")){
			return TipoReferencia.PARAMETRO;
		}
		return null;
	}
	
	public static TipoReferencia getTipoReferencia(Object objeto){
		if (objeto instanceof Actor){
			return TipoReferencia.ACTOR;
		}
		if (objeto instanceof Atributo){
			return TipoReferencia.ATRIBUTO;
		}
		if (objeto instanceof Entidad){
			return TipoReferencia.ENTIDAD;
		}
		if (objeto instanceof CasoUso){
			return TipoReferencia.CASOUSO;
		}
		if (objeto instanceof Pantalla){
			return TipoReferencia.PANTALLA;
		}
		if (objeto instanceof Mensaje){
			return TipoReferencia.MENSAJE;
		}
		if (objeto instanceof ReglaNegocio){
			return TipoReferencia.REGLANEGOCIO;
		}
		if (objeto instanceof TerminoGlosario){
			return TipoReferencia.TERMINOGLS;
		}
		if (objeto instanceof Accion){
			return TipoReferencia.ACCION;
		}
		if (objeto instanceof Trayectoria){
			return TipoReferencia.TRAYECTORIA;
		}
		if (objeto instanceof Paso){
			return TipoReferencia.PASO;
		}
		System.out.println("No es instancia de ninguna clase");
		
		return null;
	}
	
	public static TipoRelacion getTipoRelacion(TipoReferencia tipoReferencia, TipoSeccion tipoSeccion){
		if(tipoReferencia == TipoReferencia.ACTOR) {
			if (tipoSeccion == TipoSeccion.ACTORES) {
				return TipoRelacion.ACTOR_ACTORES;
			}
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.ACTOR_POSTPRECONDICIONES;
				
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.ACTOR_PASOS;
			}			
		}
		
		if(tipoReferencia == TipoReferencia.ENTIDAD) {
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.ENTIDAD_POSTPRECONDICIONES;
				
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.ENTIDAD_PASOS;
			}			
		}
		
		if(tipoReferencia == TipoReferencia.CASOUSO) {
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.CASOUSO_POSTPRECONDICIONES;
				
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.CASOUSO_PASOS;
			}			
		}		
		
		if(tipoReferencia == TipoReferencia.PANTALLA) {
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.PANTALLA_PASOS;
			}		
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.PANTALLA_POSTPRECONDICIONES;
				
			}
		}
		
		if(tipoReferencia == TipoReferencia.MENSAJE) {
			if (tipoSeccion == TipoSeccion.SALIDAS) {
				return TipoRelacion.MENSAJE_SALIDAS;
			}	
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.MENSAJE_POSTPRECONDICIONES;
				
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.MENSAJE_PASOS;
			}	
		}	
		
		if(tipoReferencia == TipoReferencia.REGLANEGOCIO) {
			if (tipoSeccion == TipoSeccion.REGLASNEGOCIOS) {
				return TipoRelacion.REGLANEGOCIO_REGLASNEGOCIOS;
			}		
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.REGLANEGOCIO_POSTPRECONDICIONES;
				
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.REGLANEGOCIO_PASOS;
			}	
		}	
		if(tipoReferencia == TipoReferencia.TERMINOGLS) {
			if (tipoSeccion == TipoSeccion.ENTRADAS) {
				return TipoRelacion.TERMINOGLS_ENTRADAS;
			}		
			if (tipoSeccion == TipoSeccion.SALIDAS) {
				return TipoRelacion.TERMINOGLS_SALIDAS;
			}	
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.TERMINOGLS_POSTPRECONDICIONES;
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.TERMINOGLS_PASOS;
			}	
		}
		
		if(tipoReferencia == TipoReferencia.ATRIBUTO) {
			if (tipoSeccion == TipoSeccion.ENTRADAS) {
				return TipoRelacion.ATRIBUTO_ENTRADAS;
			}		
			if (tipoSeccion == TipoSeccion.SALIDAS) {
				return TipoRelacion.ATRIBUTO_SALIDAS;
			}	
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.ATRIBUTO_POSTPRECONDICIONES;
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.ATRIBUTO_PASOS;
			}	
		}
		
		if(tipoReferencia == TipoReferencia.ACCION) {	
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.ACCION_POSTPRECONDICIONES;
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.ACCION_PASOS;
			}	
		}
	
		if(tipoReferencia == TipoReferencia.TRAYECTORIA) {	
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.TRAYECTORIA_POSTPRECONDICIONES;
			}
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.TRAYECTORIA_PASOS;
			}	
		}
		
		if(tipoReferencia == TipoReferencia.PASO) {	
			if (tipoSeccion == TipoSeccion.PASOS) {
				return TipoRelacion.PASO_PASOS;
			}
			if (tipoSeccion == TipoSeccion.POSTPRECONDICIONES) {
				return TipoRelacion.PASO_POSTPRECONDICIONES;
			}
			if (tipoSeccion == TipoSeccion.EXTENSIONES) {
				return TipoRelacion.PASO_EXTENSIONES;
			}	
		}
		return null;
	}

	public static String getTabla(TipoReferencia referencia) {
		switch(referencia){
		case ACCION:
			return "Acción";
		case ACTOR:
			return "Actor";
		case ATRIBUTO:
			return "Atributo";
		case CASOUSO:
			return "CasoUso";
		case ENTIDAD:
			return "Entidad";		
		case MENSAJE:
			return "Mensaje";
		case PANTALLA:
			return "Pantalla";
		case PASO:
			return "Paso";
		case REGLANEGOCIO:
			return "ReglaNegocio";
		case TERMINOGLS:
			return "TerminoGlosario";
		case TRAYECTORIA:
			return "Trayectoria";
		default:
			break;
		
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getClase(TipoReferencia referencia) {
		switch(referencia){
		case ACCION:
			return Accion.class;
		case ACTOR:
			return Actor.class;
		case ATRIBUTO:
			return Atributo.class;
		case CASOUSO:
			return CasoUso.class;
		case ENTIDAD:
			return Entidad.class;		
		case MENSAJE:
			return Mensaje.class;
		case PANTALLA:
			return Pantalla.class;
		case PASO:
			return Paso.class;
		case REGLANEGOCIO:
			return ReglaNegocio.class;
		case TERMINOGLS:
			return TerminoGlosario.class;
		case TRAYECTORIA:
			return Trayectoria.class;
		default:
			break;
		
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getClaseCatalogo (TipoCatalogo tipoCatalogo) {
		switch(tipoCatalogo){
		case CARDINALIDAD:
			return Cardinalidad.class;
		case ESTADOELEMENTO:
			return EstadoElemento.class;
		case ESTADOPROYECTO:
			return EstadoProyecto.class;
		case OPERADOR:
			return Operador.class;
		case PARAMETRO:
			return Parametro.class;
		case ROL:
			return Rol.class;
		case TIPOACCION:
			return TipoAccion.class;
		case TIPOCOMPARACION:
			return TipoComparacion.class;
		case TIPODATO:
			return TipoDato.class;
		case TIPOPARAMETRO:
			return TipoParametro.class;
		case TIPOREGLANEGOCIO:
			return TipoReglaNegocio.class;
		case UNIDADTAMANIO:
			return UnidadTamanio.class;
		case VERBO:
			return Verbo.class;
		default:
			break;
			
		}
		return null;

	}

	public static TipoReferencia getTipoReferenciaParametro(
			ReferenciaParametro referenciaParametro) {
		if (referenciaParametro.getTipoParametro().getNombre().equals(ACCION)){
			return TipoReferencia.ACCION;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(ACTOR)){
			return TipoReferencia.ACTOR;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(ATRIBUTO)){
			return TipoReferencia.ATRIBUTO;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(CASOUSO)){
			return TipoReferencia.CASOUSO;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(ENTIDAD)){
			return TipoReferencia.ENTIDAD;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(MENSAJE)){
			return TipoReferencia.MENSAJE;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(PANTALLA)){
			return TipoReferencia.PANTALLA;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(PASO)){
			return TipoReferencia.PASO;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(REGLANEGOCIO)){
			return TipoReferencia.REGLANEGOCIO;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(TERMINO)){
			return TipoReferencia.TERMINOGLS;
		}
		if (referenciaParametro.getTipoParametro().getNombre().equals(TRAYECTORIA)){
			return TipoReferencia.TRAYECTORIA;
		}
		
		System.out.println("No es instancia de ninguna clase ID: " + referenciaParametro.getId());
		
		return null;
	}
}
