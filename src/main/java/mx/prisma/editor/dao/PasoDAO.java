package mx.prisma.editor.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class PasoDAO extends GenericDAO{

	public PasoDAO() {
		super();
	}
	public Paso consultarPaso(int id) {
		Paso paso = null;

		try {
			session.beginTransaction();
			paso = (Paso) session.get(Paso.class, id);
			if (paso != null) {
				paso.getReferencias().size();
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		return paso;
	}
	public List<ReferenciaParametro> obtenerReferencias(Integer id) throws HibernateException {		
		List<ReferenciaParametro> pasos = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery("from ReferenciaParametro where Pasoid= :id");
			query.setParameter("id", id);
			pasos = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return pasos;

	}
}
