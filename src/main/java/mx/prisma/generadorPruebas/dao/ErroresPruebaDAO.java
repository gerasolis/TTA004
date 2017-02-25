package mx.prisma.generadorPruebas.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;

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
	
	/*public List<ErroresPrueba> obtenerErrores(ErroresPrueba error){
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
					
		}*/
	public List<ErroresPrueba> consultarErrores(){
		List<ErroresPrueba> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ErroresPrueba");
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}

	public List<Pantalla> consultarPantallas(){
		List<Pantalla> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from Pantalla");
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	public List<Mensaje> consultarMensajes(){
		List<Mensaje> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from Mensaje");
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	public List<ValorMensajeParametro> consultarValorMensajeParametros(){
		List<ValorMensajeParametro> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ValorMensajeParametro");
			results = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	public List<ErroresPrueba> consultarErroresxCasoUso(CasoUso casoUso){
		List<ErroresPrueba> results = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from ErroresPrueba where CasoUsoid = :idCasoUso");
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
	public void modificarError(ErroresPrueba e){
		try{
			session.beginTransaction();
			Query query = session.createQuery("update from ErroresPrueba set Mensajeid = :idMensaje, Pasoid = :idPaso where id = :idError");
			query.setParameter("idError", e.getId());
			query.setParameter("idMensaje", e.getMensajeid());
			query.setParameter("idPaso", e.getPasoid());
			query.executeUpdate();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	/*
	public List<ErroresPrueba> consultarErroresCasosUso(Modulo modulo){
		List<ErroresPrueba> results = null;
		List<ErroresPrueba> results2 = new ArrayList<ErroresPrueba>();
		try{
			session.beginTransaction();
			//Query query = session.createSQLQuery("SELECT id,tipoError,numError,porcentaje,porcentajeTodo,CasoUsoid FROM ErroresPrueba, CasoUso where CasoUsoId = Elementoid and Moduloid = :idModulo");
			Query query = session.createSQLQuery("SELECT id,tipoError,numError,porcentaje,porcentajeTodo,CasoUsoid FROM ErroresPrueba LEFT JOIN CasoUso ON ErroresPrueba.CasoUsoId=CasoUso.Elementoid and CasoUso.Moduloid = :idModulo");
			query.setParameter("idModulo", modulo.getId());
			results = query.list();
			Iterator itr = results.iterator();
			while(itr.hasNext()){
			   Object[] obj = (Object[]) itr.next();
			   ErroresPrueba error = new ErroresPrueba();
			   error.setId(Integer.parseInt(String.valueOf(obj[0])));
			   error.setTipoError(String.valueOf(obj[1]));
			   error.setNumError(Integer.parseInt(String.valueOf(obj[2])));
			   error.setPorcentaje(Double.valueOf(String.valueOf(obj[3])));
			   error.setPorcentajeTodo(Double.valueOf(String.valueOf(obj[4])));
			   List<CasoUso> c = null;
			   query = session.createQuery("from CasoUso where Elementoid = :idCasoUso");
			   query.setParameter("idCasoUso", Integer.parseInt(String.valueOf(obj[5])));
			   c = query.list();
			   error.setCasoUsoid(c.get(0));
			   results2.add(error);
			}
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results2;
	}*/
	public List<CasoUso> consultarCasosUso(Modulo modulo){
		 List<CasoUso> c = null;
		try{
			session.beginTransaction();
			Query query = session.createQuery("from CasoUso where Moduloid = :idModulo");
			query.setParameter("idModulo", modulo.getId());
			c = query.list();
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return c;
	}
	/*
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
	}*/
}
