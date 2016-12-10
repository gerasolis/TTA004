package mx.prisma.admin.dao;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.dao.GenericDAO;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ProyectoDAO extends GenericDAO {
	

	public ProyectoDAO() {
		super();
	}
	
	public void registrarProyecto(Proyecto proyecto) {
		try {
			session.beginTransaction();
			session.save(proyecto);
			for(ColaboradorProyecto colaborador : proyecto.getProyecto_colaboradores()){
				session.save(colaborador);
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public void modificarProyecto(Proyecto proyecto){
		try {
			session.beginTransaction();
			session.update(proyecto);
			for(ColaboradorProyecto colaborador : proyecto.getProyecto_colaboradores()){
				session.saveOrUpdate(colaborador);
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}	
	}

	public Proyecto consultarProyecto(int id) {
		Proyecto proyecto = null;

		try {
			session.beginTransaction();
			proyecto = (Proyecto) session.get(Proyecto.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
		return proyecto;

	}

	@SuppressWarnings("unchecked")
	public Proyecto consultarProyecto(String clave){
		List<Proyecto> proyectos = null;
		

		try {
			session.beginTransaction();
			Query query = session.createQuery("from Proyecto where clave = :clave");
			query.setParameter("clave", clave);
			proyectos = query.list();			
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
		if(proyectos.isEmpty()){
			return null;
		} else {
			return proyectos.get(0);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Proyecto> consultarProyectos() {
		List<Proyecto> proyectos = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Proyecto");
			proyectos = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (proyectos == null) {
			return null;
		} else {
			return proyectos;
		}
	}

	public void eliminarProyecto(Proyecto model) {
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

	public List<Proyecto> findByColaborador(String idColaborador) {
		List<Proyecto> proyectos = null;
		Query queryObject = null;
		String queryString = "select proy from Proyecto as proy, IN (proy.proyecto_colaboradores) AS colab WHERE colab.colaborador.id = :idColaborador";

		proyectos = new ArrayList<Proyecto>();
		
		try {
			session.beginTransaction();
			queryObject = session.createQuery(queryString);
			queryObject.setParameter("idColaborador", idColaborador);
			for (Object object : queryObject.list()) {
				proyectos.add((Proyecto) object);
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return proyectos;
	}

}

