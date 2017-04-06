package mx.prisma.guionPruebas.bs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.EntradaDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ValorEntrada;


public class ValorEntradaBs {
	
	public static void guardarValores(List<File> entradas, List<String> idAtributos, List<String> nombreArchivos, int tipoEntrada) throws IOException{
		//System.out.println("TOTAL ENTRADAS: "+entradas.size());
		List<Entrada> entradasBD = new ArrayList<Entrada>();
		//Lee la lista de archivos
		for(int i=0; i<entradas.size(); i++){
			//Obtenemos la entrada a la que se le va a asignar el valor 
			EntradaDAO edao = new EntradaDAO();
			Entrada e = edao.obtenerEntrada(idAtributos.get(i));
			//Obtenemos el archivo en la posición del contador
			File entrada = entradas.get(i);
			//Leemos el archivo 
			String cadena;
			FileReader f = new FileReader(entrada);
			BufferedReader b = new BufferedReader(f);
			//Mientras exista una línea nueva en el archivo 
			while((cadena = b.readLine())!=null){
				//Comparamos el tipo de entrada que es
				//Valor NO GENERABLE
				//public ValorEntrada(Entrada entrada, ReglaNegocio reglaNegocio, String valor, Boolean valido,
				//	Boolean correcto, Boolean registrado, Boolean aleatorio, Boolean insercion, Boolean modificacion)
				//Si no es válildo por regla debe tener asociada una RN
				if(tipoEntrada == 1){
					
				}
				//Valor CORRECTO(INSERCIÓN) true en insercion_modificacion
				else if(tipoEntrada == 2){
					ValorEntrada v = new ValorEntrada(e, cadena, true, true, true, false, true, false);
					ValorEntradaDAO vedao = new ValorEntradaDAO();
					vedao.registrarValorEntrada(v);
				}
				//Valor CORRECTO(MODIFICACIÓN) false en insercion_modificacion
				else if(tipoEntrada == 3){
					ValorEntrada v = new ValorEntrada(e, cadena, true, true, true, false, false, true);
					ValorEntradaDAO vedao = new ValorEntradaDAO();
					vedao.registrarValorEntrada(v);
				}
				//Valor INCORRECTO
				else if(tipoEntrada == 4){
					ValorEntrada v = new ValorEntrada(e, cadena, true, false, true, false, false, false);
					ValorEntradaDAO vedao = new ValorEntradaDAO();
					vedao.registrarValorEntrada(v);
				}
			}
		}	
	}
	
	public static void crearArchivo(List<File> entradas, List<String> idAtributos, List<String> nombreArchivos) throws IOException{
		int i=0;
		////System.out.println("ARCHIVO: "+getUploadFileName());
		//System.out.println("TOTAL ENTRADAS: "+entradas.size());
		Entrada entrada_1 =new Entrada();
		List<Entrada> entradasBD = new ArrayList<Entrada>(); 
		for(String idAtributo : idAtributos){
			entrada_1= new ValorDesconocidoDAO().obtenerEntrada(idAtributo);
			//System.out.println("entrada_1: "+entrada_1.getId());
			entradasBD.add(entrada_1);
		}
		for(File entrada : entradas){
			//System.out.println("RUTA: "+entrada.getAbsolutePath());
			File fichero_entrada = new File("/Users/enyamartinez/Desktop/Mi PRISMA/AplicacionTTB064-master/src/main/webapp/resources/archivos/guion/"+nombreArchivos.get(i));
			InputStream in = new FileInputStream(entrada);
			FileOutputStream out = new FileOutputStream(fichero_entrada);
			
			byte[] buf = new byte[1024];
			int len;
			 
			while ((len = in.read(buf)) > 0) {
			  out.write(buf, 0, len);
			}
			
			in.close();
			out.close();
			String nuevaRuta=fichero_entrada.getAbsolutePath();
			//System.out.println("Nueva ruta: "+nuevaRuta);
			//AQUÍ TENGO QUE LLAMAR AL DAO PARA INSERTAR LA NUEVA RUTA EN LA TABLA VALORDESCONOCIDO.
			if(new ValorDesconocidoDAO().consultarDesconocido(entradasBD.get(i))){//Si la entrada, ya está registrada, se borra.
				new ValorDesconocidoDAO().borrarRuta(entradasBD.get(i));
			}
			new ValorDesconocidoDAO().registraRuta(nuevaRuta,entradasBD.get(i),1);
			i++;
		}
	}

	public static void cambiarStatus(List<String> valor, CasoUso casoUso) {
		Set<Entrada> entradas = casoUso.getEntradas();
		int i=0;
		for(Entrada entrada : entradas){
			ValorEntradaDAO vedao = new ValorEntradaDAO();
			for(ValorEntrada ve : vedao.consultarValores(entrada)){
				//System.out.println(ve.getValor()+"-"+valor.get(i));
				if(ve.getValor().equals(valor.get(i))){
					ve.setSeleccionada(true);
				}else{
					ve.setSeleccionada(false);
				}
				vedao = new ValorEntradaDAO();
				vedao.registrarValorEntrada(ve);
			}
			i++;
		}
	}
}
