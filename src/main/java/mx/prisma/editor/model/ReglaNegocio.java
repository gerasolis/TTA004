package mx.prisma.editor.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import mx.prisma.admin.model.Proyecto;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "ReglaNegocio", catalog = "PRISMA")
@PrimaryKeyJoinColumn(name = "Elementoid", referencedColumnName = "id")
//@JsonTypeName("reglaNegocio")
public class ReglaNegocio extends Elemento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String redaccion;
	private TipoReglaNegocio tipoReglaNegocio;
	private Atributo atributoUnicidad;
	private Atributo atributoFechaI;
	private Atributo atributoFechaF;
	private TipoComparacion tipoComparacion;
	private Atributo atributoComp1;
	private Atributo atributoComp2;
	private Operador operadorComp;
	private String expresionRegular;
	private Atributo atributoExpReg;

	public ReglaNegocio() {
	}

	public ReglaNegocio(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento, String redaccion,
			TipoReglaNegocio tipoReglaNegocio) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.redaccion = redaccion;
		this.tipoReglaNegocio = tipoReglaNegocio;
	}

	@Column(name = "redaccion", nullable = false, length = 999)
	public String getRedaccion() {
		return this.redaccion;
	}

	public void setRedaccion(String redaccion) {
		this.redaccion = redaccion;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TipoReglaNegocioid", referencedColumnName = "id")
	public TipoReglaNegocio getTipoReglaNegocio() {
		return tipoReglaNegocio;
	}

	public void setTipoReglaNegocio(TipoReglaNegocio tipoReglaNegocio) {
		this.tipoReglaNegocio = tipoReglaNegocio;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_unicidad", referencedColumnName = "id")	
	public Atributo getAtributoUnicidad() {
		return atributoUnicidad;
	}

	public void setAtributoUnicidad(Atributo atributoUnicidad) {
		this.atributoUnicidad = atributoUnicidad;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_fechaI", referencedColumnName = "id")	
	public Atributo getAtributoFechaI() {
		return atributoFechaI;
	}

	public void setAtributoFechaI(Atributo atributoFechaI) {
		this.atributoFechaI = atributoFechaI;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_fechaT", referencedColumnName = "id")	
	public Atributo getAtributoFechaF() {
		return atributoFechaF;
	}

	public void setAtributoFechaF(Atributo atributoFechaF) {
		this.atributoFechaF = atributoFechaF;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TipoComparacionid", referencedColumnName = "id")	
	public TipoComparacion getTipoComparacion() {
		return tipoComparacion;
	}

	public void setTipoComparacion(TipoComparacion tipoComparacion) {
		this.tipoComparacion = tipoComparacion;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_comp1", referencedColumnName = "id")	
	public Atributo getAtributoComp1() {
		return atributoComp1;
	}

	public void setAtributoComp1(Atributo atributoComp1) {
		this.atributoComp1 = atributoComp1;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_comp2", referencedColumnName = "id")	
	public Atributo getAtributoComp2() {
		return atributoComp2;
	}

	public void setAtributoComp2(Atributo atributoComp2) {
		this.atributoComp2 = atributoComp2;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Operadorid", referencedColumnName = "id")	
	public Operador getOperadorComp() {
		return operadorComp;
	}

	public void setOperadorComp(Operador operadorComp) {
		this.operadorComp = operadorComp;
	}

	@Column(name = "expresionRegular", nullable = true, length = 100)
	public String getExpresionRegular() {
		return expresionRegular;
	}

	public void setExpresionRegular(String expresionRegular) {
		this.expresionRegular = expresionRegular;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid_expReg", referencedColumnName = "id")	
	public Atributo getAtributoExpReg() {
		return atributoExpReg;
	}

	public void setAtributoExpReg(Atributo atributoExpReg) {
		this.atributoExpReg = atributoExpReg;
	}

	@JsonIgnore
	@Transient
	public String getType() {
		return "reglaNegocio";
	}

}
