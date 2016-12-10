package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.model.Actualizacion;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

public class PantallaDAO extends ElementoDAO {

	public PantallaDAO() {
		super();
	}
	
	public void registrarEntidad(Pantalla pantalla) {
		super.registrarElemento(pantalla);
	}
	
	@SuppressWarnings("unchecked")
	public Pantalla consultarPantalla(String clave, String numero, Proyecto proyecto) {
		List<Pantalla> results = null;

		try {
			session.beginTransaction();
			SQLQuery query = session
					.createSQLQuery("SELECT * FROM Elemento INNER JOIN Pantalla ON Elemento.id = Pantalla.Elementoid WHERE Elemento.Proyectoid = :proyecto AND Elemento.clave = :clave AND numero = :numero").addEntity(Pantalla.class);
			query.setParameter("proyecto", proyecto.getId());
			query.setParameter("clave", clave);
			query.setParameter("numero", numero);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if (results.isEmpty()){
			return null;
		} else 
			return results.get(0);

	}
	
	@SuppressWarnings("unchecked")
	public Pantalla consultarPantalla(Modulo modulo, String numero) {
		List<Pantalla> results = null;

		try {
			session.beginTransaction();
			SQLQuery query = session
					.createSQLQuery("SELECT * FROM Elemento INNER JOIN Pantalla ON Elemento.id = Pantalla.Elementoid WHERE Pantalla.Moduloid = :modulo AND Elemento.numero = :numero").addEntity(Pantalla.class);
			query.setParameter("modulo", modulo.getId());
			query.setParameter("numero", numero);

			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if(results == null) {
			return null;
		}
		if (results.isEmpty()){
			return null;
		} else 
			return results.get(0);

	}

	public Pantalla consultarPantalla(int id) {
		return (Pantalla) super.consultarElemento(id);
	}

	@SuppressWarnings("unchecked")
	public List<Pantalla> consultarPantallasModulo(Modulo modulo) {
		List<Pantalla> pantallas = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from Pantalla where Moduloid = :modulo");
			query.setParameter("modulo", modulo.getId());
			pantallas = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return pantallas;
	}
	
	/*
	 * 
	 * public void modificarPantalla (@model, @actualizacion)
	 * NOTA: Se accede a los miembros de la pantalla porque en alguna parte superior del trazado
	 * se asigna la pantalla de la sesión a otro objeto, el cual es el que llega aquí bajo
	 * el nombre "model", lo que provoca una excepción nounique.
	 * 
	 */

//	public void modificarPantalla(Pantalla model, Actualizacion actualizacion) {
//		try {
//			session.beginTransaction();
// 			session.saveOrUpdate(model);
//			session.save(actualizacion);
//			session.getTransaction().commit();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			session.getTransaction().rollback();
//			throw he;
//		}
//		
//	}
	public void modificarPantalla(Pantalla model) {
		try {
			session.beginTransaction();
 			session.saveOrUpdate(model);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

}
