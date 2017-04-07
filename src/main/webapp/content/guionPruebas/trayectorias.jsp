<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Gestionar Guiones de Prueba</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/proyectos/js/guionesprueba.js"></script>
]]>
</head>

<body>
	<h1>Gestionar Guiones de Prueba</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<br />

	<s:form autocomplete="off" theme="simple" action="%{#pageContext.request.contextPath}/guion-pruebas!seleccionarGuiones" onsubmit="prepararEnvio()" method="post">
		<div class="form">
			<table id="tablaGuionesPrueba" class="tablaGestion" cellspacing="0" width="100%">
				<thead>
					<th style="width: 5%;"><s:text name="colElegir" /></th>
					<th></th>
					<th style="width: 70%;"><s:text name="colGuionPrueba" /></th>
					<th style="width: 25%;">Seleccionar trayectorias</th>
				</thead>
				<tbody>
					<s:iterator value="casosUso" var="casoUso" status="indice">
						<tr>
							<td align="center"><s:checkbox id="checkbox-%{#casoUso.id}" name="%{#casoUso.id}" checked="false"/></td>
							<td><s:property value="%{#casoUso.id}" /></td>
							<td><s:property value="%{#casoUso.clave + #casoUso.numero + ' ' +#casoUso.nombre}" /></td>
							<td>
								<s:select list="trayectorias" multiple="true" listValue="clave" cssClass="inputFormulario" name="valorSel" id="valor-%{#casoUso.id}" 
		       						cssErrorClass="input-error" listKey="id"></s:select>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<br />
		<div align="center">
			<s:submit class="boton" value="Descargar" method="post" />
			<s:url var="urlGestionarProyectos"
				value="%{#pageContext.request.contextPath}/proyectos!descargarGuion?idSel=%{#proyecto.id}">
				<s:param name="extension">pdf</s:param>
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlPrev}'" value="Cancelar" />
		</div>
		<s:hidden id="jsonGuionesTabla" name="jsonGuionesTabla"
			value="%{jsonGuionesTabla}" />
		<s:hidden id="idProyecto" name="idProyecto"
			value="%{proyecto.id}" />
	</s:form>
</body>
	</html>
</jsp:root>

