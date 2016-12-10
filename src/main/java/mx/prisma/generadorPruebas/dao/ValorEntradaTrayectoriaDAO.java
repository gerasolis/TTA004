package mx.prisma.generadorPruebas.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;

public class ValorEntradaTrayectoriaDAO extends GenericDAO {

	public void registrarValorEntradaTrayectoria(ValorEntradaTrayectoria valor) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(valor);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	public void registrarValorEntradaTrayectoria(Set<ValorEntradaTrayectoria> valores) {
		
		try {
		session.beginTransaction();
		for(ValorEntradaTrayectoria valor: valores){
		
			
			session.saveOrUpdate(valor);
			session.getTransaction().commit();
		}
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	
	public void registrarValorEntradaTrayectoria(Entrada valor) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(valor);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

	public List<ValorEntrada> consultarValores(Entrada entrada) {
		List<ValorEntrada> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntrada where entrada = :entrada");
			query.setParameter("entrada", entrada);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
		if(results == null) {
			return null;
		} else if (results.isEmpty()) {
			return null;
		} else {
			return results;
		}
	}

	public ValorEntradaTrayectoria consultarValor(Entrada entrada, Trayectoria trayectoria) {
		ValorEntradaTrayectoria valor = null;
		List<ValorEntradaTrayectoria> results = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntradaTrayectoria where TrayectoriaEntradaid = :trayectoria AND EntradaTrayectoriaid = :entrada");
			query.setParameter("trayectoria", trayectoria);
			query.setParameter("entrada", entrada);
			//System.out.println("La queriy es " + query.list().);
			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if(results == null) {
			return null;
		} else if (results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

	

	public boolean consultarRegistro(Integer idEntrada,Integer idTrayectoria){
		boolean valor=false;
		List<ValorEntradaTrayectoria> results = null;
		try{
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntradaTrayectoria where Entradaid = :idEntrada and Trayectoriaid = :idTrayectoria");
			query.setParameter("idEntrada", idEntrada);
			query.setParameter("idTrayectoria", idTrayectoria);
			
			results = query.list();
			if(results==null){
				valor=false;
			}else{
				valor=true;
			}
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return valor;
	}
	public List<Trayectoria> consultarEstado(Trayectoria trayectoria){
		String valor = null;
		List<Trayectoria> results = null;
		try{
		
		session.beginTransaction();
		
	    Query query = session.createQuery("from Trayectoria where id =  :idTrayectoria");
	    query.setParameter("idTrayectoria", trayectoria.getId());
	    results = query.list();
	    
	    if(results==null){
			valor="";
		}else{
			valor="";
		}
		session.getTransaction().commit();
	}catch(HibernateException he){
		he.printStackTrace();
		session.getTransaction().rollback();
		throw he;
	}
		return results;
				
	}
	
	public List<ValorEntradaTrayectoria> obtenerValores(Trayectoria trayectoria){
		List<ValorEntradaTrayectoria> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ValorEntradaTrayectoria where TrayectoriaEntradaid = :idTrayectoria");
			query.setParameter("idTrayectoria", trayectoria.getId());
			results = query.list();
			if(results== null){
				valor= "";
			}
			else valor="";
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	
	public List<ValorEntradaTrayectoria> obtenerValores(Entrada entrada){
		List<ValorEntradaTrayectoria> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ValorEntradaTrayectoria where EntradaTrayectoriaid = :idEntrada");
			query.setParameter("idEntrada", entrada.getId());
			results = query.list();
			if(results== null){
				valor= "";
			}
			else valor="";
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
			return results;
					
		}
	}


