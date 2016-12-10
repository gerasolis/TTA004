<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Punto de extensión</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.atwho.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/extensiones/js/token.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/extensiones/js/index-edit.js"></script>
	
]]>

</head>
<body>
	<h1>Modificar Punto de extensión</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<s:set var="obs" value="observaciones" />

	<s:if test="%{#obs != null}">
		<div class="formulario">
			<div class="tituloObservaciones">Observaciones</div>
			<div class="observaciones">
				<s:property value="#obs" />
			</div>
		</div>
	</s:if>

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmExtension" theme="simple" action="%{#pageContext.request.contextPath}/extensiones/%{idSel}"  method="post">
	<s:hidden name="_method" value="put" />
		<div class="formulario">
			<s:hidden value="%{idCU}" name="idCU" />
			<div class="tituloFormulario">Información general del Punto de
				extensión</div>
			<table class="seccion">
				<tr>
					<td class="label obligatorio"><s:text name="labelCasoUsoExtiende" /></td>
					<td><s:select name="idCasoUsoDestino" id="model.idCu" headerValue="Seleccione" headerKey="-1" 
							list="catalogoCasoUso" listKey="id" 
							listValue="%{clave + numero + ' ' +  nombre}" cssErrorClass="select-error" cssClass="inputFormulario ui-widget"></s:select>
							<s:fielderror fieldName="claveCasoUsoDestino" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelCausa" /></td>
					<td><s:textarea name="model.causa" id="model.idCausa" cssErrorClass="input-error" maxlength="999" 
					cssClass="inputFormularioExtraGrande ui-widget"></s:textarea> <s:fielderror
							fieldName="model.causa" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelRegion" /></td>
					<td><s:textarea name="model.region" id="inputor" cssErrorClass="input-error" maxlength="499" 
					cssClass="inputFormularioExtraGrande ui-widget"></s:textarea> <s:fielderror
							fieldName="region" cssClass="error" theme="jquery" /></td>
				</tr>				
			</table>
		</div>

		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />

			<s:url var="urlGestionarExtensiones"
				value="%{#pageContext.request.contextPath}/extensiones">
				<s:param name="idCU" value="%{idCU}" />
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarExtensiones}'"
				value="Cancelar" />
		</div>
		<s:hidden name="jsonPasos" id="jsonPasos" value="%{jsonPasos}"/>
		
	</s:form>

</body>
</html>
</jsp:root>
