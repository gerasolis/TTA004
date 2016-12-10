package mx.prisma.generadorPruebas.bs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.EntradaDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class ValorEntradaBs {
	public static void registrarValorEntrada(ValorEntrada valor) throws Exception{
		new ValorEntradaDAO().registrarValorEntrada(valor);
	}

	public static List<ValorEntrada> consultarValores(Entrada entrada) {
		List<ValorEntrada> valores = null;
		valores = new ValorEntradaDAO().consultarValores(entrada);
		return valores;
	}
	
	public static ValorEntrada consultarValorValido(Entrada entrada) {
		ValorEntrada valor = new ValorEntradaDAO().consultarValorValido(entrada);
		return valor;
	}

	public static ValorEntrada consultarValor(Integer id) {
		ValorEntrada valor = new ValorEntradaDAO().consultarValor(id);
		return valor;
	}

	public static ValorEntrada consultarValorInvalido(
			ReglaNegocio reglaNegocio, Entrada entrada) {
		return new ValorEntradaDAO().consultarValorInvalido(reglaNegocio, entrada);
	}
	
	public static void modificarValorEntrada(ValorEntrada valor, boolean validarObligatorios, Entrada entradaBD) throws Exception{
		validar(valor, validarObligatorios, entradaBD);
		new ValorEntradaDAO().registrarValorEntrada(valor);
		
	}
	
	private static void validar(ValorEntrada valor, boolean validarObligatorios, Entrada entrada) {

			if(valor.getValido()) {
				String valor2 = valor.getValor();
				if (validarObligatorios && Validador.esNuloOVacio(valor2)) {
					throw new PRISMAValidacionException(
							"El usuario no ingresó el valor de una entrada", "MSG38", null,
							"campos");
				}
				if (Validador.validaLongitudMaxima(valor2, 2000)) {
					throw new PRISMAValidacionException(
							"El usuario ingreso un valor muy largo.", "MSG39",
							new String[] { "2000", "caracteres", "el valor de " + entrada.getAtributo().getNombre() }, "campos");
				}
			}
		
		String etiqueta = entrada.getNombreHTML();
		if (validarObligatorios && Validador.esNuloOVacio(etiqueta)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la etiqueta de una entrada.", "MSG38", null,
					"campos");
		}
		if (Validador.validaLongitudMaxima(etiqueta, 200)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una etiqueta muy larga.", "MSG39",
					new String[] { "200", "caracteres", "la etiqueta de " + entrada.getAtributo().getNombre() }, "campos");
		}
	}

	//@Transactional(rollbackFor = Exception.class) 'Para evitar excepciones en varias conexiones. 
	//Se utiliza para inserts, updates y deletes.
	
	
}
