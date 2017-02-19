package mx.prisma.guionPruebas.bs;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.editor.model.Verbo;
import mx.prisma.guionPruebas.dao.SinonimoDAO;
import mx.prisma.guionPruebas.dao.VerboSinonimoDAO;
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
	
}
