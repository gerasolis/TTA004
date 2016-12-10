package mx.prisma.editor.dao;


import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Actualizacion;

import org.hibernate.HibernateException;

public class ActualizacionDAO extends GenericDAO {

	public ActualizacionDAO() {
		super();
	}

	public void registrarActualizacion(Actualizacion actualizacion) throws Exception {

		try {
			session.beginTransaction();
			session.save(actualizacion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

	}
}
