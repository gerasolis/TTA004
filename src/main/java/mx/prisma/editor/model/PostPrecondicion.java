package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 13/06/2015
 */
import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "PostPrecondicion", catalog = "PRISMA")
public class PostPrecondicion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String redaccion;
	private boolean precondicion;
	private CasoUso casoUso;
	private Set<ReferenciaParametro> referencias = new HashSet<ReferenciaParametro>(0);



	public PostPrecondicion() {
	}

	public PostPrecondicion(String redaccion,
			boolean precondicion, CasoUso casoUso) {
		this.redaccion = redaccion;
		this.precondicion = precondicion;
		this.casoUso = casoUso;
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

	@Column(name = "redaccion", nullable = false, length = 1000)
	public String getRedaccion() {
		return this.redaccion;
	}

	public void setRedaccion(String redaccion) {
		this.redaccion = redaccion;
	}
	
	@Column(name = "precondicion", nullable = false)
	public boolean isPrecondicion() {
		return precondicion;
	}

	public void setPrecondicion(boolean precondicion) {
		this.precondicion = precondicion;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CasoUsoElementoid", referencedColumnName = "Elementoid")
	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postPrecondicion", cascade = CascadeType.ALL)
	public Set<ReferenciaParametro> getReferencias() {
		return referencias;
	}

	public void setReferencias(Set<ReferenciaParametro> referencias) {
		this.referencias = referencias;
	}



}
