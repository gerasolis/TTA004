<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Módulos</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/modulos/js/index.js"></script>
]]>
</head>

<body>
	<div class="modal" id="modal"><!-- Place at bottom of page --></div>
	<h1>Gestionar Módulos</h1>
	<s:actionmessage theme="jquery"/>
	<s:actionerror theme="jquery"/>
	
	<br/>
	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
	<div class="form">
		<table id="gestion" class="tablaGestion" cellspacing="0" width="100%">
			<thead>
				<th style="width: 80%;"><s:text name="colModulo"/></th>
				<th style="width: 20%;"><s:text name="colAcciones"/></th>
			</thead>
			<tbody>
			<s:iterator value="listModulos" var="modulo">
				<tr>
					<td><s:property value="%{#modulo.clave + ' ' + #modulo.nombre}"/></td>		
					<td align="center">
						${blanks}
						<s:url var="urlCU" value="%{#pageContext.request.contextPath}/modulos!entrarCU?idSel=%{#modulo.id}"/>
						<s:a href="%{urlCU}">
						<img id="" class="button" title="Gestionar Casos de uso"
								src="${pageContext.request.contextPath}/resources/images/icons/uc.png" /></s:a>
						${blanks}
						<s:url var="urlIU" value="%{#pageContext.request.contextPath}/modulos!entrarIU?idSel=%{#modulo.id}"/>
						<s:a href="%{urlIU}">
						<img id="" class="button" title="Gestionar Pantallas"
								src="${pageContext.request.contextPath}/resources/images/icons/iu.png" /></s:a>
						${blanks}
						<s:url var="urlEditar" value="%{#pageContext.request.contextPath}/modulos/%{#modulo.id}/edit"/>			
						<s:a href="%{urlEditar}">
							<img id="" class="button" title="Modificar Módulo"
									src="${pageContext.request.contextPath}/resources/images/icons/editar.png" />
						</s:a>
						<s:a onclick="return verificarEliminacionElemento(%{#modulo.id});">
						<img id="" class="button" title="Eliminar Módulo"
								src="${pageContext.request.contextPath}/resources/images/icons/eliminar.png" /></s:a>
						${blanks}
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
			onclick="location.href='${pageContext.request.contextPath}/modulos/new'">
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
	<!-- EMERGENTE ERROR REFERENCIAS -->
	<sj:dialog id="mensajeReferenciasDialog" title="Confirmación" autoOpen="false"
		minHeight="150" minWidth="700" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion" name="frmConfirmarEliminacionName" theme="simple">
				<div class="seccion">
				<s:text name="MSG40"/>
				<div id="elementosReferencias"></div>
				</div>
			<br />
			<div align="center">
				<input type="button" onclick="cerrarMensajeReferencias()" value="Aceptar"/> 
			</div>
		</s:form>
	</sj:dialog>
	</div>	
</body>
</html>
</jsp:root>

