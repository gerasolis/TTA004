package mx.prisma.admin.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import mx.prisma.admin.bs.ColaboradorBs;
import mx.prisma.admin.model.Colaborador;
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
@ResultPath("/content/administrador/")
@Results({ @Result(name = ActionSupportPRISMA.SUCCESS, type = "redirectAction", params = {
		"actionName", "personal" }),
		@Result(name = "referencias", type = "json", params = {
				"root",
				"proyectosLider"})
})
public class PersonalCtrl extends ActionSupportPRISMA implements
ModelDriven<Colaborador>, SessionAware{
	private Colaborador model;
	private static final long serialVersionUID = 1L;
	private Map<String, Object> userSession;
	private List<Colaborador> listPersonal;
	private String idSel;
	private String contrasenaAnterior;
	private String correoAnterior;
	private List<String> proyectosLider;
	
	public String index() throws Exception {
		try {
			listPersonal = ColaboradorBs.consultarPersonal();
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
			ColaboradorBs.enviarCorreo(model, null, null);
			ColaboradorBs.registrarColaborador(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"Persona", "registrada" }));

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
			contrasenaAnterior = model.getContrasenia();
			correoAnterior = model.getCorreoElectronico();
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
	
	public String update() throws Exception {
		String resultado = null;
		try {
			ColaboradorBs.enviarCorreo(model, contrasenaAnterior, correoAnterior);
			ColaboradorBs.modificarColaborador(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"Persona", "modificada" }));

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
	
	public String destroy() throws Exception {
		String resultado = null;
		try {
			ColaboradorBs.eliminarColaborador(model);
			resultado = SUCCESS;
			addActionMessage(getText("MSG1", new String[] { "La",
					"Persona", "eliminada" }));
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
	
	public String verificarProyectosLider() {
		try {
			proyectosLider = ColaboradorBs.verificarProyectosLider(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "referencias";
	}
	
	public Colaborador getModel() {
		return (model == null) ? model = new Colaborador() : model;
	}
	public void setModel(Colaborador model) {
		this.model = model;
	}
	public Map<String, Object> getUserSession() {
		return userSession;
	}
	public void setUserSession(Map<String, Object> userSession) {
		this.userSession = userSession;
	}
	public List<Colaborador> getListPersonal() {
		return listPersonal;
	}
	public void setListPersonal(List<Colaborador> listPersonal) {
		this.listPersonal = listPersonal;
	}
	public String getIdSel() {
		return idSel;
	}
	public void setIdSel(String idSel) {
		this.idSel = idSel;
		model = ColaboradorBs.consultarPersona(idSel);
	}
	public void setSession(Map<String, Object> session) {
		
	}

	public String getContrasenaAnterior() {
		return contrasenaAnterior;
	}

	public void setContrasenaAnterior(String contrasenaAnterior) {
		this.contrasenaAnterior = contrasenaAnterior;
	}

	public String getCorreoAnterior() {
		return correoAnterior;
	}

	public void setCorreoAnterior(String correoAnterior) {
		this.correoAnterior = correoAnterior;
	}

	public List<String> getProyectosLider() {
		return proyectosLider;
	}

	public void setProyectosLider(List<String> proyectosLider) {
		this.proyectosLider = proyectosLider;
	}
	
	
}

