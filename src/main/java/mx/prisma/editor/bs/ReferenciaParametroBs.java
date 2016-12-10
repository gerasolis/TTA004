package mx.prisma.editor.bs;

import java.util.HashSet;
import java.util.Set;

import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.dao.ValorMensajeParametroDAO;
import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import mx.prisma.generadorPruebas.model.ValorMensajeParametroTrayectoria;
import mx.prisma.util.PRISMAValidacionException;
import mx.prisma.util.Validador;

public class ReferenciaParametroBs {

	public static ReferenciaParametro consultarReferenciaParametro(Integer id) {
		ReferenciaParametro referencia = new ReferenciaParametroDAO().consultarReferenciaParametro(id);
		return referencia;
	}

	public static void modificarReferenciaParametro(
			ReferenciaParametro referencia, boolean validarObligatorios, Trayectoria trayectoria) {
		if(trayectoria.isAlternativa()){
			Set<ValorMensajeParametro> valores = new HashSet<ValorMensajeParametro>(0);
			if(referencia.getElementoDestino() instanceof Mensaje) {
				for(ValorMensajeParametro vmp : referencia.getValoresMensajeParametro()) {
					if(vmp.getValor() == null || vmp.getValor().isEmpty()) {
						Mensaje mensaje = (Mensaje)referencia.getElementoDestino();
						for(MensajeParametro mensajeParametro : mensaje.getParametros()) {
							if(vmp.getMensajeParametro().getId() == mensajeParametro.getId()) {
								vmp.setValor(mensajeParametro.getParametro().getNombre());
							}
						}
					}
					System.out.println("El valor de VMP es" + vmp.getValor());

					valores.add(vmp);
				}
			}
			referencia.setValoresMensajeParametro(valores);
			//validar(referencia, validarObligatorios);
			System.out.println("Agrego los valores a la referencia del BS" + referencia.getValoresMensajeParametroTrayectoria().toString());

			new ReferenciaParametroDAO().modificarReferenciaParametro(referencia);
			
		}
		else{
			Set<ValorMensajeParametro> valores = new HashSet<ValorMensajeParametro>(0);
			if(referencia.getElementoDestino() instanceof Mensaje) {
				for(ValorMensajeParametro vmp : referencia.getValoresMensajeParametro()) {
					if(vmp.getValor() == null || vmp.getValor().isEmpty()) {
						Mensaje mensaje = (Mensaje)referencia.getElementoDestino();
						for(MensajeParametro mensajeParametro : mensaje.getParametros()) {
							if(vmp.getMensajeParametro().getId() == mensajeParametro.getId()) {
								vmp.setValor(mensajeParametro.getParametro().getNombre());
							}
						}
					}
					valores.add(vmp);
				}
			}
			referencia.setValoresMensajeParametro(valores);
			validar(referencia, validarObligatorios);
			new ReferenciaParametroDAO().modificarReferenciaParametro(referencia);
		}
	}
	
	public static void modificarReferenciaParametro(
			ReferenciaParametro referencia, boolean validarObligatorios) {
		Set<ValorMensajeParametro> valores = new HashSet<ValorMensajeParametro>(0);
		if(referencia.getElementoDestino() instanceof Mensaje) {
			for(ValorMensajeParametro vmp : referencia.getValoresMensajeParametro()) {
				if(vmp.getValor() == null || vmp.getValor().isEmpty()) {
					Mensaje mensaje = (Mensaje)referencia.getElementoDestino();
					for(MensajeParametro mensajeParametro : mensaje.getParametros()) {
						if(vmp.getMensajeParametro().getId() == mensajeParametro.getId()) {
							vmp.setValor(mensajeParametro.getParametro().getNombre());
						}
					}
				}else{
					new ValorMensajeParametroDAO().modificarValorMensajeParametro(vmp);
				}
				valores.add(vmp);
			}
		}
		referencia.setValoresMensajeParametro(valores);
		validar(referencia, validarObligatorios);
		new ReferenciaParametroDAO().modificarReferenciaParametro(referencia);

	}

	private static void validar(ReferenciaParametro referencia, boolean validarObligatorios) {
		if(referencia.getElementoDestino() instanceof ReglaNegocio) {
			for(Query query : referencia.getQueries()) {
				String queryCadena = query.getQuery();
				if (validarObligatorios && Validador.esNuloOVacio(queryCadena)) {
					throw new PRISMAValidacionException(
							"El usuario no ingresó alguna query.", "MSG38", null,
							"campos");
				}
				if (Validador.validaLongitudMaxima(queryCadena, 999)) {
					throw new PRISMAValidacionException(
							"El usuario ingreso una query muy larga.", "MSG39",
							new String[] { "999", "caracteres", "la Query de " + referencia.getElementoDestino().getClave()
									+ referencia.getElementoDestino().getNumero() + " " + referencia.getElementoDestino().getNombre()}, "campos");
				}
			}
		} else if(referencia.getElementoDestino() instanceof Mensaje) { 
			for(ValorMensajeParametro valor : referencia.getValoresMensajeParametro()) {
				String valorCadena = valor.getValor();
				if (Validador.validaLongitudMaxima(valorCadena, 2000)) {
					throw new PRISMAValidacionException(
							"El usuario ingreso parámetro muy largo.", "MSG39",
							new String[] { "2000", "caracteres", "el Parámetro de " + referencia.getElementoDestino().getClave()
									+ referencia.getElementoDestino().getNumero() + " " + referencia.getElementoDestino().getNombre()}, "campos");
				}
			}
		}
		
		
	}

}
