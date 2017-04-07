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
import mx.prisma.editor.bs.MensajeBs;
import mx.prisma.editor.bs.MensajeParametroBs;
import mx.prisma.editor.bs.PasoBs;
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
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroBs;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.guionPruebas.dao.GuionPruebaDAO;
import mx.prisma.guionPruebas.dao.InstruccionDAO;
import mx.prisma.guionPruebas.model.GuionPrueba;
import mx.prisma.guionPruebas.model.Instruccion;

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

public static List<String> obtenerTokens(Mensaje mensaje) {
	// Creamos una lista para asignar los tokens encontrados
		List<String> tokens = new ArrayList<String>();
	// Arreglo de cadenas para las palabras de la redacción del paso
		String[] palabrasRedaccion;

	// Separamos las palabras de la redacción del paso
		palabrasRedaccion = mensaje.getRedaccion().split(" ");

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

/*********AGREGAR AL NUEVO***********/
public static String compararTokenUsuario(String actionContext, Paso paso, String token, Set<Entrada> entradas, ReglaNegocio reglaNegocio) {
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
//Si es una RN
if (token.contains(TokenBs.tokenRN)) {
	instruccion = "Ingrese la siguiente información: ";
// Obtenemos el valor de las entradas
	for (Entrada e : entradas) {
		instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
	}
}
// Si es un atributo
if (token.contains(TokenBs.tokenATR)) {
	System.out.println("Esá entrado al fi con la rn");
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
			}
		}
		System.out.println(instruccion);
	} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
		instruccion = "Sustituya el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
		Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
		for (Entrada e : entradas) {
			if (e.getAtributo().getId() == a.getId()) {
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
	System.out.println("Esá entrado al fi con la rn");
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
	instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
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

/*********AGREGAR AL NUEVO***********/
public static String compararTokenUsuarioBD(String actionContext, Paso paso, String token, Set<Entrada> entradas, ReglaNegocio reglaNegocio) {
	String instruccion = "";

// Si es una acción (ACC·#)
	if (token.contains(TokenBs.tokenACC)) {
		System.out.println("Entró a Acción");
		String accion = TokenBs.decodificarCadenaSinToken(token);
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
//Si es una RN
if (token.contains(TokenBs.tokenRN)) {
	instruccion = "Ingrese la siguiente información: ";
// Obtenemos el valor de las entradas
	for (Entrada e : entradas) {
		instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
	}
}	
// Si es un atributo
if (token.contains(TokenBs.tokenATR)) {
	String atributo = TokenBs.decodificarCadenaSinToken(token);
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
				instruccion += obtenerValorEntrada(e, reglaNegocio);
			}
		}
		System.out.println(instruccion);
	} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
		instruccion = "Sustituya el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
		Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
		for (Entrada e : entradas) {
			if (e.getAtributo().getId() == a.getId()) {
				instruccion += obtenerValorEntrada(e, reglaNegocio);
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
	String entidad = TokenBs.decodificarCadenaSinToken(token);
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
	instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
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
	String pantalla = TokenBs.decodificarCadenaSinToken(token);
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

public static List<String> compararTokenSistema(String actionContext, Paso paso, String token, CasoUso casoUso, int idGuionPrueba) {
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
	/*if (token.contains(TokenBs.tokenRN)) {
// Obtenemos la regla de negocio mediante el token
		String rn = TokenBs.agregarReferencias(actionContext, token,"_blank");
// Obtenemos la redacción de la RN
		ReglaNegocio redaccionRN = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" " + token);
// Comparamos el verbo
		if (paso.getVerbo().getNombre().equals("Verifica")) {
			instruccion.add("¿Se cumple la regla de negocio " + rn + ": " + redaccionRN.getRedaccion() + "?");
		}
	}*/
	// Si es un mensaje (MSG·#)
	if (token.contains(TokenBs.tokenMSG)) {
		String resultado="";
		int contador2=0;
		String cadena="";
		String cadenaUsar="";
		// Obtenemos el mensaje mediante el token
		String mensaje = TokenBs.agregarReferencias(actionContext, token,"_blank");
		// Obtenemos la redacción del mensaje
		for(ReferenciaParametro rp : PasoBs.obtenerReferencias(paso.getId())){
			if(rp.getTipoParametro().getId() == 6){
				Mensaje m = MensajeBs.consultarMensaje(rp.getElementoDestino().getId());
				//System.out.println(m.getRedaccion());
				//Aquí sustituyo los token y comparo con los mensajes de la prueba.
				//primero cuento la cantidad de tokens sólo si es de tipo parametrizado.
				if(m.isParametrizado()){
					cadena = m.getRedaccion();
					contador2=0;
					while(cadena.indexOf("PARAM")!=-1){
						cadena = cadena.substring(cadena.indexOf("PARAM")+"PARAM".length(),cadena.length());
						contador2++;
					}
					System.out.println(contador2);
					for(int z=0;z<contador2;z++){
						if(z==0){
							cadenaUsar = m.getRedaccion();
						}
						for(MensajeParametro mp : MensajeParametroBs.consultarMensajeParametro_(m.getId())){
							for(ValorMensajeParametro v : ValorMensajeParametroBs.consultarValores_(mp.getId())){
								if(v.getReferenciaParametro().getPaso().getRedaccion().equals(paso.getRedaccion())){
									//System.out.println(v.getValor());
									
									if(v.getMensajeParametro().getParametro().getNombre().equals("DETERMINADO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"$PARAM·1", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("ENTIDAD")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·2", v.getValor());	
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("OPERACIÓN")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·3", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("TIPO_DATO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·4", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("TAMAÑO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·5", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("UNIDAD_TIPO_DATO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·6", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("VALOR")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·7", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("ATRIBUTO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·8", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("CONTRASEÑA")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·9", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("NOMBRE")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·10", v.getValor());	
									}
								}
							}
							
						}
					}
					System.out.println("*********************************");
					System.out.println("CADENA A USAR: "+cadenaUsar);
					if(cadenaUsar.charAt(0) == '$'){
						cadenaUsar = cadenaUsar.substring(1,cadenaUsar.length());
						System.out.println("CADENA A USAR SIN $: "+cadenaUsar);
					}
					System.out.println("*********************************");
				}else{
					cadena = m.getRedaccion();
					System.out.println("*********************************");
					System.out.println("CADENA NO PARAMETRIZADA A USAR: "+cadena);
					if(cadena.charAt(0) == '$'){
						cadena = cadena.substring(1,cadena.length());
						System.out.println("CADENA NO PARAMETRIZADA A USAR SIN $: "+cadena);
					}
				}//aquí acaba el else
			}
		}

		// Comparamos el verbo
		if (paso.getVerbo().getNombre().equals("Envía")) {
			instruccion.add("¿Se envió correctamente el mensaje " + mensaje + ": " + cadena
				+ "?");
		} else if (paso.getVerbo().getNombre().equals("Muestra")) {
			instruccion.add("¿Se muestra correctamente el mensaje " + mensaje + ": " + cadena
				+ "?");
		}
	}
	/*if (token.contains(TokenBs.tokenTray)) {
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
					if (!GuionPruebasBs.compararTokenUsuario(actionContext, pasoA, tokenA, entradas).equals("")){
						String ins = GuionPruebasBs.compararTokenUsuario(actionContext, pasoA, tokenA, entradas);
						instruccion.add(ins);
						ins = GuionPruebasBs.compararTokenUsuarioBD(actionContext, pasoA, tokenA, entradas);
						Instruccion instruc = new Instruccion(ins, idGuionPrueba, paso.getId());
						InstruccionDAO idao = new InstruccionDAO();
						idao.agregarInstruccion(instruc);

					}
				}
// Si el actor es el SISTEMA
				else {
					if (!GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso, idGuionPrueba).equals(""))
						instruccion.addAll(GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso, idGuionPrueba));
				}
			}
		}
	}*/
	return instruccion;
}



public static List<String> compararTokenSistemaBD(String actionContext, List<Trayectoria> trayectorias, Paso paso, String token, CasoUso casoUso, int idGuionPrueba) {
	List<String> instruccion = new ArrayList<String>();

// Si es una acción (ACC·#)
	if (token.contains(TokenBs.tokenACC)) {
		String accion = TokenBs.decodificarCadenaSinToken(token);
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
		String atributo = TokenBs.decodificarCadenaSinToken(token);
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
		String entidad = TokenBs.decodificarCadenaSinToken(token);
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
		String pantalla = TokenBs.decodificarCadenaSinToken(token);
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
		String rn = TokenBs.decodificarCadenaSinToken(token);
// Obtenemos la redacción de la RN
		ReglaNegocio redaccionRN = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" " + token);
// Comparamos el verbo
		if (paso.getVerbo().getNombre().equals("Verifica")) {
			instruccion.add("¿Se cumple la regla de negocio " + rn + ": " + redaccionRN.getRedaccion() + "?");
		}
	}
	// Si es un mensaje (MSG·#)
	if (token.contains(TokenBs.tokenMSG)) {
		String resultado="";
		int contador2=0;
		String cadena="";
		String cadenaUsar="";
		// Obtenemos el mensaje mediante el token
		String mensaje = TokenBs.decodificarCadenaSinToken(token);
		// Obtenemos la redacción del mensaje
		for(ReferenciaParametro rp : PasoBs.obtenerReferencias(paso.getId())){
			if(rp.getTipoParametro().getId() == 6){
				Mensaje m = MensajeBs.consultarMensaje(rp.getElementoDestino().getId());
				//System.out.println(m.getRedaccion());
				//Aquí sustituyo los token y comparo con los mensajes de la prueba.
				//primero cuento la cantidad de tokens sólo si es de tipo parametrizado.
				if(m.isParametrizado()){
					cadena = m.getRedaccion();
					contador2=0;
					while(cadena.indexOf("PARAM")!=-1){
						cadena = cadena.substring(cadena.indexOf("PARAM")+"PARAM".length(),cadena.length());
						contador2++;
					}
					System.out.println(contador2);
					for(int z=0;z<contador2;z++){
						if(z==0){
							cadenaUsar = m.getRedaccion();
						}
						for(MensajeParametro mp : MensajeParametroBs.consultarMensajeParametro_(m.getId())){
							for(ValorMensajeParametro v : ValorMensajeParametroBs.consultarValores_(mp.getId())){
								if(v.getReferenciaParametro().getPaso().getRedaccion().equals(paso.getRedaccion())){
									//System.out.println(v.getValor());
									if(v.getMensajeParametro().getParametro().getNombre().equals("DETERMINADO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"$PARAM·1", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("ENTIDAD")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·2", v.getValor());	
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("OPERACIÓN")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·3", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("TIPO_DATO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·4", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("TAMAÑO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·5", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("UNIDAD_TIPO_DATO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·6", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("VALOR")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·7", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("ATRIBUTO")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·8", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("CONTRASEÑA")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·9", v.getValor());
									}else if(v.getMensajeParametro().getParametro().getNombre().equals("NOMBRE")){
										cadenaUsar = TokenBs.remplazoToken(cadenaUsar,"PARAM·10", v.getValor());	
									}
								}
							}
							
						}
					}
					System.out.println("*********************************");
					System.out.println("CADENA A USAR: "+cadenaUsar);
					if(cadenaUsar.charAt(0) == '$'){
						cadenaUsar = cadenaUsar.substring(1,cadenaUsar.length());
						System.out.println("CADENA A USAR SIN $: "+cadenaUsar);
					}
					System.out.println("*********************************");
				}else{
					cadena = m.getRedaccion();
					System.out.println("*********************************");
					System.out.println("CADENA NO PARAMETRIZADA A USAR: "+cadena);
					if(cadena.charAt(0) == '$'){
						cadena = cadena.substring(1,cadena.length());
						System.out.println("CADENA NO PARAMETRIZADA A USAR SIN $: "+cadena);	
					}
				}//aquí acaba el else
			}
		}

		// Comparamos el verbo
		if (paso.getVerbo().getNombre().equals("Envía")) {
			instruccion.add("¿Se envió correctamente el mensaje " + mensaje + ": " + cadenaUsar
				+ "?");
		} else if (paso.getVerbo().getNombre().equals("Muestra")) {
			instruccion.add("¿Se muestra correctamente el mensaje " + mensaje + ": " + cadenaUsar
					+ "?");
		}
	}
		/*if (token.contains(TokenBs.tokenTray)) {
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
					if (!GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso, idGuionPrueba).equals(""))
						instruccion.addAll(GuionPruebasBs.compararTokenSistema(actionContext, pasoA, tokenA, casoUso, idGuionPrueba));
				}
			}
		}
	}*/
	return instruccion;
}

// Función para obtener el valor de la entrada desde el archivo
private static String obtenerValorEntrada(Entrada entrada, ReglaNegocio reglaNegocio) {
	String valor = "";

	ValorEntradaDAO vedao = new ValorEntradaDAO();
	List<ValorEntrada> valores = vedao.consultarValores(entrada);
	for(ValorEntrada valorEntrada : valores){
		if(valorEntrada.getEntrada().getId().equals(entrada.getId())){
			//Valor correcto
			System.out.println("*********************************");
			System.out.println(valorEntrada.getValor());
			System.out.println(reglaNegocio.getId());
			System.out.println(valorEntrada.getId());
			System.out.println(valorEntrada.getCorrecto_guion());
			System.out.println(valorEntrada.getAleatoriocorrecto_guion());
			if(valorEntrada.getValido()==true && (valorEntrada.getCorrecto_guion()==true || valorEntrada.getAleatoriocorrecto_guion()==true)){
				System.out.println("PINCHE VALIDO: "+valorEntrada.getValor());
				valor = valorEntrada.getValor();
				break;
			}else if(valorEntrada.getValido()==false && reglaNegocio.getId()==valorEntrada.getReglaNegocio().getId()){
				System.out.println("PINSHI NO VALIDO: "+valorEntrada.getValor());
				System.out.println(reglaNegocio.getId()+"="+valorEntrada.getReglaNegocio().getId());
				System.out.println(valorEntrada.getValor());
				valor = valorEntrada.getValor();
				break;
			}
		}
	}

	return valor;
}

//Función para obtener la entrada acorde a la RN
public static ReglaNegocio obtenerEntradasXRN(){

	return null;	
}

public static List<String> obtenerInstrucciones(CasoUso casoUso, List<Trayectoria> trayectorias, String contextPath) {
	/* Consultamos si existe el guión de pruebas para ese cu */
	GuionPruebaDAO gpdao = new GuionPruebaDAO();
	GuionPrueba guionPrueba = new GuionPrueba();
	// //System.out.println("Crea gpdao y guionprueba");

	// Si no existe un guion de prueba para ese CU, lo creamos
	if (gpdao.select(casoUso.getId()) == null) {
		gpdao = new GuionPruebaDAO();
		// //System.out.println("Entra al if");
		// Agregamos el elemento Guion de Prueba a la tabla Guion de Prueba
		guionPrueba = new GuionPrueba(casoUso.getId());
		guionPrueba.setSeleccionado(false);
		gpdao.insert(guionPrueba);
	}
	// Si ya existe, consultamos el guion de prueba
	else {
		gpdao = new GuionPruebaDAO();
		guionPrueba = gpdao.select(casoUso.getId());
		// Eliminamos las instrucciones de ese guión
		InstruccionDAO idao = new InstruccionDAO();
		idao.delete(guionPrueba.getIdGuionPrueba());
	}
	
	/**CREAMOS EL GUIÓN**/
	// Lista de las instrucciones
	List<String> instrucciones = new ArrayList<String>();

	ReglaNegocio reglaNegocio = new ReglaNegocio();
	/*******AGREGAR AL NUEVO*******/
	//Lista de Reglas de negocio 
	List<ReglaNegocio> reglasNegocio = new ArrayList<ReglaNegocio>();
	//Consultamos la trayectoria principal
	//Trayectoria trayectoriaPrincipal = GuionPruebasBs.trayectoriaPrincipal(casoUso);
	for(Trayectoria trayectoriaPrincipal:trayectorias){
		//Consultamos los pasos de la trayectoria principal
		List<Paso> pasos = new ArrayList<Paso>();
		
		//Si es una trayectoria alternativa
		if(trayectoriaPrincipal.isAlternativa()){
			////Obtenemos la trayectoria principal
			Trayectoria tp = GuionPruebasBs.trayectoriaPrincipal(casoUso);
			//Obtenemos los pasos de la trayectoria principal
			List<Paso> pasosTP = TrayectoriaBs.obtenerPasos_(tp.getId());
			for(Paso ptp : pasosTP){
				int salir = 0;
				//Obtenemos los tokens del paso
				List<String> tokens = GuionPruebasBs.obtenerTokens(ptp);
				for(String tk : tokens){
					//Si el token es una Trayectoria Alternativa 
					if(tk.contains(TokenBs.tokenTray)){
						//Obtemos el objeto de esa trayectoria y la comparamos con trayectoriaPrincipal
						Trayectoria trayectoria = (Trayectoria) TokenBs.obtenerTokenObjeto(" "+tk);
						if(trayectoria.getId()==trayectoriaPrincipal.getId()){
							salir = 1;
							break;
						}
					/*******AGREGAR AL NUEVO*******/
					}else if(tk.contains(TokenBs.tokenRN)){
						//Obtenemos el objeto de la RN
						ReglaNegocio rn = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" "+tk);
						System.out.println("----------------------------------------");
						System.out.println(rn.getId());
						System.out.println("----------------------------------------");
						reglasNegocio.add(rn);
					}
				}
				if(salir==1)
					break;
				else
					pasos.add(ptp);
			}
		}
		
		//Agregamos los pasos de la trayectoria (principal o alternativa)
		pasos.addAll(TrayectoriaBs.obtenerPasos_(trayectoriaPrincipal.getId()));
				
				// Obtenemos el total de pasos de la trayectora principal
				int tpasos = pasos.size();
				// Consultamos las entradas del caso de uso
				Set<Entrada> entradas = casoUso.getEntradas();
				
				//Agregamos la instrucción la url del CU (Ingrese a la siguiente url ...)
				//Pantalla pantalla = CasoUsoPantallaDAO.
				//instrucciones.add("Ingrese a la siguiente url: ");
				
				List<String> rnRevisadas = new ArrayList<String>();
	
				for (int i = 0; i < tpasos; i++) {
					// Consultamos el paso actual
					Paso paso = pasos.get(i);
					
					System.out.println(paso.getNumero()+": "+paso.getRedaccion());
	
					// Obtenemos los tokens del paso
					List<String> tokens = GuionPruebasBs.obtenerTokens(paso);
	
					// Comparación de los tokens
					for (String token : tokens) {
						
						//Si es una instrucción que ya se registró porque era desconocida
						
						/*if(paso.getOtroVerbo()!=null){
							int idSinonimo = VerboSinonimoBs.sinonimos(paso.getOtroVerbo()).getIdSinonimo();
							InstruccionDesconocidaDAO iddao = new InstruccionDesconocidaDAO();
							instrucciones.add(iddao.consultarInstruccionDesconocida(paso.getId(), idSinonimo).getInstruccion());
						}
						// Si el actor es el USUARIO
						else*/ 
						//Si es la trayectoria principal y el token es una RN
						if(token.contains(TokenBs.tokenRN)) {
							reglaNegocio = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" "+token);
							break;
						}
						if (paso.isRealizaActor()) {
							String comparacion = GuionPruebasBs.compararTokenUsuario(contextPath, paso, token, entradas, reglaNegocio);
							String instruccionBD = GuionPruebasBs.compararTokenUsuarioBD(contextPath, paso, " "+token,
									entradas, reglaNegocio);
							if (!comparacion.equals("")) {
								instrucciones.add(comparacion);
								Instruccion instruccion = new Instruccion(instruccionBD,
										guionPrueba.getIdGuionPrueba(), paso.getId());
								InstruccionDAO idao = new InstruccionDAO();
								idao.agregarInstruccion(instruccion);
							}
						}
						// Si el actor es el SISTEMA
						else {
							List<String> comparacion = GuionPruebasBs.compararTokenSistema(contextPath, paso, token, casoUso, guionPrueba.getIdGuionPrueba());
							List<String> instruccionesBD = GuionPruebasBs.compararTokenSistemaBD(contextPath,
									trayectorias, paso, " "+token, casoUso, guionPrueba.getIdGuionPrueba());

							if (!comparacion.isEmpty()) {
								for (String instr : comparacion) {
									instrucciones.add(instr);
								}
								for (String instr : instruccionesBD) {
									if (!token.contains(TokenBs.tokenTray)) {
										Instruccion instruccion = new Instruccion(instr,
												guionPrueba.getIdGuionPrueba(), paso.getId());
										InstruccionDAO idao = new InstruccionDAO();
										idao.agregarInstruccion(instruccion);
									}
								}
							}
							/*//Si el token es una RN y no se ha revisado, se agrega a la lista de RN revisadas
							if(token.contains(TokenBs.tokenRN) && !rnRevisadas.contains(token)){
								rnRevisadas.add(token);
							}
							else if(rnRevisadas.contains(token)){
								break;
							}
							//Si el resultado de la comparación del token es un paso 
							if(comparacion.contains(TokenBs.tokenP)){
								Paso pasoC = (Paso) TokenBs.obtenerTokenObjeto(" "+comparacion);
								i = pasoC.getNumero()-1;
							}
							//Si el resultado de la comparación es una intrucción 
							else */
						}
					}
				}
		}
		return instrucciones;
}

}