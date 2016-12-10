package mx.prisma.generadorPruebas.bs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.generadorPruebas.dao.QueryDAO;
import mx.prisma.generadorPruebas.model.Query;

public class QueryBs {
	public static void registrarQuery(Query valor) throws Exception{
		new QueryDAO().registrarQuery(valor);
	}

	public static Set<Query> consultarQueries(ReferenciaParametro referencia) {
		List<Query> queries = new QueryDAO().findByReferenciaParametro(referencia);
		if(queries != null) {
			 return new HashSet<Query>(queries);
		}
		
		return null;
	}

	public static Query consultarQuery(Integer id) {
		Query valor = new QueryDAO().findById(id);
		return valor;
	}

	
}
