package mx.prisma.generadorPruebas.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import mx.prisma.generadorPruebas.bs.ValorEntradaBs;
import mx.prisma.generadorPruebas.bs.ValorMensajeParametroBs;
import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
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

@ResultPath("/content/generadorPruebas/")
@Results({
	@Result(name = "pantallaConfiguracionCasoUso", type = "dispatcher", location = "configuracion/casoUso.jsp"),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "modulos", type = "redirectAction", params = {
			"actionName", "modulos" }),
	@Result(name = "anterior", type = "redirectAction", params = {
			"actionName", "configuracion-casos-uso-previos!prepararConfiguracion" }),
	@Result(name = "documento", type = "stream", params = { 
	        "contentType", "${type}", 
	        "inputName", "fileInputStream", 
	        "bufferSize", "1024", 
	        "contentDisposition", "attachment;filename=\"${filename}\""})})

public class ConfiguracionCasoUsoCtrl extends ActionSupportPRISMA {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Colaborador colaborador;
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
	
	private InputStream fileInputStream;
	private String type;
    private String filename;
	
	public String prepararConfiguracion() {
		String resultado;
		try {
			colaborador = SessionManager.consultarColaboradorActivo();
			proyecto = SessionManager.consultarProyectoActivo();
			modulo = SessionManager.consultarModuloActivo();
			casoUso = SessionManager.consultarCasoUsoActivo();

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
			if(jsonEntradas == null || jsonEntradas.isEmpty()) {
				obtenerJsonCamposEntradas(casoUso);
			}
			
			if(jsonAcciones == null || jsonAcciones.isEmpty()) {
				obtenerJsonCamposAcciones(casoUso);
			}
			
			if(jsonReferenciasReglasNegocio == null || jsonReferenciasReglasNegocio.isEmpty()) {
				obtenerJsonCamposReglasNegocio(casoUso);
			}
			
			if(jsonReferenciasParametrosMensaje == null || jsonReferenciasParametrosMensaje.isEmpty()) {
				obtenerJsonCamposParametrosMensaje(casoUso);
			}
			
			if(jsonPantallas == null || jsonPantallas.isEmpty()) {
				obtenerJsonCamposPantallas(casoUso);
			}
			resultado = "pantallaConfiguracionCasoUso";
			
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
		
		return resultado; 
	}
	
	public String configurar() {
		String resultado;
		try {
			modificarEntradas(true);
			modificarAcciones(true);
			modificarReferenciasReglasNegocio(true);
			modificarReferenciasParametrosMensaje(true);
			modificarPantallas(true);
			
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
			
			modificarEntradas(false);
			modificarAcciones(false);
			modificarReferenciasReglasNegocio(false);
			modificarReferenciasParametrosMensaje(false);
			modificarPantallas(false);
			
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				throw new Exception();
			}
			
			
			addActionMessage(getText("MSG1", new String[] { "La", "Configuración del Caso de Uso",
			"guardada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
			resultado = prepararConfiguracion();
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
	
	private void modificarEntradas(boolean validarObligatorios) throws Exception {
		if (jsonEntradas != null && !jsonEntradas.equals("")) {
			List<Entrada> entradasVista = JsonUtil.mapJSONToArrayList(this.jsonEntradas, Entrada.class);
			
			for(Entrada entradaVista : entradasVista) {
				Entrada entradaBD = EntradaBs.consultarEntrada(entradaVista.getId());
				entradaBD.setNombreHTML(entradaVista.getNombreHTML());
				
				Set<ValorEntrada> valores = new HashSet<ValorEntrada>(0);
				
				
				 
				for(ValorEntrada veVista : entradaVista.getValores()) {
					ValorEntrada veValido = null;
					//Se agregan los valores que ya tenía asociada la entrada
					for(ValorEntrada valorBD : entradaBD.getValores()) {
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
						veVista.setReglaNegocio(null);
						valores.add(veVista);
					}
				}
				
				entradaBD.getValores().clear();
				entradaBD.getValores().addAll(valores);

				EntradaBs.modificarEntrada(entradaBD, validarObligatorios);
			}
			
		}
		
		
		
	}
	
	private void modificarAcciones(boolean validarObligatorios) throws Exception {
		if(jsonAcciones != null && !jsonAcciones.equals("")) {
			List<Accion> accionesVista = JsonUtil.mapJSONToArrayList(this.jsonAcciones, Accion.class);
			for(Accion accionVista : accionesVista) {
				Accion accionBD = AccionBs.consultarAccion(accionVista.getId());
				accionBD.setMetodo(accionVista.getMetodo());
				accionBD.setUrlDestino(accionVista.getUrlDestino());
				AccionBs.modificarAccion(accionBD, validarObligatorios);
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
	
	private void modificarReferenciasParametrosMensaje(boolean validarObligatorios) throws Exception {
		if(jsonReferenciasParametrosMensaje != null && !jsonReferenciasParametrosMensaje.equals("")) {
			List<ReferenciaParametro> referenciasVista = JsonUtil.mapJSONToListWithSubtypes(this.jsonReferenciasParametrosMensaje, ReferenciaParametro.class);
			for(ReferenciaParametro referenciaVista : referenciasVista) {
				ReferenciaParametro referenciaBD = ReferenciaParametroBs.consultarReferenciaParametro(referenciaVista.getId());
				
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
	
	private void modificarPantallas(boolean validarObligatorios) throws Exception {
		if(jsonPantallas != null && !jsonPantallas.equals("")) {
			List<Pantalla> pantallasVista = JsonUtil.mapJSONToArrayList(jsonPantallas, Pantalla.class);
			for(Pantalla pantallaVista : pantallasVista) {
				Pantalla pantallaBD = PantallaBs.consultarPantalla(pantallaVista.getId());
				pantallaBD.setPatron(pantallaVista.getPatron());
				PantallaBs.modificarPatronPantalla(pantallaBD, validarObligatorios);
			}
		}
		
	}
	
	private void obtenerJsonCamposPantallas(CasoUso casoUso2) {
		List<Pantalla> pantallasAuxTrayectorias = new ArrayList<Pantalla>();
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			pantallasAuxTrayectorias.addAll(CuPruebasBs.obtenerPantallas(trayectoria));
			
		}
		
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
		
	}

	private void obtenerJsonCamposParametrosMensaje(CasoUso casoUso) {
		List<ReferenciaParametro> referenciasParametroAux = new ArrayList<ReferenciaParametro>();
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			referenciasParametroAux.addAll(CuPruebasBs.obtenerReferenciasParametroMensajes(trayectoria));
		}
		
		
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
			
			
			/*Set<ValorMensajeParametro> valoresMensajeParametro = new HashSet<ValorMensajeParametro>(0);
			valoresMensajeParametro.add(new ValorMensajeParametro(null, null, null));
			referenciaAux.setValoresMensajeParametro(valoresMensajeParametro);*/
			referencias.add(referenciaAux);
		}
		jsonReferenciasParametrosMensaje = JsonUtil.mapListToJSON(referencias);
		
	}

	private void obtenerJsonCamposReglasNegocio(CasoUso casoUso2) {
		List<ReferenciaParametro> referenciasReglaNegocioAux = new ArrayList<ReferenciaParametro>();
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			referenciasReglaNegocioAux.addAll(CuPruebasBs.obtenerReferenciasReglasNegocioQuery(trayectoria));
		}
		
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

	private void obtenerJsonCamposEntradas(CasoUso previo) throws Exception{
		
		List<Entrada> entradasAux = new ArrayList<Entrada>(previo.getEntradas());

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
		

		
	}
	
	private void obtenerJsonCamposAcciones(CasoUso casoUso) {
		
		List<Accion> accionesAux = new ArrayList<Accion>();
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			if(!trayectoria.isAlternativa()) {
				accionesAux.addAll(CuPruebasBs.obtenerAcciones(trayectoria));
			}
		}
		for(Trayectoria trayectoria : casoUso.getTrayectorias()) {
			if(trayectoria.isAlternativa()) {
				accionesAux.addAll(CuPruebasBs.obtenerAcciones(trayectoria));
			}
		}
		
		
		List<Accion> acciones = new ArrayList<Accion>();
		List<String> imagenesPantallaAcciones = new ArrayList<String>();
		for(Accion accion : accionesAux) {
			Accion accionAux = new Accion();
			accionAux.setId(accion.getId());
			accionAux.setNombre(accion.getNombre());
			accionAux.setTipoAccion(accion.getTipoAccion());
			accionAux.setMetodo(accion.getMetodo());
			accionAux.setUrlDestino(accion.getUrlDestino());
			
			Pantalla pantalla = new Pantalla();
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
			
			accionAux.setPantalla(pantalla);
			
			acciones.add(accionAux);
		}
		jsonAcciones = JsonUtil.mapListToJSON(acciones);
		jsonImagenesPantallasAcciones = JsonUtil.mapListToJSON(imagenesPantallaAcciones);
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
