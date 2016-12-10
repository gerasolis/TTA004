package mx.prisma.generadorPruebas.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;

import mx.prisma.admin.model.Proyecto;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.Elemento;
import mx.prisma.editor.model.EstadoElemento;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Trayectoria;


@Entity
@Table(name = "ValorPantallaTrayectoria", catalog = "PRISMA")
public class ValorPantallaTrayectoria extends Elemento implements java.io.Serializable{

	
	
	private static final long serialVersionUID = 1L;
	private Trayectoria trayectoria;
	//private Elemento elemento;
	private String patron;
	private Pantalla pantalla;

	public ValorPantallaTrayectoria() {
	}


	public ValorPantallaTrayectoria(Trayectoria trayectoria, Pantalla pantalla, String patron ){
		this.trayectoria = trayectoria;
		this.pantalla = pantalla;
		this.patron = patron;
	}


	@Column(name = "patron", length = 2000)
	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}
	

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "TrayectoriaPantallaid", referencedColumnName = "id")		
	public Trayectoria getTrayectoria() {
		return this.trayectoria;
	}

	public void setTrayectoria(Trayectoria trayectoria) {
		this.trayectoria = trayectoria;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "PantallaTrayectoriaid", referencedColumnName = "Elementoid")		
	public Pantalla getPantalla() {
		return this.pantalla;
	}

	public void setPantalla(Pantalla pantalla) {
		this.pantalla = pantalla;
	}
}
