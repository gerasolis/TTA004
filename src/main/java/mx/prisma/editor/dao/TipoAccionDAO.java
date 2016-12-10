package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.TipoAccion;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class TipoAccionDAO extends GenericDAO{
	

	public TipoAccionDAO() {
		super();
	}

	public TipoAccion consultarTipoAccion(int id) {
		TipoAccion tipoAccion = null;
		try {
			session.beginTransaction();
			tipoAccion = (TipoAccion) session.get(TipoAccion.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return tipoAccion;
	}
	
	@SuppressWarnings("unchecked")
	public TipoAccion consultarTipoAccion(String nombre) {
		List<TipoAccion> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoAccion where nombre = :nombre");
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
	public List<TipoAccion> consultarTiposAccion() {
		List<TipoAccion> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoAccion");

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
