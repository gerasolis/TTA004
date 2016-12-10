package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.bs.ReferenciaEnum.TipoSeccion;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Extension;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ExtensionDAO extends GenericDAO {

	public ExtensionDAO() {
		super();
	}

	public void registrarExtension(Extension extension) {

		try {
			session.beginTransaction();
			session.save(extension);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}

	public Extension consultarExtension(int id) {
		Extension extension = null;

		try {
			session.beginTransaction();
			extension = (Extension) session.get(Extension.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}

		return extension;

	}

	@SuppressWarnings("unchecked")
	public List<Extension> consultarReferencias(CasoUso casoUso) {
		List<Extension> results = null;
		Query query = null;
		String queryCadena = null;
		queryCadena = "FROM Extension WHERE casoUsoDestino.id = :idCasoUso";

		try {
			session.beginTransaction();
			query = session.createQuery(queryCadena);
			query.setParameter("idCasoUso", casoUso.getId());
			results = query.list();
			session.getTransaction().commit();

		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}

		return results;
	}

	public void modificarExtension(Extension extension) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(extension);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

	public void eliminarExtension(Extension model) {
		try {
			session.beginTransaction();
			session.delete(model);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}	
		
	}
}
