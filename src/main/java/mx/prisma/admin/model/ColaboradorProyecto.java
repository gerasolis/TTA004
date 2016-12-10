package mx.prisma.admin.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
 */

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Colaborador_Proyecto", catalog = "PRISMA", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ColaboradorCURP", "Proyectoid" }))
public class ColaboradorProyecto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Colaborador colaborador;
	private Rol rol;
	private Proyecto proyecto;

	public ColaboradorProyecto() {
	}

	public ColaboradorProyecto(Colaborador colaborador, Rol rol,
			Proyecto proyecto) {
		this.colaborador = colaborador;
		this.rol = rol;
		this.proyecto = proyecto;

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ColaboradorCURP", nullable = false)
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Rolid", nullable = false)
	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Proyectoid")
	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

}
