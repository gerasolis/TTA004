package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
 */

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;

import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@Entity
@Table(name = "CasoUso", catalog = "PRISMA")
@PrimaryKeyJoinColumn(name = "Elementoid", referencedColumnName = "id")
public class CasoUso extends Elemento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String redaccionActores;
	private String redaccionEntradas;
	private String redaccionSalidas;
	private String redaccionReglasNegocio;
	private Modulo modulo;
	private Set<CasoUsoActor> actores = new HashSet<CasoUsoActor>(0);
	private Set<Salida> salidas = new HashSet<Salida>(0);
	private Set<Entrada> entradas = new HashSet<Entrada>(0);
	private Set<CasoUsoReglaNegocio> reglas = new HashSet<CasoUsoReglaNegocio>(0);
	private Set<PostPrecondicion> postprecondiciones = new HashSet<PostPrecondicion>(0);
	private Set<Trayectoria> trayectorias = new HashSet<Trayectoria>(0);
	private Set<Inclusion> incluidoEn = new HashSet<Inclusion>(0);
	private Set<Inclusion> incluye = new HashSet<Inclusion>(0);
	private Set<Extension> Extiende = new HashSet<Extension>(0);
	private Set<Extension> ExtendidoDe = new HashSet<Extension>(0);
	private Set<Revision> revisiones = new HashSet<Revision>(0);
	private ConfiguracionHttp configuracionHttp;
	private ConfiguracionBaseDatos configuracionBaseDatos;
	

	
	public CasoUso() {
	}

	public CasoUso(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento, Modulo modulo) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.modulo = modulo;
	}

	public CasoUso(String redaccionActores,
			String redaccionEntradas, String redaccionSalidas,
			String redaccionReglasNegocio, Modulo modulo) {
		this.redaccionActores = redaccionActores;
		this.redaccionEntradas = redaccionEntradas;
		this.redaccionSalidas = redaccionSalidas;
		this.redaccionReglasNegocio = redaccionReglasNegocio;
		this.modulo = modulo;
	}
	
	@StringLengthFieldValidator(message = "%{getText('MSG6',{'1000', 'caracteres'})}", trim = true, maxLength = "999", shortCircuit= true)
	@Column(name = "redaccionActores", length = 999)
	public String getRedaccionActores() {
		return this.redaccionActores;
	}

	public void setRedaccionActores(String redaccionActores) {
		this.redaccionActores = redaccionActores;
	}

	@StringLengthFieldValidator(message = "%{getText('MSG6',{'1000', 'caracteres'})}", trim = true, maxLength = "999", shortCircuit= true)
	@Column(name = "redaccionEntradas", length = 999)
	public String getRedaccionEntradas() {
		return this.redaccionEntradas;
	}

	public void setRedaccionEntradas(String redaccionEntradas) {
		this.redaccionEntradas = redaccionEntradas;
	}

	@StringLengthFieldValidator(message = "%{getText('MSG6',{'1000', 'caracteres'})}", trim = true, maxLength = "999", shortCircuit= true)
	@Column(name = "redaccionSalidas", length = 999)
	public String getRedaccionSalidas() {
		return this.redaccionSalidas;
	}

	public void setRedaccionSalidas(String redaccionSalidas) {
		this.redaccionSalidas = redaccionSalidas;
	}

	@StringLengthFieldValidator(message = "%{getText('MSG6',{'1000', 'caracteres'})}", trim = true, maxLength = "999", shortCircuit= true)
	@Column(name = "redaccionReglasNegocio", length = 999)
	public String getRedaccionReglasNegocio() {
		return this.redaccionReglasNegocio;
	}

	public void setRedaccionReglasNegocio(String redaccionReglasNegocio) {
		this.redaccionReglasNegocio = redaccionReglasNegocio;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Moduloid")
	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casouso", cascade = CascadeType.ALL, orphanRemoval = false)
	public Set<CasoUsoActor> getActores() {
		return actores;
	}

	public void setActores(Set<CasoUsoActor> actores) {
		this.actores = actores;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("id")
	public Set<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(Set<Entrada> entradas) {
		this.entradas = entradas;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)	
	public Set<Salida> getSalidas() {
		return salidas;
	}

	public void setSalidas(Set<Salida> salidas) {
		this.salidas = salidas;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)	
	public Set<CasoUsoReglaNegocio> getReglas() {
		return reglas;
	}

	public void setReglas(Set<CasoUsoReglaNegocio> reglas) {
		this.reglas = reglas;
	}
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)	
	public Set<PostPrecondicion> getPostprecondiciones() {
		return postprecondiciones;
	}

	public void setPostprecondiciones(Set<PostPrecondicion> postprecondiciones) {
		this.postprecondiciones = postprecondiciones;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso")
	@OrderBy("clave")
	public Set<Trayectoria> getTrayectorias() {
		return trayectorias;
	}

	public void setTrayectorias(Set<Trayectoria> trayectorias) {
		this.trayectorias = trayectorias;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUsoDestino")	
	public Set<Extension> getExtendidoDe() {
		return ExtendidoDe;
	}

	public void setExtendidoDe(Set<Extension> ExtendidoDe) {
		this.ExtendidoDe = ExtendidoDe;
	}
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUsoOrigen")
	public Set<Extension> getExtiende() {
		return Extiende;
	}

	public void setExtiende(Set<Extension> Extiende) {
		this.Extiende = Extiende;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUsoDestino")
	public Set<Inclusion> getIncluidoEn() {
		return incluidoEn;
	}

	public void setIncluidoEn(Set<Inclusion> incluidoEn) {
		this.incluidoEn = incluidoEn;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUsoOrigen")
	public Set<Inclusion> getIncluye() {
		return incluye;
	}

	public void setIncluye(Set<Inclusion> incluye) {
		this.incluye = incluye;
	}
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "casoUso")
	public Set<Revision> getRevisiones() {
		return revisiones;
	}

	public void setRevisiones(Set<Revision> revisiones) {
		this.revisiones = revisiones;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)
	public ConfiguracionHttp getConfiguracionHttp() {
		return configuracionHttp;
	}

	public void setConfiguracionHttp(ConfiguracionHttp configuracionHttp) {
		this.configuracionHttp = configuracionHttp;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "casoUso", cascade = CascadeType.ALL, orphanRemoval = false)
	public ConfiguracionBaseDatos getConfiguracionBaseDatos() {
		return configuracionBaseDatos;
	}

	public void setConfiguracionBaseDatos(
			ConfiguracionBaseDatos configuracionBaseDatos) {
		this.configuracionBaseDatos = configuracionBaseDatos;
	}
	
	

}
