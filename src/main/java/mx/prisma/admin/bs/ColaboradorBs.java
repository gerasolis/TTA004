package mx.prisma.admin.bs;

import java.util.ArrayList; 
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import mx.prisma.admin.dao.ColaboradorDAO;
import mx.prisma.admin.dao.ColaboradorProyectoDAO;
import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.bs.RolBs;
import mx.prisma.bs.RolBs.Rol_Enum;
import mx.prisma.util.Correo;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class ColaboradorBs {

	public static Colaborador consultarPersona(String idSel) {
		Colaborador col = new ColaboradorDAO().consultarColaborador(idSel);
		if(col == null) {
			throw new PRISMAException("No se puede consultar el colaborador.",
					"MSG13");
		}
		return col;
	}

	public static List<Colaborador> consultarPersonal() {
		List<Colaborador> colaboradores = new ColaboradorDAO().consultarColaboradores();
		if(colaboradores == null) {
			throw new PRISMAException("No se pueden consultar los colaboradores.",
					"MSG13");
		}
		return colaboradores;
	}

	public static void registrarColaborador(Colaborador model) throws Exception {
		try {
			validar(model);
			new ColaboradorDAO().registrarColaborador(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException("La Persona con CURP"
						+ model.getCurp() + " ya existe.", "MSG7",
						new String[] { "La", "persona con CURP", model.getCurp() },
						"model.curp");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
		
	}

	private static void validar(Colaborador model) {
		//Validaciones del CURP
		if (Validador.esNuloOVacio(model.getCurp())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la CURP del colaborador.", "MSG4",
					null, "model.curp");
		}
		if (Validador.validaLongitudMaxima(model.getCurp(), 18)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una CURP muy larga.", "MSG6",
					new String[] { "18", "caracteres" }, "model.curp");
		}
		// Validaciones del nombre
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre del colaborador.", "MSG4",
					null, "model.nombre");
		}
		if (Validador.validaLongitudMaxima(model.getNombre(), 45)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre muy largo.", "MSG6",
					new String[] { "45", "caracteres" }, "model.nombre");
		}
		// Validaciones de los apellidos
		if (Validador.esNuloOVacio(model.getApellidoPaterno())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el apellido paterno del colaborador.", "MSG4",
					null, "model.apellidoPaterno");
		}
		if (Validador.validaLongitudMaxima(model.getApellidoPaterno(), 45)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un apellido paterno muy largo.", "MSG6",
					new String[] { "45", "caracteres" }, "model.apellidoPaterno");
		}
		if (Validador.validaLongitudMaxima(model.getApellidoMaterno(), 45)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un apellido materno muy largo.", "MSG6",
					new String[] { "45", "caracteres" }, "model.apellidoMaterno");
		}
		// Validaciones del correo
		if (Validador.esNuloOVacio(model.getCorreoElectronico())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el correo del colaborador.", "MSG4",
					null, "model.correoElectronico");
		}
		if (Validador.validaLongitudMaxima(model.getCorreoElectronico(), 45)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un correo muy largo.", "MSG6",
					new String[] { "45", "caracteres" }, "model.correoElectronico");
		}
		Colaborador colaboradorBD = new ColaboradorDAO().consultarColaboradorCorreo(model.getCorreoElectronico());
		if(colaboradorBD != null && !colaboradorBD.getCurp().equals(model.getCurp())) {
			throw new PRISMAValidacionException(
					"El correo del colaborador ya existe.",
					"MSG7",
					new String[] { "El", "Correo electrónico", model.getCorreoElectronico() },
					"model.correoElectronico");
		}
		
		// Validaciones de la contraseña
		if (Validador.esNuloOVacio(model.getContrasenia())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la contraseña del colaborador.", "MSG4",
					null, "model.contrasenia");
		}
		if (Validador.validaLongitudMaxima(model.getContrasenia(), 20)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una contraseña muy larga.", "MSG6",
					new String[] { "20", "caracteres" }, "model.contrasenia");
		}
		
	}

	public static void enviarCorreo(Colaborador model,
			String contrasenaAnterior, String correoAnterior) throws AddressException, MessagingException {
		if(contrasenaAnterior == null || correoAnterior == null) {
			//Correo.enviarCorreo(model, 0);
			System.out.println("Se envió un correo al usuario que se registró.");
		} else if(!contrasenaAnterior.equals(model.getContrasenia())) {
			Correo.enviarCorreo(model, 0);
			System.out.println("Se envió un correo porque cambió la contraseña.");
		} else if(!correoAnterior.equals(model.getCorreoElectronico())) {
			Correo.enviarCorreo(model, 0);
			System.out.println("Se envió un correo porque cambio el correo electrónico.");
		}
	}

	public static void modificarColaborador(Colaborador model) throws Exception {
		try {
			validar(model);
			new ColaboradorDAO().modificarColaborador(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException("La Persona con CURP"
						+ model.getCurp() + " ya existe.", "MSG7",
						new String[] { "La", "persona con CURP", model.getCurp() },
						"model.curp");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	public static void eliminarColaborador(Colaborador model) throws Exception {
		try {
			if(!esLiderProyecto(model)) {
				new ColaboradorDAO().eliminarColaborador(model);
			} else {
				throw new PRISMAException("No se puede eliminar la persona.", "MSG13");
			}
			
		} catch (JDBCException je) {
			if(je.getErrorCode() == 1451)
			{
				throw new PRISMAException("No se puede eliminar la persona.", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	public static boolean esLiderProyecto(Colaborador model) {
		Set<ColaboradorProyecto> colaboradoresProyecto = model.getColaborador_proyectos();
		int idLider = RolBs.consultarIdRol(Rol_Enum.LIDER);
		for(ColaboradorProyecto cp : colaboradoresProyecto) {
			if(cp.getRol().getId() == idLider) {
				return true;
			}
		}
		return false;
	}

	public static List<String> verificarProyectosLider(Colaborador model) {
		int idLider = RolBs.consultarIdRol(Rol_Enum.LIDER);
		List<String> proyectos = new ArrayList<String>();
		Set<String> setProyectos = new HashSet<String>(0);
		
		List<ColaboradorProyecto> colaboradoresProyecto = null;
		colaboradoresProyecto = new ColaboradorProyectoDAO().consultarLiderColaboradoresProyecto(model);
		
		for(ColaboradorProyecto cp : colaboradoresProyecto) {
			if(cp.getRol().getId() == idLider) {
				String linea = "";
				String proyecto = cp.getProyecto().getClave() + " " + cp.getProyecto().getNombre();
				linea = "Esta persona es líder del Proyecto " + proyecto + ".";
				setProyectos.add(linea);
			}
		}
		
		proyectos.addAll(setProyectos);
		return proyectos;
	}
}
