package mx.prisma.generadorPruebas.bs;

import java.util.List;

import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroDAO;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroTrayectoriaDAO;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametroTrayectoria;


public class ValorMensajeParametroTrayectoriaBs {
	public static ValorMensajeParametroTrayectoria consultarValor(
			int id) {
		return new ValorMensajeParametroTrayectoriaDAO().consultarValor(id);
	}
	
	public static List<ValorMensajeParametroTrayectoria> consultarValores(ReferenciaParametro referencia) {
		List<ValorMensajeParametroTrayectoria> vmp = new ValorMensajeParametroTrayectoriaDAO().findByReferenciaParametro(referencia);
		if(vmp != null) {
			return vmp;
		}
		
		return null;
	}
		
}
