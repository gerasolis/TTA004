package mx.prisma.editor.bs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mx.prisma.editor.dao.AtributoDAO;
import mx.prisma.editor.dao.EntradaDAO;
import mx.prisma.editor.dao.PasoDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.dao.ReglaNegocioDAO;
import mx.prisma.editor.dao.SalidaDAO;
import mx.prisma.editor.model.Atributo;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.PostPrecondicion;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Salida;
import mx.prisma.util.PRISMAException;

public class AtributoBs {
	
	public static Atributo consultarAtributo(int idAtributo1) {
		Atributo atributo = new AtributoDAO().consultarAtributo(idAtributo1);
		if (atributo == null) {
			System.out.println("No se puede consultar el atributo por el id");
		}
		return atributo;
	}
	
	public static List<String> verificarReferencias(Atributo atributo) {

		List<ReferenciaParametro> referenciasParametro;
		List<Entrada> referenciasEntrada;
		List<Salida> referenciasSalida;
		List<ReglaNegocio> referenciasReglaNegocio;

		List<String> listReferenciasVista = new ArrayList<String>();
		Set<String> setReferenciasVista = new HashSet<String>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		String casoUso = "";
		String reglaN = "";

		referenciasParametro = new ReferenciaParametroDAO().consultarReferenciasParametro(atributo);
		referenciasEntrada = new EntradaDAO().consultarReferencias(atributo);
		referenciasSalida = new SalidaDAO().consultarReferencias(atributo);
		referenciasReglaNegocio = new ReglaNegocioDAO().consultarReferencias(atributo);
		
		for (ReferenciaParametro referencia : referenciasParametro) {
			String linea = "";
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
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

			} else if (paso != null) {
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
		
		for (Salida salida : referenciasSalida) {
			String linea = "";
			casoUso = salida.getCasoUso().getClave()
					+ salida.getCasoUso().getNumero() + " "
					+ salida.getCasoUso().getNombre();
			linea = "Salidas del caso de uso " + casoUso;
				setReferenciasVista.add(linea);
			
		}
		
		for (Entrada entrada : referenciasEntrada) {
			String linea = "";
			casoUso = entrada.getCasoUso().getClave()
					+ entrada.getCasoUso().getNumero() + " "
					+ entrada.getCasoUso().getNombre();
			linea = "Entradas del caso de uso " + casoUso;
				setReferenciasVista.add(linea);
			
		}
		
		for (ReglaNegocio reglaNegocio : referenciasReglaNegocio) {
			String linea = "";
			reglaN = reglaNegocio.getClave()
					+ reglaNegocio.getNumero() + " "
					+ reglaNegocio.getNombre();
			linea = "Regla de negocio " + reglaN;
				setReferenciasVista.add(linea);
			
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

	public static Collection<? extends CasoUso> verificarCasosUsoReferencias(
			Atributo atributo) {
		List<ReferenciaParametro> referenciasParametro;
		List<Entrada> referenciasEntrada;
		List<Salida> referenciasSalida;

		List<CasoUso> listReferenciasVista = new ArrayList<CasoUso>();
		Set<CasoUso> setReferenciasVista = new HashSet<CasoUso>(0);
		PostPrecondicion postPrecondicion = null;
		Paso paso = null;

		referenciasParametro = new ReferenciaParametroDAO().consultarReferenciasParametro(atributo);
		referenciasEntrada = new EntradaDAO().consultarReferencias(atributo);
		referenciasSalida = new SalidaDAO().consultarReferencias(atributo);
		
		for (ReferenciaParametro referencia : referenciasParametro) {
			postPrecondicion = referencia.getPostPrecondicion();
			paso = referencia.getPaso();

			if (postPrecondicion != null) {
				setReferenciasVista.add(postPrecondicion.getCasoUso());

			} else if (paso != null) {
				setReferenciasVista.add(paso.getTrayectoria().getCasoUso());
			}
		}
		
		for (Salida salida : referenciasSalida) {
			setReferenciasVista.add(salida.getCasoUso());
		}
		
		for (Entrada entrada : referenciasEntrada) {
			setReferenciasVista.add(entrada.getCasoUso());			
		}
		
		listReferenciasVista.addAll(setReferenciasVista);
		return listReferenciasVista;
	}
}
