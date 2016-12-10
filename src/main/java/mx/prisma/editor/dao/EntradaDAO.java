package mx.prisma.editor.dao;

import java.util.List;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.TerminoGlosario;
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
}
