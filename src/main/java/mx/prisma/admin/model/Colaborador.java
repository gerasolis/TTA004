package mx.prisma.admin.model;

/*
 * Sergio Ramírez Camacho 07/06/2015
 */

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Colaborador", catalog = "PRISMA")
public class Colaborador implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curp;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String correoElectronico;
	private String contrasenia;
	private boolean administrador;
	private Set<ColaboradorProyecto> colaborador_proyectos = new HashSet<ColaboradorProyecto>(0);
	private Set<Telefono> telefonos = new HashSet<Telefono>(0);
	
	public Colaborador() {
	}

	public Colaborador(String curp, String nombre, String apellidoPaterno,
			String apellidoMaterno, String correoElectronico, String contrasenia, boolean administrador) {
		this.curp = curp;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.correoElectronico = correoElectronico;
		this.contrasenia = contrasenia;
		this.administrador = administrador;
	}

	@Id
	@Column(name = "CURP", unique = true, nullable = false, length = 18)
	public String getCurp() {
		return this.curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	@Column(name = "nombre", nullable = false, length = 45)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "apellidoPaterno", nullable = false, length = 45)
	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	@Column(name = "apellidoMaterno", nullable = false, length = 45)
	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@Column(name = "correoElectronico", nullable = false, length = 45)
	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Column(name = "contrasenia", nullable = false, length = 20)
	public String getContrasenia() {
		return this.contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "colaborador", cascade = CascadeType.ALL)
	public Set<ColaboradorProyecto> getColaborador_proyectos() {
		return colaborador_proyectos;
	}

	public void setColaborador_proyectos(
			Set<ColaboradorProyecto> colaborador_proyectos) {
		this.colaborador_proyectos = colaborador_proyectos;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "colaborador", cascade = CascadeType.ALL)
	public Set<Telefono> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Set<Telefono> telefonos) {
		this.telefonos = telefonos;
	}
	
	@Column(name = "administrador", nullable = false, length = 20)
	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	
}
