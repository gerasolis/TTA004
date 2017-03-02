<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<title>Guión de prueba</title>
		</head>
		<body>
			<h1>Guión de prueba</h1>
			<h3><s:property value="%{casoUso.clave + ' ' + casoUso.numero + ' ' + casoUso.nombre}" /></h3>
			<s:actionmessage theme="jquery" />
			<s:actionerror theme="jquery" />
			
			<div class="formulario">
				<div class="tituloFormulario">${blanks}</div>
				<div class="seccion">
					<h4><s:property value="%{casoUso.clave + ' ' + casoUso.numero + ' ' + casoUso.nombre}" /></h4>
					<h5>
						<s:text name="labelResumen"></s:text>
					</h5>
					<p class="instrucciones">
						<s:text name="casoUso.descripcion"></s:text>
					</p>
					<br />
					<table class="tablaDescripcion">
						<tr>
							<td colspan="4" class="encabezadoTabla" align="center">
								Proyecto: <s:property value="%{proyecto.clave + ' - ' + proyecto.nombre}" /><p></p>
								Módulo: <s:property value="%{modulo.clave + ' - ' + modulo.nombre}" /><p></p>
								Caso de uso: <s:property value="%{casoUso.clave + casoUso.numero + ' ' + casoUso.nombre}" />
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center">
								Proyecto <s:property value="%{proyecto.clave + ' - ' + proyecto.nombre}" />
								- <s:property value="%{casoUso.clave + casoUso.numero + ' ' + casoUso.nombre}" />
								con validación de datos, reglas de negocio, mensajes y botones asociados.
							</td>
						</tr>
						<tr>
							<td class="ui-widget inputFormulario">Pregunta</td>
							<td >Sí</td>
							<td >No</td>
							<td class="ui-widget inputFormulario">Observaciones</td>
						</tr>
						<s:iterator value="instrucciones" status="incr">
							<tr>
								<s:set var="pregunta"><s:property value="@mx.prisma.guionPruebas.controller.GuionPruebasCtrl@esInstruccion(instrucciones[#incr.index])"/></s:set>
								<s:if test="%{pregunta}">
									<td colspan="4" class="ui-widget">
										${instrucciones[incr.index]}
									</td>
								</s:if>
								<s:else>
									<td class="ui-widget">
										${instrucciones[incr.index]}
									</td><td></td><td></td><td></td>
								</s:else>
							</tr>
						</s:iterator>
						
					</table>
				</div>
			</div>
			
			<!-- REGRESAMOS A LA PANTALLA ANTERIOR (LISTADO DE LOS CU) -->
			<br />
			<div align="center">
				<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/cu">
				</s:url>
				<input class="boton" type="button"
					onclick="location.href='${urlGestionarCU}'"
					value="Aceptar" />
			</div>
		</body>
	</html>
</jsp:root>