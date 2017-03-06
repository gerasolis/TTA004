package mx.prisma.guionPruebas.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import mx.prisma.dao.GenericDAO;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Paso;
import mx.prisma.guionPruebas.model.InstruccionDesconocida;

public class InstruccionDesconocidaDAO extends GenericDAO{
	
	public InstruccionDesconocidaDAO() {
		super();
	}
	
	public InstruccionDesconocida consultarInstruccionDesconocida(int id) {
		InstruccionDesconocida instruccion = null;

		try {
			session.beginTransaction();
			instruccion = (InstruccionDesconocida) session.get(InstruccionDesconocida.class, id);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
		}

		return instruccion;
	}
	
	public void agregarInstruccion(InstruccionDesconocida instruccion) {
		try {
			session.beginTransaction();
			session.saveOrUpdate(instruccion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			he.printStackTrace();
			session.getTransaction().rollback();
			throw he;
		}
		
	}
	
}
