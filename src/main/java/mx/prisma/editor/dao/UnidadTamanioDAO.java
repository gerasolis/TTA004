package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.UnidadTamanio;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class UnidadTamanioDAO extends GenericDAO {
	

	public UnidadTamanioDAO() {
		super();
	}

	public UnidadTamanio consultarUnidadTamanio(int identificador) {
		UnidadTamanio unidadTamanio = null;

		try {
			session.beginTransaction();
			unidadTamanio = (UnidadTamanio) session.get(UnidadTamanio.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return unidadTamanio;

	}
	
	@SuppressWarnings("unchecked")
	public UnidadTamanio consultarUnidadTamanio(String nombre) {
		List<UnidadTamanio> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from UnidadTamanio where nombre = :nombre");
 			query.setParameter("nombre", nombre);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results!=null && !results.isEmpty()){
			if (results.get(0) != null){
				return results.get(0);
			}
		}
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public UnidadTamanio consultarUnidadTamanioAbreviatura(String abreviatura) {
		List<UnidadTamanio> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from UnidadTamanio where abreviatura = :abreviatura");
 			query.setParameter("abreviatura", abreviatura);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results == null) {
			return null;
		} else 
			if (results.isEmpty()) {
				return null;
			} else
				return results.get(0);

	}

	@SuppressWarnings("unchecked")
	public List<UnidadTamanio> consultarUnidadesTamanio() {
		List<UnidadTamanio> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from UnidadTamanio");

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results == null) {
			return null;
		} else 
			if (results.isEmpty()) {
				return null;
			} else
				return results;

	}
}
