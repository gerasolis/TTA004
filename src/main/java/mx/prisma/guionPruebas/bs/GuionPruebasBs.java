package mx.prisma.guionPruebas.bs;

//Librerías
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import mx.prisma.editor.bs.EntradaBs;
import mx.prisma.editor.bs.ReglaNegocioBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ValorEntrada;

public class GuionPruebasBs {

	// Función para obtener la trayectoria principal
	public static Trayectoria trayectoriaPrincipal(CasoUso casoUso) {
		// Creamos la trayectoria
		Trayectoria tp = null;

		for (Trayectoria trayectoria : casoUso.getTrayectorias()) {
			// Si es la trayectoria principal
			if (!trayectoria.isAlternativa()) {
				tp = trayectoria;
			}
		}

		// Retornamos la trayectoria principal
		return tp;
	}

	/*@SuppressWarnings("null")
	public static List<String> obtenerInstrucciones(List<Paso> pasos, CasoUso casoUso) {
		

		return instrucciones;
	}*/

	public static List<String> obtenerTokens(Paso paso) {
		// Creamos una lista para asignar los tokens encontrados
		List<String> tokens = new ArrayList<String>();
		// Arreglo de cadenas para las palabras de la redacción del paso
		String[] palabrasRedaccion;

		// Separamos las palabras de la redacción del paso
		palabrasRedaccion = paso.getRedaccion().split(" ");

		for (String token : palabrasRedaccion) {
			// Si la palabra es un token
			if (TokenBs.esToken_(token)) {
				// Se agrega al arreglo de tokens
				tokens.add(token);
			}
		}

		// Regresamos los tokens
		return tokens;
	}

	public static String compararTokenUsuario(String actionContext, Paso paso, String token, Set<Entrada> entradas) {
		String instruccion = "";
		
		// Si es una acción (ACC·#)
		if (token.contains(TokenBs.tokenACC)) {
			System.out.println("Entró a Acción");
			String accion = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Oprime") || paso.getVerbo().getNombre().equals("Confirma")
					|| paso.getVerbo().getNombre().equals("Ejecuta") || paso.getVerbo().getNombre().equals("Reproduce")
					|| paso.getVerbo().getNombre().equals("Selecciona")
					|| paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Accede")
					|| paso.getVerbo().getNombre().equals("Ingresa")) {
				instruccion = "Dé click en el botón " + accion;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Descarga")) {
				instruccion = "Dé click en el botón " + accion + " para descargar el archivo";
				System.out.println(instruccion);
			}
		}
		// Si es un atributo
		if (token.contains(TokenBs.tokenATR)) {
			String atributo = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Mueve")) {
				instruccion = "Mueva el campo " + atributo;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Selecciona")) {
				instruccion = "Seleccione el campo " + atributo;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Ingresa")
					|| paso.getVerbo().getNombre().equals("Registra")) {
				instruccion = "Ingrese en el campo " + atributo + ": ";
				// Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for (Entrada e : entradas) {
					if (e.getAtributo().getId() == a.getId()) {
						instruccion += obtenerValorEntrada(e);
					}
				}
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Adjunta")) {
				instruccion = "Adjunta el archivo /*VALOR*/ en el campo " + atributo;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Busca")) {
				instruccion = "Busque el atributo " + atributo;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Modifica")) {
				instruccion = "Modifique el valor del campo " + atributo + " a: ";
				// Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for (Entrada e : entradas) {
					if (e.getAtributo().getId() == a.getId()) {
						instruccion += obtenerValorEntrada(e);
					}
				}
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Elimina")) {
				instruccion = "Elimine el valor del campo " + atributo;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Restablece")) {
				instruccion = "Restablezca el valor del campo " + atributo + " a: ";
				// Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for (Entrada e : entradas) {
					if (e.getAtributo().getId() == a.getId()) {
						instruccion += obtenerValorEntrada(e);
					}
				}
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
				instruccion = "Sustituya el valor del campo " + atributo + " a: ";
				// Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for (Entrada e : entradas) {
					if (e.getAtributo().getId() == a.getId()) {
						instruccion += obtenerValorEntrada(e);
					}
				}
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion = "¿Es correcta la información en el campo " + atributo + "?";
				System.out.println(instruccion);
			}
		}
		// Si es una entidad (ENT·#)
		if (token.contains(TokenBs.tokenENT)) {
			String entidad = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Mueve")) {
				instruccion = "Mueva la entidad " + entidad;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Selecciona")) {
				instruccion = "Seleccione la entidad " + entidad;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Ingresa")
					|| paso.getVerbo().getNombre().equals("Registra")) {
				instruccion = "Ingrese en la entidad " + entidad + " la siguiente información: ";// Falta
																									// agregar
																									// entrada:valor
				// Obtenemos el valor de las entradas
				for (Entrada e : entradas) {
					instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e) + " ";
				}
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Busca")) {
				instruccion = "Busque la entidad " + entidad;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Modifica")) {
				instruccion = "Modifique los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
				System.out.println(instruccion);																		// agregar
																													// entrada:valor
			} else if (paso.getVerbo().getNombre().equals("Elimina")) {
				instruccion = "Elimine la información de la entidad " + entidad;
			} else if (paso.getVerbo().getNombre().equals("Restablece")) {
				instruccion = "Restablezca los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
				System.out.println(instruccion);																						// agregar
																														// entrada:valor
			} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
				instruccion = "Sustituya los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
																													// agregar
				System.out.println(instruccion);																							// entrada:valor
			} else if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion = "¿Es correcta la información en la entidad " + entidad + "?";
				System.out.println(instruccion);
			}
		}
		// Si es una pantalla (IU·#)
		if (token.contains(TokenBs.tokenIU)) {
			String pantalla = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Ejecuta")) {
				instruccion = "Ejecute la pantalla " + pantalla;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Selecciona")) {
				instruccion = "Seleccione la pantalla " + pantalla;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Ingresa")
					|| paso.getVerbo().getNombre().equals("Gestiona")) {
				System.out.println("ENTRO AL IF DE IU");
				instruccion = "Ingrese a la pantalla " + pantalla;
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion = "¿Es correcta la pantalla " + pantalla + "?";
				System.out.println(instruccion);
			}
		}
		System.out.println("ANTES DEL RETURN: "+instruccion);
		return instruccion;
	}

	public static List<String> compararTokenSistema(String actionContext, Paso paso, String token, CasoUso casoUso) {
		List<String> instruccion = new ArrayList<String>();
		
		// Si es una acción (ACC·#)
		if (token.contains(TokenBs.tokenACC)) {
			String accion = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Ejecuta")) {
				instruccion.add("¿Se ejecutó correctamente " + accion + "?");
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Reproduce")) {
				instruccion.add("¿Se reprodujo correctamente " + accion + "?");
				System.out.println(instruccion);
			} else if (paso.getVerbo().getNombre().equals("Habilita")) {
				instruccion.add("¿Se habilitó correctamente el botón " + accion + "?");
			} else if (paso.getVerbo().getNombre().equals("Oculta")) {
				instruccion.add("¿Se ocultó correctamente " + accion + "?");
			}
		}
		// Si es un atributo (ATR·#)
		if (token.contains(TokenBs.tokenATR)) {
			String atributo = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Mueve")) {
				instruccion.add("¿Se movió correctamente el campo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Habilita")) {
				instruccion.add("¿Se habilitó correctamente el campo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Oculta")) {
				instruccion.add("¿Se ocultó correctamente el campo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Calcula")) {
				instruccion.add("¿Se calculó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Crea")) {
				instruccion.add("¿Se creó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Descarga")) {
				instruccion.add("¿Se descargó correctamente?");
			} else if (paso.getVerbo().getNombre().equals("Busca")) {
				instruccion.add("¿Se buscó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Registra")) {
				instruccion.add("¿Se registró correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Modifica")) {
				instruccion.add("¿Se modificó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Elimina")) {
				instruccion.add("¿Se eliminó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Restablece")) {
				instruccion.add("¿Se restableció correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
				instruccion.add("¿Se sustituyó correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Envía")) {
				instruccion.add("¿Se envió correctamente el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Muestra")) {
				instruccion.add("¿Se muestra correctamente la información en el atributo " + atributo + "?");
			} else if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion.add("¿Se verificó correctamente el atributo " + atributo + "?");
			}
		}
		// Si es un entidad (ENT·#)
		if (token.contains(TokenBs.tokenENT)) {
			String entidad = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Mueve")) {
				instruccion.add("¿Se movió correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Habilita")) {
				instruccion.add("¿Se habilitó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Oculta")) {
				instruccion.add("¿Se ocultó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Crea")) {
				instruccion.add("¿Se creó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Busca")) {
				instruccion.add("¿Se buscó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Registra")) {
				instruccion.add("¿Se registró correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Modifica")) {
				instruccion.add("¿Se modificó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Elimina")) {
				instruccion.add("¿Se eliminó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Restablece")) {
				instruccion.add("¿Se restableció correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
				instruccion.add("¿Se sustituyó correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Envía")) {
				instruccion.add("¿Se envió correctamente la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Muestra")) {
				instruccion.add("¿Se muestra correctamente la información en la entidad " + entidad + "?");
			} else if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion.add("¿Se verificó correctamente la entidad " + entidad + "?");
			}
		}
		// Si es una pantalla (IU·#)
		if (token.contains(TokenBs.tokenIU)) {
			String pantalla = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Habilita")) {
				instruccion.add("¿Se habilitó correctamente la pantalla " + pantalla + "?");
			} else if (paso.getVerbo().getNombre().equals("Oculta")) {
				instruccion.add("¿Se ocultó correctamente la pantalla " + pantalla + "?");
			} else if (paso.getVerbo().getNombre().equals("Accede") || paso.getVerbo().getNombre().equals("Muestra")) {
				instruccion.add("¿Se muestra correctamente la pantalla " + pantalla + "?");
			} else if (paso.getVerbo().getNombre().equals("Envía")) {
				instruccion.add("¿Se envió correctamente la pantalla " + pantalla + "?");
			}
		}
		// Si es una regla de negocio (RN·#)
		if (token.contains(TokenBs.tokenRN)) {
			// Obtenemos la regla de negocio mediante el token
			String rn = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Obtenemos la redacción de la RN
			ReglaNegocio redaccionRN = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" " + token);
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Verifica")) {
				instruccion.add("¿Se cumple la regla de negocio " + rn + ": " + redaccionRN.getRedaccion() + "?");
			}
		}
		// Si es un mensaje (MSG·#)
		if (token.contains(TokenBs.tokenMSG)) {
			// Obtenemos el mensaje mediante el token
			String mensaje = TokenBs.agregarReferencias(actionContext, token,"_blank");
			// Obtenemos la redacción del mensaje
			Mensaje redaccionMensaje = (Mensaje) TokenBs.obtenerTokenObjeto(" " + token);
			// Comparamos el verbo
			if (paso.getVerbo().getNombre().equals("Envía")) {
				instruccion.add("¿Se envió correctamente el mensaje " + mensaje + ": " + redaccionMensaje.getRedaccion()
						+ "?");
			} else if (paso.getVerbo().getNombre().equals("Muestra")) {
				instruccion.add("¿Se muestra correctamente el mensaje " + mensaje + ": " + redaccionMensaje.getRedaccion()
						+ "?");
			}
		}
		if (token.contains(TokenBs.tokenTray)) {
			//Obtenemos la trayectoria
			Trayectoria trayectoria = (Trayectoria) TokenBs.obtenerTokenObjeto(" "+token);
			//Consultamos los pasos de la trayectoria alternativa
			List<Paso> pasosT = TrayectoriaBs.obtenerPasos_(trayectoria.getId());
			// Obtenemos el total de pasos de la trayectora principal
			int tpasos = pasosT.size();
			// Consultamos las entradas del caso de uso
			Set<Entrada> entradas = casoUso.getEntradas();
			
			for (int i = 0; i < tpasos; i++) {
				// Consultamos el paso actual
				Paso pasoA = pasosT.get(i);
				
				System.out.println("PASO ALTERNATIvo: "+pasoA.getNumero()+": "+pasoA.getRedaccion());

				// Obtenemos los tokens del paso
				List<String> tokens = GuionPruebasBs.obtenerTokens(pasoA);

				// Comparación de los tokens
				for (String tokenA : tokens) {
					// Si el actor es el USUARIO
					if (paso.isRealizaActor()) {
						if (!GuionPruebasBs.compararTokenUsuario(actionContext, pasoA, tokenA, entradas).equals(""))
							instruccion.add(GuionPruebasBs.compararTokenUsuario(actionContext, pasoA, tokenA, entradas));
					}
					// Si el actor es el SISTEMA
					else {
						if (!GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso).equals(""))
							instruccion.addAll(GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso));
					}
				}
			}
		}
		return instruccion;
	}

	// Función para obtener el valor de la entrada desde el archivo
	private static String obtenerValorEntrada(Entrada entrada) {
		String valor = "";
		
		ValorEntradaDAO vedao = new ValorEntradaDAO();
		List<ValorEntrada> valores = vedao.consultarValores(entrada);
		for(ValorEntrada valorEntrada : valores){
			if(valorEntrada.getSeleccionada() && valorEntrada.getEntrada().getId().equals(entrada.getId())){
				valor = valorEntrada.getValor();
			}
		}

		return valor;
	}

}