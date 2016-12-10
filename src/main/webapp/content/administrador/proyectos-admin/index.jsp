<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Proyectos</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/administrador/proyectos-admin/js/index.js"></script>
]]>
</head>

<body>
	<h1>Gestionar Proyectos</h1>
	<s:actionmessage theme="jquery"/>
	<s:actionerror theme="jquery"/>
	
	<br/>
	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
	<div class="form">
		<table id="gestion" class="tablaGestion" cellspacing="0" width="100%">
			<thead>
				<th style="width: 40%;"><s:text name="colProyecto"/></th>
				<th style="width: 40%;"><s:text name="colLiderProyecto"/></th>
				<th style="width: 20%;"><s:text name="colAcciones"/></th>
			</thead>
			<tbody>
			<s:iterator value="listProyectos" var="proyecto">
				<tr>
					<td><s:property value="%{#proyecto.clave + ' ' + #proyecto.nombre}"/></td>
					<td>
					<s:iterator value="%{#proyecto.proyecto_colaboradores}" var="proy_col">
						<s:if test="%{#proy_col.rol.nombre == 'Líder'}">
							<s:property value="%{#proy_col.colaborador.nombre + ' ' + #proy_col.colaborador.apellidoPaterno + ' ' + #proy_col.colaborador.apellidoMaterno}"/>
							<s:set var="breakLoop" value="%{true}" />
						</s:if>
					</s:iterator>		
					</td>
					<td align="center">
						<s:url var="urlEditar" value="%{#pageContext.request.contextPath}/proyectos-admin/%{#proyecto.id}/edit"/>			
						<s:a href="%{urlEditar}">
							<img id="" class="button" title="Modificar Proyecto"
									src="${pageContext.request.contextPath}/resources/images/icons/editar.png" />
						</s:a>
						${blanks}
						<s:a onclick="return mostrarMensajeEliminacion(%{#proyecto.id});">
						<img id="" class="button" title="Eliminar Proyecto"
								src="${pageContext.request.contextPath}/resources/images/icons/eliminar.png" /></s:a>		
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
			onclick="location.href='${pageContext.request.contextPath}/proyectos-admin/new'">
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

