package mx.prisma.generadorPruebas.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.PantallaDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorEntradaTrayectoriaDAO;
import mx.prisma.generadorPruebas.dao.ValorPantallaTrayectoriaDAO;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.generadorPruebas.model.ValorPantallaTrayectoria;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class ValorPantallaTrayectoriaBs {

	
	public static void modificarPatronPantalla(ValorPantallaTrayectoria pantalla, boolean validarObligatorios) {
		String patron = pantalla.getPatron();
		
			
		
		if (validarObligatorios && Validador.esNuloOVacio(patron)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó algún patrón de pantalla.", "MSG38", null,
					"campos");
		}
		if (Validador.validaLongitudMaxima(patron, 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un patrón de pantalla muy largo.", "MSG39",
					new String[] { "999", "caracteres", "el patrón de la pantalla "}, "campos");
		}
		
		if(new ValorPantallaTrayectoriaDAO().consultarValorPantallaTrayectoria(pantalla).isEmpty()){
			System.out.println("Entro a registrar de VALORPANTALLA TRAYETORIA");
			new ValorPantallaTrayectoriaDAO().registrarPantalla(pantalla);	
		}
		else{

			System.out.println("Entro a modificar de VALORPANTALLA TRAYECTORIA");
			new ValorPantallaTrayectoriaDAO().modificarValorPantallaTrayectoria(pantalla);
		}
	}
	public static Set<ValorPantallaTrayectoria> obtenerValoresPantallaTrayectoria(Trayectoria trayectoria){
		Set<ValorPantallaTrayectoria> pantallas = new HashSet<ValorPantallaTrayectoria>(0);
		List<Paso> pasos = new ArrayList<Paso>();
		pasos = TrayectoriaBs.obtenerPasos(trayectoria.getId());//
		for(Paso paso : pasos) {
			if(AnalizadorPasosBs.isSistemaMuestraPantalla(paso)) {
				Pantalla pantalla = (Pantalla) AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.PANTALLA).getElementoDestino();
				ValorPantallaTrayectoria pantallaValorTrayectoria = new ValorPantallaTrayectoriaDAO().obtenerValorPantallaTrayectoria(pantalla, trayectoria);
					pantallas.add(pantallaValorTrayectoria);
				}
			}
		return pantallas;
	}
	
	public static ValorPantallaTrayectoria consultarValor(Pantalla pantalla, Trayectoria trayectoria) {
		ValorPantallaTrayectoria valor = new ValorPantallaTrayectoriaDAO().obtenerValorPantallaTrayectoria(pantalla,trayectoria);
		return valor;
	}
	public static Pantalla obtenerPantalla(ValorPantallaTrayectoria pantalla){//¿Para qué creamos este?
		Pantalla pantalla_1 = new Pantalla();
		pantalla_1=new ValorPantallaTrayectoriaDAO().obtenerPantalla(pantalla);
		return pantalla_1;
	}
}


