package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Salida;
import mx.prisma.editor.model.TerminoGlosario;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class SalidaDAO extends GenericDAO{
	
	public SalidaDAO() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public List<Salida> consultarReferencias(Object objeto) {
		List<Salida> results = null;
		Query query = null;
		String queryCadena = null;
		
		switch(ReferenciaEnum.getTipoReferencia(objeto)) {
		case ATRIBUTO:
			Atributo atributo = (Atributo) objeto;
			queryCadena = "FROM Salida WHERE atributo.id = " + atributo.getId();
			break;
		case MENSAJE:
			Mensaje mensaje = (Mensaje) objeto;
			queryCadena = "FROM Salida WHERE mensaje.id = " + mensaje.getId();
			break;
		case TERMINOGLS:
			TerminoGlosario termino = (TerminoGlosario) objeto;
			queryCadena = "FROM Salida WHERE terminoGlosario.id = " + termino.getId();
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
