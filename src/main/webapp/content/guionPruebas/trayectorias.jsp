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
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/guionPruebas/js/cu.js"></script>
]]>
</head>

<body>
	<h1>Generar documento de guiones de prueba</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<br />

	<s:form autocomplete="off" theme="simple" action="%{#pageContext.request.contextPath}/guion-pruebas!seleccionarGuiones"
	method="post" onsubmit="return prepararEnvio();">
		<div class="formulario">
			<div class="tituloFormulario">Casos de Uso</div>
			<div class="seccion">
				
				<table id="tablaPaso" class="tablaGestion" cellspacing="0"
					width="100%">
					<s:hidden name="numPasos" value="%{casosUso.length}" id="numPasos" />
					<thead>
						<tr>
							<th style="width: 5%;">Orden</th>
							<th style="width: 55%;"><s:text name="colCasoUso" /></th>
							<th style="width: 25%;">Seleccionar trayectorias</th>
							<th style="width: 10%;"><s:text name="colAcciones" /></th>
							<th style="width: 0;"><s:text name=""/></th>
							<th style="width: 0;"><s:text name=""/></th>
							<th style="width: 0;"><s:text name=""/></th>
							<th style="width: 0;"><s:text name=""/></th>	
						</tr>
					</thead>

				</table>
			</div>
		</div>
		<br />
		<div align="center">
			<s:submit class="boton" value="Descargar" />
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
		<s:hidden id="jsonPasosTabla" name="jsonPasosTabla"
			value="%{jsonPasosTabla}" />
	</s:form>
</body>
	</html>
</jsp:root>

