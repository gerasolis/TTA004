package mx.prisma.generadorPruebas.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import com.opensymphony.xwork2.Action;
import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.ValorDesconocidoBs;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.SessionManager;

@ResultPath("/content/generadorPruebas/")
@Results({
	//@Result(name = "pantallaConfiguracionTrayectorias", type = "dispatcher", location = "configuracion/trayectorias.jsp"),
	@Result(name = "error", type = "dispatcher", location = "configuracion/desconocido.jsp"),
	@Result(name = "error_1", type = "redirectAction", params = {
			"actionName", "configuracion-trayectoria!prepararConfiguracion" }),
	@Result(name = "pantallaConfiguracionTrayectoria", type = "redirectAction", params = {
			"actionName", "configuracion-trayectoria!cargarConfiguracion" }),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = {
			"actionName", "modulos" }),
	@Result(name = "anterior", type = "redirectAction", params = {
			"actionName", "configuracion-trayectorias!prepararConfiguracion" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""})})
public class ConfiguracionDesconocidosCtrl extends ActionSupportPRISMA{
	
	private static final long serialVersionUID = 1L;
	private Colaborador colaborador;
	private Trayectoria previo;
	private Proyecto proyecto;
	private Modulo modulo;
	private Integer idCU;
	private List<File> file;
	private CasoUso casoUso;
	private String jsonAcciones;
	private Integer idTrayectoria;
	private List<String> filename;
	private Trayectoria trayectoria;
	private List<String> idAtributo;
	private List<Entrada> listaIncidencias = new ArrayList<Entrada>();
	private List<Atributo>listaAtributo = new ArrayList<Atributo>();

    
	public String prepararConfiguracion() {
		SessionManager.delete("mensajesAccion");
        Map<String, Object> session = null;
		String resultado="";

		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();
			trayectoria = SessionManager.consultarTrayectoriaActual();
		
			System.out.println("Id del atributo: "+getIdAtributo());
		
							
			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}
			if (modulo == null) {
				resultado = "modulos";
				return resultado;
			}
			if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				return resultado;
			}
			if(getUpload()==null){
				resultado="error_1";
				addActionMessage(getText("MSG42", new String[] { "No",
						"ingresado", "archivo txt" }));
				SessionManager.set(this.getActionMessages(), "mensajesAccion");
				//SessionManager.set(this.casoUso.getId(), "casoUsoId");
				
			}else{
				ValorDesconocidoBs.crearArchivo(getUpload(),getIdAtributo(),getUploadFileName());
				resultado="pantallaConfiguracionTrayectoria";
				addActionMessage(getText("MSG41", new String[] { "Los",
						"txt", "registrado" }));
				SessionManager.set(this.getActionMessages(), "mensajesAccion");
			}

				/*if(jsonAcciones == null || jsonAcciones.isEmpty()) {//¿SE LLENARÁN ESTOS VALORES MANUALMENTE?
					obtenerJsonCamposAcciones(trayectoria);
				}*/
				
				/*@SuppressWarnings("unchecked")
				Collection<String> msjs = (Collection<String>) SessionManager
						.get("mensajesAccion");
				this.setActionMessages(msjs);
				SessionManager.delete("mensajesAccion");
				
				@SuppressWarnings("unchecked")
				Collection<String> msjsError = (Collection<String>) SessionManager
						.get("mensajesError");
				this.setActionErrors(msjsError);
				SessionManager.delete("mensajesError");*/
			} catch(Exception e) {
				ErrorManager.agregaMensajeError(this, e);
				SessionManager.set(this.getActionErrors(), "mensajesError");
				resultado = "anterior";
			}
			return resultado; 
	}
	
	/*public String getPic() {
		return filename;
	}

	public void setPic(String filename) {
		this.filename = filename;
	}*/
	public void setUpload(List<File> file) {
		this.file = file;
	}
	public List<File> getUpload() {
		return file;
	}
	public List<String> getIdAtributo(){
		return idAtributo;
	}
	public void setIdAtributo(List<String> idAtributo){
		this.idAtributo = idAtributo;
	}
	public void setUploadFileName(List<String> filename) {
        this.filename = filename;
     }
	public List<String> getUploadFileName() {
        return filename;
     }
}
