package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import mx.prisma.admin.model.Proyecto;

@Entity
@Table(name = "Actor", catalog = "PRISMA")
@PrimaryKeyJoinColumn(name = "Elementoid", referencedColumnName = "id")
public class Actor extends Elemento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String otraCardinalidad;
	private Cardinalidad cardinalidad;

	public Actor() {
	}

	public Actor(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento, Cardinalidad cardinalidad) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.cardinalidad = cardinalidad;
	}

	public Actor(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento, String otraCardinalidad, Cardinalidad cardinalidad) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.otraCardinalidad = otraCardinalidad;
		this.cardinalidad = cardinalidad;
	}

	@Column(name = "otraCardinalidad", length = 10)
	public String getOtraCardinalidad() {
		return this.otraCardinalidad;
	}

	public void setOtraCardinalidad(String otraCardinalidad) {
		this.otraCardinalidad = otraCardinalidad;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Cardinalidadid", nullable = false)
	public Cardinalidad getCardinalidad() {
		return this.cardinalidad;
	}

	public void setCardinalidad(Cardinalidad cardinalidad) {
		this.cardinalidad = cardinalidad;
	}

}
