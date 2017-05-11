package mx.prisma.generadorPruebas.bs;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.bs.TipoReglaNegocioEnum.TipoReglaNegocioENUM;
import mx.prisma.editor.bs.PasoBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.Verbo;

public class AnalizadorPasosBs {
	
	public enum TipoPaso {
		actorOprimeBoton,
		sistemaValidaPrecondicion,
		sistemaMuestraPantalla,
		actorIngresaDatos,
		sistemaValidaReglaNegocio,
		sistemaEjecutaTransaccion,
		sistemaMuestraMensaje,
		actorSoliciaSeleccionarRegistro
	}	
	
	public enum AmbitoOprimeBoton {
		oprimeBotonSolicitud,
		oprimeBotonEjecucion
	}
	
	public static boolean isSelectorRegistroGestion(Paso paso) {
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == true && patron2.getNombre().equals("Oprime")
				&& redaccion.contains(TokenBs.tokenACC) && redaccion.contains("desea")) {
			return true;
		} else {
			return false;
		}
	}
	
	public AmbitoOprimeBoton obtenerAmbito(Paso paso) {
		AmbitoOprimeBoton ambito = null;
		Trayectoria trayectoria = paso.getTrayectoria();
		List<Paso> pasosAntOprimeBoton = new ArrayList<Paso>();
		List<Paso> pasosSigOprimeBoton = new ArrayList<Paso>();
		List<Paso> pasosSigEjecutaTransaccion = new ArrayList<Paso>();
		
		for(Paso p : trayectoria.getPasos()) {
			if(isActorOprimeBoton(p) && p.getId() != paso.getId()) {
				if(p.getNumero() < paso.getNumero()) {
					pasosAntOprimeBoton.add(p);
				} else {
					pasosSigOprimeBoton.add(p);
				}				
			} 
			if(isSistemaEjecutaTransaccion(p)) {
				pasosSigEjecutaTransaccion.add(p);
			}
		}
		
		if(pasosAntOprimeBoton.isEmpty()) {
			return AmbitoOprimeBoton.oprimeBotonSolicitud; 
		} else {
			if(pasosSigOprimeBoton.isEmpty() && !pasosSigEjecutaTransaccion.isEmpty()) {
				return AmbitoOprimeBoton.oprimeBotonEjecucion;
			} 
		} 
		return ambito;
	}

	//Generar petición HTTP 
	public static boolean isActorOprimeBoton(Paso paso) {
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == true && (patron2.getNombre().equals("Solicita") || patron2.getNombre().equals("Oprime"))
				&& redaccion.contains(TokenBs.tokenACC)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSistemaValidaPrecondicion(Paso paso) {
	// Petición JDBC con query
	// Controlador if
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		TipoReglaNegocioENUM patron3 = TipoReglaNegocioENUM.VERFCATALOGOS;
		
		if (patron1 == false) {
			if ((patron2.getNombre().equals("Busca") || patron2.getNombre().equals("Verifica"))
					&& redaccion.contains(TokenBs.tokenRN)) {
				Paso pasoRefs = PasoBs.consultarPaso(paso.getId());
				for (ReferenciaParametro referenciaParametro : pasoRefs
						.getReferencias()) {
					if (referenciaParametro.getElementoDestino() instanceof ReglaNegocio) {
						ReglaNegocio reglaNegocio = (ReglaNegocio) referenciaParametro
								.getElementoDestino();
						if (TipoReglaNegocioEnum
								.getTipoReglaNegocio(reglaNegocio
										.getTipoReglaNegocio()) == patron3) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	//Assert
	public static boolean isSistemaMuestraPantalla(Paso paso) {
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == false && patron2.getNombre().equals("Muestra")
				&& redaccion.contains(TokenBs.tokenIU)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isActorIngresaDatos(Paso paso) {
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == true
				&& (patron2.getNombre().equals("Ingresa")
						|| patron2.getNombre().equals("Selecciona") || patron2
						.getNombre().equals("Modifica"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isSistemaValidaReglaNegocio(Paso paso) {
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();

		if (patron1 == false) {
			if ((patron2.getNombre().equals("Verifica"))
					&& redaccion.contains(TokenBs.tokenRN)) {
				Paso pasoRefs = PasoBs.consultarPaso(paso.getId());
				for (ReferenciaParametro referenciaParametro : pasoRefs
						.getReferencias()) {
					if (referenciaParametro.getElementoDestino() instanceof ReglaNegocio) {
						ReglaNegocio reglaNegocio = (ReglaNegocio) referenciaParametro
								.getElementoDestino();
						switch(TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio())) {
						case COMPATRIBUTOS:
							return false;
						case DATOCORRECTO:
							return true;
						case FORMATOARCH:
							return false;
						case FORMATOCAMPO:
							return true;//IIII
						case LONGITUD:
							return true;
						case OBLIGATORIOS:
							return true;
						case OTRO:
							return false;
						case TAMANOARCH:
							return false;
						case UNICIDAD:
							return true;
						case VERFCATALOGOS:
							return false;
						default:
							return false;						
						}
					}
				}

			}
		}
		return false;
	}

	//Petición HTTP con hijos
	//Contenedor csv
	
	public static boolean isSistemaEjecutaTransaccion(Paso paso) {
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == false
				&& (patron2.getNombre().equals("Registra")
						|| patron2.getNombre().equals("Modifica") || patron2
						.getNombre().equals("Elimina"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSistemaMuestraMensaje(Paso paso) {
		String redaccion = paso.getRedaccion();
		boolean patron1 = paso.isRealizaActor();
		Verbo patron2 = paso.getVerbo();
		if (patron1 == false && patron2.getNombre().equals("Muestra")
				&& redaccion.contains(TokenBs.tokenMSG) && redaccion.contains(TokenBs.tokenIU)) {
			return true;
		} else {
			return false;
		}
	}

	public static TipoPaso calcularTipo(Paso p) {
		TipoPaso tipoPaso = null;
		if (isActorOprimeBoton(p)) {
			//System.out.println("actorOprimeBoton");
			tipoPaso = TipoPaso.actorOprimeBoton;
		}			
		if (isSistemaMuestraPantalla(p)) {
			//System.out.println("sistemaMuestraPantalla");	
			tipoPaso = TipoPaso.sistemaMuestraPantalla;
		}	
		if (isActorIngresaDatos(p)) {
			//System.out.println("actorIngresaDatos");
			tipoPaso = TipoPaso.actorIngresaDatos;
		}	
		if (isSistemaValidaReglaNegocio(p)) {
			//System.out.println("sistemaValidaReglaNegocio");
			tipoPaso = TipoPaso.sistemaValidaReglaNegocio;
		}
		
		if (isSistemaValidaPrecondicion(p)) {
			//System.out.println("isSistemaValidaPrecondicion");
			tipoPaso = TipoPaso.sistemaValidaPrecondicion;
		}
		
		if (isSistemaEjecutaTransaccion(p)) {
			//System.out.println("sistemaEjecutaTransaccion");
			tipoPaso = TipoPaso.sistemaEjecutaTransaccion;
		}
		if (isSistemaMuestraMensaje(p)) {
			//System.out.println("sistemaMuestraMensaje");
			tipoPaso = TipoPaso.sistemaMuestraMensaje;
		}
		if(isSelectorRegistroGestion(p)) {
			tipoPaso = TipoPaso.actorSoliciaSeleccionarRegistro;
		}
		
		if (tipoPaso == null) {
			//System.out.println("No clasificado: " + seguimiento(p));
		} else {
			//System.out.println(tipoPaso+": " + seguimiento(p));
		}
		return tipoPaso;
	}

	public static Paso calcularSiguiente(Paso pasoActual, ArrayList<Paso> pasos) {
		if(pasoActual == null) {
			return null;
		}
		
		int idTrayectoriaActual = pasoActual.getTrayectoria().getId();
		
		Paso pasoSiguiente = new Paso();
		pasoSiguiente.setNumero(Integer.MAX_VALUE);
		
		for(Paso paso : pasos) {
			if(idTrayectoriaActual == paso.getTrayectoria().getId()) {
				if(pasoActual.getNumero() < paso.getNumero() && pasoSiguiente.getNumero() > paso.getNumero()) {
					pasoSiguiente = paso;
				}
			}
		}
		
		return pasoSiguiente.getNumero() == Integer.MAX_VALUE ? null : pasoSiguiente;
	}
	
	public static Paso calcularPasoAlternativo(Paso paso) {
		ReferenciaParametro referenciaTrayectoria = obtenerPrimerReferencia(paso, TipoReferencia.TRAYECTORIA);
		System.out.println(referenciaTrayectoria.getId());
		Trayectoria trayectoriaRef = referenciaTrayectoria.getTrayectoria();
		if(trayectoriaRef.isAlternativa()) {
			return buscarPaso(1, trayectoriaRef);
		}
		return null;
	}
	
	private static Paso buscarPaso(int numero, Trayectoria trayectoria) {
		List<Paso>pasosaux = new ArrayList<Paso>();
		pasosaux = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		for (Paso pasoi : pasosaux) {
			if (pasoi.getNumero() == numero) {
				return pasoi;
			}
		}
		return null;
	}

	public static ReferenciaParametro obtenerPrimerReferencia(Paso paso, ReferenciaEnum.TipoReferencia tipoReferencia) {
		ReferenciaParametro referencia = null;
		paso = new PasoDAO().consultarPaso(paso.getId());
		
		System.out.println(paso.getId()+": "+paso.getRedaccion());
		for(ReferenciaParametro rp : paso.getReferencias()) {
			if(ReferenciaEnum.getTipoReferenciaParametro(rp).equals(tipoReferencia)) {
				referencia = rp;
				System.out.println("Referencia: "+referencia.getTipoParametro());
				break;
			}
		}
		System.out.println("Antes de regresar");
		return referencia;
	}
	
	public static ArrayList<Paso> ordenarPasos(Trayectoria trayectoria) {
		List<Paso>pasosaux = new ArrayList<Paso>();
		pasosaux = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		int longitud = pasosaux.size();
		ArrayList<Paso> pasos = new ArrayList<Paso>();
		pasos.addAll(pasosaux);
		Paso paso = null;
		for (int i = 0; i < longitud; i++) {
			for (int j = 0; j < longitud; j++) {
				if (pasos.get(i).getNumero() < pasos.get(j).getNumero()) {
					paso = pasos.get(j);
					pasos.set(j, pasos.get(i));
					pasos.set(i, paso);
				}
			}
		}
		return pasos;
		
	}
		
	public static String seguimiento(Paso paso) {
		return paso.getTrayectoria().getCasoUso().getClave() + paso.getTrayectoria().getCasoUso().getNumero() + "-" +  paso.getTrayectoria().getClave() + paso.getNumero();

	}
}
