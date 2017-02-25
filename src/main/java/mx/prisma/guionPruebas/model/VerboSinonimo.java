package mx.prisma.guionPruebas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "VerboSinonimo", catalog = "PRISMA")
public class VerboSinonimo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idVerboSinonimo;
	private Integer Sinonimo_idSinonimo;
	private Integer Verbo_idVerbo;

	public VerboSinonimo() {
	}

	public VerboSinonimo(Integer idVerboSinonimo, Integer Sinonimo_idSinonimo, Integer Verbo_idVerbo) {
		this.idVerboSinonimo = idVerboSinonimo;
		this.Sinonimo_idSinonimo = Sinonimo_idSinonimo;
		this.Verbo_idVerbo = Verbo_idVerbo;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idVerboSinonimo", unique = true, nullable = false)
	public Integer getIdVerboSinonimo() {
		return this.idVerboSinonimo;
	}

	public void setIdVerboSinonimo(Integer idVerboSinonimo) {
		this.idVerboSinonimo = idVerboSinonimo;
	}

	@JoinColumn(name = "Sinonimo_idSinonimo", referencedColumnName = "idSinonimo")
	public Integer getSinonimo_idSinonimo() {
		return Sinonimo_idSinonimo;
	}
	
	public void setSinonimo_idSinonimo(Integer sinonimo_idSinonimo) {
		Sinonimo_idSinonimo = sinonimo_idSinonimo;
	}
	
	@JoinColumn(name = "Sinonimo_idSinonimo", referencedColumnName = "idSinonimo")
	public Integer getVerbo_idVerbo() {
		return Verbo_idVerbo;
	}
	
	public void setVerbo_idVerbo(Integer verbo_idVerbo) {
		Verbo_idVerbo = verbo_idVerbo;
	}

}
