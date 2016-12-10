package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.dao.CasoUsoActorDAO;
import mx.prisma.editor.dao.ModuloDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoActor;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class ModuloBs {
	public static void registrarModulo(Modulo model)
			throws Exception {
		try {
			validar(model);
			new ModuloDAO().registrarModulo(model);
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
	}

	public static List<Modulo> consultarModulosProyecto(
			Proyecto proyecto) {
		List<Modulo> listModulos = new ModuloDAO()
				.consultarModulos(proyecto.getId());
		if (listModulos == null) {
			throw new PRISMAException(
					"No se pueden consultar los módulos.",
					"MSG13");
		}
		return listModulos;
	}

	private static void validar(Modulo model) {

		// Validaciones del nombre
	
		if (Validador.esNuloOVacio(model.getClave())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la clave del módulo.", "MSG4", null,
					"model.clave");
		}
		if (Validador.validaLongitudMaxima(model.getClave(), 10)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre muy largo.", "MSG6",
					new String[] { "200", "caracteres" }, "model.nombre");
		}
		
		if (Validador.contieneCaracterInvalido(model.getClave())) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una clave con caracter inválido.",
					"MSG23", new String[] { "La", "clave" }, "model.clave");
		}
		
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre del módulo.", "MSG4", null,
					"model.nombre");
		}
		if (Validador.validaLongitudMaxima(model.getNombre(), 200)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre muy largo.", "MSG6",
					new String[] { "200", "caracteres" }, "model.nombre");
		}
		
		// Validaciones de la Descripción
		if (Validador.esNuloOVacio(model.getDescripcion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la descripción del módulo.", "MSG4",
					null, "model.descripcion");
		}

		if (Validador.validaLongitudMaxima(model.getDescripcion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una descripción muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.descripcion");
		}
	}

	public static Modulo consultarModulo(int idActor) {
		Modulo modulo = null;
		modulo = new ModuloDAO()
				.consultarModulo(idActor);
		if (modulo == null) {
			throw new PRISMAException(
					"No se pueden consultar los módulos.",
					"MSG13");
		}
		return modulo;
	}

	public static void eliminarTermino(Modulo model) throws Exception {
		try {
			
			new ModuloDAO().eliminarModulo(model);
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}


	public static void modificarModulo(Modulo model) throws Exception {
		try {
			model.setNombre(model.getNombre().trim());
			
			validar(model);

			new ModuloDAO().modificarModulo(model);

		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	public static void eliminarModulo(Modulo model) {
		new ModuloDAO().eliminarModulo(model);
		
	}

	public static List<String> verificarReferencias(Modulo model) {


		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		
		List<CasoUso> casosUso = CuBs.consultarCasosUsoModulo(model);
		List<Pantalla> pantallas = PantallaBs.consultarPantallasModulo(model);
		
		for(CasoUso casoUso : casosUso) {
			setReferenciasVista.addAll(CuBs.verificarReferencias(casoUso, model));
		}
		
		for(Pantalla pantalla : pantallas) {
			setReferenciasVista.addAll(PantallaBs.verificarReferencias(pantalla, model));
		}
		

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

}
