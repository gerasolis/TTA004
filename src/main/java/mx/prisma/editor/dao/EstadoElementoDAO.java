package mx.prisma.editor.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.EstadoElemento;

import org.hibernate.HibernateException;

public class EstadoElementoDAO extends GenericDAO {
	
	public EstadoElementoDAO() {
		super();
	}

	public EstadoElemento consultarEstadoElemento(int identificador) {
		EstadoElemento estadoElemento = null;

		try {
			session.beginTransaction();
			estadoElemento = (EstadoElemento) session.get(EstadoElemento.class,
					identificador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return estadoElemento;

	}
}
