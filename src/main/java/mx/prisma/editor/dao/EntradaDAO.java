package mx.prisma.editor.dao;

import java.util.Iterator;
import java.util.List;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.TipoParametro;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.model.ValorEntrada;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class EntradaDAO extends GenericDAO {
	public EntradaDAO() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<Entrada> consultarReferencias(Object objeto) {
		List<Entrada> results = null;
		Query query = null;
		String queryCadena = null;

		switch (ReferenciaEnum.getTipoReferencia(objeto)) {
		case ATRIBUTO:
			Atributo atributo = (Atributo) objeto;
			queryCadena = "FROM Entrada WHERE atributo.id = "
					+ atributo.getId();
			break;
		case TERMINOGLS:
			TerminoGlosario termino = (TerminoGlosario) objeto;
			queryCadena = "FROM Entrada WHERE terminoGlosario.id = "
					+ termino.getId();
			break;

		default:
			break;

		}
		try {
			session.beginTransaction();
			query = session.createQuery(queryCadena);
			results = query.list();
			session.getTransaction().commit();

		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}

		return results;
	}

	public Entrada findById(Integer id) {
		Entrada entrada = null;
		try {
			session.beginTransaction();
			entrada = (Entrada) session.get(Entrada.class, id);
			entrada.getValores().size();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return entrada;

	}
	@SuppressWarnings("unchecked")
	public Entrada consultarEntradaxAtributo(int idAtributo, int idCasoUso, int id){
		List<Entrada> listEntrada= null;
		System.out.println("idAtributo: "+idAtributo);
		System.out.println("idAtributo: "+idCasoUso);
		System.out.println("id: "+id);
		try{
		
			session.beginTransaction();
			
		    Query query = session.createQuery("from Entrada where Atributoid= :idAtributo and CasoUsoElementoid= :idCasoUso and id= :id");
		    query.setParameter("idAtributo", idAtributo);
		    query.setParameter("idCasoUso", idCasoUso);
		    query.setParameter("id", id);
		    listEntrada =  query.list();
			session.getTransaction().commit();
		   
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		 return listEntrada.get(0);

	}
	

	public void modificarEntrada(Entrada entrada) {
		try {
			session.beginTransaction();
			session.update(entrada);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	@SuppressWarnings("unchecked")
	public Entrada obtenerEntrada(int idAtributo, int CasoUsoid){
		
		List<Entrada> results = null;
		Entrada result = null;

		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from Entrada where Atributoid="+idAtributo+" and CasoUsoElementoid="+CasoUsoid);
		
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
	
	
	@SuppressWarnings("unchecked")
	public Entrada obtenerEntrada2(int idAtributo, int CasoUsoid){
		
		List<Entrada> results = null;
		Entrada result = null;

		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from Entrada where Atributoid="+idAtributo+" and CasoUsoElementoid="+CasoUsoid);
		
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new Entrada();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setTipoParametro((TipoParametro)obj[1]);
			   result.setCasoUso((CasoUso)obj[2]);
			   result.setAtributo((Atributo)obj[3]);
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
	
	@SuppressWarnings("unchecked")
	public Entrada obtenerEntrada3(int idAtributo, int CasoUsoid){
		
		List<Entrada> results = null;
		Entrada result = null;

		try {
			session.beginTransaction();
			
			Query query = session.createSQLQuery("SELECT * from Entrada where id="+idAtributo+" and CasoUsoElementoid="+CasoUsoid);
		
			List <Object>result_1 =  query.list();
			session.getTransaction().commit();
			Iterator itr = result_1.iterator();
			while(itr.hasNext()){
				result = new Entrada();
			   Object[] obj = (Object[]) itr.next();
			   result.setId(Integer.parseInt(String.valueOf(obj[0])));
			   result.setTipoParametro((TipoParametro)obj[1]);
			   result.setCasoUso((CasoUso)obj[2]);
			   result.setAtributo((Atributo)obj[3]);
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
	
	public Entrada obtenerAtributo(int id){
		@SuppressWarnings("unchecked")
		List<Entrada> results = null;
		Entrada result = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createSQLQuery("SELECT * from Entrada where id = :entrada");
			query.setParameter("entrada", id);

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
}
