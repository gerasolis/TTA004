package mx.prisma.guionPruebas.controller;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.AnalizadorPasosBs;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.bs.ValorEntradaBs;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.guionPruebas.bs.GuionPruebasBs;
import mx.prisma.guionPruebas.bs.VerboSinonimoBs;
import mx.prisma.guionPruebas.dao.InstruccionDesconocidaDAO;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.SessionManager;

@ResultPath("/content/guionPruebas/")
@Results({
	@Result(name = "pantallaGuionPruebas", type = "dispatcher", location = "guion.jsp"),
	@Result(name = "cu", type = "redirectAction", params = { "actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = { "actionName", "modulos" }),
	@Result(name = "anterior", type = "redirectAction", params = {
			"actionName", "configuracion-casos-uso-previos!prepararConfiguracion" })})
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
	
	//Variable del guion
	List<String> instrucciones = new ArrayList<String>();
	
	//Función para mostrar la pantalla del guión de prueba
	public String mostrarGuion() throws Exception {
		System.out.println("Entramos al guion");
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
		
		/**CREAMOS EL GUIÓN**/
		//Consultamos la trayectoria principal
		Trayectoria trayectoriaPrincipal = GuionPruebasBs.trayectoriaPrincipal(casoUso);
		//Consultamos los pasos de la trayectoria principal
		List<Paso> pasos = TrayectoriaBs.obtenerPasos_(trayectoriaPrincipal.getId());
		
		// Lista de las instrucciones
				
				// Obtenemos el total de pasos de la trayectora principal
				int tpasos = pasos.size();
				// Consultamos las entradas del caso de uso
				Set<Entrada> entradas = casoUso.getEntradas();
				
				//Agregamos la instrucción la url del CU (Ingrese a la siguiente url ...)
				//Pantalla pantalla = CasoUsoPantallaDAO.
				//instrucciones.add("Ingrese a la siguiente url: ");
				
				List<String> rnRevisadas = new ArrayList<String>();

				for (int i = 0; i < tpasos; i++) {
					// Consultamos el paso actual
					Paso paso = pasos.get(i);
					
					System.out.println(paso.getNumero()+": "+paso.getRedaccion());

					// Obtenemos los tokens del paso
					List<String> tokens = GuionPruebasBs.obtenerTokens(paso);

					// Comparación de los tokens
					for (String token : tokens) {
						
						//Si es una instrucción que ya se registró porque era desconocida
						
						if(paso.getOtroVerbo()!=null){
							int idSinonimo = VerboSinonimoBs.sinonimos(paso.getOtroVerbo()).getIdSinonimo();
							InstruccionDesconocidaDAO iddao = new InstruccionDesconocidaDAO();
							instrucciones.add(iddao.consultarInstruccionDesconocida(paso.getId(), idSinonimo).getInstruccion());
						}
						// Si el actor es el USUARIO
						else if (paso.isRealizaActor()) {
							String comparacion = GuionPruebasBs.compararTokenUsuario(request.getContextPath(), paso, token, entradas);
							if (!comparacion.equals(""))
								instrucciones.add(comparacion);
						}
						// Si el actor es el SISTEMA
						else {
							List<String> comparacion = GuionPruebasBs.compararTokenSistema(request.getContextPath(), paso, token, casoUso);
							/*//Si el token es una RN y no se ha revisado, se agrega a la lista de RN revisadas
							if(token.contains(TokenBs.tokenRN) && !rnRevisadas.contains(token)){
								rnRevisadas.add(token);
							}
							else if(rnRevisadas.contains(token)){
								break;
							}
							//Si el resultado de la comparación del token es un paso 
							if(comparacion.contains(TokenBs.tokenP)){
								Paso pasoC = (Paso) TokenBs.obtenerTokenObjeto(" "+comparacion);
								i = pasoC.getNumero()-1;
							}
							//Si el resultado de la comparación es una intrucción 
							else */if (!comparacion.isEmpty()){
								for(String instr : comparacion)
									instrucciones.add(instr);
							}
						}
					}
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
}
