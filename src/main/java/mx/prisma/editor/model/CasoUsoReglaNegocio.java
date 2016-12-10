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
@Table(name = "CasoUso_ReglaNegocio", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CasoUsoElementoid", "ReglaNegocioElementoid"}))
public class CasoUsoReglaNegocio implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private CasoUso casoUso;
	private ReglaNegocio reglaNegocio;
	
	public CasoUsoReglaNegocio() {
	}

	public CasoUsoReglaNegocio(CasoUso casoUso, ReglaNegocio reglaNegocio) {
		this.casoUso = casoUso;
		this.reglaNegocio = reglaNegocio;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int  id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CasoUsoElementoid", referencedColumnName = "Elementoid")
	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ReglaNegocioElementoid", referencedColumnName = "Elementoid")
	public ReglaNegocio getReglaNegocio() {
		return reglaNegocio;
	}

	public void setReglaNegocio(ReglaNegocio reglaNegocio) {
		this.reglaNegocio = reglaNegocio;
	}

	
}
