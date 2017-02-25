package mx.prisma.gestorEntradas.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.guionPruebas.bs.ValorEntradaBs;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.SessionManager;

@ResultPath("/content/gestorEntradas/")
@Results({
	@Result(name = "pantallaEntradas", type = "dispatcher", location = "entradasGuion.jsp"),
	@Result(name = "error_1", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = {
			"actionName", "modulos" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""})})

public class ConfigurarEntradasCtrl extends ActionSupportPRISMA {
	
	private static final long serialVersionUID = 1L;
	
	// Proyecto y módulo
	private Proyecto proyecto;
	private Modulo modulo;
	private Colaborador colaborador;
	
	// Modelo
	private CasoUso casoUso;
	private Integer idCU;				//Identificador del caso de uso
	private ConfiguracionBaseDatos cbd;
	private ConfiguracionHttp chttp;
	
	// Lista de registros
	private List<Atributo> listEntradas = new ArrayList<Atributo>();
	private List<File> vci;
	private List<File> vcm;
	private List<File> vi;
	private List<String> idAtributo;
	private List<String> filename;
		
	//Función para mostrar la pantalla de configuración de entradas o el guion de prueba
	public String prepararConfiguracion() {
		SessionManager.delete("mensajesAccion");
	    Map<String, Object> session = null;
		String resultado="";

		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();
				
			//Si el caso de uso es nulo
			if (casoUso == null) {
				//Obtiene el valor de idCU que se mandó como parámetro
				session = ActionContext.getContext().getSession();
				session.put("idCU", idCU);
				//Consulta el caso de uso
				casoUso = SessionManager.consultarCasoUsoActivo();
			}
				
			if (casoUso == null) {
				resultado = "cu";
				System.out.println("CU");
				return resultado;
			}
			if (modulo == null) {
				resultado = "modulos";
				System.out.println("M");
				return resultado;
			}
			if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				System.out.println("C");
				return resultado;
			}
			
			//Mostramos la pantalla para cargar los archivos de las entradas
			for(Entrada entrada: casoUso.getEntradas()){
				System.out.println(entrada.getNombreHTML());
				listEntradas.add(entrada.getAtributo());
			}
			resultado = "pantallaEntradas";
				
			} catch(Exception e) {
				ErrorManager.agregaMensajeError(this, e);
				SessionManager.set(this.getActionErrors(), "mensajesError");
				resultado = "anterior";
			}
			return resultado; 
	}
	
	public static boolean tieneEntradas(int id) {
		CasoUso casoUso = CuBs.consultarCasoUso(id);
		System.out.println("Entra a tieneEntradas");
		try {
			if(casoUso.getEntradas().size()>0){
				return true;
			}else{
				return false;
			}
		} catch (PRISMAException pe) {
			return false;
		}
	}
	
	//Función para agregar valores de entrada y redireccionar al guion de prueba
		public String anadirValoresEntradas(){
			SessionManager.delete("mensajesAccion");
	        Map<String, Object> session = null;
			String resultado="";

			try {
				colaborador = SessionManager.consultarColaboradorActivo();
				proyecto = SessionManager.consultarProyectoActivo();
				modulo = SessionManager.consultarModuloActivo();
				casoUso = SessionManager.consultarCasoUsoActivo();
				
				//Si el caso de uso es nulo
				if (casoUso == null) {
					//Obtiene el valor de idCU que se mandó como parámetro
					session = ActionContext.getContext().getSession();
					session.put("idCU", idCU);
					//Consulta el caso de uso
					casoUso = SessionManager.consultarCasoUsoActivo();
				}
				
				if (casoUso == null) {
					resultado = "cu";
					System.out.println("CU");
					return resultado;
				}
				if (modulo == null) {
					resultado = "modulos";
					System.out.println("M");
					return resultado;
				}
				if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
					resultado = Action.LOGIN;
					System.out.println("C");
					return resultado;
				}
				
				//Guardamos los valores correctos (insertar) 
				if(getVci()==null){
					resultado="error_1";
					addActionMessage(getText("MSG42", new String[] { "No",
							"ingresado", "archivo txt" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
					//SessionManager.set(this.casoUso.getId(), "casoUsoId");
				}else{
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),2);
					resultado="cu";
					addActionMessage(getText("MSG41", new String[] { "Los",
							"txt", "registrado" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}
				//Guardamos los valores correctos (modificar) 
				if(getVcm()==null){
					resultado="error_1";
					addActionMessage(getText("MSG42", new String[] { "No",
							"ingresado", "archivo txt" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
					//SessionManager.set(this.casoUso.getId(), "casoUsoId");
				}else{
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),3);
					resultado="cu";
					addActionMessage(getText("MSG41", new String[] { "Los",
							"txt", "registrado" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}
				//Guardamos los valores incorrectos
				if(getVci()==null){
					resultado="error_1";
					addActionMessage(getText("MSG42", new String[] { "No",
							"ingresado", "archivo txt" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
					//SessionManager.set(this.casoUso.getId(), "casoUsoId");
				}else{
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),4);
					resultado="cu";
					addActionMessage(getText("MSG41", new String[] { "Los",
							"txt", "registrado" }));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}
				
				} catch(Exception e) {
					ErrorManager.agregaMensajeError(this, e);
					SessionManager.set(this.getActionErrors(), "mensajesError");
					resultado = "anterior";
				}
				return resultado; 
		}
	
	//Getters y setter
	public Proyecto getProyecto() {
		return proyecto;
	}
	
	public Modulo getModulo() {
		return modulo;
	}
	
	public Colaborador getColaborador() {
		return colaborador;
	}
	
	public CasoUso getCasoUso(){
		return casoUso;
	}
	
	public List<Atributo> getListEntradas() {
		return listEntradas;
	}
	
	public Integer getIdCU() {
		return idCU;
	}
	
	public ConfiguracionBaseDatos getCbd() {
		if (cbd == null) {
			cbd = new ConfiguracionBaseDatos();
			cbd.setCasoUso(casoUso);
		}
		return cbd;
	}

	public void setCbd(ConfiguracionBaseDatos cbd) {
		this.cbd = cbd;
	}

	public ConfiguracionHttp getChttp() {
		if (chttp == null) {
			chttp = new ConfiguracionHttp();
			chttp.setCasoUso(casoUso);
		}
		return chttp;
	}
	
	public void setVci(List<File> vci) {
		this.vci = vci;
	}
	
	public List<File> getVci() {
		return vci;
	}

	public void setVcm(List<File> vcm) {
		this.vcm = vcm;
	}
	
	public List<File> getVcm() {
		return vcm;
	}
	
	public void setVi(List<File> vi) {
		this.vi = vi;
	}
	
	public List<File> getVi() {
		return vi;
	}
	
	public List<String> getIdAtributo(){
		return idAtributo;
	}

	public void setUploadFileName(List<String> filename) {
        this.filename = filename;
     }
	public List<String> getUploadFileName() {
        return filename;
     }
	
	public void setIdAtributo(List<String> idAtributo){
		this.idAtributo = idAtributo;
	}
	
	public void setChttp(ConfiguracionHttp chttp) {
		this.chttp = chttp;
	}

	public void setIdCU(Integer idCU) {
		this.idCU = idCU;
		this.casoUso = CuBs.consultarCasoUso(idCU);
		this.cbd = ConfiguracionGeneralBs.consultarConfiguracionBaseDatos(casoUso);
		this.chttp = ConfiguracionGeneralBs.consultarConfiguracionHttp(casoUso);

	}
	
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	
	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	
	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public void setListEntradas(List<Atributo> listEntradas) {
		this.listEntradas = listEntradas;
	}
}