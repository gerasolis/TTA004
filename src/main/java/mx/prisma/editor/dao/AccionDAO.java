package mx.prisma.editor.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Extension;
import mx.prisma.editor.model.Pantalla;

public class AccionDAO extends GenericDAO {

	public AccionDAO() {
		super();
	}

	public void registrarAccion(Accion accion) throws Exception {

		try {
			session.beginTransaction();
			session.save(accion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

	}

	public Accion consultarAccion(int id) {
		Accion accion = null;

		try {
			session.beginTransaction();
			accion = (Accion) session.get(Accion.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		return accion;

	}
	
	@SuppressWarnings("unchecked")
	public Accion consultarAccion(String nombre, Pantalla pantalla) {
		List<Accion> results  = null;

		try {
			session.beginTransaction();
			Query query = session.createQuery("from Accion where nombre = :nombre AND PantallaElementoid = :pantalla");
			query.setParameter("nombre", nombre);
			query.setParameter("pantalla", pantalla.getId());
			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		if (results.isEmpty()){
			return null;
		} else 
			return results.get(0);

	}

	public void modificarAccion(Accion accion) {
		try {
			session.beginTransaction();
			session.update(accion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

	public List<Accion> consultarReferencias(Pantalla pantalla) {
		List<Accion> results = null;
		Query query = null;
		String queryCadena = null;
		queryCadena = "FROM Accion WHERE pantallaDestino.id = :idPantalla";

		try {
			session.beginTransaction();
			query = session.createQuery(queryCadena);
			query.setParameter("idPantalla", pantalla.getId());
			results = query.list();
			session.getTransaction().commit();

		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}

		return results;
	}

}
