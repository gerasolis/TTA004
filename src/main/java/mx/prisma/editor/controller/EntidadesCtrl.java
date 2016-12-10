package mx.prisma.editor.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.bs.AtributoBs;
import mx.prisma.editor.bs.EntidadBs;
import mx.prisma.editor.dao.TipoDatoDAO;
import mx.prisma.editor.dao.UnidadTamanioDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.TipoDato;
import mx.prisma.editor.model.UnidadTamanio;
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
import com.opensymphony.xwork2.ModelDriven;

@ResultPath("/content/editor/")
@Results({
		@Result(name = ActionSupportPRISMA.SUCCESS, type = "redirectAction", params = {
				"actionName", "entidades" }),
		@Result(name = "referencias", type = "json", params = { "root",
				"elementosReferencias" }),
		@Result(name = "proyectos", type = "redirectAction", params = {
						"actionName", "proyectos" })
})

public class EntidadesCtrl extends ActionSupportPRISMA implements
		ModelDriven<Entidad>, SessionAware {
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> userSession;
	private Proyecto proyecto;
	private Entidad model;
	private Colaborador colaborador;
	private Modulo modulo;
	
	private List<Entidad> listEntidades;
	private String jsonAtributosTabla;
	private List<TipoDato> listTipoDato;
	private List<UnidadTamanio> listUnidadTamanio;
	private Integer idSel;
	private List<String> elementosReferencias;
	private String comentario;

	public String index() throws Exception {
		String resultado;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(proyecto, colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			listEntidades = EntidadBs.consultarEntidadesProyecto(proyecto);

			@SuppressWarnings("unchecked")
			Collection<String> msjs = (Collection<String>) SessionManager
					.get("mensajesAccion");
			this.setActionMessages(msjs);
			SessionManager.delete("mensajesAccion");

		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INDEX;
	}

	public String editNew() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(proyecto, colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			buscaCatalogos();
			resultado = EDITNEW;
		} catch (PRISMAException pe) {
			System.err.println(pe.getMessage());
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
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(proyecto, colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			agregarAtributos();
			Proyecto proyecto = SessionManager.consultarProyectoActivo();
			model.setProyecto(proyecto);
			EntidadBs.registrarEntidad(model);

			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La", "Entidad",
					"registrada" }));

			SessionManager.set(this.getActionMessages(), "mensajesAccion");
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = editNew();
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

	public String edit() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			prepararVista();
			buscaCatalogos();
			resultado = EDIT;
		} catch (PRISMAException pe) {
			System.err.println(pe.getMessage());
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	private void prepararVista() {
		List<Atributo> listAtributos = new ArrayList<Atributo>();
		for (Atributo atributo : model.getAtributos()) {
			Atributo atAux = new Atributo();
			atAux.setNombre(atributo.getNombre());
			atAux.setDescripcion(atributo.getDescripcion());
			atAux.setId(atributo.getId());
			atAux.setFormatoArchivo(atributo.getFormatoArchivo());
			atAux.setLongitud(atributo.getLongitud());
			atAux.setObligatorio(atributo.isObligatorio());
			atAux.setOtroTipoDato(atributo.getOtroTipoDato());
			atAux.setTamanioArchivo(atributo.getTamanioArchivo());
			atAux.setTipoDato(atributo.getTipoDato());
			atAux.setUnidadTamanio(atributo.getUnidadTamanio());
			listAtributos.add(atAux);
		}
		jsonAtributosTabla = JsonUtil.mapListToJSON(listAtributos);

	}

	public String update() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			model.getAtributos().clear();
			agregarAtributos();
//			Actualizacion actualizacion = new Actualizacion(new Date(),
//					comentario, model,
//					SessionManager.consultarColaboradorActivo());

//			EntidadBs.modificarEntidad(model, actualizacion);
			EntidadBs.modificarEntidad(model);

			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La", "Entidad",
					"modificada" }));

			SessionManager.set(this.getActionMessages(), "mensajesAccion");
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = edit();
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

	public String show() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			resultado = SHOW;
		} catch (PRISMAException pe) {
			pe.setIdMensaje("MSG26");
			ErrorManager.agregaMensajeError(this, pe);
			return index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			return index();
		}
		return resultado;
	}

	public String destroy() throws Exception {
		String resultado = null;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (proyecto == null) {
				resultado = "proyectos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(model.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			model.setProyecto(proyecto);
			EntidadBs.eliminarEntidad(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La", "Entidad",
					"eliminada" }));
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
		listTipoDato = EntidadBs.consultarTiposDato();
		listUnidadTamanio = EntidadBs.consultarUnidadesTamanio();
		if (listUnidadTamanio == null || listUnidadTamanio.isEmpty()) {
			throw new PRISMAException(
					"No hay unidades para registrar el atributo.", "MSG13");
		}
		if (listTipoDato == null || listTipoDato.isEmpty()) {
			throw new PRISMAException(
					"No hay tipos de dato para registrar el atributo.", "MSG13");
		}
	}

	private void agregarAtributos() {
		Set<Atributo> atributosModelo = new HashSet<Atributo>(0);
		Set<Atributo> atributosVista = new HashSet<Atributo>(0);

		Atributo atributoBD = null;
		if (jsonAtributosTabla != null && !jsonAtributosTabla.equals("")) {
			atributosVista = JsonUtil.mapJSONToSet(jsonAtributosTabla,
					Atributo.class);

			if (atributosVista != null) {

				for (Atributo atributoVista : atributosVista) {
					TipoDato tipoDato = new TipoDatoDAO()
							.consultarTipoDato(atributoVista.getTipoDato()
									.getNombre());

					if (atributoVista.getId() != null
							&& atributoVista.getId() != 0) {
						atributoBD = AtributoBs.consultarAtributo(atributoVista
								.getId());
						atributoBD.setTipoDato(tipoDato);
						atributoBD.setNombre(atributoVista.getNombre());
						atributoBD.setDescripcion(atributoVista
								.getDescripcion());
						atributoBD
								.setObligatorio(atributoVista.isObligatorio());

						if (tipoDato.getNombre().equals("Archivo")) {
							UnidadTamanio unidadTamanio = new UnidadTamanioDAO()
									.consultarUnidadTamanioAbreviatura(atributoVista
											.getUnidadTamanio()
											.getAbreviatura());
							atributoBD.setUnidadTamanio(unidadTamanio);
							atributoBD.setTamanioArchivo(atributoVista
									.getTamanioArchivo());
							atributoBD.setFormatoArchivo(atributoVista
									.getFormatoArchivo());
							atributoBD.setLongitud(null);
							atributoBD.setOtroTipoDato(null);
						} else if (tipoDato.getNombre().equals("Cadena")
								|| tipoDato.getNombre().equals("Entero")
								|| tipoDato.getNombre().equals("Flotante")) {
							atributoBD.setLongitud(atributoVista.getLongitud());
							atributoBD.setTamanioArchivo(null);
							atributoBD.setUnidadTamanio(null);
							atributoBD.setFormatoArchivo(null);
							atributoBD.setOtroTipoDato(null);
							
						} else if (tipoDato.getNombre().equals("Otro")) {
							atributoBD.setOtroTipoDato(atributoVista
									.getOtroTipoDato());
							atributoBD.setTamanioArchivo(null);
							atributoBD.setUnidadTamanio(null);
							atributoBD.setFormatoArchivo(null);
							atributoBD.setLongitud(null);
						} else if (tipoDato.getNombre().equals("Booleano") || tipoDato.getNombre().equals("Fecha")) {
							atributoBD.setTamanioArchivo(null);
							atributoBD.setUnidadTamanio(null);
							atributoBD.setFormatoArchivo(null);
							atributoBD.setLongitud(null);
							atributoBD.setOtroTipoDato(null);
						}
						atributosModelo.add(atributoBD);
					} else {
						atributoVista.setId(null);
						atributoVista.setTipoDato(tipoDato);
						if (tipoDato.getNombre().equals("Archivo")) {
							UnidadTamanio unidadTamanio = new UnidadTamanioDAO()
									.consultarUnidadTamanioAbreviatura(atributoVista
											.getUnidadTamanio()
											.getAbreviatura());
							atributoVista.setUnidadTamanio(unidadTamanio);
							atributoVista.setTamanioArchivo(atributoVista
									.getTamanioArchivo());
							atributoVista.setFormatoArchivo(atributoVista
									.getFormatoArchivo());
							atributoVista.setLongitud(null);
							atributoVista.setOtroTipoDato(null);
						} else if (tipoDato.getNombre().equals("Cadena")
								|| tipoDato.getNombre().equals("Entero")
								|| tipoDato.getNombre().equals("Flotante")) {
							atributoVista.setLongitud(atributoVista
									.getLongitud());
							atributoVista.setTamanioArchivo(null);
							atributoVista.setUnidadTamanio(null);
							atributoVista.setFormatoArchivo(null);
							atributoVista.setOtroTipoDato(null);
						} else if (tipoDato.getNombre().equals("Otro")) {
							atributoVista.setOtroTipoDato(atributoVista
									.getOtroTipoDato());
							atributoVista.setTamanioArchivo(null);
							atributoVista.setUnidadTamanio(null);
							atributoVista.setFormatoArchivo(null);
							atributoVista.setLongitud(null);
						} else if (tipoDato.getNombre().equals("Booleano") || tipoDato.getNombre().equals("Fecha")) {
							atributoVista.setTamanioArchivo(null);
							atributoVista.setUnidadTamanio(null);
							atributoVista.setFormatoArchivo(null);
							atributoVista.setLongitud(null);
							atributoVista.setOtroTipoDato(null);
						}

						atributoVista.setEntidad(model);

						atributosModelo.add(atributoVista);
					}
				}
				model.getAtributos().addAll(atributosModelo);
			}

		}

	}

	public String verificarElementosReferencias() {
		try {
			elementosReferencias = new ArrayList<String>();
			elementosReferencias = EntidadBs.verificarReferencias(model);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "referencias";
	}

	public Entidad getModel() {
		return (model == null) ? model = new Entidad() : this.model;
	}

	public void setModel(Entidad model) {
		this.model = model;
	}

	public Map<String, Object> getUserSession() {
		return userSession;
	}

	public void setUserSession(Map<String, Object> userSession) {
		this.userSession = userSession;
	}

	public List<Entidad> getListEntidades() {
		return listEntidades;
	}

	public void setListEntidades(List<Entidad> listEntidades) {
		this.listEntidades = listEntidades;
	}

	public String getJsonAtributosTabla() {
		return jsonAtributosTabla;
	}

	public void setJsonAtributosTabla(String jsonAtributosTabla) {
		this.jsonAtributosTabla = jsonAtributosTabla;
	}

	public List<TipoDato> getListTipoDato() {
		return listTipoDato;
	}

	public void setListTipoDato(List<TipoDato> listTipoDato) {
		this.listTipoDato = listTipoDato;
	}

	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public List<UnidadTamanio> getListUnidadTamanio() {
		return listUnidadTamanio;
	}

	public void setListUnidadTamanio(List<UnidadTamanio> listUnidadTamanio) {
		this.listUnidadTamanio = listUnidadTamanio;
	}

	public Integer getIdSel() {
		return idSel;
	}

	public void setIdSel(Integer idSel) {
		this.idSel = idSel;
		model = EntidadBs.consultarEntidad(idSel);
	}

	public List<String> getElementosReferencias() {
		return elementosReferencias;
	}

	public void setElementosReferencias(List<String> elementosReferencias) {
		this.elementosReferencias = elementosReferencias;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
}
