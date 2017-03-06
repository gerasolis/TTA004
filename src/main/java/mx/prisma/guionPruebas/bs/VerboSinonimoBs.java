package mx.prisma.guionPruebas.bs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.prisma.editor.model.Verbo;
import mx.prisma.guionPruebas.dao.InstruccionDesconocidaDAO;
import mx.prisma.guionPruebas.dao.SinonimoDAO;
import mx.prisma.guionPruebas.dao.VerboSinonimoDAO;
import mx.prisma.guionPruebas.model.InstruccionDesconocida;
import mx.prisma.guionPruebas.model.Sinonimo;

public class VerboSinonimoBs {
	
	public static boolean esSinonimo (String sinonimo){
		SinonimoDAO sdao = new SinonimoDAO();
		return sdao.esSinonimo(sinonimo);
	}
	
	public static Sinonimo sinonimos (String sinonimo){
		SinonimoDAO sdao = new SinonimoDAO();
		
		//Consultamos el sinonimo
		Sinonimo s = sdao.consultarSinonimo(sinonimo);
		
		//Retornamos la lista con los verbos similares
		return s;
	}
	
	public static List<Verbo> verbos(Sinonimo s){
		List<Verbo> verbosSimilares = new ArrayList<Verbo>();
		VerboSinonimoDAO vsdao = new VerboSinonimoDAO();
		
		//Obtenemos los verbos similares
		verbosSimilares = vsdao.verbosSimilares(s.getIdSinonimo());
		
		return verbosSimilares;		
	}

	public static void agregarInstruccion(List<File> upload, List<String> uploadFileName, Integer idPaso, Integer idSinonimo) throws IOException {
		for(int i=0; i<upload.size(); i++){
			//Obtenemos el archivo en la posición del contador
			File archivo = upload.get(i);
			//Leemos el archivo 
			String cadena;
			FileReader f;
			try {
				f = new FileReader(archivo);
				BufferedReader b = new BufferedReader(f);
				//Mientras exista una línea nueva en el archivo
				while((cadena = b.readLine())!=null){
					InstruccionDesconocida id = new InstruccionDesconocida(cadena, idSinonimo, idPaso);
					InstruccionDesconocidaDAO iddao = new InstruccionDesconocidaDAO();
					iddao.agregarInstruccion(id);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
}
