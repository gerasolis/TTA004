package mx.prisma.gestorEntradas.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.bs.CuPruebasBs;
import mx.prisma.generadorPruebas.bs.ValorDesconocidoBs;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.guionPruebas.bs.ValorEntradaBs;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.JsonUtil;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.SessionManager;
import net.sf.json.JSONObject;

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
	private List<String> entradas;
	private List<File> vci;
	private List<File> vi;
	private List<File> file;
	private List<String> idAtributo;
	private List<String> filename;
	private List<Entrada> listaIncidencias = new ArrayList<Entrada>();
	private List<String> checkbox;
	private List<String> checkbox2;
	private List<String> checkbox3;
	private List<String> checkbox4;
	private String jsonEntradasTabla;
	private List<JSONObject> listJson = new ArrayList<JSONObject>();
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
			System.out.println("antes de tronar");
			//jsonEntradasTabla = JsonUtil.mapListToJSON(listEntradas); 
		
			for(Atributo a : listEntradas){
			  JSONObject obj = new JSONObject();
			  obj.put("id", a.getId());
			  obj.put("nombre", a.getNombre());
		      System.out.println(obj);
		      listJson.add(obj);
			}
			System.out.println(listJson);
			jsonEntradasTabla = listJson.toString();

		      
			//System.out.println("jsonEntradasTabla: "+jsonEntradasTabla);
			Set<ReglaNegocio> reglas = new HashSet<ReglaNegocio>(0);
			
			for(CasoUsoReglaNegocio curn : casoUso.getReglas()) {
				reglas.add(curn.getReglaNegocio());
			}

			//Vamos a generar los valores aleatorios correctos, para los que no se puedan, por la lista de incidencias,
			//poner que no se puede en front.
			listaIncidencias=CuPruebasBs.generarValores(casoUso.getEntradas(), reglas);
			
			for(Entrada e : listaIncidencias){
				System.out.println(e.getAtributo().getNombre());
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
				System.out.println("famoso JSON: "+jsonEntradasTabla);
				//Guardamos los valores correctos (insertar) los que se insertan por txt
				/*if(getVci()==null){
					resultado="error_1";
					

				}else{
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),getCorrecto_prueba(),getCorrecto_guion(),2,jsonEntradasTabla,entradas);
					resultado="cu";
					addActionMessage(getText("MSG41", new String[] { "Los",
							"txt", "valores correctos", "registrado", casoUso.getClave()+casoUso.getNumero()+" "+casoUso.getNombre()}));
					SessionManager.set(this.getActionMessages(), "mensajesAccion");
				}*/
				
				ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),2,jsonEntradasTabla);
				resultado="cu";
				addActionMessage(getText("MSG41", new String[] { "Los",
						"txt", "valores correctos", "registrado", casoUso.getClave()+casoUso.getNumero()+" "+casoUso.getNombre()}));
				SessionManager.set(this.getActionMessages(), "mensajesAccion");
				
				
				
				/*if(getAleatorioCorrecto_prueba()!=null || getAleatorioCorrecto_guion()!=null){
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),getAleatorioCorrecto_prueba(),getAleatorioCorrecto_guion(),3,jsonEntradasTabla,entradas);
				}
				if(getAleatorioIncorrecto_prueba()!=null || getAleatorioIncorrecto_guion()!=null){
					ValorEntradaBs.guardarValores(getVci(),getIdAtributo(),getUploadFileName(),getAleatorioIncorrecto_prueba(),getAleatorioIncorrecto_guion(),4,jsonEntradasTabla,entradas);
				}*/

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

	public void setVi(List<File> vi) {
		this.vi = vi;
	}
	
	public List<File> getVi() {
		return vi;
	}
	
	public List<String> getIdAtributo(){
		return idAtributo;
	}
	
	public List<String> getIdAtributo_nogenerables(){
		return idAtributo;
	}
	public void setIdAtributo_nogenerables(List<String> idAtributo){
		this.idAtributo = idAtributo;
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
	
	public void setEntradas(List<String> entradas){
		this.entradas = entradas;
	}
	public List<String> getEntradas() {
        return entradas;
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
	
	public List<Entrada> getListaIncidencias(){
		return listaIncidencias;
	}
	public void setListaIncidencias(List<Entrada> listaIncidencias){
		this.listaIncidencias = listaIncidencias;
	}
	public void setVinc(List<File> file) {
		this.file = file;
	}
	public List<File> getVinc() {
		return file;
	}
	public void setAleatorioCorrecto_prueba(List<String> checkbox) {
		this.checkbox = checkbox;
	}
	public List<String> getAleatorioCorrecto_prueba() {
		return checkbox;
	}
	public void setAleatorioCorrecto_guion(List<String> checkbox2) {
		this.checkbox2 = checkbox2;
	}
	public List<String> getAleatorioCorrecto_guion() {
		return checkbox2;
	}
	public void setCorrecto_prueba(List<String> checkbox3) {
		this.checkbox3 = checkbox3;
	}
	public List<String> getCorrecto_prueba() {
		return checkbox3;
	}
	public void setCorrecto_guion(List<String> checkbox4) {
		this.checkbox4 = checkbox4;
	}
	public List<String> getCorrecto_guion() {
		return checkbox4;
	}
	public void setAleatorioIncorrecto_prueba(List<String> checkbox) {
		this.checkbox = checkbox;
	}
	public List<String> getAleatorioIncorrecto_prueba() {
		return checkbox;
	}
	public void setAleatorioIncorrecto_guion(List<String> checkbox2) {
		this.checkbox2 = checkbox2;
	}
	public List<String> getAleatorioIncorrecto_guion() {
		return checkbox2;
	}
	
	public String getJsonEntradasTabla() {
		return jsonEntradasTabla;
	}

	public void setJsonEntradasTabla(String jsonEntradasTabla) {
		this.jsonEntradasTabla = jsonEntradasTabla;
	}
	public List<JSONObject> getListJson() {
		return listJson;
	}

	public void setListJson(List<JSONObject> listJson) {
		this.listJson = listJson;
	}
	
}