package mx.prisma.admin.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import mx.prisma.admin.bs.ColaboradorBs;
import mx.prisma.admin.bs.ProyectoBs;
import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.EstadoProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

@ResultPath("/content/administrador/")
@Results({ @Result(name = ActionSupportPRISMA.SUCCESS, type = "redirectAction", params = {
		"actionName", "proyectos-admin" })
})
@Conversion(
	    conversions = {
	         // key must be the name of a property for which converter should be used
	         @TypeConversion(key = "model.fechaInicio", converter = "mx.prisma.util.StrutsDateConverter"),
	         @TypeConversion(key = "model.fechaTermino", converter = "mx.prisma.util.StrutsDateConverter"),
	         @TypeConversion(key = "model.fechaInicioProgramada", converter = "mx.prisma.util.StrutsDateConverter"),
	         @TypeConversion(key = "model.fechaTerminoProgramada", converter = "mx.prisma.util.StrutsDateConverter")
	    }
	)
public class ProyectosAdminCtrl extends ActionSupportPRISMA implements
ModelDriven<Proyecto>, SessionAware{
	private Proyecto model;
	private static final long serialVersionUID = 1L;
	private Map<String, Object> userSession;
	private List<Proyecto> listProyectos;
	private List<EstadoProyecto> listEstadosProyecto;
	private List<Colaborador> listPersonas;
	private Integer idSel;
	private int idEstadoProyecto;
	private String curpLider;
	
	public String index() throws Exception {
		try {
			listProyectos = ProyectoBs.consultarProyectos();
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
			buscarCatalogos();
			resultado = EDITNEW;
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}
	
	private void buscarCatalogos() {
		listEstadosProyecto = ProyectoBs.consultarEstadosProyectoRegistro();
		listPersonas = ColaboradorBs.consultarPersonal();
	}
	
	private void buscarCatalogosModificacion() {
		listEstadosProyecto = ProyectoBs.consultarEstadosProyecto();
		listPersonas = ColaboradorBs.consultarPersonal();
	}

	public String create() throws Exception {
		String resultado = null;
		try {
			if(curpLider.equals("-1")) {
				throw new PRISMAValidacionException("El usuario no seleccionó el lider del proyecto.", "MSG4", null, "curpLider");
			}
			if(idEstadoProyecto == -1) {
				throw new PRISMAValidacionException("El usuario no seleccionó el estado del proyecto.", "MSG4", null, "idEstadoProyecto");
			}
			
			ProyectoBs.agregarEstado(model, idEstadoProyecto);
			ProyectoBs.agregarLider(model, curpLider);
			ProyectoBs.registrarProyecto(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "El",
					"Proyecto", "registrado" }));

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
			buscarCatalogosModificacion();
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

	private void prepararVista() {
		idEstadoProyecto = model.getEstadoProyecto().getId();
		curpLider = ProyectoBs.consultarColaboradorProyectoLider(model).getColaborador().getCurp();
	}

	public String update() throws Exception {
		String resultado = null;
		try {
			if(curpLider.equals("-1")) {
				throw new PRISMAValidacionException("El usuario no seleccionó el lider del proyecto.", "MSG4", null, "curpLider");
			}
			if(idEstadoProyecto == -1) {
				throw new PRISMAValidacionException("El usuario no seleccionó el estado del proyecto.", "MSG4", null, "idEstadoProyecto");
			}

			ProyectoBs.agregarEstado(model, idEstadoProyecto);
			ProyectoBs.agregarLider(model, curpLider);
			
			ProyectoBs.modificarProyecto(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "El",
					"Proyecto", "modificado" }));

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
			ProyectoBs.eliminarProyecto(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "El",
					"Proyecto", "eliminado" }));
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
	
	public Proyecto getModel() {
		return (model == null) ? model = new Proyecto() : model;
	}
	public void setModel(Proyecto model) {
		this.model = model;
	}
	public Map<String, Object> getUserSession() {
		return userSession;
	}
	public void setUserSession(Map<String, Object> userSession) {
		this.userSession = userSession;
	}
	public List<Proyecto> getListProyectos() {
		return listProyectos;
	}
	public void setListProyectos(List<Proyecto> listProyectos) {
		this.listProyectos = listProyectos;
	}
	public Integer getIdSel() {
		return idSel;
	}
	public void setIdSel(Integer idSel) {
		this.idSel = idSel;
		model = ProyectoBs.consultarProyecto(idSel);
	}
	public void setSession(Map<String, Object> session) {
		
	}

	public List<EstadoProyecto> getListEstadosProyecto() {
		return listEstadosProyecto;
	}

	public void setListEstadosProyecto(List<EstadoProyecto> listEstadosProyecto) {
		this.listEstadosProyecto = listEstadosProyecto;
	}

	public int getIdEstadoProyecto() {
		return idEstadoProyecto;
	}

	public void setIdEstadoProyecto(int idEstadoProyecto) {
		this.idEstadoProyecto = idEstadoProyecto;
	}

	public String getCurpLider() {
		return curpLider;
	}

	public void setCurpLider(String curpLider) {
		this.curpLider = curpLider;
	}

	public List<Colaborador> getListPersonas() {
		return listPersonas;
	}

	public void setListPersonas(List<Colaborador> listPersonas) {
		this.listPersonas = listPersonas;
	}
	
	

}
