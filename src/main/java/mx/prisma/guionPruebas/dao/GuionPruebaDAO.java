package mx.prisma.guionPruebas.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.guionPruebas.model.GuionPrueba;
import mx.prisma.guionPruebas.model.Sinonimo;

import java.util.List;

public class GuionPruebaDAO extends GenericDAO{
	
	public GuionPruebaDAO() {
		super();
	}
	
	public void insert(GuionPrueba guionprueba){
		try {
			session.beginTransaction();
			session.saveOrUpdate(guionprueba);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public void update(GuionPrueba guionprueba){
		try {
			session.beginTransaction();
			session.update(guionprueba);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
	public GuionPrueba select(int id){
		GuionPrueba guionprueba;
		
		try{
			session.beginTransaction();
			Query query = session
					.createQuery("from GuionPrueba where CasoUsoElementoid = :idCU");
			query.setParameter("idCU", id);
			//Si se encuentra en la lista de sinónimos
			if(!query.list().isEmpty()){
				guionprueba = (GuionPrueba) query.list().get(0);
			}else{
				guionprueba = null;
			}
			session.getTransaction().commit();
		}catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
		return guionprueba;
	}
	
	public List<GuionPrueba> selectAll(){
		List<GuionPrueba> guionprueba;
		
		try{
			session.beginTransaction();
			Query query = session
					.createQuery("from GuionPrueba");
			//Si se encuentra en la lista de sinónimos
			if(!query.list().isEmpty()){
				guionprueba = query.list();
			}else{
				guionprueba = null;
			}
			session.getTransaction().commit();
		}catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
		return guionprueba;
	}
}
