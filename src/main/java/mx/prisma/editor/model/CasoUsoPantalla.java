package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
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
@Table(name = "CasoUso_Pantalla", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CasoUsoElementoid", "PantallaElementoid"}))
public class CasoUsoPantalla implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Pantalla pantalla;
	private CasoUso casoUso;

	public CasoUsoPantalla() {
	}

	public CasoUsoPantalla(CasoUso casouso, Pantalla pantalla) {
		this.casoUso = casouso;
		this.pantalla = pantalla;
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
    @JoinColumn(name = "Pantallaid", referencedColumnName = "id")
	public Pantalla getPantalla() {
		return pantalla;
	}

	public void setPantalla(Pantalla pantalla) {
		this.pantalla = pantalla;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CasoUsoElementoid", referencedColumnName = "Elementoid")
	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	

}
