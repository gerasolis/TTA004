package mx.prisma.generadorPruebas.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.editor.bs.AccionBs;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.bs.EntradaBs;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.CuPruebasBs;
import mx.prisma.generadorPruebas.bs.ValorEntradaBs;
import mx.prisma.generadorPruebas.model.ConfiguracionTrayectoria;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
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
	@Result(name = "pantallaConfiguracionCasosUsoPrevios", type = "dispatcher", location = "configuracion/casosUsoPrevios.jsp"),
	@Result(name = "pantallaConfiguracionCasoUsoPrevio", type = "dispatcher", location = "configuracion/casoUsoPrevio.jsp"),
	@Result(name = "cu", type = "redirectAction", params = {
			"actionName", "cu" }),
	@Result(name = "siguiente", type = "redirectAction", params = {
			//"actionName", "configuracion-caso-uso!prepararConfiguracion"})})
	//@Result(name = "siguiente", type = "redirectAction", params = {
			"actionName", "configuracion-trayectorias!prepararConfiguracion"})})
public class ConfiguracionCasosUsoPreviosCtrl extends ActionSupportPRISMA {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CasoUso casoUso;
	private CasoUso previo;
	private Proyecto proyecto;
	private Modulo modulo;
	private Colaborador colaborador;
	private Integer idCU;
	private List<CasoUso> listCU;
	private Integer idCUPrevio;
	private String jsonEntradas;
	private String jsonAcciones;
	private String jsonImagenesPantallasAcciones;
	private ConfiguracionTrayectoria configTray;
	
	public String prepararConfiguracion() throws Exception {
		String resultado;

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
		
		listCU = CuBs.obtenerCaminoPrevioMasCorto(casoUso);
		SessionManager.set(listCU, "casosUsoPrevios");
		
		SessionManager.delete("idPrevio");
		
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
		return "pantallaConfiguracionCasosUsoPrevios"; 
	}
	
	public String configurar() {
		String resultado;
		
		try {
			//Se generan los valores
			casoUso = SessionManager.consultarCasoUsoActivo();
			Set<ReglaNegocio> reglas = new HashSet<ReglaNegocio>(0);
			for(CasoUsoReglaNegocio curn : casoUso.getReglas()) {
				reglas.add(curn.getReglaNegocio());
			}
			//CuPruebasBs.generarValores(casoUso.getEntradas(), reglas);
			//CuPruebasBs.generarValoresTrayectorias(casoUso.getEntradas(), reglas, casoUso.getTrayectorias());
			listCU = (List<CasoUso>) SessionManager.get("casosUsoPrevios");
			SessionManager.delete("casosUsoPrevios");
			if(listCU != null) {
				for(CasoUso cu : listCU) {
					if (cu.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.CONFIGURADO) && cu.getEstadoElemento().getId() != ElementoBs.getIdEstado(Estado.PRECONFIGURADO)){
						throw new PRISMAValidacionException("Falta configurar un caso de uso.", "MSG37");
					}
				}
			}
			resultado = "siguiente";
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "pantallaConfiguracionCasosUsoPrevios";
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
	
	public String prepararConfiguracionCasoUso() {
		Map<String, Object> session = null;
		String resultado;
		try {
			this.clearErrorsAndMessages();
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
			
			
			previo = SessionManager.consultarCasoUsoPrevio();
			
			if (previo == null) {
				session = ActionContext.getContext().getSession();
				session.put("idPrevio", idCUPrevio);
				previo = SessionManager.consultarCasoUsoPrevio();
			}

			if(jsonEntradas == null || jsonEntradas.isEmpty()) {
				obtenerJsonCamposEntradas(previo);
			}
			
			if(jsonAcciones == null || jsonAcciones.isEmpty()) {
				obtenerJsonCamposAcciones(previo);
			}
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "pantallaConfiguracionCasosUsoPrevios";
		}
		
		
		return "pantallaConfiguracionCasoUsoPrevio";
	}
	
	public String configurarCasoUso() throws Exception {
		String resultado;
		try {
			
			modificarEntradas(true);
			modificarAcciones(true);
			
			previo = SessionManager.consultarCasoUsoPrevio();
			ElementoBs.modificarEstadoElemento(previo, Estado.PRECONFIGURADO);
			SessionManager.delete("idPrevio");
			
			addActionMessage(getText("MSG1", new String[] { "La", "Configuración del caso de uso",
			"registrada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
			return prepararConfiguracion();
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = prepararConfiguracionCasoUso();
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
	
	public String guardarCasoUso() throws Exception {
		String resultado;
		try {
			
			modificarEntradas(false);
			modificarAcciones(false);
			
			previo = SessionManager.consultarCasoUsoPrevio();
			ElementoBs.modificarEstadoElemento(previo, Estado.LIBERADO);
			
			
			addActionMessage(getText("MSG1", new String[] { "La", "Configuración del caso de uso",
			"guardada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");
			return prepararConfiguracionCasoUso();
		} catch (PRISMAValidacionException pve) {
			ErrorManager.agregaMensajeError(this, pve);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = prepararConfiguracionCasoUso();
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
	
	private void obtenerJsonCamposAcciones(CasoUso previo) {
		Trayectoria trayectoriaPrincipal = CuBs.obtenerTrayectoriaPrincipal(previo);
		
		if(trayectoriaPrincipal != null) {
			List<Accion> accionesAux = CuPruebasBs.obtenerAcciones(trayectoriaPrincipal);
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
	}

	public List<CasoUso> getListCU() {
		return listCU;
	}

	public void setListCU(List<CasoUso> listCU) {
		this.listCU = listCU;
	}
	
	public ConfiguracionTrayectoria getConfigTray(){
		return configTray;
	}
	public void setConfigTray(ConfiguracionTrayectoria configTray){
		this.configTray = configTray;
		HttpSession misession = request.getSession(true);
		misession.setAttribute("configTray", configTray);
	
	}
	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
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

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Integer getIdCU() {
		return idCU;
	}

	public void setIdCU(Integer idCU) {
		this.idCU = idCU;
	}

	public Integer getIdCUPrevio() {
		return idCUPrevio;
	}

	public void setIdCUPrevio(Integer idCUPrevio) {
		this.idCUPrevio = idCUPrevio;
	}

	public String getJsonEntradas() {
		return jsonEntradas;
	}

	public void setJsonEntradas(String jsonEntradas) {
		this.jsonEntradas = jsonEntradas;
	}

	public String getJsonAcciones() {
		return jsonAcciones;
	}

	public void setJsonAcciones(String jsonAcciones) {
		this.jsonAcciones = jsonAcciones;
	}

	public CasoUso getPrevio() {
		return previo;
	}

	public void setPrevio(CasoUso previo) {
		this.previo = previo;
	}

	public String getJsonImagenesPantallasAcciones() {
		return jsonImagenesPantallasAcciones;
	}

	public void setJsonImagenesPantallasAcciones(
			String jsonImagenesPantallasAcciones) {
		this.jsonImagenesPantallasAcciones = jsonImagenesPantallasAcciones;
	}

	
	
}