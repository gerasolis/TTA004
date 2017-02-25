package mx.prisma.generadorPruebas.bs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroDAO;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.util.PRISMAException;

public class ValorMensajeParametroBs {

	public static List<ValorMensajeParametro> consultarValores(ReferenciaParametro referencia) {
		List<ValorMensajeParametro> vmp = new ValorMensajeParametroDAO().findByReferenciaParametro(referencia);
		if(vmp != null) {
			return vmp;
		}
		
		return null;
	}

	public static ValorMensajeParametro consultarValor(int id) {
		return new ValorMensajeParametroDAO().consultarValor(id);
	}
	
	public static List<ValorMensajeParametro> consultarValores_(Integer id){
		List<ValorMensajeParametro> listValorMensajeParametro=null;
		try{
			listValorMensajeParametro = new ValorMensajeParametroDAO().consultarValores_(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (listValorMensajeParametro == null) {
			throw new PRISMAException(
					"No se pueden consultar los pasos por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		return listValorMensajeParametro;
	}

}
