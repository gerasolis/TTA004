package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.TipoDato;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class TipoDatoDAO extends GenericDAO{
	

	public TipoDatoDAO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public TipoDato consultarTipoDato(int id) {
		List<TipoDato> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoDato where id = :id");
 			query.setParameter("id", id);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results!=null){
			if (results.get(0) != null){
				return results.get(0);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TipoDato consultarTipoDato(String nombre) {
		List<TipoDato> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoDato where nombre = :nombre");
 			query.setParameter("nombre", nombre);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results!=null){
			if (results.get(0) != null){
				return results.get(0);
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<TipoDato> consultarTiposDato() {
		List<TipoDato> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoDato");

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
