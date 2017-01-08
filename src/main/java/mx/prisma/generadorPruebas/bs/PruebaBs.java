package mx.prisma.generadorPruebas.bs;

import java.util.List;

import mx.prisma.editor.model.CasoUso;
import mx.prisma.generadorPruebas.dao.ErroresPruebaDAO;
import mx.prisma.generadorPruebas.dao.PruebaDAO;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;

public class PruebaBs {
	public static List<Prueba> consultarPruebasxCasoUso(CasoUso casoUso){
		List<Prueba> listPruebas=null;
		listPruebas = new PruebaDAO().consultarPruebasxCasoUso(casoUso);
		return listPruebas;
	}
	public static List<Prueba> consultarPruebas(){
		List<Prueba> listPruebas=null;
		listPruebas = new PruebaDAO().consultarPruebas();
		return listPruebas;
	}
}
