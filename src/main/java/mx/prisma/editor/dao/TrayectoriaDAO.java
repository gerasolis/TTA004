package mx.prisma.editor.dao;


import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Actualizacion;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Paso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class TrayectoriaDAO extends GenericDAO {

	public TrayectoriaDAO() {
		super();
	}


	public void registrarTrayectoria(Trayectoria trayectoria) {
		try {
			session.beginTransaction();
			session.save(trayectoria);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
//	public void modificarTrayectoria(Trayectoria trayectoria, Actualizacion actualizacion) {
//		try {
//			session.beginTransaction();			
// 			session.saveOrUpdate(trayectoria);
//			session.save(actualizacion);
//			session.getTransaction().commit();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			session.getTransaction().rollback();
//			throw he;
//		}
//	}
	
	public void modificarTrayectoria(Trayectoria trayectoria) {
		try {
			session.beginTransaction();			
 			session.saveOrUpdate(trayectoria);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public Trayectoria consultarTrayectoria(int id) throws HibernateException {		
		Trayectoria trayectoria = null;
		try {
			session.beginTransaction();
			trayectoria = (Trayectoria)session.get(Trayectoria.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return trayectoria;

	}
	
	public void eliminarTrayectoria(Trayectoria trayectoria) {

		try {
			session.beginTransaction();
			session.delete(trayectoria);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public void modificarEstado(Trayectoria trayectoria){

		try {
			System.out.println("Trayectoria de guardar DAO: " + trayectoria.getCondicion());

			session.beginTransaction();
			session.update(trayectoria);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	@SuppressWarnings("unchecked")
	public List<Trayectoria> consultarTrayectoriaxCasoUso(CasoUso idCaso){
		List<Trayectoria> listTrayectoria = null;
		Trayectoria trayectoria = null;
		try{
			System.out.println("Id caso: "+idCaso.getId());
			session.beginTransaction();
			
		    Query query = session.createQuery("from Trayectoria where CasoUsoElementoid= :idCaso");
		    query.setParameter("idCaso", idCaso.getId());
		    listTrayectoria =  query.list();
			session.getTransaction().commit();
		   
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		 return listTrayectoria;
		 //return trayectoria;
	}
	
	public List<Paso> obtenerPasos(Integer id) throws HibernateException {		
		List<Paso> pasos = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Paso where Trayectoriaid= :id");
			query.setParameter("id", id);
			pasos = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return pasos;

	}
	public List<Paso> obtenerPasos_(Integer id) throws HibernateException {		
		List<Paso> pasos = null;
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Paso where Trayectoriaid= :id order by numero");
			query.setParameter("id", id);
			pasos = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return pasos;

	}
	

}
