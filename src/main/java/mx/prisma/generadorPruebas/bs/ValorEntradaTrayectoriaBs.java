package mx.prisma.generadorPruebas.bs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.bs.TipoDatoEnum;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.editor.bs.EntradaBs;
import mx.prisma.editor.bs.ReglaNegocioBs;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaTrayectoriaDAO;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.util.GeneradorCadenasUtil;
import mx.prisma.util.JsonUtil;

public class ValorEntradaTrayectoriaBs {
	
	public static void registrarValorEntradaTrayectoria(ValorEntradaTrayectoria valor) throws Exception{
		new ValorEntradaTrayectoriaDAO().registrarValorEntradaTrayectoria(valor);
	}
	
	public static void registrarValorEntradaTrayectoria(Entrada valor) throws Exception{
		new ValorEntradaTrayectoriaDAO().registrarValorEntradaTrayectoria(valor);
	}
	
	public static void registrarValorEntradaTrayectoria(Set<ValorEntradaTrayectoria> valor) throws Exception{
		new ValorEntradaTrayectoriaDAO().registrarValorEntradaTrayectoria(valor);
	}
	
	public static void generarValores(Set<Entrada> entradas,
			Set<Trayectoria> trayectorias) throws Exception {
		for(Trayectoria trayectoria: trayectorias) {
				for(Entrada entrada : entradas) {
					if(entrada.getAtributo() != null) {

						ValorEntradaTrayectoria valorInvalidoBD = null;
						String valorCadenaInvalido = null;
						
						valorInvalidoBD = ValorEntradaTrayectoriaBs.consultarValor(entrada, trayectoria);
						
						if(valorInvalidoBD == null) {
							valorInvalidoBD = new ValorEntradaTrayectoria();
							valorInvalidoBD.setTrayectoria(trayectoria);
							valorInvalidoBD.setEntrada(entrada);
							valorInvalidoBD.setValido(false);
						}
						
							valorCadenaInvalido = generarValor();
						
						
						if(valorCadenaInvalido != null) { 
							valorInvalidoBD.setValor(valorCadenaInvalido);
							System.out.println("Entro a valorValidoBD 1" + valorInvalidoBD.getId());

							ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(valorInvalidoBD);
						}
					} 
					
				}
		}
		
	}
	
	private static String generarValor() {
		String valorCadena = null;
		valorCadena = "a";
		return valorCadena;
	}

	/*
	public static Set<ValorEntradaTrayectoria> consultarValores(Entrada entrada) {
		Set<ValorEntradaTrayectoria> valores = new HashSet<ValorEntradaTrayectoria>(new ValorEntradaTrayectoriaDAO().consultarValores(entrada));
		return valores;
	}*/
		
	public static ValorEntradaTrayectoria consultarValor(Entrada entrada, Trayectoria trayectoria) {
		ValorEntradaTrayectoria valor = new ValorEntradaTrayectoriaDAO().consultarValor(entrada,trayectoria);
		return valor;
	}
	public static List<Trayectoria> consultarEstado(Trayectoria trayectoria){
		List<Trayectoria> results=null;
		
		results=new ValorEntradaTrayectoriaDAO().consultarEstado(trayectoria);
		//Checar el cast de list a set.
		return results;
	}
	
	public static void modificarEntradas(Trayectoria trayectoria, boolean validarObligatorios, String jsonEntradas) throws Exception {
		if (jsonEntradas != null && !jsonEntradas.equals("")) {
			System.out.println("Entro a Modificar Entrada");
			List<Entrada> entradasVista = JsonUtil.mapJSONToArrayList(jsonEntradas, Entrada.class);
			System.out.println("JsonEntradas de modificarEntradas :" + jsonEntradas);
			//System.out.println("entradasVista primer objeto :" + entradasVista.get(0));

			for(Entrada entradaVista : entradasVista) {
				
				Entrada entradaBD = EntradaBs.consultarEntrada(entradaVista.getId());
				//ValorEntrada nuevo = ValorEntradaBs.consultarValorValido(entradaVista);
				//System.out.println("entradasVista primer valor :" + nuevo.getValor());

				entradaBD.setNombreHTML(entradaVista.getNombreHTML());
				System.out.println("Es alternativa: " + trayectoria.isAlternativa());

				//Termina el normal
				
				if(!trayectoria.isAlternativa()){
					Set<ValorEntrada> valores = new HashSet<ValorEntrada>(0);
					
					for(ValorEntradaTrayectoria veVista : entradaVista.getValoresEntradaTrayectoria()) {
						ValorEntrada veVista2 = new ValorEntrada();
						ValorEntrada veValido = null;
						//ValorEntrada valorTrayectoria = null;
						List<ValorEntrada> ListaForEntradas = null;
						 ListaForEntradas = obtenerValoresEntrada(entradaBD);
						//Se agregan los valores que ya tenía asociada la entrada
						for(ValorEntrada valorBD : ListaForEntradas) {
							
							if(valorBD.getId() != veVista.getId()) {
								valores.add(valorBD);
								//valorTrayectoria.setValor(veVista.getValor());
								//valorTrayectoria.setId(veVista.getId());
								//valorTrayectoria.setValido(true);
								//valorTrayectoria.setEntrada(entradaBD);
								//valorTrayectoria.setReglaNegocio(valorBD.getReglaNegocio());
							}
							
							if(valorBD.getId().equals(veVista.getId())){
								veValido = valorBD;
								
								//valorTrayectoria.setValor(veVista.getValor());
								//valorTrayectoria.setId(veVista.getId());
								//valorTrayectoria.setValido(true);
							}
						}
						
						if(veValido != null) {
							veValido.setValor(veVista.getValor());
							//valorTrayectoria.setEntrada(entradaBD);
							//valorTrayectoria.setReglaNegocio(veValido.getReglaNegocio());
							valores.add(veValido);
						} else {
							veVista2.setId(null);
							veVista2.setValido(true);
							veVista2.setEntrada(entradaBD);
							veVista2.setReglaNegocio(null);
							valores.add(veVista2);
						}
						
						//ValorEntradaBs.modificarValorEntrada(valorTrayectoria, validarObligatorios, entradaBD);
					}
					System.out.println("Termino los set");

					entradaBD.getValores().clear();
					entradaBD.getValores().addAll(valores);
					EntradaBs.modificarEntrada(entradaBD, validarObligatorios);
					
					System.out.println("Modifico Entrada PRINCIPAL");
				}
				else{
					
					Set<ValorEntradaTrayectoria> valores = new HashSet<ValorEntradaTrayectoria>(0);
					//Set<ValorEntradaTrayectoria> valores2 = new HashSet<ValorEntradaTrayectoria>(0);
					ValorEntradaTrayectoria valorTrayectoria = new ValorEntradaTrayectoria();
					if(entradaVista.getValoresEntradaTrayectoria() == null){
						//Qué hacer si no encuentra registros anteriores	
						}
					List<ValorEntradaTrayectoria> ListaForTrayectoria = null;
					ListaForTrayectoria = obtenerValores(trayectoria);
					for(ValorEntradaTrayectoria veVista : entradaVista.getValoresEntradaTrayectoria()) {
						 System.out.println("Entro con trayectoria ");

						ValorEntradaTrayectoria veValido = null;

						 List<ValorEntradaTrayectoria> ListaForEntradas = null;
						 ListaForEntradas = obtenerValores(entradaBD);
						 System.out.println("Tamaño listaForEntradas: " + ListaForEntradas.size());

						//Se agregan los valores que ya tenía asociada la entrada
						for(ValorEntradaTrayectoria valorBD : ListaForEntradas) {
							if(valorBD.getId() != veVista.getId()) {
								valores.add(valorBD);
								valorTrayectoria.setValor(veVista.getValor());
								valorTrayectoria.setId(veVista.getId());
								valorTrayectoria.setValido(false);
								valorTrayectoria.setEntrada(entradaBD);
								valorTrayectoria.setTrayectoria(trayectoria);
								 System.out.println("Entro a ids no iguales");

							}
							
							if(valorBD.getId().equals(veVista.getId())){
								veValido = valorBD;
								//valorTrayectoria = valorBD;
							}
						}
						if(veValido != null) {
							valorTrayectoria.setValor(veVista.getValor());
							valorTrayectoria.setId(veVista.getId());
							valorTrayectoria.setValido(false);
							valorTrayectoria.setEntrada(entradaBD);
							valorTrayectoria.setTrayectoria(trayectoria);

							valores.add(veValido);
							

						} else {
							valorTrayectoria.setValor(veVista.getValor());
							valorTrayectoria.setId(veVista.getId());
							valorTrayectoria.setValido(false);
							valorTrayectoria.setEntrada(entradaBD);
							valorTrayectoria.setTrayectoria(trayectoria);
							valores.add(veVista);
							//ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(veVista);

						}
						
				}
					
					ValorEntradaTrayectoriaBs.registrarValorEntradaTrayectoria(valorTrayectoria);
					
					//entradaBD.getValoresEntradaTrayectoria().clear();
					//entradaBD.getValoresEntradaTrayectoria().addAll(valores);
					//if(ValorEntradaTrayectoriaBs.consultarRegistro(entradaBD,trayectoria)){
						
					//}
				//	else{
						
					//}
					//EntradaBs.modificarEntradaAlternativa(entradaBD, validarObligatorios);
			}
			
		}
		
		}
		
	}



public static List<ValorEntradaTrayectoria> obtenerValores (Trayectoria trayectoria){
	List<ValorEntradaTrayectoria> lista = null;
	lista = new ValorEntradaTrayectoriaDAO().obtenerValores(trayectoria);
	if(lista.equals(null)){
		return null;
	}
	else
	return lista;
	
}

public static List<ValorEntradaTrayectoria> obtenerValores (Entrada entrada){
	List<ValorEntradaTrayectoria> lista = null;
	lista = new ValorEntradaTrayectoriaDAO().obtenerValores(entrada);
	if(lista.equals(null)){
		return null;
	}
	else
	return lista;
	
}

public static List<ValorEntrada> obtenerValoresEntrada (Entrada entrada){
	List<ValorEntrada> lista = null;
	lista = new ValorEntradaDAO().obtenerValoresEntrada(entrada);
	if(lista.equals(null)){
		return null;
	}
	else
	return lista;
	
}

}
	/*
	public static boolean consultarRegistro(Entrada entrada, Trayectoria trayectoria) {
		boolean valor = new ValorEntradaTrayectoriaDAO().consultarRegistro(entrada.getId(),trayectoria.getId());
		return valor;
	}
}*/

