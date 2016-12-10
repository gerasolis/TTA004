package mx.prisma.editor.dao;


import java.util.List;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entidad;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.TerminoGlosario;
import mx.prisma.editor.model.Trayectoria;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class ReferenciaParametroDAO extends GenericDAO {

	public ReferenciaParametroDAO() {
		super();
	}
	
	public ReferenciaParametro consultarReferenciaParametro(int id) {
		ReferenciaParametro referenciaParametro = null;

		try {
			session.beginTransaction();
			referenciaParametro = (ReferenciaParametro) session.get(ReferenciaParametro.class, id);
			referenciaParametro.getQueries().size();
			referenciaParametro.getValoresMensajeParametro().size();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return referenciaParametro;

	}
	
	@SuppressWarnings("unchecked")
	public List<ReferenciaParametro> consultarReferenciasParametro(Object objeto) {
		List<ReferenciaParametro> results = null;
		Query query = null;
		String queryCadena = null;
		
		switch(ReferenciaEnum.getTipoReferencia(objeto)) {
		case ACCION:
			Accion accion = (Accion) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE accionDestino.id = " + accion.getId();
			break;
		case ACTOR:
			Actor actor = (Actor) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = " + actor.getId();
			break;
		case ATRIBUTO:
			Atributo atributo = (Atributo) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE atributo.id = " + atributo.getId();
			break;
		case CASOUSO:
			CasoUso casoUso = (CasoUso) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = " + casoUso.getId();
			break;
		case ENTIDAD:
			Entidad entidad = (Entidad) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = " + entidad.getId();
			break;
		case MENSAJE:
			Mensaje mensaje = (Mensaje) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = " + mensaje.getId();
			break;
		case PANTALLA:
			Pantalla pantalla = (Pantalla) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = " + pantalla.getId();
			break;
		case PASO:
			Paso paso = (Paso) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE pasoDestino.id = "+paso.getId();
			break;
		case REGLANEGOCIO:
			ReglaNegocio reglaNegocio = (ReglaNegocio) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = "+reglaNegocio.getId();

			break;
		case TERMINOGLS:
			TerminoGlosario terminoGlosario = (TerminoGlosario) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE elementoDestino.id = "+terminoGlosario.getId();
			break;
		case TRAYECTORIA:
			Trayectoria trayectoria = (Trayectoria) objeto;
			queryCadena = "FROM ReferenciaParametro WHERE trayectoria.id = "+trayectoria.getId();
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

	public void modificarReferenciaParametro(ReferenciaParametro referencia) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(referencia);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}

}
