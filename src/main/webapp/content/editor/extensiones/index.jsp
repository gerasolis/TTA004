<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Puntos de extensión</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/extensiones/js/index.js"></script>
]]>
</head>

<body>
	<h1>Gestionar Puntos de extensión</h1>
	<s:actionmessage theme="jquery"/>
	<s:actionerror theme="jquery"/>
	
	<br/>
	<s:set var="obs" value="observaciones" />

	<s:if test="%{#obs != null}">
		<div class="formulario">
			<div class="tituloObservaciones">Observaciones</div>
			<div class="observaciones">
				<s:property value="#obs" />
			</div>
		</div>
	</s:if>
	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
	<div class="form">
		<table id="gestion" class="tablaGestion" cellspacing="0" width="100%">
			<thead>
				<th style="width: 35%;"><s:text name="colCausa"/></th>
				<th style="width: 30%;"><s:text name="colRegion"/></th>
				<th style="width: 25%;"><s:text name="colExtiende"/></th>
				<th style="width: 10%;"><s:text name="colAcciones"/></th>
				
			</thead>
			<tbody>
			<s:iterator value="listPtosExtension" var="ext">
				<tr>
					<td><s:property value="%{#ext.causa}"/></td>
					<td><s:property value="%{#ext.region}"/></td>
					<td><s:property value="%{#ext.casoUsoDestino.clave + #ext.casoUsoDestino.numero + ' ' + #ext.casoUsoDestino.nombre}"/></td>
					
					<td align="center">
						<s:url var="urlEditar" value="%{#pageContext.request.contextPath}/extensiones/%{#ext.id}/edit"/>			
						<s:a href="%{urlEditar}">
							<img id="" class="button" title="Modificar Punto de extensión"
									src="${pageContext.request.contextPath}/resources/images/icons/editar.png" />
						</s:a>
						<s:a onclick="return mostrarMensajeEliminacion(%{#ext.id});">
							<img id="" class="button" title="Eliminar Punto de extensión"
									src="${pageContext.request.contextPath}/resources/images/icons/eliminar.png" />
						</s:a>
					</td>	
				</tr>
			</s:iterator>
			</tbody>
		</table>
		
	</div>
	<br />
	<br />
	<div align="center">
		<button class="boton" 
			onclick="location.href='${pageContext.request.contextPath}/cu'">
			<s:text name="Regresar"></s:text>
		</button>
		<button class="boton" 
			onclick="location.href='${pageContext.request.contextPath}/extensiones/new'">
			<s:text name="Registrar"></s:text>
		</button>
	</div>
	</s:form>
	<div class = "invisible">
	<!-- EMERGENTE CONFIRMAR ELIMINACIÓN -->
	<sj:dialog id="confirmarEliminacionDialog" title="Confirmación" autoOpen="false"
		minHeight="100" minWidth="400" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion" name="frmConfirmarEliminacionName" theme="simple">
				<div class="seccion">
				<s:text name="MSG11"></s:text>
				</div>
			<br />
			<div align="center">
				<input id = "btnConfirmarEliminacion" type="button" onclick="" value="Aceptar"/> <input
					type="button" onclick="cancelarConfirmarEliminacion();" value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>
	</div>
</body>
</html>
</jsp:root>

