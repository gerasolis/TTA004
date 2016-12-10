package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Parametro;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ParametroDAO extends GenericDAO{
	
	public ParametroDAO() {
		super();
	}

	public Parametro consultarParametro(int identificador) {
		Parametro Parametro = null;

		try {
			session.beginTransaction();
			Parametro = (Parametro) session.get(Parametro.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return Parametro;

	}
	
	@SuppressWarnings("unchecked")
	public Parametro consultarParametro(String nombre, int idProyecto) {
		List<Parametro> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Parametro where nombre = :nombre AND Proyectoid = :idProyecto");
 			query.setParameter("nombre", nombre);
 			query.setParameter("idProyecto", idProyecto);

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
	public List<Parametro> consultarParametros(int idProyecto) {
		List<Parametro> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Parametro where Proyectoid = :idProyecto");
 			query.setParameter("idProyecto", idProyecto);
			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results == null) {
			return null;
		} else
			return results;

	}
}
