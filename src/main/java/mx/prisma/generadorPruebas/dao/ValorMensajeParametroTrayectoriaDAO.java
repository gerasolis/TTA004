package mx.prisma.generadorPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametroTrayectoria;

public class ValorMensajeParametroTrayectoriaDAO extends GenericDAO  {
	public ValorMensajeParametroTrayectoria consultarValor(Integer id) {
		ValorMensajeParametroTrayectoria valor = null;
		try {
			session.beginTransaction();
			valor = (ValorMensajeParametroTrayectoria) session.get(ValorMensajeParametroTrayectoria.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		return valor;
	}
	
	public List<ValorMensajeParametroTrayectoria> findByReferenciaParametro(ReferenciaParametro referencia) {
		List<ValorMensajeParametroTrayectoria> results = null;
		
		try {
			session.beginTransaction();
			Query query = session
					.createQuery("from ValorMensajeParametroTrayectoria where ReferenciaParametroTrayectoriaid = :referencia");
			query.setParameter("referencia", referencia);

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
}
