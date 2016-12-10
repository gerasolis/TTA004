package mx.prisma.editor.dao;

import java.util.ArrayList;
import java.util.List;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.TerminoGlosario;

public class TerminoGlosarioDAO extends ElementoDAO{
	
	public TerminoGlosarioDAO () {
		super();
	}
	
	public TerminoGlosario consultarTerminoGlosario(String nombre, Proyecto proyecto) {
		return (TerminoGlosario) super.consultarElemento(nombre, proyecto, ReferenciaEnum.getTabla(TipoReferencia.TERMINOGLS));
	}
	public TerminoGlosario consultarTerminoGlosario(int id) {
		return (TerminoGlosario) super.consultarElemento(id);
	}
    public void registrarTerminoGlosario(TerminoGlosario terminoGlosario) throws Exception {
    	super.registrarElemento(terminoGlosario);
    }
    
    public List<TerminoGlosario> consultarTerminosGlosario(int idProyecto) {
		List<TerminoGlosario> terminosGlosario = new ArrayList<TerminoGlosario>();
		List<Elemento> elementos = super.consultarElementos(TipoReferencia.TERMINOGLS,  idProyecto);
		if (elementos != null)
		for (Elemento elemento : elementos) {
			terminosGlosario.add((TerminoGlosario) elemento);
		}
		return terminosGlosario;
	}
	
	public String siguienteNumeroTerminoGlosario(int idProyecto) {
		return super.siguienteNumero(TipoReferencia.TERMINOGLS, idProyecto);
	}
}
