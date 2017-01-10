package mx.prisma.generadorPruebas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.bs.AccionBs;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.EntradaBs;
import mx.prisma.editor.bs.MensajeParametroBs;
import mx.prisma.editor.bs.PantallaBs;
import mx.prisma.editor.bs.ReferenciaParametroBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.TrayectoriaDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Parametro;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.CuPruebasBs;
import mx.prisma.generadorPruebas.bs.GeneradorPruebasBs;
import mx.prisma.generadorPruebas.bs.QueryBs;
import mx.prisma.generadorPruebas.bs.ValorAccionTrayectoriaBs;
import mx.prisma.generadorPruebas.bs.ValorEntradaBs;
import mx.prisma.generadorPruebas.bs.ValorEntradaTrayectoriaBs;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroBs;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroTrayectoriaBs;
import mx.prisma.generadorPruebas.bs.ValorPantallaTrayectoriaBs;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroDAO;
import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorAccionTrayectoria;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametroTrayectoria;
import mx.prisma.generadorPruebas.model.ValorPantallaTrayectoria;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.FileUtil;
import mx.prisma.util.ImageConverterUtil;
import mx.prisma.util.JsonUtil;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

@ResultPath("/content/generadorPruebas/")
@Results({
	@Result(name = "pantallaConfiguracionTrayectoria", type = "dispatcher", location = "configuracion/trayectoria.jsp"),
	@Result(name = "desconocido", type = "dispatcher", location = "configuracion/desconocido.jsp"),
	/*@Result(name = "desconocido", type = "redirectAction", params = {
			"actionName", "configuracion-desconocidos!prepararConfiguracion" }),*/
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

public class ConfiguracionTrayectoriaCtrl extends ActionSupportPRISMA {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Colaborador colaborador;
	private Trayectoria previo;
	private Proyecto proyecto;
	private Modulo modulo;
	private Integer idCU;
	private CasoUso casoUso;
	private String jsonAcciones;
	private String jsonReferenciasReglasNegocio;
	private String jsonEntradas;
	private String jsonReferenciasParametrosMensaje;
	private String jsonPantallas;
	private String jsonImagenesPantallasAcciones;
	private Integer idTrayectoria;
	private Trayectoria condicionTrayectoria;
	private Trayectoria id;
	private Trayectoria trayectoria;
	private List<Entrada> listaIncidencias = new ArrayList<Entrada>();
	private List<Atributo>listaAtributo = new ArrayList<Atributo>();
	private InputStream fileInputStream;
	private String type;
    private String filename;
	
	public String prepararConfiguracion() {
        Map<String, Object> session = null;
		String resultado;
			
		try {
			System.out.println("Entra a configuracionTrayectoriaCtrl");
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();
			trayectoria = SessionManager.consultarTrayectoriaActual();
			setTrayectoria(trayectoria);

			if (trayectoria == null) {
				System.out.println("Entro al null de trayectoria");

				session = ActionContext.getContext().getSession();
				session.put("idTrayectoria", idTrayectoria);
				trayectoria = SessionManager.consultarTrayectoriaActual();
				setTrayectoria(trayectoria);
				}
			
			Set<ReglaNegocio> reglas = new HashSet<ReglaNegocio>(0);
			for(CasoUsoReglaNegocio curn : casoUso.getReglas()) {
				reglas.add(curn.getReglaNegocio());
			}
			
			List<Trayectoria> trayectorias=null;
			trayectorias=ValorEntradaTrayectoriaBs.consultarEstado(trayectoria);
		
				//if(trayectoria.getEstado().equals("Configurado")){
					
				listaIncidencias.clear();		
				//}else if(trayectoria.getEstado().equals("No Configurado") ){
					if(trayectoria.isAlternativa()){
						System.out.println("Entra a GENERARVALORESTRAYECTORIA");
						CuPruebasBs.generarValoresTrayectorias(casoUso.getEntradas(),reglas, trayectoria);
						listaIncidencias.clear();
						System.out.println(listaIncidencias);
					}
					else{
						System.out.println("Entra a GENERARVALORES");					
						listaIncidencias=CuPruebasBs.generarValores(casoUso.getEntradas(), reglas);
						System.out.println("listaIncidencias: "+listaIncidencias);
					}
				//}
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
			
			
			//Se arman los json con los campos que se mostrarán en pantalla
			
			
			if(!listaIncidencias.isEmpty()){
				System.out.println("Entra al if de listaIncidencias");
				resultado="desconocido";
				
				for(Entrada entrada: listaIncidencias){
					System.out.println("Nombre del atributo: "+entrada.getAtributo().getNombre());
					listaAtributo.add(entrada.getAtributo());
				}
			}
			else{	
				if(jsonEntradas == null || jsonEntradas.isEmpty()) {
				obtenerJsonCamposEntradas(casoUso, trayectoria);
				}
				if(jsonAcciones == null || jsonAcciones.isEmpty()) {//¿SE LLENARÁN ESTOS VALORES MANUALMENTE?
					obtenerJsonCamposAcciones(trayectoria);
				}
				
				if(jsonReferenciasReglasNegocio == null || jsonReferenciasReglasNegocio.isEmpty()) {
					obtenerJsonCamposReglasNegocio(trayectoria);
				}
				
				if(jsonReferenciasParametrosMensaje == null || jsonReferenciasParametrosMensaje.isEmpty()) {
					obtenerJsonCamposParametrosMensaje(trayectoria);
				}
				
				if(jsonPantallas == null || jsonPantallas.isEmpty()) {
					obtenerJsonCamposPantallas(trayectoria);
				}
				resultado = "pantallaConfiguracionTrayectoria";
			}	
				@SuppressWarnings("unchecked")
				Collection<String> msjs = (Collection<String>) SessionManager
						.get("mensajesAccion");
				this.setActionMessages(msjs);
				SessionManager.delete("mensajesAccion");
				
				@SuppressWarnings("unchecked")
				Collection<String> msjsError = (Collection<String>) SessionManager
						.get("mensajesError");
				this.setActionErrors(msjsError);
				SessionManager.delete("mensajesError");
			} catch(Exception e) {
				ErrorManager.agregaMensajeError(this, e);
				SessionManager.set(this.getActionErrors(), "mensajesError");
				resultado = "anterior";
			}
			System.out.println("resultado: "+resultado);
			return resultado; 
	}
	
	public String cargarConfiguracion() {
		  Map<String, Object> session = null;
			String resultado;
			listaIncidencias = null;	
			try {
				System.out.println("Entra a configuracionTrayectoriaCtrl");
				colaborador = SessionManager.consultarColaboradorActivo();
				proyecto = SessionManager.consultarProyectoActivo();
				modulo = SessionManager.consultarModuloActivo();
				casoUso = SessionManager.consultarCasoUsoActivo();
				trayectoria = SessionManager.consultarTrayectoriaActual();
				setTrayectoria(trayectoria);

				if (trayectoria == null) {
					System.out.println("Entro al null de trayectoria");

					session = ActionContext.getContext().getSession();
					session.put("idTrayectoria", idTrayectoria);
					trayectoria = SessionManager.consultarTrayectoriaActual();
					setTrayectoria(trayectoria);
					}
				
				Set<ReglaNegocio> reglas = new HashSet<ReglaNegocio>(0);
				for(CasoUsoReglaNegocio curn : casoUso.getReglas()) {
					reglas.add(curn.getReglaNegocio());
				}
				
				List<Trayectoria> trayectorias=null;
				trayectorias=ValorEntradaTrayectoriaBs.consultarEstado(trayectoria);
			
					if(trayectoria.getEstado().equals("Configurado")){
						
					}else if(trayectoria.getEstado().equals("No Configurado") ){
						if(trayectoria.isAlternativa()){
							System.out.println("Entra a GENERARVALORESTRAYECTORIA");
							CuPruebasBs.generarValoresTrayectorias(casoUso.getEntradas(),reglas, trayectoria);
						}
						else{
							System.out.println("Entra a GENERARVALORES");					
							listaIncidencias=CuPruebasBs.generarValores(casoUso.getEntradas(), reglas);
						}
					}
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
				
				
				//Se arman los json con los campos que se mostrarán en pantalla
				
				/*if(jsonEntradas == null || jsonEntradas.isEmpty()) {
					obtenerJsonCamposEntradas(casoUso, trayectoria);
				}*/		
				if(jsonAcciones == null || jsonAcciones.isEmpty()) {//¿SE LLENARÁN ESTOS VALORES MANUALMENTE?
					obtenerJsonCamposAcciones(trayectoria);
				}
					
				if(jsonReferenciasReglasNegocio == null || jsonReferenciasReglasNegocio.isEmpty()) {
					obtenerJsonCamposReglasNegocio(trayectoria);
				}
					
				if(jsonReferenciasParametrosMensaje == null || jsonReferenciasParametrosMensaje.isEmpty()) {
					obtenerJsonCamposParametrosMensaje(trayectoria);
				}
					
				if(jsonPantallas == null || jsonPantallas.isEmpty()) {
					obtenerJsonCamposPantallas(trayectoria);
				}
				resultado = "pantallaConfiguracionTrayectoria";
					
				@SuppressWarnings("unchecked")
				Collection<String> msjs = (Collection<String>) SessionManager
						.get("mensajesAccion");
				this.setActionMessages(msjs);
				SessionManager.delete("mensajesAccion");
					
				@SuppressWarnings("unchecked")
				Collection<String> msjsError = (Collection<String>) SessionManager
						.get("mensajesError");
				this.setActionErrors(msjsError);
				SessionManager.delete("mensajesError");
			} catch(Exception e) {
				ErrorManager.agregaMensajeError(this, e);
				SessionManager.set(this.getActionErrors(), "mensajesError");
				resultado = "anterior";
			}
			System.out.println("resultado: "+resultado);
			return resultado; 
	}
	
	public String configurar() {
		String resultado;
		try {
			ValorEntradaTrayectoriaBs.modificarEntradas(trayectoria, true, jsonEntradas);
			modificarAcciones(trayectoria, true);
			modificarReferenciasReglasNegocio(true);
			System.out.println("Antes de modificar Parametros");

			modificarReferenciasParametrosMensaje(trayectoria ,true);
			modificarPantallas(trayectoria, true);
			
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				throw new Exception();
			}
			ElementoBs.modificarEstadoElemento(casoUso, Estado.CONFIGURADO);

			addActionMessage(getText("MSG1", new String[] { "La", "Prueba",
			"configurada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
			resultado = "cu";
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = prepararConfiguracion();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
		return resultado;
	}
	
	public String guardar() {
		String resultado;
		try {	
			casoUso = SessionManager.consultarCasoUsoActivo();
			trayectoria = SessionManager.consultarTrayectoriaActual();

			if (trayectoria == null) {
				System.out.println("Trayectoria es nulo");

			}
			System.out.println("Entra a método guardar");
			ValorEntradaTrayectoriaBs.modificarEntradas(trayectoria, false, jsonEntradas);
			modificarAcciones(trayectoria, false);
			modificarReferenciasReglasNegocio(false);
			System.out.println("Antes de modificar Parametros");

			modificarReferenciasParametrosMensaje(trayectoria ,false);
			System.out.println("Despues de modificar Parametros");

			modificarPantallas(trayectoria, false);
			

			//	resultado = "cu";
			//	throw new Exception();
			//}
			System.out.println("Trayectoria de guardar: " + trayectoria.getCondicion());
			modificarEstado(trayectoria);
			addActionMessage(getText("MSG1", new String[] { "La", "Configuración del Caso de Uso",
			"guardada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
			resultado = cargarConfiguracion();
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = prepararConfiguracion();
		} catch (PRISMAException pe) {
			ErrorManager.agregaMensajeError(this, pe);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
		return resultado;
	}

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
		
		SessionManager.delete("rutaPruebaGenerada");
		SessionManager.delete("idCUPruebaGenerada");
		SessionManager.delete("pruebaGenerada");
		
		filename = casoUso.getClave() + casoUso.getNumero() + "_" + casoUso.getNombre().replace(" ", "_") + ".zip";

		type = "application/zip";
		
		String rutaFolder = ruta + casoUso.getId() + "/";

				
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
	}
	private void modificarEstado(Trayectoria trayectoria) throws Exception {
		
		trayectoria.setEstado("Configurado");
		TrayectoriaBs.modificarEstado(trayectoria);
		
		
	}
	/*private void modificarEntradas(Trayectoria trayectoria, boolean validarObligatorios) throws Exception {
		if (jsonEntradas != null && !jsonEntradas.equals("")) {
			System.out.println("Entro a Modificar Entrada");
			List<Entrada> entradasVista = JsonUtil.mapJSONToArrayList(this.jsonEntradas, Entrada.class);
			System.out.println("JsonEntradas de modificarEntradas :" + jsonEntradas);
			//System.out.println("entradasVista primer objeto :" + entradasVista.get(0));

			for(Entrada entradaVista : entradasVista) {
				Entrada entradaBD = EntradaBs.consultarEntrada(entradaVista.getId());
				//ValorEntrada nuevo = ValorEntradaBs.consultarValorValido(entradaVista);
				//System.out.println("entradasVista primer valor :" + nuevo.getValor());

				entradaBD.setNombreHTML(entradaVista.getNombreHTML());
				System.out.println("Es alternativa: " + trayectoria.isAlternativa());
				if(!trayectoria.isAlternativa()){
					Set<ValorEntradaTrayectoria> valores = new HashSet<ValorEntradaTrayectoria>(0);

					for(ValorEntradaTrayectoria veVista : entradaVista.getValoresEntradaTrayectoria()) {
						System.out.println("Entro con constructor de EntradaTrayectoria");
						ValorEntradaTrayectoria veValido = null;
						//Se agregan los valores que ya tenía asociada la entrada
						for(ValorEntradaTrayectoria valorBD : entradaBD.getValoresEntradaTrayectoria()) {
							if(valorBD.getId() != veVista.getId()) {
								valores.add(valorBD);
							}
							
							if(valorBD.getId().equals(veVista.getId())){
								veValido = valorBD;
							}
						}
						
						if(veValido != null) {
							veValido.setValor(veVista.getValor());
							valores.add(veValido);
						} else {
							veVista.setId(null);
							veVista.setValido(true);
							veVista.setEntrada(entradaBD);
							veVista.setTrayectoria(trayectoria);
							valores.add(veVista);
						}
					}
					System.out.println("Termino los set");

					entradaBD.getValoresEntradaTrayectoria().clear();
					entradaBD.getValoresEntradaTrayectoria().addAll(valores);
					
					EntradaBs.modificarEntrada(entradaBD, validarObligatorios);
					System.out.println("Modifico Entrada PRINCIPAL");
				}
				else{
					
					Set<ValorEntradaTrayectoria> valores = new HashSet<ValorEntradaTrayectoria>(0);
					Set<ValorEntradaTrayectoria> valores2 = new HashSet<ValorEntradaTrayectoria>(0);
					ValorEntradaTrayectoria valorTrayectoria = new ValorEntradaTrayectoria();
					if(entradaVista.getValoresEntradaTrayectoria() == null){
						//Qué hacer si no encuentra registros anteriores	
						}
					
					for(ValorEntradaTrayectoria veVista : trayectoria.getValoresEntradaTrayectoria()) {
						 System.out.println("Entro con trayectoria");

						ValorEntradaTrayectoria veValido = null;
						 valores2 = entradaBD.getValoresEntradaTrayectoria();
						 if (valores2 == null) {
							 System.out.println("BOBO");
						 }
						//Se agregan los valores que ya tenía asociada la entrada
						for(ValorEntradaTrayectoria valorBD : entradaBD.getValoresEntradaTrayectoria()) {
							if(valorBD.getId() != veVista.getId()) {
								valores.add(valorBD);
							}
							
							if(valorBD.getId().equals(veVista.getId())){
								veValido = valorBD;
							}
						}
						if(veValido != null) {
							veValido.setValor(veVista.getValor());
							valores.add(veValido);
						} else {
							veVista.setId(null);
							veVista.setValido(true);
							veVista.setEntrada(entradaBD);
							veVista.setTrayectoria(trayectoria);
							valores.add(veVista);
						}
				}
					entradaBD.getValoresEntradaTrayectoria().clear();
					entradaBD.getValoresEntradaTrayectoria().addAll(valores);
					/*if(ValorEntradaTrayectoriaBs.consultarRegistro(entradaBD,trayectoria)){
						
					}
					else{
						ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(entradaBD);
					//}
					//EntradaBs.modificarEntradaAlternativa(entradaBD, validarObligatorios);
			}
			
		}
		
		}
		
	}
	*/
	private void modificarAcciones(Trayectoria trayectoria, boolean validarObligatorios) throws Exception {
		if(jsonAcciones != null && !jsonAcciones.equals("")) {
			List<Accion> accionesVista = JsonUtil.mapJSONToArrayList(this.jsonAcciones, Accion.class);
			for(Accion accionVista : accionesVista) {
				//Para alternativas
				if(trayectoria.isAlternativa()){
				ValorAccionTrayectoria accionTrayectoria = null;
				ValorAccionTrayectoria accionTrayectoria2 = new ValorAccionTrayectoria();

				accionTrayectoria = ValorAccionTrayectoriaBs.consultarValor(accionVista, trayectoria);
				if(accionTrayectoria == null){
					Accion accionBD = AccionBs.consultarAccion(accionVista.getId());
					accionTrayectoria2.setAccion(accionBD);
					accionTrayectoria2.setTrayectoria(trayectoria);
					accionTrayectoria2.setMetodo(accionVista.getMetodo());
					accionTrayectoria2.setUrlDestino(accionVista.getUrlDestino());
					ValorAccionTrayectoriaBs.registrarValorAccionTrayectoria(accionTrayectoria2);
					}
				else{
					accionTrayectoria.setMetodo(accionVista.getMetodo());
					accionTrayectoria.setUrlDestino(accionVista.getUrlDestino());
					ValorAccionTrayectoriaBs.registrarValorAccionTrayectoria(accionTrayectoria);
				}
				}
				//Para principales
				else{
				Accion accionBD = AccionBs.consultarAccion(accionVista.getId());
				accionBD.setMetodo(accionVista.getMetodo());
				accionBD.setUrlDestino(accionVista.getUrlDestino());
				AccionBs.modificarAccion(accionBD, validarObligatorios);
				}
			}
		}
		
	}
	
	private void modificarReferenciasReglasNegocio(boolean validarObligatorios) throws Exception {
		if(jsonReferenciasReglasNegocio != null && !jsonReferenciasReglasNegocio.equals("")) {
			List<ReferenciaParametro> referenciasVista = JsonUtil.mapJSONToArrayList(this.jsonReferenciasReglasNegocio, ReferenciaParametro.class);
			for(ReferenciaParametro referenciaVista : referenciasVista) {
				ReferenciaParametro referenciaBD = ReferenciaParametroBs.consultarReferenciaParametro(referenciaVista.getId());
				
				Set<Query> queriesVista = referenciaVista.getQueries();
				Set<Query> queries = new HashSet<Query>(0);
				for(Query queryVista : queriesVista) {
					Query queryBD = QueryBs.consultarQuery(queryVista.getId());
					if(queryBD != null) {
						queryBD.setQuery(queryVista.getQuery());
						queries.add(queryBD);
					} else {
						queryVista.setReferenciaParametro(referenciaBD);
						queryVista.setId(null);
						queries.add(queryVista);
					}
				}
				referenciaBD.getQueries().clear();
				referenciaBD.getQueries().addAll(queries);
				
				ReferenciaParametroBs.modificarReferenciaParametro(referenciaBD, validarObligatorios);
			}
		}
	}
	

	private void modificarReferenciasParametrosMensaje(Trayectoria trayectoria, boolean validarObligatorios) throws Exception {

		if(jsonReferenciasParametrosMensaje != null && !jsonReferenciasParametrosMensaje.equals("")) {
			List<ReferenciaParametro> referenciasVista = JsonUtil.mapJSONToListWithSubtypes(this.jsonReferenciasParametrosMensaje, ReferenciaParametro.class);
			for(ReferenciaParametro referenciaVista : referenciasVista) {
				ReferenciaParametro referenciaBD = ReferenciaParametroBs.consultarReferenciaParametro(referenciaVista.getId());
				System.out.println("referenciaVista.getId(): "+referenciaVista.getId());
				Set<ValorMensajeParametro> valoresVista = referenciaVista.getValoresMensajeParametro();
				Set<ValorMensajeParametro> valores = new HashSet<ValorMensajeParametro>(0);
				
				for(ValorMensajeParametro valorVista : valoresVista) {
					ValorMensajeParametro valorBD = ValorMensajeParametroBs.consultarValor(valorVista.getId());
					
					MensajeParametro mensajeParametroBD = MensajeParametroBs.consultarMensajeParametro(valorVista.getMensajeParametro().getId());
					
					
					
					if(valorBD != null) {
						valorBD.setValor(valorVista.getValor());
						valores.add(valorBD);
					} else {
						valorVista.setId(null);
						valorVista.setReferenciaParametro(referenciaBD);
						valorVista.setMensajeParametro(mensajeParametroBD);
						valores.add(valorVista);
					}
				}
				referenciaBD.getValoresMensajeParametro().clear();
				referenciaBD.getValoresMensajeParametro().addAll(valores);
				
				ReferenciaParametroBs.modificarReferenciaParametro(referenciaBD, validarObligatorios);
			}
		}
		
		
	}
	
	private void modificarPantallas(Trayectoria trayectoria, boolean validarObligatorios) throws Exception {
		if(jsonPantallas != null && !jsonPantallas.equals("")) {
			List<Pantalla> pantallasVista = JsonUtil.mapJSONToArrayList(jsonPantallas, Pantalla.class);
			if(trayectoria.isAlternativa()){
				for(Pantalla pantallaVista : pantallasVista) {
					ValorPantallaTrayectoria pantallaTrayectoria = new ValorPantallaTrayectoria();
					Pantalla pantallaBD = PantallaBs.consultarPantalla(pantallaVista.getId());
					pantallaTrayectoria.setPantalla(pantallaBD);
					pantallaTrayectoria.setPatron(pantallaVista.getPatron());
					pantallaTrayectoria.setTrayectoria(trayectoria);
					//pantallaTrayectoria.setClave(pantallaVista.getClave());
					ValorPantallaTrayectoriaBs.modificarPatronPantalla(pantallaTrayectoria, validarObligatorios);
				}
			}
			else{
				for(Pantalla pantallaVista : pantallasVista) {
					Pantalla pantallaBD = PantallaBs.consultarPantalla(pantallaVista.getId());
					pantallaBD.setPatron(pantallaVista.getPatron());
					PantallaBs.modificarPatronPantalla(pantallaBD, validarObligatorios);
				}
			}
		}
		
	}
	
	private void obtenerJsonCamposPantallas(Trayectoria trayectoria) {
		if(trayectoria.isAlternativa()){
			/*List<ValorPantallaTrayectoria> pantallasAuxValorPantallaTrayectoria = new ArrayList<ValorPantallaTrayectoria>();
			pantallasAuxValorPantallaTrayectoria.addAll(ValorPantallaTrayectoriaBs.obtenerValoresPantallaTrayectoria(trayectoria));
			System.out.println("pantallasAuxValorPantallaTrayectoria: "+pantallasAuxValorPantallaTrayectoria);
			List<ValorPantallaTrayectoria> pantallasTrayectoriaAux= new ArrayList<ValorPantallaTrayectoria>();
			Set<Integer> identificadores = new HashSet<Integer>(0);
			
			for(ValorPantallaTrayectoria pantalla : pantallasAuxValorPantallaTrayectoria) {
				
				if(!identificadores.contains(pantalla.getId())) {
					System.out.println("pantalla.getPantalla(): "+pantalla.getPantalla());
					pantallasTrayectoriaAux.add(pantalla);
					identificadores.add(pantalla.getId());
				}
			}
			System.out.println("pantallasTrayectoriaAux: "+pantallasTrayectoriaAux);
			
			List<ValorPantallaTrayectoria> pantallas = new ArrayList<ValorPantallaTrayectoria>();
			for(ValorPantallaTrayectoria pantalla : pantallasTrayectoriaAux) {//anteriormente estaba pantallasTrayectoriaAux
				ValorPantallaTrayectoria pantallaAux = new ValorPantallaTrayectoria();
				pantallaAux.setId(pantalla.getId());
				//pantallaAux.setPantalla(ValorPantallaTrayectoriaBs.obtenerPantalla(pantalla));
				System.out.println("-> pantalla.getPantalla(): "+pantalla.getPantalla());
				pantallaAux.setPantalla(pantalla.getPantalla());
				pantallaAux.setTrayectoria(trayectoria);
				pantallaAux.setPatron(pantalla.getPatron());				
				pantallas.add(pantallaAux);
				System.out.println("pantallas: "+pantallas);
			}
			jsonPantallas = JsonUtil.mapListToJSON(pantallas);
			System.out.println("El contenido de jsonPantallas: "+jsonPantallas);
			*/
			/*List<ValorPantallaTrayectoria> pantallasAuxValorPantallaTrayectoria = new ArrayList<ValorPantallaTrayectoria>();
			pantallasAuxValorPantallaTrayectoria.addAll(ValorPantallaTrayectoriaBs.obtenerValoresPantallaTrayectoria(trayectoria));*/
			
			List<Pantalla> pantallasAuxTrayectorias = new ArrayList<Pantalla>();
			pantallasAuxTrayectorias.addAll(CuPruebasBs.obtenerPantallas(trayectoria));	
			List<Pantalla> pantallasAux = new ArrayList<Pantalla>();
			Set<Integer> identificadores = new HashSet<Integer>(0);
			Set<ValorPantallaTrayectoria> pantallasTrayectoriaAux= new HashSet<ValorPantallaTrayectoria>(0);
			for(Pantalla pantalla : pantallasAuxTrayectorias) {
				if(!identificadores.contains(pantalla.getId())) {
					pantallasAux.add(pantalla);
					identificadores.add(pantalla.getId());
				
					ValorPantallaTrayectoria valorValido = ValorPantallaTrayectoriaBs.consultarValor(pantalla, trayectoria);
					if(valorValido != null) {
						valorValido.setPantalla(null);
						valorValido.setTrayectoria(null);
						pantallasTrayectoriaAux.add(valorValido);
					}
				}
			}
			/*for(ValorPantallaTrayectoria pantalla : pantallasAuxValorPantallaTrayectoria) {
				pantallasTrayectoriaAux.add(pantalla);
			}*/

			List<Pantalla> pantallas = new ArrayList<Pantalla>();
			for(Pantalla pantalla : pantallasAux) {
				Pantalla pantallaAux = new Pantalla();
				pantallaAux.setId(pantalla.getId());
				pantallaAux.setClave(pantalla.getClave());
				pantallaAux.setNumero(pantalla.getNumero());
				pantallaAux.setNombre(pantalla.getNombre());
				pantallaAux.setPatron(pantalla.getPatron());
				pantallaAux.setValoresPantallaTrayectoria(pantallasTrayectoriaAux);
				
				pantallas.add(pantallaAux);
			}
			jsonPantallas = JsonUtil.mapListToJSON(pantallas);
			System.out.println("El contenido de jsonPantallas: "+jsonPantallas);
		}
		else{
			List<Pantalla> pantallasAuxTrayectorias = new ArrayList<Pantalla>();
			pantallasAuxTrayectorias.addAll(CuPruebasBs.obtenerPantallas(trayectoria));	
			List<Pantalla> pantallasAux = new ArrayList<Pantalla>();
			Set<Integer> identificadores = new HashSet<Integer>(0);
		
			for(Pantalla pantalla : pantallasAuxTrayectorias) {
				if(!identificadores.contains(pantalla.getId())) {
					pantallasAux.add(pantalla);
					identificadores.add(pantalla.getId());
				}
			}
			

			List<Pantalla> pantallas = new ArrayList<Pantalla>();
			for(Pantalla pantalla : pantallasAux) {
				Pantalla pantallaAux = new Pantalla();
				pantallaAux.setId(pantalla.getId());
				pantallaAux.setClave(pantalla.getClave());
				pantallaAux.setNumero(pantalla.getNumero());
				pantallaAux.setNombre(pantalla.getNombre());
				pantallaAux.setPatron(pantalla.getPatron());
				
				pantallas.add(pantallaAux);
			}
			jsonPantallas = JsonUtil.mapListToJSON(pantallas);
			System.out.println("El contenido de jsonPantallas: "+jsonPantallas);
		}
		
	}

	private void obtenerJsonCamposParametrosMensaje(Trayectoria trayectoria) {
		List<ReferenciaParametro> referenciasParametroAux = new ArrayList<ReferenciaParametro>();
		
			referenciasParametroAux.addAll(CuPruebasBs.obtenerReferenciasParametroMensajes(trayectoria));
		
		
		
		List<ReferenciaParametro> referencias = new ArrayList<ReferenciaParametro>();
	
		
		for(ReferenciaParametro referencia : referenciasParametroAux) {
			ReferenciaParametro referenciaAux = new ReferenciaParametro();
			
			Paso paso = referencia.getPaso();
			Paso pasoAux = new Paso();
			pasoAux.setRealizaActor(paso.isRealizaActor());
			pasoAux.setOtroVerbo(paso.getOtroVerbo());
			pasoAux.setVerbo(paso.getVerbo());
			pasoAux.setRedaccion(TokenBs.decodificarCadenaSinToken(paso.getRedaccion()));
			
			referenciaAux.setPaso(pasoAux);
			
			Mensaje mensaje = (Mensaje)referencia.getElementoDestino();
			Mensaje mensajeAux = new Mensaje();
			mensajeAux.setId(mensaje.getId());
			mensajeAux.setClave(mensaje.getClave());
			mensajeAux.setNumero(mensaje.getNumero());
			mensajeAux.setNombre(mensaje.getNombre());
			mensajeAux.setRedaccion(mensaje.getRedaccion());
			
			List<MensajeParametro> parametrosAux = new ArrayList<MensajeParametro>(mensaje.getParametros());
			List<MensajeParametro> parametros = new ArrayList<MensajeParametro>();
			
			for(MensajeParametro mensajeParametro : parametrosAux) {
				MensajeParametro mensajeParametroAux = new MensajeParametro();
				mensajeParametroAux.setId(mensajeParametro.getId());
				
				Parametro parametro = new Parametro();
				Parametro parametroAux = mensajeParametro.getParametro();
				
				parametro.setId(parametroAux.getId());
				parametro.setNombre(parametroAux.getNombre());
				
				mensajeParametroAux.setParametro(parametro);
				parametros.add(mensajeParametroAux);
			}
			
			mensajeAux.setParametros(new HashSet<MensajeParametro>(parametros));
			
			referenciaAux.setId(referencia.getId());
			referenciaAux.setElementoDestino(mensajeAux);
			if(trayectoria.isAlternativa()){
				List<ValorMensajeParametro> vmpsAux = ValorMensajeParametroBs.consultarValores(referencia);
				List<ValorMensajeParametro> vmps = new ArrayList<ValorMensajeParametro>();
				
				if(vmpsAux != null) {
					for(ValorMensajeParametro vmp : vmpsAux) {
						ValorMensajeParametro vmpAux = new ValorMensajeParametro();
						vmpAux.setId(vmp.getId());
						vmpAux.setValor(vmp.getValor());
						MensajeParametro mensajeParametroConId = new MensajeParametro();
						mensajeParametroConId.setId(vmp.getMensajeParametro().getId());
						vmpAux.setMensajeParametro(mensajeParametroConId);
						vmps.add(vmpAux);
					}
					referenciaAux.setValoresMensajeParametro(new HashSet<ValorMensajeParametro>(vmps));
				}
			}
			else{
				List<ValorMensajeParametro> vmpsAux = ValorMensajeParametroBs.consultarValores(referencia);
				List<ValorMensajeParametro> vmps = new ArrayList<ValorMensajeParametro>();
				
				if(vmpsAux != null) {
					for(ValorMensajeParametro vmp : vmpsAux) {
						ValorMensajeParametro vmpAux = new ValorMensajeParametro();
						vmpAux.setId(vmp.getId());
						vmpAux.setValor(vmp.getValor());
						MensajeParametro mensajeParametroConId = new MensajeParametro();
						mensajeParametroConId.setId(vmp.getMensajeParametro().getId());
						vmpAux.setMensajeParametro(mensajeParametroConId);
						vmps.add(vmpAux);
					}
					referenciaAux.setValoresMensajeParametro(new HashSet<ValorMensajeParametro>(vmps));
				}
			}
			
			
			/*Set<ValorMensajeParametro> valoresMensajeParametro = new HashSet<ValorMensajeParametro>(0);
			valoresMensajeParametro.add(new ValorMensajeParametro(null, null, null));
			referenciaAux.setValoresMensajeParametro(valoresMensajeParametro);*/
			referencias.add(referenciaAux);
		}
		jsonReferenciasParametrosMensaje = JsonUtil.mapListToJSON(referencias);
		System.out.println("El contenido de jsonReferenciasParametrosMensaje Alternativa: " + jsonReferenciasParametrosMensaje);

		
	}

	private void obtenerJsonCamposReglasNegocio(Trayectoria trayectoria) {
		List<ReferenciaParametro> referenciasReglaNegocioAux = new ArrayList<ReferenciaParametro>();
		
			referenciasReglaNegocioAux.addAll(CuPruebasBs.obtenerReferenciasReglasNegocioQuery(trayectoria));
		
		
		List<ReferenciaParametro> referencias = new ArrayList<ReferenciaParametro>();
		for(ReferenciaParametro referencia : referenciasReglaNegocioAux) {
			ReferenciaParametro referenciaAux = new ReferenciaParametro();

			Paso paso = referencia.getPaso();
			Paso pasoAux = new Paso();
			pasoAux.setRealizaActor(paso.isRealizaActor());
			pasoAux.setOtroVerbo(paso.getOtroVerbo());
			pasoAux.setVerbo(paso.getVerbo());
			pasoAux.setRedaccion(TokenBs.decodificarCadenaSinToken(paso.getRedaccion()));
			
			referenciaAux.setPaso(pasoAux);
			
			referenciaAux.setId(referencia.getId());
			
			ReglaNegocio regla = (ReglaNegocio)referencia.getElementoDestino();
			ReglaNegocio reglaNegocioAux = new ReglaNegocio();
			reglaNegocioAux.setId(regla.getId());
			reglaNegocioAux.setClave(regla.getClave());
			reglaNegocioAux.setNumero(regla.getNumero());
			reglaNegocioAux.setNombre(regla.getNombre());
			referenciaAux.setElementoDestino(reglaNegocioAux);
			

			Set<Query> queriesAux = QueryBs.consultarQueries(referencia);
			Set<Query> queries = new HashSet<Query>(0);
			
			if(queriesAux != null) {
				for(Query query : queriesAux) {
					Query queryAux = new Query();
					queryAux.setId(query.getId());
					queryAux.setQuery(query.getQuery());
					ReferenciaParametro referenciaConId = new ReferenciaParametro();
					referenciaConId.setId(referencia.getId());
					queryAux.setReferenciaParametro(referenciaConId);
					
					queries.add(queryAux);
				}
				referenciaAux.setQueries(queries);
			}
			
			referencias.add(referenciaAux);
		}
		
		
		jsonReferenciasReglasNegocio = JsonUtil.mapListToJSON(referencias);
		
	}

	private void obtenerJsonCamposEntradas(CasoUso actual, Trayectoria trayectoria) throws Exception{
		
		List<Entrada> entradasAux = new ArrayList<Entrada>(actual.getEntradas());
		//Si la trayectoria es Alternativa
		if(trayectoria.isAlternativa()){
			System.out.println("Entro a is Alternativa");
			List<Entrada> entradas = new ArrayList<Entrada>();
			for(Entrada entrada : entradasAux) {
				Entrada entradaAux = new Entrada();
				Atributo atributo = new Atributo();
				Atributo atributoAux = entrada.getAtributo();
				
				TerminoGlosario termino = new TerminoGlosario();
				TerminoGlosario terminoAux = entrada.getTerminoGlosario();
				
				if(atributoAux != null) {
					atributo.setNombre(atributoAux.getNombre());	
					entradaAux.setAtributo(atributo);
				}
				
				if(terminoAux != null) {
					termino.setNombre(terminoAux.getNombre());	
					entradaAux.setTerminoGlosario(termino);
				}
				
				
				Set<ValorEntradaTrayectoria> valores = new HashSet<ValorEntradaTrayectoria>(0);
				
				ValorEntradaTrayectoria valorValido = ValorEntradaTrayectoriaBs.consultarValor(entrada, trayectoria);
				//System.out.println("ValorEntradaTrayectoria es: " + valorValido.getValor());
				if(valorValido != null) {
					valorValido.setEntrada(null);
					valorValido.setTrayectoria(null);
					valores.add(valorValido);
				}
				
				entradaAux.setValoresEntradaTrayectoria(valores);
				entradaAux.setId(entrada.getId());
				entradaAux.setNombreHTML(entrada.getNombreHTML());
				entradas.add(entradaAux);
				System.out.println("Lleno los campos");
			}
			jsonEntradas = JsonUtil.mapListToJSON(entradas);
			System.out.println("El contenido de jsonEntradas Alternativa: " + jsonEntradas);

			System.out.println("Mapeo a JSon");
		}
		else{
		List<Entrada> entradas = new ArrayList<Entrada>();
		for(Entrada entrada : entradasAux) {
			Entrada entradaAux = new Entrada();
			Atributo atributo = new Atributo();
			Atributo atributoAux = entrada.getAtributo();
			
			TerminoGlosario termino = new TerminoGlosario();
			TerminoGlosario terminoAux = entrada.getTerminoGlosario();
			
			if(atributoAux != null) {
				atributo.setNombre(atributoAux.getNombre());	
				entradaAux.setAtributo(atributo);
			}
			
			if(terminoAux != null) {
				termino.setNombre(terminoAux.getNombre());	
				entradaAux.setTerminoGlosario(termino);
			}
			
			
			Set<ValorEntrada> valores = new HashSet<ValorEntrada>(0);
			
			ValorEntrada valorValido = ValorEntradaBs.consultarValorValido(entrada);
			if(valorValido != null) {
				valorValido.setEntrada(null);
				valorValido.setReglaNegocio(null);
				valores.add(valorValido);
			}
			
			entradaAux.setValores(valores);
			entradaAux.setId(entrada.getId());
			entradaAux.setNombreHTML(entrada.getNombreHTML());
			entradas.add(entradaAux);
			
		}
		jsonEntradas = JsonUtil.mapListToJSON(entradas);
		System.out.println("El contenido de jsonEntradas PRINCIPAL: " + jsonEntradas);

		} 
		
		
		

		
	}
	
	private void obtenerJsonCamposAcciones(Trayectoria trayectoria) throws Exception {
		
		List<Accion> accionesAux = new ArrayList<Accion>();

				accionesAux.addAll(CuPruebasBs.obtenerAcciones(trayectoria));							
		if(accionesAux == null){
			System.out.println("Está vacía");
		}
		List<Accion> acciones = new ArrayList<Accion>();
		List<String> imagenesPantallaAcciones = new ArrayList<String>();
		for(Accion accion : accionesAux) {
			Accion accionAux = new Accion();
			Pantalla pantalla = new Pantalla();


			//Si es alternativa
			if(trayectoria.isAlternativa()){
				ValorAccionTrayectoria valorAccionTrayectoria= null;
				valorAccionTrayectoria = ValorAccionTrayectoriaBs.consultarValor(accion, trayectoria);
				//Si es alternativa pero es la primera vez que se registra para esta Accion
				if(valorAccionTrayectoria == null){
					accionAux.setId(accion.getId());
					accionAux.setNombre(accion.getNombre());
					accionAux.setTipoAccion(accion.getTipoAccion());
					accionAux.setMetodo(accion.getMetodo());
					accionAux.setUrlDestino(accion.getUrlDestino());
					
					//Pantalla pantalla = new Pantalla();
					pantalla.setNombre(accion.getPantalla().getNombre());
					pantalla.setClave(accion.getPantalla().getClave());
					pantalla.setNumero(accion.getPantalla().getNumero());
					imagenesPantallaAcciones.add(ImageConverterUtil.parseBytesToPNGB64String(accion.getPantalla().getImagen()));
					pantalla.setId(accion.getPantalla().getId());
					
					if(accion.getPantallaDestino() != null) {
						Pantalla pantallaDestino = new Pantalla();
						pantallaDestino.setNombre(accion.getPantallaDestino().getNombre());
						pantallaDestino.setClave(accion.getPantallaDestino().getClave());
						pantallaDestino.setNumero(accion.getPantallaDestino().getNumero());
						pantallaDestino.setId(accion.getPantallaDestino().getId());
						accionAux.setPantallaDestino(pantallaDestino);
					}

				}
				//Si es alternativa pero ya existen campos
				else{
					//Accion accionAux = new Accion();
					accionAux.setId(accion.getId());
					accionAux.setNombre(accion.getNombre());
					accionAux.setTipoAccion(accion.getTipoAccion());
					accionAux.setMetodo(valorAccionTrayectoria.getMetodo());
					accionAux.setUrlDestino(valorAccionTrayectoria.getUrlDestino());
					
					//Pantalla pantalla = new Pantalla();
					pantalla.setNombre(accion.getPantalla().getNombre());
					pantalla.setClave(accion.getPantalla().getClave());
					pantalla.setNumero(accion.getPantalla().getNumero());
					imagenesPantallaAcciones.add(ImageConverterUtil.parseBytesToPNGB64String(accion.getPantalla().getImagen()));
					pantalla.setId(accion.getPantalla().getId());
					
					if(accion.getPantallaDestino() != null) {
						Pantalla pantallaDestino = new Pantalla();
						pantallaDestino.setNombre(accion.getPantallaDestino().getNombre());
						pantallaDestino.setClave(accion.getPantallaDestino().getClave());
						pantallaDestino.setNumero(accion.getPantallaDestino().getNumero());
						pantallaDestino.setId(accion.getPantallaDestino().getId());
						accionAux.setPantallaDestino(pantallaDestino);
					}
				}
			}
			//Si es Trayectoria Principal
			else{
			//Accion accionAux = new Accion();
			accionAux.setId(accion.getId());
			accionAux.setNombre(accion.getNombre());
			accionAux.setTipoAccion(accion.getTipoAccion());
			accionAux.setMetodo(accion.getMetodo());
			accionAux.setUrlDestino(accion.getUrlDestino());
			
			pantalla.setNombre(accion.getPantalla().getNombre());
			pantalla.setClave(accion.getPantalla().getClave());
			pantalla.setNumero(accion.getPantalla().getNumero());
			imagenesPantallaAcciones.add(ImageConverterUtil.parseBytesToPNGB64String(accion.getPantalla().getImagen()));
			pantalla.setId(accion.getPantalla().getId());
			
			if(accion.getPantallaDestino() != null) {
				Pantalla pantallaDestino = new Pantalla();
				pantallaDestino.setNombre(accion.getPantallaDestino().getNombre());
				pantallaDestino.setClave(accion.getPantallaDestino().getClave());
				pantallaDestino.setNumero(accion.getPantallaDestino().getNumero());
				pantallaDestino.setId(accion.getPantallaDestino().getId());
				accionAux.setPantallaDestino(pantallaDestino);
			}
			}
			
			accionAux.setPantalla(pantalla);
			
			acciones.add(accionAux);
		}
		jsonAcciones = JsonUtil.mapListToJSON(acciones);
		jsonImagenesPantallasAcciones = JsonUtil.mapListToJSON(imagenesPantallaAcciones);
	}

	public Trayectoria getCondicionTrayectoria(){
		return condicionTrayectoria;
	}
	public void setCondicionTrayectoria(Trayectoria condicionTrayectoria){
		this.condicionTrayectoria = condicionTrayectoria;
	}
	public List<Entrada> getListaIncidencias(){
		return listaIncidencias;
	}
	public void setListaIncidencias(List<Entrada> listaIncidencias){
		this.listaIncidencias = listaIncidencias;
	}
	public List<Atributo> getListaAtributo(){
		return listaAtributo;
	}
	public void setListaAtributo(List<Atributo> listaAtributo){
		this.listaAtributo = listaAtributo;
	}
	public Integer getIdTrayectoria(){
		return idTrayectoria;	
	}
	public void setIdTrayectoria(Integer idTrayectoria){
		this.idTrayectoria = idTrayectoria;
	}
	
	public Trayectoria getTrayectoria(){
		return trayectoria;	
	}
	public void setTrayectoria(Trayectoria trayectoria){
		this.trayectoria = trayectoria;
	}
	
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
	}

	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}

	public String getJsonAcciones() {
		return jsonAcciones;
	}

	public void setJsonAcciones(String jsonAcciones) {
		this.jsonAcciones = jsonAcciones;
	}

	public String getJsonReferenciasReglasNegocio() {
		return jsonReferenciasReglasNegocio;
	}

	public void setJsonReferenciasReglasNegocio(String jsonReferenciasReglasNegocio) {
		this.jsonReferenciasReglasNegocio = jsonReferenciasReglasNegocio;
	}

	public String getJsonEntradas() {
		return jsonEntradas;
	}

	public void setJsonEntradas(String jsonEntradas) {
		this.jsonEntradas = jsonEntradas;
	}

	

	public String getJsonReferenciasParametrosMensaje() {
		return jsonReferenciasParametrosMensaje;
	}

	public void setJsonReferenciasParametrosMensaje(
			String jsonReferenciasParametrosMensaje) {
		this.jsonReferenciasParametrosMensaje = jsonReferenciasParametrosMensaje;
	}

	public String getJsonPantallas() {
		return jsonPantallas;
	}

	public void setJsonPantallas(String jsonPantallas) {
		this.jsonPantallas = jsonPantallas;
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

	public String getJsonImagenesPantallasAcciones() {
		return jsonImagenesPantallasAcciones;
	}

	public void setJsonImagenesPantallasAcciones(
			String jsonImagenesPantallasAcciones) {
		this.jsonImagenesPantallasAcciones = jsonImagenesPantallasAcciones;
	}
}