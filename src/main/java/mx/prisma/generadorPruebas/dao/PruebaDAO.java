package mx.prisma.generadorPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;

public class PruebaDAO extends GenericDAO{
	public void registrarPrueba(Prueba prueba) {
		try {
			session.beginTransaction();	
			session.saveOrUpdate(prueba);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}

	public void modificarReporte(Prueba prueba){
		try{
			session.beginTransaction();
			Query query = session.createQuery("update from CasoUso set reporte = 1 where Elementoid = :idCasoUso");
			query.setParameter("idCasoUso", prueba.getCasoUsoid());
			query.executeUpdate();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public List<Prueba> consultarPruebasxCasoUso(CasoUso casoUso){
		List<Prueba> results = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Prueba where CasoUsoid = :idCasoUso");
			query.setParameter("idCasoUso", casoUso.getId());
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
	}
	
	public List<Prueba> consultarPruebas(){
		List<Prueba> results = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from Prueba");
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
	}
}
