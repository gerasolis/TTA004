package mx.prisma.generadorPruebas.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;

@Entity
@Table(name = "Prueba", catalog = "PRISMA")
public class Prueba implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String fecha;
	private Integer estado;
	private CasoUso CasoUsoid;
	
	public Prueba(){
		
	}
	public Prueba(String fecha,CasoUso idCasoUso) {
		this.fecha = fecha;
		this.CasoUsoid = idCasoUso;
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
	
	@Column(name = "fecha")
	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CasoUsoid", referencedColumnName = "Elementoid")		
	public CasoUso getCasoUsoid() {
		return this.CasoUsoid;
	}

	public void setCasoUsoid(CasoUso CasoUsoid) {
		this.CasoUsoid = CasoUsoid;
	}
	
	@Column(name = "estado")
	public Integer getEstado() {
		return this.estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}
