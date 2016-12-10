<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Elegir Colaboradores</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/proyectos/js/colaboradores.js"></script>
]]>
</head>

<body>
	<h1>Elegir Colaboradores</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<br />

	<s:form autocomplete="off" theme="simple" action="%{#pageContext.request.contextPath}/proyectos!guardarColaboradores" onsubmit="prepararEnvio()" method="post">
		<div class="form">
			<table id="tablaColaboradores" class="tablaGestion" cellspacing="0" width="100%">
				<thead>
					<th style="width: 5%;"><s:text name="colElegir" /></th>
					<th style="width: 40%;"><s:text name="colCURP" /></th>
					<th style="width: 40%;"><s:text name="colColaborador" /></th>
				</thead>
				<tbody>
					<s:iterator value="listColaboradores" var="colaborador">
						<tr>
							<td><input id="checkbox-${colaborador.curp}" type="checkbox" /></td>
							<td><s:property value="%{#colaborador.curp}" /></td>
							<td><s:property
									value="%{#colaborador.nombre + ' ' + #colaborador.apellidoPaterno + ' ' + #colaborador.apellidoMaterno}" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />
			<s:url var="urlGestionarProyectos"
				value="%{#pageContext.request.contextPath}/proyectos">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarProyectos}'" value="Cancelar" />
		</div>
		<s:hidden id="jsonColaboradoresTabla" name="jsonColaboradoresTabla"
			value="%{jsonColaboradoresTabla}" />
	</s:form>
</body>
	</html>
</jsp:root>

