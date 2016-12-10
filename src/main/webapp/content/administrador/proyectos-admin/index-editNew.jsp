<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Proyecto</title>
<![CDATA[
  	<script src="${pageContext.request.contextPath}/resources/template/themes/smoothness-prisma/jquery-ui.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/administrador/proyectos-admin/js/index-editNew.js"></script>	
]]>

</head>
<body>

	<h1>Registrar Proyecto</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmProyecto" theme="simple"
		action="%{#pageContext.request.contextPath}/proyectos-admin" method="post">
		<div class="formulario">
			<div class="tituloFormulario">Información general del Proyecto</div>
			<div class="seccion">
				<table>
					<tr>
						<td class="label obligatorio"><s:text name="labelClave" /></td>
						<td><s:textfield name="model.clave" maxlength="10"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="model.clave" cssClass="error"
								theme="jquery" /></td>
					</tr>
					<tr>
						<td class="label obligatorio"><s:text name="labelNombre" /></td>
						<td><s:textfield name="model.nombre" maxlength="45"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="model.nombre" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>

						<td class="label"><s:text name="labelFechaInicio" /></td>
						<td><s:date name="model.fechaInicio" var="fechaInicioFormato" />
							<s:textfield name="model.fechaInicio" id="fechaInicio"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget"
								readonly="true" value="%{#fechaInicioFormato}" /> <s:fielderror
								fieldName="model.fechaInicio" cssClass="error" theme="jquery" /></td>
					</tr>

					<tr>
						<td class="label"><s:text name="labelFechaTermino" /></td>
						<td><s:date name="model.fechaTermino"
								var="fechaTerminoFormato" /> <s:textfield
								name="model.fechaTermino" id="fechaTermino"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget"
								readonly="true" value="%{#fechaTerminoFormato}" /> <s:fielderror
								fieldName="model.fechaTermino" cssClass="error" theme="jquery" /></td>
					</tr>

					<tr>
						<td class="label obligatorio"><s:text
								name="labelFechaInicioProg" /></td>
						<td><s:date name="model.fechaInicioProgramada"
								var="fechaInicioProgramadaFormato" />
							<s:textfield name="model.fechaInicioProgramada"
								id="fechaInicioProgramada" cssErrorClass="input-error"
								cssClass="inputFormulario ui-widget" readonly="true"
								value="%{#fechaInicioProgramadaFormato}" /> <s:fielderror
								fieldName="model.fechaInicioProgramada" cssClass="error"
								theme="jquery" /></td>
					</tr>

					<tr>
						<td class="label obligatorio"><s:text
								name="labelFechaTerminoProg" /></td>
						<td><s:date name="model.fechaTerminoProgramada"
								var="fechaTerminoProgramadaFormato" />
							<s:textfield name="model.fechaTerminoProgramada"
								id="fechaTerminoProgramada" cssErrorClass="input-error"
								cssClass="inputFormulario ui-widget" readonly="true"
								value="%{#fechaTerminoProgramadaFormato}" /> <s:fielderror
								fieldName="model.fechaTerminoProgramada" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>
						<td class="label obligatorio"><s:text name="labelLider" /></td>
						<td><s:select name="curpLider" list="listPersonas" headerValue="Seleccione" headerKey="-1"
								listKey="curp" listValue="nombre + ' ' + apellidoPaterno + ' ' + apellidoMaterno" value="curpLider"
								cssErrorClass="select-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="curpLider" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>
						<td class="label obligatorio"><s:text name="labelDescripcion" /></td>
						<td><s:textarea name="model.descripcion" maxlength="999"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="model.descripcion" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>
						<td class="label obligatorio"><s:text name="labelContraparte" /></td>
						<td><s:textfield name="model.contraparte" maxlength="45"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="model.contraparte" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>
						<td class="label"><s:text name="labelPresupuesto" /></td>
						<td><s:textfield name="model.presupuesto"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="model.presupuesto" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
					<tr>
						<td class="label obligatorio"><s:text name="labelEstado" /></td>
						<td><s:select name="idEstadoProyecto" list="listEstadosProyecto" headerValue="Seleccione" headerKey="-1"
								listKey="id" listValue="nombre" value="idEstadoProyecto"
								cssErrorClass="select-error" cssClass="inputFormulario ui-widget" />
							<s:fielderror fieldName ="idEstadoProyecto" cssClass="error"
								theme="jquery" /></td>
					</tr>
					
				</table>
			</div>
		</div>
		
		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />

			<s:url var="urlGestionarProyectos"
				value="%{#pageContext.request.contextPath}/proyectos-admin">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarProyectos}'"
				value="Cancelar" />
		</div>
	</s:form>
</body>
	</html>
</jsp:root>