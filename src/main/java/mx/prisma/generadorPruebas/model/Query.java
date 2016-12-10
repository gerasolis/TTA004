package mx.prisma.generadorPruebas.model;

// Generated 05-nov-2015 19:22:29 by Hibernate Tools 4.0.0

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mx.prisma.editor.model.ReferenciaParametro;


@Entity
@Table(name = "Query", catalog = "PRISMA")
public class Query implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String query;
	private ReferenciaParametro referenciaParametro;

	public Query() {
	}

	public Query(String query, ReferenciaParametro referenciaParametro) {
		this.query = query;
		this.referenciaParametro = referenciaParametro;
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

	@Column(name = "query", nullable = false, length = 999)
	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ReferenciaParametroid", referencedColumnName = "id")	
	public ReferenciaParametro getReferenciaParametro() {
		return this.referenciaParametro;
	}

	public void setReferenciaParametro(ReferenciaParametro referenciaParametro) {
		this.referenciaParametro = referenciaParametro;
	}

}
