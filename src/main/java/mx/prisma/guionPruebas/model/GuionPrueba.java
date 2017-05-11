package mx.prisma.guionPruebas.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;


@Entity
@Table(name = "GuionPrueba", catalog = "PRISMA")
public class GuionPrueba implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idGuionPrueba;
	private Integer CasoUsoElementoid;
	private Boolean seleccionado;
	private Integer orden;
	
	public GuionPrueba() {
	}

	public GuionPrueba(Integer CasoUsoElementoid) {
		this.CasoUsoElementoid = CasoUsoElementoid;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idGuionPrueba", unique = true, nullable = false)
	public Integer getIdGuionPrueba() {
		return idGuionPrueba;
	}
	
	public void setIdGuionPrueba(Integer idGuionPrueba) {
		this.idGuionPrueba = idGuionPrueba;
	}

	@JoinColumn(name = "CasoUsoElementoid", referencedColumnName = "Elementoid")
	@JsonProperty("CasoUsoElementoid")
	public Integer getCasoUsoElementoid() {
		return CasoUsoElementoid;
	}

	public void setCasoUsoElementoid(Integer casoUsoElementoid) {
		this.CasoUsoElementoid = casoUsoElementoid;
	}
	
	@Column(name = "seleccionado")
	public Boolean getSeleccionado() {
		return seleccionado;
	}
	
	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
	
	@Column(name = "orden")
	public Integer getOrden() {
		return orden;
	}
	
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
}
