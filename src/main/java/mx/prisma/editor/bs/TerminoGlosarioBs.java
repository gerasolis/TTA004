package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AnalisisEnum.CU_Glosario;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.dao.EntradaDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.SalidaDAO;
import mx.prisma.editor.dao.TerminoGlosarioDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.Salida;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class TerminoGlosarioBs {
	private static final String CLAVE = "GLS";

	public static void registrarTerminoGlosario(TerminoGlosario model)
			throws Exception {
		try {
			validar(model);
			model.setClave(CLAVE);
			model.setNumero(new TerminoGlosarioDAO()
					.siguienteNumeroTerminoGlosario(model.getProyecto().getId()));
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			new TerminoGlosarioDAO().registrarTerminoGlosario(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException("El termino "
						+ model.getNombre() + " ya existe.", "MSG7",
						new String[] { "El", "termino", model.getNombre() },
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

	public static List<TerminoGlosario> consultarTerminosGlosarioProyecto(
			Proyecto proyecto) {
		List<TerminoGlosario> listTerminosGlosario = new TerminoGlosarioDAO()
				.consultarTerminosGlosario(proyecto.getId());
		if (listTerminosGlosario == null) {
			throw new PRISMAException(
					"No se pueden consultar los terminos del glosario.",
					"MSG13");
		}
		return listTerminosGlosario;
	}

	private static void validar(TerminoGlosario model) {

		// Validaciones del nombre
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre del término.", "MSG4", null,
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
		// Se asegura la unicidad del nombre
		List<TerminoGlosario> terminos = TerminoGlosarioBs.consultarTerminosGlosarioProyecto(model.getProyecto());
		for(TerminoGlosario termino : terminos) {
			if(termino.getId() != model.getId()) {
				if(termino.getNombre().equals(model.getNombre())) {
					throw new PRISMAValidacionException(
							"El nombre del caso de uso ya existe.",
							"MSG7",
							new String[] { "El", "Término", model.getNombre() },
							"model.nombre");
				}
			}
		}
		// Validaciones de la Descripción
		if (Validador.esNuloOVacio(model.getDescripcion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la descripción del término.", "MSG4",
					null, "model.descripcion");
		}

		if (Validador.validaLongitudMaxima(model.getDescripcion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una descripcion muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.descripcion");
		}
	}

	public static TerminoGlosario consultarTerminoGlosario(int idActor) {
		TerminoGlosario terminoGlosario = null;
		terminoGlosario = new TerminoGlosarioDAO()
				.consultarTerminoGlosario(idActor);
		if (terminoGlosario == null) {
			throw new PRISMAException(
					"No se pueden consultar los terminos del glosario.",
					"MSG13");
		}
		return terminoGlosario;
	}

	public static void eliminarTermino(TerminoGlosario model) throws Exception {
		try {
			ElementoBs.verificarEstado(model, CU_Glosario.ELIMINARTERMINO10_3);
			new TerminoGlosarioDAO().eliminarElemento(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1451) {
				throw new PRISMAException(
						"No se puede eliminar el término.", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	public static List<String> verificarReferencias(TerminoGlosario model) {

		List<ReferenciaParametro> referenciasParametro;
		List<Salida> referenciasSalida;
		List<Entrada> referenciasEntrada;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasSalida = new SalidaDAO().consultarReferencias(model);
		referenciasEntrada = new EntradaDAO().consultarReferencias(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
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

			} else if (paso != null) {
				casoUso = paso.getTrayectoria().getCasoUso().getClave()
						+ paso.getTrayectoria().getCasoUso().getNumero() + " "
						+ paso.getTrayectoria().getCasoUso().getNombre();
				linea = "Paso "
						+ paso.getNumero()
						+ " de la trayectoria "
						+ ((paso.getTrayectoria().isAlternativa()) ? "alternativa "
								+ paso.getTrayectoria().getClave()
								: "principal") + " del caso de uso " + casoUso;
			}
			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}

		for (Salida salida : referenciasSalida) {
			String linea = "";
			casoUso = salida.getCasoUso().getClave()
					+ salida.getCasoUso().getNumero() + " "
					+ salida.getCasoUso().getNombre();
			linea = "Salidas del caso de uso " + casoUso;
			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}

		for (Entrada entrada : referenciasEntrada) {
			String linea = "";
			casoUso = entrada.getCasoUso().getClave()
					+ entrada.getCasoUso().getNumero() + " "
					+ entrada.getCasoUso().getNombre();
			linea = "Entradas del caso de uso " + casoUso;
			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
	
	public static List<CasoUso> verificarCasosUsoReferencias(TerminoGlosario model) {

		List<ReferenciaParametro> referenciasParametro;
		List<Salida> referenciasSalida;
		List<Entrada> referenciasEntrada;

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasSalida = new SalidaDAO().consultarReferencias(model);
		referenciasEntrada = new EntradaDAO().consultarReferencias(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
				setReferenciasVista.add(postPrecondicion.getCasoUso());
			} else if (paso != null) {
				setReferenciasVista.add(paso.getTrayectoria().getCasoUso());
			}
		}

		for (Salida salida : referenciasSalida) {
			setReferenciasVista.add(salida.getCasoUso());
		}

		for (Entrada entrada : referenciasEntrada) {
			setReferenciasVista.add(entrada.getCasoUso());
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

//	public static void modificarTerminoGlosario(TerminoGlosario model,
//			Actualizacion actualizacion) throws Exception {
//		try {
//			validar(model);
//			ElementoBs
//					.verificarEstado(model, CU_Glosario.MODIFICARTERMINO10_2);
//			model.setEstadoElemento(ElementoBs
//					.consultarEstadoElemento(Estado.EDICION));
//			model.setNombre(model.getNombre().trim());
//
//			new TerminoGlosarioDAO().modificarElemento(model, actualizacion);
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
	public static void modificarTerminoGlosario(TerminoGlosario model) throws Exception {
		try {
			validar(model);
			ElementoBs
					.verificarEstado(model, CU_Glosario.MODIFICARTERMINO10_2);
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			model.setNombre(model.getNombre().trim());

			new TerminoGlosarioDAO().modificarElemento(model);

		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

}
