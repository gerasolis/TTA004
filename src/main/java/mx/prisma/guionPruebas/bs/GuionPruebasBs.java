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
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;

public class GuionPruebasBs {
	
	//Función para obtener la trayectoria principal
	public static Trayectoria trayectoriaPrincipal(CasoUso casoUso){
		//Creamos la trayectoria 
		Trayectoria tp = null;
		
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			//Si es la trayectoria principal
			if(!trayectoria.isAlternativa()){
				tp = trayectoria;
			}
		}
		
		//Retornamos la trayectoria principal
		return tp;
	}
	
	public static List<String> obtenerTokens(Paso paso) {
		//Creamos una lista para asignar los tokens encontrados
		List<String> tokens = new ArrayList<String>();
		//Arreglo de cadenas para las palabras de la redacción del paso
		String[] palabrasRedaccion;
		
		//Separamos las palabras de la redacción del paso
		palabrasRedaccion = paso.getRedaccion().split(" ");
		
		for(String token : palabrasRedaccion){
			//Si la palabra es un token
			if(TokenBs.esToken_(token)){
				//Se agrega al arreglo de tokens
				tokens.add(token);
			}
		}
		
		//Regresamos los tokens
		return tokens;
	}

	public static String compararTokenUsuario(Paso paso, String token, Set<Entrada> entradas) {
		String instruccion = "";
		//Si es una acción (ACC·#)
		if(token.contains(TokenBs.tokenACC)){
			String accion = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Oprime") || paso.getVerbo().getNombre().equals("Confirma")
				|| paso.getVerbo().getNombre().equals("Ejecuta") || paso.getVerbo().getNombre().equals("Reproduce")
				|| paso.getVerbo().getNombre().equals("Selecciona") || paso.getVerbo().getNombre().equals("Solicita")
				|| paso.getVerbo().getNombre().equals("Accede") || paso.getVerbo().getNombre().equals("Ingresa")){
				instruccion = "Dé click en el botón "+accion;
			}else if(paso.getVerbo().getNombre().equals("Descarga")){
				instruccion = "Dé click en el botón "+accion+" para descargar el archivo";
			}
		}
		//Si es un atributo
		if(token.contains(TokenBs.tokenATR)){
			String atributo = TokenBs.decodificarCadenaSinToken(token);
			//Comparamos el verbo 
			if(paso.getVerbo().getNombre().equals("Mueve")){
				instruccion = "Mueva el campo "+atributo;
			}else if(paso.getVerbo().getNombre().equals("Selecciona")){
				instruccion = "Seleccione el campo "+atributo;
			}else if(paso.getVerbo().getNombre().equals("Ingresa") || paso.getVerbo().getNombre().equals("Registra")){
				instruccion = "Ingrese en el campo "+atributo+": ";
				//Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for(Entrada e : entradas){
					if(e.getAtributo().getId() == a.getId()){
						instruccion += obtenerValorEntrada(e);
					}
				}
			}else if(paso.getVerbo().getNombre().equals("Adjunta")){
				instruccion = "Adjunta el archivo /*VALOR*/ en el campo "+atributo;
			}else if(paso.getVerbo().getNombre().equals("Busca")){
				instruccion = "Busque el atributo "+atributo;
			}else if(paso.getVerbo().getNombre().equals("Modifica")){
				instruccion = "Modifique el valor del campo "+atributo+" a: ";
				//Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for(Entrada e : entradas){
					if(e.getAtributo().getId() == a.getId()){
						instruccion += obtenerValorEntrada(e);
					}
				}
			}else if(paso.getVerbo().getNombre().equals("Elimina")){
				instruccion = "Elimine el valor del campo "+atributo;
			}else if(paso.getVerbo().getNombre().equals("Restablece")){
				instruccion = "Restablezca el valor del campo "+atributo+" a: ";
				//Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for(Entrada e : entradas){
					if(e.getAtributo().getId() == a.getId()){
						instruccion += obtenerValorEntrada(e);
					}
				}
			}else if(paso.getVerbo().getNombre().equals("Sustituye")){
				instruccion = "Sustituya el valor del campo "+atributo+" a: ";
				//Obtenemos el valor de las entradas
				Atributo a = (Atributo) TokenBs.obtenerTokenObjeto(token);
				for(Entrada e : entradas){
					if(e.getAtributo().getId() == a.getId()){
						instruccion += obtenerValorEntrada(e);
					}
				}
			}else if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Es correcta la información en el campo "+atributo+"?";
			}
		}
		//Si es una entidad (ENT·#)
		if(token.contains(TokenBs.tokenENT)){
			String entidad = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Mueve")){
				instruccion = "Mueva la entidad "+entidad;
			}else if(paso.getVerbo().getNombre().equals("Selecciona")){
				instruccion = "Seleccione la entidad "+entidad;
			}else if(paso.getVerbo().getNombre().equals("Ingresa") || paso.getVerbo().getNombre().equals("Registra")){
				instruccion = "Ingrese en la entidad "+entidad+" la siguiente información: ";//Falta agregar entrada:valor
				//Obtenemos el valor de las entradas
				for(Entrada e : entradas){
					instruccion += e.getAtributo().getNombre()+":"+obtenerValorEntrada(e)+" ";
				}
			}else if(paso.getVerbo().getNombre().equals("Busca")){
				instruccion = "Busque la entidad "+entidad;
			}else if(paso.getVerbo().getNombre().equals("Modifica")){
				instruccion = "Modifique los valores de la entidad "+entidad+" con la siguiente información: ";//Falta agregar entrada:valor
			}else if(paso.getVerbo().getNombre().equals("Elimina")){
				instruccion = "Elimine la información de la entidad "+entidad;
			}else if(paso.getVerbo().getNombre().equals("Restablece")){
				instruccion = "Restablezca los valores de la entidad "+entidad+" con la siguiente información: ";//Falta agregar entrada:valor
			}else if(paso.getVerbo().getNombre().equals("Sustituye")){
				instruccion = "Sustituya los valores de la entidad "+entidad+" con la siguiente información: ";//Falta agregar entrada:valor
			}else if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Es correcta la información en la entidad "+entidad+"?";
			}
		}
		//Si es una pantalla (IU·#)
		if(token.contains(TokenBs.tokenIU)){
			String pantalla = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Ejecuta")){
				instruccion = "Ejecute la pantalla "+pantalla;
			}else if(paso.getVerbo().getNombre().equals("Selecciona")){
				instruccion = "Seleccione la pantalla "+pantalla;
			}else if(paso.getVerbo().getNombre().equals("Solicita") || paso.getVerbo().getNombre().equals("Ingresa")
					|| paso.getVerbo().getNombre().equals("Gestiona")){
				instruccion = "Ingrese a la pantalla "+pantalla;
			}else if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Es correcta la pantalla "+pantalla+"?";
			}
		}
		return instruccion;
	}
	
	public static String compararTokenSistema(Paso paso, String token) {
		String instruccion = "";
		//Si es una acción (ACC·#)
		if(token.contains(TokenBs.tokenACC)){
			String accion = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Ejecuta")){
				instruccion = "¿Se ejecutó correctamente "+accion+"?";
			}else if(paso.getVerbo().getNombre().equals("Reproduce")){
				instruccion = "¿Se reprodujo correctamente "+accion+"?";
			}else if(paso.getVerbo().getNombre().equals("Habilita")){
				instruccion = "¿Se habilitó correctamente el botón "+accion+"?";
			}else if(paso.getVerbo().getNombre().equals("Oculta")){
				instruccion = "¿Se ocultó correctamente "+accion+"?";
			}
		}
		//Si es un atributo (ATR·#)
		if(token.contains(TokenBs.tokenATR)){
			String atributo = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Mueve")){
				instruccion = "¿Se movió correctamente el campo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Habilita")){
				instruccion = "¿Se habilitó correctamente el campo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Oculta")){
				instruccion = "¿Se ocultó correctamente el campo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Calcula")){
				instruccion = "¿Se calculó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Crea")){
				instruccion = "¿Se creó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Descarga")){
				instruccion = "¿Se descargó correctamente?";
			}else if(paso.getVerbo().getNombre().equals("Busca")){
				instruccion = "¿Se buscó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Registra")){
				instruccion = "¿Se registró correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Modifica")){
				instruccion = "¿Se modificó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Elimina")){
				instruccion = "¿Se eliminó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Restablece")){
				instruccion = "¿Se restableció correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Sustituye")){
				instruccion = "¿Se sustituyó correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Envía")){
				instruccion = "¿Se envió correctamente el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Muestra")){
				instruccion = "¿Se muestra correctamente la información en el atributo "+atributo+"?";
			}else if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Se verificó correctamente el atributo "+atributo+"?";
			}
		}
		//Si es un entidad (ENT·#)
		if(token.contains(TokenBs.tokenENT)){
			String entidad = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Mueve")){
				instruccion = "¿Se movió correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Habilita")){
				instruccion = "¿Se habilitó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Oculta")){
				instruccion = "¿Se ocultó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Crea")){
				instruccion = "¿Se creó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Busca")){
				instruccion = "¿Se buscó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Registra")){
				instruccion = "¿Se registró correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Modifica")){
				instruccion = "¿Se modificó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Elimina")){
				instruccion = "¿Se eliminó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Restablece")){
				instruccion = "¿Se restableció correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Sustituye")){
				instruccion = "¿Se sustituyó correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Envía")){
				instruccion = "¿Se envió correctamente la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Muestra")){
				instruccion = "¿Se muestra correctamente la información en la entidad "+entidad+"?";
			}else if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Se verificó correctamente la entidad "+entidad+"?";
			}
		}
		//Si es una pantalla (IU·#)
		if(token.contains(TokenBs.tokenIU)){
			String pantalla = TokenBs.decodificarCadenaSinToken(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Habilita")){
				instruccion = "¿Se habilitó correctamente la pantalla "+pantalla+"?";
			}else if(paso.getVerbo().getNombre().equals("Oculta")){
				instruccion = "¿Se ocultó correctamente la pantalla "+pantalla+"?";
			}else if(paso.getVerbo().getNombre().equals("Accede") || paso.getVerbo().getNombre().equals("Muestra")){
				instruccion = "¿Se muestra correctamente la pantalla "+pantalla+"?";
			}else if(paso.getVerbo().getNombre().equals("Envía")){
				instruccion = "¿Se envió correctamente la pantalla "+pantalla+"?";
			}
		}
		//Si es una regla de negocio (RN·#)
		if(token.contains(TokenBs.tokenRN)){
			//Obtenemos la regla de negocio mediante el token
			String rn = TokenBs.decodificarCadenaSinToken(" "+token);
			//Obtenemos la redacción de la RN
			ReglaNegocio redaccionRN = (ReglaNegocio) TokenBs.obtenerTokenObjeto(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Verifica")){
				instruccion = "¿Se cumple la regla de negocio "+rn+": "+redaccionRN.getRedaccion()+"?";
			}
		}
		//Si es un mensaje (MSG·#)
		if(token.contains(TokenBs.tokenMSG)){
			//Obtenemos el mensaje mediante el token
			String mensaje = TokenBs.decodificarCadenaSinToken(" "+token);
			//Obtenemos la redacción del mensaje
			Mensaje redaccionMensaje = (Mensaje) TokenBs.obtenerTokenObjeto(" "+token);
			//Comparamos el verbo
			if(paso.getVerbo().getNombre().equals("Envía")){
				instruccion = "¿Se envió correctamente el mensaje "+mensaje+": "+redaccionMensaje.getRedaccion()+"?";
			}else if(paso.getVerbo().getNombre().equals("Muestra")){
				instruccion = "¿Se muestra correctamente el mensaje "+mensaje+": "+redaccionMensaje.getRedaccion()+"?";
			}
		}
		return instruccion;
	}
	
	//Función para obtener el valor de la entrada desde el archivo
	private static String obtenerValorEntrada(Entrada entrada){
		String valor = "";
			if(new ValorDesconocidoDAO().consultarValorGuion(entrada)){
				System.out.println("Consulta valor");
				//Aquí aplicamos el algoritmo para el valor aleatorio.
					
				String ruta=new ValorDesconocidoDAO().obtenerRutaValorGuion(entrada);
				File fichero_entrada = new File (ruta);
				if (!fichero_entrada.exists()) {
					System.out.println ("No existe el fichero de entrada especificado"); 
				}else {
		            Scanner scan1;
					try {
						scan1 = new Scanner (fichero_entrada);
						ArrayList<String> datosDeEntrada = new ArrayList<String>();
				        int contador = 0;
				        while (scan1.hasNext()){
				               String lineaExtraida = scan1.nextLine();
				               datosDeEntrada.add(lineaExtraida);
				               contador++;
				         }
					     int ran= (int) (Math.random()*contador+0);
					     System.out.println ("Dato en el txt: "+datosDeEntrada.get(ran)); //Este es el dato que obtenemos en el txt.
					     valor = org.apache.commons.lang.StringEscapeUtils.escapeCsv(datosDeEntrada.get(ran));
					} catch (FileNotFoundException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					}
		        }
			}
		
		return valor;
	}
}