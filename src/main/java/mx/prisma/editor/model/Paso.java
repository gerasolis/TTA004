package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 17/06/2015
 */

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "Paso", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"numero", "Trayectoriaid" }))
public class Paso implements java.io.Serializable, Comparable<Paso> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private int numero;
	private boolean realizaActor;
	private String redaccion;
	private Trayectoria trayectoria;
	private Verbo verbo;
	private String otroVerbo;


	private Set<ReferenciaParametro> referencias = new HashSet<ReferenciaParametro>(0);


	public Paso() {
	}

	public Paso(int numero, boolean realizaActor, String redaccion,
			Trayectoria trayectoria, Verbo verbo) {
		this.numero = numero;
		this.realizaActor = realizaActor;
		this.redaccion = redaccion;
		this.trayectoria = trayectoria;
		this.verbo = verbo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "numero", nullable = false)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "realizaActor", nullable = false)
	public boolean isRealizaActor() {
		return this.realizaActor;
	}

	
	public void setRealizaActor(boolean realizaActor) {
		this.realizaActor = realizaActor;
	}

	@Column(name = "redaccion", nullable = false, length = 1000)
	public String getRedaccion() {
		return this.redaccion;
	}

	public void setRedaccion(String redaccion) {
		this.redaccion = redaccion;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Trayectoriaid", referencedColumnName ="id", nullable = false)
	public Trayectoria getTrayectoria() {
		return trayectoria;
	}

	public void setTrayectoria(Trayectoria trayectoria) {
		this.trayectoria = trayectoria;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paso", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ReferenciaParametro> getReferencias() {
		return referencias;
	}

	public void setReferencias(Set<ReferenciaParametro> referencias) {
		this.referencias = referencias;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Verboid",referencedColumnName="id", nullable = false)
	public Verbo getVerbo() {
		return verbo;
	}

	public void setVerbo(Verbo verbo) {
		this.verbo = verbo;
	}

	@Column(name = "otroVerbo", nullable = true, length = 45)
	public String getOtroVerbo() {
		return otroVerbo;
	}

	public void setOtroVerbo(String otroVerbo) {
		this.otroVerbo = otroVerbo;
	}

	public int compareTo(Paso o) {
		return Integer.compare(this.numero, o.getNumero());
	}


}
