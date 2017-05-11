package mx.prisma.guionPruebas.bs;

//Librerías
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.bs.TipoReglaNegocioEnum.TipoReglaNegocioENUM;
import mx.prisma.editor.bs.EntradaBs;
import mx.prisma.editor.bs.MensajeBs;
import mx.prisma.editor.bs.MensajeParametroBs;
import mx.prisma.editor.bs.PantallaBs;
import mx.prisma.editor.bs.PasoBs;
import mx.prisma.editor.bs.ReglaNegocioBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.dao.TipoReglaNegocioDAO;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.dao.VerboDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TipoReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.Verbo;
import mx.prisma.generadorPruebas.bs.QueryBs;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroBs;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.guionPruebas.dao.GuionPruebaDAO;
import mx.prisma.guionPruebas.dao.InstruccionDAO;
import mx.prisma.guionPruebas.dao.VerboSinonimoDAO;
import mx.prisma.guionPruebas.model.GuionPrueba;
import mx.prisma.guionPruebas.model.Instruccion;
import mx.prisma.guionPruebas.model.Sinonimo;
import mx.prisma.guionPruebas.model.VerboSinonimo;

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
        String accion = TokenBs.agregarReferencias(actionContext, token,"_blank");
// Comparamos el verbo
        if (paso.getVerbo().getNombre().equals("Oprime") || paso.getVerbo().getNombre().equals("Confirma")
            || paso.getVerbo().getNombre().equals("Ejecuta") || paso.getVerbo().getNombre().equals("Reproduce")
            || paso.getVerbo().getNombre().equals("Selecciona")
            || paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Accede")
            || paso.getVerbo().getNombre().equals("Ingresa")) {
            //Obtenemos la pantalla a la cual pertenece la acción 
            String pantalla = "";
            Accion acc = (Accion) TokenBs.obtenerTokenObjeto(" "+token);
            Pantalla pa = acc.getPantalla();
            pantalla = " dentro de la pantalla  <a target='_blank' class='referencia' href='" + actionContext + "/pantallas/" + pa.getId() +"'>" + pa.getClave()
                            + " " + pa.getNumero() + " "
                            + pa.getNombre() + "</a>";
            instruccion = "Dé click en el botón " + accion + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Descarga")) {
        instruccion = "Dé click en el botón " + accion + " para descargar el archivo";
        
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
    
    String atributo = TokenBs.agregarReferencias(actionContext, token,"_blank");
// Comparamos el verbo
    if (paso.getVerbo().getNombre().equals("Mueve")) {
        instruccion = "Mueva el campo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione el campo " + atributo;
        
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
        
    } else if (paso.getVerbo().getNombre().equals("Adjunta")) {
        instruccion = "Adjunte un archivo en el campo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Busca")) {
        instruccion = "Busque el atributo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Modifica")) {
        instruccion = "Modifique el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Elimina")) {
        instruccion = "Elimine el valor del campo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Restablece")) {
        instruccion = "Restablezca el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Sustituye")) {
        instruccion = "Sustituya el valor del campo " + atributo + " por: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Verifica")) {
        instruccion = "¿Es correcta la información en el campo " + atributo + "?";
        
    }
}
// Si es una entidad (ENT·#)
if (token.contains(TokenBs.tokenENT)) {
    
    String entidad = TokenBs.agregarReferencias(actionContext, token,"_blank");
    // Comparamos el verbo
    if (paso.getVerbo().getNombre().equals("Mueve")) {
        instruccion = "Mueva la entidad " + entidad;
    
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione la entidad " + entidad;
    
    } else if (paso.getVerbo().getNombre().equals("Ingresa")
        || paso.getVerbo().getNombre().equals("Registra")) {
    
        instruccion = "Ingrese en la entidad " + entidad + " la siguiente información: ";
	
	} else if (paso.getVerbo().getNombre().equals("Busca")) {
	    instruccion = "Busque la entidad " + entidad;
	
	} else if (paso.getVerbo().getNombre().equals("Modifica")) {
		instruccion = "Modifique los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
	
	// entrada:valor
	} else if (paso.getVerbo().getNombre().equals("Elimina")) {
	    instruccion = "Elimine la información de la entidad " + entidad;
	} else if (paso.getVerbo().getNombre().equals("Restablece")) {
		instruccion = "Restablezca los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
	
	// entrada:valor
	} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
		instruccion = "Sustituya los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
	// agregar
	
	} else if (paso.getVerbo().getNombre().equals("Verifica")) {
	    instruccion = "¿Es correcta la información en la entidad " + entidad + "?";
	
	}
    for (Entrada e : entradas) {
        instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
    }
}
// Si es una pantalla (IU·#)
if (token.contains(TokenBs.tokenIU)) {
    String pantalla = TokenBs.agregarReferencias(actionContext, token,"_blank");
// Comparamos el verbo
    if (paso.getVerbo().getNombre().equals("Ejecuta")) {
        instruccion = "Ejecute la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Ingresa")
        || paso.getVerbo().getNombre().equals("Gestiona")) {
        
        instruccion = "Ingrese a la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Verifica")) {
        instruccion = "¿Es correcta la pantalla " + pantalla + "?";
        
    }
}

return instruccion;
}

/*********AGREGAR AL NUEVO***********/
public static String compararTokenUsuarioBD(String actionContext, Paso paso, String token, Set<Entrada> entradas, ReglaNegocio reglaNegocio) {
    String instruccion = "";

// Si es una acción (ACC·#)
    if (token.contains(TokenBs.tokenACC)) {
        
        String accion = TokenBs.decodificarCadenaSinToken(token);
// Comparamos el verbo
        if (paso.getVerbo().getNombre().equals("Oprime") || paso.getVerbo().getNombre().equals("Confirma")
            || paso.getVerbo().getNombre().equals("Ejecuta") || paso.getVerbo().getNombre().equals("Reproduce")
            || paso.getVerbo().getNombre().equals("Selecciona")
            || paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Accede")
            || paso.getVerbo().getNombre().equals("Ingresa")) {
            String pantalla = "";
            Accion acc = (Accion) TokenBs.obtenerTokenObjeto(" "+token);
            Pantalla pa = acc.getPantalla();
            pantalla = " dentro de la pantalla "+pa.getClave()+pa.getNumero() + " "
                            + pa.getNombre();
            instruccion = "Dé click en el botón " + accion + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Descarga")) {
        instruccion = "Dé click en el botón " + accion + " para descargar el archivo";
        
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
        
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione el campo " + atributo;
        
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
        
    } else if (paso.getVerbo().getNombre().equals("Adjunta")) {
        instruccion = "Adjunte un archivo en el campo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Busca")) {
        instruccion = "Busque el atributo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Modifica")) {
        instruccion = "Modifique el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Elimina")) {
        instruccion = "Elimine el valor del campo " + atributo;
        
    } else if (paso.getVerbo().getNombre().equals("Restablece")) {
        instruccion = "Restablezca el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Sustituye")) {
        instruccion = "Sustituya el valor del campo " + atributo + " a: ";
// Obtenemos el valor de las entradas
        Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
        for (Entrada e : entradas) {
            if (e.getAtributo().getId() == a.getId()) {
                instruccion += obtenerValorEntrada(e, reglaNegocio);
            }
        }
        
    } else if (paso.getVerbo().getNombre().equals("Verifica")) {
        instruccion = "¿Es correcta la información en el campo " + atributo + "?";
        
    }
}
// Si es una entidad (ENT·#)
if (token.contains(TokenBs.tokenENT)) {
    String entidad = TokenBs.decodificarCadenaSinToken(token);
// Comparamos el verbo
    if (paso.getVerbo().getNombre().equals("Mueve")) {
        instruccion = "Mueva la entidad " + entidad;
        
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione la entidad " + entidad;
        
    } else if (paso.getVerbo().getNombre().equals("Ingresa")
        || paso.getVerbo().getNombre().equals("Registra")) {
        instruccion = "Ingrese en la entidad " + entidad + " la siguiente información: ";
            
		
		} else if (paso.getVerbo().getNombre().equals("Busca")) {
		    instruccion = "Busque la entidad " + entidad;
		
		} else if (paso.getVerbo().getNombre().equals("Modifica")) {
		instruccion = "Modifique los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
		
		// entrada:valor
		} else if (paso.getVerbo().getNombre().equals("Elimina")) {
		    instruccion = "Elimine la información de la entidad " + entidad;
		} else if (paso.getVerbo().getNombre().equals("Restablece")) {
		instruccion = "Restablezca los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
		
		// entrada:valor
		} else if (paso.getVerbo().getNombre().equals("Sustituye")) {
		instruccion = "Sustituya los valores de la entidad " + entidad + " con la siguiente información: ";// Falta
		// agregar
		
		} else if (paso.getVerbo().getNombre().equals("Verifica")) {
		    instruccion = "¿Es correcta la información en la entidad " + entidad + "?";
		
		}
	    for (Entrada e : entradas) {
	        instruccion += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
	    }
	}
// Si es una pantalla (IU·#)
if (token.contains(TokenBs.tokenIU)) {
    String pantalla = TokenBs.decodificarCadenaSinToken(token);
// Comparamos el verbo
    if (paso.getVerbo().getNombre().equals("Ejecuta")) {
        instruccion = "Ejecute la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Selecciona")) {
        instruccion = "Seleccione la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Ingresa")
        || paso.getVerbo().getNombre().equals("Gestiona")) {
        
        instruccion = "Ingrese a la pantalla " + pantalla;
        
    } else if (paso.getVerbo().getNombre().equals("Verifica")) {
        instruccion = "¿Es correcta la pantalla " + pantalla + "?";
        
    }
}

return instruccion;
}

public static List<String> compararTokenSistema(String actionContext, Paso paso, String token, CasoUso casoUso, int idGuionPrueba, Set<Entrada> entradas, ReglaNegocio reglaNegocio) {
    List<String> instruccion = new ArrayList<String>();

// Si es una acción (ACC·#)
    if (token.contains(TokenBs.tokenACC)) {
        
        String accion = TokenBs.decodificarCadenaSinToken(token);
// Comparamos el verbo
        if (paso.getVerbo().getNombre().equals("Oprime") || paso.getVerbo().getNombre().equals("Confirma")
            || paso.getVerbo().getNombre().equals("Ejecuta") || paso.getVerbo().getNombre().equals("Reproduce")
            || paso.getVerbo().getNombre().equals("Selecciona")
            || paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Accede")
            || paso.getVerbo().getNombre().equals("Ingresa")) {
            Accion acc = (Accion) TokenBs.obtenerTokenObjeto(" "+token);
            instruccion.add("Dé click en el botón " + accion + " dentro de la pantalla " + acc.getPantalla().getNumero() + acc.getPantalla().getNombre());
    } else if (paso.getVerbo().getNombre().equals("Descarga")) {
        instruccion.add("Dé click en el botón " + accion + " para descargar el archivo");
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
  //Si es una RN
    if (token.contains(TokenBs.tokenRN)) {
        String entr = "Ingrese la siguiente información: ";
    // Obtenemos el valor de las entradas
        for (Entrada e : entradas) {
            entr += e.getAtributo().getNombre() + ":" + obtenerValorEntrada(e, reglaNegocio) + " ";
        }
        instruccion.add(entr);
    }
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
                cadena = m.getRedaccion();
                if(cadena.charAt(0) == '$'){
                    cadena = cadena.substring(1,cadena.length());
                }
                //Aquí sustituyo los token y comparo con los mensajes de la prueba.
                //primero cuento la cantidad de tokens sólo si es de tipo parametrizado.
                if(m.isParametrizado()){
                	//Obtenemos la lista de paramentros del mensaje
                    List<String> parametros = obtenerTokens(m);
                    for(String param : parametros){
                    	if(param.charAt(0) == '$'){
                            param = param.substring(1,param.length());
                        }
                    	for(MensajeParametro mp : MensajeParametroBs.consultarMensajeParametro_(m.getId())){
                    		for(ValorMensajeParametro v : ValorMensajeParametroBs.consultarValores_(mp.getId())){
                    			if(v.getReferenciaParametro().getPaso().getRedaccion().equals(paso.getRedaccion())){
                    				String paramOriginal = TokenBs.decodificarCadenasToken(" "+param);
                    				if(v.getMensajeParametro().getParametro().getNombre().equals(paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length()))){
                    					paramOriginal = paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length());
                    					cadena = TokenBs.remplazoToken(cadena,param, v.getValor());
                    					break;
                    				}
                    			}
                    		}
                    	}
                    }
                }else{
                    cadena = m.getRedaccion();
                    if(cadena.charAt(0) == '$'){
                        cadena = cadena.substring(1,cadena.length());
                    }
                }//aquí acaba el else
            }
        }
        
        String pantalla ="";
        
        //Si es un mensaje que se muestra en la pantalla 
        if(paso.getRedaccion().contains(TokenBs.tokenMSG) && paso.getRedaccion().contains(TokenBs.tokenIU)){
            //Consultamos la lista de tokens del paso que contiene el mensaje
            List<String> tokens = obtenerTokens(paso);
            //Si el token es una pantalla
            for(String tokenIU : tokens){
                if(tokenIU.contains(TokenBs.tokenIU)){
                    //Consultamos el nombre de la pantalla
                    pantalla = TokenBs.agregarReferencias(actionContext, tokenIU,"_blank");
                    break;
                }
            }
        }

        // Comparamos el verbo
        if (paso.getVerbo().getNombre().equals("Envía")) {
            instruccion.add("¿Se envió correctamente el mensaje " + mensaje + ": " + cadena
                + "?");
        } else if (paso.getVerbo().getNombre().equals("Muestra")) {
            instruccion.add("¿Se muestra correctamente el mensaje " + mensaje + ": " + cadena
                + " en la pantalla "+pantalla+"?");
        }
    }
    /*if(token.contains(TokenBs.tokenTray)) {
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
            
        } else if (paso.getVerbo().getNombre().equals("Reproduce")) {
            instruccion.add("¿Se reprodujo correctamente " + accion + "?");
            
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
        String mensaje = TokenBs.decodificarCadenaSinToken(" "+token);
        // Obtenemos la redacción del mensaje
        for(ReferenciaParametro rp : PasoBs.obtenerReferencias(paso.getId())){
            if(rp.getTipoParametro().getId() == 6){
                Mensaje m = MensajeBs.consultarMensaje(rp.getElementoDestino().getId());
                cadena = m.getRedaccion();
                if(cadena.charAt(0) == '$'){
                    cadena = cadena.substring(1,cadena.length());
                }
                //System.out.println(m.getRedaccion());
                //Aquí sustituyo los token y comparo con los mensajes de la prueba.
                //primero cuento la cantidad de tokens sólo si es de tipo parametrizado.
                if(m.isParametrizado()){
                	//Obtenemos la lista de paramentros del mensaje
                    List<String> parametros = obtenerTokens(m);
                    for(String param : parametros){
                    	if(param.charAt(0) == '$'){
                            param = param.substring(1,param.length());
                        }
                    	for(MensajeParametro mp : MensajeParametroBs.consultarMensajeParametro_(m.getId())){
                    		for(ValorMensajeParametro v : ValorMensajeParametroBs.consultarValores_(mp.getId())){
                    			if(v.getReferenciaParametro().getPaso().getRedaccion().equals(paso.getRedaccion())){
                    				String paramOriginal = TokenBs.decodificarCadenasToken(" "+param);
                    				if(v.getMensajeParametro().getParametro().getNombre().equals(paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length()))){
                    					paramOriginal = paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length());
                    					cadena = TokenBs.remplazoToken(cadena,param, v.getValor());
                    					break;
                    				}
                    			}
                    		}
                    	}
                    	
                        /*if(param.charAt(0) == '$'){
                            param = param.substring(1,param.length());
                        }
                        String paramOriginal = TokenBs.decodificarCadenasToken(" "+param);
                        cadena = cadena.replaceAll(param, paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length()));*/
                    }
                }else{
                    cadena = m.getRedaccion();
                    if(cadena.charAt(0) == '$'){
                        cadena = cadena.substring(1,cadena.length());
                    }
                }//aquí acaba el else
            }
        }
        
        String pantalla ="";
        
        //Si es un mensaje que se muestra en la pantalla 
        if(paso.getRedaccion().contains(TokenBs.tokenMSG) && paso.getRedaccion().contains(TokenBs.tokenIU)){
            //Consultamos la lista de tokens del paso que contiene el mensaje
            List<String> tokens = obtenerTokens(paso);
            //Si el token es una pantalla
            for(String tokenIU : tokens){
                if(tokenIU.contains(TokenBs.tokenIU)){
                    //Consultamos el nombre de la pantalla
                    pantalla = TokenBs.decodificarCadenaSinToken(" "+tokenIU);
                    break;
                }
            }
        }

        // Comparamos el verbo
        if (paso.getVerbo().getNombre().equals("Envía")) {
            instruccion.add("¿Se envió correctamente el mensaje " + mensaje + ": " + cadena
                + "?");
        } else if (paso.getVerbo().getNombre().equals("Muestra")) {
            instruccion.add("¿Se muestra correctamente el mensaje " + mensaje + ": " + cadena
                + " en la pantalla "+pantalla+"?");
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

static int unicidad = 0;

// Función para obtener el valor de la entrada desde el archivo
private static String obtenerValorEntrada(Entrada entrada, ReglaNegocio reglaNegocio) {
    String valor = "";
    
    ValorEntradaDAO vedao = new ValorEntradaDAO();
    List<ValorEntrada> valores = vedao.consultarValores(entrada);
    for(ValorEntrada valorEntrada : valores){
        if(valorEntrada.getEntrada().getId().equals(entrada.getId())){
            //Valor correcto
            if(valorEntrada.getValido()==true && (valorEntrada.getCorrecto_guion()==true || valorEntrada.getAleatoriocorrecto_guion()==true)){
                valor = valorEntrada.getValor();
                break;
            }else if(valorEntrada.getValido()==false && reglaNegocio.getId()==valorEntrada.getReglaNegocio().getId()){
                valor = valorEntrada.getValor();
                break;
            }
        }
    }
    
    if(reglaNegocio.getId() != 0){
    	reglaNegocio = ReglaNegocioBs.consultaReglaNegocio(reglaNegocio.getId());
    	TipoReglaNegocioENUM tipoRN = TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio());
    	if (tipoRN == TipoReglaNegocioENUM.UNICIDAD && unicidad == 0) {
    		ReferenciaParametroDAO rpdao = new ReferenciaParametroDAO();
    		List<ReferenciaParametro> listrp = rpdao.consultarReferenciasParametro(reglaNegocio);
    		if(listrp!=null)
	    		for(ReferenciaParametro rp : listrp){
	    			Set<Query> query = QueryBs.consultarQueries(rp);
	    			if(query!=null)
		    			for(Query q : query){
		    				valor = q.getQuery().substring(q.getQuery().indexOf("\"")+1, q.getQuery().length()-2);
		    				unicidad = 1;
		    			}
	    		}
    	}
    }
    
    if(valor.equals("")){
    	valor = "no inserte información";
    }

    return valor;
}

public static List<String> obtenerInstrucciones(CasoUso casoUso, List<Trayectoria> trayectorias, String contextPath) {
    /* Consultamos si existe el guión de pruebas para ese cu */
    GuionPruebaDAO gpdao = new GuionPruebaDAO();
    GuionPrueba guionPrueba = new GuionPrueba();
    
    // Si no existe un guion de prueba para ese CU, lo creamos
    if (gpdao.select(casoUso.getId()) == null) {
        gpdao = new GuionPruebaDAO();
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
    
    for(int a=0; a<trayectorias.size(); a++){
    	Trayectoria trayectoriaPrincipal = trayectorias.get(a);
        //Consultamos los pasos de la trayectoria principal
        List<Paso> pasos = new ArrayList<Paso>();
        
        //Si es una trayectoria alternativa
        if(trayectoriaPrincipal.isAlternativa()){
            //Obtenemos la trayectoria principal
            Trayectoria tp = GuionPruebasBs.trayectoriaPrincipal(casoUso);
            //Obtenemos los pasos de la trayectoria principal
            List<Paso> pasosTP = TrayectoriaBs.obtenerPasos_(tp.getId());
            
            /*
             * Para agregar pasos de la trayectoria principal antes de verificar la trayectoria alternativa
             * Verificar si la trayectoria anterior es una trayectoria alternativa y fin del caso de uso:
             * 		Sí: se agrega desde el primer paso de la trayectoria principal
             * 		No: se agrega desde el paso que indica la trayectoria anterior
             */
            int k = 0; //Inicio para el paso de la trayectoria principal
            if(a>0 && trayectorias.get(a-1).isAlternativa() && trayectorias.get(a-1).isFinCasoUso()){
            	k=0;
            }else if(a>0){
            	Trayectoria trayectoriaAnterior = new Trayectoria();
            	trayectoriaAnterior.setId(trayectorias.get(a-1).getId());
            	trayectoriaAnterior.setPasos(trayectorias.get(a-1).getPasos());
            	int ultimoPasoIndex = TrayectoriaBs.obtenerPasos_(trayectoriaAnterior.getId()).size()-1;
            	int idTrayectoriaAnteior = trayectoriaAnterior.getId();
            	//Obtenemos el último paso de la trayectoria alternativa anterior
            	Paso ultimoPaso = TrayectoriaBs.obtenerPasos_(idTrayectoriaAnteior).get(ultimoPasoIndex);
            	List<String> tokens = GuionPruebasBs.obtenerTokens(ultimoPaso);
            	for(String tk : tokens){
            		if(tk.contains(TokenBs.tokenP)){
            			ultimoPaso = (Paso) TokenBs.obtenerTokenObjeto(" "+tk);
            			k = ultimoPaso.getNumero()-1;
            		}
            	}
            }
            
            for(int i=k; i<pasosTP.size(); i++){
                //if(trayectoriaPrincipal.isFinCasoUso()){
                    //if((pasoAnterior.getNumero()<ptp.getNumero())){
                        int salir = 0;
                        //Obtenemos los tokens del paso
                        List<String> tokens = GuionPruebasBs.obtenerTokens(pasosTP.get(i));
                        for(String tk : tokens){
                        	//Si el token es una Regla de Negocio
                        	if(tk.contains(TokenBs.tokenRN)){
                        		reglaNegocio = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" "+tk);
                        	}
                            //Si el token es una Trayectoria Alternativa 
                        	if(tk.contains(TokenBs.tokenTray)){
                                //Obtemos el objeto de esa trayectoria y la comparamos con trayectoriaPrincipal
                                Trayectoria trayectoria = (Trayectoria) TokenBs.obtenerTokenObjeto(" "+tk);
                                if(trayectoria.getId()==trayectoriaPrincipal.getId()){
                                    salir = 1;
                                    break;
                                }
                            }
                        }
                        if(salir==1){
                            break;
                        }
                        else{
                            pasos.add(pasosTP.get(i));
                        }
                    //}
                //}
            }
        }
        
        //Agregamos los pasos de la trayectoria (principal o alternativa)
        pasos.addAll(TrayectoriaBs.obtenerPasos_(trayectoriaPrincipal.getId()));
                
                // Obtenemos el total de pasos de la trayectora principal
                int tpasos = pasos.size();
                // Consultamos las entradas del caso de uso
                Set<Entrada> entradas = casoUso.getEntradas();
                
                for (int i = 0; i < tpasos; i++) {
                	Paso paso = pasos.get(i);
                	if(paso.getOtroVerbo()!=null){
                    	System.out.println("Otro verbo: "+paso.getOtroVerbo());
                        Sinonimo sinonimo = VerboSinonimoBs.sinonimos(paso.getOtroVerbo());
                        System.out.println("sinonimo: "+sinonimo.getSinonimo());
                        if(sinonimo != null){
                        	List<VerboSinonimo> verbosSimilares = VerboSinonimoBs.verbos(sinonimo);
                        	System.out.println("Total de verbos similares: "+verbosSimilares.size());
                        	VerboDAO vdao = new VerboDAO();
                        	Verbo v = vdao.consultarVerbo(verbosSimilares.get(0).getVerbo_idVerbo());
                        	System.out.println(v.getNombre());
                        	paso.setVerbo(v);
                        }
                        
                    }
                    
                    System.out.println("VERBO DEL PASO "+paso.getVerbo().getNombre());
                    System.out.println("ID DEL PASO "+paso.getId());
                    if((paso.getVerbo().getNombre().equals("Elimina") || paso.getVerbo().getNombre().equals("Modifica") || (paso.getOtroVerbo()!= null && paso.getOtroVerbo().equals("Consulta")))){
                            	instrucciones.add("En caso de haber realizado un registro previamente, utilice dicho registro. De lo contrario, utilice el registro de su preferencia");
                            	Instruccion instruccion = new Instruccion("En caso de haber realizado un registro previamente, utilice dicho registro. De lo contrario, utilice el registro de su preferencia",
                                        guionPrueba.getIdGuionPrueba(), paso.getId());
                                InstruccionDAO idao = new InstruccionDAO();
                                idao.agregarInstruccion(instruccion);
                                break;
                    }
                }
                
                for (int i = 0; i < tpasos; i++) {
                    // Consultamos el paso actual
                    Paso paso = pasos.get(i);
                    
                    // Obtenemos los tokens del paso
                    List<String> tokens = GuionPruebasBs.obtenerTokens(paso);
                    
                    // Comparación de los tokens
                    for (String token : tokens) {
                        
                        
                        //Si es la trayectoria principal y el token es una RN
                        if(token.contains(TokenBs.tokenRN)) {
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
                            List<String> comparacion = GuionPruebasBs.compararTokenSistema(contextPath, paso, token, casoUso, guionPrueba.getIdGuionPrueba(), entradas, reglaNegocio);
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
                        }
                        //Una vez agregada la instrucción, vemos si el paso muestra un mensaje dentro de una pantalla, si es así, se rompe el ciclo 
                        if(paso.getRedaccion().contains(TokenBs.tokenMSG) && paso.getRedaccion().contains(TokenBs.tokenIU)){
                            break;
                        }
                        //Una vez agregada la instrucción, vemos si el paso tiene una acción y especifica la pantalla, si es así, se rompe el ciclo
                        if(paso.getRedaccion().contains(TokenBs.tokenACC) && paso.getRedaccion().contains(TokenBs.tokenIU)){
                            break;
                        }
                    }
                }
        }
    	
	    
        return instrucciones;
    }

}