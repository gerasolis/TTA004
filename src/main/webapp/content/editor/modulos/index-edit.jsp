<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Módulo</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/modulos/js/index-edit.js"></script>	
]]>

</head>
<body>

	<h1>Modificar Módulo</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmTermino" theme="simple"
		action="%{#pageContext.request.contextPath}/modulos/%{idSel}" method="post">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Información general del Módulo</div>
			<table class="seccion">
				<tr>
					<td class="label obligatorio"><s:text name="labelClave" /></td>
					<td class="labelDerecho"><s:property value="model.clave"/></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelNombre" /></td>
					<td><s:textfield name="model.nombre" maxlength="45"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName ="model.nombre" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelDescripcion" /></td>
					<td><s:textarea name="model.descripcion" maxlength="999"
							cssErrorClass="input-error" cssClass="inputFormularioExtraGrande ui-widget" />
						<s:fielderror fieldName ="model.descripcion" cssClass="error"
							theme="jquery" /></td>
				</tr>
			</table>
		</div>
		
		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar"/>

			<s:url var="urlGestion"
				value="%{#pageContext.request.contextPath}/modulos">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestion}'"
				value="Cancelar" />
		</div>
	</s:form>
	
</body>
	</html>
</jsp:root>