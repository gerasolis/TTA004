package mx.prisma.generadorPruebas.bs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;


public class ValorDesconocidoBs {
	public static void crearArchivo(List<File> entradas, List<String> idAtributos, List<String> nombreArchivos) throws IOException{
		int i=0;
		//System.out.println("ARCHIVO: "+getUploadFileName());
		Entrada entrada_1 =new Entrada();
		List<Entrada> entradasBD = new ArrayList<Entrada>(); 
		for(String idAtributo : idAtributos){
			entrada_1= new ValorDesconocidoDAO().obtenerEntrada(idAtributo);
			System.out.println("entrada_1: "+entrada_1.getId());
			entradasBD.add(entrada_1);
		}
		for(File entrada : entradas){
			System.out.println("RUTA: "+entrada.getAbsolutePath());
			//File fichero_entrada = new File("/Users/enyamartinez/Desktop/Mi PRISMA/AplicacionTTB064-master/src/main/webapp/resources/archivos/desconocidos/"+nombreArchivos.get(i));
			File fichero_entrada = new File("/Users/gerasolis/Downloads/AplicacionTTB06408OCT2016/src/main/webapp/resources/archivos/"+nombreArchivos.get(i));
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
			System.out.println("Nueva ruta: "+nuevaRuta);
			//AQUÍ TENGO QUE LLAMAR AL DAO PARA INSERTAR LA NUEVA RUTA EN LA TABLA VALORDESCONOCIDO.
			if(new ValorDesconocidoDAO().consultarDesconocido(entradasBD.get(i))){//Si la entrada, ya está registrada, se borra.
				new ValorDesconocidoDAO().borrarRuta(entradasBD.get(i));
			}
			new ValorDesconocidoDAO().registraRuta(nuevaRuta,entradasBD.get(i),0);
			i++;
		}
	}
}
