package mx.prisma.bs;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import mx.prisma.admin.dao.ColaboradorDAO;
import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.util.Correo;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class AccessBs {

	public static Colaborador verificarLogin(String userName, String password) {
		Colaborador colaborador = null;
		if (Validador.esNuloOVacio(userName)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el correo electrónico", "MSG4", null,
					"userName");
		}
		
		if (Validador.esNuloOVacio(password)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la contraseña.", "MSG4", null,
					"password");
		}
		
		try {
			colaborador = new ColaboradorDAO().consultarColaboradorCorreo(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (colaborador == null || !colaborador.getContrasenia().equals(password)) {
			throw new PRISMAValidacionException("Colaborador no encontrado o contraseña incorrecta", "MSG31");
		}
		return colaborador; 
		
	}

	public static boolean isLogged(Map<String, Object> userSession) {
		boolean logged = false;
		if (userSession != null) {
			if (userSession.get("login") != null) {
				logged = (Boolean) userSession.get("login");
				System.out.println(logged);
				return logged;
			}
		} 
		return false;
	}

	public static void recuperarContrasenia(String userName) throws AddressException, MessagingException {
		Colaborador colaborador = null;
		if (Validador.esNuloOVacio(userName)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el correo electrónico", "MSG4", null,
					"userName");
		}
		if (!Validador.esCorreo(userName)) {
			throw new PRISMAValidacionException("Colaborador no encontrado", "MSG33");

		}
		try {
			colaborador = new ColaboradorDAO().consultarColaboradorCorreo(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (colaborador == null) {
			throw new PRISMAValidacionException("Colaborador no encontrado", "MSG33");
		}
		Correo.enviarCorreo(colaborador, 1);
		
	}
	
	public static boolean verificarPermisos(Proyecto proyecto, Colaborador colaborador) {
		boolean acceso = false;
		for (ColaboradorProyecto colaboradorProyecto : colaborador.getColaborador_proyectos()) {
			if (colaboradorProyecto.getProyecto().getId() == proyecto.getId()) {
				acceso = true;
				break;
			}
		}
		return acceso;
	}
}
