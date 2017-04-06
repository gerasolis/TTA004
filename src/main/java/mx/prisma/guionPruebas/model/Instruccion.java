package mx.prisma.guionPruebas.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "Instruccion", catalog = "PRISMA")
public class Instruccion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idInstruccion;
	private String instruccion;
	private Integer GuionPrueba_idGuionPrueba;
	private Integer Paso_idPaso;

	public Instruccion() {
	}

	public Instruccion(String instruccion, Integer GuionPrueba_idGuionPrueba, Integer Paso_idPaso) {
		this.instruccion = instruccion;
		this.GuionPrueba_idGuionPrueba = GuionPrueba_idGuionPrueba;
		this.Paso_idPaso = Paso_idPaso;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idInstruccion", unique = true, nullable = false)
	public Integer getIdInstruccion() {
		return this.idInstruccion;
	}

	public void setIdInstruccion(Integer idInstruccion) {
		this.idInstruccion = idInstruccion;
	}
	
	@Column(name = "instruccion", unique = true, nullable = false)
	public String getInstruccion() {
		return this.instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	public Integer getGuionPrueba_idGuionPrueba() {
		return GuionPrueba_idGuionPrueba;
	}
	
	@Lazy(value = true)
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instruccion", cascade = CascadeType.ALL, orphanRemoval = true)
	public void setGuionPrueba_idGuionPrueba(Integer GuionPrueba_idGuionPrueba) {
		this.GuionPrueba_idGuionPrueba = GuionPrueba_idGuionPrueba;
	}
	
	public Integer getPaso_idPaso() {
		return Paso_idPaso;
	}
	
	@Lazy(value = true)
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instruccion", cascade = CascadeType.ALL, orphanRemoval = true)
	public void setPaso_idPaso(Integer paso_idPaso) {
		Paso_idPaso = paso_idPaso;
	}

}
