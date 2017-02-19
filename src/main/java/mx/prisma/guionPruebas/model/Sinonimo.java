package mx.prisma.guionPruebas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Sinonimo", catalog = "PRISMA")
public class Sinonimo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idSinonimo;
	private String sinonimo;

	public Sinonimo() {
	}

	public Sinonimo(Integer idSinonimo, String sinonimo) {
		this.idSinonimo = idSinonimo;
		this.sinonimo = sinonimo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idSinonimo", unique = true, nullable = false)
	public Integer getIdSinonimo() {
		return this.idSinonimo;
	}

	public void setIdSinonimo(Integer idSinonimo) {
		this.idSinonimo = idSinonimo;
	}

	@Column(name = "sinonimo", nullable = false, length = 45)
	public String getSinonimo() {
		return this.sinonimo;
	}

	public void setSinonimo(String sinonimo) {
		this.sinonimo = sinonimo;
	}

}
