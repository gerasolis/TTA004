package mx.prisma.controller;

import java.util.Collection;
import java.util.Map;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.model.Modulo;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;

@InterceptorRef(value = "defaultStack")
@Results({
		@Result(name = "administrador", type = "redirectAction", params = {
				"actionName", "proyectos-admin" }),
		@Result(name = "colaborador", type = "redirectAction", params = {
				"actionName", "proyectos" }),
		@Result(name = "recover", type = "dispatcher", location = "recover.jsp") })
public class AccessCtrl extends ActionSupportPRISMA implements SessionAware {
	/** 
	 * 
	 */ 
	private static final long serialVersionUID = 1L;
	private Map<String, Object> userSession;
	private String userName;
	private String password;

	public String index() {
		System.out.println("Entramos a index");
		String resultado = INDEX;
		try {
			if (SessionManager.isLogged()) {
				if (SessionManager.consultarColaboradorActivo()
						.isAdministrador()) {
					resultado = "administrador";
				} else {
					resultado = "colaborador";
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
			e.printStackTrace();
		}
		return resultado;
	}

	public String login() throws Exception {
		System.out.println("Entramos a login");
		String resultado = null;
		Colaborador colaborador = null;
		Map<String, Object> session = null;
		try {
			if (userSession != null) {
				userSession.clear();
			}
			colaborador = AccessBs.verificarLogin(userName, password);
			session = ActionContext.getContext().getSession();
			session.put("login", true);
			session.put("colaboradorCURP", colaborador.getCurp());
			setSession(session);
			if (SessionManager.consultarColaboradorActivo().isAdministrador()) {
				resultado = "administrador";
			} else {
				resultado = "colaborador";
			}

		} catch (PRISMAValidacionException pve) {
			System.out.println("Uno");
			ErrorManager.agregaMensajeError(this, pve);
			System.out.println("Pve: "+pve);
			return index();
		} catch (PRISMAException pe) {
			System.out.println("dos");
			System.out.println("Pe: "+pe);
			ErrorManager.agregaMensajeError(this, pe);
		} catch (Exception e) {
			System.out.println("E: "+e);
			System.out.println("tres");
			ErrorManager.agregaMensajeError(this, e);
		}
		return resultado;
	}

	public String logout() {
		if (userSession != null) {
			userSession.clear();
		}
		return index();
	}

	public String recover() {
		String resultado = null;
		try {
			resultado = "recover";
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = recover();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public String sendPassword() {
		String resultado = null;
		try {
			AccessBs.recuperarContrasenia(userName);
			resultado = INDEX;
			addActionMessage(getText("MSG32"));

			SessionManager.set(this.getActionMessages(), "mensajesAccion");

		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			resultado = recover();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			resultado = index();
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			resultado = index();
		}
		return resultado;
	}

	public static String getMenu() throws Exception {
		Proyecto proyecto = SessionManager.consultarProyectoActivo();
		Colaborador colaborador = SessionManager.consultarColaboradorActivo();
		if (colaborador != null && colaborador.isAdministrador()) {
			return "administrador/menus/menuAdministrador";
		} else if (proyecto == null) {
			return "editor/menus/menuAnalista";
		} else {
			return "editor/menus/menuAnalistaProyecto";
		}
	}
	
	public static String getRol() throws Exception {
		Proyecto proyecto = SessionManager.consultarProyectoActivo();
		Colaborador colaborador = SessionManager.consultarColaboradorActivo();
		
		for (ColaboradorProyecto colaboradorProyecto : proyecto.getProyecto_colaboradores()) {
			if (colaboradorProyecto.getColaborador().getCurp().equals(colaborador.getCurp())) {
				return colaboradorProyecto.getRol().getId() + "";
			}
		}
		
		return "";
	}

	public static Proyecto getInfoProyecto() throws Exception {
		Proyecto proyecto = null;
		proyecto = SessionManager.consultarProyectoActivo();
		return proyecto;
	}

	public static Modulo getInfoModulo() throws Exception {
		Modulo modulo = null;
		modulo = SessionManager.consultarModuloActivo();
		return modulo;
	}
	
	public void setSession(Map<String, Object> session) {
		this.userSession = session;
	}

	public Map<String, Object> getUserSession() {
		return userSession;
	}

	public void setUserSession(Map<String, Object> userSession) {
		this.userSession = userSession;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
