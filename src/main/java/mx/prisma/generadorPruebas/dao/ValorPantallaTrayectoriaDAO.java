package mx.prisma.generadorPruebas.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
//import mx.prisma.editor.dao.ElementoDAO;
//import mx.prisma.editor.model.Pantalla;
import mx.prisma.generadorPruebas.model.ValorPantallaTrayectoria;

public class ValorPantallaTrayectoriaDAO extends GenericDAO {
	public void registrarPantalla(ValorPantallaTrayectoria model) {
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("INSERT INTO ValorPantallaTrayectoria (PantallaTrayectoriaid, TrayectoriaPantallaid, patron) "
					+ "VALUES ("+ model.getPantalla().getId()+ ", "+ model.getTrayectoria().getId()+ ", '"+ model.getPatron()+ "')");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	public Pantalla obtenerPantalla(ValorPantallaTrayectoria pantalla) {
		@SuppressWarnings("unchecked")
			List<Pantalla> results = null;
			Pantalla result = null;

			try {
				session.beginTransaction();
				/*SQLQuery query = session
						.createSQLQuery("SELECT * FROM Elemento INNER JOIN Pantalla ON Elemento.id = Pantalla.Elementoid WHERE Elementoid = :PantallaTrayectoriaid").addEntity(Pantalla.class);
				query.setParameter("PantallaTrayectoriaid", pantalla.getPantalla().getId());*/
				Query query = session.createSQLQuery("SELECT Elementoid,imagen,Moduloid FROM Pantalla,ValorPantallaTrayectoria where elementoid=:id and elementoid=PantallaTrayectoriaid");
				query.setParameter("id",pantalla.getPantalla().getId());
				//results = (List<Pantalla>)query.list();
				
				List <Object>result_1 =  query.list();
				session.getTransaction().commit();
				Iterator itr = result_1.iterator();
				while(itr.hasNext()){
					result = new Pantalla();
					
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
		
	
	
	public void modificarValorPantallaTrayectoria(ValorPantallaTrayectoria model) {
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("UPDATE ValorPantallaTrayectoria "
					+ "SET patron = '" + model.getPatron()+ "' WHERE PantallaTrayectoriaid = " + model.getPantalla().getId()+ " AND TrayectoriaPantallaid = "+ model.getTrayectoria().getId());
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ValorPantallaTrayectoria> consultarValorPantallaTrayectoria(ValorPantallaTrayectoria model) {
		List<ValorPantallaTrayectoria> result = null;
		boolean valor = false;
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM ValorPantallaTrayectoria "
					+ " WHERE PantallaTrayectoriaid = " + model.getPantalla().getId()+ " AND TrayectoriaPantallaid = "+ model.getTrayectoria().getId());
			result =  (List<ValorPantallaTrayectoria>)query.list();
			session.getTransaction().commit();
			
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if(result.isEmpty()){
			
			System.out.println("ESTO ES EL BOOLEAN: " + valor + " y id pantalla es  "+ model.getPantalla().getId() + " y idtrayectoria es " + model.getTrayectoria().getId());
			return result;
		}
		else{
			System.out.println("ESTO ES EL BOOLEAN2: " + valor);
			valor = false;
			return result;
		}	
	}
	
	public ValorPantallaTrayectoria obtenerValorPantallaTrayectoria(Pantalla pantalla, Trayectoria trayectoria) {
		ValorPantallaTrayectoria result = null;
		try {
			System.out.println("pantalla.getId(): "+pantalla.getId());
			System.out.println("trayectoria.getId(): "+trayectoria.getId());
			session.beginTransaction();
			Query query = session.createSQLQuery("SELECT * FROM ValorPantallaTrayectoria "
					+ " WHERE PantallaTrayectoriaid = " + pantalla.getId()+ " AND TrayectoriaPantallaid = "+ trayectoria.getId());
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new ValorPantallaTrayectoria();
				
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setPantalla(pantalla);
			   result.setTrayectoria(trayectoria);
			   result.setPatron(String.valueOf(obj[3]));

			}
			//System.out.println("Result: "+result.toString());
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if(result == null){
			System.out.println("ENTRA EN EL NULL :( ");
			return result;
		}
		else{
			return result;
		}	
	}//ESTE SÍ SIRVE.
	
	
	/*public ValorPantallaTrayectoria consultarValor(Pantalla pantalla, Trayectoria trayectoria) {

		List<ValorPantallaTrayectoria> results = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorPantallaTrayectoria where TrayectoriaPantallaid = :trayectoria AND PantallaTrayectoriaid = :pantalla");
			query.setParameter("trayectoria", trayectoria);
			query.setParameter("pantalla", pantalla);
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
	}*/
}
