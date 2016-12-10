package mx.prisma.editor.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.AnalisisEnum.CU_ReglasNegocio;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.editor.bs.AtributoBs;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.bs.EntidadBs;
import mx.prisma.editor.bs.ReglaNegocioBs;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Operador;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TipoReglaNegocio;
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
				"actionName", "reglas-negocio" }),
		@Result(name = "atributos", type = "json", params = { "root",
				"listAtributos" }),
		@Result(name = "entidades", type = "json", params = { "root",
				"listEntidades" }),
		@Result(name = "operadores", type = "json", params = { "root",
				"listOperadores" }),
		@Result(name = "referencias", type = "json", params = { "root",
				"elementosReferencias" }),
		@Result(name = "proyectos", type = "redirectAction", params = {
				"actionName", "proyectos" }) })
public class ReglasNegocioCtrl extends ActionSupportPRISMA implements
		ModelDriven<ReglaNegocio>, SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Proyecto proyecto;
	private ReglaNegocio model;
	private Colaborador colaborador;
	private Modulo modulo;

	private List<ReglaNegocio> listReglasNegocio;
	private List<TipoReglaNegocio> listTipoRN;
	private int idTipoRN;
	private String jsonAtributos;
	private String jsonEntidades;
	private String jsonAtributos2;
	private String jsonEntidades2;
	private String jsonOperadores;
	private List<Entidad> listEntidades;
	private List<Atributo> listAtributos;
	private List<Entidad> listEntidades2;
	private List<Atributo> listAtributos2;
	private List<Operador> listOperadores;

	private int idAtributo;
	private int idEntidad;

	private int idEntidadUnicidad;
	private int idAtributoUnicidad;

	private int idAtributoFormato;
	private int idEntidadFormato;
	
	private int idAtributo1;
	private int idEntidad1;
	private int idAtributo2;
	private int idEntidad2;
	private int idOperador;
	private List<String> elementosReferencias;

	private boolean esEliminable;

	private String comentario;

	private Integer idSel;

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
			listReglasNegocio = ReglaNegocioBs
					.consultarReglasNegocioProyecto(proyecto);

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
			model.setClave("RN");
			buscaCatalogos();
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
			if (idTipoRN == -1) {
				throw new PRISMAValidacionException(
						"El usuario no seleccionó el tipo de regla de negocio.",
						"MSG4", null, "idTipoRN");
			}
			model.setTipoReglaNegocio(ReglaNegocioBs
					.consultaTipoReglaNegocio(idTipoRN));
			
			buscarElementosListas();

			TipoReglaNegocio trn = model.getTipoReglaNegocio();

			switch (TipoReglaNegocioEnum.getTipoReglaNegocio(trn)) {
			case COMPATRIBUTOS:
				ReglaNegocioBs.validarReglaCompAtributos(idEntidad1, idAtributo1, idOperador, idEntidad2, idAtributo2);
				model = ReglaNegocioBs.agregarElementosComparacion(model,
						idAtributo1, idOperador, idAtributo2);
				break;
			case FORMATOCAMPO:
				ReglaNegocioBs.validarReglaNegocioFormatoCampo(idEntidadFormato, idAtributoFormato);
				model = ReglaNegocioBs.agregarElementosFormatoCampo(model,
						idAtributoFormato);
				break;
			case UNICIDAD:
				ReglaNegocioBs.validarReglaNegocioUnicidad(idEntidadUnicidad, idAtributoUnicidad);
				model = ReglaNegocioBs.agregarElementosUnicidad(model,
						idEntidadUnicidad, idAtributoUnicidad);
				break;
			default:
				break;
			}
			

			// Se prepara el modelo para el registro
			proyecto = SessionManager.consultarProyectoActivo();
			model.setProyecto(proyecto);
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));

			// Se registra la regla de negocio
			ReglaNegocioBs.registrarReglaNegocio(model);
			resultado = SUCCESS;

			// Se agrega mensaje de éxito
			addActionMessage(getText("MSG1", new String[] { "La",
					"Regla de negocio", "registrada" }));

			// Se agrega el mensaje a la sesión
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
			ElementoBs.verificarEstado(model,
					CU_ReglasNegocio.MODIFICARREGLANEGOCIO8_2);

			buscaCatalogos();
			prepararVista();
			buscarElementosListas();
			

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

	private void prepararVista() {
		TipoReglaNegocio trn = model.getTipoReglaNegocio();
		idTipoRN = trn.getId();
		switch (TipoReglaNegocioEnum.getTipoReglaNegocio(trn)) {
		case COMPATRIBUTOS:
			idEntidad1 = model.getAtributoComp1().getEntidad().getId();
			idAtributo1 = model.getAtributoComp1().getId();
			idOperador = model.getOperadorComp().getId();
			idEntidad2 = model.getAtributoComp2().getEntidad().getId();
			idAtributo2 = model.getAtributoComp2().getId();
			break;
		case FORMATOCAMPO:
			idEntidadFormato = model.getAtributoExpReg().getEntidad().getId();
			idAtributoFormato = model.getAtributoExpReg().getId();
			break;
		case UNICIDAD:
			idEntidadUnicidad = model.getAtributoUnicidad().getEntidad().getId();
			idAtributoUnicidad = model.getAtributoUnicidad().getId();
			break;
		default:
			break;

		}

	}

	private void buscarElementosListas() {
		TipoReglaNegocio trn = model.getTipoReglaNegocio();
		idTipoRN = trn.getId();
		List<Atributo> listAtributosAux = null;
		List<Entidad> listEntidadesAux = null;
		switch (TipoReglaNegocioEnum.getTipoReglaNegocio(trn)) {
		case COMPATRIBUTOS:
			Entidad entidad1 = null;
			Atributo atributo1 = null;
			Entidad entidad2 = null;
			
			if(idEntidad1 != 0 && idEntidad1 != -1) {
				entidad1 = EntidadBs.consultarEntidad(idEntidad1);
			}
			
			if(idAtributo1 != 0 && idAtributo1 != -1) {
				atributo1 = AtributoBs.consultarAtributo(idAtributo1);
			}
			
			if(idEntidad2 != 0 && idEntidad2 != -1) {
				entidad2 = EntidadBs.consultarEntidad(idEntidad2);
			}
			
			listEntidadesAux = EntidadBs.consultarEntidadesProyecto(proyecto);
			listEntidades = new ArrayList<Entidad>();
			for(Entidad e : listEntidadesAux) {
				e.setAtributos(null);
				e.setProyecto(null);
				listEntidades.add(e);
			}
			jsonEntidades = JsonUtil.mapListToJSON(listEntidades);
			
			if(entidad1 != null) {
				listAtributosAux = new ArrayList<Atributo>(entidad1.getAtributos());
				listAtributos = new ArrayList<Atributo>();
				for(Atributo a : listAtributosAux) {
					a.setEntidad(null);
					listAtributos.add(a);
				}
				jsonAtributos = JsonUtil.mapListToJSON(listAtributos);
			}
			
			if(atributo1 != null) {
				listOperadores = ReglaNegocioBs
						.consultarOperadoresDisponibles(atributo1.getTipoDato()
								.getNombre());
				jsonOperadores = JsonUtil.mapListToJSON(listOperadores);
				
				List<Entidad> listEntidades2Aux = ReglaNegocioBs.consultarEntidadesTipoDato(
						proyecto, atributo1.getTipoDato()
								.getNombre());
				listEntidades2 = new ArrayList<Entidad>();
				for(Entidad e2 : listEntidades2Aux) {
					e2.setAtributos(null);
					e2.setProyecto(null);
					listEntidades2.add(e2);
				}
				jsonEntidades2 = JsonUtil.mapListToJSON(listEntidades2);
				
			}
			
			if(entidad2 != null && atributo1 != null) {
				List<Atributo> listAtributos2Aux = ReglaNegocioBs.consultarAtributosTipoDato(
						entidad2, atributo1.getTipoDato()
								.getNombre());
				listAtributos2 = new ArrayList<Atributo>();
				for(Atributo a2 : listAtributos2Aux) {
					a2.setEntidad(null);
					listAtributos2.add(a2);
				}
				
			}
			
			break;
		case FORMATOCAMPO:
			Entidad entidadFormato = null;
			
			if(idEntidadFormato != 0 && idEntidadFormato != -1) {
				entidadFormato = EntidadBs.consultarEntidad(idEntidadFormato);
			}
			
			listEntidadesAux = EntidadBs.consultarEntidadesProyecto(proyecto);
			listEntidades = new ArrayList<Entidad>();
			for(Entidad e : listEntidadesAux) {
				e.setAtributos(null);
				e.setProyecto(null);
				listEntidades.add(e);
			}
			jsonEntidades = JsonUtil.mapListToJSON(listEntidades);
			
			if(entidadFormato != null) {
				listAtributosAux = new ArrayList<Atributo>(entidadFormato.getAtributos());
				listAtributos = new ArrayList<Atributo>();
				for(Atributo a : listAtributosAux) {
					a.setEntidad(null);
					listAtributos.add(a);
				}
				jsonAtributos = JsonUtil.mapListToJSON(listAtributos);
			}
			break;
		case UNICIDAD:
			Entidad entidadUnicidad = null;
			
			if(idEntidadUnicidad != 0 && idEntidadUnicidad != -1) {
				entidadUnicidad = EntidadBs.consultarEntidad(idEntidadUnicidad);
			}
			
			listEntidadesAux = EntidadBs.consultarEntidadesProyecto(proyecto);
			listEntidades = new ArrayList<Entidad>();
			for(Entidad e : listEntidadesAux) {
				e.setAtributos(null);
				e.setProyecto(null);
				listEntidades.add(e);
			}
			jsonEntidades = JsonUtil.mapListToJSON(listEntidades);
			
			if(entidadUnicidad != null) {
				listAtributosAux = new ArrayList<Atributo>(entidadUnicidad.getAtributos());
				listAtributos = new ArrayList<Atributo>();
				for(Atributo a : listAtributosAux) {
					a.setEntidad(null);
					listAtributos.add(a);
				}
				jsonAtributos = JsonUtil.mapListToJSON(listAtributos);
			}
			break;
		default:
			break;

		}
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

			if (idTipoRN == -1) {
				throw new PRISMAValidacionException(
						"El usuario no seleccionó el tipo de regla de negocio.",
						"MSG4", null, "idTipoRN");
			}

			model.setTipoReglaNegocio(ReglaNegocioBs
					.consultaTipoReglaNegocio(idTipoRN));
			TipoReglaNegocio trn = model.getTipoReglaNegocio();

			switch (TipoReglaNegocioEnum.getTipoReglaNegocio(trn)) {
			case COMPATRIBUTOS:
				ReglaNegocioBs.validarReglaCompAtributos(idEntidad1, idAtributo1, idOperador, idEntidad2, idAtributo2);
				model = ReglaNegocioBs.agregarElementosComparacion(model,
						idAtributo1, idOperador, idAtributo2);
				break;
			case FORMATOCAMPO:
				ReglaNegocioBs.validarReglaNegocioFormatoCampo(idEntidadFormato, idAtributoFormato);
				model = ReglaNegocioBs.agregarElementosFormatoCampo(model,
						idAtributoFormato);
				break;
			case UNICIDAD:
				ReglaNegocioBs.validarReglaNegocioUnicidad(idEntidadUnicidad, idAtributoUnicidad);
				model = ReglaNegocioBs.agregarElementosUnicidad(model,
						idEntidadUnicidad, idAtributoUnicidad);
				break;
			default:
				break;
			}

//			Actualizacion actualizacion = new Actualizacion(new Date(),
//					comentario, model,
//					SessionManager.consultarColaboradorActivo());
//
//			ReglaNegocioBs.modificarReglaNegocio(model, actualizacion);
			ReglaNegocioBs.modificarReglaNegocio(model);
			resultado = SUCCESS;

			addActionMessage(getText("MSG1", new String[] { "La",
					"Regla de negocio", "modificada" }));
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

	public String destroy() {
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
			ReglaNegocioBs.eliminarReglaNegocio(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"Regla de negocio", "eliminada" }));
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

	public String cargarEntidades() {
		try {
			proyecto = SessionManager.consultarProyectoActivo();
			List<Entidad> listEntidadesAux = EntidadBs
					.consultarEntidadesProyecto(proyecto);
			listEntidades = new ArrayList<Entidad>();
			for (Entidad en : listEntidadesAux) {
				en.setProyecto(null);
				listEntidades.add(en);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "entidades";
	}

	public String cargarEntidadesDependientes() {
		try {
			proyecto = SessionManager.consultarProyectoActivo();
			Atributo atributo = AtributoBs.consultarAtributo(this.idAtributo);
			List<Entidad> listEntidadesAux = ReglaNegocioBs
					.consultarEntidadesTipoDato(proyecto, atributo
							.getTipoDato().getNombre());
			listEntidades = new ArrayList<Entidad>();
			for (Entidad en : listEntidadesAux) {
				en.setProyecto(null);
				listEntidades.add(en);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "entidades";
	}

	public String cargarAtributos() {
		try {
			Entidad entidad = EntidadBs.consultarEntidad(this.idEntidad);
			ArrayList<Atributo> listAtributosAux = new ArrayList<Atributo>(
					entidad.getAtributos());
			listAtributos = new ArrayList<Atributo>();
			for (Atributo at : listAtributosAux) {
				at.setEntidad(null);
				listAtributos.add(at);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "atributos";
	}

	public String cargarAtributosDependientes() {
		try {
			Atributo atributo = AtributoBs.consultarAtributo(this.idAtributo);
			String tipoDato = atributo.getTipoDato().getNombre();
			Entidad entidad = EntidadBs.consultarEntidad(idEntidad);
			ArrayList<Atributo> listAtributosAux = new ArrayList<Atributo>(
					ReglaNegocioBs.consultarAtributosTipoDato(entidad,
							tipoDato));
			listAtributos = new ArrayList<Atributo>();
			for (Atributo at : listAtributosAux) {
				at.setEntidad(null);
				listAtributos.add(at);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "atributos";
	}

	public String cargarOperadores() {
		try {
			Atributo atributo = AtributoBs.consultarAtributo(this.idAtributo);
			listOperadores = ReglaNegocioBs
					.consultarOperadoresDisponibles(atributo.getTipoDato()
							.getNombre());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "operadores";
	}

	private void buscaCatalogos() {
		listTipoRN = ReglaNegocioBs.consultarTipoRNDisponibles(proyecto,
				model.getTipoReglaNegocio());
		listOperadores = new ArrayList<Operador>();
		
		listEntidades = new ArrayList<Entidad>();
		listAtributos = new ArrayList<Atributo>();
		listEntidades2 = new ArrayList<Entidad>();
		listAtributos2 = new ArrayList<Atributo>();
	}

	public String verificarElementosReferencias() {
		try {
			elementosReferencias = new ArrayList<String>();
			elementosReferencias = ReglaNegocioBs.verificarReferencias(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "referencias";
	}

	public void setSession(Map<String, Object> session) {
	}

	public ReglaNegocio getModel() {
		if (model == null) {
			model = new ReglaNegocio();
		}
		return model;
	}

	public List<ReglaNegocio> getListReglasNegocio() {
		return listReglasNegocio;
	}

	public void setListReglasNegocio(List<ReglaNegocio> listReglasNegocio) {
		this.listReglasNegocio = listReglasNegocio;
	}

	public List<TipoReglaNegocio> getListTipoRN() {
		return listTipoRN;
	}

	public void setListTipoRN(List<TipoReglaNegocio> listTipoRN) {
		this.listTipoRN = listTipoRN;
	}

	public void setModel(ReglaNegocio model) {
		this.model = model;
	}

	public int getIdTipoRN() {
		return idTipoRN;
	}

	public void setIdTipoRN(int idTipoRN) {
		this.idTipoRN = idTipoRN;
	}

	public int getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(int idEntidad) {
		this.idEntidad = idEntidad;
	}

	public String getJsonAtributos() {
		return jsonAtributos;
	}

	public void setJsonAtributos(String jsonAtributos) {
		this.jsonAtributos = jsonAtributos;
	}

	public String getJsonEntidades() {
		return jsonEntidades;
	}

	public void setJsonEntidades(String jsonEntidades) {
		this.jsonEntidades = jsonEntidades;
	}

	public List<Entidad> getListEntidades() {
		return listEntidades;
	}

	public void setListEntidades(List<Entidad> listEntidades) {
		this.listEntidades = listEntidades;
	}

	public List<Atributo> getListAtributos() {
		return listAtributos;
	}

	public void setListAtributos(List<Atributo> listAtributos) {
		this.listAtributos = listAtributos;
	}

	public List<Operador> getListOperadores() {
		return listOperadores;
	}

	public void setListOperadores(List<Operador> listOperadores) {
		this.listOperadores = listOperadores;
	}

	public int getIdEntidadUnicidad() {
		return idEntidadUnicidad;
	}

	public void setIdEntidadUnicidad(int idEntidadUnicidad) {
		this.idEntidadUnicidad = idEntidadUnicidad;
	}

	public int getIdAtributoUnicidad() {
		return idAtributoUnicidad;
	}

	public void setIdAtributoUnicidad(int idAtributoUnicidad) {
		this.idAtributoUnicidad = idAtributoUnicidad;
	}

	public int getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(int idAtributo) {
		this.idAtributo = idAtributo;
	}

	public int getIdAtributoFormato() {
		return idAtributoFormato;
	}

	public void setIdAtributoFormato(int idAtributoFormato) {
		this.idAtributoFormato = idAtributoFormato;
	}

	public int getIdAtributo2() {
		return idAtributo2;
	}

	public void setIdAtributo2(int idAtributo2) {
		this.idAtributo2 = idAtributo2;
	}

	public int getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(int idOperador) {
		this.idOperador = idOperador;
	}

	public int getIdAtributo1() {
		return idAtributo1;
	}

	public void setIdAtributo1(int idAtributo1) {
		this.idAtributo1 = idAtributo1;
	}

	public Integer getIdSel() {
		return idSel;
	}

	public void setIdSel(Integer idSel) {
		this.idSel = idSel;
		this.model = ReglaNegocioBs.consultaReglaNegocio(this.idSel);
	}

	public int getDefaultIdEntidadUnicidad() {
		return model.getAtributoUnicidad().getEntidad().getId();
	}

	public int getDefaultIdAtributoUnicidad() {
		return model.getAtributoUnicidad().getId();
	}

	public boolean isEsEliminable() {
		return esEliminable;
	}

	public void setEsEliminable(boolean esEliminable) {
		this.esEliminable = esEliminable;
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

	public List<Entidad> getListEntidades2() {
		return listEntidades2;
	}

	public void setListEntidades2(List<Entidad> listEntidades2) {
		this.listEntidades2 = listEntidades2;
	}

	public List<Atributo> getListAtributos2() {
		return listAtributos2;
	}

	public void setListAtributos2(List<Atributo> listAtributos2) {
		this.listAtributos2 = listAtributos2;
	}

	public int getIdEntidad1() {
		return idEntidad1;
	}

	public void setIdEntidad1(int idEntidad1) {
		this.idEntidad1 = idEntidad1;
	}

	public int getIdEntidad2() {
		return idEntidad2;
	}

	public void setIdEntidad2(int idEntidad2) {
		this.idEntidad2 = idEntidad2;
	}

	public int getIdEntidadFormato() {
		return idEntidadFormato;
	}

	public void setIdEntidadFormato(int idEntidadFormato) {
		this.idEntidadFormato = idEntidadFormato;
	}
	
	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public String getJsonAtributos2() {
		return jsonAtributos2;
	}

	public void setJsonAtributos2(String jsonAtributos2) {
		this.jsonAtributos2 = jsonAtributos2;
	}

	public String getJsonEntidades2() {
		return jsonEntidades2;
	}

	public void setJsonEntidades2(String jsonEntidades2) {
		this.jsonEntidades2 = jsonEntidades2;
	}

	public String getJsonOperadores() {
		return jsonOperadores;
	}

	public void setJsonOperadores(String jsonOperadores) {
		this.jsonOperadores = jsonOperadores;
	}
	
	
	
}
