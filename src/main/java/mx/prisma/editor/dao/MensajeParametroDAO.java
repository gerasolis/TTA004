package mx.prisma.editor.dao;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.util.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;

public class MensajeParametroDAO extends GenericDAO {
	
	
	public MensajeParametroDAO() {
		super();
	}

	public MensajeParametro findById(Integer id) {
		MensajeParametro mensajeParametro = null;

		try {
			session.beginTransaction();
			mensajeParametro = (MensajeParametro) session.get(MensajeParametro.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return mensajeParametro;
	}
	
}
