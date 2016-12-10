package mx.prisma.generadorPruebas.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.ReferenciaParametro;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class QueryDAO extends GenericDAO {

	public void registrarQuery(mx.prisma.generadorPruebas.model.Query valor) {
		try {
			session.beginTransaction();
			session.save(valor);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

	public List<mx.prisma.generadorPruebas.model.Query> findByReferenciaParametro(ReferenciaParametro referencia) {
		List<mx.prisma.generadorPruebas.model.Query> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Query where referenciaParametro = :referencia");
			query.setParameter("referencia", referencia);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results == null) {
			return null;
		} else if (results.isEmpty()) {
			return null;
		} else {
			return results;
		}
	}

	public mx.prisma.generadorPruebas.model.Query findById(Integer id) {
		mx.prisma.generadorPruebas.model.Query valor = null;
		try {
			session.beginTransaction();
			valor = (mx.prisma.generadorPruebas.model.Query) session.get(mx.prisma.generadorPruebas.model.Query.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return valor;
	}
}
