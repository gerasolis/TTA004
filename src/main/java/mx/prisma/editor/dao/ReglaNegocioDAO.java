package mx.prisma.editor.dao;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.editor.model.Actualizacion;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.ReglaNegocio;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ReglaNegocioDAO extends ElementoDAO {
	
	public ReglaNegocioDAO() {
		super();
	}

	public void registrarReglaNegocio(ReglaNegocio reglaNegocio) throws Exception {
		super.registrarElemento(reglaNegocio);
	}

	public ReglaNegocio consultarReglaNegocio(int id) {
		return (ReglaNegocio) super.consultarElemento(id);
	}

	public ReglaNegocio consultarReglaNegocio(String nombre, Proyecto proyecto) {
		return (ReglaNegocio) super.consultarElemento(nombre, proyecto,
				ReferenciaEnum.getTabla(TipoReferencia.REGLANEGOCIO));
	}

	public List<ReglaNegocio> consultarReglasNegocio(int idProyecto) {
		List<ReglaNegocio> reglasNegocio = new ArrayList<ReglaNegocio>();
		List<Elemento> elementos = super.consultarElementos(TipoReferencia.REGLANEGOCIO, idProyecto);
		for (Elemento elemento : elementos) {
			reglasNegocio.add((ReglaNegocio)elemento);
		}
		return reglasNegocio;
	}

	public String siguienteNumero(int idProyecto) {
		return super.siguienteNumero(TipoReferencia.REGLANEGOCIO, idProyecto);
	}

//	public void modificarReglaNegocio(ReglaNegocio model,
//			Actualizacion actualizacion) {
//		try {
//			session.beginTransaction();
//			
//			
//			session.update(model);
//			session.save(actualizacion);
//			session.getTransaction().commit();
//		} catch (HibernateException he) {
//			he.printStackTrace();
//			session.getTransaction().rollback();
//			throw he;
//		}
//		
//	}
	
	public void modificarReglaNegocio(ReglaNegocio model) {
		try {
			session.beginTransaction();
			
			
			session.update(model);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ReglaNegocio> consultarReferencias(Object objeto) {
		List<ReglaNegocio> results = null;
		Query query = null;
		String queryCadena = null;

		switch(ReferenciaEnum.getTipoReferencia(objeto)) {
		case ATRIBUTO:
			Atributo atributo = (Atributo) objeto;
			queryCadena = "FROM ReglaNegocio WHERE atributoUnicidad.id = " + atributo.getId() + " OR "
			+ "atributoFechaI.id = " + atributo.getId() + " OR "
			+ "atributoFechaF.id = " + atributo.getId() + " OR "
			+ "atributoComp1.id = " + atributo.getId() + " OR "
			+ "atributoComp2.id = " + atributo.getId() + " OR "
			+ "atributoExpReg.id = " + atributo.getId();
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
}
