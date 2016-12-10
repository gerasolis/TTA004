<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Recuperar contraseña</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/cu/js/index.js"></script>
]]>
</head>
<body>
	<h1>Recuperar contraseña</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" theme="simple"
		action="%{pageContext.request.contextPath}/access!sendPassword">
		<div class="formulario">
			<div class="tituloFormulario">Recuperar contraseña</div>
			<table class="seccion">

				<tr>
					<td class="label obligatorio"><s:text name="labelCorreo" /></td>
					<td><s:textfield name="userName" maxlength="45"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName="userName" cssClass="error" theme="jquery" /></td>
				</tr>
			</table>
		</div>

		<div align="center">
			<s:submit class="boton" value="Aceptar" />
			<s:url var="urlAccess"
				value="%{#pageContext.request.contextPath}/access">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlAccess}'" value="Cancelar" />
		</div>
	</s:form>
</body>
	</html>
</jsp:root>

