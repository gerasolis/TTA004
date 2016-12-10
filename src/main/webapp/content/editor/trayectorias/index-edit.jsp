<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Trayectoria</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.atwho.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/trayectorias/js/token.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/trayectorias/js/index-edit.js"></script>
		
]]>

</head>
<body>
	<h1>Modificar Trayectoria</h1>

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
	<s:form autocomplete="off" id="frmTrayectoria" theme="simple"
		action="%{#pageContext.request.contextPath}/trayectorias/%{idSel}"
		method="post" onsubmit="return prepararEnvio();">
		<s:hidden name="_method" value="put" />

		<div class="formulario">
			<div class="tituloFormulario">Información general de la
				Trayectoria</div>
			<table class="seccion">
				<tr>
					<td class="label obligatorio"><s:text name="labelClave" /></td>
					<td><s:textfield name="model.clave" maxlength="5"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" />
						<s:fielderror fieldName="model.clave" cssClass="error"
							theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelTipo" /></td>
					<td><s:select list="listAlternativa" headerKey="-1"
							headerValue="Seleccione" id="idAlternativaPrincipal"
							name="alternativaPrincipal" cssErrorClass="select-error"
							onchange="cambiarElementosAlternativaPrincipal();"
							cssClass="inputFormulario ui-widget" /> <s:fielderror
							fieldName="alternativaPrincipal" cssClass="error" theme="jquery" />
						<p id="textoAyudaPA" class="textoAyuda" /></td>
				</tr>
				<tr id="filaCondicion" style="display: none;">
					<td class="label obligatorio"><s:text name="labelCondicion" /></td>
					<td><s:textarea rows="5" name="model.condicion"
							cssClass="inputFormularioExtraGrande ui-widget" id="model.idCondicion"
							maxlength="999" cssErrorClass="input-error"></s:textarea> <s:fielderror
							fieldName="model.condicion" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label"><s:text name="labelFinCasoUso" /></td>
					<td><s:checkbox name="model.finCasoUso" id="model.finCasoUso"
							cssErrorClass="input-error"></s:checkbox> <s:fielderror
							fieldName="model.finCasoUso" cssClass="error" theme="jquery" /></td>
				</tr>
			</table>
		</div>
		<div class="formulario">
			<div class="tituloFormulario">Pasos de la Trayectoria</div>
			<div class="seccion">
				<s:fielderror fieldName="model.pasos" cssClass="error"
					theme="jquery" />
				<table id="tablaPaso" class="tablaGestion" cellspacing="0"
					width="100%">
					<s:hidden name="numPasos" value="%{listPasos.length}" id="numPasos" />
					<thead>
						<tr>
							<th style="width: 5%;"><s:text name="colNumero" /></th>
							<th style="width: 55%;"><s:text name="colRedaccion" /></th>
							<th style="width: 0;"><!-- Realiza actor --></th>
							<th style="width: 0;"><!-- Verbo --></th>
							<th style="width: 0;"><!-- OtroVerbo --></th>
							<th style="width: 0;"><!-- Redacción --></th>
							<th style="width: 0;"><!-- Id --></th>
							<th style="width: 10%;"><s:text name="colAcciones" /></th>
						</tr>
					</thead>

				</table>
				<div align="center">
					<sj:a onclick="solicitarRegistroPaso();" button="true">Registrar</sj:a>
				</div>
			</div>
		</div>

		<br />
		<div align="center">
			<s:submit class="boton" value="Aceptar" />

			<s:url var="urlGestionarTrayectorias"
				value="%{#pageContext.request.contextPath}/trayectorias">
				<s:param name="idCU" value="%{idCU}" />
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarTrayectorias}'"
				value="Cancelar" />
		</div>
		<s:hidden id="jsonPasosTabla" name="jsonPasosTabla"
			value="%{jsonPasosTabla}" />
		<s:hidden name="comentario" id="comentario" value="%{comentario}" />
		
	</s:form>


	<!-- EMERGENTE REGISTRAR PASO -->
	<sj:dialog id="pasoDialog" title="Registrar Paso" autoOpen="false"
		minHeight="300" minWidth="800" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmPaso" name="frmPasoName"
			theme="simple">
			<s:hidden id="filaPaso" />
			<div class="formulario">
				<div class="tituloFormulario">Información del Paso</div>
				<table class="seccion">
					<tr>
						<td class="label obligatorio"><s:text name="labelRealiza" /></td>
						<td><s:select list="listRealiza" cssClass="inputFormulario"
								name="paso.realizaActor" id="realiza"
								cssErrorClass="input-error" headerKey="-1"
								headerValue="Seleccione"></s:select></td>
					</tr>
					<tr>
						<td class="label obligatorio"><s:text name="labelVerbo" /></td>
						<td><s:select list="listVerbos" cssClass="inputFormulario"
								name="paso.verbo" id="paso.verbo" cssErrorClass="input-error"
								headerKey="-1" headerValue="Seleccione"
								onchange="verificarOtro();"></s:select></td>
					</tr>
					<tr style="display: none;" id="otroVerbo">
						<td class="label obligatorio"><s:text name="labelOtro" /></td>
						<td><s:textfield name="paso.otroVerbo" id="paso.otroVerbo"
								maxlength="10" cssErrorClass="input-error"
								cssClass="inputFormulario ui-widget" /></td>
					</tr>
					<tr>
						<td class="label obligatorio"><s:text name="labelRedaccion" /></td>
						<td><s:textarea rows="5" name="paso.redaccion" id="inputor" cssClass="inputFormularioExtraGrande ui-widget"
								maxlength="999" cssErrorClass="input-error"></s:textarea></td>
					</tr>
				</table>
			</div>
			<br />
			<div align="center">
				<input type="button" onclick="verificarRegistroModificacion()" value="Aceptar" /> <input
					type="button" onclick="cancelarRegistrarPaso()" value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>

	<!-- COMENTARIOS DE LA ACTUALIZACIÓN -->
	<sj:dialog id="mensajeConfirmacion" title="Confirmación"
		autoOpen="false" minHeight="300" minWidth="800" modal="true"
		draggable="true">
		<s:form autocomplete="off" id="frmComentario"
			name="frmPostcondicionName" theme="simple">
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
			<br />
			</div>
			<div align="center">
				<input type="button" onclick="enviarComentarios()" value="Aceptar" />
				<input type="button" onclick="cancelarRegistroComentarios()"
					value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>
	
	<!-- EMERGENTE ERROR REFERENCIAS -->
	<sj:dialog id="mensajeReferenciasDialog" title="Confirmación"
		autoOpen="false" minHeight="200" minWidth="700" modal="true"
		draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion"
			name="frmConfirmarEliminacionName" theme="simple">
			<div class="seccion">
				<s:text name="MSG14" />
				<div id="elementosReferencias"></div>
			</div>
			<br />
			<div align="center">
				<input type="button" onclick="cerrarMensajeReferencias()"
					value="Aceptar" />
			</div>
		</s:form>
	</sj:dialog>

	<!-- Json de elementos -->
	<s:hidden name="jsonReglasNegocio" id="jsonReglasNegocio"
		value="%{jsonReglasNegocio}" />
	<s:hidden name="jsonEntidades" id="jsonEntidades"
		value="%{jsonEntidades}" />
	<s:hidden name="jsonCasosUsoProyecto" id="jsonCasosUsoProyecto"
		value="%{jsonCasosUsoProyecto}" />
	<s:hidden name="jsonPantallas" id="jsonPantallas"
		value="%{jsonPantallas}" />
	<s:hidden name="jsonMensajes" id="jsonMensajes" value="%{jsonMensajes}" />
	<s:hidden name="jsonActores" id="jsonActores" value="%{jsonActores}" />
	<s:hidden name="jsonTerminosGls" id="jsonTerminosGls"
		value="%{jsonTerminosGls}" />
	<s:hidden name="jsonAtributos" id="jsonAtributos"
		value="%{jsonAtributos}" />
	<s:hidden name="jsonPasos" id="jsonPasos" value="%{jsonPasos}" />
	<s:hidden name="jsonTrayectorias" id="jsonTrayectorias"
		value="%{jsonTrayectorias}" />
	<s:hidden name="jsonAcciones" id="jsonAcciones" value="%{jsonAcciones}" />
	<!-- Booleano que indica si existen trayectorias -->
	<s:hidden id="existeTPrincipal" value="%{existeTPrincipal}" />

</body>
	</html>
</jsp:root>