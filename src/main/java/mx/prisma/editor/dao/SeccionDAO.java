package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Seccion;
import mx.prisma.editor.model.TipoAccion;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class SeccionDAO extends GenericDAO{
	

	public SeccionDAO() {
		super();
	}

	public Seccion consultarSeccion(int id) {
		Seccion seccion = null;
		try {
			session.beginTransaction();
			seccion = (Seccion) session.get(Seccion.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return seccion;
	}
	
	@SuppressWarnings("unchecked")
	public Seccion consultarSeccion(String nombre) {
		List<Seccion> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Seccion where nombre = :nombre");
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
