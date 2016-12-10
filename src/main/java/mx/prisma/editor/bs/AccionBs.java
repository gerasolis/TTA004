package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.AccionDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class AccionBs {

	public static List<String> verificarReferencias(Accion model, Modulo modulo) {

		List<ReferenciaParametro> referenciasParametro;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);

		PostPrecondicion postPrecondicion = null; // Origen de la referencia
		Paso paso = null; // Origen de la referencia
		String casoUso = "";

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null && (modulo == null || postPrecondicion.getCasoUso().getModulo().getId() != modulo.getId())) {
				casoUso = postPrecondicion.getCasoUso().getClave()
						+ postPrecondicion.getCasoUso().getNumero() + " "
						+ postPrecondicion.getCasoUso().getNombre();
				if (postPrecondicion.isPrecondicion()) {
					linea = "Precondiciones del caso de uso " + casoUso;
				} else {
					linea = "Postcondiciones del caso de uso "
							+ postPrecondicion.getCasoUso().getClave()
							+ postPrecondicion.getCasoUso().getNumero() + " "
							+ postPrecondicion.getCasoUso().getNombre();
				}

			} else if (paso != null && (modulo == null || paso.getTrayectoria().getCasoUso().getModulo().getId() != modulo.getId())) {
				casoUso = paso.getTrayectoria().getCasoUso().getClave()
						+ paso.getTrayectoria().getCasoUso().getNumero() + " "
						+ paso.getTrayectoria().getCasoUso().getNombre();
				linea = "Paso "
						+ paso.getNumero()
						+ " de la trayectoria "
						+ ((paso.getTrayectoria().isAlternativa()) ? "alternativa "
								+ paso.getTrayectoria().getClave()
								: "principal") + " del caso de uso " + casoUso;
			}

			if (linea != "") {
				setReferenciasVista.add(linea);
			}
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}
	
	public static List<CasoUso> verificarCasosUsoReferencias(Accion model) {

		List<ReferenciaParametro> referenciasParametro;

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);

		PostPrecondicion postPrecondicion = null; // Origen de la referencia
		Paso paso = null; // Origen de la referencia

		referenciasParametro = new ReferenciaParametroDAO()
				.consultarReferenciasParametro(model);

		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
				setReferenciasVista.add(postPrecondicion.getCasoUso());

			} else if (paso != null) {
				setReferenciasVista.add(paso.getTrayectoria().getCasoUso());
			}
		}

		listReferenciasVista.addAll(setReferenciasVista);

		return listReferenciasVista;
	}

	public static Accion consultarAccion(Integer id) {
		Accion accion = new AccionDAO().consultarAccion(id);
		if (accion == null) {
			throw new PRISMAException(
					"No se puede consultar la acción por el id.");
		}
		return accion;
	}

	public static void modificarAccion(Accion accion, boolean validarObligatorios) throws Exception{
		validar(accion, validarObligatorios);
		new AccionDAO().modificarAccion(accion);
		
	}

	private static void validar(Accion accion, boolean validarObligatorios) {
		String valor = accion.getUrlDestino();
		if (validarObligatorios && Validador.esNuloOVacio(valor)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la url destino.", "MSG38", null,
					"campos");
		}
		if (Validador.validaLongitudMaxima(valor, 2000)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una url destino muy larga.", "MSG39",
					new String[] { "2000", "caracteres", "la Url destino de la Acción " + accion.getNombre() }, "campos");
		}
		
		valor = accion.getMetodo();
		if (validarObligatorios && Validador.esNuloOVacio(valor)) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el valor del método.", "MSG38", null,
					"campos");
		}
		if (Validador.validaLongitudMaxima(valor, 10)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un método muy largo.", "MSG39",
					new String[] { "10", "caracteres", "el Método de la Acción " + accion.getNombre() }, "campos");
		}
		
	}

}
