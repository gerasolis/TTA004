package mx.prisma.editor.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Revision;

import org.hibernate.HibernateException;

public class RevisionDAO extends GenericDAO {
	public RevisionDAO() {
		super();
	}

	public void update(Revision revision) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(revision);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public void delete(Revision revision) {
		try {
			session.beginTransaction();
			session.delete(revision);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
}
