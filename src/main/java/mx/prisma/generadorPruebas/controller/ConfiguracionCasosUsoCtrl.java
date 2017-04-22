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
	private String type;
    private String filename;
	
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
											System.out.println("*********************************");
											//Comparamos la cadena con las cadenas de los mensajes de error.
											for (ErroresPrueba liste : listErrores){
												byte ptext[] = liste.getTipoError().getBytes(ISO_8859_1); 
												String value = new String(ptext, UTF_8); 
												try{
													String[] prueba = value.split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadenaUsar.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
		
												}catch(Exception e){
													if(cadenaUsar.equals(value)){
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
											for (ErroresPrueba liste : listErrores){
												byte ptext[] = liste.getTipoError().getBytes(ISO_8859_1); 
												String value = new String(ptext, UTF_8); 
												try{
													String[] prueba = value.split("/");
													System.out.println("ERROR: "+prueba[1]);
													if(cadena.equals(prueba[1])){
														System.out.println("ENTRA");
														liste.setMensajeid(m);
														liste.setPasoid(p);
														EjecutarPruebaBs.modificarError(liste);
													}
		
												}catch(Exception e){
													if(cadena.equals(value)){
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
					descargarPrueba(casoUso);
					//Utilizar el método de ejecutarPruebaAutomática(), pero para todos los casos de uso de listCasoUso.
					resultado = "cu";
					
				}
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
	public String descargarPrueba(CasoUso casoUso) {
		//int idCUPruebaGenerada = (Integer)SessionManager.get("idCUPruebaGenerada");
		//casoUso = CuBs.consultarCasoUso(idCUPruebaGenerada);
		String ruta = (String) SessionManager.get("rutaPruebaGenerada");
		
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
		EjecutarPruebaBs.ejecutarPruebaAutomatica(rutaReporte,casoUso,rutaFolder);
		
		System.out.println("Después de ejecutar prueba automática");	
		try {
				FileUtil.zipIt(rutaFolder, ruta + filename);
		    	
		        File doc = new File(ruta + filename);
		        this.fileInputStream = new FileInputStream(doc);
		        File pruebaCU = new File(ruta);
		        FileUtil.delete(pruebaCU);
	        } catch (Exception e) {
	        	ErrorManager.agregaMensajeError(this, e);
	        	return "cu";
	        }
			
		
	    return "cu";
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
}
