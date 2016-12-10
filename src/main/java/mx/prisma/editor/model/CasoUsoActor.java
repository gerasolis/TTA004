package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 13/06/2015
 */
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name = "CasoUso_Actor", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CasoUsoElementoid", "ActorElementoid" }))
public class CasoUsoActor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private CasoUso casouso;
	private Actor actor;

	public CasoUsoActor() {
	}

	public CasoUsoActor(CasoUso casouso, Actor actor) {
		this.casouso = casouso;
		this.actor = actor;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CasoUsoElementoid", referencedColumnName ="Elementoid", nullable = false)
	public CasoUso getCasouso() {
		return casouso;
	}

	public void setCasouso(CasoUso casouso) {
		this.casouso = casouso;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ActorElementoid", referencedColumnName ="Elementoid", nullable = false)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	

}
