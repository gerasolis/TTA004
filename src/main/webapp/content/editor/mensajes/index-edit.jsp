<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Mensajes</title>

<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.caret.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/jquery.atwho.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/mensajes/js/token.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/mensajes/js/index-edit.js"></script>	
]]>

</head>
<body>
	<h1>Modificar Mensaje</h1>
	
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br/>

	<p class="instrucciones">Ingrese la información solicitada.</p>


	<s:form autocomplete="off" id="frmMsj" theme="simple"
		action="%{#pageContext.request.contextPath}/mensajes/%{idSel}"
		onsubmit="return prepararEnvio();" method="post">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Información general del Mensaje</div>
			<table class="seccion">
				<tr>
					<td class="label"><s:text name="labelClave" /></td>
					<td class="labelDerecho"><s:property value="model.clave"/></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelNumero" /></td>
					<td><s:textfield name="model.numero" maxlength="20"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" /> <s:fielderror
							fieldName="model.numero" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
					<td class="label obligatorio"><s:text name="labelNombre" /></td>
					<td><s:textfield name="model.nombre" maxlength="200"
							cssErrorClass="input-error" cssClass="inputFormulario ui-widget" /> <s:fielderror
							fieldName="model.nombre" cssClass="error" theme="jquery" /></td>
				</tr>
				<tr>
						<td class="label obligatorio"><s:text name="labelDescripcion" /></td>
						<td><s:textarea rows="5" name="model.descripcion" cssClass="inputFormularioExtraGrande ui-widget"
								maxlength="999" cssErrorClass="input-error"></s:textarea> 
								<s:fielderror
								fieldName="model.descripcion" cssClass="error"
								theme="jquery" /></td>
				</tr>
				<tr>
					<td> </td>
					<td><span class="textoAyuda">Para utilizar un parámetro escriba el token PARAM· más el nombre del parámetro.</span></td>
				</tr>
				<tr>
						<td class="label obligatorio"><s:text name="labelRedaccion" /></td>
						<td><s:textarea rows="5" name="model.redaccion" cssClass="inputFormularioExtraGrande ui-widget" id="inputor" readonly="false"
								maxlength="999" cssErrorClass="input-error" oninput="verificarEsParametrizado();" onchange="verificarEsParametrizado();"/> 
								<s:fielderror
								fieldName="model.redaccion" cssClass="error"
								theme="jquery" /></td>
						<td class="botonEditar" id="botonEditar">
							<sj:a onclick="habilitarEdicionRedaccion();">
							<img class="button"
							title="Redefinir Mensaje"
							src="${pageContext.request.contextPath}/resources/images/icons/editar.png" onclick="habilitarEdicionRedaccion();"/>
							</sj:a></td>
				</tr>
				
			</table>
		</div>
		
		<s:fielderror fieldName="model.parametros" cssClass="error errorFormulario" theme="jquery" />
		<div  class="formulario" style="display: none;" id = "seccionParametros">
			<div class="tituloFormulario">Parámetros del Mensaje</div>
			<div class="seccion">
			<div class="instrucciones">Ingrese la descripción de los parámetros.</div>
			<br/>
			<table id="parametros">
				<thead>
					<th></th>
					<th></th>
				</thead>
			</table>
			</div>
		</div>
		<br />
		<div align="center">
			
			<s:submit class="boton" value="Aceptar"/>
			<input class="boton" type="button"
				onclick="location.href='${pageContext.request.contextPath}/mensajes'"
				value="Cancelar" />
		</div>
		<s:hidden value="%{cambioRedaccion}" name="cambioRedaccion" id="cambioRedaccion"/>
		<s:hidden value="%{parametrizado}" name="parametrizado" id="parametrizado"/>
		<s:hidden value="%{jsonParametros}" name="jsonParametros" id="jsonParametros"/>
		<s:hidden name="comentario" id="comentario" value="%{comentario}" />
		
		<!-- Json de parametros guardados-->
		<s:hidden name="jsonParametrosGuardados" id="jsonParametrosGuardados" value="%{jsonParametrosGuardados}"/>
	</s:form>
	
	<!-- MENSAJE DE ALERTA -->	
   	<sj:dialog id="mensajeConfirmacion" title="Confirmación" autoOpen="false" 
   	minHeight="200" minWidth="400" modal="true" draggable="true" >
	   	
			<div class="seccion">
				<p class="instrucciones">Escriba la redacción de cada uno de los parámetros utilizados en el mensaje.</p>
			</div>
		
			<br />
			<div align="center">
				<input type="button"
					onclick="cerrarEmergente()"
					value="Aceptar" />
			</div>
	</sj:dialog>
	
	<!-- COMENTARIOS DE LA ACTUALIZACIÓN -->
	<sj:dialog id="mensajeComentarios" title="Confirmación"
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