package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AnalisisEnum.CU_Mensajes;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.dao.MensajeDAO;
import mx.prisma.editor.dao.ParametroDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.SalidaDAO;
import mx.prisma.editor.model.Actualizacion;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Parametro;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.Salida;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class MensajeBs {
	private static final String CLAVE = "MSG";

	public static List<Mensaje> consultarMensajesProyecto(Proyecto proyecto) {
		List<Mensaje> listMensajes = new MensajeDAO().consultarMensajes(proyecto.getId());
		if(listMensajes == null) {
			throw new PRISMAException("No se pueden consultar los mensajes.", "MSG13");
		}
		return listMensajes;
	}

	public static void registrarMensaje(Mensaje model) throws Exception{
		try {
				validar(model);
				model.setClave(CLAVE);
				model.setEstadoElemento(ElementoBs.consultarEstadoElemento(Estado.EDICION));
				model.setNombre(model.getNombre().trim());
				new MensajeDAO().registrarMensaje(model);
		} catch (JDBCException je) {
				if(je.getErrorCode() == 1062)
				{
					throw new PRISMAValidacionException("El nombre del mensaje ya existe.", "MSG7",
							new String[] { "El","Mensaje", model.getNombre()}, "model.nombre");
				}
				System.out.println("ERROR CODE " + je.getErrorCode());
				je.printStackTrace();
				throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

//	public static void modificarMensaje(Mensaje model, Actualizacion actualizacion) throws Exception{
//		try {
//				validar(model);
//				ElementoBs.verificarEstado(model, CU_Mensajes.MODIFICARMENSAJE9_2);
//				model.setEstadoElemento(ElementoBs
//						.consultarEstadoElemento(Estado.EDICION));
//				model.setNombre(model.getNombre().trim());
//				
//				new MensajeDAO().modificarMensaje(model, actualizacion);
//		
//		} catch (JDBCException je) {
//			System.out.println("ERROR CODE " + je.getErrorCode());
//			je.printStackTrace();
//			throw new Exception();
//		} catch(HibernateException he) {
//			he.printStackTrace();
//			throw new Exception();
//		}
//	}
	
	public static void modificarMensaje(Mensaje model) throws Exception{
		try {
				validar(model);
				ElementoBs.verificarEstado(model, CU_Mensajes.MODIFICARMENSAJE9_2);
				model.setEstadoElemento(ElementoBs
						.consultarEstadoElemento(Estado.EDICION));
				model.setNombre(model.getNombre().trim());
				
				new MensajeDAO().modificarMensaje(model);
		
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
	}

	private static void validar(Mensaje model) {
		//Validaciones del número
		if(Validador.esNuloOVacio(model.getNumero())) {
			throw new PRISMAValidacionException("El usuario no ingresó el número del mensaje.", "MSG4", null, "model.numero");
		}
		if(!Pattern.matches("[1-9]+[0-9]*", model.getNumero())) {
			throw new PRISMAValidacionException("El usuario no ingresó un número válido", "MSG5", new String[]{"un", "número entero"}, "model.numero");
		}
		if(Validador.validaLongitudMaxima(model.getNumero().toString(), 20)) {
			throw new PRISMAValidacionException("El usuario ingreso un número muy largo.", "MSG6", new String[] { "20",
			"números"}, "model.numero");
		}
		
		//Se asegura la unicidad del nombre y del numero
		List<Mensaje> mensajes = consultarMensajesProyecto(model.getProyecto());
		for(Mensaje msj : mensajes) {
			if(msj.getId() != model.getId()) {
				if(msj.getNombre().equals(model.getNombre())) {
					throw new PRISMAValidacionException("El nombre del mensaje ya existe.", "MSG7",
							new String[] { "El","Mensaje", msj.getNombre()}, "model.nombre");
				}
				if(msj.getNumero().equals(model.getNumero())) {
					throw new PRISMAValidacionException("El numero del mensaje ya existe.", "MSG7",
							new String[] { "El","Mensaje", msj.getNumero()}, "model.numero");
				}
			}
		}
		
		// Validaciones del nombre
		if(Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException("El usuario no ingresó el nombre del mensaje.", "MSG4", null, "model.nombre");
		}
		if(Validador.validaLongitudMaxima(model.getNombre(), 200)) {
			throw new PRISMAValidacionException("El usuario ingreso un nombre muy largo.", "MSG6", new String[] { "200",
			"caracteres"}, "model.nombre");
		}
		if(Validador.contieneCaracterInvalido(model.getNombre())) {
			throw new PRISMAValidacionException("El usuario ingreso un nombre con caracter inválido.", "MSG23", new String[] { "El",
			"nombre"}, "model.nombre");
		}
		// Validaciones de la Descripción
		if(Validador.esNuloOVacio(model.getDescripcion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la desripción del mensaje.", "MSG4",
					null, "model.descripcion");
		}
		if (Validador.validaLongitudMaxima(model.getDescripcion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una descripcion muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.descripcion");
		}
		//Validaciones de la Redacción
		if(Validador.esNuloOVacio(model.getRedaccion())) {
			throw new PRISMAValidacionException("El usuario no ingresó la redaccion del mensaje.", "MSG4", null, "model.redaccion");
		}
		if(model.getRedaccion() != null && Validador.validaLongitudMaxima(model.getRedaccion(), 999)) {
			throw new PRISMAValidacionException("El usuario ingreso una redaccion muy larga.", "MSG6", new String[] { "999",
			"caracteres"}, "model.redaccion");
		}
		
		//Validaciones de mensaje parametrizado
		if(model.isParametrizado()) {
			List<Parametro> parametros = obtenerParametros(model.getRedaccion(), model.getProyecto().getId());
			if(parametros.size() != model.getParametros().size()) {
				throw new PRISMAValidacionException("El usuario no ingresó la descripcion de algun parametros del mensaje.", "MSG24", null, "model.parametros");
			}
			//Validacion de las descripciones de los parámetros
			for(MensajeParametro mp : model.getParametros()) {
				if(Validador.esNuloOVacio(mp.getParametro().getDescripcion())) {
					throw new PRISMAValidacionException("El usuario no ingresó la descripcion de algun parametros del mensaje.", "MSG24", null, "model.parametros");
				}
			}
		}
		
	}
	
	public static boolean esParametrizado(String redaccion) {
		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
				
		if(tokens.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static List<Parametro> obtenerParametros(String redaccion, int idProyecto) {
		//Se convierte la lista de parametros en json para enviarlos a la vista
		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
		ArrayList<Parametro> listParametros = new ArrayList<Parametro>();
		Parametro parametroAux = null;
		if(listParametros.size() > 10) {
			throw new PRISMAValidacionException("El usuario no ingresó la descripcion de algun parametros del mensaje.", "MSG6", new String[]{"10", "parámetros"}, 
					"model.parametros");
		}
		ArrayList<String> segmentos;
		for(String token : tokens) {
			segmentos = TokenBs.segmentarToken(token);
			//Se hace la consulta con base en el nombre
			Parametro parametro = consultarParametro(segmentos.get(1), idProyecto);
			if(parametro == null) {
				//Si el parámetro existe en la bd
				parametro = new Parametro(segmentos.get(1),"");
			}
			if (!pertecene(parametro, listParametros)) {
				parametroAux = new Parametro(parametro.getNombre(), parametro.getDescripcion());
				listParametros.add(parametroAux);
				
			}
		}
		return listParametros;
	}

	private static boolean pertecene(Parametro parametro,
			ArrayList<Parametro> listParametros) {
		for (Parametro parametroi : listParametros) {
			if (parametroi.getNombre().equals(parametro.getNombre())) {
				return true;
			}
		}
		return false;
	}

	public static Parametro consultarParametro(String nombre, int idProyecto) {
		Parametro parametro = new ParametroDAO().consultarParametro(nombre, idProyecto);
		return parametro;
	}

	public static List<Parametro> consultarParametros(int idProyecto) {
		List<Parametro> listParametros = new ParametroDAO().consultarParametros(idProyecto);
		return listParametros;
	}

	public static Mensaje consultarMensaje(Integer id) {
		Mensaje mensaje = new MensajeDAO().consultarMensaje(id);
		if(mensaje == null) {
			throw new PRISMAException("No se puede consultar el mensaje.", "MSG13");
		}
		return mensaje;
	}
	
	public static void eliminarMensaje(Mensaje model) throws Exception {
		try {
			ElementoBs.verificarEstado(model, CU_Mensajes.ELIMINARMENSAJE9_3);
			new MensajeDAO().eliminarElemento(model);
	} catch (JDBCException je) {
			if(je.getErrorCode() == 1451)
			{
				throw new PRISMAException("No se puede eliminar el caso de uso", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
	} catch(HibernateException he) {
		he.printStackTrace();
		throw new Exception();
	}
		
	}

	public static List<String> verificarReferencias(Mensaje model) {

		List<ReferenciaParametro> referenciasParametro;
		List<Salida> referenciasSalida;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasSalida = new SalidaDAO().consultarReferencias(model);

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

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
	
	public static List<CasoUso> verificarCasosUsoReferencias(Mensaje model) {

		List<ReferenciaParametro> referenciasParametro;
		List<Salida> referenciasSalida;

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);
		referenciasSalida = new SalidaDAO().consultarReferencias(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
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

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
}
