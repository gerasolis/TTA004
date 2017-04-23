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
	private List<ErroresPrueba> listErrores;
	private List<CasoUso> listCasosUso;
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
			listPantallas = EjecutarPruebaBs.consultarPantallas();
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
														if(v.getReferenciaParametro().getPaso().getRedaccion().equals(p.getRedaccion())){
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
											//Comparamos la cadena con las cadenas de los mensajes de error.
											for (ErroresPrueba liste : listErrores){
												//byte ptext[] = liste.getTipoError().getBytes(ISO_8859_1); 
												//String value = new String(ptext, UTF_8); 
												try{
													//String[] prueba = value.split("/");
													String[] prueba = liste.getTipoError().split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadenaUsar.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
		
												}catch(Exception e){
													//if(cadenaUsar.equals(value)){
													if(cadenaUsar.equals(liste.getTipoError())){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
												} 
											}
											cadenaUsar="";
								
										}else{
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
													//String[] prueba = value.split("/");
													String[] prueba = liste.getTipoError().split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadena.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
		
												}catch(Exception e){
													//if(cadena.equals(value)){
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
    	String resultado="";
    	List<File> listaArchivos = new ArrayList<File>();
    	try {
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
					//Empezamos a ejecutar la prueba para todos los casos de uso. PENDIENTE.
					
					generarPrueba(casoUso);
					System.out.println("Antes de entrar a descargarPrueba");
					listaArchivos.add(descargarPrueba(casoUso));
					
					//Utilizar el método de ejecutarPruebaAutomática(), pero para todos los casos de uso de listCasoUso.
					//resultado = "documento";
					
				}
				
					descargarPruebaGeneral();
					for(File archivo : listaArchivos){
						//FileUtil.delete(archivo);
					}
				
				System.out.println("Después de generar el documento general");
				return "documentoGeneral";
				//return "cu";
			}
			
    	}catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
    	addActionMessage(getText("MSG45", new String[] { "prueba general",
		"ejecutada"}));
		SessionManager.set(this.getActionMessages(), "mensajesAccion");
    	return resultado;
    }
    
    public String generarPrueba(CasoUso casoUso) {
		try {
			@SuppressWarnings("deprecation")
			String ruta = request.getRealPath("/") + "/tmp/pruebas/"; 
			
			SessionManager.set(ruta, "rutaPruebaGenerada");
			//SessionManager.set(idCU, "idCUPruebaGenerada");
			SessionManager.set(true, "pruebaGenerada");
			
			//casoUso = CuBs.consultarCasoUso(idCU);
			
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
		//int idCUPruebaGenerada = (Integer)SessionManager.get("idCUPruebaGenerada");
		//casoUso = CuBs.consultarCasoUso(idCUPruebaGenerada);
		//String ruta = (String) SessionManager.get("rutaPruebaGenerada");
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
   
	public void descargarPruebaGeneral() {
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

}
