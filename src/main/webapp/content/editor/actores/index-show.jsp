<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Actor</title>
</head>
<body>
	<h1>Consultar Actor</h1>
	<h3>
		<s:property value="model.nombre" />
	</h3>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />


	<div class="formulario">
		<div class="tituloFormulario">${blanks}</div>
		<div class="seccion">
			<h4><s:property value="model.nombre"/></h4>
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
					<td><span class="label consulta"><s:text
								name="labelCardinalidad" /></span> <span
						class="inputFormulario ui-widget"> ${blanks} <s:if
								test="model.cardinalidad.nombre != 'Otra'">
								<s:property value="model.cardinalidad.nombre" />
							</s:if> <s:else>
								<s:property value="model.otraCardinalidad" />
							</s:else></span></td>
				</tr>
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