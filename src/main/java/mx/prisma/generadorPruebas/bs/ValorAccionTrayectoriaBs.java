package mx.prisma.generadorPruebas.bs;

import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorAccionTrayectoriaDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaTrayectoriaDAO;
import mx.prisma.generadorPruebas.model.ValorAccionTrayectoria;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class ValorAccionTrayectoriaBs {

	public static void registrarValorAccionTrayectoria(ValorAccionTrayectoria valor) throws Exception{
		validar(valor);
		new ValorAccionTrayectoriaDAO().registrarValorAccionTrayectoria(valor);
	}
	
	
	private static void validar(ValorAccionTrayectoria accion) {
		String valor = accion.getUrlDestino();
		
		if (Validador.validaLongitudMaxima(valor, 2000)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una url destino muy larga.", "MSG39",
					new String[] { "2000", "caracteres", "la Url destino de la Acción "}, "campos");
		}
		
		valor = accion.getMetodo();
		
		if (Validador.validaLongitudMaxima(valor, 10)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un método muy largo.", "MSG39",
					new String[] { "10", "caracteres", "el Método de la Acción "}, "campos");
		}
		
	}
	
	public static ValorAccionTrayectoria consultarValor(Accion accion, Trayectoria trayectoria ) throws Exception{
		ValorAccionTrayectoria valor = new ValorAccionTrayectoriaDAO().consultarValor(accion, trayectoria);
		return valor;
	}
	
}

