package mx.prisma.admin.bs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mx.prisma.admin.dao.ColaboradorDAO;
import mx.prisma.admin.dao.EstadoProyectoDAO;
import mx.prisma.admin.dao.ProyectoDAO;
import mx.prisma.admin.dao.RolDAO;
import mx.prisma.admin.model.Colaborador;
import mx.prisma.admin.model.ColaboradorProyecto;
import mx.prisma.admin.model.EstadoProyecto;
import mx.prisma.admin.model.Proyecto;
import mx.prisma.admin.model.Rol;
import mx.prisma.bs.EstadoProyectoEnum;
import mx.prisma.bs.RolBs;
import mx.prisma.bs.RolBs.Rol_Enum;
import mx.prisma.util.PRISMAException;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

public class ProyectoBs {

	public static Proyecto consultarProyecto(Integer idSel) {
		Proyecto proyecto = new ProyectoDAO().consultarProyecto(idSel);
		if(proyecto == null) {
			throw new PRISMAException("No se puede consultar el proyecto.",
					"MSG13");
		}
		return proyecto;
	}

	public static List<Proyecto> consultarProyectos() {
		List<Proyecto> proyectos = new ProyectoDAO().consultarProyectos();
		if(proyectos == null) {
			throw new PRISMAException("No se pueden consultar los proyectos.",
					"MSG13");
		}
		return proyectos;
	}

	public static void registrarProyecto(Proyecto model) throws Exception {
		try {
			validar(model);
			new ProyectoDAO().registrarProyecto(model);
		} catch (JDBCException je) {
			if (je.getErrorCode() == 1062) {
				throw new PRISMAValidacionException("El Proyecto"
						+ model.getClave() + " " + model.getNombre() + " ya existe.", "MSG7");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	private static void validar(Proyecto model) {
		// Validaciones de la clave
		if (Validador.esNuloOVacio(model.getClave())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la clave del proyecto.", "MSG4",
					null, "model.clave");
		}
		if (Validador.validaLongitudMaxima(model.getClave(), 10)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una clave muy larga.", "MSG6",
					new String[] { "10", "caracteres" }, "model.clave");
		}
		// Validaciones del nombre
		if (Validador.esNuloOVacio(model.getNombre())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó el nombre del proyecto.", "MSG4",
					null, "model.nombre");
		}
		if (Validador.validaLongitudMaxima(model.getNombre(), 50)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso un nombre muy largo.", "MSG6",
					new String[] { "50", "caracteres" }, "model.nombre");
		}
		//Se asegura la unicidad del nombre y clave
		List<Proyecto> proyectosBD = new ProyectoDAO().consultarProyectos();
		for(Proyecto proy : proyectosBD) {
			if(model.getId() != proy.getId()) {
				if(model.getClave().equals(proy.getClave())) {
					throw new PRISMAValidacionException(
							"La clave del proyecto ya existe.",
							"MSG7",
							new String[] { "El", "Proyecto", model.getClave() },
							"model.clave");
				}
				if(model.getNombre().equals(proy.getNombre())) {
					throw new PRISMAValidacionException(
							"El nombre del proyecto ya existe.",
							"MSG7",
							new String[] { "El", "Proyecto", model.getNombre() },
							"model.nombre");
				}
			}
		}
		// Validaciones de las fechas
		if (Validador.esNulo(model.getFechaInicioProgramada())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la fecha de inicio programada.", "MSG4",
					null, "model.fechaInicioProgramada");
		}
		if (Validador.esNulo(model.getFechaTerminoProgramada())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la fecha de término programada.", "MSG4",
					null, "model.fechaTerminoProgramada");
		}
		if (model.getFechaInicio() != null && model.getFechaTermino() != null && Validador.esInvalidoOrdenFechas(model.getFechaInicio(), model.getFechaTermino())) {
			throw new PRISMAValidacionException(
					"El usuario ingresó en desorden las fechas.", "MSG35",
					new String[] { "fecha de término", "fecha de inicio" }, "model.fechaTermino");
		}
		if (model.getFechaInicioProgramada() != null && model.getFechaTerminoProgramada() != null && Validador.esInvalidoOrdenFechas(model.getFechaInicioProgramada(), model.getFechaTerminoProgramada())) {
			throw new PRISMAValidacionException(
					"El usuario ingresó en desorden las fechas.", "MSG35",
					new String[] { "fecha de término programada", "fecha de inicio programada" }, "model.fechaTerminoProgramada");
		}
		// Validaciones de la descripción
		if (Validador.esNuloOVacio(model.getDescripcion())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la descripción del proyecto.", "MSG4",
					null, "model.descripcion");
		}
		if (Validador.validaLongitudMaxima(model.getDescripcion(), 999)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una descripción muy larga.", "MSG6",
					new String[] { "999", "caracteres" }, "model.descripcion");
		}
		// Validaciones de la contraparte
		if (Validador.esNuloOVacio(model.getContraparte())) {
			throw new PRISMAValidacionException(
					"El usuario no ingresó la contraparte del proyecto.", "MSG4",
					null, "model.contraparte");
		}
		if (Validador.validaLongitudMaxima(model.getContraparte(), 45)) {
			throw new PRISMAValidacionException(
					"El usuario ingreso una contraparte muy larga.", "MSG6",
					new String[] { "45", "caracteres" }, "model.contraparte");
		}
		
		
		
				
	}

	public static List<EstadoProyecto> consultarEstadosProyecto() {
		List<EstadoProyecto> estados = new EstadoProyectoDAO().consultarEstadosProyecto();
		if(estados == null) {
			throw new PRISMAException("No se pueden consultar los estados de proyectos.",
					"MSG13");
		}
		return estados;
	}

	public static void eliminarProyecto(Proyecto model) throws Exception {
		try {
			new ProyectoDAO().eliminarProyecto(model);
			
		} catch (JDBCException je) {
			if(je.getErrorCode() == 1451)
			{
				throw new PRISMAException("No se puede eliminar el proyecto.", "MSG14");
			}
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	public static void agregarEstado(Proyecto model, int idEstadoProyecto) {
		EstadoProyecto estado = new EstadoProyectoDAO().consultarEstadoProyecto(idEstadoProyecto);
		model.setEstadoProyecto(estado);
	}

	public static void agregarLider(Proyecto proyecto, String curpLider) {
		Colaborador liderVista = new ColaboradorDAO().consultarColaborador(curpLider);
		ColaboradorProyecto colaboradorproyecto = null;
		ColaboradorProyecto lider = null;
		int idLider = RolBs.consultarIdRol(Rol_Enum.LIDER);
		Rol rolLider = new RolDAO().consultarRol(idLider);
		
		for (ColaboradorProyecto colaborador : proyecto.getProyecto_colaboradores()) {
			if (colaborador.getRol().getId() == idLider) {
				lider = colaborador;
			}
			if (curpLider.equals(colaborador.getColaborador().getCurp())) {
				colaboradorproyecto = colaborador;
			}
		}
		
		for (ColaboradorProyecto colaborador : proyecto.getProyecto_colaboradores()) {
			if (curpLider.equals(colaborador.getColaborador().getCurp())) {
				colaboradorproyecto = colaborador;
			}
		}

		
		if (colaboradorproyecto == null) {
			colaboradorproyecto = new ColaboradorProyecto(liderVista, rolLider, proyecto);
			proyecto.getProyecto_colaboradores().add(colaboradorproyecto);
			proyecto.getProyecto_colaboradores().remove(lider);
			
		} else 
			if (lider.getId() == colaboradorproyecto.getId()){
				colaboradorproyecto.setRol(rolLider);
			} else {
				colaboradorproyecto.setRol(rolLider);
				proyecto.getProyecto_colaboradores().remove(lider);
		}
		
	}


	public static ColaboradorProyecto consultarColaboradorProyectoLider(Proyecto model) {
		Set<ColaboradorProyecto> colaboradores_proyecto = model.getProyecto_colaboradores();
		int idLider = RolBs.consultarIdRol(Rol_Enum.LIDER);
		for(ColaboradorProyecto cp : colaboradores_proyecto) {
			if(cp.getRol().getId() == idLider) {
				return cp;
			}
		}
		return null;
	}

	public static List<EstadoProyecto> consultarEstadosProyectoRegistro() {
		List<EstadoProyecto> estadosAux = new EstadoProyectoDAO().consultarEstadosProyecto();
		List<EstadoProyecto> estados = new ArrayList<EstadoProyecto>();
		int idEstadoTerminado = EstadoProyectoEnum.consultarIdEstadoProyecto(EstadoProyectoEnum.EstadoProyecto.TERMINADO);
				
		if(estadosAux == null) {
			throw new PRISMAException("No se pueden consultar los estados de proyectos.",
					"MSG13");
		}
		for(EstadoProyecto ep : estadosAux) {
			if(ep.getId() != idEstadoTerminado) {
				estados.add(ep);
			}
		}
		
		return estados;
	}

	public static void modificarProyecto(Proyecto model) throws Exception {
		try {
			validar(model);
			new ProyectoDAO().modificarProyecto(model);
		} catch (JDBCException je) {
			System.out.println("ERROR CODE " + je.getErrorCode());
			je.printStackTrace();
			throw new Exception();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}
		
	}

	public static List<Proyecto> findByColaborador(Colaborador colaborador) throws Exception {
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		try {
			proyectos = new ProyectoDAO().findByColaborador(colaborador.getCurp());
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new Exception();
		}

		return proyectos;
	}
}
