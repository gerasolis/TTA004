package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.model.Extension;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.util.PRISMAException;

public class PasoBs {
	public static List<String> verificarReferencias(Paso model, Modulo modulo) {

		List<ReferenciaParametro> referenciasParametro;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;
		Extension extension = null;

		String casoUso = "";
		Integer idSelf = null;

		referenciasParametro = new ReferenciaParametroDAO().consultarReferenciasParametro(model);
		
		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();
			extension = referencia.getExtension();

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
				idSelf = paso.getId();
				casoUso = paso.getTrayectoria().getCasoUso().getClave()
						+ paso.getTrayectoria().getCasoUso().getNumero() + " "
						+ paso.getTrayectoria().getCasoUso().getNombre();
				linea = "Paso "
						+ paso.getNumero()
						+ " de la trayectoria "
						+ ((paso.getTrayectoria().isAlternativa()) ? "alternativa "
								+ paso.getTrayectoria().getClave()
								: "principal") + " del caso de uso " + casoUso;
			} else if (extension != null && (modulo == null || extension.getCasoUsoOrigen().getModulo().getId() != modulo.getId())) {
				casoUso = extension.getCasoUsoOrigen().getClave()
						+ extension.getCasoUsoOrigen().getNumero() + " "
						+ extension.getCasoUsoOrigen().getNombre();
				linea = "Puntos de extensión del caso de uso " + casoUso;
			}
			if (linea != "" && idSelf != model.getId()) {
				setReferenciasVista.add(linea);
			}
		}
		listReferenciasVista.addAll(setReferenciasVista);
		return listReferenciasVista;
	}

	public static boolean isListado(List<Integer> enteros, Integer entero) {
		for (Integer i : enteros) {
			if (i == entero) {
				return true;
			}
		}
		return false;
	}

	public static Paso consultarPaso(Integer id) {
		Paso paso = null;
		try {
			paso = new PasoDAO().consultarPaso(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (paso == null) {
			throw new PRISMAException(
					"No se puede consultar el paso por el id.", "MSG16",
					new String[] { "El", "paso" });
		}
		return paso;
	}
}
