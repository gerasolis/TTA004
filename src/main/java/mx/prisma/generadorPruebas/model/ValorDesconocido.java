package mx.prisma.generadorPruebas.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.ReglaNegocio;

@Entity
@Table(name = "ValorDesconocido", catalog = "PRISMA")
public class ValorDesconocido implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String ruta;
	private Integer valorGuion;

	public ValorDesconocido() {
	}

	public ValorDesconocido(int id, String ruta, int valorGuion) {
		this.id = id;
		this.ruta = ruta;
		this.valorGuion = valorGuion;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "id", unique = true, nullable = false)
	@JoinColumn(name = "id", referencedColumnName = "id",unique = true, nullable = false)	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ruta")
	public String getRuta() {
		return this.ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Column(name = "valorGuion")
	public int getValorGuion() {
		return this.valorGuion;
	}

	public void setValorGuion(int valorGuion) {
		this.valorGuion = valorGuion;
	}

}
