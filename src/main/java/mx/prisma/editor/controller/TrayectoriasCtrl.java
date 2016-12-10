package mx.prisma.editor.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.TipoSeccionEnum;
import mx.prisma.bs.TipoSeccionEnum.TipoSeccionENUM;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.PasoBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Revision;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.JsonUtil;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

@ResultPath("/content/editor/")
@Results({
		@Result(name = ActionSupportPRISMA.SUCCESS, type = "redirectAction", params = {
				"actionName", "trayectorias" }),
		@Result(name = "referencias", type = "json", params = { "root",
				"elementosReferencias" }),
		@Result(name = "cu", type = "redirectAction", params = { "actionName",
				"cu" }) })
public class TrayectoriasCtrl extends ActionSupportPRISMA implements
		ModelDriven<Trayectoria>, SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> userSession;

	private Proyecto proyecto;
	private Modulo modulo;
	private Colaborador colaborador;

	private CasoUso casoUso;
	private Trayectoria model;
	private Set<Paso>pasos = new HashSet<Paso>();
	private List<Trayectoria> listTrayectorias;
	private String jsonPasosTabla;
	private Integer idCU;
	private List<String> listRealiza;
	private List<String> listVerbos;

	private String jsonReglasNegocio;
	private String jsonEntidades;
	private String jsonCasosUsoProyecto;
	private String jsonPantallas;
	private String jsonMensajes;
	private String jsonActores;
	private String jsonTerminosGls;
	private String jsonAtributos;
	private String jsonPasos;
	private String jsonTrayectorias;
	private String jsonAcciones;

	private Integer idSel;
	private Integer idSelPaso;

	private boolean existeTPrincipal;
	private List<String> listAlternativa;
	private String alternativaPrincipal;

	private String observaciones;
	private String comentario;
	private List<String> elementosReferencias;

	public String index() throws Exception {
		String resultado;
		Map<String, Object> session = null;
		try {
			if (SessionManager.consultarCasoUsoActivo() == null) {
				session = ActionContext.getContext().getSession();
				session.put("idCU", idCU);
			}

			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(casoUso.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}

			model.setCasoUso(casoUso);

			listTrayectorias = new ArrayList<Trayectoria>();
			for (Trayectoria t : casoUso.getTrayectorias()) {
				listTrayectorias.add(t);
			}

			for (Revision rev : model.getCasoUso().getRevisiones()) {
				if (!rev.isRevisado()
						&& rev.getSeccion()
								.getNombre()
								.equals(TipoSeccionEnum
										.getNombre(TipoSeccionENUM.TRAYECTORIA))) {
					this.observaciones = rev.getObservaciones();
				}
			}

			@SuppressWarnings("unchecked")
			Collection<String> msjs = (Collection<String>) SessionManager
					.get("mensajesAccion");
			this.setActionMessages(msjs);
			SessionManager.delete("mensajesAccion");

		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
		}
		return INDEX;
	}

	public String editNew() throws Exception {

		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(casoUso.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setCasoUso(casoUso);

			existeTPrincipal = TrayectoriaBs.existeTrayectoriaPrincipal(casoUso
					.getId());
			buscaElementos();
			buscaCatalogos();

			resultado = EDITNEW;
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public String create() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(casoUso.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			
			if (alternativaPrincipal == null
					|| alternativaPrincipal.equals("Alternativa")) {
				model.setAlternativa(true);
			} else if (alternativaPrincipal.equals("Principal")) {
				model.setAlternativa(false);
			} else {
				throw new PRISMAValidacionException(
						"El usuario no seleccionó el tipo de la trayectoria.",
						"MSG4", null, "alternativaPrincipal");
			}

			model.setCasoUso(casoUso);
			agregarPasos();
			TrayectoriaBs.preAlmacenarObjetosToken(model);

			TrayectoriaBs.registrarTrayectoria(model);

			resultado = SUCCESS;

			addActionMessage(getText("MSG1", new String[] { "La",
					"Trayectoria", "registrada" }));

			SessionManager.set(this.getActionMessages(), "mensajesAccion");
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = editNew();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public String edit() throws Exception {
		String resultado = null;
		try {
			Date date = new Date(97, 1, 23);
			long t1 = date.getTime();
			colaborador = SessionManager.consultarColaboradorActivo();
			
			t1 = date.getTime();
			proyecto = SessionManager.consultarProyectoActivo();
			
			t1 = date.getTime();
			modulo = SessionManager.consultarModuloActivo();
			
			t1 = date.getTime();
			casoUso = SessionManager.consultarCasoUsoActivo();
			
			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			
			t1 = date.getTime();
			if (!AccessBs.verificarPermisos(model.getCasoUso().getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setCasoUso(casoUso);

			t1 = date.getTime();
			ElementoBs.verificarEstado(casoUso,
					CU_CasosUso.MODIFICARTRAYECTORIA5_1_1_2);

			t1 = date.getTime();
			buscaElementos();
			
			t1 = date.getTime();
			buscaCatalogos();
			
			t1 = date.getTime();
			existeTPrincipal = TrayectoriaBs.existeTrayectoriaPrincipal(
					casoUso.getId(), model.getId());

			t1 = date.getTime();
			prepararVista();

			resultado = EDIT;
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public String update() throws Exception {
		String resultado = null;
		
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getCasoUso().getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			
			if (alternativaPrincipal == null
					|| alternativaPrincipal.equals("Alternativa")) {
				model.setAlternativa(true);
			} else if (alternativaPrincipal.equals("Principal")) {
				model.setAlternativa(false);
			} else {
				throw new PRISMAValidacionException(
						"El usuario no seleccionó el tipo de la trayectoria.",
						"MSG4", null, "alternativaPrincipal");
			}
			List<Paso>pasosAux = TrayectoriaBs.obtenerPasos_(model.getId());//HOLI
			pasos.addAll(pasosAux);
			pasos.clear();
			model.setPasos(null);//ANTES ESTABA model.getPasos().clear();
			
			agregarPasos();
			TrayectoriaBs.preAlmacenarObjetosToken(model);
//			Actualizacion actualizacion = new Actualizacion(new Date(),
//					comentario, model.getCasoUso(),
//					SessionManager.consultarColaboradorActivo());
//
//			TrayectoriaBs.modificarTrayectoria(model, actualizacion);
			TrayectoriaBs.modificarTrayectoria(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"Trayectoria", "modificada" }));

			SessionManager.set(this.getActionMessages(), "mensajesAccion");
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = edit();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public String destroy() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getCasoUso().getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			TrayectoriaBs.eliminarTrayectoria(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"trayectoria", "eliminada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	private void buscaCatalogos() {
		// Se llena la lista del catálogo de quien realiza
		listRealiza = new ArrayList<String>();
		listRealiza.add("Actor");
		listRealiza.add("Sistema");

		// Se llena la lista par indicar si es alternativa o no
		listAlternativa = new ArrayList<String>();
		listAlternativa.add("Principal");
		listAlternativa.add("Alternativa");

		// Se extraen los verbos de la BD
		listVerbos = TrayectoriaBs.consultarVerbos();
	}

	private void agregarPasos() {
		Set<Paso> pasosModelo = new HashSet<Paso>(0);
		Set<Paso> pasosVista = new HashSet<Paso>(0);

		Paso pasoBD = null;
		if (jsonPasosTabla != null && !jsonPasosTabla.equals("")) {
			pasosVista = JsonUtil.mapJSONToSet(jsonPasosTabla, Paso.class);
			for (Paso pasoVista : pasosVista) {
				if (pasoVista.getId() != null && pasoVista.getId() != 0) {
					System.out.println("pasoVista if: "+pasoVista.getRedaccion());
					pasoBD = new PasoDAO().consultarPaso(pasoVista.getId());
					pasoBD.setNumero(pasoVista.getNumero());
					pasoBD.setRealizaActor(pasoVista.isRealizaActor());
					pasoBD.setVerbo(TrayectoriaBs.consultaVerbo(pasoVista
							.getVerbo().getNombre()));
					if(pasoVista.getOtroVerbo() != null && pasoVista.getOtroVerbo().isEmpty()) {
						pasoVista.setOtroVerbo(null);
					} else {
						pasoBD.setOtroVerbo(pasoVista.getOtroVerbo());
					}
					pasoBD.setRedaccion(pasoVista.getRedaccion());
					pasoBD.getReferencias().clear();
					pasosModelo.add(pasoBD);

				} else {
					System.out.println("pasoVista else: "+pasoVista.getRedaccion());
					pasoVista.setId(null);
					pasoVista.setVerbo(TrayectoriaBs.consultaVerbo(pasoVista
							.getVerbo().getNombre()));
					if(pasoVista.getOtroVerbo() != null && pasoVista.getOtroVerbo().isEmpty()) {
						pasoVista.setOtroVerbo(null);
					}
					pasoVista.setTrayectoria(model);
					pasosModelo.add(pasoVista);

				}
			}
			
			pasos.addAll(pasosModelo);
			System.out.println("Pasos: "+pasos);
			model.setPasos(pasos); //antes estaba model.getPasos().addAll(pasosModelo).
		}
	}

	private void buscaElementos() throws Exception {
		// Lists de los elementos disponibles
		List<Elemento> listElementos;
		List<ReglaNegocio> listReglasNegocio = new ArrayList<ReglaNegocio>();
		List<Entidad> listEntidades = new ArrayList<Entidad>();
		List<CasoUso> listCasosUso = new ArrayList<CasoUso>();
		List<Pantalla> listPantallas = new ArrayList<Pantalla>();
		List<Mensaje> listMensajes = new ArrayList<Mensaje>();
		List<Actor> listActores = new ArrayList<Actor>();
		List<TerminoGlosario> listTerminosGls = new ArrayList<TerminoGlosario>();
		List<Atributo> listAtributos = new ArrayList<Atributo>();
		List<Paso> listPasos = new ArrayList<Paso>();
		List<Trayectoria> listTrayectorias = new ArrayList<Trayectoria>();
		List<Accion> listAcciones = new ArrayList<Accion>();
		// Se consulta el modulo y el proyecto activos
		modulo = SessionManager.consultarModuloActivo();
		proyecto = modulo.getProyecto();

		// Se consultan los elementos de todo el proyecto
		listElementos = CuBs.consultarElementos(proyecto);

		// Módulo auxiliar para la serialización
		Modulo moduloAux = null;

		if (listElementos != null && !listElementos.isEmpty()) {
			// Se clasifican los conjuntos
			for (Elemento el : listElementos) {
				switch (ReferenciaEnum.getTipoReferencia(el)) {

				case ACTOR:
					Actor auxActor = new Actor();
					auxActor.setClave(el.getClave());
					auxActor.setNombre(el.getNombre());
					listActores.add(auxActor);
					break;
				case CASOUSO:					
					CasoUso auxCasoUso = new CasoUso();
					CasoUso cu = (CasoUso) el;
					
					moduloAux = new Modulo();
					moduloAux.setId(cu.getModulo().getId());
					moduloAux.setNombre(cu.getModulo().getNombre());
					moduloAux.setClave(cu.getModulo().getClave());
					
					auxCasoUso.setClave(cu.getClave());
					auxCasoUso.setNumero(cu.getNumero());
					auxCasoUso.setNombre(cu.getNombre());
					auxCasoUso.setModulo(moduloAux);
					listCasosUso.add(auxCasoUso);

					// Se obtienen las Trayectorias
					Set<Trayectoria> trayectorias = ((CasoUso) el)
							.getTrayectorias();
					for (Trayectoria tray : trayectorias) {
						if (tray.getCasoUso().getId() == SessionManager
								.consultarCasoUsoActivo().getId()) {
							Trayectoria auxTrayectoria = new Trayectoria();
							auxTrayectoria.setClave(tray.getClave());
							auxTrayectoria.setCasoUso(auxCasoUso);
							listTrayectorias.add(auxTrayectoria);
							// Se obtienen los Pasos
							List<Paso>pasos = TrayectoriaBs.obtenerPasos_(tray.getId());//HOLI
						//	Set<Paso> pasos = tray.getPasos();
							for (Paso paso : pasos) {
								Paso auxPaso = new Paso();
								auxPaso.setTrayectoria(auxTrayectoria);
								auxPaso.setNumero(paso.getNumero());
								auxPaso.setRealizaActor(paso.isRealizaActor());
								auxPaso.setVerbo(paso.getVerbo());
								auxPaso.setOtroVerbo(paso.getOtroVerbo());
								auxPaso.setRedaccion(TokenBs
										.decodificarCadenaSinToken(paso
												.getRedaccion()));
								listPasos.add(auxPaso);
							}
						}
					}
					break;
				case ENTIDAD:
					Entidad auxEntidad = new Entidad();
					auxEntidad.setNombre(el.getNombre());
					listEntidades.add(auxEntidad);
					// Se obtienen los Atributos
					Set<Atributo> atributos = ((Entidad) el).getAtributos();
					for (Atributo atributo : atributos) {
						Atributo auxAtributo = new Atributo();
						auxAtributo.setEntidad(auxEntidad);
						auxAtributo.setNombre(atributo.getNombre());
						listAtributos.add(auxAtributo);
					}

					break;
				case MENSAJE:
					Mensaje auxMensaje = new Mensaje();
					auxMensaje.setNumero(el.getNumero());
					auxMensaje.setNombre(el.getNombre());
					listMensajes.add(auxMensaje);
					break;
				case PANTALLA:
					Pantalla auxPantalla = new Pantalla();
					Pantalla pantalla = (Pantalla) el;
					moduloAux = new Modulo();
					moduloAux.setId(pantalla.getModulo().getId());
					moduloAux.setNombre(pantalla.getModulo().getNombre());
					moduloAux.setClave(pantalla.getModulo().getClave());
					
					auxPantalla.setClave(pantalla.getClave());
					auxPantalla.setNumero(pantalla.getNumero());
					auxPantalla.setNombre(pantalla.getNombre());
					auxPantalla.setModulo(moduloAux);
					listPantallas.add(auxPantalla);
					// Se obtienen las acciones
					Set<Accion> acciones = ((Pantalla) el).getAcciones();
					for (Accion accion : acciones) {
						Accion auxAccion = new Accion();
						auxAccion.setPantalla(auxPantalla);
						auxAccion.setNombre(accion.getNombre());
						listAcciones.add(auxAccion);
					}
					break;
				case REGLANEGOCIO:
					ReglaNegocio auxReglaNegocio = new ReglaNegocio();
					auxReglaNegocio.setNumero(el.getNumero());
					auxReglaNegocio.setNombre(el.getNombre());
					listReglasNegocio.add(auxReglaNegocio);
					break;
				case TERMINOGLS:
					TerminoGlosario auxTerminoGlosario = new TerminoGlosario();
					auxTerminoGlosario.setNombre(el.getNombre());
					listTerminosGls.add(auxTerminoGlosario);
					break;
				default:
					break;
				}
			}

			// Se convierte en json las Reglas de Negocio
			if (listReglasNegocio != null) {
				this.jsonReglasNegocio = JsonUtil
						.mapListToJSON(listReglasNegocio);
			}
			// Se convierte en json las Entidades
			if (listEntidades != null) {
				this.jsonEntidades = JsonUtil.mapListToJSON(listEntidades);
			}
			// Se convierte en json los Casos de Uso
			if (listCasosUso != null) {
				this.jsonCasosUsoProyecto = JsonUtil
						.mapListToJSON(listCasosUso);
			}
			// Se convierte en json las Pantallas
			if (listPantallas != null) {
				this.jsonPantallas = JsonUtil.mapListToJSON(listPantallas);
			}
			// Se convierte en json los Mensajes
			if (listMensajes != null) {
				this.jsonMensajes = JsonUtil.mapListToJSON(listMensajes);
			}
			// Se convierte en json los Actores
			if (listActores != null) {
				this.jsonActores = JsonUtil.mapListToJSON(listActores);
			}
			// Se convierte en json los Términos del Glosario
			if (listTerminosGls != null) {
				this.jsonTerminosGls = JsonUtil.mapListToJSON(listTerminosGls);
			}
			// Se convierte en json los Atributos
			if (listAtributos != null) {
				this.jsonAtributos = JsonUtil.mapListToJSON(listAtributos);
			}
			// Se convierte en json los Pasos
			if (listPasos != null) {
				this.jsonPasos = JsonUtil.mapListToJSON(listPasos);
			}
			// Se convierte en json las Trayectorias
			if (listTrayectorias != null) {
				this.jsonTrayectorias = JsonUtil
						.mapListToJSON(listTrayectorias);
			}
			// Se convierte en json las Acciones
			if (listAcciones != null) {
				this.jsonAcciones = JsonUtil.mapListToJSON(listAcciones);
			}
		}

	}

	private void prepararVista() {
		//Set<Paso> pasos = model.getPasos();
		ArrayList<Paso> pasosTabla = new ArrayList<Paso>();
		Paso pasoAux;
		List<Paso>pasos = TrayectoriaBs.obtenerPasos_(model.getId());//HOLI
		for (Paso paso : pasos) {
			pasoAux = new Paso();
			pasoAux.setNumero(paso.getNumero());
			pasoAux.setRedaccion(TokenBs.decodificarCadenasToken(paso
					.getRedaccion()));
			pasoAux.setRealizaActor(paso.isRealizaActor());
			pasoAux.setVerbo(paso.getVerbo());
			pasoAux.setOtroVerbo(paso.getOtroVerbo());
			pasoAux.setId(paso.getId());
			pasosTabla.add(pasoAux);
		}

		this.jsonPasosTabla = JsonUtil.mapListToJSON(pasosTabla);

		if (model.isAlternativa()) {
			alternativaPrincipal = "Alternativa";
		} else {
			alternativaPrincipal = "Principal";
		}

		for (Revision rev : model.getCasoUso().getRevisiones()) {
			if (!rev.isRevisado()
					&& rev.getSeccion()
							.getNombre()
							.equals(TipoSeccionEnum
									.getNombre(TipoSeccionENUM.TRAYECTORIA))) {
				this.observaciones = rev.getObservaciones();
			}
		}

	}

	public String verificarElementosReferencias() {
		try {
			model.setId(idSel);
			elementosReferencias = new ArrayList<String>();
			elementosReferencias = TrayectoriaBs.verificarReferencias(model, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "referencias";
	}

	public String verificarElementosReferenciasPaso() {
		try {
			elementosReferencias = new ArrayList<String>();
			elementosReferencias = PasoBs.verificarReferencias(PasoBs
					.consultarPaso(idSelPaso), null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "referencias";
	}

	public Trayectoria getModel() {
		if (this.model == null) {
			model = new Trayectoria();
		}
		return model;
	}

	public void setModel(Trayectoria model) {
		this.model = model;
	}

	public List<Trayectoria> getListTrayectorias() {
		return listTrayectorias;
	}

	public void setListTrayectorias(List<Trayectoria> listTrayectorias) {
		this.listTrayectorias = listTrayectorias;
	}

	public Integer getIdCU() {
		return idCU;
	}

	public void setIdCU(Integer idCU) {
		this.idCU = idCU;
	}

	public List<String> getListRealiza() {
		return listRealiza;
	}

	public void setListRealiza(List<String> listRealiza) {
		this.listRealiza = listRealiza;
	}

	public List<String> getListVerbos() {
		return listVerbos;
	}

	public void setListVerbos(List<String> listVerbos) {
		this.listVerbos = listVerbos;
	}

	public String getJsonPasosTabla() {
		return jsonPasosTabla;
	}

	public void setJsonPasosTabla(String jsonPasosTabla) {
		this.jsonPasosTabla = jsonPasosTabla;
	}

	public String getJsonReglasNegocio() {
		return jsonReglasNegocio;
	}

	public void setJsonReglasNegocio(String jsonReglasNegocio) {
		this.jsonReglasNegocio = jsonReglasNegocio;
	}

	public String getJsonEntidades() {
		return jsonEntidades;
	}

	public void setJsonEntidades(String jsonEntidades) {
		this.jsonEntidades = jsonEntidades;
	}

	public String getJsonCasosUsoProyecto() {
		return jsonCasosUsoProyecto;
	}

	public void setJsonCasosUsoProyecto(String jsonCasosUsoProyecto) {
		this.jsonCasosUsoProyecto = jsonCasosUsoProyecto;
	}

	public String getJsonPantallas() {
		return jsonPantallas;
	}

	public void setJsonPantallas(String jsonPantallas) {
		this.jsonPantallas = jsonPantallas;
	}

	public String getJsonMensajes() {
		return jsonMensajes;
	}

	public void setJsonMensajes(String jsonMensajes) {
		this.jsonMensajes = jsonMensajes;
	}

	public String getJsonActores() {
		return jsonActores;
	}

	public void setJsonActores(String jsonActores) {
		this.jsonActores = jsonActores;
	}

	public String getJsonTerminosGls() {
		return jsonTerminosGls;
	}

	public void setJsonTerminosGls(String jsonTerminosGls) {
		this.jsonTerminosGls = jsonTerminosGls;
	}

	public String getJsonAtributos() {
		return jsonAtributos;
	}

	public void setJsonAtributos(String jsonAtributos) {
		this.jsonAtributos = jsonAtributos;
	}

	public String getJsonTrayectorias() {
		return jsonTrayectorias;
	}

	public void setJsonTrayectorias(String jsonTrayectorias) {
		this.jsonTrayectorias = jsonTrayectorias;
	}

	public String getJsonAcciones() {
		return jsonAcciones;
	}

	public void setJsonAcciones(String jsonAcciones) {
		this.jsonAcciones = jsonAcciones;
	}

	public String getJsonPasos() {
		return jsonPasos;
	}

	public void setJsonPasos(String jsonPasos) {
		this.jsonPasos = jsonPasos;
	}

	public void setSession(Map<String, Object> session) {
		userSession = session;

	}

	public boolean isExisteTPrincipal() {
		return existeTPrincipal;
	}

	public void setExisteTPrincipal(boolean existeTPrincipal) {
		this.existeTPrincipal = existeTPrincipal;
	}

	public List<String> getListAlternativa() {
		return listAlternativa;
	}

	public void setListAlternativa(List<String> listAlternativa) {
		this.listAlternativa = listAlternativa;
	}

	public String getAlternativaPrincipal() {
		return alternativaPrincipal;
	}

	public void setAlternativaPrincipal(String alternativaPrincipal) {
		this.alternativaPrincipal = alternativaPrincipal;
	}

	public Map<String, Object> getUserSession() {
		return userSession;
	}

	public void setUserSession(Map<String, Object> userSession) {
		this.userSession = userSession;
	}

	public Integer getIdSel() {
		return idSel;
	}

	public void setIdSel(Integer idSel) {
		this.idSel = idSel;
		if (this.model == null) {
			this.model = TrayectoriaBs.consultarTrayectoria(idSel);
		}
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public List<String> getElementosReferencias() {
		return elementosReferencias;
	}

	public void setElementosReferencias(List<String> elementosReferencias) {
		this.elementosReferencias = elementosReferencias;
	}

	public Integer getIdSelPaso() {
		return idSelPaso;
	}

	public void setIdSelPaso(Integer idSelPaso) {
		this.idSelPaso = idSelPaso;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	
	

}
