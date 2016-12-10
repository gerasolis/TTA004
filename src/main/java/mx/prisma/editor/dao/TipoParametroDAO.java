package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.TipoParametro;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class TipoParametroDAO extends GenericDAO{
	

	public TipoParametroDAO() {
		super();
	}

	public TipoParametro consultarTipoParametro(int identificador) {
		TipoParametro tipoParametro = null;

		try {
			session.beginTransaction();
			tipoParametro = (TipoParametro) session.get(TipoParametro.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return tipoParametro;

	}
	
	@SuppressWarnings("unchecked")
	public TipoParametro consultarTipoParametro(String nombre) {
		List<TipoParametro> results = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from TipoParametro where nombre = :nombre");
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
}
