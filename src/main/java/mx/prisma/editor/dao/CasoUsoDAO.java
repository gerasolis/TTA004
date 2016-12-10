package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.Trayectoria;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

public class CasoUsoDAO extends ElementoDAO {
	public CasoUsoDAO() {
		super();
	}

	public void cleanRelaciones(CasoUso casodeuso) {
		casodeuso.getActores().clear();
		casodeuso.getEntradas().clear();
		casodeuso.getSalidas().clear();
		casodeuso.getReglas().clear();
	}

	@SuppressWarnings("unchecked")
	public List<CasoUso> consultarCasosUso(Modulo modulo) {
		List<CasoUso> casosdeuso = null;

		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from CasoUso where Moduloid = :modulo");
			query.setParameter("modulo", modulo.getId());
			casosdeuso = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		return casosdeuso;

	}

	public CasoUso consultarCasoUso(int id) {
		return (CasoUso) super.consultarElemento(id);
	}
	
	public CasoUso consultarCasoUsoTrayLAZY(int id) {
		CasoUso elemento = null;

		try {
			session.beginTransaction();
			elemento = (CasoUso) session.get(CasoUso.class, id);
			for( Trayectoria tray : elemento.getTrayectorias()) {
				tray.getPasos().size();
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return elemento;

	}

	@SuppressWarnings("unchecked")
	public CasoUso consultarCasoUso(Modulo modulo, String numero) {
		List<CasoUso> results = null;

		try {
			session.beginTransaction();
			SQLQuery query = session
					.createSQLQuery("SELECT * FROM Elemento INNER JOIN CasoUso ON Elemento.id = CasoUso.Elementoid WHERE CasoUso.Moduloid = :modulo AND Elemento.numero = :numero").addEntity(CasoUso.class);
			query.setParameter("modulo", modulo.getId());
			query.setParameter("numero", numero);
			results = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}
		if (results.isEmpty()) {
			return null;
		} else

			return results.get(0);

	}

//	public void modificarCasoUso(CasoUso casodeuso, Actualizacion actualizacion) {
//		try {
//			session.beginTransaction();
//			Query query1 = session.createQuery("DELETE FROM CasoUsoActor WHERE casouso.id = :id");
//			query1.setParameter("id", casodeuso.getId());
//			query1.executeUpdate();
//			Query query2 = session.createQuery("DELETE FROM Entrada WHERE casoUso.id = :id");
//			query2.setParameter("id", casodeuso.getId());
//			query2.executeUpdate();
//			Query query3 = session.createQuery("DELETE FROM Salida WHERE casoUso.id = :id");
//			query3.setParameter("id", casodeuso.getId());
//			query3.executeUpdate();
//			Query query4 = session.createQuery("DELETE FROM CasoUsoReglaNegocio WHERE casoUso.id = :id");
//			query4.setParameter("id", casodeuso.getId());
//			query4.executeUpdate();
//			Query query5 = session.createQuery("DELETE FROM PostPrecondicion WHERE casoUso.id = :id");
//			query5.setParameter("id", casodeuso.getId());
//			query5.executeUpdate();
//			
//			session.update(casodeuso);
//			session.save(actualizacion);
//			session.getTransaction().commit();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			session.getTransaction().rollback();
//			throw he;
//		}	
//	}
	
	public void modificarCasoUso(CasoUso casodeuso, boolean modificarReferencias) {
		try {
			session.beginTransaction();
			if(modificarReferencias) {
				Query query1 = session.createQuery("DELETE FROM CasoUsoActor WHERE casouso.id = :id");
				query1.setParameter("id", casodeuso.getId());
				query1.executeUpdate();
				Query query2 = session.createQuery("DELETE FROM Entrada WHERE casoUso.id = :id");
				query2.setParameter("id", casodeuso.getId());
				query2.executeUpdate();
				Query query3 = session.createQuery("DELETE FROM Salida WHERE casoUso.id = :id");
				query3.setParameter("id", casodeuso.getId());
				query3.executeUpdate();
				Query query4 = session.createQuery("DELETE FROM CasoUsoReglaNegocio WHERE casoUso.id = :id");
				query4.setParameter("id", casodeuso.getId());
				query4.executeUpdate();
				Query query5 = session.createQuery("DELETE FROM PostPrecondicion WHERE casoUso.id = :id");
				query5.setParameter("id", casodeuso.getId());
				query5.executeUpdate();
			}
			
			session.update(casodeuso);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}	
	}
	
	

	public void registrarCasoUso(CasoUso casodeuso) {
		super.registrarElemento(casodeuso);

	}

	@SuppressWarnings("unchecked")
	/* 
	 * consultarCasoUso(@clave, @numero, @proyecto)
	 * 
	 * El siguiente segmento de código, inicializa los objetos LAZY para el paso.
	 * 
	 * if (casosdeuso != null)
				for (Trayectoria trayectoria : casosdeuso.get(0).getTrayectorias()) {
					for (Paso paso : trayectoria.getPasos()) {
						paso.getReferencias().size();
					}
				}
	 */
	public CasoUso consultarCasoUso(String clave, String numero, Proyecto proyecto) {
		List<CasoUso> casosdeuso = null;

		try {
			session.beginTransaction();
			SQLQuery query = session
					.createSQLQuery("SELECT * FROM Elemento INNER JOIN CasoUso ON Elemento.id = CasoUso.Elementoid WHERE Elemento.Proyectoid = :proyecto AND Elemento.numero = :numero AND Elemento.clave = :clave").addEntity(CasoUso.class);
			query.setParameter("proyecto", proyecto.getId());
			query.setParameter("numero", numero);
			query.setParameter("clave", clave);
			casosdeuso = query.list();
			
			if (casosdeuso != null)
				for (Trayectoria trayectoria : casosdeuso.get(0).getTrayectorias()) {
					for (Paso paso : trayectoria.getPasos()) {
						paso.getReferencias().size();
					}
				}
			
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (casosdeuso == null){
			return null;
		} else

			return casosdeuso.get(0);

	}

	@SuppressWarnings("unchecked")
	public List<CasoUso> consultarCasosUso(Integer proyectoId) {

		List<CasoUso> casosdeuso = null;

		try {
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT * FROM Elemento INNER JOIN CasoUso ON Elemento.id = CasoUso.Elementoid WHERE Elemento.Proyectoid = :proyecto").addEntity(CasoUso.class);
			query.setParameter("proyecto", proyectoId);
			casosdeuso = query.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		if (casosdeuso == null) {
			return null;
		} else 
			if (casosdeuso.isEmpty()) {
				return null;
			} else {
				return casosdeuso;
			}
	}
}
