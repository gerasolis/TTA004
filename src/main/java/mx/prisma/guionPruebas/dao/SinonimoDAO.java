package mx.prisma.guionPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Verbo;
import mx.prisma.guionPruebas.model.Sinonimo;

public class SinonimoDAO extends GenericDAO{
	
	public SinonimoDAO() {
		super();
	}
	
	//Función para saber si la palabra asignada como "Otro" se encuentra la lista de sinónimos
	@SuppressWarnings("unchecked")
	public boolean esSinonimo(String sinonimo){
		boolean result = false;
		try {
			//HAcemos la consulta
			session.beginTransaction();
			Query query = session
					.createQuery("from Sinonimo where sinonimo = '"+sinonimo+"'");
 			
 			//Si no se encuentra en la lista de sinónimos
			if(query.list().isEmpty()){
				System.out.println("NO ENCONTRÓ SINÓNIMO");
				result = false;
			//Si se encuentra en la lista de sinónimos
			}else{
				System.out.println("ENCONTRÓ SINÓNIMO");
				result = true;
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		//Retornamos el resultado
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Sinonimo consultarSinonimo(String sinonimo){
		Sinonimo result = new Sinonimo();
		try {
			//HAcemos la consulta
			session.beginTransaction();
			Query query = session
					.createQuery("from Sinonimo where sinonimo = '"+sinonimo+"'");

 			//Si se encuentra en la lista de sinónimos
			if(!query.list().isEmpty()){
				result = (Sinonimo) query.list().get(0);
			}else{
				result = null;
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		//Retornamos el resultado
		return result;
	}
}
