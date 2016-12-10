package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.PostPrecondicion;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class PostPrecondicionDAO extends GenericDAO{

	public PostPrecondicionDAO() {
		super();
	}

	public void registrarPostPrecondicion(PostPrecondicion postprecondicion) {
		try {
			session.beginTransaction();
			session.save(postprecondicion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
	}

	@SuppressWarnings("unchecked")
	public List<PostPrecondicion> consultarPostPrecondiciones(CasoUso casodeuso, boolean precondicion) {
		List<PostPrecondicion> postPrecondiciones  = null;

		try {
			session.beginTransaction();
			Query query = session.createQuery("from PostPrecondicion where CasoUsoElementoid = "+casodeuso.getId()+"");
			postPrecondiciones = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return postPrecondiciones;

	}
	
}
