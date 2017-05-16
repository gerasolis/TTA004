package mx.prisma.generadorPruebas.bs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.bs.TipoDatoEnum;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.editor.bs.ReglaNegocioBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.EscenarioDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.Escenario;
import mx.prisma.generadorPruebas.model.EscenarioValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.util.GeneradorCadenasUtil;
import mx.prisma.util.PRISMARandomUtil;

public class CuPruebasBs {
	
	/*public static void generarEscenarios(Set<Entrada> entradas, Set<CasoUsoReglaNegocio> casoUso_reglaNegocio) throws Exception {
		Set<ReglaNegocio> reglas = new HashSet<ReglaNegocio>(0);
		for(CasoUsoReglaNegocio curn : casoUso_reglaNegocio) {
			reglas.add(curn.getReglaNegocio());
		}
		
		generarValores(entradas, reglas);
		
		
		for(Entrada entradaPrivote : entradas) {
			generarEscenariosEntrada(entradaPrivote);
		}
	}*/

	
	private static void generarEscenariosEntrada(Entrada entrada) {
		
		Set<ValorEntrada> valoresValidos = obtenerValoreValidos(entrada.getCasoUso());
		Set<ValorEntrada> valoresInvalidosEntrada = obtenerValoresInvalidos(entrada);
		
		
		for(ValorEntrada valorInvalidoEntrada : valoresInvalidosEntrada) {
			Escenario escenario = new Escenario();
			Set<EscenarioValorEntrada> valoresEntrada = new HashSet<EscenarioValorEntrada>(0);
			//Set<ValorEntrada> valoresEntrada = new HashSet<ValorEntrada>(0);
			EscenarioValorEntrada eve = new EscenarioValorEntrada();
			eve.setEscenario(escenario);
			eve.setValorEntradaid(valorInvalidoEntrada);
			valoresEntrada.add(eve);
			//valoresEntrada.add(valorInvalidoEntrada);
			for(ValorEntrada valorValido : valoresValidos) {
				if(valorValido.getEntrada().getId() != entrada.getId()) {
					eve = new EscenarioValorEntrada();
					eve.setEscenario(escenario);
					eve.setValorEntradaid(valorValido);
					valoresEntrada.add(eve);
					//valoresEntrada.add(valorValido);
				}
			}
			
			escenario.setValoresEntrada(valoresEntrada);
			new EscenarioDAO().registrarEscenario(escenario);
		}
	}

	private static Set<ValorEntrada> obtenerValoresInvalidos(Entrada entrada) {
		Set<ValorEntrada> valoresInvalidos = new HashSet<ValorEntrada>(0);
		List<ValorEntrada> valores = new ValorEntradaDAO().consultarValoresInvalidos(entrada);
		if(valores != null) {
			for(ValorEntrada valor : valores) {
				valoresInvalidos.add(valor);
			}
		}
		return valoresInvalidos;
	}

	private static Set<ValorEntrada> obtenerValoreValidos(CasoUso casoUso) {
		Set<ValorEntrada> valoresValidos = new HashSet<ValorEntrada>(0);
		
		for(Entrada entrada : casoUso.getEntradas()) {
			ValorEntrada valorValido = new ValorEntradaDAO().consultarValorValido(entrada);
			valoresValidos.add(valorValido);
		}
		return valoresValidos;
	}

public static Set<ValorEntrada> generarValoresIncorectos(Set<Entrada> entradas,
		Set<CasoUsoReglaNegocio> reglasNegocio)throws Exception {
	
	String valorCadenaInvalido = null;
	ValorEntrada valorInvalidoBD = null;
	Set<ValorEntrada> listAleatoriasIncorrectas = new HashSet<ValorEntrada>(0);
	Set<ReglaNegocio> reglasN = new HashSet<ReglaNegocio>(0);
	
	for(CasoUsoReglaNegocio curn : reglasNegocio) {
		for(Entrada en : entradas){
			if(!ReglaNegocioBs.esGlobal(curn.getReglaNegocio().getTipoReglaNegocio())) {
				if(CuPruebasBs.entradaPerteneceReglaNegocio(en, curn.getReglaNegocio())) {
					valorCadenaInvalido = CuPruebasBs.generarValor(en, curn.getReglaNegocio(), reglasN, false);
				}
			} else {
				valorCadenaInvalido = generarValor(en, curn.getReglaNegocio(), reglasN, false);
			}
			if(valorCadenaInvalido != null) { 
				valorInvalidoBD = new ValorEntrada();
				valorInvalidoBD.setValor(valorCadenaInvalido);
				valorInvalidoBD.setReglaNegocio(curn.getReglaNegocio());
				valorInvalidoBD.setEntrada(en);
				valorInvalidoBD.setValido(false);
				
				listAleatoriasIncorrectas.add(valorInvalidoBD);
			}
		}
	}
	return listAleatoriasIncorrectas;
}
	public static List<Entrada> generarValores(Set<Entrada> entradas,
			Set<ReglaNegocio> reglasNegocio) throws Exception {
		List<Entrada> listaIncidencias = new ArrayList<Entrada>();
		/*for(ReglaNegocio rn : reglasNegocio){
		for(Entrada entrada : entradas) {

				ValorEntrada valorValidoBD = null;
				String valorCadenaValida = null;
				
				valorValidoBD=ValorEntradaBs.consultarValorValido(entrada);
				if(valorValidoBD == null) {
					valorValidoBD = new ValorEntrada();
					valorValidoBD.setReglaNegocio(null);
					valorValidoBD.setEntrada(entrada);
					valorValidoBD.setValido(true);
				}
				*/
				/*for(ReglaNegocio reglaNegocio: reglasNegocio) {
					System.out.println("Tipo de regla de negocio: " +reglaNegocio.getTipoReglaNegocio());

					if(entrada.getAtributo() != null) {
						
						boolean[] arreglo = entradaPerteneceReglaNegocioIncidencias(entrada, reglaNegocio);
						if(arreglo[0]) {
							System.out.println("Significa que la RN sí pertenece a la entrada.");
							if(arreglo[1]){
								System.out.println("Sí se genera el valor.");
								valorCadenaValida = generarValor(entrada, reglaNegocio, reglasNegocio, true); //GENERAR MÉTODO PARA QUE LA CADENA VÁLIDA CUMPLA CON TODAS LAS REGLAS DE NEGOCIO.
							}else{
								System.out.println("No se genera el valor.");//Entra curp,pero no el correo electrónico.
								System.out.println("CLAVE RN: "+reglaNegocio.getClave());
								System.out.println("NÚMERO RN: "+reglaNegocio.getNumero());
								System.out.println("NOMBRE RN: "+reglaNegocio.getNombre());
								System.out.println("DESC RN: "+reglaNegocio.getDescripcion());
								System.out.println("TIPO RN: "+reglaNegocio.getTipoReglaNegocio().getNombre());
								System.out.println("NOMBRE ENTRADA: "+entrada.getAtributo().getNombre());
								listaIncidencias.add(entrada);
								valorCadenaValida=null;
								entradas.remove(entrada);
								//break;
								//AQUÍ SE HACE LA LISTA DE LAS ENTRADAS QUE NO SE PUEDEN GENERAR. <3
								
							}
						}else{
							System.out.println("Significa que la RN no pertenece a la entrada.");
						}
					}
					else if(entrada.getTerminoGlosario() != null) {
						System.out.println("Entra a terminoGlosario");
						valorCadenaValida = "1";
					}
					
					
				}
				
				if(valorCadenaValida != null) {
					System.out.println("valorCadenaValida: "+valorCadenaValida);
					valorValidoBD.setValor(valorCadenaValida);
					ValorEntradaBs.registrarValorEntrada(valorValidoBD);
				}	
			}
		}
			return listaIncidencias;*/
		for(ReglaNegocio reglaNegocio: reglasNegocio) {//PARA GENERAR VALORES INVÁLIDOS.
				for(Entrada entrada : entradas) {
					System.out.println("ValorEntrada no es nulo");
					//if(ValorEntradaBs.consultarValores(entrada)== null){
					
					if(entrada.getAtributo() != null) {
						boolean[] arreglo = entradaPerteneceReglaNegocioIncidencias(entrada, reglaNegocio);
						if(arreglo[0]) {
							System.out.println("Significa que la RN sí pertenece a la entrada.");
							if(!arreglo[1]){
								System.out.println("No se genera el valor.");
								if (!listaIncidencias.contains(entrada)){
									//valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, true); //GENERAR MÉTODO PARA QUE LA CADENA VÁLIDA CUMPLA CON TODAS LAS REGLAS DE NEGOCIO.
									listaIncidencias.add(entrada);
								}
								
							}
						 }
							ValorEntrada valorInvalidoBD = null;
							String valorCadenaInvalido = null;
							
							valorInvalidoBD = ValorEntradaBs.consultarValorInvalido(reglaNegocio, entrada);
							
							if(valorInvalidoBD == null) {
								valorInvalidoBD = new ValorEntrada();
								valorInvalidoBD.setReglaNegocio(reglaNegocio);
								valorInvalidoBD.setEntrada(entrada);
								valorInvalidoBD.setValido(false);
							}
							
							if(!ReglaNegocioBs.esGlobal(reglaNegocio.getTipoReglaNegocio())) {
								if(entradaPerteneceReglaNegocio(entrada, reglaNegocio)) {
									valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
								}
							} else {
								valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
							}
							
							if(valorCadenaInvalido != null) { 
								valorInvalidoBD.setValor(valorCadenaInvalido);
								ValorEntradaBs.registrarValorEntrada(valorInvalidoBD);
							}
						
						
					}/*
					else if(entrada.getTerminoGlosario() != null) {
						String valorCadenaInvalido = null;
						ValorEntrada valorInvalidoBD = null;
						if(ValorEntradaBs.consultarValor(entrada.getId()))
						System.out.println("Entra a terminoGlosario");
						valorCadenaInvalido = "1";
						valorInvalidoBD.setValor(valorCadenaInvalido);
						ValorEntradaBs.registrarValorEntrada(valorInvalidoBD);
					}*/
					
				}
		}
		for(Entrada entradaIncidencia : listaIncidencias){
			//Remueve las entradas que no se pueden generar de entradas que se encuentran en la lista de incidencias.
			entradas.remove(entradaIncidencia);
		}
		
		/*for(Entrada entrada : entradas) {
				System.out.println("jdsd: "+entrada.getId());
				//if(ValorEntradaBs.consultarValores(entrada)== null){
				
			ValorEntrada valorValidoBD = ValorEntradaBs.consultarValorValido(entrada);
			String valorCadenaValido = null;
			
			if(valorValidoBD == null) {
				valorValidoBD = new ValorEntrada();
				valorValidoBD.setReglaNegocio(null);
				valorValidoBD.setEntrada(entrada);
				valorValidoBD.setValido(true);
			}
			System.out.println("ANTES DE");
			if(entrada.getAtributo() != null) {	
				System.out.println("if(entrada.getAtributo() != null) {	");
				valorCadenaValido = generarValor(entrada, null, reglasNegocio, true);
			} else if(entrada.getTerminoGlosario() != null) {
				valorCadenaValido = "1";
			}
			
			if(valorCadenaValido != null) {
				valorValidoBD.setValor(valorCadenaValido);
				ValorEntradaBs.registrarValorEntrada(valorValidoBD);
			}
			else{}
		}*/
		return listaIncidencias;
	}
	
	
	private static boolean entradaPerteneceReglaNegocio(Entrada entrada,
			ReglaNegocio reglaNegocio) {
		Atributo atributo = entrada.getAtributo();
		switch(TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio())) {
		case FORMATOCAMPO:
			if(reglaNegocio.getAtributoExpReg().getId() == atributo.getId()) {
				return true;
			} else {
				return false;
			}
		case COMPATRIBUTOS:
			if(reglaNegocio.getAtributoComp1().getId() == atributo.getId() || reglaNegocio.getAtributoComp2().getId() == atributo.getId()) {
				return true;
			} else {
				return false;
			}
		case UNICIDAD:
			if(reglaNegocio.getAtributoUnicidad().getId() == atributo.getId()) {
				return true;
			} else {
				return false;
			}
			default:
				break;
		
		}
		return false;
	}
	
	public static boolean[] entradaPerteneceReglaNegocioIncidencias(Entrada entrada,
			ReglaNegocio reglaNegocio) { //ESTE MÉTODO SE CREÓ EL VIE 21/OCT/16 A LAS 17:03 PARA ENCONTRAR QUE ENTRADAS ESTÁN RELACIONADAS A QUÉ RN. PARA GESTIÓN DE INCIDENCIAS.
		Atributo atributo = entrada.getAtributo();
		//List<bool> arreglo = new ArrayList<bool>();
		boolean[] arreglo = new boolean[2];
		//arreglo[0] indica si pertenece o no a la RN.
		//arreglo[1] indica si se puede o no generar el valor. Si no se genera el valor, se agrega a la lista de incidencias.
		switch(TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio())) {
		case LONGITUD:
			if(atributo.getLongitud()!=null) {
				arreglo[0]=true;
				arreglo[1]=true;
				return arreglo;
			}else {
				arreglo[0]=false;
				arreglo[1]=true;
				return arreglo;
			}
		case OBLIGATORIOS:
			if(atributo.isObligatorio()) {
				arreglo[0]=true;
				arreglo[1]=true;
				return arreglo;
			}else {
				arreglo[0]=false;
				arreglo[1]=true;
				return arreglo;
			} 
		case DATOCORRECTO:
			if(!TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.OTRO) ) {
				arreglo[0]=true;
				arreglo[1]=true;
				return arreglo;
			} else {
				arreglo[0]=true;
				arreglo[1]=false;
				return arreglo;
			}
		case FORMATOCAMPO:
			if(reglaNegocio.getAtributoExpReg().getId() == atributo.getId()) {
				arreglo[0]=true;
				arreglo[1]=false;
				return arreglo;
			} else {
				arreglo[0]=false;
				arreglo[1]=false;
				return arreglo;
			}
		case COMPATRIBUTOS:
			if(reglaNegocio.getAtributoComp1().getId() == atributo.getId() || reglaNegocio.getAtributoComp2().getId() == atributo.getId()) {
				arreglo[0]=true;
				arreglo[1]=false;
				return arreglo;
			} else {
				arreglo[0]=false;
				arreglo[1]=false;
				return arreglo;
			}
		case UNICIDAD:
			if(reglaNegocio.getAtributoUnicidad().getId() == atributo.getId()) {
				arreglo[0]=true;
				arreglo[1]=false;
				return arreglo;
			} else {
				arreglo[0]=false;
				arreglo[1]=false;
				return arreglo;
			}
		case OTRO:
				arreglo[0]=false;
				arreglo[1]=false;
				return arreglo;
			
			default:
				arreglo[0]=false;
				arreglo[1]=false;
				return arreglo;
		}
	}

	private static String generarValor(Entrada entrada,
			ReglaNegocio reglaNegocio, Set<ReglaNegocio> reglasNegocio,
			boolean valido) {
		Atributo atributo = entrada.getAtributo();
		String valorCadena = null;

		if(TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.OTRO)) {
			return null;
		}
		
		if(valido) {
			System.out.println("Es válido");
			valorCadena = generarCadenaValidaGlobal(entrada, reglasNegocio);
		} else {
			switch(TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio())) {
				case DATOCORRECTO:
					if(TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.CADENA) ) {
						return null;
					}
					valorCadena = "a";
					break;
				case FORMATOCAMPO:
					valorCadena = GeneradorCadenasUtil.generarCadenaAleatoria(4, true) + "xX01_ ?@{}#";
					break;
				case LONGITUD:
					if(TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.BOOLEANO) 
							|| TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.FECHA)) {
						return null; //CREO QUE NO ESTÁ IMPLEMENTADO. TENDRÍAMOS QUE GENERAR VALORES DE TIPO FECHA.
					}
					boolean numero = false;
					if(TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.CADENA)) {
						numero = false; 
					}
					if(TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.ENTERO) ||
							TipoDatoEnum.getTipoDato(atributo.getTipoDato()).equals(TipoDatoEnum.tipoDato.FLOTANTE)) {
						numero = true; 
					}
					valorCadena = GeneradorCadenasUtil.generarCadenaInvalida(atributo.getLongitud(), numero);
					break;
				case OBLIGATORIOS:
					if(entrada.getAtributo().isObligatorio()) {
						valorCadena = "";
					} else {
						return null;
					}
					break;
				default:
					return null;
			}
		}

		return valorCadena;
	}

	public static String generarCadenaValidaFormatoCorrecto(Entrada entrada, ReglaNegocio reglaNegocio) {
		return GeneradorCadenasUtil.generarCadena(reglaNegocio.getExpresionRegular());
	}
	
	public static String generarCadenaValidaComparacionAtributos(Entrada entrada, String valor, ReglaNegocio reglaNegocio) {
		return null;
	}
	
	public static String generarCadenaValidaUnicidad() {
		return null;
	}
	
	
	public static String generarCadenaValidaGlobal(Entrada entrada,
			Set<ReglaNegocio> reglasNegocio) {
		Atributo atributo = entrada.getAtributo();		
		
		String cadenaValida = null;
		System.out.println("TipoDatoEnum.getTipoDato(atributo.getTipoDato()): "+TipoDatoEnum.getTipoDato(atributo.getTipoDato()));
		switch(TipoDatoEnum.getTipoDato(atributo.getTipoDato())) {
		case ARCHIVO:
			cadenaValida = null;
			break;
		case BOOLEANO:
			if(PRISMARandomUtil.generarRandomBooleano() || atributo.isObligatorio()) {
				cadenaValida = "true";
			} else {
				cadenaValida = "false";
			}
			break;
		case CADENA:
			cadenaValida = GeneradorCadenasUtil.generarCadenaAleatoria(atributo.getLongitud(), atributo.isObligatorio());
			break;
		case ENTERO:
			cadenaValida = GeneradorCadenasUtil.generarEnteroAleatorio(atributo.getLongitud());
			break;
		case FECHA:
			cadenaValida = GeneradorCadenasUtil.generarFechaAleatoria();
			break;
		case FLOTANTE:
			cadenaValida = GeneradorCadenasUtil.generarFlotanteAleatorio(atributo.getLongitud());
			break;
		case OTRO:
			cadenaValida = null; //<--Aquí debemos regresar un dato del txt.
		/*File fichero_entrada = new File ("/Users/gerasolis/Downloads/AplicacionTTB06408OCT2016/src/main/webapp/resources/datos.txt");
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
		             System.out.println ("Dato en el txt: "+datosDeEntrada.get(ran));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
            }*/
			break;
		default:
			break;
		}
		return cadenaValida;
	}

	/*public List<Campo> obtenerCamposPrueba(Trayectoria trayectoria, boolean esPruebaCompleta) {
		List<Campo> campos = new ArrayList<Campo>();
		for(Paso pasox : trayectoria.getPasos()) {
			if(AnalizadorPasosBs.isActorOprimeBoton(pasox)) {
				Accion accion = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.ACCION).getAccionDestino();
				campos.add(new Campo(accion.getNombre(), "URL", null));
			}
			if(AnalizadorPasosBs.isActorIngresaDatos(paso)){
				for(Entrada entrada : trayectoria.getCasoUso().getEntradas()) {
					campos.add(new Campo(entrada.getAtributo().getNombre(), "Valor", null));
					
					if(esPruebaCompleta) {
						campos.add(new Campo(entrada.getAtributo().getNombre(), "Etiqueta", null));
					}
				}
			}
			if(esPruebaCompleta) {
				if(AnalizadorPasosBs.isSistemaMuestraMensaje(paso)) {
					Mensaje mensaje = (Mensaje) AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.MENSAJE).getElementoDestino();
					if(mensaje.isParametrizado()) {
						for(MensajeParametro mensajeParametro : mensaje.getParametros()) {
							campos.add(new Campo(mensajeParametro.getParametro().getNombre(), "ParametroMensaje", null));
						}
					}
				}
				if(AnalizadorPasosBs.isSistemaMuestraPantalla(paso)) {
					Pantalla pantalla = (Pantalla) AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.PANTALLA).getElementoDestino();
					campos.add(new Campo(pantalla.));
				}
			}
			
		}
		return campos;
	}
*/

	public static List<Accion> obtenerAcciones(
			Trayectoria trayectoria) {
		List<Accion> acciones = new ArrayList<Accion>();
		List<Paso> pasos = new ArrayList<Paso>();
		pasos = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		for(Paso paso : pasos) {
			if(AnalizadorPasosBs.isActorOprimeBoton(paso)) {				
				Accion accion = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.ACCION).getAccionDestino();
				acciones.add(accion);
			}
		}
		return acciones;
	}


	public static Set<Pantalla> obtenerPantallas(
			Trayectoria trayectoria) {
		Set<Pantalla> pantallas = new HashSet<Pantalla>(0);
		List<Paso> pasos = new ArrayList<Paso>();
		pasos = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		for(Paso paso : pasos) {
			if(AnalizadorPasosBs.isSistemaMuestraPantalla(paso)) {
				Pantalla pantalla = (Pantalla) AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.PANTALLA).getElementoDestino();
				pantallas.add(pantalla);
			}
		}
		return pantallas;
	}





	public static Set<ReferenciaParametro> obtenerReferenciasReglasNegocioQuery(
			Trayectoria trayectoria) {
		Set<ReferenciaParametro> referencias = new HashSet<ReferenciaParametro>(0);
		List<Paso> pasos = new ArrayList<Paso>();
		pasos = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		for(Paso paso : pasos) {
			if(AnalizadorPasosBs.isSistemaValidaReglaNegocio(paso) || AnalizadorPasosBs.isSistemaValidaPrecondicion(paso)) {
				ReferenciaParametro referenciaReglaNegocio = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.REGLANEGOCIO);
				ReglaNegocio reglaNegocio = (ReglaNegocio) referenciaReglaNegocio.getElementoDestino();
				switch(TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio())) {
				case UNICIDAD:
					referencias.add(referenciaReglaNegocio);
					break;
				case VERFCATALOGOS:
					referencias.add(referenciaReglaNegocio);
					break;
				default:
					break;
					
				}
			}
		}
		
		return referencias;
	}


	public static List<ReferenciaParametro> obtenerReferenciasParametroMensajes(
			Trayectoria trayectoria) {
		List<ReferenciaParametro> referencias = new ArrayList<ReferenciaParametro>();
		List<Paso> pasos = new ArrayList<Paso>();
		pasos = TrayectoriaBs.obtenerPasos(trayectoria.getId());
		for(Paso paso : pasos) {
			if(AnalizadorPasosBs.isSistemaMuestraMensaje(paso)) {
				ReferenciaParametro referencia = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.MENSAJE);
				Mensaje mensaje = (Mensaje) referencia.getElementoDestino();
				if(mensaje.getParametros() != null && mensaje.getParametros().size() > 0) {
					referencias.add(referencia);
				}
				
			}
		}
		return referencias;
	}
	
	public static void generarValoresTrayectorias(Set<Entrada> entradas,
			Set<ReglaNegocio> reglasNegocio, Trayectoria trayectoria) throws Exception {
		for(ReglaNegocio reglaNegocio: reglasNegocio) {
				if (trayectoria.isAlternativa()){
				for(Entrada entrada : entradas) {
					if(ValorEntradaTrayectoriaBs.consultarValor(entrada, trayectoria)== null){
					if(entrada.getAtributo() != null) {
						
							ValorEntradaTrayectoria valorInvalidoBD = null;
							String valorCadenaInvalido = null;
							
							valorInvalidoBD = ValorEntradaTrayectoriaBs.consultarValor(entrada, trayectoria);
							if(valorInvalidoBD == null) {
								valorInvalidoBD = new ValorEntradaTrayectoria();
								valorInvalidoBD.setTrayectoria(trayectoria);
								valorInvalidoBD.setEntrada(entrada);
								valorInvalidoBD.setValido(false);
							}
							
							if(!ReglaNegocioBs.esGlobal(reglaNegocio.getTipoReglaNegocio())) {
								if(entradaPerteneceReglaNegocio(entrada, reglaNegocio)) {
									valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
								}
							} else {
								valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
							}
							
							if(valorCadenaInvalido != null) { 
								valorInvalidoBD.setValor(valorCadenaInvalido);
								ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(valorInvalidoBD);
							}
							}
				}
				else{
				}
				}
				/*for(Entrada entrada : entradas) {
					if(ValorEntradaBs.consultarValores(entrada) == null){
					ValorEntradaTrayectoria valorValidoBD = ValorEntradaTrayectoriaBs.consultarValor(entrada, trayectoria);
					String valorCadenaValido = null;
					
					if(valorValidoBD == null) {
						valorValidoBD = new ValorEntradaTrayectoria();
						valorValidoBD.setTrayectoria(trayectoria);
						valorValidoBD.setEntrada(entrada);
						valorValidoBD.setValido(true);
					}
					
					if(entrada.getAtributo() != null) {	
						valorCadenaValido = generarValor(entrada, null, reglasNegocio, true);
					} else if(entrada.getTerminoGlosario() != null) {
						valorCadenaValido = "1";
					}
					
					if(valorCadenaValido != null) {
						valorValidoBD.setValor(valorCadenaValido);
						ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(valorValidoBD);
						}
				}
				else{}
					}
				}
				
						else{
							
							for(Entrada entrada : entradas) {
								if(ValorEntradaBs.consultarValores(entrada) == null){
								if(entrada.getAtributo() != null) {
						ValorEntrada valorInvalidoBD = null;
						String valorCadenaInvalido = null;
						
						valorInvalidoBD = ValorEntradaBs.consultarValorInvalido(reglaNegocio, entrada);
						
						if(valorInvalidoBD == null) {
							valorInvalidoBD = new ValorEntrada();
							valorInvalidoBD.setReglaNegocio(reglaNegocio);
							valorInvalidoBD.setEntrada(entrada);
							valorInvalidoBD.setValido(false);
						}
						
						if(!ReglaNegocioBs.esGlobal(reglaNegocio.getTipoReglaNegocio())) {
							if(entradaPerteneceReglaNegocio(entrada, reglaNegocio)) {
								valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
							}
						} else {
							valorCadenaInvalido = generarValor(entrada, reglaNegocio, reglasNegocio, false);
						}
						
						if(valorCadenaInvalido != null) { 
							valorInvalidoBD.setValor(valorCadenaInvalido);
							ValorEntradaBs.registrarValorEntrada(valorInvalidoBD);
						}
					}
				}
								else{}
			}
								
								
							for(Entrada entrada : entradas) {
								if(ValorEntradaBs.consultarValores(entrada) == null){
								ValorEntrada valorValidoBD = ValorEntradaBs.consultarValorValido(entrada);
								String valorCadenaValido = null;
								
								if(valorValidoBD == null) {
									valorValidoBD = new ValorEntrada();
									valorValidoBD.setReglaNegocio(null);
									valorValidoBD.setEntrada(entrada);
									valorValidoBD.setValido(true);
								}
								
								if(entrada.getAtributo() != null) {	
									valorCadenaValido = generarValor(entrada, null, reglasNegocio, true);
								} else if(entrada.getTerminoGlosario() != null) {
									valorCadenaValido = "1";
								}
								
								if(valorCadenaValido != null) {
									valorValidoBD.setValor(valorCadenaValido);
									ValorEntradaBs.registrarValorEntrada(valorValidoBD);
									}
								
							}else{}
								}
							
				}
		
		

		
	
	}*/
				}
		}
		
}
}
	

