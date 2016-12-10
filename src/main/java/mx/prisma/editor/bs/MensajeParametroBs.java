package mx.prisma.editor.bs;

import mx.prisma.editor.dao.MensajeParametroDAO;
import mx.prisma.editor.model.MensajeParametro;

public class MensajeParametroBs {

	public static MensajeParametro consultarMensajeParametro(Integer id) {
		return new MensajeParametroDAO().findById(id);
	}

}
