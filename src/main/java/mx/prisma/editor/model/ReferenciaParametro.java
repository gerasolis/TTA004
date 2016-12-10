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

import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametroTrayectoria;


@Entity
@Table(name = "ReferenciaParametro", catalog = "PRISMA")
public class ReferenciaParametro implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private TipoParametro tipoParametro;
	private Set<ValorMensajeParametro> valoresMensajeParametro; 
	private Set<Query> queries;
	private Set<ValorMensajeParametroTrayectoria> valoresMensajeParametroTrayectoria;
	
	// Entidad que hizo la referencia:
	private PostPrecondicion postPrecondicion;
	private Paso paso;
	private Extension extension;
	
	// Entidad a la que se hizo referencia
	private Paso pasoDestino;
	private Trayectoria trayectoria;
	private Elemento elementoDestino;
	private Accion accionDestino;
	private Atributo atributo;

	public ReferenciaParametro() {
	}


	public ReferenciaParametro(TipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
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
	@JoinColumn(name = "PostPrecondicionid", referencedColumnName="id", nullable = true)
	public PostPrecondicion getPostPrecondicion() {
		return postPrecondicion;
	}

	public void setPostPrecondicion(PostPrecondicion postPrecondicion) {
		this.postPrecondicion = postPrecondicion;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Pasoid", referencedColumnName="id", nullable = true)
	public Paso getPaso() {
		return paso;
	}

	public void setPaso(Paso paso) {
		this.paso = paso;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Extensionid", referencedColumnName="id", nullable = true)
	public Extension getExtension() {
		return extension;
	}

	public void setExtension(Extension extension) {
		this.extension = extension;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PasoidDestino", referencedColumnName="id", nullable = true)
	public Paso getPasoDestino() {
		return pasoDestino;
	}

	public void setPasoDestino(Paso pasoDestino) {
		this.pasoDestino = pasoDestino;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ElementoidDestino", referencedColumnName="id", nullable = true)
	public Elemento getElementoDestino() {
		return elementoDestino;
	}

	public void setElementoDestino(Elemento elementoDestino) {
		this.elementoDestino = elementoDestino;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AccionidDestino", referencedColumnName="id", nullable = true)
	public Accion getAccionDestino() {
		return accionDestino;
	}

	public void setAccionDestino(Accion accionDestino) {
		this.accionDestino = accionDestino;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Atributoid", referencedColumnName="id", nullable = true)
	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TipoParametroid", referencedColumnName="id", nullable = true)
	public TipoParametro getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(TipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Trayectoriaid", referencedColumnName="id", nullable = true)
	public Trayectoria getTrayectoria() {
		return trayectoria;
	}

	public void setTrayectoria(Trayectoria trayectoria) {
		this.trayectoria = trayectoria;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "referenciaParametro", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ValorMensajeParametro> getValoresMensajeParametro() {
		return valoresMensajeParametro;
	}


	public void setValoresMensajeParametro(
			Set<ValorMensajeParametro> valoresMensajeParametro) {
		this.valoresMensajeParametro = valoresMensajeParametro;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "referenciaParametro", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Query> getQueries() {
		return queries;
	}

	public void setQueries(Set<Query> queries) {
		this.queries = queries;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "referenciaParametro", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<ValorMensajeParametroTrayectoria> getValoresMensajeParametroTrayectoria() {
		return valoresMensajeParametroTrayectoria;
	}


	public void setValoresMensajeParametroTrayectoria(
			Set<ValorMensajeParametroTrayectoria> valoresMensajeParametroTrayectoria) {
		this.valoresMensajeParametroTrayectoria = valoresMensajeParametroTrayectoria;
	}

	
	
	

	
}
