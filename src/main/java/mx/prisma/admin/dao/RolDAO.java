package mx.prisma.admin.dao;

import mx.prisma.admin.model.Rol;
import mx.prisma.dao.GenericDAO;

import org.hibernate.HibernateException;

public class RolDAO extends GenericDAO {
	

	public RolDAO() {
		super();
	}
	public Rol consultarRol(Integer id) {
		Rol rol = null;

		try {
			session.beginTransaction();
			rol = (Rol) session.get(Rol.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
		return rol;

	}
}
