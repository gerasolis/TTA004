<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Actor</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.atwho.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/actores/js/index-editNew.js"></script>	
]]>

</head>
<body>

	<h1>Registrar Actor</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/actores" method="post">
		<div class="formulario">
			<div class="tituloFormulario">Información general del Actor</div>
			<table class="seccion">
				<tr>
					<td class="label obligatorio"><s:text name="labelNombre" /></td>
					<td><s:textfield name="model.nombre" maxlength="200"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName ="model.nombre" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelDescripcion" /></td>
					<td><s:textarea name="model.descripcion" maxlength="200"
							cssErrorClass="input-error" cssClass="inputFormularioExtraGrande ui-widget" />
						<s:fielderror fieldName ="model.descripcion" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
				<td class="label obligatorio"><s:text name="labelCardinalidad" /></td>
				<td><s:select list="listCardinalidad" cssClass="inputFormulario" name="model.cardinalidad.id" id="cardinalidad"
       						cssErrorClass="select-error" listKey="id" headerValue="Seleccione" headerKey="-1" listValue="nombre" onchange="verificarOtro();"></s:select>
       						<s:fielderror fieldName = "model.cardinalidad.id" cssClass="error"
							theme="jquery" />
				</td>
				</tr>
				
				<tr style="display: none;" id = "otro">
				<td class="label obligatorio"><s:text name="labelOtro" /></td>
					<td><s:textfield name="model.otraCardinalidad" id="otraCardinalidad" maxlength="10"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName = "model.otraCardinalidad" cssClass="error"
							theme="jquery" /></td>
				</tr>
			</table>
		</div>
		
		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />

			<s:url var="urlGestionarActores"
				value="%{#pageContext.request.contextPath}/actores">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarActores}'"
				value="Cancelar" />
		</div>
	</s:form>
</body>
	</html>
</jsp:root>