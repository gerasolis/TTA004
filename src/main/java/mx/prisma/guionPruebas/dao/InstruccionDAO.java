package mx.prisma.guionPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Paso;
import mx.prisma.guionPruebas.model.GuionPrueba;
import mx.prisma.guionPruebas.model.Instruccion;
import mx.prisma.guionPruebas.model.Sinonimo;

public class InstruccionDAO extends GenericDAO{
	
	public InstruccionDAO() {
		super();
	}
	
	public Instruccion consultarInstruccion(int paso) {
		Instruccion instruccion = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Instruccion where Paso_idPaso = :idPaso");
			query.setParameter("idPaso", paso);
			//Si se encuentra en la lista de sinónimos
			if(!query.list().isEmpty()){
				instruccion = (Instruccion) query.list().get(0);
			}else{
				instruccion = null;
			}
			
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		return instruccion;
	}
	
	public void agregarInstruccion(Instruccion instruccion) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(instruccion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	public void delete(int id){
		try{
			session.beginTransaction();
			Query query = session
					.createQuery("delete from Instruccion where GuionPrueba_idGuionPrueba = :idGuionPrueba");
			query.setParameter("idGuionPrueba", id);
			query.executeUpdate();
			session.getTransaction().commit();
		}catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	
}
