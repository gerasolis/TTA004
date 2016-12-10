package mx.prisma.editor.model;

/*
 * Sergio Ramírez Camacho 13/06/2015
 */

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class InclusionId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CasoUso casoUsoOrigen;
	private CasoUso casoUsoDestino;

	public InclusionId() {
	}

	public InclusionId(CasoUso casoUsoOrigen, CasoUso casoUsoDestino) {
		this.casoUsoOrigen = casoUsoOrigen;
		this.casoUsoDestino = casoUsoDestino;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "CasoUsoElementoid_origen", referencedColumnName = "Elementoid")
	public CasoUso getCasoUsoOrigen() {
		return casoUsoOrigen;
	}

	public void setCasoUsoOrigen(CasoUso casoUsoOrigen) {
		this.casoUsoOrigen = casoUsoOrigen;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "CasoUsoElementoid_destino", referencedColumnName = "Elementoid")
	public CasoUso getCasoUsoDestino() {
		return casoUsoDestino;
	}

	public void setCasoUsoDestino(CasoUso casoUsoDestino) {
		this.casoUsoDestino = casoUsoDestino;
	}


}
