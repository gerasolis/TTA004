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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.generadorPruebas.model.ValorPantallaTrayectoria;

@Entity
@Table(name = "Pantalla", catalog = "PRISMA")
@PrimaryKeyJoinColumn(name = "Elementoid", referencedColumnName = "id")
public class Pantalla extends Elemento implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] imagen;
	private Modulo modulo;
	private Set<Accion> acciones = new HashSet<Accion>(0);
	private String patron;
	private Set<ValorPantallaTrayectoria> valoresPantallaTrayectoria;

	public Pantalla() {
	}

	public Pantalla(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento, Modulo modulo) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.modulo = modulo;
	}

	public Pantalla(String clave, String numero, String nombre,
			Proyecto proyecto, String descripcion, EstadoElemento estadoElemento,byte[] imagen, Modulo modulo) {
		super(clave, numero, nombre, proyecto, descripcion, estadoElemento);
		this.imagen = imagen;
		this.modulo = modulo;
	}

	@Column(name = "imagen", length = 999)
	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Moduloid", referencedColumnName = "id")	
	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pantalla", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<Accion> getAcciones() {
		return acciones;
	}

	public void setAcciones(Set<Accion> acciones) {
		this.acciones = acciones;
	}

	@Column(name = "patron", length = 2000)
	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pantalla")
	public Set<ValorPantallaTrayectoria> getValoresPantallaTrayectoria() {
		return valoresPantallaTrayectoria;
	}


	public void setValoresPantallaTrayectoria(
			Set<ValorPantallaTrayectoria> valoresPantallaTrayectoria) {
		this.valoresPantallaTrayectoria = valoresPantallaTrayectoria;
	}
	

	

}
