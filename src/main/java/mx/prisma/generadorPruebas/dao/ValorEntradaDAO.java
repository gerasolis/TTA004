package mx.prisma.generadorPruebas.dao;

import java.util.Iterator;
import java.util.List;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ValorEntradaDAO extends GenericDAO {

	public void registrarValorEntrada(ValorEntrada valor) {
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
	
	public ValorEntrada consultarValorValido(Entrada entrada) {
		List<ValorEntrada> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntrada where Entradaid = :entrada AND valido = true");
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
			return results.get(0);
		}
	}

	public List<ValorEntrada> consultarValoresInvalidos(Entrada entrada) {
		List<ValorEntrada> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntrada where Entradaid = :entrada AND valido = false");
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

	public List<ValorEntrada> consultarValores(Entrada entrada) {
		List<ValorEntrada> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntrada where Entradaid = :entrada");
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

	public ValorEntrada consultarValor(Integer id) {
		ValorEntrada valor = null;
		try {
			session.beginTransaction();
			valor = (ValorEntrada) session.get(ValorEntrada.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return valor;
	}
	
	public void borrarValorEntrada(Entrada e){
		try {
			session.beginTransaction();
			Query query = session.createSQLQuery("DELETE FROM ValorEntrada where valido=1 and Entradaid = :entrada");
			query.setParameter("entrada", e.getId());
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
	}
	public ValorEntrada consultarValorInvalido(ReglaNegocio reglaNegocio,
			Entrada entrada) {
		List<ValorEntrada> results = null;
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorEntrada where ReglaNegocioElementoid = :reglaNegocio AND Entradaid = :entrada AND valido = false");
			query.setParameter("reglaNegocio", reglaNegocio);
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
			return results.get(0);
		}
	}
	
	public List<ValorEntrada> obtenerValoresEntrada(Entrada entrada){
		List<ValorEntrada> results = null;
		String valor = null;

		try{
			session.beginTransaction();
			Query query = session.createQuery("from ValorEntrada where Entradaid = :idEntrada");
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
	//
	
	public void modificarValorEntrada(Entrada entrada) {
		List<ValorEntrada> results = null;
		
		try {
			session.beginTransaction();
			Query query = session.createQuery("update ValorEntrada set nogenerable_guion = true where Entradaid =:idEntrada");
			query.setParameter("idEntrada", entrada.getId());
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		
	
	}

	
}
