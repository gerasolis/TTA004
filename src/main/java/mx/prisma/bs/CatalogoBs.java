package mx.prisma.bs;

import java.util.List;

import mx.prisma.bs.ReferenciaEnum.TipoCatalogo;
import mx.prisma.editor.model.Cardinalidad;
import mx.prisma.editor.model.TipoDato;
import mx.prisma.editor.model.TipoReglaNegocio;
import mx.prisma.editor.model.Verbo;

public class CatalogoBs {
	private static String otro = "Otro";
	private static String otra = "Otra";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void opcionOtro(List lista, TipoCatalogo tipoCatalogo) {
		boolean cambio;
			switch (tipoCatalogo){
			case CARDINALIDAD:
				Cardinalidad cardinalidad = null;
				cambio = false;
				lista:
				for (Object objeto : lista) {
					cardinalidad = (Cardinalidad)objeto;
					if (cardinalidad.getNombre().equals(otro) || cardinalidad.getNombre().equals(otra)) {
						
						cambio = true;
						break lista;
					}
				}	
				if(cambio) {
					lista.remove(cardinalidad);
					lista.add(cardinalidad);
				}
				break;
			case ESTADOELEMENTO:
				break;
			case ESTADOPROYECTO:
				break;
			case OPERADOR:
				break;
			case PARAMETRO:
				break;
			case ROL:
				break;
			case TIPOACCION:
				break;
			case TIPOCOMPARACION:
				break;
			case TIPODATO:
				TipoDato tipoDato = null;
				cambio = false;
				lista:
				for (Object objeto : lista) {
					tipoDato = (TipoDato)objeto;
					if (tipoDato.getNombre().equals(otro) || tipoDato.getNombre().equals(otra)) {	
						cambio = true;
						break lista;
					}
				}	
				if(cambio) {
					lista.remove(tipoDato);
					lista.add(tipoDato);
				}
				break;
			case TIPOPARAMETRO:
				break;
			case TIPOREGLANEGOCIO:
				TipoReglaNegocio tipoRegla = null;
				cambio = false;
				lista:
				for (Object objeto : lista) {
					tipoRegla = (TipoReglaNegocio)objeto;
					if (tipoRegla.getNombre().equals(otro) || tipoRegla.getNombre().equals(otra)) {	
						cambio = true;
						break lista;
					}
				}	
				if(cambio) {
					lista.remove(tipoRegla);
					lista.add(tipoRegla);
				}
				break;
			case UNIDADTAMANIO:
				break;
			case VERBO:
				Verbo verbo = null;
				cambio = false;
				lista:
				for (Object objeto : lista) {
					verbo = (Verbo)objeto;
					if (verbo.getNombre().equals(otro) || verbo.getNombre().equals(otra)) {	
						cambio = true;
						break lista;
					}
				}	
				if(cambio) {
					lista.remove(verbo);
					lista.add(verbo);
				}
				break;
			default:
				break;
			
			}
		
	}

}
