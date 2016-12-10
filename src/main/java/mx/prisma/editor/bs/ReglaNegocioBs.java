package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AnalisisEnum.CU_ReglasNegocio;
import mx.prisma.bs.CatalogoBs;
import mx.prisma.bs.ReferenciaEnum.TipoCatalogo;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.dao.CasoUsoReglaNegocioDAO;
import mx.prisma.editor.dao.EntidadDAO;
import mx.prisma.editor.dao.OperadorDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.dao.TipoReglaNegocioDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Operador;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TipoReglaNegocio;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class ReglaNegocioBs {
	private static final String CLAVE = "RN";

	public static List<ReglaNegocio> consultarReglasNegocioProyecto(
			Proyecto proyecto) {
		List<ReglaNegocio> listReglasNegocio = new ReglaNegocioDAO()
				.consultarReglasNegocio(proyecto.getId());
		if (listReglasNegocio == null) {
			throw new PRISMAException(
					"No se pueden consultar las reglas de negocio.", "MSG13");
		}
		return listReglasNegocio;
	}

	public static List<TipoReglaNegocio> consultarTipoRN() {
		List<TipoReglaNegocio> listTipoRN = new TipoReglaNegocioDAO()
				.consultarTiposReglaNegocio();
		if (listTipoRN == null) {
			throw new PRISMAException(
					"No se pueden consultar los tipos de regla de negocio.",
					"MSG13");
		}
		return listTipoRN;
	}

	public static void registrarReglaNegocio(ReglaNegocio model)
			throws Exception {
		try {
			validar(model);
			model.setClave(CLAVE);
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			model.setNombre(model.getNombre().trim());
			new ReglaNegocioDAO().registrarReglaNegocio(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException(
						"El nombre de la regla de negocio ya existe.", "MSG7",
						new String[] { "La", "Regla de negocio",
								model.getNombre() }, "model.nombre");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	private static void validar(ReglaNegocio model) {
		// Validaciones del número
		if (Validador.esNuloOVacio(model.getNumero())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el número de la regla de negocio.",
					"MSG4", null, "model.numero");
		}
		if (!Pattern.matches("[1-9]+[0-9]*", model.getNumero())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el número de la regla de negocio.",
					"MSG5", new String[] { "un", "número entero" },
					"model.numero");
		}

		// Se asegura la unicidad del nombre y del numero
		List<ReglaNegocio> reglasNegocio = consultarReglasNegocioProyecto(model
				.getProyecto());
		for (ReglaNegocio rn : reglasNegocio) {
			if (rn.getId() != model.getId()) {
				if (rn.getNombre().equals(model.getNombre())) {
					throw new PRISMAValidacionException(
							"El nombre de la regla de negocio ya existe.",
							"MSG7", new String[] { "La", "Regla de negocio",
									rn.getNombre() }, "model.nombre");
				}
				if (rn.getNumero().equals(model.getNumero())) {
					throw new PRISMAValidacionException(
							"El numero de la regla de negocio ya existe.",
							"MSG7", new String[] { "La", "Regla de negocio",
									rn.getNumero() }, "model.numero");
				}
			}
		}

		// Validaciones del nombre
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre de la regla de negocio.",
					"MSG4", null, "model.nombre");
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
		
		// Validaciones de la Redacción
		if (Validador.esNuloOVacio(model.getRedaccion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la redaccion de la regla de negocio.", "MSG4",
					null, "model.redaccion");
		}
		if (Validador.validaLongitudMaxima(model.getRedaccion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una redaccion muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.redaccion");
		}
		if(TipoReglaNegocioEnum.getTipoReglaNegocio(model.getTipoReglaNegocio()).equals(TipoReglaNegocioEnum.TipoReglaNegocioENUM.FORMATOCAMPO)) {
			if(Validador.esNuloOVacio(model.getExpresionRegular())) {
				throw new PRISMAValidacionException(
						"El usuario no ingresó la expresión regular.", "MSG4",
						null, "model.expresionRegular");
			}
		}
	}

	public static TipoReglaNegocio consultaTipoReglaNegocio(int idTipoRN) {
		TipoReglaNegocio tipoRN = new TipoReglaNegocioDAO()
				.consultarTipoReglaNegocio(idTipoRN);
		if (tipoRN == null) {
			throw new PRISMAException(
					"No se puede consulta la regla de negocio.", "MSG13");
		}
		return tipoRN;
	}

	public static List<Operador> consultarOperadores() {
		List<Operador> listOperadores = new OperadorDAO().consultarOperadores();
		if (listOperadores == null) {
			throw new PRISMAException("No se pueden consultar los operadores.",
					"MSG13");
		}
		return listOperadores;
	}

	public static ReglaNegocio agregarElementosUnicidad(ReglaNegocio model,
			int idEntidad, int idAtributo) {
		Entidad entidad = new EntidadDAO().consultarEntidad(idEntidad);
		if (entidad == null) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó la entidad.", "MSG4", null,
					"idEntidad");
		}
		Set<Atributo> atributos = entidad.getAtributos();
		if (idAtributo == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el nombre del atributo.", "MSG4",
					null, "idAtributo");
		}
		for (Atributo atributo : atributos) {
			if (atributo.getId() == idAtributo) {
				model.setAtributoUnicidad(atributo);
				break;
			}
		}
		return model;
	}

	public static String getClave() {
		return CLAVE;
	}

	public static List<Operador> consultarOperadoresDisponibles(String tipoDato) {
		List<Operador> listOperadores = new ArrayList<Operador>();
		if (tipoDato.equals("Flotante") || tipoDato.equals("Entero") || tipoDato.equals("Fecha")) {
			listOperadores = consultarOperadores();
		} else {
			listOperadores.add(consultarOperadorSimbolo("!="));
			listOperadores.add(consultarOperadorSimbolo("="));
		}

		return listOperadores;
	}

	private static Operador consultarOperadorSimbolo(String simbolo) {
		Operador operador = new OperadorDAO().consultarOperadorSimbolo(simbolo);
		if (operador == null) {
			throw new PRISMAException("No se puede consultar el operador.",
					"MSG13");
		}
		return operador;
	}

	public static List<Entidad> consultarEntidadesTipoDato(Proyecto proyecto,
			String tipoDato) {
		List<Entidad> listEntidadesAux = EntidadBs
				.consultarEntidadesProyecto(proyecto);
		List<Entidad> listEntidades = new ArrayList<Entidad>();
		for (Entidad en : listEntidadesAux) {
			if (contieneAtributosTipoDato(en, tipoDato)) {
				listEntidades.add(en);
			}
		}
		return listEntidades;
	}

	private static boolean contieneAtributosTipoDato(Entidad en, String tipoDato) {
		Set<Atributo> atributos = en.getAtributos();
		for (Atributo at : atributos) {

			if (at.getTipoDato().getNombre().equals(tipoDato)) {
				return true;
			} else if (at.getTipoDato().getNombre().equals("Entero")
					&& tipoDato.equals("Flotante")) {
				return true;
			} else if (at.getTipoDato().getNombre().equals("Flotante")
					&& tipoDato.equals("Entero")) {
				return true;
			}
		}
		return false;
	}

	public static List<Atributo> consultarAtributosTipoDato(Entidad entidad,
			String tipoDato) {
		Set<Atributo> atributos = entidad.getAtributos();
		List<Atributo> listAtributos = new ArrayList<Atributo>();
		for (Atributo at : atributos) {
			if (at.getTipoDato().getNombre().equals(tipoDato)) {
				listAtributos.add(at);
			}
		}
		return listAtributos;
	}

	public static List<TipoReglaNegocio> consultarTipoRNDisponibles(
			Proyecto proyecto, TipoReglaNegocio tipoReglaNegocioActual) {
		List<TipoReglaNegocio> tiposRNAux = consultarTipoRN();
		List<TipoReglaNegocio> tiposRN = new ArrayList<TipoReglaNegocio>();
		for (TipoReglaNegocio trn : tiposRNAux) {
			if (esGlobal(trn)) {
				if (!existeTipoRN(proyecto, trn)) {
					tiposRN.add(trn);
				} else {
					if (tipoReglaNegocioActual != null
							&& trn.getId() == tipoReglaNegocioActual.getId()) {
						tiposRN.add(trn);
					}
				}
			} else {
				tiposRN.add(trn);
			}

		}

		CatalogoBs.opcionOtro(tiposRN, TipoCatalogo.TIPOREGLANEGOCIO);
		return tiposRN;
	}

	private static boolean existeTipoRN(Proyecto proyecto, TipoReglaNegocio trn) {
		List<ReglaNegocio> listReglasNegocio = new ReglaNegocioDAO()
				.consultarReglasNegocio(proyecto.getId());
		for (ReglaNegocio rn : listReglasNegocio) {
			if (rn.getTipoReglaNegocio().getNombre().equals(trn.getNombre())) {
				return true;
			}
		}
		return false;
	}

	public static ReglaNegocio agregarElementosComparacion(ReglaNegocio model,
			int idAtributo1, int idOperador, int idAtributo2) {
		if(idAtributo1 == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el atributo 1 de la comparación.",
					"MSG4", null, "model.numero");
		}
		
		model.setAtributoComp1(AtributoBs.consultarAtributo(idAtributo1));
		model.setOperadorComp(consultarOperador(idOperador));
		model.setAtributoComp2(AtributoBs.consultarAtributo(idAtributo2));
		return model;
	}

	private static Operador consultarOperador(int idOperador) {
		Operador operador = new OperadorDAO().consultarOperador(idOperador);
		if (operador == null) {
			throw new PRISMAException("No se puede consultar el operador.",
					"MSG13");
		}
		return operador;
	}

	public static ReglaNegocio agregarElementosFormatoCampo(ReglaNegocio model,
			int idAtributoFormato) {
		model.setAtributoExpReg(AtributoBs.consultarAtributo(idAtributoFormato));
		return model;
	}

	public static ReglaNegocio consultaReglaNegocio(Integer id) {
		ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
				.consultarReglaNegocio(id);
		if (reglaNegocio == null) {
			throw new PRISMAException(
					"No se puede consultar la regla de negocio.", "MSG13");
		}
		return reglaNegocio;
	}

//	public static void modificarReglaNegocio(ReglaNegocio model,
//			Actualizacion actualizacion) throws Exception {
//		try {
//			validar(model);
//			model.setClave(CLAVE);
//			model.setEstadoElemento(ElementoBs
//					.consultarEstadoElemento(Estado.EDICION));
//			model.setNombre(model.getNombre().trim());
//			ElementoBs.verificarEstado(model,
//					CU_ReglasNegocio.MODIFICARREGLANEGOCIO8_2);
//			new ReglaNegocioDAO().modificarReglaNegocio(model, actualizacion);
//		} catch (JDBCException je) {
//			if (je.getErrorCode() == 1062) {
//				throw new PRISMAValidacionException(
//						"El nombre de la regla de negocio ya existe.", "MSG7",
//						new String[] { "La", "Regla de negocio",
//								model.getNombre() }, "model.nombre");
//			}
//			System.out.println("ERROR CODE " + je.getErrorCode());
//			je.printStackTrace();
//			throw new Exception();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			throw new Exception();
//		}
//
//	}
	
	public static void modificarReglaNegocio(ReglaNegocio model) throws Exception {
		try {
			validar(model);
			model.setClave(CLAVE);
			model.setEstadoElemento(ElementoBs
					.consultarEstadoElemento(Estado.EDICION));
			model.setNombre(model.getNombre().trim());
			ElementoBs.verificarEstado(model,
					CU_ReglasNegocio.MODIFICARREGLANEGOCIO8_2);
			new ReglaNegocioDAO().modificarReglaNegocio(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException(
						"El nombre de la regla de negocio ya existe.", "MSG7",
						new String[] { "La", "Regla de negocio",
								model.getNombre() }, "model.nombre");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	public static void eliminarReglaNegocio(ReglaNegocio model)
			throws Exception {
		try {
			ElementoBs.verificarEstado(model,
					CU_ReglasNegocio.MODIFICARREGLANEGOCIO8_2);
			new ReglaNegocioDAO().eliminarElemento(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1451) {
				throw new PRISMAException(
						"No se puede eliminar la regla de negocio.", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

	}

	public static List<String> verificarReferencias(ReglaNegocio model) {

		List<ReferenciaParametro> referenciasParametro;
		List<CasoUsoReglaNegocio> referenciasReglaNegocio;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasReglaNegocio = new CasoUsoReglaNegocioDAO()
				.consultarReferencias(model);

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

		for (CasoUsoReglaNegocio casoUsoReglaNegocio : referenciasReglaNegocio) {
			String linea = "";
			casoUso = casoUsoReglaNegocio.getCasoUso().getClave()
					+ casoUsoReglaNegocio.getCasoUso().getNumero() + " "
					+ casoUsoReglaNegocio.getCasoUso().getNombre();
			linea = "Reglas de negocio del caso de uso " + casoUso;
			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
	
	public static List<CasoUso> verificarCasosUsoReferencias(ReglaNegocio model) {

		List<ReferenciaParametro> referenciasParametro;
		List<CasoUsoReglaNegocio> referenciasReglaNegocio;

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasReglaNegocio = new CasoUsoReglaNegocioDAO()
				.consultarReferencias(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
				setReferenciasVista.add(postPrecondicion.getCasoUso());
			} else if (paso != null) {
				setReferenciasVista.add(paso.getTrayectoria().getCasoUso());
			}
		}

		for (CasoUsoReglaNegocio casoUsoReglaNegocio : referenciasReglaNegocio) {
			setReferenciasVista.add(casoUsoReglaNegocio.getCasoUso());
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

	public static void validarReglaCompAtributos(int idEntidad1, int idAtributo1,
			int idOperador, int idEntidad2, int idAtributo2) {
		if(idEntidad1 == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó la entidad 1 de la comparación.", "MSG4",
					null, "idEntidad1");
		}
		if(idAtributo1 == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el atributo 1 de la comparación.", "MSG4",
					null, "idAtributo1");
		}
		if(idOperador == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el operador de la comparación.", "MSG4",
					null, "idOperador");
		}
		if(idEntidad1 == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó la entidad 2 de la comparación.", "MSG4",
					null, "idEntidad2");
		}
		if(idEntidad1 == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el atributo 2 de la comparación.", "MSG4",
					null, "idAtributo2");
		}
		
		
	}

	public static void validarReglaNegocioFormatoCampo(int idEntidadFormato,
			int idAtributoFormato) {
		if(idEntidadFormato == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó la entidad del formato correcto.", "MSG4",
					null, "idEntidadFormato");
		}
		if(idAtributoFormato == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el atributo del formato correcto.", "MSG4",
					null, "idAtributoFormato");
		}
		
	}

	public static void validarReglaNegocioUnicidad(int idEntidadUnicidad,
			int idAtributoUnicidad) {
		if(idEntidadUnicidad == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó la entidad de la unicidad.", "MSG4",
					null, "idEntidadUnicidad");
		}
		if(idAtributoUnicidad == -1) {
			throw new PRISMAValidacionException(
					"El usuario no seleccionó el atributo de la unicidad.", "MSG4",
					null, "idAtributoUnicidad");
		}
		
	}
	
	public static boolean esGlobal(TipoReglaNegocio trn) {
		if(trn != null) {
			switch(TipoReglaNegocioEnum.getTipoReglaNegocio(trn)) {
			case COMPATRIBUTOS:
				return false;
			case DATOCORRECTO:
				return true;
			case FORMATOARCH:
				return true;
			case FORMATOCAMPO:
				return false;
			case LONGITUD:
				return true;
			case OBLIGATORIOS:
				return true;
			case OTRO:
				return false;
			case TAMANOARCH:
				return true;
			case UNICIDAD:
				return false;
			case VERFCATALOGOS:
				return true;
			default:
				return false;
			}	
		}
		return false;
	}
}

