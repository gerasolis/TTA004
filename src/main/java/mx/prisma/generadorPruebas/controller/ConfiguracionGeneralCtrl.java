package mx.prisma.generadorPruebas.controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.List;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.AccessBs;
import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.ElementoBs;
import mx.prisma.editor.bs.ElementoBs.Estado;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.generadorPruebas.bs.ConfiguracionGeneralBs;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.generadorPruebas.model.ConfiguracionTrayectoria;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.SessionManager;
import mx.prisma.editor.model.Trayectoria;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

@ResultPath("/content/generadorPruebas/")
@Results({
		@Result(name = ActionSupportPRISMA.SUCCESS, type = "redirectAction", params = { "actionName",
				"configuracion-general" }),
		@Result(name = "cu", type = "redirectAction", params = { "actionName", "cu" }),
		@Result(name = "modulos", type = "redirectAction", params = { "actionName", "modulos" }),
		@Result(name = "pantallaConfiguracionGeneral", type = "dispatcher", location = "configuracion/general.jsp"),
		@Result(name = "ultimoPaso", type = "redirectAction", params = { "actionName",
				"configuracion-caso-uso!prepararConfiguracion" }),
		@Result(name = "conexion", type = "json", params = { "root", "resultadoConexion" }),
		@Result(name = "siguiente", type = "redirectAction", params = { "actionName",
				"configuracion-casos-uso-previos!prepararConfiguracion" }) })
public class ConfiguracionGeneralCtrl extends ActionSupportPRISMA {
	private static final long serialVersionUID = 1L;
	private ConfiguracionBaseDatos cbd;
	private ConfiguracionHttp chttp;
	private Colaborador colaborador;
	private Proyecto proyecto;
	private Modulo modulo;
	private Integer idCU;
	private CasoUso casoUso;
	private String resultadoConexion;
	private String url;
	private String driver;
	private String usuario;
	private String contrasenia;
	private int id;
	private List<String> checkMe;
	private List<Trayectoria> listTrayectoria;
	private ConfiguracionTrayectoria cty;

	@SuppressWarnings("unchecked")
	public String prepararConfiguracion() throws Exception {
		Map<String, Object> session = null;

		String resultado;
		colaborador = SessionManager.consultarColaboradorActivo();
		proyecto = SessionManager.consultarProyectoActivo();
		modulo = SessionManager.consultarModuloActivo();
		casoUso = SessionManager.consultarCasoUsoActivo();

		if (casoUso == null) {
			session = ActionContext.getContext().getSession();
			session.put("idCU", idCU);
			casoUso = SessionManager.consultarCasoUsoActivo();
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

		listTrayectoria = TrayectoriaBs.consultarTrayectoriaxCasoUso(casoUso);
		SessionManager.set(listTrayectoria, "trayectorias");

		Collection<String> msjs = (Collection<String>) SessionManager.get("mensajesAccion");
		this.setActionMessages(msjs);
		SessionManager.delete("mensajesAccion");

		Collection<String> msjsError = (Collection<String>) SessionManager.get("mensajesError");
		this.setActionErrors(msjsError);
		SessionManager.delete("mensajesError");
		return "pantallaConfiguracionGeneral";
	}

	public String configurar() throws Exception {
		String resultado;
		try {
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}

			cbd.setCasoUso(casoUso);
			chttp.setCasoUso(casoUso);

			ConfiguracionBaseDatos cbdBD = ConfiguracionGeneralBs.consultarConfiguracionBaseDatos(casoUso);
			ConfiguracionHttp chttpBD = ConfiguracionGeneralBs.consultarConfiguracionHttp(casoUso);

			if (cbdBD == null) {
				cbdBD = cbd;
			} else {
				if (cbd.getContrasenia() == null) {
					cbdBD.setContrasenia("");
				} else {
					cbdBD.setContrasenia(cbd.getContrasenia());
				}

				cbdBD.setDriver(cbd.getDriver());
				cbdBD.setUrlBaseDatos(cbd.getUrlBaseDatos());
				cbdBD.setUsuario(cbd.getUsuario());
			}

			if (chttpBD == null) {
				chttpBD = chttp;
			} else {
				chttpBD.setIp(chttp.getIp());
				chttpBD.setPuerto(chttp.getPuerto());
			}
			ConfiguracionGeneralBs.validar(cbd, true);
			ConfiguracionGeneralBs.validar(chttp, true);

			casoUso.setConfiguracionBaseDatos(cbdBD);
			casoUso.setConfiguracionHttp(chttpBD);

			ElementoBs.modificarEstadoElemento(casoUso, Estado.PRECONFIGURADO);

			System.out.println("Prueba :" + isCheckMe());
			setCheckMe(isCheckMe());
			ConfiguracionTrayectoria cty1 = new ConfiguracionTrayectoria();
			cty1.setCasoUso(casoUso);
			cty1.setCondicion(isCheckMe());
			System.out.println(cty1.getCondicion());
			ConfiguracionCasosUsoPreviosCtrl ccp = new ConfiguracionCasosUsoPreviosCtrl();
			ccp.setConfigTray(cty1);

			resultado = "siguiente";

			addActionMessage(getText("MSG1", new String[] { "La", "Configuración general", "registrada" }));
			SessionManager.set(this.getActionMessages(), "mensajesAccion");

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

	public String guardar() throws Exception {
		String resultado;
		try {
			casoUso = SessionManager.consultarCasoUsoActivo();

			if (casoUso == null) {
				resultado = "cu";
				return resultado;
			}

			cbd.setCasoUso(casoUso);
			chttp.setCasoUso(casoUso);

			ConfiguracionBaseDatos cbdBD = ConfiguracionGeneralBs.consultarConfiguracionBaseDatos(casoUso);
			ConfiguracionHttp chttpBD = ConfiguracionGeneralBs.consultarConfiguracionHttp(casoUso);

			if (cbdBD == null) {
				cbdBD = cbd;
			} else {
				if (cbd.getContrasenia() == null) {
					cbdBD.setContrasenia("");
				} else {
					cbdBD.setContrasenia(cbd.getContrasenia());
				}
				cbdBD.setDriver(cbd.getDriver());
				cbdBD.setUrlBaseDatos(cbd.getUrlBaseDatos());
				cbdBD.setUsuario(cbd.getUsuario());
			}

			if (chttpBD == null) {
				chttpBD = chttp;
			} else {
				chttpBD.setIp(chttp.getIp());
				chttpBD.setPuerto(chttp.getPuerto());
			}
			ConfiguracionGeneralBs.validar(cbd, false);
			ConfiguracionGeneralBs.validar(chttp, false);

			casoUso.setConfiguracionBaseDatos(cbdBD);
			casoUso.setConfiguracionHttp(chttpBD);

			ElementoBs.modificarEstadoElemento(casoUso, Estado.PRECONFIGURADO);

			addActionMessage(getText("MSG1", new String[] { "La", "Configuración general", "guardada" }));
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

	public static boolean esConfigurable(int id) {
		CasoUso casoUso = CuBs.consultarCasoUso(id);
		try {
			ElementoBs.verificarEstado(casoUso, CU_CasosUso.CONFIGURARPRUEBA5_7);
			for (CasoUso previo : CuBs.obtenerCaminoPrevioMasCorto(casoUso)) {
				ElementoBs.verificarEstado(previo, CU_CasosUso.CONFIGURARPRUEBA5_7);
			}
			return true;
		} catch (PRISMAException pe) {
			return false;
		}
	}

	public String probarConexion() {
		resultadoConexion = "Hubo un error en la conexión\n";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException cne) {
			System.out.println("No se encontró el driver: ");
			resultadoConexion = resultadoConexion.concat(cne.getMessage());
			cne.printStackTrace();
			return "conexion";
		}
		try {
			Connection conn = DriverManager.getConnection(url, usuario, contrasenia);

			if (conn != null) {
				resultadoConexion = "Conexión exitosa";
			}
			conn.close();
		} catch (SQLException e) {
			System.out.println("Se generó un error en la conexión: ");
			e.printStackTrace();
			resultadoConexion = resultadoConexion.concat(e.getMessage());
		}
		return "conexion";
	}

	public List<Trayectoria> getListTrayectoria() {
		return listTrayectoria;
	}

	public void setListTrayectoria(List<Trayectoria> listTrayectoria) {
		this.listTrayectoria = listTrayectoria;
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

	public void setCty(ConfiguracionTrayectoria cty) {
		this.cty = cty;
	}

	public ConfiguracionTrayectoria getCty() {
		if (cty == null) {
			cty = new ConfiguracionTrayectoria();
			cty.setCasoUso(casoUso);
		}
		return cty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> isCheckMe() {
		return checkMe;
	}

	public void setCheckMe(List<String> checkMe) {
		this.checkMe = checkMe;
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

	public String getResultadoConexion() {
		return resultadoConexion;
	}

	public void setResultadoConexion(String resultadoConexion) {
		this.resultadoConexion = resultadoConexion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

}
