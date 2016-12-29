package mx.prisma.generadorPruebas.controller;

import java.io.InputStream;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.generadorPruebas.bs.EjecutarPruebaBs;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.util.ActionSupportPRISMA;
import mx.prisma.util.ErrorManager;
import mx.prisma.util.SessionManager;

@ResultPath("/content/generadorPruebas/")
@Results({
	@Result(name = "pantallaReporteGeneral", type = "dispatcher", location = "configuracion/reporteGeneral.jsp")})

public class ConfiguracionCasosUsoCtrl extends ActionSupportPRISMA{
	private static final long serialVersionUID = 1L;
	private Integer idCU;
	private CasoUso casoUso;
	private Modulo modulo;
	private List<ErroresPrueba> listErrores;
	private List<CasoUso> listCasosUso;
	
    public String generarReporteGeneral() {
		String resultado="";
		try {
			modulo = SessionManager.consultarModuloActivo();
			if (modulo == null) {
				System.out.println("error");
				return resultado;
			}
			listCasosUso = EjecutarPruebaBs.consultarCasosUso(modulo);
			listErrores = EjecutarPruebaBs.consultarErroresCasosUso(modulo);
			for(ErroresPrueba e : listErrores){
				System.out.println(e.getTipoError());
				System.out.println(e.getCasoUsoid().getDescripcion());
			}
			resultado = "pantallaReporteGeneral";
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
			SessionManager.set(this.getActionErrors(), "mensajesError");
			resultado = "cu";
		}
		return resultado;
	}
    
    public List<ErroresPrueba> getListErrores(){
		return listErrores;
	}
	public void setListErrores(List<ErroresPrueba> listErrores){
		this.listErrores = listErrores;
	}
	
	public List<CasoUso> getListCasosUso(){
		return listCasosUso;
	}
	public void setListCasoUso(List<CasoUso> listCasosUso){
		this.listCasosUso = listCasosUso;
	}

}
