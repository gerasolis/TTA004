package mx.prisma.generadorPruebas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.generadorPruebas.bs.EjecutarPruebaBs;
import mx.prisma.generadorPruebas.bs.GeneradorPruebasBs;
import mx.prisma.generadorPruebas.bs.PruebaBs;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.FileUtil;
import mx.prisma.util.SessionManager;

@ResultPath("/content/generadorPruebas/")
@Results({
	@Result(name = "pantallaReporteGeneral", type = "dispatcher", location = "configuracion/reporteGeneral.jsp"),
	@Result(name = "error", type = "redirectAction", params = {
		"actionName", "cu" })})

public class ConfiguracionCasosUsoCtrl extends ActionSupportPRISMA{
	private static final long serialVersionUID = 1L;
	private Integer idCU;
	private CasoUso casoUso;
	private Modulo modulo;
	private List<ErroresPrueba> listErrores;
	private List<CasoUso> listCasosUso;
	private List<Prueba> listPruebas;
	
    public String generarReporteGeneral() {
		String resultado="";
		try {
			modulo = SessionManager.consultarModuloActivo();
			if (modulo == null) {
				System.out.println("error");
				return resultado;
			}
			listCasosUso = EjecutarPruebaBs.consultarCasosUso(modulo);
			listPruebas = PruebaBs.consultarPruebas();
			//listErrores = EjecutarPruebaBs.consultarErroresCasosUso(modulo);
			listErrores = EjecutarPruebaBs.consultarErrores();
			/*
			for(ErroresPrueba e : listErrores){
				System.out.println(e.getTipoError());
				System.out.println(e.getCasoUsoid().getDescripcion());
			}*/
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
			for (CasoUso casoUso : listCasosUso){
				System.out.println("CU : "+casoUso.getNombre()+" , "+casoUso.getEstadoElemento().getNombre());
				if (!casoUso.getEstadoElemento().getNombre().equals("Configurado")){
					resultado = "error";
					addActionMessage(getText("MSG43", new String[] { casoUso.getNombre(),
							"no", "configurado" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}else{
					//Empezamos a ejecutar la prueba para todos los casos de uso.
				}
			}
    	}catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
    	return resultado;
    }
    /*
    public String generarPrueba() {
		try {
			@SuppressWarnings("deprecation")
			String ruta = request.getRealPath("/") + "/tmp/pruebas/"; 
			
			SessionManager.set(ruta, "rutaPruebaGenerada");
			SessionManager.set(idCU, "idCUPruebaGenerada");
			SessionManager.set(true, "pruebaGenerada");
			
			casoUso = CuBs.consultarCasoUso(idCU);
			
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
	public String descargarPrueba() {
		int idCUPruebaGenerada = (Integer)SessionManager.get("idCUPruebaGenerada");
		casoUso = CuBs.consultarCasoUso(idCUPruebaGenerada);
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
			
		
	    return "documento";
	}*/
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
}
