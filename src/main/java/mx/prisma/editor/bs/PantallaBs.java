package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AnalisisEnum.CU_Pantallas;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.dao.AccionDAO;
import mx.prisma.editor.dao.PantallaDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.TipoAccionDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.TipoAccion;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class PantallaBs {

	private static final String CLAVE = "IU";

	public static List<Pantalla> consultarPantallasModulo(Modulo modulo) {
		List<Pantalla> listPantallas = new PantallaDAO()
				.consultarPantallasModulo(modulo);
		if (listPantallas == null) {
			throw new PRISMAException("No se pueden consultar las pantallas.",
					"MSG13");
		}
		return listPantallas;
	}

	public static void registrarPantalla(Pantalla model) throws Exception {
		try {
			validar(model);
			model.setClave(CLAVE + model.getModulo().getClave());
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			model.setNombre(model.getNombre().trim());
			new PantallaDAO().registrarElemento(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException("La pantalla "
						+ model.getNombre() + " ya existe.", "MSG7",
						new String[] { "La", "Pantalla", model.getNombre() },
						"model.nombre");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	private static void validar(Pantalla model) {
		// Validaciones del número
		if (Validador.esNuloOVacio(model.getNumero())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el número de la pantalla.", "MSG4", null,
					"model.numero");
		}
		if (!Pattern.matches("[0-9]+(\\.[0-9]+)*", model.getNumero())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el número de la pantalla.", "MSG5",
					new String[] { "un", "número" }, "model.numero");
		}
		// Se asegura la unicidad del nombre y del numero
		List<Pantalla> pantallas = consultarPantallasModulo(model.getModulo());
		for (Pantalla p : pantallas) {
			if (p.getId() != model.getId()) {
				if (p.getNombre().equals(model.getNombre())) {
					throw new PRISMAValidacionException(
							"El nombre de la pantalla ya existe.", "MSG7",
							new String[] { "La", "Pantalla",
									model.getNombre() }, "model.nombre");
				}
				if (p.getNumero().equals(model.getNumero())) {
					throw new PRISMAValidacionException(
							"El numero de la pantalla ya existe.", "MSG7",
							new String[] { "La", "Pantalla",
									model.getNumero() }, "model.numero");
				}
			}
		}

		// Validaciones del nombre
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre de la pantalla.", "MSG4", null,
					"model.nombre");
		}
		if (Validador.validaLongitudMaxima(model.getNombre(), 200)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre muy largo.", "MSG6",
					new String[] { "200", "caracteres" }, "model.nombre");
		}
		if (Validador.contieneCaracterInvalido(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre con caracter inválido.",
					"MSG23", new String[] { "El", "nombre" }, "model.nombre");
		}
		// Validaciones de la Descripción
		if(Validador.esNuloOVacio(model.getDescripcion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la descripción de la pantalla.", "MSG4", null,
					"model.descripcion");
		}
		if (Validador.validaLongitudMaxima(model.getDescripcion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una descripción muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.descripcion");
		}

	}

	public static Pantalla consultarPantalla(Integer idSel) {
		Pantalla p = null;
		try {
			p = new PantallaDAO().consultarPantalla(idSel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (p == null) {
			throw new PRISMAException(
					"No se puede consultar la pantalla por el id.", "MSG16",
					new String[] { "La", "pantalla" });
		}
		return p;
	}

	public static List<TipoAccion> consultarTiposAccion() {
		List<TipoAccion> listTiposAccion = new TipoAccionDAO()
				.consultarTiposAccion();
		if (listTiposAccion == null) {
			throw new PRISMAException(
					"No se pueden consultar los tipos de acción.", "MSG13");
		}
		return listTiposAccion;
	}

	public static List<Pantalla> consultarPantallasProyecto(Proyecto proyecto) {
		List<Elemento> listPantallasAux = new PantallaDAO().consultarElementos(
				ReferenciaEnum.TipoReferencia.PANTALLA, proyecto.getId());
		List<Pantalla> listPantallas = new ArrayList<Pantalla>();
		if (listPantallasAux == null) {
			throw new PRISMAException(
					"No se pueden consultar las pantallas por proyecto.",
					"MSG13");
		}
		for (Elemento elem : listPantallasAux) {
			listPantallas.add((Pantalla) elem);
		}

		return listPantallas;
	}

	public static TipoAccion consultarTipoAccion(Integer id) {
		TipoAccion ta = new TipoAccionDAO().consultarTipoAccion(id);
		if (ta == null) {
			throw new PRISMAException(
					"No se puede consultar el tipo de accion por el id.",
					"MSG13");
		}
		return ta;
	}

	public static void eliminarPantalla(Pantalla model) throws Exception {
		try {
			ElementoBs.verificarEstado(model, CU_Pantallas.ELIMINARPANTALLA6_3);
			new PantallaDAO().eliminarElemento(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1451) {
				throw new PRISMAException("No se puede eliminar la pantalla",
						"MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	public static List<String> verificarReferencias(Pantalla model, Modulo modulo) {

		List<ReferenciaParametro> referenciasParametro = new ArrayList<ReferenciaParametro>();
		List<Accion> referenciasAccion = new ArrayList<Accion>();

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);

		PostPrecondicion postPrecondicion = null; // Origen de la referencia
		Paso paso = null; // Origen de la referencia
		Accion accion = null; // Origen de la referencia
		String casoUso = ""; // Caso de uso que tiene la referencia
		String pantalla = ""; // Pantalla que tiene la referencia

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasAccion = new AccionDAO().consultarReferencias(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();
			accion = referencia.getAccionDestino();

			if (postPrecondicion != null && (modulo == null || postPrecondicion.getCasoUso().getModulo().getId() != modulo.getId())) {
				casoUso = postPrecondicion.getCasoUso().getClave()
						+ postPrecondicion.getCasoUso().getNumero() + " "
						+ postPrecondicion.getCasoUso().getNombre();
				if (postPrecondicion.isPrecondicion()) {
					linea = "Precondiciones del caso de uso " + casoUso;
				} else {
					linea = "Postcondiciones del caso de uso "
							+ postPrecondicion.getCasoUso().getClave()
							+ postPrecondicion.getCasoUso().getNumero() + " "
							+ postPrecondicion.getCasoUso().getNombre();
				}

			} else if (paso != null && (modulo == null || paso.getTrayectoria().getCasoUso().getModulo().getId() != modulo.getId())) {
				casoUso = paso.getTrayectoria().getCasoUso().getClave()
						+ paso.getTrayectoria().getCasoUso().getNumero() + " "
						+ paso.getTrayectoria().getCasoUso().getNombre();
				linea = "Paso "
						+ paso.getNumero()
						+ " de la trayectoria "
						+ ((paso.getTrayectoria().isAlternativa()) ? "alternativa "
								+ paso.getTrayectoria().getClave()
								: "principal") + " del caso de uso " + casoUso;
			} else if (accion != null && (modulo == null || accion.getPantalla().getModulo().getId() != modulo.getId())) {
				if (accion.getPantalla() != model) {
					pantalla = accion.getPantalla().getClave()
							+ accion.getPantalla().getNumero() + " "
							+ accion.getPantalla().getNombre();
					linea = "Acción " + accion.getNombre() + " de la pantalla "
							+ pantalla;
				}
			}

			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}
		for(Accion accionOP : referenciasAccion) {
			if(modulo == null || accionOP.getPantalla().getModulo().getId() != modulo.getId()) {
				String linea = "";
				pantalla = accionOP.getPantalla().getClave()
						+ accionOP.getPantalla().getNumero() + " "
						+ accionOP.getPantalla().getNombre();
				linea = "Acción " + accionOP.getNombre() + " de la pantalla "
						+ pantalla; 
				if (linea != "") {
					setReferenciasVista.add(linea);
				}
			}
		}

		for (Accion acc : model.getAcciones()) {
			setReferenciasVista.addAll(AccionBs.verificarReferencias(acc, modulo));
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
	
	public static List<CasoUso> verificarCasosUsoReferencias(Pantalla model) {

		List<ReferenciaParametro> referenciasParametro = new ArrayList<ReferenciaParametro>();

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);

		PostPrecondicion postPrecondicion = null; // Origen de la referencia
		Paso paso = null; // Origen de la referencia
		Accion accion = null; // Origen de la referencia
		String casoUso = ""; // Caso de uso que tiene la referencia
		String pantalla = ""; // Pantalla que tiene la referencia

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();
			accion = referencia.getAccionDestino();

			if (postPrecondicion != null) {
				setReferenciasVista.add(postPrecondicion.getCasoUso());

			} else if (paso != null) {
				setReferenciasVista.add(paso.getTrayectoria().getCasoUso());
			}
		}

		for (Accion acc : model.getAcciones()) {
			setReferenciasVista.addAll(AccionBs.verificarCasosUsoReferencias(acc));
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

//	public static void modificarPantalla(Pantalla model,
//			Actualizacion actualizacion) throws Exception {
//		try {
//			validar(model);
//			ElementoBs
//					.verificarEstado(model, CU_Pantallas.MODIFICARPANTALLA6_2);
//			model.setEstadoElemento(ElementoBs
//					.consultarEstadoElemento(Estado.EDICION));
//			model.setNombre(model.getNombre().trim());
//
//			new PantallaDAO().modificarPantalla(model, actualizacion);
//
//		} catch (JDBCException je) {
//			System.out.println("ERROR CODE " + je.getErrorCode());
//			je.printStackTrace();
//			throw new Exception();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			throw new Exception();
//		}
//
//	}
	public static void modificarPantalla(Pantalla model) throws Exception {
		try {
			validar(model);
			ElementoBs
					.verificarEstado(model, CU_Pantallas.MODIFICARPANTALLA6_2);
			model.setClave(CLAVE + model.getModulo().getClave());
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			model.setNombre(model.getNombre().trim());

			new PantallaDAO().modificarPantalla(model);

		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	public static void modificarPatronPantalla(Pantalla pantalla, boolean validarObligatorios) {
		String patron = pantalla.getPatron();
		if (validarObligatorios && Validador.esNuloOVacio(patron)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó algún patrón de pantalla.", "MSG38", null,
					"campos");
		}
		if (Validador.validaLongitudMaxima(patron, 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un patrón de pantalla muy largo.", "MSG39",
					new String[] { "999", "caracteres", "el patrón de la pantalla " + pantalla.getClave() + pantalla.getNumero() + " " + pantalla.getNombre() }, "campos");
		}
		
		new PantallaDAO().modificarPantalla(pantalla);
	}
}
