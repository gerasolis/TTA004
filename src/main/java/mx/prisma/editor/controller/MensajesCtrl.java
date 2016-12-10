package mx.prisma.editor.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.AnalisisEnum.CU_Mensajes;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.bs.MensajeBs;
import mx.prisma.editor.model.Actualizacion;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Parametro;
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
				"actionName", "mensajes" }),
		@Result(name = "parametros", type = "json", params = { "root",
				"listParametros" }),
		@Result(name = "referencias", type = "json", params = { "root",
				"elementosReferencias" }),
		@Result(name = "proyectos", type = "redirectAction", params = {
				"actionName", "proyectos" }) })
public class MensajesCtrl extends ActionSupportPRISMA implements
		ModelDriven<Mensaje>, SessionAware {
	private static final long serialVersionUID = 1L;

	private Proyecto proyecto;
	private Modulo modulo;
	private Mensaje model;
	private Colaborador colaborador;
	private List<Mensaje> listMensajes;

	private String jsonParametros;
	private String jsonParametrosGuardados;
	private String cambioRedaccion;
	private boolean parametrizado = false;
	private Integer idSel;
	private boolean existenParametros;
	private String comentario;
	private List<String> elementosReferencias;
	private String redaccionMensaje;
	private List<Parametro> listParametros;

	public String index() {
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
			listMensajes = MensajeBs.consultarMensajesProyecto(proyecto);

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

	public String editNew() {
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
			buscarParametrosDisponibles(proyecto.getId());
			model.setClave("MSG");
			resultado = EDITNEW;
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

	public String create() {
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
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento((Estado.EDICION)));

			agregarParametros();

			MensajeBs.registrarMensaje(model);
			resultado = SUCCESS;

			addActionMessage(getText("MSG1", new String[] { "El", "Mensaje",
					"registrado" }));

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

	public String edit() {
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
			ElementoBs.verificarEstado(model, CU_Mensajes.MODIFICARMENSAJE9_2);
			buscarParametrosDisponibles(proyecto.getId());
			prepararVista();

			resultado = EDIT;
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

	public String update() {
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
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento((Estado.EDICION)));

			model.getParametros().clear();
			agregarParametros();

//			Actualizacion actualizacion = new Actualizacion(new Date(),
//					comentario, model,
//					SessionManager.consultarColaboradorActivo());
//			MensajeBs.modificarMensaje(model, actualizacion);
			MensajeBs.modificarMensaje(model);
			resultado = SUCCESS;

			addActionMessage(getText("MSG1", new String[] { "El", "Mensaje",
					"modificado" }));

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
			this.existenParametros = model.getParametros().size() > 0 ? true
					: false;
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
			MensajeBs.eliminarMensaje(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "El", "Mensaje",
					"eliminado" }));
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

	public String verificarElementosReferencias() {
		try {
			elementosReferencias = new ArrayList<String>();
			elementosReferencias = MensajeBs.verificarReferencias(model);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "referencias";
	}

	private void buscarParametrosDisponibles(int idProyecto) {
		List<Parametro> listParametrosAux = MensajeBs
				.consultarParametros(idProyecto);
		List<Parametro> listParametros = new ArrayList<Parametro>();
		for (Parametro par : listParametrosAux) {
			Parametro parametroAux = new Parametro();
			parametroAux.setNombre(par.getNombre());
			parametroAux.setDescripcion(par.getDescripcion());
			listParametros.add(parametroAux);
		}

		// Se convierte en json las Reglas de Negocio
		if (listParametros != null) {
			this.jsonParametrosGuardados = JsonUtil
					.mapListToJSON(listParametros);
		}

	}

	private void prepararVista() {
		ArrayList<Parametro> parametrosVista = new ArrayList<Parametro>();
		Parametro parametroAux = null;
		if (cambioRedaccion == null) {
			cambioRedaccion = "false";
		}

		if (jsonParametros == null || jsonParametros.isEmpty()) {
			for (MensajeParametro parametro : model.getParametros()) {
				parametroAux = new Parametro(parametro.getParametro()
						.getNombre(), parametro.getParametro().getDescripcion());
				parametrosVista.add(parametroAux);
			}
			this.jsonParametros = JsonUtil.mapListToJSON(parametrosVista);
		}

	}

	private void agregarParametros() throws Exception {
		model.setParametrizado(true);
		if (jsonParametros != null && !jsonParametros.equals("")) {
			Set<Parametro> parametros = JsonUtil.mapJSONToSet(jsonParametros,
					Parametro.class);

			for (Parametro p : parametros) {
				Parametro parametroAux = MensajeBs.consultarParametro(p
						.getNombre(), SessionManager.consultarProyectoActivo()
						.getId());
				if (parametroAux != null) {
					// Si existe, debe mantener los cambios hechos en la vista
					parametroAux.setDescripcion(p.getDescripcion());
					model.getParametros().add(
							new MensajeParametro(model, parametroAux));
				} else {
					p.setProyecto(proyecto);
					MensajeParametro nuevoParametro = new MensajeParametro(
							model, p);
					nuevoParametro.setId(null);
					model.getParametros().add(nuevoParametro);
				}
			}
		}
		System.out
				.println("Model params size: " + model.getParametros().size());
	}

	public String verificarParametros() {
		listParametros = new ArrayList<Parametro>();
		try {
			proyecto = SessionManager.consultarProyectoActivo();
			if (MensajeBs.esParametrizado(redaccionMensaje)) {
				listParametros = MensajeBs.obtenerParametros(redaccionMensaje,
						proyecto.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "parametros";
	}

	public void setSession(Map<String, Object> session) {
	}

	public Mensaje getModel() {
		if (model == null) {
			model = new Mensaje();
		}
		return model;
	}

	public List<Mensaje> getListMensajes() {
		return listMensajes;
	}

	public void setListMensajes(List<Mensaje> listMensajes) {
		this.listMensajes = listMensajes;
	}

	public void setModel(Mensaje model) {
		this.model = model;
	}

	public String getCambioRedaccion() {
		return cambioRedaccion;
	}

	public void setCambioRedaccion(String cambioRedaccion) {
		this.cambioRedaccion = cambioRedaccion;
	}

	public String getJsonParametros() {
		return jsonParametros;
	}

	public void setJsonParametros(String jsonParametros) {
		this.jsonParametros = jsonParametros;
	}

	public String getJsonParametrosGuardados() {
		return jsonParametrosGuardados;
	}

	public void setJsonParametrosGuardados(String jsonParametrosGuardados) {
		this.jsonParametrosGuardados = jsonParametrosGuardados;
	}

	public boolean isParametrizado() {
		return parametrizado;
	}

	public void setParametrizado(boolean parametrizado) {
		this.parametrizado = parametrizado;
	}

	public Integer getIdSel() {
		return idSel;
	}

	public void setIdSel(Integer idSel) {
		this.idSel = idSel;
		this.model = MensajeBs.consultarMensaje(idSel);

	}

	public boolean isExistenParametros() {
		return existenParametros;
	}

	public void setExistenParametros(boolean existenParametros) {
		this.existenParametros = existenParametros;
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

	public List<Parametro> getListParametros() {
		return listParametros;
	}

	public void setListParametros(List<Parametro> listParametros) {
		this.listParametros = listParametros;
	}

	public String getRedaccionMensaje() {
		return redaccionMensaje;
	}

	public void setRedaccionMensaje(String redaccionMensaje) {
		this.redaccionMensaje = redaccionMensaje;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
}
