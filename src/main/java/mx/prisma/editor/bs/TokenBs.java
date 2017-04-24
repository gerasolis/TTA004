package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.Set;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoSeccion;
import mx.prisma.editor.dao.AccionDAO;
import mx.prisma.editor.dao.ActorDAO;
import mx.prisma.editor.dao.AtributoDAO;
import mx.prisma.editor.dao.CasoUsoDAO;
import mx.prisma.editor.dao.EntidadDAO;
import mx.prisma.editor.dao.MensajeDAO;
import mx.prisma.editor.dao.ModuloDAO;
import mx.prisma.editor.dao.PantallaDAO;
import mx.prisma.editor.dao.ParametroDAO;
import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.dao.TerminoGlosarioDAO;
import mx.prisma.editor.dao.TipoParametroDAO;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoActor;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Extension;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Parametro;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Salida;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.TipoParametro;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.util.PRISMAValidacionException;

public class TokenBs {

	private static String tokenSeparator1 = "·";
	private static String tokenSeparator2 = ":";
	/*
	 * Estructura para referenciar elementos:
	 * 
	 * Regla de negocio: RN·Número:Nombre Entidad: ENT·Nombre Caso de uso:
	 * CU·ClaveMódulo·Número:Nombre Pantalla: IU·ClaveMódulo·Número:Nombre
	 * Mensaje: MSG·Número:Nombre Actor: ACT·Nombre Término Glosario: GLS·Nombre
	 * Atributo: ATR·Entidad:Nombre Paso:
	 * P·ClaveCasoUso·NúmeroCasoUso:NombreCasoUso:ClaveTrayectoria·Número
	 * Trayectoria:
	 * TRAY·ClaveCasoUso·NúmeroCasoUso:NombreCasoUso:ClaveTrayectoria Acción:
	 * ACC·ClavePantalla·NúmeroPantalla:NombrePantalla:Nombre Parámetros (Msj.)
	 * PARAM·Nombre
	 */
	public static String tokenRN = "RN" + tokenSeparator1;
	public static String tokenENT = "ENT" + tokenSeparator1;
	public static String tokenCU = "CU" + tokenSeparator1;
	public static String tokenIU = "IU" + tokenSeparator1;
	public static String tokenMSG = "MSG" + tokenSeparator1;
	public static String tokenACT = "ACT" + tokenSeparator1;
	public static String tokenGLS = "GLS" + tokenSeparator1;
	public static String tokenATR = "ATR" + tokenSeparator1;
	public static String tokenP = "P" + tokenSeparator1;
	public static String tokenTray = "TRAY" + tokenSeparator1;
	public static String tokenACC = "ACC" + tokenSeparator1;
	public static String tokenPARAM = "PARAM" + tokenSeparator1;

	/*
	 * El método ArrayList<Object> convertirToken_Objeto(String @redaccion,
	 * Proyecto @proyecto) se encarga de generar objetos con base en los tokens
	 * contenidos en una cadena.
	 * 
	 * Parámetros:
	 * 
	 * @redaccion: Cadena cuyo contenido incluye los tokens en su versión
	 * edición, por ejemplo: ATR.Escuela:Nombre. Se utilizará para procesar los
	 * tokens y convertirlos a objetos.
	 * 
	 * @proyecto: Proyecto en cuestión. Se utilizará para únicamente entregar
	 * como respuesta una lista de objetos, presentes para el proyecto actual.
	 * 
	 * 
	 * Ejemplo:
	 * 
	 * Para la cadena 'El sistema muestra la pantalla IU.SF.1:Gestionar
	 * elementos con el mensaje MSG.1:Operación exitosa', el método entregaría
	 * como resultado un ArrayList con dos objetos; uno de tipo Pantalla y otro
	 * de tipo Mensaje, con sus respectivas variables cargadas.
	 */
	public static ArrayList<Object> convertirToken_Objeto(String redaccion,
			Proyecto proyecto) {

		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
		ArrayList<Object> objetos = new ArrayList<Object>();
		ArrayList<String> segmentos;

		Atributo atributo;
		Actor actor;
		Pantalla pantalla;
		Accion accion;
		Modulo modulo;
		CasoUso casodeuso;
		Trayectoria trayectoria;
		Paso paso;

		for (String token : tokens) {
			segmentos = segmentarToken(token);

			switch (ReferenciaEnum.getTipoReferencia(segmentos.get(0))) {
			case ACCION: // ACC.IUM.NUM:PANTALLA:NOMBRE_ACC =
							// ACC.IUSF.7:Registrar_incendio:Aceptar
				if (segmentos.size() != 5) {
					errorEnToken("la", "acción");
				}
				pantalla = new PantallaDAO().consultarPantalla(segmentos.get(1)
						.replaceAll("_", " "), segmentos.get(2), proyecto);
				if (pantalla == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "pantalla",
							segmentos.get(1) + segmentos.get(2), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La pantalla "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}

				accion = new AccionDAO().consultarAccion(segmentos.get(4)
						.replaceAll("_", " "), pantalla);
				if (accion == null) {
					String[] parametros = {
							"la",
							"accion",
							segmentos.get(4).replaceAll("_", " ")
									+ " de la pantalla " + segmentos.get(1)
									+ segmentos.get(2), "registrada" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La acción "
									+ segmentos.get(4).replaceAll("_", " ")
									+ " de la pantalla " + segmentos.get(1)
									+ segmentos.get(2) + " no está registrada",
							"MSG15", parametros);
				}
				objetos.add(accion);
				break;
			case ATRIBUTO: // ATR.ENTIDAD_A_B:NOMBRE_ATT
				if (segmentos.size() != 3) {
					errorEnToken("el", "atributo");
				}
				Entidad entidad = new EntidadDAO().consultarEntidad(segmentos
						.get(1).replaceAll("_", " "), proyecto);
				if (entidad == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "entidad",
							segmentos.get(1).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La entidad "
									+ segmentos.get(1).replaceAll("_", " ")
									+ " no está registrada", "MSG15",
							parametros);
				}

				atributo = new AtributoDAO().consultarAtributo(segmentos.get(2)
						.replaceAll("_", " "), entidad);
				if (atributo == null) {
					String[] parametros = {
							"el",
							"atributo",
							segmentos.get(2).replaceAll("_", " ")
									+ " de la entidad " + segmentos.get(1),
							"registrado" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El atributo "
									+ segmentos.get(2) + " de la entidad "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}
				objetos.add(atributo);
				break;
			case ACTOR: // ACT.NOMBRE_ACT
				if (segmentos.size() != 2) {
					errorEnToken("el", "actor");
				}
				actor = new ActorDAO().consultarActor(segmentos.get(1)
						.replaceAll("_", " "), proyecto);
				if (actor == null) {
					String[] parametros = {
							// Construcción del mensaje de error;
							"el", "actor",
							segmentos.get(1).replaceAll("_", " "), "registrado" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El actor "
									+ segmentos.get(1).replaceAll("_", " ")
									+ " no está registrado", "MSG15",
							parametros);
				}
				objetos.add(actor);

				break;
			case CASOUSO: // CU.MODULO.NUMERO:NOMBRE_CU
				if (segmentos.size() != 4) {
					errorEnToken("el", "caso de uso");
				}
				modulo = new ModuloDAO().consultarModulo(segmentos.get(1),
						proyecto);
				if (modulo == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "modulo",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El módulo "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}

				casodeuso = new CasoUsoDAO().consultarCasoUso(modulo,
						segmentos.get(2));
				if (casodeuso == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "caso de uso",
							tokenCU + segmentos.get(1) + segmentos.get(2),
							"registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ token + " no está registrado", "MSG15",
							parametros);
				}
				objetos.add(casodeuso);

				break;
			case ENTIDAD: // ENT.NOMBRE_ENT
				if (segmentos.size() != 2) {
					errorEnToken("la", "entidad");
				}
				entidad = new EntidadDAO().consultarEntidad(segmentos.get(1)
						.replaceAll("_", " "), proyecto);
				if (entidad == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "entidad",
							segmentos.get(1).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La entidad "
									+ segmentos.get(1) + " no está registrada",
							"MSG15", parametros);
				}
				objetos.add(entidad);
				break;
			case TERMINOGLS: // GLS.NOMBRE_GLS
				if (segmentos.size() != 2) {
					errorEnToken("el", "término");
				}
				TerminoGlosario terminoGlosario = new TerminoGlosarioDAO()
						.consultarTerminoGlosario(
								segmentos.get(1).replaceAll("_", " "), proyecto);
				if (terminoGlosario == null) {
					String[] parametros = { "el", "término",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El término no está registrado",
							"MSG15", parametros);
				}
				objetos.add(terminoGlosario);
				break;
			case PANTALLA: // IU.MODULO.NUMERO:NOMBRE_IU
				if (segmentos.size() != 4) {
					errorEnToken("la", "pantalla");
				}
				modulo = new ModuloDAO().consultarModulo(segmentos.get(1),
						proyecto);
				if (modulo == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "modulo",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El módulo "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}

				pantalla = new PantallaDAO().consultarPantalla(modulo,
						segmentos.get(2));
				if (pantalla == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "pantalla",
							tokenIU + segmentos.get(1) + segmentos.get(2),
							"registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La pantalla "
									+ token + " no está registrada", "MSG15",
							parametros);
				}
				objetos.add(pantalla);

				break;
			case MENSAJE: // MSG.NUMERO:NOMBRE_MSG
				if (segmentos.size() != 3) {
					errorEnToken("el", "mensaje");
				}
				Mensaje mensaje = new MensajeDAO().consultarMensaje(segmentos
						.get(2).replaceAll("_", " "), proyecto);
				if (mensaje == null) {
					String[] parametros = { "el", "mensaje",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El mensaje "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}
				objetos.add(mensaje);
				break;
			case REGLANEGOCIO: // RN.NUMERO:NOMBRE_RN
				if (segmentos.size() != 3) {
					errorEnToken("la", "regla de negocio");
				}
				ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
						.consultarReglaNegocio(
								segmentos.get(2).replaceAll("_", " "), proyecto);
				if (reglaNegocio == null) {
					String[] parametros = { "la", "regla de negocio",
							segmentos.get(2).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La regla de negocio "
									+ segmentos.get(2) + " no está registrada",
							"MSG15", parametros);
				}
				objetos.add(reglaNegocio);
				break;
			// TRAY·CUSF·001:s:A
			case TRAYECTORIA: // TRAY.CUMODULO.NUM:NOMBRECU:CLAVETRAY
				if (segmentos.size() != 5) {
					errorEnToken("la", "trayectoria");
				}
				casodeuso = new CasoUsoDAO().consultarCasoUso(segmentos.get(1),
						segmentos.get(2), proyecto);
				if (casodeuso == null) {
					String[] parametros = { "el", "caso de uso",
							segmentos.get(1) + segmentos.get(2), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}

				trayectoria = null;
				for (Trayectoria t : casodeuso.getTrayectorias()) {
					if (t.getClave().equals(segmentos.get(4))) {
						trayectoria = t;
					}
				}

				if (trayectoria == null) {
					String[] parametros = {
							"la",
							"trayectoria",
							segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2),
							"registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La trayectoria "
									+ segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}
				objetos.add(trayectoria);
				break;

			case PASO: // P.CUMODULO.NUM:NOMBRECU:CLAVETRAY.NUMERO
				if (segmentos.size() != 6) {
					errorEnToken("el", "paso");
				}
				casodeuso = new CasoUsoDAO().consultarCasoUso(segmentos.get(1),
						segmentos.get(2), proyecto);
				if (casodeuso == null) {
					String[] parametros = { "el", "caso de uso",
							segmentos.get(1) + segmentos.get(2), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}

				trayectoria = null;
				for (Trayectoria t : casodeuso.getTrayectorias()) {
					if (t.getClave().equals(segmentos.get(4))) {
						trayectoria = t;
					}
				}

				if (trayectoria == null) {
					String[] parametros = {
							"la",
							"trayectoria",
							segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2),
							"registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La trayectoria "
									+ segmentos.get(4) + "del caso de uso"
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}
				paso = null;
				for (Paso p : trayectoria.getPasos()) {
					if (p.getNumero() == Integer.parseInt(segmentos.get(5))) {
						paso = p;
					}
				}

				if (paso == null) {
					String[] parametros = {
							"el",
							"paso",
							segmentos.get(5) + " de la trayectoria "
									+ segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2),
							"registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El paso "
									+ segmentos.get(5) + "de la trayectoria"
									+ segmentos.get(4) + "del caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}

				objetos.add(paso);
				break;
			default:
				break;

			}
		}

		return objetos;
	}

	/*
	 * El método String codificarCadenaToken(String @redaccion, Proyecto
	 * 
	 * @proyecto se encarga de codificar la cadena a su versión base de datos
	 * (cruda).
	 * 
	 * Parámetros:
	 * 
	 * @cadenaCodificada: Cadena cuyo contenido incluye los tokens en su versión
	 * edición, por ejemplo: ATR·Producto:Peso.
	 * 
	 * Ejemplo:
	 * 
	 * El resultado de decodificar la cadena "ATR·Producto:Peso." sería "ATR·1",
	 * siendo "1" el id del atributo "Peso".
	 */
	public static String codificarCadenaToken(String redaccion,
			Proyecto proyecto) {

		ArrayList<String> tokens = procesarTokenIpunt(redaccion);
		ArrayList<String> segmentos;
		Pantalla pantalla;
		Accion accion;
		Modulo modulo;
		CasoUso casoUso;
		Entidad entidad;
		Atributo atributo;
		Trayectoria trayectoria;
		Paso paso;
		for (String token : tokens) {
			segmentos = segmentarToken(token);
			switch (ReferenciaEnum.getTipoReferencia(segmentos.get(0))) {
			case ACCION:

				if (segmentos.size() != 5) {
					errorEnToken("la", "acción");
				}
				pantalla = new PantallaDAO().consultarPantalla(segmentos.get(1)
						.replaceAll("_", " "), segmentos.get(2), proyecto);
				if (pantalla == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "pantalla",
							segmentos.get(1) + segmentos.get(2), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La pantalla "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}

				accion = new AccionDAO().consultarAccion(segmentos.get(4)
						.replaceAll("_", " "), pantalla);
				if (accion == null) {
					String[] parametros = {
							"la",
							"accion",
							segmentos.get(4).replaceAll("_", " ")
									+ " de la pantalla " + segmentos.get(1)
									+ segmentos.get(2), "registrada" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La acción"
									+ segmentos.get(4).replaceAll("_", " ")
									+ "de la pantalla" + segmentos.get(1)
									+ segmentos.get(2) + "no está registrada",
							"MSG15", parametros);
				}
				redaccion = redaccion.replace(token, tokenACC + accion.getId());
				break;
			case ATRIBUTO:
				if (segmentos.size() != 3) {
					errorEnToken("el", "atributo");
				}
				entidad = new EntidadDAO().consultarEntidad(segmentos.get(1)
						.replaceAll("_", " "), proyecto);
				if (entidad == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "entidad",
							segmentos.get(1).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La entidad "
									+ segmentos.get(1).replaceAll("_", " ")
									+ " no está registrada", "MSG15",
							parametros);
				}

				atributo = new AtributoDAO().consultarAtributo(segmentos.get(2)
						.replaceAll("_", " "), entidad);
				if (atributo == null) {
					String[] parametros = {
							"el",
							"atributo",
							segmentos.get(2).replaceAll("_", " ")
									+ " de la entidad " + segmentos.get(1),
							"registrado" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El atributo"
									+ segmentos.get(2) + "de la entidad"
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}
				redaccion = redaccion.replace(token,
						tokenATR + atributo.getId());
				break;
			case ACTOR:
				if (segmentos.size() != 2) {
					errorEnToken("el", "actor");
				}
				Actor actor = new ActorDAO().consultarActor(segmentos.get(1)
						.replaceAll("_", " "), proyecto);
				if (actor == null) {
					String[] parametros = {
							// Construcción del mensaje de error;
							"el", "actor",
							segmentos.get(1).replaceAll("_", " "), "registrado" };
					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El actor no está registrado",
							"MSG15", parametros);
				}
				redaccion = redaccion.replace(token, tokenACT + actor.getId());

				break;
			case CASOUSO:
				if (segmentos.size() != 4) {
					errorEnToken("el", "caso de uso");
				}
				modulo = new ModuloDAO().consultarModulo(segmentos.get(1),
						proyecto);
				if (modulo == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "modulo",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El módulo "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}

				casoUso = new CasoUsoDAO().consultarCasoUso(modulo,
						segmentos.get(2));
				if (casoUso == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "caso de uso",
							segmentos.get(1) + segmentos.get(2), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ token + " no está registrado", "MSG15",
							parametros);
				}
				redaccion = redaccion.replace(token, tokenCU + casoUso.getId());
				break;
			case ENTIDAD:
				if (segmentos.size() != 2) {
					errorEnToken("la", "entidad");
				}
				entidad = new EntidadDAO().consultarEntidad(segmentos.get(1)
						.replaceAll("_", " "), proyecto);
				if (entidad == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "entidad",
							segmentos.get(1).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La entidad "
									+ segmentos.get(1) + " no está registrada",
							"MSG15", parametros);
				}
				redaccion = redaccion
						.replace(token, tokenENT + entidad.getId());
				break;
			case TERMINOGLS:
				if (segmentos.size() != 2) {
					errorEnToken("el", "término");
				}
				TerminoGlosario terminoGlosario = new TerminoGlosarioDAO()
						.consultarTerminoGlosario(
								segmentos.get(1).replaceAll("_", " "), proyecto);
				if (terminoGlosario == null) {
					String[] parametros = { "el", "término",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El término no está registrado",
							"MSG15", parametros);
				}
				redaccion = redaccion.replace(token, segmentos.get(0)
						+ tokenSeparator1 + terminoGlosario.getId());
				break;
			case PANTALLA:
				if (segmentos.size() != 4) {
					errorEnToken("la", "pantalla");
				}
				modulo = new ModuloDAO().consultarModulo(segmentos.get(1),
						proyecto);
				if (modulo == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "el", "modulo",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El módulo "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}

				pantalla = new PantallaDAO().consultarPantalla(modulo,
						segmentos.get(2));
				if (pantalla == null) {
					// Construcción del mensaje de error;
					String[] parametros = { "la", "pantalla", token,
							"registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La pantalla "
									+ token + " no está registrada", "MSG15",
							parametros);
				}
				redaccion = redaccion
						.replace(token, tokenIU + pantalla.getId());
				break;
			case MENSAJE:
				if (segmentos.size() != 3) {
					errorEnToken("el", "mensaje");
				}
				Mensaje mensaje = new MensajeDAO().consultarMensaje(segmentos
						.get(2).replaceAll("_", " "), proyecto);
				if (mensaje == null) {
					String[] parametros = { "el", "mensaje",
							segmentos.get(1).replaceAll("_", " "), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El mensaje "
									+ segmentos.get(1) + " no está registrado",
							"MSG15", parametros);
				}
				redaccion = redaccion
						.replace(token, tokenMSG + mensaje.getId());
				break;
			case REGLANEGOCIO:
				if (segmentos.size() != 3) {
					errorEnToken("la", "regla de negocio");
				}
				ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
						.consultarReglaNegocio(
								segmentos.get(2).replaceAll("_", " "), proyecto);
				if (reglaNegocio == null) {
					String[] parametros = { "la", "regla de negocio",
							segmentos.get(2).replaceAll("_", " "), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La regla de negocio "
									+ segmentos.get(2) + " no está registrada",
							"MSG15", parametros);
				}
				redaccion = redaccion.replace(token,
						tokenRN + reglaNegocio.getId());
				break;
			case TRAYECTORIA:
				if (segmentos.size() != 5) {
					errorEnToken("la", "trayectoria");
				}
				trayectoria = null;
				casoUso = new CasoUsoDAO().consultarCasoUso(segmentos.get(1),
						segmentos.get(2), proyecto);
				if (casoUso == null) {
					String[] parametros = { "el", "caso de uso",
							segmentos.get(1) + segmentos.get(2), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}
				for (Trayectoria t : casoUso.getTrayectorias()) {
					if (t.getClave().equals(segmentos.get(4))) {
						trayectoria = t;
					}
				}
				if (trayectoria == null) {
					String[] parametros = {
							"la",
							"trayectoria",
							segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2),
							"registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La trayectoria "
									+ segmentos.get(4) + "del caso de uso"
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}
				redaccion = redaccion.replace(token,
						tokenTray + trayectoria.getId());
				break;
			case PASO:
				if (segmentos.size() != 6) {
					errorEnToken("el", "paso");
				}
				casoUso = new CasoUsoDAO().consultarCasoUso(segmentos.get(1),
						segmentos.get(2), proyecto);
				if (casoUso == null) {
					String[] parametros = { "el", "caso de uso",
							segmentos.get(1) + segmentos.get(2), "registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}

				trayectoria = null;
				paso = null;
				for (Trayectoria t : casoUso.getTrayectorias()) {
					if (t.getClave().equals(segmentos.get(4))) {
						trayectoria = t;
						for (Paso p : trayectoria.getPasos()) {
							if (p.getNumero() == Integer.parseInt(segmentos
									.get(5))) {
								paso = p;
								break;
							}
						}
					}
				}
				if (trayectoria == null) {
					String[] parametros = {
							"la",
							"trayectoria",
							segmentos.get(4) + " del caso de uso "
									+ segmentos.get(3), "registrada" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: La trayectoria "
									+ segmentos.get(4) + "del caso de uso"
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrada", "MSG15",
							parametros);
				}

				if (paso == null) {
					String[] parametros = {
							"el",
							"paso",
							segmentos.get(5) + " de la trayectoria "
									+ segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2),
							"registrado" };

					throw new PRISMAValidacionException(
							"TokenBs.convertirToken_Objeto: El paso "
									+ segmentos.get(5) + " de la trayectoria "
									+ segmentos.get(4) + " del caso de uso "
									+ segmentos.get(1) + segmentos.get(2)
									+ " no está registrado", "MSG15",
							parametros);
				}

				redaccion = redaccion.replace(token, tokenP + paso.getId());
				break;
			default:
				break;

			}
		}
		if (!redaccion.isEmpty())
			return "$" + redaccion;
		else
			return "";
	}

	/*
	 * El método String decodificarCadenasToken(String @cadenaCodificada) se
	 * encarga de decodificar la cadena a su versión de edición.
	 * 
	 * Parámetros:
	 * 
	 * @cadenaCodificada: Cadena cuyo contenido incluye los tokens en su versión
	 * base de datos (cruda), por ejemplo: ATR·1.
	 * 
	 * Ejemplo:
	 * 
	 * El resultado de decodificar la cadena "ATR·1" sería "ATR·Producto:Peso",
	 * siendo "ATR·Producto:Peso" el token para referenciar al atributo "Peso"
	 * de la entidad "Producto".
	 */
	public static String decodificarCadenasToken(String cadenaCodificada) {
		String cadenaDecodificada = "";
		if (cadenaCodificada != null && cadenaCodificada != "") {
			String cadenaCodificadaBruta = cadenaCodificada.substring(1);
			ArrayList<String> tokens = procesarTokenIpunt(cadenaCodificadaBruta);
			ArrayList<String> segmentos;
			Modulo modulo;
			cadenaDecodificada = cadenaCodificadaBruta;

			for (String token : tokens) {
				segmentos = segmentarToken(token);
				switch (ReferenciaEnum.getTipoReferencia(segmentos.get(0))) {
				case ACCION:
					Accion accion = new AccionDAO().consultarAccion(Integer
							.parseInt(segmentos.get(1)));
					if (accion == null) {
						cadenaDecodificada = "";
						break;
					} else {
						Pantalla pantalla = accion.getPantalla();
						cadenaDecodificada = remplazoToken(
								cadenaDecodificada,
								token,
								tokenACC
										+ pantalla.getClave()
										+ tokenSeparator1
										+ pantalla.getNumero()
										+ tokenSeparator2
										+ pantalla.getNombre()
												.replace(" ", "_")
										+ tokenSeparator2
										+ accion.getNombre().replace(" ", "_"));
					}
					break;
				case ATRIBUTO:
					Atributo atributo = new AtributoDAO()
							.consultarAtributo(Integer.parseInt(segmentos
									.get(1)));
					if (atributo == null) {
						cadenaDecodificada = "";
						break;
					} else {
						Entidad entidad = atributo.getEntidad();
						cadenaDecodificada = remplazoToken(cadenaDecodificada,
								token,
								tokenATR
										+ entidad.getNombre().replace(" ", "_")
										+ tokenSeparator2
										+ atributo.getNombre()
												.replace(" ", "_"));
					}
					break;
				case ACTOR:
					Actor actor = new ActorDAO().consultarActor(Integer
							.parseInt(segmentos.get(1)));
					if (actor == null) {
						cadenaDecodificada = "";
						break;
					}
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token,
							tokenACT + actor.getNombre().replace(" ", "_"));

					break;
				case CASOUSO:
					CasoUso casoUso = new CasoUsoDAO().consultarCasoUso(Integer
							.parseInt(segmentos.get(1)));
					if (casoUso == null) {
						cadenaDecodificada = "";
						break;
					}
					modulo = casoUso.getModulo();
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenCU + modulo.getClave()
									+ tokenSeparator1 + casoUso.getNumero()
									+ tokenSeparator2
									+ casoUso.getNombre().replace(" ", "_"));

					break;
				case ENTIDAD:
					Entidad entidad = new EntidadDAO().consultarEntidad(Integer
							.parseInt(segmentos.get(1)));
					if (entidad == null) {
						cadenaDecodificada = "";
						break;
					}
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token,
							tokenENT + entidad.getNombre().replace(" ", "_"));

					break;
				case TERMINOGLS:
					TerminoGlosario terminoGlosario = new TerminoGlosarioDAO()
							.consultarTerminoGlosario(Integer
									.parseInt(segmentos.get(1)));
					if (terminoGlosario == null) {
						cadenaDecodificada = "";
					}
					cadenaDecodificada = remplazoToken(
							cadenaDecodificada,
							token,
							tokenGLS
									+ terminoGlosario.getNombre().replace(" ",
											"_"));
					break;
				case PANTALLA:
					Pantalla pantalla = new PantallaDAO()
							.consultarPantalla(Integer.parseInt(segmentos
									.get(1)));
					if (pantalla == null) {
						cadenaDecodificada = "";
						break;
					}
					modulo = pantalla.getModulo();
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenIU + modulo.getClave()
									+ tokenSeparator1 + pantalla.getNumero()
									+ tokenSeparator2
									+ pantalla.getNombre().replace(" ", "_"));

					break;

				case MENSAJE:
					Mensaje mensaje = new MensajeDAO().consultarMensaje(Integer
							.parseInt(segmentos.get(1)));
					if (mensaje == null) {
						cadenaDecodificada = "";
					}
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenMSG + mensaje.getNumero()
									+ tokenSeparator2
									+ mensaje.getNombre().replace(" ", "_"));
					break;
				case REGLANEGOCIO:
					
					ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
							.consultarReglaNegocio(Integer.parseInt(segmentos
									.get(1)));
					if (reglaNegocio == null) {
						cadenaDecodificada = "";
					}
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenRN
									+ reglaNegocio.getNumero()
									+ tokenSeparator2
									+ reglaNegocio.getNombre()
											.replace(" ", "_"));
					break;
				case TRAYECTORIA:
					Trayectoria trayectoria = new TrayectoriaDAO()
							.consultarTrayectoria(Integer.parseInt(segmentos
									.get(1)));
					if (trayectoria == null) {
						cadenaDecodificada = "";
					}

					CasoUso cu = trayectoria.getCasoUso();
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenTray + cu.getClave() + tokenSeparator1
									+ cu.getNumero() + tokenSeparator2
									+ cu.getNombre().replace(" ", "_")
									+ tokenSeparator2 + trayectoria.getClave());
					break;

				case PASO:
					Paso paso = new PasoDAO().consultarPaso(Integer
							.parseInt(segmentos.get(1)));
					if (paso == null) {
						cadenaDecodificada = "";
					}
					Trayectoria t = paso.getTrayectoria();
					CasoUso cut = t.getCasoUso();
					cadenaDecodificada = remplazoToken(cadenaDecodificada,
							token, tokenP + cut.getClave() + tokenSeparator1
									+ cut.getNumero() + tokenSeparator2
									+ cut.getNombre().replace(" ", "_")
									+ tokenSeparator2 + t.getClave()
									+ tokenSeparator1 + paso.getNumero());
					break;
					
				case PARAMETRO:
					System.out.println("segmento "+segmentos.get(1));
					Parametro parametro = new ParametroDAO().consultarParametro(segmentos.get(1),2);
					if (parametro == null) {
						cadenaDecodificada = "";
					}
					cadenaDecodificada = remplazoToken(cadenaDecodificada, token, tokenPARAM + parametro.getNombre());
					break;
				default:
					break;

				}
			}
		}
		return cadenaDecodificada;
	}

	/*
	 * El método String decodificarCadenaSinToken(String @cadenaCodificada) se
	 * encarga de decodificar la cadena a su versión de consulta.
	 * 
	 * Parámetros:
	 * 
	 * @cadenaCodificada: Cadena cuyo contenido incluye los tokens en su versión
	 * base de datos (cruda), por ejemplo: ATR·1.
	 * 
	 * Ejemplo:
	 * 
	 * El resultado de decodificar la cadena "ATR·1" sería "Peso", siendo "Peso"
	 * el nombre del atributo cuyo id es 1.
	 */
	public static String decodificarCadenaSinToken(String redaccion) {
		if (redaccion == null || redaccion.isEmpty()) {
			return "Sin información";
		}
		redaccion = redaccion.substring(1);
		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
		for (String token : tokens) {
			ArrayList<String> segmentos = TokenBs.segmentarToken(token);
			String tokenReferencia = segmentos.get(0);
			switch (ReferenciaEnum.getTipoReferencia(tokenReferencia)) {
			case ACCION:
				Accion accion = new AccionDAO().consultarAccion(Integer
						.parseInt(segmentos.get(1)));
				if (accion == null) {
					redaccion = "";
					break;
				} else {
					redaccion = remplazoToken(redaccion, token,
							accion.getNombre());
				}
				break;
			case ACTOR:
				Actor actor = new ActorDAO().consultarActor(Integer
						.parseInt(segmentos.get(1)));
				if (actor == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(redaccion, token, actor.getNombre());
				break;
			case ATRIBUTO:
				Atributo atributo = new AtributoDAO().consultarAtributo(Integer
						.parseInt(segmentos.get(1)));
				if (atributo == null) {
					redaccion = "";
					break;
				} else {
					redaccion = remplazoToken(redaccion, token,
							atributo.getNombre());
				}
				break;
			case CASOUSO:
				CasoUso casoUso = new CasoUsoDAO().consultarCasoUso(Integer
						.parseInt(segmentos.get(1)));
				if (casoUso == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(redaccion, token, casoUso.getClave()
						+ " " + casoUso.getNumero() + " " + casoUso.getNombre());

				break;
			case ENTIDAD: // ENT.ID -> ENT.NOMBRE_ENT
				Entidad entidad = new EntidadDAO().consultarEntidad(Integer
						.parseInt(segmentos.get(1)));
				if (entidad == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(redaccion, token, entidad.getNombre());

				break;
			case TERMINOGLS:
				TerminoGlosario terminoGlosario = new TerminoGlosarioDAO()
						.consultarTerminoGlosario(Integer.parseInt(segmentos
								.get(1)));
				if (terminoGlosario == null) {
					redaccion = "";
				}
				redaccion = remplazoToken(redaccion, token,
						terminoGlosario.getNombre());
				break;
			case PANTALLA: 
				Pantalla pantalla = new PantallaDAO().consultarPantalla(Integer
						.parseInt(segmentos.get(1)));
				if (pantalla == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(redaccion, token,
						pantalla.getClave() + " " + pantalla.getNumero() + " "
								+ pantalla.getNombre());
				break;

			case MENSAJE: 
				Mensaje mensaje = new MensajeDAO().consultarMensaje(Integer
						.parseInt(segmentos.get(1)));
				if (mensaje == null) {
					redaccion = "";
				}
				redaccion = remplazoToken(redaccion, token, mensaje.getClave()
						+ " " + mensaje.getNumero() + " " + mensaje.getNombre());
				break;
			case REGLANEGOCIO:
				ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
						.consultarReglaNegocio(Integer.parseInt(segmentos
								.get(1)));
				if (reglaNegocio == null) {
					redaccion = "";
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						reglaNegocio.getClave() + " "
								+ reglaNegocio.getNumero() + " "
								+ reglaNegocio.getNombre());
				break;
			case TRAYECTORIA:
				Trayectoria trayectoria = new TrayectoriaDAO()
						.consultarTrayectoria(Integer.parseInt(segmentos.get(1)));
				if (trayectoria == null) {
					redaccion = "";
				}

				redaccion = remplazoToken(redaccion, token,
						trayectoria.getClave());
				break;

			case PASO:
				Paso paso = new PasoDAO().consultarPaso(Integer
						.parseInt(segmentos.get(1)));
				if (paso == null) {
					redaccion = "";
				}
				redaccion = remplazoToken(redaccion, token, paso.getNumero()
						+ "");
				break;
				
			case PARAMETRO:
				Parametro parametro = new ParametroDAO().consultarParametro(Integer
						.parseInt(segmentos.get(1)));
				if (parametro == null) {
					redaccion = "";
				}
				redaccion = remplazoToken(redaccion, token, parametro.getNombre());
				break;
			default:
				break;

			}
		}

		redaccion = redaccion.replace("\n", "<br/>");
		redaccion = redaccion.replace("\r", "<br/>");
		return redaccion;

	}
	
	//Función que retorna el objeto con su id
	public static Object obtenerTokenObjeto(String redaccion) {
		System.out.println("ENTRA AL OBTENER TOKEN OBJETO");
		if (redaccion == null || redaccion.isEmpty()) {
			System.out.println("ENTRA AL SIN INFO");
			return "Sin información";
		}
		redaccion = redaccion.substring(1);
		System.out.println("OBTIENE EL SUBSTRING DE REDACCION");
		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
		System.out.println("PROCESA EL TOKEN");
		Object resultado = new Object();
		System.out.println("CREA RESULTADO "+tokens.size());
		for (String token : tokens) {
			System.out.println("ENTRA AL FOR DE TOKENS ");
			ArrayList<String> segmentos = TokenBs.segmentarToken(token);
			String tokenReferencia = segmentos.get(0);
			switch (ReferenciaEnum.getTipoReferencia(tokenReferencia)) {
			case ACCION:
				resultado = new AccionDAO().consultarAccion(Integer
						.parseInt(segmentos.get(1)));
				break;
			case ACTOR:
				resultado = new ActorDAO().consultarActor(Integer
						.parseInt(segmentos.get(1)));
				break;
			case ATRIBUTO:
				resultado = new AtributoDAO().consultarAtributo(Integer
						.parseInt(segmentos.get(1)));
				break;
			case CASOUSO:
				resultado = new CasoUsoDAO().consultarCasoUso(Integer
						.parseInt(segmentos.get(1)));
				break;
			case ENTIDAD: // ENT.ID -> ENT.NOMBRE_ENT
				resultado = new EntidadDAO().consultarEntidad(Integer
						.parseInt(segmentos.get(1)));
				break;
			case TERMINOGLS:
				resultado = new TerminoGlosarioDAO()
						.consultarTerminoGlosario(Integer.parseInt(segmentos
								.get(1)));
				break;
			case PANTALLA: 
				resultado = new PantallaDAO().consultarPantalla(Integer
						.parseInt(segmentos.get(1)));
				break;

			case MENSAJE: 
				System.out.println("ENTRA A MENSAJES ");
				resultado = new MensajeDAO().consultarMensaje(Integer
						.parseInt(segmentos.get(1)));
				break;
			case REGLANEGOCIO:
				System.out.println("ENTRO A RN");
				resultado = new ReglaNegocioDAO()
						.consultarReglaNegocio(Integer.parseInt(segmentos
								.get(1)));
				System.out.println("RN = "+resultado.toString());
				break;
			case TRAYECTORIA:
				resultado = new TrayectoriaDAO()
						.consultarTrayectoria(Integer.parseInt(segmentos.get(1)));
				break;

			case PASO:
				resultado = new PasoDAO().consultarPaso(Integer
						.parseInt(segmentos.get(1)));
				break;
				
			case PARAMETRO:
				resultado = new ParametroDAO().consultarParametro(Integer
						.parseInt(segmentos.get(1)));
				break;
			default:
				break;

			}
			return resultado;
		}

		redaccion = redaccion.replace("\n", "<br/>");
		redaccion = redaccion.replace("\r", "<br/>");
		return redaccion;

	}

	/*
	 * El método ArrayList<String> segmentarToken(String @token) construye un
	 * ArrayList de segmentos de un token, por ejemplo: para el token
	 * "ACT·Profesor", la función devolvería un ArrayList con dos elementos: [0]
	 * = ACT [1] = Profesor
	 * 
	 * Parámetros:
	 * 
	 * @token: Token que se desea segmentar
	 */
	public static ArrayList<String> segmentarToken(String token) {
		String segmento = "";
		ArrayList<String> segmentos = new ArrayList<String>();
		String caracterAt;

		for (int i = 0; i < token.length(); i++) {
			caracterAt = token.charAt(i) + "";
			if (caracterAt.equals(tokenSeparator1)
					|| caracterAt.equals(tokenSeparator2)) {
				segmentos.add(segmento);
				segmento = "";
			} else {
				segmento += token.charAt(i);
			}
		}
		if (segmento != "") {
			segmentos.add(segmento);
		}
		return segmentos;
	}

	/*
	 * El método ArrayList<String> procesarTokenIpunt(String cadena) construye
	 * un ArrayLisy de tokens,ejemplo: para la cadena
	 * "ATR·Escuela:Nombre, GLS·Clave_Zona" el resultado sería [0] =
	 * ATR·Escuela:Nombre [1] = GLS·Clave_Zona
	 * 
	 * Parámetros:
	 * 
	 * @cadena: Cadena que contiene los tokens.
	 */
	public static ArrayList<String> procesarTokenIpunt(String cadena) {
		ArrayList<String> tokens = new ArrayList<String>();
		String pila = "";
		String token = "";
		char caracter;
		boolean almacenar = false;
		if (cadena != null) {
			int longitud = cadena.length();
			for (int i = 0; i < longitud; i++) {
				caracter = cadena.charAt(i);
				if (almacenar) {
					if (puntoFinal(longitud, i, caracter)) {
						tokens.add(token);
					} else if (ignorarEscape(cadena, i, caracter)) {
						tokens.add(token);
						pila = "";
						almacenar = false;
					} else if (longitud - 1 == i) {
						token += caracter;
						tokens.add(token);
					} else {
						token += caracter;
					}

				} else {
					if (caracter == ' ') {
						pila = "";
					} else if (!ignorarEscape(cadena, i, caracter)) {
						pila += cadena.charAt(i);
						/*
						 * Si el sistema encuentra un token, el estado de la
						 * pila será almacenar.
						 */
						if (esToken(pila)) {
							almacenar = true;
							token = pila;
						}
					}
				}
			}
		}
		return tokens;
	}

	/*
	 * El método String remplazoToken(String @cadena, String @cadena_sustituir,
	 * String @cadena_sustituta) remplaza los tokens por el valor
	 * correspondiente según la decodificación realizada. Es útil frente a un
	 * simple replace, porque soluciona el siguiente problema:
	 * 
	 * Si se deseara remplazar el segmente de cadena "ACT·1" por "Profesor" en
	 * la cadena "ACT·1, ACT·11" resultado sería "Profesor, Profesor1" lo cual
	 * es indeseable por que cada token representa una referencia diferente.
	 * 
	 * Este método remplaza únicamente si la subcadena en la que se encuentra el
	 * patrón es una referencia/token completo.
	 * 
	 * Parámetros:
	 * 
	 * @cadena: Cadena en la que se realizarán los remplazos.
	 * 
	 * @cadena_sustituir: Cadena que contiene el token que se desea sustituir
	 * por su valor decodificado.
	 * 
	 * @cadena_sustituta: Cadena que contiene el valor decodificado que
	 * sustituirá a cadena_sustituir.
	 */
	public static String remplazoToken(String cadena, String cadena_sustituir,
			String cadena_sustituta) {
		String cadenaFinal = null;
		int indiceInicial = 0;
		int indiceFinal = 0;
		indiceInicial = cadena.indexOf(cadena_sustituir, indiceInicial);
		//System.out.println("indiceInicial1 :"+indiceInicial);
		while (indiceInicial != -1) {
			indiceFinal = indiceInicial + cadena_sustituir.length() - 1;
			//System.out.println("indiceFinalWhile: "+indiceFinal);
			if (indiceFinal + 1  == cadena.length()
					|| !ignore(cadena.charAt(indiceFinal + 1))) {
				cadenaFinal = cadena.substring(0,
						(indiceInicial != 0) ? indiceInicial : 0)
						+ cadena_sustituta
						+ cadena.substring(indiceFinal + 1, cadena.length());
				//System.out.println("cadena.substring(indiceFinal + 1, cadena.length()): "+cadena.substring(indiceFinal + 1, cadena.length()));
				//System.out.println("CadenaFinal: "+cadenaFinal);
				indiceInicial = cadenaFinal.indexOf(cadena_sustituir, indiceInicial + cadena_sustituta.length());
				cadena = cadenaFinal;
			} else {
				indiceInicial = cadena.indexOf(cadena_sustituir, indiceInicial + 1);
				System.out.println("IndiceInicialElse: "+indiceInicial);
			}
		}
		return cadena;
	}

	public static boolean ignore(char nextChar) {
		try {
			Integer.parseInt(nextChar + "");
		} catch (NumberFormatException e) {
			if((nextChar + "").equals(tokenSeparator1) || (nextChar + "").equals(tokenSeparator2)) {
				return true;
			}
			return false;
		}
		return false;
	}

	public static boolean duplicadoActor_Actores(Set<CasoUsoActor> actores,
			CasoUsoActor casoUsoActor) {

		for (CasoUsoActor casoUsoActori : actores) {
			if (casoUsoActori.getActor().getId() == casoUsoActor.getActor()
					.getId()) {
				if (casoUsoActori.getCasouso().getId() == casoUsoActor
						.getCasouso().getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean duplicadoAtributo_Entradas(Set<Entrada> entradas,
			Entrada entrada) {
		for (Entrada entradai : entradas) {
			if (entradai.getAtributo() != null && entrada.getAtributo() != null)
				if (entradai.getAtributo().getId() == entrada.getAtributo()
						.getId()) {
					if (entradai.getCasoUso().getId() == entrada.getCasoUso()
							.getId()) {
						return true;
					}
				}
		}
		return false;
	}

	public static boolean duplicadoAtributo_Salidas(Set<Salida> salidas,
			Salida salida) {
		for (Salida salidai : salidas) {
			if (salidai.getAtributo() != null)
				if (salidai.getAtributo().getId() == salida.getAtributo()
						.getId()) {
					if (salidai.getCasoUso().getId() == salida.getCasoUso()
							.getId()) {
						return true;
					}
				}
		}
		return false;
	}

	public static boolean duplicadoMensaje_Salidas(Set<Salida> salidas,
			Salida salida) {
		for (Salida salidai : salidas) {
			if (salidai.getMensaje() != null)
				if (salidai.getMensaje().getId() == salida.getMensaje().getId()) {
					if (salidai.getCasoUso().getId() == salida.getCasoUso()
							.getId()) {
						return true;
					}
				}
		}
		return false;
	}

	public static boolean duplicadoRegla_Reglas(
			Set<CasoUsoReglaNegocio> reglas, CasoUsoReglaNegocio casoUsoReglas) {
		for (CasoUsoReglaNegocio reglai : reglas) {
			if (reglai.getReglaNegocio().getId() == casoUsoReglas
					.getReglaNegocio().getId()) {
				if (reglai.getCasoUso().getId() == casoUsoReglas.getCasoUso()
						.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean duplicadoTermino_Entradas(Set<Entrada> entradas,
			Entrada entrada) {
		for (Entrada entradai : entradas) {
			if (entradai.getTerminoGlosario() != null)
				if (entradai.getTerminoGlosario().getId() == entrada
						.getTerminoGlosario().getId()) {
					if (entradai.getCasoUso().getId() == entrada.getCasoUso()
							.getId()) {
						return true;
					}
				}
		}
		return false;
	}

	public static boolean duplicadoTermino_Salidas(Set<Salida> salidas,
			Salida salida) {
		for (Salida salidai : salidas) {
			if (salidai.getTerminoGlosario() != null)
				if (salidai.getTerminoGlosario().getId() == salida
						.getTerminoGlosario().getId()) {
					if (salidai.getCasoUso().getId() == salida.getCasoUso()
							.getId()) {
						return true;
					}
				}
		}
		return false;
	}

	public static void almacenarObjetosToken(ArrayList<Object> objetos,
			CasoUso casouso, TipoSeccion tipoSeccion) {

		// Secciones:
		CasoUsoActor casoUsoActor;
		Entrada entrada;
		Salida salida;
		CasoUsoReglaNegocio casoUsoReglas;

		// Elementos
		Actor actor;
		Atributo atributo;
		TerminoGlosario termino;
		Mensaje mensaje;
		ReglaNegocio reglaNegocio;

		for (Object objeto : objetos) {
			switch (ReferenciaEnum.getTipoRelacion(
					ReferenciaEnum.getTipoReferencia(objeto), tipoSeccion)) {
			case ACTOR_ACTORES:
				actor = (Actor) objeto;
				casoUsoActor = new CasoUsoActor(
						casouso, actor);
				if (!TokenBs.duplicadoActor_Actores(casouso.getActores(),
						casoUsoActor)) {
					casouso.getActores().add(casoUsoActor);
				}
				break;
			case ATRIBUTO_ENTRADAS:
				atributo = (Atributo) objeto;
				entrada = new Entrada(
						new TipoParametroDAO()
								.consultarTipoParametro("Atributo"), casouso);
				entrada.setAtributo(atributo);
				if (!TokenBs.duplicadoAtributo_Entradas(casouso.getEntradas(),
						entrada)) {
					casouso.getEntradas().add(entrada);
				}
				break;
			case TERMINOGLS_ENTRADAS:
				termino = (TerminoGlosario) objeto;
				entrada = new Entrada(
						new TipoParametroDAO()
								.consultarTipoParametro("Término del glosario"),
						casouso);
				entrada.setTerminoGlosario(termino);
				if (!TokenBs.duplicadoTermino_Entradas(casouso.getEntradas(),
						entrada)) {
					casouso.getEntradas().add(entrada);
				}
				break;
			case TERMINOGLS_SALIDAS:
				termino = (TerminoGlosario) objeto;
				salida = new Salida(
						new TipoParametroDAO()
								.consultarTipoParametro("Término del glosario"),
						casouso);
				salida.setTerminoGlosario(termino);
				if (!TokenBs.duplicadoTermino_Salidas(casouso.getSalidas(),
						salida)) {
					casouso.getSalidas().add(salida);
				}
				break;
			case MENSAJE_SALIDAS:
				mensaje = (Mensaje) objeto;
				salida = new Salida(new TipoParametroDAO()
								.consultarTipoParametro("Mensaje"), casouso);
				salida.setMensaje(mensaje);
				if (!TokenBs.duplicadoMensaje_Salidas(casouso.getSalidas(),
						salida)) {
					casouso.getSalidas().add(salida);
				}
				break;
			case ATRIBUTO_SALIDAS:
				atributo = (Atributo) objeto;
				salida = new Salida(new TipoParametroDAO()
								.consultarTipoParametro("Atributo"), casouso);
				salida.setAtributo(atributo);
				if (!TokenBs.duplicadoAtributo_Salidas(casouso.getSalidas(),
						salida)) {
					casouso.getSalidas().add(salida);
				}
				break;
			case REGLANEGOCIO_REGLASNEGOCIOS:
				reglaNegocio = (ReglaNegocio) objeto;
				casoUsoReglas = new CasoUsoReglaNegocio(casouso, reglaNegocio);
				casoUsoReglas.setReglaNegocio(reglaNegocio);
				if (!TokenBs.duplicadoRegla_Reglas(casouso.getReglas(),
						casoUsoReglas)) {
					casouso.getReglas().add(casoUsoReglas);
				}
				break;

			default:
				break;

			}
		}
	}

	public static void almacenarObjetosToken(ArrayList<Object> objetos,
			CasoUso casouso, TipoSeccion tipoSeccion,
			PostPrecondicion postPrecondicion) {


		// Elementos
		ReferenciaParametro referenciaParametro = null;
		Accion accion;
		Atributo atributo;
		Actor actor;
		TipoParametro tipoParametro;
		CasoUso casoUso;
		Entidad entidad;
		Mensaje mensaje;
		Pantalla pantalla;
		ReglaNegocio reglaNegocio;
		Paso paso;
		TerminoGlosario terminoGlosario;
		Trayectoria trayectoria;

		for (Object objeto : objetos) {
			switch (ReferenciaEnum.getTipoRelacion(
					ReferenciaEnum.getTipoReferencia(objeto), tipoSeccion)) {

			case ACCION_POSTPRECONDICIONES:
				accion = (Accion) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Acción");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setAccionDestino(accion);

				break;
			case ACTOR_POSTPRECONDICIONES:
				actor = (Actor) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Actor");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(actor);
				break;
			case ATRIBUTO_POSTPRECONDICIONES:
				atributo = (Atributo) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Atributo");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setAtributo(atributo);

				break;
			case CASOUSO_POSTPRECONDICIONES:
				casoUso = (CasoUso) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Caso de uso");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(casoUso);
				break;
			case ENTIDAD_POSTPRECONDICIONES:
				entidad = (Entidad) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Entidad");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(entidad);
				break;
			case MENSAJE_POSTPRECONDICIONES:
				mensaje = (Mensaje) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Mensaje");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(mensaje);
				break;
			case PANTALLA_POSTPRECONDICIONES:
				pantalla = (Pantalla) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Pantalla");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(pantalla);

				break;
			case PASO_POSTPRECONDICIONES:
				paso = (Paso) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Paso");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setPasoDestino(paso);
				break;
			case REGLANEGOCIO_POSTPRECONDICIONES:
				reglaNegocio = (ReglaNegocio) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Regla de negocio");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(reglaNegocio);
				break;

			case TERMINOGLS_POSTPRECONDICIONES:
				terminoGlosario = (TerminoGlosario) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Término del glosario");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(terminoGlosario);
				break;
			case TRAYECTORIA_POSTPRECONDICIONES:
				trayectoria = (Trayectoria) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Trayectoria");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setTrayectoria(trayectoria);
				break;
			default:
				break;

			}
			if (referenciaParametro != null) {
				postPrecondicion.getReferencias().add(referenciaParametro);
				referenciaParametro.setPostPrecondicion(postPrecondicion);
			}

		}

	}

	public static void almacenarObjetosToken(ArrayList<Object> objetos,
			TipoSeccion tipoSeccion, Extension extension) {

		// Elementos
		ReferenciaParametro referenciaParametro = null;
		TipoParametro tipoParametro;
		Paso paso;

		for (Object objeto : objetos) {
			switch (ReferenciaEnum.getTipoRelacion(
					ReferenciaEnum.getTipoReferencia(objeto), tipoSeccion)) {

			case PASO_EXTENSIONES:
				paso = (Paso) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Paso");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setPasoDestino(paso);
				break;
			default:
				break;
			}
			if (referenciaParametro != null) {
				extension.getReferencias().add(referenciaParametro);
				referenciaParametro.setExtension(extension);
			}
		}
	}

	public static void almacenarObjetosToken(ArrayList<Object> objetos,
			TipoSeccion tipoSeccion, Paso paso) {

		// Elementos
		ReferenciaParametro referenciaParametro = null;
		Accion accion;
		Atributo atributo;
		Actor actor;
		TipoParametro tipoParametro;
		CasoUso casoUso;
		Entidad entidad;
		Mensaje mensaje;
		Pantalla pantalla;
		ReglaNegocio reglaNegocio;
		TerminoGlosario terminoGlosario;
		Trayectoria trayectoria;
		Paso pasoDestino;
		for (Object objeto : objetos) {
			switch (ReferenciaEnum.getTipoRelacion(
					ReferenciaEnum.getTipoReferencia(objeto), tipoSeccion)) {

			case ACCION_PASOS:
				accion = (Accion) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Acción");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setAccionDestino(accion);

				break;
			case ACTOR_PASOS:
				actor = (Actor) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Actor");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(actor);
				break;
			case ATRIBUTO_PASOS:
				atributo = (Atributo) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Atributo");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setAtributo(atributo);

				break;
			case CASOUSO_PASOS:
				casoUso = (CasoUso) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Caso de uso");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(casoUso);
				break;
			case ENTIDAD_PASOS:
				entidad = (Entidad) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Entidad");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(entidad);
				break;
			case MENSAJE_PASOS:
				mensaje = (Mensaje) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Mensaje");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(mensaje);
				break;
			case PANTALLA_PASOS:
				pantalla = (Pantalla) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Pantalla");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(pantalla);

				break;
			case PASO_PASOS:
				pasoDestino = (Paso) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Paso");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setPasoDestino(pasoDestino);
				break;
			case REGLANEGOCIO_PASOS:
				reglaNegocio = (ReglaNegocio) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Regla de negocio");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(reglaNegocio);
				break;

			case TERMINOGLS_PASOS:
				terminoGlosario = (TerminoGlosario) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Término del glosario");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setElementoDestino(terminoGlosario);
				break;
			case TRAYECTORIA_PASOS:
				trayectoria = (Trayectoria) objeto;
				tipoParametro = new TipoParametroDAO()
						.consultarTipoParametro("Trayectoria");
				referenciaParametro = new ReferenciaParametro(tipoParametro);
				referenciaParametro.setTrayectoria(trayectoria);
				break;
			default:
				break;

			}
			if (referenciaParametro != null) {
				paso.getReferencias().add(referenciaParametro);
				referenciaParametro.setPaso(paso);
			}
		}

	}

	public static String agregarReferencias(String actionContext,
			String redaccion, String target) {
		if (redaccion == null || redaccion.isEmpty()) {
			return "Sin información";
		}
		if (redaccion.charAt(0) == '$') {
			redaccion = redaccion.substring(1);
		}
		ArrayList<String> tokens = TokenBs.procesarTokenIpunt(redaccion);
		for (String token : tokens) {
			ArrayList<String> segmentos = TokenBs.segmentarToken(token);
			String tokenReferencia = segmentos.get(0);
			int id = Integer.parseInt(segmentos.get(1));
			switch (ReferenciaEnum.getTipoReferencia(tokenReferencia)) {
			case ACCION:
				Accion accion = new AccionDAO().consultarAccion(Integer
						.parseInt(segmentos.get(1)));
				if (accion == null) {
					redaccion = "";
					break;
				} else {
					Pantalla pantalla = accion.getPantalla();
					redaccion = remplazoToken(
							redaccion,
							token,
							"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
									+ "/pantallas/" + pantalla.getId() 
									+ "#accion-" + id
									+ "'>"
									+ accion.getNombre() + "</a>");
				}
				break;
			case ACTOR:
				Actor actor = new ActorDAO().consultarActor(Integer
						.parseInt(segmentos.get(1)));
				if (actor == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(redaccion, token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
								+ "/actores/" + id + "'>" + actor.getNombre()
								+ "</a>");
				break;
			case ATRIBUTO:
				Atributo atributo = new AtributoDAO().consultarAtributo(Integer
						.parseInt(segmentos.get(1)));
				if (atributo == null) {
					redaccion = "";
					break;
				} else {
					Entidad entidad = atributo.getEntidad();
					redaccion = remplazoToken(
							redaccion,
							token,
							"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
									+ "/entidades/" + entidad.getId()
									+ "#atributo-" + id + "'>"
									+ atributo.getNombre() + "</a>");
				}
				break;
			case CASOUSO:
				CasoUso casoUso = new CasoUsoDAO().consultarCasoUso(Integer
						.parseInt(segmentos.get(1)));
				if (casoUso == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext + "/cu/"
								+ id + "'>" + casoUso.getClave() + " "
								+ casoUso.getNumero() + " "
								+ casoUso.getNombre() + "</a>");

				break;
			case ENTIDAD: // ENT.ID -> ENT.NOMBRE_ENT
				Entidad entidad = new EntidadDAO().consultarEntidad(Integer
						.parseInt(segmentos.get(1)));
				if (entidad == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
								+ "/entidades/" + id + "'>"
								+ entidad.getNombre() + "</a>");

				break;
			case TERMINOGLS: // GLS.ID -> GLS.NOMBRE_GLS
				TerminoGlosario terminoGlosario = new TerminoGlosarioDAO()
						.consultarTerminoGlosario(Integer.parseInt(segmentos
								.get(1)));
				if (terminoGlosario == null) {
					redaccion = "";
					break;
				} else {
					redaccion = remplazoToken(redaccion, token,
							"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
									+ "/glosario/" + id + "'>"
									+ terminoGlosario.getNombre() + "</a>");
				}
				break;
			case PANTALLA: // IU.ID -> // IU.MODULO.NUMERO:NOMBRE_IU
				Pantalla pantalla = new PantallaDAO().consultarPantalla(Integer
						.parseInt(segmentos.get(1)));
				if (pantalla == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext + "/pantallas/" + id +"'>" + pantalla.getClave()
								+ " " + pantalla.getNumero() + " "
								+ pantalla.getNombre() + "</a>");
				break;

			case MENSAJE: // GLS.ID -> MSG.NUMERO:NOMBRE_MSG
				Mensaje mensaje = new MensajeDAO().consultarMensaje(Integer
						.parseInt(segmentos.get(1)));
				if (mensaje == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
								+ "/mensajes/" + id + "'>" + mensaje.getClave()
								+ " " + mensaje.getNumero() + " "
								+ mensaje.getNombre() + "</a>");
				break;
			case REGLANEGOCIO: // RN.ID -> RN.NUMERO:NOMBRE_RN
				ReglaNegocio reglaNegocio = new ReglaNegocioDAO()
						.consultarReglaNegocio(Integer.parseInt(segmentos
								.get(1)));
				if (reglaNegocio == null) {
					redaccion = "";
					break;
				}
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext
								+ "/reglas-negocio/" + id + "'>"
								+ reglaNegocio.getClave() + " "
								+ reglaNegocio.getNumero() + " "
								+ reglaNegocio.getNombre() + "</a>");
				break;
			case TRAYECTORIA: // TRAY.ID -> TRAY.CUMODULO.NUM:NOMBRECU:CLAVETRAY
				Trayectoria trayectoria = new TrayectoriaDAO()
						.consultarTrayectoria(Integer.parseInt(segmentos.get(1)));
				if (trayectoria == null) {
					redaccion = "";
					break;
				}
				CasoUso cu = trayectoria.getCasoUso();
				redaccion = remplazoToken(redaccion, token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext + "/cu/"
								+ cu.getId() + "#trayectoria-" + id + "'>"
								+ trayectoria.getClave() + "</a>");
				break;

			case PASO: // P.CUMODULO.NUM:NOMBRECU:CLAVETRAY.NUMERO
				Paso paso = new PasoDAO().consultarPaso(Integer
						.parseInt(segmentos.get(1)));
				if (paso == null) {
					redaccion = "";
					break;
				}
				CasoUso cup = paso.getTrayectoria().getCasoUso();
				redaccion = remplazoToken(
						redaccion,
						token,
						"<a target='" + target + "'" +  "class='referencia' href='" + actionContext + "/cu/"
								+ cup.getId() + "#paso-" + id + "'>"
								+ paso.getNumero() + "</a>");
				break;

			default:
				break;
			}
		}

		redaccion = redaccion.replace("\r\n", "<br/>");
		redaccion = redaccion.replace("\n", "<br/>");
		redaccion = redaccion.replace("\r", "<br/>");

		return redaccion;

	}

	public static void errorEnToken(String articulo, String elemento) {
		String[] parametros = { articulo, elemento, };

		throw new PRISMAValidacionException(
				"TokenBs.errorEnToken: El token ingresado para " + articulo
						+ " " + elemento + " es inválido.", "MSG27", parametros);
	}

	public static boolean ignorarEscape(String cadena, int i, char caracter) {
		if (puntoSeguido(cadena, i, caracter) || espacio(cadena, i, caracter)
				|| coma(cadena, i, caracter) || puntoComa(cadena, i, caracter)
				|| caracter == '\n' || caracter == '\t' || caracter == '\r') {
			return true;
		}

		return false;
	}

	private static boolean coma(String cadena, int i, char caracter) {
		if (caracter == ',') {
			if (cadena.length() - 1 > i) {
				return true;
			}
		}
		return false;
	}

	public static boolean espacio(String cadena, int i, char caracter) {
		if (caracter == ' ') {
			return true;
		}
		return false;
	}

	public static boolean dospuntos(String cadena, int i, char caracter) {
		if (caracter == ':') {
			return true;
		}
		return false;
	}

	public static boolean puntoComa(String cadena, int i, char caracter) {
		if (caracter == ';') {
			return true;
		}
		return false;
	}

	public static boolean esToken(String pila) {
		if (pila.equals(tokenRN) || pila.equals(tokenENT)
				|| pila.equals(tokenCU) || pila.equals(tokenIU)
				|| pila.equals(tokenMSG) || pila.equals(tokenACT)
				|| pila.equals(tokenGLS) || pila.equals(tokenATR)
				|| pila.equals(tokenP) || pila.equals(tokenTray)
				|| pila.equals(tokenACC) || pila.equals(tokenPARAM)) {
			return true;
		}
		return false;
	}
	
	public static boolean esToken_(String pila) {
		if (pila.contains(tokenRN) || pila.contains(tokenENT)
				|| pila.contains(tokenCU) || pila.contains(tokenIU)
				|| pila.contains(tokenMSG) || pila.contains(tokenACT)
				|| pila.contains(tokenGLS) || pila.contains(tokenATR)
				|| pila.contains(tokenP) || pila.contains(tokenTray)
				|| pila.contains(tokenACC) || pila.contains(tokenPARAM)) {
			return true;
		}
		return false;
	}

	public static boolean puntoFinal(int longitud, int i, char caracter) {
		if (caracter == '.' && longitud - 1 == i) {
			return true;
		}
		return false;
	}

	public static boolean puntoSeguido(String cadena, int i, char caracter) {
		if (caracter == '.') {
			if (cadena.length() - 1 > i) {
				if (cadena.charAt(i + 1) == ' ') {
					return true;
				} else {
					return false;
				}

			}
		}

		return false;
	}

}
