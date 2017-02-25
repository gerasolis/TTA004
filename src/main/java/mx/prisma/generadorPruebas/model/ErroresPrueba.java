package mx.prisma.generadorPruebas.model;

import static javax.persistence.GenerationType.IDENTITY;

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
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReglaNegocio;

@Entity
@Table(name = "ErroresPrueba", catalog = "PRISMA")
public class ErroresPrueba implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tipoError;
	private int numError;
	private double porcentaje;
	private double porcentajeTodo;
	private Prueba pruebaid;
	private Mensaje mensajeid;
	private Paso pasoid;
	
	public ErroresPrueba(){
		
	}
	public ErroresPrueba(String tipoError,int numError,double porcentaje,double porcentajeTodo,Prueba pruebaid,Mensaje mensajeid,Paso pasoid) {
		this.tipoError = tipoError;
		this.numError = numError;
		this.porcentaje = porcentaje;
		this.porcentajeTodo = porcentajeTodo;
		this.pruebaid = pruebaid;
		this.mensajeid = mensajeid;
		this.pasoid = pasoid;
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
	
	@Column(name = "tipoError", length = 2000)
	public String getTipoError() {
		return this.tipoError;
	}

	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}
	
	@Column(name = "numError")
	public int getNumError() {
		return this.numError;
	}

	public void setNumError(int numError) {
		this.numError = numError;
	}
	
	@Column(name = "porcentaje")
	public double getPorcentaje() {
		return this.porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	@Column(name = "porcentajeTodo")
	public double getPorcentajeTodo() {
		return this.porcentajeTodo;
	}

	public void setPorcentajeTodo(double porcentajeTodo) {
		this.porcentajeTodo = porcentajeTodo;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Pruebaid", referencedColumnName = "id")		
	public Prueba getPruebaid() {
		return this.pruebaid;
	}

	public void setPruebaid(Prueba pruebaid) {
		this.pruebaid = pruebaid;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Mensajeid", referencedColumnName = "Elementoid")		
	public Mensaje getMensajeid() {
		return this.mensajeid;
	}

	public void setMensajeid(Mensaje mensajeid) {
		this.mensajeid = mensajeid;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Pasoid", referencedColumnName = "id")		
	public Paso getPasoid() {
		return this.pasoid;
	}

	public void setPasoid(Paso pasoid) {
		this.pasoid = pasoid;
	}
}
