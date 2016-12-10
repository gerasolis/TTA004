package mx.prisma.bs;

import mx.prisma.admin.dao.RolDAO;
import mx.prisma.admin.model.Rol;

public class RolBs {
	public enum Rol_Enum {
		LIDER, ANALISTA
	}
	public static int consultarIdRol(Rol_Enum rolEnum) {
		switch(rolEnum) {
		case ANALISTA:
			return 2;
		case LIDER:
			return 1;
		default:
			return -1;
		
		}
	}
	
	public static Rol findById(Integer id) {
		return new RolDAO().consultarRol(id);
	}
}
