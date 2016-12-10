package mx.prisma.generadorPruebas.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.generadorPruebas.model.Escenario;

import org.hibernate.HibernateException;

public class EscenarioDAO extends GenericDAO {

	public void registrarEscenario(Escenario escenario) {
		try {
			session.beginTransaction();
			session.save(escenario);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
}
