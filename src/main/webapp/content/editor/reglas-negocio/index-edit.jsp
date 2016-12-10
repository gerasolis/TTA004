<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Regla de negocio</title>

<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/reglas-negocio/js/index-edit.js"></script>	
]]>

</head>
<body>
	<h1>Modificar Regla de negocio</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Ingrese la información solicitada.</p>

	<s:form autocomplete="off" id="frmReglasNegocio" theme="simple"
		action="%{#pageContext.request.contextPath}/reglas-negocio/%{idSel}"
		method="post" onsubmit="return prepararEnvio();">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Información general de la Regla
				de negocio</div>
			<table class="seccion">
				<tr>
					<td class="label"><s:text name="labelClave" /></td>
					<td class="labelDerecho"><s:property value="model.clave" /> <s:fielderror
							fieldName="model.clave" cssClass="error" theme="jquery" /></td>
					<s:hidden value="%{model.clave}" name="model.clave" />
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelNumero" /></td>
					<td><s:textfield name="model.numero" maxlength="20"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName="model.numero" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelNombre" /></td>
					<td><s:textfield name="model.nombre" maxlength="200"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName="model.nombre" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label"><s:text name="labelDescripcion" /></td>
					<td><s:textarea rows="5" name="model.descripcion"
							cssClass="inputFormularioExtraGrande ui-widget" maxlength="999"
							cssErrorClass="input-error"></s:textarea> <s:fielderror
							fieldName="model.descripcion" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelTipo" /></td>
					<td><s:select list="listTipoRN"
							cssClass="inputFormulario ui-widget" name="idTipoRN"
							id="idTipoRN" listKey="id" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							onchange="mostrarCamposTipoRN(); cargarCamposTipoRN();" value="idTipoRN"></s:select>
							<s:fielderror fieldName="idTipoRN" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr id="filaTextoAyudaTipoRN" class="oculto">
					<td></td>
					<td id="instrucciones" class="textoAyuda"></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelRedaccion" /></td>
					<td><s:textarea rows="5" name="model.redaccion"
							cssClass="inputFormularioExtraGrande ui-widget" id="model.redaccion"
							maxlength="999" cssErrorClass="input-error"></s:textarea> <s:fielderror
							fieldName="model.redaccion" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaTextoAyudaInterF" class="oculto">
					<td></td>
					<td class="textoAyuda">A continuación se muestran las
						entidades que poseen atributos de tipo fecha.</td>
				</tr>
				<!-- FORMATO CORRECTO -->
				<tr id="filaEntidadFormato" class="oculto">
					<td class="label obligatorio"><s:text name="labelEntidad" /></td>
					<td><s:select list="listEntidades"
							cssClass="inputFormulario ui-widget" name="idEntidadFormato"
							id="entidadFormato" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id" onchange="cargarAtributos(this, 'atributoFormato');"
							value="model.atributoExpReg.entidad.id"></s:select> <s:fielderror
							fieldName="idEntidadFormato" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaAtributoFormato" class="oculto">
					<td class="label obligatorio"><s:text name="labelAtributo" /></td>
					<td><s:select list="listAtributos"
							cssClass="inputFormulario ui-widget" name="idAtributoFormato"
							id="atributoFormato" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id" value="model.atributoExpReg.id"></s:select> <s:fielderror
							fieldName="idAtributoFormato" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaExpresionRegular">
					<td class="label obligatorio"><s:text name="labelExpReg" /></td>
					<td><s:textfield name="model.expresionRegular" maxlength="200"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName="model.expresionRegular" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<!-- UNICIDAD DE PARÁMETROS -->
				<tr id="filaEntidadUnicidad" class="oculto">
					<td class="label obligatorio"><s:text name="labelEntidad" /></td>
					<td><s:select autocomplete="off" list="listEntidades"
							cssClass="inputFormulario ui-widget" name="idEntidadUnicidad"
							id="entidadUnicidad" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id"
							onchange="cargarAtributos(this, 'atributoUnicidad');"/> <s:fielderror
							fieldName="idEntidadUnicidad" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaAtributoUnicidad" class="oculto">
					<td class="label obligatorio"><s:text name="labelAtributo" /></td>
					<td><s:select list="listAtributos"
							cssClass="inputFormulario ui-widget" name="idAtributoUnicidad"
							id="atributoUnicidad" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id"/> <s:fielderror
							fieldName="idAtributoUnicidad" cssClass="error" theme="jquery" /></td>
				</tr>

				<!-- COMPARACIÓN DE ATRIBUTOS -->
				<tr id="filaEntidad1" class="oculto">
					<td class="label obligatorio"><s:text name="labelEntidad1" /></td>
					<td><s:select list="listEntidades"
							cssClass="inputFormulario ui-widget" name="idEntidad1"
							id="entidad1" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id" onchange="cargarAtributos(this, 'atributo1');"
							value="idEntidad1"></s:select> <s:fielderror
							fieldName="idEntidad1" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaAtributo1" class="oculto">
					<td class="label obligatorio"><s:text name="labelAtributo1" /></td>
					<td><s:select list="listAtributos"
							cssClass="inputFormulario ui-widget" name="idAtributo1"
							id="atributo1" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id"
							onchange="cargarOperadores(this);cargarEntidadesDependientes(this, 'entidad2');"
							value="idAtributo1"></s:select> <s:fielderror
							fieldName="idAtributo1" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaOperador" class="oculto">
					<td class="label obligatorio"><s:text
							name="labelTipoComparacion" /></td>
					<td><s:select list="listOperadores"
							cssClass="inputFormulario ui-widget" name="idOperador"
							id="operador" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="simbolo"
							listKey="id" value="idOperador"></s:select>
							<s:fielderror
							fieldName="idOperador" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaEntidad2" class="oculto">
					<td class="label obligatorio"><s:text name="labelEntidad2" /></td>
					<td><s:select list="listEntidades2"
							cssClass="inputFormulario ui-widget" name="idEntidad2"
							id="entidad2" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id"
							onchange="cargarAtributosDependientes(this, 'atributo2');"
							value="idEntidad2"></s:select> <s:fielderror
							fieldName="idEntidad2" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaAtributo2" class="oculto">
					<td class="label obligatorio"><s:text name="labelAtributo2" /></td>
					<td><s:select list="listAtributos2"
							cssClass="inputFormulario ui-widget" name="idAtributo2"
							id="atributo2" cssErrorClass="select-error"
							headerValue="Seleccione" headerKey="-1" listValue="nombre"
							listKey="id" value="idAtributo2"></s:select> <s:fielderror
							fieldName="idAtributo2" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr id="filaResultado" class="oculto">
					<td class="label"><s:text name="labelResultado" /></td>
					<td class="textoAyuda"><span id="resultado"></span></td>
				</tr>

			</table>
		</div>
		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />

			<input class="boton" type="button"
				onclick="location.href='${pageContext.request.contextPath}/reglas-negocio'"
				value="Cancelar" />
		</div>
		<s:hidden name="jsonAtributos" id="jsonAtributos"
			value="%{jsonAtributos}" />
		<s:hidden name="jsonEntidades" id="jsonEntidades"
			value="%{jsonEntidades}" />
		<s:hidden name="jsonOperadores" id="jsonOperadores"
			value="%{jsonOperadores}" />
		<s:hidden name="jsonAtributos2" id="jsonAtributos2"
			value="%{jsonAtributos2}" />
		<s:hidden name="jsonEntidades2" id="jsonEntidades2"
			value="%{jsonEntidades2}" />
			
		<s:hidden id="idEntidadFormato"
			value="%{idEntidadFormato}" />
		<s:hidden id="idAtributoFormato"
			value="%{idAtributoFormato}" />
		
		<s:hidden id="idEntidadUnicidad"
			value="%{idEntidadUnicidad}" />
		<s:hidden id="idAtributoUnicidad"
			value="%{idAtributoUnicidad}" />
			
		<s:hidden id="idEntidad1"
			value="%{idEntidad1}" />
		<s:hidden id="idAtributo1"
			value="%{idAtributo1}" />
		<s:hidden id="idEntidad2"
			value="%{idEntidad2}" />
		<s:hidden id="idAtributo2"
			value="%{idAtributo2}" />
			
		<s:hidden id="idOperador"
			value="%{idOperador}" />
			
			
	</s:form>
	<!-- COMENTARIOS DE LA ACTUALIZACIÓN -->
	<sj:dialog id="mensajeConfirmacion" title="Confirmación"
		autoOpen="false" minHeight="300" minWidth="800" modal="true"
		draggable="true">
		<s:form autocomplete="off" id="frmComentario"
			name="frmComentario" theme="simple">
			<div class="formulario">
				<div class="tituloFormulario">Comentarios de la modificación</div>
				<div class="seccion">
					<p class="instrucciones">Ingrese un comentario referente a la
						modificación realizada.</p>

				<table>
					<tr>
						<td class="label obligatorio"><s:text name="labelComentarios" /></td>
						<td><s:textarea rows="5" name="comentarioDialogo"
								id="comentarioDialogo" maxlength="999"
								cssErrorClass="input-error" cssClass="inputFormulario ui-widget" /></td>
					</tr>
				</table>
				</div>
			</div>
			<br />
			<div align="center">
				<input type="button" onclick="enviarComentarios()" value="Aceptar" />
				<input type="button" onclick="cancelarRegistroComentarios()"
					value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>
</body>
	</html>
</jsp:root>