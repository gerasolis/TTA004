package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Verbo;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class VerboDAO extends GenericDAO{
	

	public VerboDAO() {
		super();
	}

	public Verbo consultarVerbo(int identificador) {
		Verbo tipoParametro = null;

		try {
			session.beginTransaction();
			tipoParametro = (Verbo) session.get(Verbo.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return tipoParametro;

	}
	
	@SuppressWarnings("unchecked")
	public Verbo consultarVerbo(String nombre) {
		List<Verbo> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Verbo where nombre = :nombre");
 			query.setParameter("nombre", nombre);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results!=null){
			if (results.isEmpty()){
				return null;
			} else 
				if (results.get(0) != null){
				return results.get(0);
			}
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<Verbo> consultarVerbos() {
		List<Verbo> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Verbo");

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results==null){
				return null;
			} else
		
		return results;

	}
}
