package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.TipoReglaNegocio;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class TipoReglaNegocioDAO extends GenericDAO{
	

	public TipoReglaNegocioDAO() {
		super();
	}

	public TipoReglaNegocio consultarTipoReglaNegocio(int identificador) {
		TipoReglaNegocio tipoRN = null;

		try {
			session.beginTransaction();
			tipoRN = (TipoReglaNegocio) session.get(TipoReglaNegocio.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return tipoRN;

	}
	
	@SuppressWarnings("unchecked")
	public TipoReglaNegocio consultarTipoReglaNegocio(String nombre) {
		List<TipoReglaNegocio> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoDTipoReglaNegocioato where nombre = :nombre");
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
	public List<TipoReglaNegocio> consultarTiposReglaNegocio() {
		List<TipoReglaNegocio> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoReglaNegocio");

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
