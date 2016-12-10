package mx.prisma.editor.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Entidad;
import mx.prisma.util.HibernateUtil;

public class AtributoDAO extends GenericDAO {

	public AtributoDAO() {
		super();
	}

	public void registrarAtributo(Atributo atributo) throws Exception {

		try {
			session.beginTransaction();
			session.save(atributo);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

	}

	public Atributo consultarAtributo(int id) {
		Atributo atributo = null;

		try {
			session.beginTransaction();
			atributo = (Atributo) session.get(Atributo.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		return atributo;

	}
	
	@SuppressWarnings("unchecked")
	public Atributo consultarAtributo(String nombre, Entidad entidad) {
		List<Atributo> results  = null;

		try {
			session.beginTransaction();
			Query query = session.createQuery("from Atributo where nombre = :nombre AND EntidadElementoid = :entidad");
			query.setParameter("nombre", nombre);
			query.setParameter("entidad", entidad.getId());
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

}
