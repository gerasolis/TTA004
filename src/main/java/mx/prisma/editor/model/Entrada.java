package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
 */
import static javax.persistence.GenerationType.IDENTITY;

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
import javax.persistence.UniqueConstraint;

import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;


@Entity
@Table(name = "Entrada", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CasoUsoElementoid", "Atributoid", "TerminoGlosarioElementoid" }))
public class Entrada implements java.io.Serializable, Comparable<Entrada> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private TipoParametro tipoParametro;
	private CasoUso casoUso;
	private Atributo atributo;
	private TerminoGlosario terminoGlosario;
	private Set<ValorEntrada> valores;
	private String nombreHTML;
	private Set<ValorEntradaTrayectoria> valoresEntradaTrayectoria;

	public Entrada() {
	}

	public Entrada(TipoParametro tipoParametro, CasoUso casoUso) {
		this.tipoParametro = tipoParametro;
		this.casoUso = casoUso;
	}

	public Entrada(int numeroToken, TipoParametro tipoParametro, CasoUso casoUso,
			Atributo atributo, TerminoGlosario terminoGlosario) {
		this.tipoParametro = tipoParametro;
		this.casoUso = casoUso;
		this.atributo = atributo;
		this.terminoGlosario = terminoGlosario;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TipoParametroid", referencedColumnName = "id")
	public TipoParametro getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(TipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CasoUsoElementoid", referencedColumnName = "Elementoid")
	public CasoUso getCasoUso() {
		return casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Atributoid", referencedColumnName = "id")
	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TerminoGlosarioElementoid", referencedColumnName = "Elementoid")
	public TerminoGlosario getTerminoGlosario() {
		return terminoGlosario;
	}

	public void setTerminoGlosario(TerminoGlosario terminoGlosario) {
		this.terminoGlosario = terminoGlosario;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entrada", cascade = CascadeType.ALL, orphanRemoval = true) 
	public Set<ValorEntrada> getValores() {
		return valores;
	}

	public void setValores(Set<ValorEntrada> valores) {
		this.valores = valores;
	}
	
	
	
	@Column(name = "nombreHTML", nullable = true)
	public String getNombreHTML() {
		return nombreHTML;
	}

	public void setNombreHTML(String nombreHTML) {
		this.nombreHTML = nombreHTML;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "entrada", cascade = CascadeType.ALL, orphanRemoval = true) 
	public Set<ValorEntradaTrayectoria> getValoresEntradaTrayectoria() {
		return valoresEntradaTrayectoria;
	}

	public void setValoresEntradaTrayectoria(Set<ValorEntradaTrayectoria> valoresEntradaTrayectoria) {
		this.valoresEntradaTrayectoria = valoresEntradaTrayectoria;
	}

	public int compareTo(Entrada o) {
		Integer.compare(this.getId(), o.getId());
		return 0;
	}
	

}
