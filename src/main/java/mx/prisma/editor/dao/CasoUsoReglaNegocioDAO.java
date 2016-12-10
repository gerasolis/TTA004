package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.CasoUsoReglaNegocio;
import mx.prisma.editor.model.ReglaNegocio;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class CasoUsoReglaNegocioDAO extends GenericDAO{
	
	public CasoUsoReglaNegocioDAO(){
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<CasoUsoReglaNegocio> consultarReferencias(ReglaNegocio reglaNegocio) {
		List<CasoUsoReglaNegocio> results = null;
		Query query = null;
		String queryCadena = null;

		queryCadena = "FROM CasoUsoReglaNegocio WHERE reglaNegocio.id = :idReglaNegocio";

		try {
			session.beginTransaction();
			query = session.createQuery(queryCadena);
			query.setParameter("idReglaNegocio", reglaNegocio.getId());
			results = query.list();
			session.getTransaction().commit();

		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}

		return results;
	}
}
