package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.bs.CatalogoBs;
import mx.prisma.bs.ReferenciaEnum.TipoCatalogo;
import mx.prisma.bs.ReferenciaEnum.TipoSeccion;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.dao.VerboDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.Verbo;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class TrayectoriaBs {
	public static void registrarTrayectoria(Trayectoria model) throws Exception {
		try {
			validar(model);
			model.setClave(model.getClave().trim());
			model.setEstado("No Configurado");
			new TrayectoriaDAO().registrarTrayectoria(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException(
						"La clave de la trayectoria ya existe.", "MSG7",
						new String[] { "La", "Trayectoria", model.getClave() },
						"model.clave");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
	}

//	public static void modificarTrayectoria(Trayectoria model,
//			Actualizacion actualizacion) throws Exception {
//		try {
//			validar(model);
//			ElementoBs.verificarEstado(model.getCasoUso(),
//					CU_CasosUso.MODIFICARTRAYECTORIA5_1_1_2);
//			model.getCasoUso().setEstadoElemento(
//					ElementoBs.consultarEstadoElemento(Estado.EDICION));
//			model.setClave(model.getClave().trim());
//
//			new TrayectoriaDAO().modificarTrayectoria(model, actualizacion);
//		} catch (JDBCException je) {
//			if (je.getErrorCode() == 1062) {
//				throw new PRISMAValidacionException(
//						"La clave de la trayectoria ya existe.", "MSG7",
//						new String[] { "La", "Trayectoria", model.getClave() },
//						"model.clave");
//			}
//			System.out.println("ERROR CODE " + je.getErrorCode());
//			je.printStackTrace();
//			throw new Exception();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			throw new Exception();
//		}
//	}
	
	public static void modificarTrayectoria(Trayectoria model) throws Exception {
		try {
			validar(model);
			ElementoBs.verificarEstado(model.getCasoUso(),
					CU_CasosUso.MODIFICARTRAYECTORIA5_1_1_2);
			model.getCasoUso().setEstadoElemento(
					ElementoBs.consultarEstadoElemento(Estado.EDICION));
			model.setClave(model.getClave().trim());

			new TrayectoriaDAO().modificarTrayectoria(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException(
						"La clave de la trayectoria ya existe.", "MSG7",
						new String[] { "La", "Trayectoria", model.getClave() },
						"model.clave");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
	}

	public static void eliminarTrayectoria(Trayectoria model) throws Exception {
		try {
			ElementoBs.verificarEstado(model.getCasoUso(),
					CU_CasosUso.ELIMINARTRAYECTORIA5_1_1_3);
			new TrayectoriaDAO().eliminarTrayectoria(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1451) {
				throw new PRISMAException(
						"No se puede eliminar el caso de uso", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	private static void validar(Trayectoria model) {
		// Validaciones de la clave
		if (Validador.esNuloOVacio(model.getClave())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la clave de la trayectoria.",
					"MSG4", null, "model.clave");
		}
		if (Validador.validaLongitudMaxima(model.getClave(), 5)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una clave larga.", "MSG6",
					new String[] { "5", "caracteres" }, "model.clave");
		}
		if (model.getClave().contains(" ")) {
			throw new PRISMAValidacionException("La clave contiene espacios.",
					"MSG19", new String[] { "La", "clave" }, "model.clave");
		}
		if (Validador.contieneCaracterInvalido(model.getClave())) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una clave con caracter inválido.",
					"MSG23", new String[] { "El", "nombre" }, "model.clave");
		}

		if (!model.isAlternativa()) {
			// Si es una trayectoria principal, entonces se debe verificar que
			// no haya una registrada previamente
			Set<Trayectoria> trayectorias = model.getCasoUso()
					.getTrayectorias();
			for (Trayectoria t : trayectorias) {
				if (!t.isAlternativa() && t.getId() != model.getId()) {
					throw new PRISMAValidacionException(
							"Ya existe una trayectoria principal registrada.",
							"MSG20", null, "alternativaPrincipal");
				}
			}
		}
		// Validaciones de la condición
		if (model.isAlternativa()
				&& Validador.esNuloOVacio(model.getCondicion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la condición.", "MSG4", null,
					"model.condicion");
		}
		if (Validador.validaLongitudMaxima(model.getCondicion(), 500)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una condición muy larga.", "MSG6",
					new String[] { "500", "caracteres" }, "model.condicion");
		}

		// Validaciones de los pasos
		if (Validador.esNuloOVacio(model.getPasos())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó ningún paso.", "MSG18",
					new String[] { "un", "paso" }, "model.pasos");
		} else {
			// Si hay pasos registrados, se valida cada uno de ellos
			for (Paso p : model.getPasos()) {
				if (Validador.esNuloOVacio(p.getRedaccion())) {
					throw new PRISMAValidacionException(
							"El usuario no ingresó la redacción de un paso.",
							"MSG4");
				}
				if (Validador.validaLongitudMaxima(p.getRedaccion(), 999)) {
					throw new PRISMAValidacionException(
							"El usuario rebaso la longitud de alguno de los pasos.",
							"MSG17", new String[] { "los", "pasos", "o" },
							"model.pasos");
				}
			}
		}
	}

	public static Verbo consultaVerbo(String nombre) {

		Verbo verbo = null;
		try {
			verbo = new VerboDAO().consultarVerbo(nombre);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (verbo == null) {
			throw new PRISMAException(
					"No se puede consultar el verbo por nombre.", "MSG16",
					new String[] { "El", "verbo" });
		}
		return verbo;
	}

	public static List<String> consultarVerbos() {
		List<Verbo> lv = new VerboDAO().consultarVerbos();
		if (lv == null) {
			throw new PRISMAException("No se pueden consultar los verbos.",
					"MSG13");
		}
		CatalogoBs.opcionOtro(lv, TipoCatalogo.VERBO);

		List<String> verbos = new ArrayList<String>();
		for (Verbo v : lv) {
			verbos.add(v.getNombre());
		}
		return verbos;
	}

	public static boolean existeTrayectoriaPrincipal(int idCU) {
		if (idCU == 0) {
			idCU = (Integer) SessionManager.get("idCU");
		}
		CasoUso casoUso = CuBs.consultarCasoUso(idCU);
		for (Trayectoria t : casoUso.getTrayectorias()) {
			if (!t.isAlternativa()) {
				return true;
			}
		}
		return false;
	}

	public static boolean existeTrayectoriaPrincipal(int idCU, int idTray) {
		if (idCU == 0) {
			idCU = (Integer) SessionManager.get("idCU");
		}
		CasoUso casoUso = CuBs.consultarCasoUso(idCU);
		for (Trayectoria t : casoUso.getTrayectorias()) {
			if (!t.isAlternativa() && t.getId() != idTray) {
				return true;
			}
		}
		return false;
	}

	public static Trayectoria consultarTrayectoria(int id) {
		Trayectoria trayectoria = null;
		try {
			trayectoria = new TrayectoriaDAO().consultarTrayectoria(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (trayectoria == null) {
			throw new PRISMAException(
					"No se puede consultar la trayectoria por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		return trayectoria;
	}

	public static void preAlmacenarObjetosToken(Trayectoria trayectoria) {
		Set<Paso> pasos = trayectoria.getPasos();

		for (Paso paso : pasos) {
			TokenBs.almacenarObjetosToken(
					TokenBs.convertirToken_Objeto(paso.getRedaccion(),
							trayectoria.getCasoUso().getProyecto()),
					TipoSeccion.PASOS, paso);
			paso.setRedaccion(TokenBs.codificarCadenaToken(paso.getRedaccion(),
					trayectoria.getCasoUso().getProyecto()));

		}
	}

	public static List<String> verificarReferencias(Trayectoria model, Modulo modulo) {

		List<ReferenciaParametro> referenciasParametro = new ArrayList<ReferenciaParametro>();

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";
		Integer idSelf = null;

		referenciasParametro = new ReferenciaParametroDAO().consultarReferenciasParametro(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

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
				idSelf = paso.getTrayectoria().getId();
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

			if (linea != "" && idSelf != model.getId()) {
				setReferenciasVista.add(linea);
			}
		}

		/*for (Paso pasoModel : model.getPasos()) {
			setReferenciasVista.addAll(PasoBs.verificarReferencias(pasoModel, modulo));
		}*/

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

	public static boolean isListado(List<Integer> enteros, Integer entero) {
		for (Integer i : enteros) {
			if (i == entero) {
				return true;
			}
		}
		return false;
	}
	
	public static void modificarEstado(Trayectoria trayectoria){
		try{
			System.out.println("Trayectoria de guardar de Bs: " + trayectoria.getCondicion());

			new TrayectoriaDAO().modificarEstado(trayectoria);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static List<Trayectoria> consultarTrayectoriaxCasoUso(CasoUso idCaso){
		Trayectoria trayectoria = null;
		List<Trayectoria> listTrayectoria=null;
		try{
			//trayectoria = new TrayectoriaDAO().consultarTrayectoriaxCasoUso(idCaso);
			listTrayectoria = new TrayectoriaDAO().consultarTrayectoriaxCasoUso(idCaso);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (listTrayectoria == null) {
			throw new PRISMAException(
					"No se puede consultar la trayectoria por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		return listTrayectoria;
	}
	
	public static List<Paso> obtenerPasos(Integer id){
		Trayectoria trayectoria = null;
		List<Paso> listPasos=null;
		try{
			//trayectoria = new TrayectoriaDAO().consultarTrayectoriaxCasoUso(idCaso);
			listPasos = new TrayectoriaDAO().obtenerPasos(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (listPasos == null) {
			throw new PRISMAException(
					"No se pueden consultar los pasos por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		return listPasos;
	}
	public static List<Paso> obtenerPasos_(Integer id){
		Trayectoria trayectoria = null;
		List<Paso> listPasos=null;
		Set<Paso> listaPasos=new HashSet<Paso>();
		try{
			//trayectoria = new TrayectoriaDAO().consultarTrayectoriaxCasoUso(idCaso);
			listPasos = new TrayectoriaDAO().obtenerPasos_(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (listPasos == null) {
			throw new PRISMAException(
					"No se pueden consultar los pasos por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		
		return listPasos;
	}

}
