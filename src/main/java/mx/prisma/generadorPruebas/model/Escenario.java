package mx.prisma.generadorPruebas.model;

// Generated 05-nov-2015 19:22:29 by Hibernate Tools 4.0.0

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Escenario", catalog = "PRISMA")
public class Escenario implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Set<EscenarioValorEntrada> valoresEntrada = new HashSet<EscenarioValorEntrada>(0);


	public Escenario() {
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "escenario", cascade = CascadeType.ALL, orphanRemoval = false)
	public Set<EscenarioValorEntrada> getValoresEntrada() {
		return valoresEntrada;
	}

	public void setValoresEntrada(Set<EscenarioValorEntrada> valoresEntrada) {
		this.valoresEntrada = valoresEntrada;
	}

	
}
