<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Entidad</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.atwho.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/entidades/js/index-edit.js"></script>	
]]>

</head>
<body>

	<h1>Modificar Entidad</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmEntidad" theme="simple"
		action="%{#pageContext.request.contextPath}/entidades/%{idSel}" method="post" onsubmit="return preparaEnvio();">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Información general de la Entidad</div>
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
					<td><s:textarea name="model.descripcion" maxlength="999"
							cssErrorClass="input-error" cssClass="inputFormularioExtraGrande ui-widget" />
						<s:fielderror fieldName ="model.descripcion" cssClass="error"
							theme="jquery" /></td>
				</tr>
			</table> 
		</div>
		<div class="formulario">
			<div class="tituloFormulario">Atributos de la Entidad</div>
			<div class="seccion">
				<s:fielderror fieldName="model.atributos" cssClass="error"
					theme="jquery" />
				<table id="tablaAtributo" class="tablaGestion" cellspacing="0"
					width="100%">
					<thead>
						<tr>
							<th style="width: 40%;"><s:text name="colAtributo" /></th>
							<th style="width: 30%;"><s:text name="colTipoDato" /></th>
							<th style="width: 10%;"><s:text name="colObligatorio" /></th>
						
							<!--  Columnas ocultas -->
							<th style="width: 0%;"><s:text name="colTipoDatoSeleccionado" /></th>
							<th style="width: 0%;"><s:text name="colOtroTipoDato" /></th>
							<th style="width: 0%;"><s:text name="colDescripcion" /></th>
							<th style="width: 0%;"><s:text name="colLongitud" /></th>
							<th style="width: 0%;"><s:text name="colFormatoArchivo" /></th>
							<th style="width: 0%;"><s:text name="colTamanioArchivo" /></th>
							<th style="width: 0%;"><s:text name="colUnidadTamanio" /></th>	
							<th style="width: 0%;"><!-- id --></th>
							
							<th style="width: 20%;"><s:text name="colAcciones" /></th>
							
						</tr>
					</thead>

				</table>
				<div align="center">
					<sj:a onclick="solicitarRegistroAtributo();" button="true">Registrar</sj:a>
				</div>
			</div>
		</div>

		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar"/>

			<s:url var="urlGestionarEntidades"
				value="%{#pageContext.request.contextPath}/entidades">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarEntidades}'"
				value="Cancelar" />
		</div>
		
		<s:hidden id="jsonAtributosTabla" name="jsonAtributosTabla"
			value="%{jsonAtributosTabla}" />
		<s:hidden name="comentario" id="comentario" value="%{comentario}" />
	</s:form>


	<!-- EMERGENTE REGISTRAR ATRIBUTO -->
	<sj:dialog id="atributoDialog" title="Registrar Atributo" autoOpen="false"
		minHeight="300" minWidth="800" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmAtributo" name="frmAtributoName" theme="simple">
			<div class="formulario">
			<s:hidden id="filaAtributo" />
			<s:hidden id="idAtributo" />
				<div class="tituloFormulario">Información del Atributo</div>
				<table class="seccion">
					<tr>
						<td class="label obligatorio"><s:text name="labelNombre" /></td>
						<td><s:textfield name="atributo.nombre" id="atributo.nombre" cssClass="inputFormulario ui-widget"
								maxlength="200" cssErrorClass="input-error"></s:textfield></td>
					</tr>
					<tr>
						<td class="label obligatorio"><s:text name="labelDescripcion" /></td>
						<td><s:textarea rows="5" name="atributo.descripcion" id="atributo.descripcion" cssClass="inputFormularioExtraGrande ui-widget"
								maxlength="999" cssErrorClass="input-error"></s:textarea></td>
					</tr>
					<tr>
						<td class="label obligatorio"><s:text name="labelTipoDato"/></td>
						<td><s:select list="listTipoDato" cssClass="inputFormulario" name="atributo.tipoDato" id="atributo.tipoDato" listKey="nombre"
       						cssErrorClass="input-error" headerValue="Seleccione" headerKey="0" listValue="nombre" onchange="disablefromTipoDato();"></s:select></td>
					</tr>
					<tr id = 'trOtro' style="display: none;">
						<td class="label obligatorio"><s:text name="labelOtro" /></td>
						<td><s:textfield name="atributo.otroTipoDato" id="atributo.otroTipoDato" cssClass="inputFormulario ui-widget"
								cssErrorClass="input-error"></s:textfield></td>
					</tr>
					
					<tr id = 'trLongitud' style="display: none;">
						<td class="label obligatorio"><s:text name="labelLongitud" /></td>
						<td><s:textfield name="atributo.longitud" id="atributo.longitud" cssClass="inputFormulario ui-widget"
								cssErrorClass="input-error"></s:textfield></td>
					</tr>
					
					<tr id = 'trTextoAyudaFormato' style="display: none;">
						<td></td>
						<td class="textoAyuda">Indique las extensiones permitidas separadas por una coma p.e.: PDF, docx, doc.</td>
					</tr>
					
					<tr id = 'trFormatoArchivo' style="display: none;">
						<td class="label obligatorio"><s:text name="labelFormatoArchivo" /></td>
						<td><s:textfield name="atributo.formatoArchivo" id="atributo.formatoArchivo" cssClass="inputFormulario ui-widget"
								cssErrorClass="input-error"></s:textfield></td>
					</tr> 
					
					<tr id = 'trTamanioArchivo' style="display: none;">
						<td class="label obligatorio"><s:text name="labelTamanioArchivo" /></td>
						<td><s:textfield name="atributo.tamanioArchivo" id="atributo.tamanioArchivo"
								cssErrorClass="input-error" ></s:textfield>${blanks}
						<s:select list="listUnidadTamanio"  name="atributo.unidadTamanio" id="atributo.unidadTamanio" listKey="abreviatura"
       						cssErrorClass="input-error" headerValue="Seleccione" headerKey="0" listValue="abreviatura"></s:select></td>
					</tr>
					
					<tr>
						<td class = "label"><s:text name="labelObligatorio" /></td>
						<td><s:checkbox 
								name="atributo.obligatorio" id="atributo.obligatorio" cssErrorClass="input-error"></s:checkbox></td>
					</tr>

				</table>
			</div>
			<br />
			<div align="center">
				<input type="button" onclick="verificarRegistroModificacion()" value="Aceptar" /> <input
					type="button" onclick="cancelarRegistrarAtributo()" value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>
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
