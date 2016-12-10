package mx.prisma.admin.dao;

import java.util.List;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.dao.GenericDAO;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ColaboradorDAO extends GenericDAO {
	
	public ColaboradorDAO() {
		super();
	}

	public void registrarColaborador(Colaborador colaborador) {
		try {
			session.beginTransaction();
			session.save(colaborador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}

	public void modificarColaborador(Colaborador colaborador) {
		try {
			session.beginTransaction();
			session.update(colaborador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}		
	}
	
	public Colaborador consultarColaborador(String curp) {
		Colaborador colaborador = null;

		try {
			session.beginTransaction();
			colaborador = (Colaborador) session.get(Colaborador.class, curp);
			if (colaborador != null)
				colaborador.getColaborador_proyectos().size();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return colaborador;

	}
	
	public void eliminarColaborador(Colaborador colaborador) {
		try {
			session.beginTransaction();
			session.delete(colaborador);
			
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}		
	}

	@SuppressWarnings("unchecked")
	public List<Colaborador> consultarColaboradores() {
		List<Colaborador> colaboradores = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Colaborador");
			colaboradores = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (colaboradores == null) {
			return null;
		} else {
			return colaboradores;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Colaborador consultarColaboradorCorreo(String correo) {
		List<Colaborador> colaboradores = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery("FROM Colaborador WHERE correoElectronico = :correo");
			query.setParameter("correo", correo);
			colaboradores = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (colaboradores == null || colaboradores.isEmpty()) {
			return null;
		} else {
			return colaboradores.get(0);
		} 
	}
}
