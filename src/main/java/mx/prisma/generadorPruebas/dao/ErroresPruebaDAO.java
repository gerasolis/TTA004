package mx.prisma.generadorPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.generadorPruebas.model.ErroresPrueba;

public class ErroresPruebaDAO extends GenericDAO{
	public void registrarError(ErroresPrueba error) {
		try {
			session.beginTransaction();	
			session.saveOrUpdate(error);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public List<ErroresPrueba> obtenerErrores(ErroresPrueba error){
		List<ErroresPrueba> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ErroresPrueba where CasoUsoid = :idCasoUso");
			query.setParameter("idCasoUso", error.getCasoUsoid());
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	public void eliminarErrores(ErroresPrueba error){
		try{
			session.beginTransaction();
			Query query = session.createQuery("delete from ErroresPrueba where CasoUsoid = :idCasoUso");
			query.setParameter("idCasoUso", error.getCasoUsoid());
			query.executeUpdate();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public void modificarReporte(ErroresPrueba error){
		try{
			session.beginTransaction();
			Query query = session.createQuery("update from CasoUso set reporte = 1 where Elementoid = :idCasoUso");
			query.setParameter("idCasoUso", error.getCasoUsoid());
			query.executeUpdate();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
}
