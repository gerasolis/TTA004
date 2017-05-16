package mx.prisma.guionPruebas.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.Hibernate;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.ModuloBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.CasoUsoDAO;
import mx.prisma.editor.dao.ElementoDAO;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.EstadoElemento;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.AnalizadorPasosBs;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.bs.ValorEntradaBs;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.guionPruebas.bs.GuionPruebasBs;
import mx.prisma.guionPruebas.bs.VerboSinonimoBs;
import mx.prisma.guionPruebas.dao.GuionPruebaDAO;
import mx.prisma.guionPruebas.dao.InstruccionDAO;
import mx.prisma.guionPruebas.model.GuionPrueba;
import mx.prisma.guionPruebas.model.Instruccion;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.FileUtil;
import mx.prisma.util.JsonUtil;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.ReportUtil;
import mx.prisma.util.SessionManager;

@ResultPath("/content/guionPruebas/")
@Results({
	@Result(name = "pantallaGuionPruebas", type = "dispatcher", location = "guion.jsp"),
	@Result(name = "gestionarTrayectoriasCU", type = "dispatcher", location = "trayectorias.jsp"),
	@Result(name = "cu", type = "redirectAction", params = { "actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = { "actionName", "modulos" }),
	@Result(name = "anterior", type = "redirectAction", params = {
			"actionName", "configuracion-casos-uso-previos!prepararConfiguracion" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""})
})
//Clase que contrala las acciones para la generación del guión de pruebas
public class GuionPruebasCtrl extends ActionSupportPRISMA{
	
	//Declaración de variables
	private static final long serialVersionUID = 1L;
	private Colaborador colaborador;	//Colaborador
	private Proyecto proyecto;			//Proyecto
	private Modulo modulo;				//Módulo
	private Integer idCU;				//Identificador del caso de uso
	private CasoUso casoUso;			//Caso de uso
	private ConfiguracionBaseDatos cbd;
	private ConfiguracionHttp chttp;
	private Proyecto model;
	private String jsonPasosTabla;
	
	//Variable del guion
	List<String> instrucciones = new ArrayList<String>();
	
	//Lista de los casos de uso 
	List<CasoUso> casosUso = new ArrayList<CasoUso>();
	
	//Lista de lista de las trayectorias por caso de uso 
	List<List<Trayectoria>> trayectorias = new ArrayList<List<Trayectoria>>();
	
	//Trayectorias seleccionadas
	List<String> valorSel;
	
	//Variables para el documento
	private InputStream fileInputStream;
	private String type;
    private String filename; 
    private String extension;
    
    private String jsonGuionesTabla;
    
    private int idProyecto;
	
	//Función que muestra la lista de los CU 
	@SuppressWarnings("unchecked")
	public String configuracionTrayectoriasCasosUso()throws Exception {
		//System.out.println("Entramos al guion");
		Map<String, Object> session = null;

		String resultado;
		colaborador = SessionManager.consultarColaboradorActivo();	//Consultar al colaborador actual
		proyecto = SessionManager.consultarProyectoActivo();		//Consultar el proyecto actual
		modulo = SessionManager.consultarModuloActivo();			//Consultar el módulo actual
		
		//Si el módulo es nulo
		if (modulo == null) {
			//Regresa al listado de los módulos
			resultado = "modulos";
			return resultado;
		}
		
		
		//Obtenemos la lista de los casos de uso  
		CasoUsoDAO cudao = new CasoUsoDAO();
		casosUso = cudao.consultarCasosUso(modulo.getId());
		
		Modulo moduloAux = new Modulo();
		moduloAux.setId(modulo.getId());
		moduloAux.setNombre(modulo.getNombre());
		moduloAux.setClave(modulo.getClave());
		moduloAux.setDescripcion(modulo.getDescripcion());
		
		ArrayList<CasoUso> cusTabla = new ArrayList<CasoUso>();
		CasoUso cuAux;
		List<CasoUso>casosUso = CuBs.consultarCasosUso(proyecto);
		for (int i=0; i<casosUso.size(); i++) {
			cuAux = new CasoUso();
			cuAux.setId(casosUso.get(i).getId());
			cuAux.setClave(casosUso.get(i).getClave());
			cuAux.setNumero(casosUso.get(i).getNumero());
			cuAux.setNombre(casosUso.get(i).getNombre());
			cuAux.setModulo(moduloAux);
			Set<Trayectoria> trayectorias = casosUso.get(i).getTrayectorias();
			List<Trayectoria> list = new ArrayList<Trayectoria>(trayectorias);
			trayectorias = new HashSet<Trayectoria>();
			Trayectoria tAux;
			for(int j=0; j<list.size(); j++){
				tAux = new Trayectoria();
				tAux.setId(list.get(j).getId());
				tAux.setClave(list.get(j).getClave());
				trayectorias.add(tAux);
			}
			cuAux.setTrayectorias(trayectorias);
			cusTabla.add(cuAux);
		}

		this.jsonPasosTabla = JsonUtil.mapListToJSON(cusTabla);
		
		System.out.println(jsonPasosTabla);
		
		//Obtenemos la lista de las trayectorias de los casos de uso 
		for(int i=0; i<casosUso.size(); i++){
			List<Trayectoria> t = new ArrayList<Trayectoria>();
			Set<Trayectoria> tray = casosUso.get(i).getTrayectorias();
		
			for(Trayectoria tr : tray){
				if(tr.isAlternativa()){ //Solo se agregan las trayectorias alternativas, la principal es forzosa
					t.add(tr);
				}
			}
			trayectorias.add(t);
		}
			
		//Si existen mensajes, se eliminan para que no aparezcan en la nueva pantalla
		@SuppressWarnings("unchecked")
		Collection<String> msjs = (Collection<String>) SessionManager.get("mensajesAccion");
		this.setActionMessages(msjs);
		SessionManager.delete("mensajesAccion");

		@SuppressWarnings("unchecked")
		Collection<String> msjsError = (Collection<String>) SessionManager.get("mensajesError");
		this.setActionErrors(msjsError);
		SessionManager.delete("mensajesError");
		
		return "gestionarTrayectoriasCU";
	}
	
	//Función para mostrar la pantalla del guión de prueba
	public String mostrarGuion() throws Exception {
		//System.out.println("Entramos al guion");
		Map<String, Object> session = null;

		String resultado;
		colaborador = SessionManager.consultarColaboradorActivo();	//Consultar al colaborador actual
		proyecto = SessionManager.consultarProyectoActivo();		//Consultar el proyecto actual
		modulo = SessionManager.consultarModuloActivo();			//Consultar el módulo actual
		casoUso = SessionManager.consultarCasoUsoActivo();			//Consultar el caso de uso actual
	
		//Si el caso de uso es nulo
		if (casoUso == null) {
			//Obtiene el valor de idCU que se mandó como parámetro
			session = ActionContext.getContext().getSession();
			session.put("idCU", idCU);
			//Consulta el caso de uso
			casoUso = SessionManager.consultarCasoUsoActivo();
		}

		//Si el caso de uso sigue siendo nulo
		if (casoUso == null) {
			//Regresa al listado de cu
			resultado = "cu";
			return resultado;
		}
		//Si el módulo es nulo
		if (modulo == null) {
			//Regresa al listado de los módulos
			resultado = "modulos";
			return resultado;
		}
		if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
			resultado = Action.LOGIN;
			return resultado;
		}
		List<Trayectoria> trayectorias = new ArrayList<Trayectoria>();
		
		for(Trayectoria t : casoUso.getTrayectorias()){
			trayectorias.add(t);
		}
		
		//Agregamos las instrucciones 
		instrucciones = GuionPruebasBs.obtenerInstrucciones(casoUso, trayectorias, request.getContextPath());
			
		//Si existen mensajes, se eliminan para que no aparezcan en la nueva pantalla
		@SuppressWarnings("unchecked")
		Collection<String> msjs = (Collection<String>) SessionManager.get("mensajesAccion");
		this.setActionMessages(msjs);
		SessionManager.delete("mensajesAccion");

		@SuppressWarnings("unchecked")
		Collection<String> msjsError = (Collection<String>) SessionManager.get("mensajesError");
		this.setActionErrors(msjsError);
		SessionManager.delete("mensajesError");
		
		//Retorna la pantalla a mostrar
		return "pantallaGuionPruebas";
	}
	
	//Función para saber si es una instrucción o una pregunta
	public static boolean esInstruccion(String instruccion){
		if((instruccion.charAt(0)=='¿') && (instruccion.charAt(instruccion.length()-1)=='?')){
			return false;
		}else{
			return true;
		}
	}
	
	public String seleccionarGuiones() throws Exception{
		Map<String, Object> session = null;

		String resultado;
		colaborador = SessionManager.consultarColaboradorActivo();	//Consultar al colaborador actual
		proyecto = SessionManager.consultarProyectoActivo();		//Consultar el proyecto actual
		modulo = SessionManager.consultarModuloActivo();			//Consultar el módulo actual
		
		//Si el módulo es nulo
		if (modulo == null) {
			//Regresa al listado de los módulos
			resultado = "modulos";
			return resultado;
		}
		if (!AccessBs.verificarPermisos(modulo.getProyecto(), colaborador)) {
			resultado = Action.LOGIN;
			return resultado;
		}
		
		Set<CasoUso> cuSeleccionados = new HashSet<CasoUso>(0);
		
		//Obtenemos los guiones de prueba selecionados
		if (jsonPasosTabla != null
				&& !jsonPasosTabla.equals("")) {
			System.out.println("Entró al if de  seleccionarGuion");
			cuSeleccionados = JsonUtil.mapJSONToSet(
					jsonPasosTabla, CasoUso.class);
			System.out.println("JSON: "+jsonPasosTabla);
		}else{
			System.out.println("JSON vacío");
		}
		
		//Ordenar el arreglo 
		ArrayList<CasoUso> casosUsos = (ArrayList<CasoUso>) JsonUtil.mapJSONToArrayList(
				jsonPasosTabla, CasoUso.class);
		
		GuionPruebaDAO gpdao = new GuionPruebaDAO();
		List<GuionPrueba> todosgp = gpdao.selectAll();
		
		int j=0;
		for(CasoUso cuSeleccionado : casosUsos){
			System.out.println("CU SELECCIONADO: "+cuSeleccionado.getNombre());
			j++;
			for(GuionPrueba gp : todosgp){
				if(gp.getCasoUsoElementoid()==cuSeleccionado.getId()){
					gp.setOrden(j);
					gpdao = new GuionPruebaDAO();
					gpdao.update(gp);
				}
			}			
		}
		
		//Creamos el guion de prueba para el cu seleccionado
		for(CasoUso cuSeleccionado : cuSeleccionados){
			List<Trayectoria> trayectoriasSeleccionadas = new ArrayList<Trayectoria>();
			//Obtenemos las trayectorias seleccionadas del caso de uso
				CasoUsoDAO cudao = new CasoUsoDAO();
				cuSeleccionado = cudao.consultarCasoUso(cuSeleccionado.getId());
				for(int i=0; i<valorSel.size(); i++){
					TrayectoriaDAO tdao = new TrayectoriaDAO();
					Trayectoria t = tdao.consultarTrayectoria(Integer.parseInt(valorSel.get(i)));
					//Consultamos las trayectorias del caso de uso 
					for(Trayectoria trayCU : cuSeleccionado.getTrayectorias()){
						if(trayCU.getId()==t.getId()){
							System.out.println("Caso de Uso: "+cuSeleccionado.getNombre());
							System.out.println("Trayectoria: "+t.getClave());
							trayectoriasSeleccionadas.add(t);
						}
					}
				}
				//Debemos enviarle también las trayectorias seleccionadas
				GuionPruebasBs.obtenerInstrucciones(cuSeleccionado, trayectoriasSeleccionadas, request.getContextPath());
		}
		
		for(GuionPrueba gp : todosgp){
			for(CasoUso cuSeleccionado : cuSeleccionados){
				if(gp.getCasoUsoElementoid()==cuSeleccionado.getId()){
					//Cambiamos el estado de seleccionado a 1
					gp.setSeleccionado(true);
				}else{
					//Cambiamos el estado de seleccionado a 0
					gp.setSeleccionado(false);
				}
				if(gp.getSeleccionado()==true){
					break;
				}
			}
			gpdao = new GuionPruebaDAO();
			gpdao.update(gp);
		}
		
		return descargarGuion(idProyecto);
	}
	
	public String descargarGuion(int idProyecto) {
		//String extension = "docx";
		@SuppressWarnings("deprecation")
		String rutaSrc = request.getRealPath("/") + "/resources/JasperReport/";
		@SuppressWarnings("deprecation")
		String rutaTarget = request.getRealPath("/") + "/resources/JasperReport/";
		
		//if(extension.equals("pdf")) {
			filename = "GuionesPrueba.pdf";
			type = "application/pdf";
		/*} else if(extension.equals("docx")) {
			filename = this.model.getNombre().replace(' ', '_') + "." + extension;
			type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		} else {
			filename = this.model.getNombre().replace(' ', '_') + ".pdf";
			type = "application/pdf";
		}*/
				
		try {
				System.out.println("ENTRA AL TRY DE DESCARGAR");
				ReportUtil.crearGuion("pdf", filename, idProyecto, rutaSrc, rutaTarget);
				System.out.println("DESPUÉS DEL REPORT..TUL");
		        File doc = new File(rutaTarget + filename);
		        System.out.println("CREA FILE DOC");
		        this.fileInputStream = new FileInputStream(doc);
		        System.out.println("FILEINPUTSTRAM");
		        FileUtil.delete(doc);
	        } catch (Exception e) {
	        	System.out.println(e);
	        	ErrorManager.agregaMensajeError(this, e);
	        	try {
					return configuracionTrayectoriasCasosUso();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println(e);
					e1.printStackTrace();
				}
	        }
			
	    return "documento";
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

	public void setInstrucciones(List<String> instrucciones){
		this.instrucciones = instrucciones;
	}
	
	public List<String> getInstrucciones(){
		return instrucciones;
	}
	
	public List<CasoUso> getCasosUso() {
		return casosUso;
	}
	
	public void setCasosUso(List<CasoUso> casosUso) {
		this.casosUso = casosUso;
	}
	
	public List<List<Trayectoria>> getTrayectorias() {
		return trayectorias;
	}
	
	public void setTrayectorias(List<List<Trayectoria>> trayectorias) {
		this.trayectorias = trayectorias;
	}
	
	public List<String> getValorSel() {
		return valorSel;
	}
	
	public void setValorSel(List<String> valorSel) {
		this.valorSel = valorSel;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getJsonGuionesTabla() {
		return jsonGuionesTabla;
	}
	
	public void setJsonGuionesTabla(String jsonGuionesTabla) {
		this.jsonGuionesTabla = jsonGuionesTabla;
	}
	
	public int getIdProyecto() {
		return idProyecto;
	}
	
	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}
	
	public String getJsonPasosTabla() {
		return jsonPasosTabla;
	}
	
	public void setJsonPasosTabla(String jsonPasosTabla) {
		this.jsonPasosTabla = jsonPasosTabla;
	}
	
}
