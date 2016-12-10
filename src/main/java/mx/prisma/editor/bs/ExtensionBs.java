package mx.prisma.editor.bs;


import java.util.Set;

import mx.prisma.bs.AnalisisEnum.CU_CasosUso;
import mx.prisma.bs.ReferenciaEnum.TipoSeccion;
import mx.prisma.editor.dao.ExtensionDAO;
import mx.prisma.editor.dao.VerboDAO;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Extension;
import mx.prisma.editor.model.Verbo;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class ExtensionBs {

	public static void registrarExtension(Extension extension) throws Exception {
		try {
				validar(extension);
				new ExtensionDAO().registrarExtension(extension);
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
				je.printStackTrace();
				throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
	}

	private static boolean validar(Extension extension) {
		//Validaciones de unicidad
		Set<Extension> extensiones = extension.getCasoUsoOrigen().getExtiende();
		for(Extension ex : extensiones) {
			if(ex.getId() != extension.getId()) {
				if(ex.getCasoUsoDestino().getId() == extension.getCasoUsoDestino().getId()) {
					CasoUso cu = extension.getCasoUsoDestino();
					throw new PRISMAValidacionException("El punto de extensión ya existe.", "MSG7",
							new String[] { "El","Punto de extensión a", cu.getClave() + cu.getNumero() + " " + cu.getNombre()}, "claveCasoUsoDestino");
				}
			}
		}
		//Validaciones de la causa
		if(Validador.esNuloOVacio(extension.getCausa())) {
			throw new PRISMAValidacionException("El usuario no ingresó la causa.", "MSG4", null, "causa");
		}
		if(Validador.validaLongitudMaxima(extension.getCausa(), 999)) {
			throw new PRISMAValidacionException("El usuario ingreso una causa muy larga.", "MSG6", new String[] { "999",
			"caracteres"}, "causa");
		}
		
		//Validaciones de la Región de la Trayectoria
		if(Validador.esNuloOVacio(extension.getRegion())) {
			throw new PRISMAValidacionException("El usuario no ingresó la región.", "MSG4", null, "region");
		}
		if(Validador.validaLongitudMaxima(extension.getCausa(), 499)) {
			throw new PRISMAValidacionException("El usuario ingreso una región muy larga.", "MSG6", new String[] { "499",
			"caracteres"}, "causa");
		}
		return true;
	}

	public static Verbo consultaVerbo(String nombre) {
		
		Verbo verbo = null;
		try {
			verbo = new VerboDAO().consultarVerbo(nombre);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(verbo == null) {
			throw new PRISMAException("No se puede consultar el verbo por nombre.", "MSG16", new String[] { "El",
					"verbo"});
		}
		return verbo;
	}

	public static Extension consultarExtension(int idSel) {
		Extension extension = new ExtensionDAO().consultarExtension(idSel);

		if (extension == null) {
			throw new PRISMAException(
					"No se puede consultar el punto de extensión por el id.", "MSG16",
					new String[] { "El", "Punto de extensión" });
		}
		return extension;
	}

	public static void modificarExtension(Extension extension) throws Exception {
		try {
			ElementoBs.verificarEstado(extension.getCasoUsoOrigen(), CU_CasosUso.MODIFICARCASOUSO5_2);
			validar(extension);
			new ExtensionDAO().modificarExtension(extension);
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			throw new Exception();
		}
	}

	public static void eliminarExtension(Extension model) throws Exception {
		try {
			new ExtensionDAO().eliminarExtension(model);
			
		} catch (JDBCException je) {
			if(je.getErrorCode() == 1451)
			{
				throw new PRISMAException("No se puede eliminar el Punto de extensión.", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}
	
	public static void preAlmacenarObjetosToken(Extension extension) {
		TokenBs.almacenarObjetosToken(TokenBs.convertirToken_Objeto(
				extension.getRegion(), extension.getCasoUsoOrigen().getProyecto()), extension.getCasoUsoOrigen(),
				TipoSeccion.EXTENSIONES);
		extension.setRegion(TokenBs.codificarCadenaToken(extension.getRegion(), extension.getCasoUsoOrigen().getProyecto()));
		
	}
	
}
