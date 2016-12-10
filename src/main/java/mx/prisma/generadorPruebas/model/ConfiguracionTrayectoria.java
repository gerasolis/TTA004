package mx.prisma.generadorPruebas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;

import mx.prisma.editor.model.CasoUso;

@Entity
@Table(name = "ConfiguracionTrayectoria", catalog = "PRISMA")
public class ConfiguracionTrayectoria implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private List<String> condicion;
	private CasoUso casoUso;

	public ConfiguracionTrayectoria() {
	}

	public ConfiguracionTrayectoria(List<String> condicion, CasoUso casoUso) {
		this.condicion = condicion;
		this.casoUso = casoUso;
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

	@Column(name = "condicion", nullable = false, length = 500)
	public List<String> getCondicion() {
		return this.condicion;
	}

	public void setCondicion(List<String> condicion) {
		this.condicion = condicion;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CasoUsoElementoid", referencedColumnName ="Elementoid", nullable = false)
	public CasoUso getCasoUso() {
		return this.casoUso;
	}

	public void setCasoUso(CasoUso casoUso) {
		this.casoUso = casoUso;
	}

}