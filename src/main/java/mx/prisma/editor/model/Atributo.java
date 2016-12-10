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

/**
 * Atributo generated by hbm2java
 */
@Entity
@Table(name = "Atributo", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"nombre", "EntidadElementoid" }))
public class Atributo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nombre;
	private Entidad entidad;
	private String descripcion;
	private boolean obligatorio;
	private Integer longitud;
	private String formatoArchivo;
	private Float tamanioArchivo;
	private UnidadTamanio unidadTamanio;
	private TipoDato tipoDato;
	private String otroTipoDato;

	public Atributo() {
	}
	public Atributo(String nombre, Entidad entidad, String descripcion,
			boolean obligatorio) {
		this.nombre = nombre;
		this.entidad = entidad;
		this.descripcion = descripcion;
		this.obligatorio = obligatorio;
	}
	public Atributo(String nombre, Entidad entidad, String descripcion,
			boolean obligatorio, int longitud) {
		this.nombre = nombre;
		this.entidad = entidad;
		this.descripcion = descripcion;
		this.obligatorio = obligatorio;
		this.longitud = longitud;
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

	@Column(name = "nombre", nullable = false, length = 45)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EntidadElementoid", referencedColumnName = "Elementoid")
	public Entidad getEntidad() {
		return entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	@Column(name = "descripcion", nullable = false, length = 999)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "obligatorio", nullable = false)
	public boolean isObligatorio() {
		return this.obligatorio;
	}

	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	@Column(name = "longitud", nullable = true)
	public Integer getLongitud() {
		return this.longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}
	
	@Column(name = "formatoArchivo", nullable = true, length = 10)
	public String getFormatoArchivo() {
		return this.formatoArchivo;
	}

	public void setFormatoArchivo(String formatoArchivo) {
		this.formatoArchivo = formatoArchivo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TipoDatoid",referencedColumnName="id", nullable = false)
	public TipoDato getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(TipoDato tipoDato) {
		this.tipoDato = tipoDato;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UnidadTamanioid",referencedColumnName="id", nullable = true)
	public UnidadTamanio getUnidadTamanio() {
		return unidadTamanio;
	}
	public void setUnidadTamanio(UnidadTamanio unidadTamanio) {
		this.unidadTamanio = unidadTamanio;
	}
	
	
	@Column(name = "tamanioArchivo", nullable = true, length = 10)
	public Float getTamanioArchivo() {
		return tamanioArchivo;
	}
	public void setTamanioArchivo(Float tamanioArchivo) {
		this.tamanioArchivo = tamanioArchivo;
	}
	
	@Column(name = "otroTipoDato", nullable = true, length = 45)
	public String getOtroTipoDato() {
		return otroTipoDato;
	}
	public void setOtroTipoDato(String otroTipoDato) {
		this.otroTipoDato = otroTipoDato;
	}

	
	
}
