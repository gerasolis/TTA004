package mx.prisma.editor.bs;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.editor.dao.MensajeParametroDAO;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroDAO;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.util.PRISMAException;

public class MensajeParametroBs {

	public static MensajeParametro consultarMensajeParametro(Integer id) {
		return new MensajeParametroDAO().findById(id);
	}
	

	
	public static List<MensajeParametro> consultarMensajeParametro_(Integer id){
		List<MensajeParametro> listMensajeParametro=null;
		try{
			listMensajeParametro = new MensajeParametroDAO().consultarMensajeParametro_(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		if (listMensajeParametro == null) {
			throw new PRISMAException(
					"No se pueden consultar los pasos por el id.", "MSG16",
					new String[] { "La", "trayectoria" });
		}
		return listMensajeParametro;
	}

}
