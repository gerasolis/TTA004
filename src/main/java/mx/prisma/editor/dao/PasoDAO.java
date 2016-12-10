package mx.prisma.editor.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Paso;

import org.hibernate.HibernateException;

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
}
