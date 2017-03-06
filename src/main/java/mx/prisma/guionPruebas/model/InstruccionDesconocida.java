package mx.prisma.guionPruebas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "InstruccionDesconocida", catalog = "PRISMA")
public class InstruccionDesconocida implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idInstruccionDesconocida;
	private String instruccion;
	private Integer Sinonimo_idSinonimo;
	private Integer Paso_idPaso;

	public InstruccionDesconocida() {
	}

	public InstruccionDesconocida(String instruccion, Integer Sinonimo_idSinonimo, Integer Paso_idPaso) {
		this.instruccion = instruccion;
		this.Sinonimo_idSinonimo = Sinonimo_idSinonimo;
		this.Paso_idPaso = Paso_idPaso;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idInstruccionDesconocida", unique = true, nullable = false)
	public Integer getIdInstruccionDesconocida() {
		return this.idInstruccionDesconocida;
	}

	public void setIdInstruccionDesconocida(Integer idInstruccionDesconocida) {
		this.idInstruccionDesconocida = idInstruccionDesconocida;
	}
	
	@Column(name = "instruccion", unique = true, nullable = false)
	public String getInstruccion() {
		return this.instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	@JoinColumn(name = "Sinonimo_idSinonimo", referencedColumnName = "idSinonimo")
	public Integer getSinonimo_idSinonimo() {
		return Sinonimo_idSinonimo;
	}
	
	public void setSinonimo_idSinonimo(Integer sinonimo_idSinonimo) {
		Sinonimo_idSinonimo = sinonimo_idSinonimo;
	}
	
	@JoinColumn(name = "Paso_idPaso", referencedColumnName = "id")
	public Integer getPaso_idPaso() {
		return Paso_idPaso;
	}
	
	public void setPaso_idPaso(Integer paso_idPaso) {
		Paso_idPaso = paso_idPaso;
	}

}
