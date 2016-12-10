package mx.prisma.editor.dao;


import java.util.ArrayList;
import java.util.List;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.editor.model.Actor;
import mx.prisma.editor.model.Elemento;


public class ActorDAO extends ElementoDAO {
	
	public ActorDAO() {
		super();
	}

    public void registrarActor(Actor actor) throws Exception {
    	super.registrarElemento(actor);
    }
    
	public Actor consultarActor(int id) {
		return (Actor)super.consultarElemento(id);
	}
	
	public Actor consultarActor(String nombre, Proyecto proyecto) {
		return (Actor)super.consultarElemento(nombre, proyecto, ReferenciaEnum.getTabla(TipoReferencia.ACTOR));
	}
	
	public List<Actor> consultarActores(int idProyecto) {
		List<Actor> actores = new ArrayList<Actor>();
		List<Elemento> elementos = super.consultarElementos(TipoReferencia.ACTOR,  idProyecto);
		if (elementos != null)
		for (Elemento elemento : elementos) {
			actores.add((Actor) elemento);
		}
		return actores;
	}
	
	public String siguienteNumeroActor(int idProyecto) {
		return super.siguienteNumero(TipoReferencia.ACTOR, idProyecto);
	}
}
