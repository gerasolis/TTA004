<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Regla de negocio</title>
</head>
<body>
	<h1>Consultar Regla de negocio</h1>
	<h3>
		<s:property
			value="model.clave + ' ' + model.numero + ' ' + model.nombre" />
	</h3>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<div class="formulario">
		<div class="tituloFormulario">${blanks}</div>
		<div class="seccion">
			<h4>
				<s:property
					value="model.clave + ' ' + model.numero + ' ' + model.nombre" />
			</h4>
			<p class="instrucciones">
				<s:property value="model.descripcion" />
			</p>
		</div>
	</div>
	<div class="formulario">
	<div class="tituloFormulario">${blanks}</div>	
		<div class="seccion">
		<table>
				<tr>
					<td class="definicion"><span class="labelIzq consulta"><s:text
								name="labelRedaccion" /></span> <span
						class="ui-widget inputFormulario"> ${blanks} <s:property
								value="model.redaccion" /></span></td>
				</tr>

				<s:if
					test="model.tipoReglaNegocio.nombre == 'Comparación de atributos'">
					<tr>
						<td class="ui-widget inputFormulario"><span class="ecuacion">
								<s:property value="model.atributoComp1.nombre" /> <s:property
									value="model.operadorComp.simbolo" /> <s:property
									value="model.atributoComp2.nombre" />
						</span></td>
					</tr>
				</s:if>
				<s:elseif test="model.tipoReglaNegocio.nombre == 'Formato correcto'">
					<tr>
						<td class="definicion"><span class="labelIzq consulta"><s:text
								name="labelExpReg" /></span> <span
						class="ui-widget inputFormulario"> ${blanks} <s:property
								value="model.expresionRegular" /></span></td>
					</tr>
					<tr>
						<td class="definicion"><span class="labelIzq consulta"><s:text
								name="labelAtributo" /></span> <span
						class="ui-widget inputFormulario"> ${blanks} <s:property
								value="model.atributoExpReg.nombre" /></span></td>
					</tr>
				</s:elseif>
				<s:elseif
					test="model.tipoReglaNegocio.nombre == 'Unicidad de parámetros'">
					<tr>
						<td class="ui-widget inputFormulario"><span
							class="labelIzq consulta"><s:text name="labelEntidad" /></span>
							<span class="ui-widget inputFormulario"> ${blanks}
							<s:property value="model.atributoUnicidad.entidad.nombre" />
						</span></td>
					</tr>
					<tr>
						<td class="ui-widget inputFormulario"><span
							class="labelIzq consulta"><s:text name="labelAtributo" /></span>
							<span class="ui-widget inputFormulario"> ${blanks} <s:property
									value="model.atributoUnicidad.nombre" />
						</span></td>
					</tr>
				</s:elseif>
			</table>
		</div>
	</div>
	<br />
	<div align="center">

		<input class="boton" type="button"
			onclick="location.href='${urlPrev}'" value="Aceptar" />
	</div>
</body>
	</html>
</jsp:root>