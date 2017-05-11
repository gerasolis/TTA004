package mx.prisma.generadorPruebas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import static java.nio.charset.StandardCharsets.*;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.MensajeBs;
import mx.prisma.editor.bs.MensajeParametroBs;
import mx.prisma.editor.bs.PasoBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.EjecutarPruebaBs;
import mx.prisma.generadorPruebas.bs.GeneradorPruebasBs;
import mx.prisma.generadorPruebas.bs.PruebaBs;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroBs;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.guionPruebas.bs.GuionPruebasBs;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.FileUtil;
import mx.prisma.util.SessionManager;

@ResultPath("/content/generadorPruebas/")
@Results({
	@Result(name = "pantallaReporteGeneral", type = "dispatcher", location = "configuracion/reporteGeneral.jsp"),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""}),
	@Result(name = "documentoGeneral", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStreamGeneral", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filenameGeneral}\""}),
	@Result(name = "error", type = "redirectAction", params = {
		"actionName", "cu" })})

public class ConfiguracionCasosUsoCtrl extends ActionSupportPRISMA{
	private static final long serialVersionUID = 1L;
	private Integer idCU;
	private CasoUso casoUso;
	private Modulo modulo;
	private Proyecto proyecto;
	private Boolean pruebaGeneradaS;
	private List<ErroresPrueba> listErrores;
	private List<CasoUso> listCasosUso;
	private List<String> listPruebaGenerada;
	private List<Prueba> listPruebas;
	private List<Pantalla> listPantallas;
	private List<Mensaje> listMensajes;
	private List<Entrada> listEntradas;
	private List<ValorMensajeParametro> listMensajeValorParametro;
	private InputStream fileInputStream;
	private InputStream fileInputStreamGeneral;
	private String type;
    private String filename;
    private String filenameGeneral;
	
    public String generarReporteGeneral() {
		int contador2=0;
		String cadena="";
		String cadenaUsar="";
		String resultado="";
		String cadenaOriginal="";
		int z=0;
		listEntradas = new ArrayList<Entrada>();
		try {
			modulo = SessionManager.consultarModuloActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			if (modulo == null) {
				System.out.println("error");
				return resultado;
			}
			listCasosUso = EjecutarPruebaBs.consultarCasosUso(modulo);
			listPruebas = PruebaBs.consultarPruebas();
			//listErrores = EjecutarPruebaBs.consultarErroresCasosUso(modulo);
			listErrores = EjecutarPruebaBs.consultarErrores();
			listPantallas = EjecutarPruebaBs.consultarPantallas(modulo);
			listMensajes = EjecutarPruebaBs.consultarMensajes();
			listMensajeValorParametro = EjecutarPruebaBs.consultarValorMensajeParametros();
			listEntradas = EjecutarPruebaBs.consultarEntradas(); //Sacar toda la lista de entradas y hacer la relación en el front.
			for(Entrada e : listEntradas){
				System.out.println(e.getCasoUso().getId()+"------------------------------------------------");
			}
			//Sacamos pantalla de CU primero.
			for(CasoUso casoUso : EjecutarPruebaBs.consultarCasosUso(modulo)){
				if(casoUso.getReporte()){
					for(Trayectoria t : casoUso.getTrayectorias()){
						for(Paso p : TrayectoriaBs.obtenerPasos(t.getId())){
							if(p.getRedaccion().contains(TokenBs.tokenMSG)){
								for(ReferenciaParametro rp : PasoBs.obtenerReferencias(p.getId())){
									if(rp.getTipoParametro().getId() == 6){
										Mensaje m = MensajeBs.consultarMensaje(rp.getElementoDestino().getId());
										cadena = m.getRedaccion();
										z=0;
						                if(cadena.charAt(0) == '$'){
						                    cadena = cadena.substring(1,cadena.length());
						                }
						                cadenaUsar = cadena;
						                //System.out.println(m.getRedaccion());
						                //Aquí sustituyo los token y comparo con los mensajes de la prueba.
						                //primero cuento la cantidad de tokens sólo si es de tipo parametrizado.
						                if(m.isParametrizado()){
						                	
						                	//Obtenemos la lista de paramentros del mensaje
						                    List<String> parametros = GuionPruebasBs.obtenerTokens(m);
						                    for(String param : parametros){
						                    	System.out.println("Param: "+param);
						                    	if(param.charAt(0) == '$'){
						                            param = param.substring(1,param.length());
						                        }
						                    	
						                    	for(MensajeParametro mp : MensajeParametroBs.consultarMensajeParametro_(m.getId())){
						                    		for(ValorMensajeParametro v : ValorMensajeParametroBs.consultarValores_(mp.getId())){
						                    			if(v.getReferenciaParametro().getPaso().getRedaccion().equals(p.getRedaccion())){
						                    				String paramOriginal = TokenBs.decodificarCadenasToken(" "+param);
						                    				System.out.println("paramOriginal: "+paramOriginal);
						                    				System.out.println("Antes de entrar al if");
						                    				System.out.println(v.getMensajeParametro().getParametro().getNombre());
						                    				System.out.println(paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length()));
						                    				String cadenaParam = paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length());
						                    				System.out.println("cadenaParam: "+cadenaParam);
						                    				if(cadenaParam.charAt(cadenaParam.length()-1) == '.'){
						                    					System.out.println("sÍ TIENE PUNTO");
						                    					cadenaParam = cadenaParam.replace(".", "");
						                    				}
						                    				
						                    				if(v.getMensajeParametro().getParametro().getNombre().equals(cadenaParam)){
						                    					System.out.println("DespuÉs de entrar al if");
						                    					System.out.println("**************** PARAMETRO DE V EN PARAMETRO MENSAJE *****************");
						                    					System.out.println("**************** "+cadena+" *****************");
						                    					if(z==0){
						                    						cadenaOriginal = cadena;
						                    					}
						                    					paramOriginal = paramOriginal.substring(paramOriginal.indexOf("·")+1, paramOriginal.length());
						                    					cadena = TokenBs.remplazoToken(cadena,param, v.getValor());
						                    					System.out.println("**************** "+cadena+" *****************");
						                    					if(cadena.charAt(0) == '$'){
						        				                    cadena = cadena.substring(1,cadena.length());
						        				                }
						                    					if(cadenaOriginal.charAt(0) == '$'){
						                    						cadenaOriginal = cadenaOriginal.substring(1,cadenaOriginal.length());
						        				                }
						                    					System.out.println("**************** "+cadenaOriginal+" *****************");
						                    					break;
						                    				}
						                    			}
						                    		}
						                    	}
						                    	z++;
						                      }
											
						                    System.out.println("*********************************");
											System.out.println("Cadena convertida: "+cadena);
											System.out.println("Cadena original: "+cadenaOriginal);
											//Comparamos la cadena con las cadenas de los mensajes de error.
											for (ErroresPrueba liste : listErrores){
												//byte ptext[] = liste.getTipoError().getBytes(ISO_8859_1); 
												//String value = new String(ptext, UTF_8); 
												try{
													String[] prueba = liste.getTipoError().split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadenaOriginal.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														liste.setTipoError("Test failed: text expected to contain /" +cadena+"/");
														EjecutarPruebaBs.modificarError(liste);
													}

												}catch(Exception e){
													if(cadenaOriginal.equals(liste.getTipoError())){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														liste.setTipoError("Test failed: text expected to contain /" +cadena+"/");
														EjecutarPruebaBs.modificarError(liste);
													}
												} 
											}
											//cadenaUsar="";
						                    
						                    
										}else{
											System.out.println("Entra al else");
											cadena = m.getRedaccion();
											System.out.println("*********************************");
											System.out.println("CADENA NO PARAMETRIZADA A USAR: "+cadena);
											if(cadena.charAt(0) == '$'){
												cadena = cadena.substring(1,cadena.length());
												System.out.println("CADENA NO PARAMETRIZADA A USAR SIN $: "+cadena);
											}
											for (ErroresPrueba liste : listErrores){
												//byte ptext[] = liste.getTipoError().getBytes(ISO_8859_1); 
												//String value = new String(ptext, UTF_8); 
												try{
													String[] prueba = liste.getTipoError().split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadena.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}

												}catch(Exception e){
													if(cadena.equals(liste.getTipoError())){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
												} 
											}
										}//aquí acaba el else
									}
								}
							}
						}
					}
				}
			}
			resultado = "pantallaReporteGeneral";
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
		return resultado;
	}
    
    public String ejecutarPruebaGeneral() {
    	listPruebaGenerada = new ArrayList<String>();
    	String resultado="";
    	List<File> listaArchivos = new ArrayList<File>();
    	try{
	    	modulo = SessionManager.consultarModuloActivo();
			listCasosUso = EjecutarPruebaBs.consultarCasosUso(modulo);
			int i=0;int bandera=0;
			for (CasoUso casoUso : listCasosUso){
				System.out.println(casoUso.getNombre()+" "+casoUso.getEstadoElemento().getNombre());
				if (casoUso.getEstadoElemento().getNombre().equals("Configurado") || casoUso.getEstadoElemento().getNombre().equals("Liberado")){
					bandera++;	
				}else{
					resultado = "error";
					if (i==0){
						addActionMessage(getText("MSG43", new String[] { "prueba",
						"ejecutada"}));
						
			    	}
					i=1;
					addActionMessage(getText("MSG44", new String[] { casoUso.getNombre(),
							"no", "configurado" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}
			}
			System.out.println("Casos de uso configurados o liberados: "+bandera);
			System.out.println("Total casos de uso: "+listCasosUso.size());
			if(bandera == listCasosUso.size()){
				System.out.println("Entra a la prueba general");
				for (CasoUso casoUso : listCasosUso){
					generarPrueba(casoUso);
					System.out.println("Antes de entrar a descargarPrueba");
					listaArchivos.add(descargarPrueba(casoUso));
				}
					//descargarPruebaGeneral();				
				System.out.println("Después de generar el documento general");
				SessionManager.set(true, "pruebaGenerada2");
			}
		}catch(Exception e){
			
		}
    	addActionMessage(getText("MSG45", new String[] { "prueba general",
		"ejecutada"}));
		SessionManager.set(this.getActionMessages(), "mensajesAccion");
    	return "cu";
    }
    
    public String generarPrueba(CasoUso casoUso) {
		try {
			@SuppressWarnings("deprecation")
			String ruta = request.getRealPath("/") + "/tmp/pruebas/"; 
			SessionManager.set(ruta, "rutaPruebaGenerada");
			GeneradorPruebasBs.generarCasosPrueba(casoUso, ruta);
			
		} catch (Exception e) {
			SessionManager.delete("rutaPruebaGenerada");
			SessionManager.delete("idCUPruebaGenerada");
			SessionManager.delete("pruebaGenerada");
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
		}
		return "cu";
	}
	public File descargarPrueba(CasoUso casoUso) {
		String rutaZIP= request.getRealPath("/") + "/tmp/zip/"; 
		String ruta = request.getRealPath("/") + "/tmp/pruebas/"; 
		
		System.out.println("RUTA: "+ruta);
		SessionManager.delete("rutaPruebaGenerada");
		SessionManager.delete("idCUPruebaGenerada");
		SessionManager.delete("pruebaGenerada");
		
		filename = casoUso.getClave() + casoUso.getNumero() + "_" + casoUso.getNombre().replace(" ", "_") + ".zip";

		type = "application/zip";

		String rutaFolder = ruta + casoUso.getId() + "/";
		System.out.println("RUTA_folder: "+rutaFolder);
		
		String rutaReporte = rutaFolder+"reporte/";
		File file = new File(rutaReporte);
		file.getParentFile().mkdirs();
		
		File file2 = new File(rutaZIP);
		file2.getParentFile().mkdirs();
		try{
			String comando2 = "mkdir "+rutaZIP;
	        Process p2 = Runtime.getRuntime().exec(comando2);
		}catch(Exception e){System.out.println("No se pudo crear la carpeta.");}
		
		EjecutarPruebaBs.ejecutarPruebaAutomatica(rutaReporte,casoUso,rutaFolder);
		
		System.out.println("Después de ejecutar prueba automática");	
		try {
				FileUtil.zipIt(rutaFolder, rutaZIP + filename);
		    	
		        File doc = new File(rutaZIP + filename);
		        this.fileInputStream = new FileInputStream(doc);
		        File pruebaCU = new File(ruta);
		        FileUtil.delete(pruebaCU);
		        return pruebaCU;
	        } catch (Exception e) {
	        	ErrorManager.agregaMensajeError(this, e);
	        	 File pruebaCU = null;
	        	return pruebaCU;
	        }
	}
   
	public String descargarPruebaGeneral() {
		System.out.println(SessionManager.get("pruebaGenerada2"));
		String rutaZIP= request.getRealPath("/") + "/tmp/zip/"; 
		try {
				filenameGeneral = "PruebaGeneral.zip";
				type = "application/zip";
				FileUtil.zipIt(rutaZIP, rutaZIP + filenameGeneral);
		    	
		        File doc = new File(rutaZIP + filenameGeneral);
		        this.fileInputStreamGeneral = new FileInputStream(doc);
		        File pruebaCU = new File(rutaZIP);
		        FileUtil.delete(pruebaCU);
	        } catch (Exception e) {
	        	ErrorManager.agregaMensajeError(this, e);
	        	System.out.println("Error");
	        }
		return "documentoGeneral";
	}
	
	public List<Entrada> getListEntradas(){
		return listEntradas;
	}
	public void setListEntradas(List<Entrada> listEntradas){
		this.listEntradas = listEntradas;
	}
	
	public List<Pantalla> getListPantallas(){
		return listPantallas;
	}
	public void setListPantallas(List<Pantalla> listPantallas){
		this.listPantallas = listPantallas;
	}
	public List<Mensaje> getListMensajes(){
		return listMensajes;
	}
	public void setListMensajes(List<Mensaje> listMensajes){
		this.listMensajes = listMensajes;
	}
    
    public Modulo getModulo(){
    	return modulo;
    }
    public void setModulo(Modulo modulo){
    	this.modulo = modulo;
    }
    public List<ErroresPrueba> getListErrores(){
		return listErrores;
	}
	public void setListErrores(List<ErroresPrueba> listErrores){
		this.listErrores = listErrores;
	}
	
	public List<CasoUso> getListCasosUso(){
		return listCasosUso;
	}
	public void setListCasoUso(List<CasoUso> listCasosUso){
		this.listCasosUso = listCasosUso;
	}
	
	public List<Prueba> getListPruebas(){
		return listPruebas;
	}
	public void setListPruebas(List<Prueba> listPruebas){
		this.listPruebas = listPruebas;
	}
	
	public List<String> getListPruebaGenerada(){
		return listPruebaGenerada;
	}
	public void setListPruebaGenerada( List<String> listPruebaGenerada){
		this.listPruebaGenerada = listPruebaGenerada;
	}
	
	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public InputStream getFileInputStreamGeneral() {
		return fileInputStreamGeneral;
	}

	public void setFileInputStreamGeneral(InputStream fileInputStreamGeneral) {
		this.fileInputStreamGeneral = fileInputStreamGeneral;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilenameGeneral() {
		return filenameGeneral;
	}

	public void setFilenameGeneral(String filenameGeneral) {
		this.filenameGeneral = filenameGeneral;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Boolean getPruebaGeneradaS() {
		if(pruebaGeneradaS == null){
			pruebaGeneradaS = (Boolean)SessionManager.get("pruebaGenerada2");
		}
		return pruebaGeneradaS;
	}

	public void setPruebaGeneradaS(Boolean pruebaGeneradaS) {
		if(pruebaGeneradaS == null){
			pruebaGeneradaS = (Boolean)SessionManager.get("pruebaGenerada2");
		}
		this.pruebaGeneradaS = pruebaGeneradaS;
		
	}

}
