package mx.prisma.guionPruebas.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.TipoSeccionEnum;
import mx.prisma.bs.TipoSeccionEnum.TipoSeccionENUM;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.RevisionDAO;
import mx.prisma.editor.dao.SeccionDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.Revision;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.Verbo;
import mx.prisma.generadorPruebas.bs.AnalizadorPasosBs;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.bs.ValorDesconocidoBs;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.guionPruebas.bs.GuionPruebasBs;
import mx.prisma.guionPruebas.bs.ValorEntradaBs;
import mx.prisma.guionPruebas.bs.VerboSinonimoBs;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;

@ResultPath("/content/guionPruebas/")
@Results({
	@Result(name = "pantallaVerbos", type = "dispatcher", location = "verbos.jsp"),
	@Result(name = "pantallaGuionPruebas", type = "redirectAction", params = {
			"actionName", "guion-pruebas!mostrarGuion" }),
	@Result(name = "error_1", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = {
			"actionName", "modulos" }),
	@Result(name = "anterior", type = "redirectAction", params = {
			"actionName", "configuracion-entradas!prepararConfiguracion" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""})})
public class ConfiguracionVerboCtrl extends ActionSupportPRISMA{
	
	private static final long serialVersionUID = 1L;
	
	private Colaborador colaborador;	//Colaborador
	private Proyecto proyecto;			//Proyecto
	private Modulo modulo;				//Módulo
	private Integer idCU;				//Identificador del caso de uso
	private CasoUso casoUso;			//Caso de uso
	private ConfiguracionBaseDatos cbd;
	private ConfiguracionHttp chttp;
	private List<File> file;
	private List<String> filename;
	private List<String> idAtributo;
	private Integer idPaso;
	private Integer idSinonimo;
	//Lista de verbos Otros
	private List<Verbo> otros = new ArrayList<Verbo>();
	private List<Verbo> sinonimos = new ArrayList<Verbo>();
	private Integer esCorrectoVerbo;
	private String sinonimo;
	private String similar;
	private List<String> instruccion = new ArrayList<String>();;
	
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
					//System.out.println("CU");
					return resultado;
				}
				if (modulo == null) {
					resultado = "modulos";
					//System.out.println("M");
					return resultado;
				}
				if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
					resultado = Action.LOGIN;
					//System.out.println("C");
					return resultado;
				}
				
				resultado = "pantallaGuionPruebas";
				
				} catch(Exception e) {
					//System.out.println("Catch");
					ErrorManager.agregaMensajeError(this, e);
					SessionManager.set(this.getActionErrors(), "mensajesError");
					resultado = "anterior";
				}
				return resultado; 
		}
	
	//Función para agregar la instrucción del verbo y redireccionar al guion de prueba
	public String anadirInstruccion(){
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
				//System.out.println("CU");
				return resultado;
			}
			if (modulo == null) {
				resultado = "modulos";
				//System.out.println("M");
				return resultado;
			}
			if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
				resultado = Action.LOGIN;
				//System.out.println("C");
				return resultado;
			}
			
			//Verificamos que haya seleccionado una opción
			if (esCorrectoVerbo != null) {
				//Si el usuario considera que la palabra no es similar a alguna ya registrada
				if (esCorrectoVerbo == 2) { 
					//Si no subió algún archivo
					if(getUpload()==null){
						resultado="error_1";
						addActionMessage(getText("MSG42", new String[] { "No",
								"ingresado", "archivo txt" }));
						SessionManager.set(this.getActionMessages(), "mensajesAccion");
						//SessionManager.set(this.casoUso.getId(), "casoUsoId");
					//Guardamos las entradas en la base de datos
					}else{
						VerboSinonimoBs.agregarInstruccion(getUpload(), getUploadFileName(), getIdPaso(), getIdSinonimo());
						//ValorEntradaBs.crearArchivo(getUpload(),getIdAtributo(),getUploadFileName());
						resultado="pantallaGuionPruebas";
						addActionMessage(getText("MSG41", new String[] { "Los",
								"txt", "registrado" }));
						SessionManager.set(this.getActionMessages(), "mensajesAccion");
					}
				} else {
					resultado="pantallaGuionPruebas";
				}
			} else {
				throw new PRISMAValidacionException(
						"El usuario no ingresó la respuesta", "MSG4", null,
						"esCorrectoResumen");
			}	
			
			} catch(Exception e) {
				ErrorManager.agregaMensajeError(this, e);
				SessionManager.set(this.getActionErrors(), "mensajesError");
				resultado = "anterior";
			}
			return resultado; 
	}
	
	/*
	 * Getters y setters: se hacen para cada una de las variables declaradas al inicio de la clase
	*/
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Integer getIdCU() {
		return idCU;
	}

	public void setIdCU(Integer idCU) {
		this.idCU = idCU;
		this.casoUso = CuBs.consultarCasoUso(idCU);
		this.cbd = ConfiguracionGeneralBs.consultarConfiguracionBaseDatos(casoUso);
		this.chttp = ConfiguracionGeneralBs.consultarConfiguracionHttp(casoUso);

	}

	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
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

	public void setChttp(ConfiguracionHttp chttp) {
		this.chttp = chttp;
	}

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
	
	public List<Verbo> getOtros() {
		return otros;
	}
	
	public void setOtros(List<Verbo> otros) {
		this.otros = otros;
	}
	
	public List<Verbo> getSinonimos() {
		return sinonimos;
	}
	
	public void setSinonimos(List<Verbo> sinonimos) {
		this.sinonimos = sinonimos;
	}
	
	public Integer getEsCorrectoVerbo() {
		return esCorrectoVerbo;
	}
	
	public void setEsCorrectoVerbo(Integer esCorrectoVerbo) {
		this.esCorrectoVerbo = esCorrectoVerbo;
	}
	
	public String getSinonimo() {
		return sinonimo;
	}
	
	public void setSinonimo(String sinonimo) {
		this.sinonimo = sinonimo;
	}
	
	public String getSimilar() {
		return similar;
	}
	
	public void setSimilar(String similar) {
		this.similar = similar;
	}
	
	public List<String> getInstruccion() {
		return instruccion;
	}
	
	public void setInstruccion(List<String> instruccion) {
		this.instruccion = instruccion;
	}
	
	public Integer getIdPaso() {
		return idPaso;
	}
	
	public void setIdPaso(Integer idPaso) {
		this.idPaso = idPaso;
	}
	
	public Integer getIdSinonimo() {
		return idSinonimo;
	}
	
	public void setIdSinonimo(Integer idSinonimo) {
		this.idSinonimo = idSinonimo;
	}
}
