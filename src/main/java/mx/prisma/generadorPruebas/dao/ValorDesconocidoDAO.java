package mx.prisma.generadorPruebas.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.generadorPruebas.model.ValorDesconocido;

public class ValorDesconocidoDAO extends GenericDAO{
	
	public Entrada obtenerEntrada(String idAtributo){
		@SuppressWarnings("unchecked")
		List<Entrada> results = null;
		Entrada result = null;

		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from Entrada where Atributoid="+idAtributo);
		
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new Entrada();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			}
			
			System.out.println("Result de la pantalla: "+result.toString());
			//session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if (result==null){
			return null;
		} else 
			return result;

	}
	
	public void registraRuta(String nuevaRuta, Entrada entrada, int valorGuion){
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("INSERT INTO ValorDesconocido (id, ruta, valorGuion) "
					+ "VALUES ("+ entrada.getId() + ", '"+nuevaRuta + "', '"+ valorGuion +"')");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	public void borrarRuta(Entrada entrada){
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("DELETE FROM ValorDesconocido WHERE id="+entrada.getId());
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	public boolean consultarDesconocido(Entrada entrada){
		boolean resultado=false;
		ValorDesconocido result = null;
		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from ValorDesconocido where id="+entrada.getId());
			
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new ValorDesconocido();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setRuta(String.valueOf(obj[0]));
			   result.setValorGuion(Integer.parseInt(String.valueOf(obj[0])));
			}
			
			
			//session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if (result!=null){
			resultado=true;
		} else 
			resultado=false;
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public boolean consultarValorGuion(Entrada entrada){
		boolean resultado=false;
		ValorDesconocido result = null;
		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from ValorDesconocido where id="+entrada.getId()+" and valorGuion = 1");
			
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new ValorDesconocido();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setRuta(String.valueOf(obj[0]));
			   result.setValorGuion(Integer.parseInt(String.valueOf(obj[0])));
			}
			
			
			//session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if (result!=null){
			resultado=true;
		} else {
			resultado=false;
		}
		return resultado;
	}
	
	public String obtenerRuta(Entrada entrada){
		ValorDesconocido result = null;
		String ruta="";
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("SELECT id,ruta from ValorDesconocido where id="+entrada.getId());
			
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
			result = new ValorDesconocido();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setRuta(String.valueOf(obj[1]));
			}
			
			System.out.println("Result de la pantalla: "+result.toString());
			//session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return result.getRuta();
	}
	
	public String obtenerRutaValorGuion(Entrada entrada){
		ValorDesconocido result = null;
		String ruta="";
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * from ValorDesconocido where id="+entrada.getId()+" and valorGuion = 1");
			
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
			result = new ValorDesconocido();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setRuta(String.valueOf(obj[1]));
			   if(String.valueOf(obj[2]).equals("true"))
				   	result.setValorGuion(1);
			}
			
			System.out.println("Result de la pantalla: "+result.toString());
			//session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return result.getRuta();
	}
}
