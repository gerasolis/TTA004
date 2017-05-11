package mx.prisma.guionPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.guionPruebas.model.VerboSinonimo;

public class VerboSinonimoDAO extends GenericDAO{
	
	public VerboSinonimoDAO() {
		super();
	}
	
	//Función para saber a qué verbos pertenece la palabra asignada a "Otro"
	@SuppressWarnings("unchecked")
	public List<VerboSinonimo> verbosSimilares(int idSinonimo){
		List<VerboSinonimo> verbos = null;
			
		try {
			//HAcemos la consulta
			session.getSessionFactory().openSession();
			session.beginTransaction();
			Query query = session
					.createQuery("from VerboSinonimo where sinonimo_idSinonimo ="+idSinonimo);
			
			//Asignamos los resultados encontrados a la lista de verbos
	 		verbos = query.list();
	 		
	 		session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
			
		//Retornamos la lista de verbos
		return verbos;
	}
	
}
