package mx.prisma.admin.dao;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.RolBs;
import mx.prisma.bs.RolBs.Rol_Enum;
import mx.prisma.dao.GenericDAO;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ColaboradorProyectoDAO extends GenericDAO {
	

	public ColaboradorProyectoDAO() {
		super();
	}	
	
	public void registrarColaboradorProyecto(ColaboradorProyecto colaborador) {
		try {
			session.beginTransaction();
			session.save(colaborador);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	
	public ColaboradorProyecto consultarColaboradorProyecto(int id) {
		ColaboradorProyecto colaboradorProyecto = null;

		try {
			session.beginTransaction();
			colaboradorProyecto = (ColaboradorProyecto) session.get(ColaboradorProyecto.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return colaboradorProyecto;

	}
	
	@SuppressWarnings("unchecked")
	public List<ColaboradorProyecto> consultarLiderColaboradoresProyecto(Colaborador colaborador) {
		List<ColaboradorProyecto> colaboradoresProyecto = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ColaboradorProyecto"
							+ " where colaborador.curp = :curp AND rol.id = :idLider");
			query.setParameter("curp", colaborador.getCurp());
			query.setParameter("idLider", RolBs.consultarIdRol(Rol_Enum.LIDER));
			colaboradoresProyecto = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (colaboradoresProyecto == null) {
			return null;
		} else {
			return colaboradoresProyecto;
		}
	}
	public ColaboradorProyecto findByColaborador_Proyecto(
			Colaborador colaborador, Proyecto proyecto) {
		
		List<ColaboradorProyecto> results = null;
		
		try {
			session.beginTransaction();
			Query query = session.createQuery("from ColaboradorProyecto as colProy where colProy.proyecto.id = :id  and colProy.colaborador.curp = :curp");
			query.setParameter("id", proyecto.getId());
			query.setParameter("curp", colaborador.getCurp());
			results = new ArrayList<ColaboradorProyecto>();
			for(Object o : query.list()) {
				results.add((ColaboradorProyecto) o);
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			session.getTransaction().rollback();
			throw he;
		}
		
		
		if(results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

	public void modificarColaboradorProyecto(
			ColaboradorProyecto colaboradorproyecto) {
		try {
			session.beginTransaction();
			session.update(colaboradorproyecto);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
	}
	
}
