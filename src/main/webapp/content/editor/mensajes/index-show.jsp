<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Mensaje</title>

</head>
<body>
	<h1>Consultar Mensaje</h1>
	<h3><s:property
			value="model.clave + ' ' + model.numero + ' ' + model.nombre" /></h3>
	
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br/>
		<div class="formulario">
			<div class="tituloFormulario">${blanks}</div>
			<div class="seccion">
				<h4><s:property
			value="model.clave + ' ' + model.numero + ' ' + model.nombre" /></h4>
				<p class="instrucciones"><s:property value="model.descripcion"/></p>
			</div>
		</div>
		<div class="formulario">
			<div class="tituloFormulario">${blanks}</div>
			<div class="seccion">
			<table >
				<tr>
					<td class="definicion">
						<span class="labelIzq consulta"><s:text name="labelRedaccion"/></span>
						<span class="inputFormulario ui-widget">${blanks}
						<s:property value="model.redaccion"/></span>
					</td>
				</tr>
			</table>
			<s:if test="existenParametros">
			<table>
				<tr>	
					<td colspan="2"><span class="labelIzq consulta"><s:text name="labelParametros"/></span>
						<span class="inputFormulario ui-widget">${blanks}
						El mensaje tiene los siguientes parámetros:</span></td>
						<br/>
				</tr>
				<s:iterator value="model.parametros" var="parametro">
				<tr>
					<td style="padding-left: 2em">
						<span class="labelIzq consulta">
							${parametro.nombre}
						</span> <span
						class="ui-widget inputFormulario"> ${blanks} ${parametro.descripcion}</span>
						
					</td>
				</tr>
				</s:iterator>
			</table>
			</s:if>
			</div>
		</div>
		
		<br />
		<div align="center">
			<input class="boton" type="button"
				onclick="location.href='${urlPrev}'"
				value="Aceptar" />
		</div>	
</body>
	</html>
</jsp:root>