<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Reglas de negocio</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/reglas-negocio/js/index.js"></script>
]]>
</head>

<body> 
	<h1>Gestionar Reglas de negocio</h1>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	
	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
	<div class="form">
	 
		<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
			<thead>
				<tr>
					<th><!-- Número de la Regla de Negocio --></th>
					<th style="width: 80%;"><s:text name="colReglasNegocio"/></th>
					<th style="width: 20%;"><s:text name="colAcciones"/></th>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="listReglasNegocio" var="rn">
				<tr>
					<td><s:property value="%{#rn.numero}"/></td>
					<td><s:property value="%{#rn.clave + #rn.numero + ' ' + #rn.nombre}"/></td>
					<td align="center">				
						<s:url var="urlConsultar" value="%{#pageContext.request.contextPath}/reglas-negocio/%{#rn.id}"/>
							<s:a href="%{urlConsultar}">
								<img id="" class="button" title="Consultar Regla de negocio"
										src="${pageContext.request.contextPath}/resources/images/icons/ver.png" />
							</s:a>
						${blanks}
						<s:if test="%{#rn.estadoElemento.id == 1}">
						<s:url var="urlEditar" value="%{#pageContext.request.contextPath}/reglas-negocio/%{#rn.id}/edit"/>
						<s:a href="%{urlEditar}">
							<img id="" class="button" title="Modificar Regla de negocio"
									src="${pageContext.request.contextPath}/resources/images/icons/editar.png" />
						</s:a>
						${blanks}
						<s:url var="urlEliminar" value="%{#pageContext.request.contextPath}/reglas-negocio/%{#rn.id}?_method=delete" method="post"/>
						<s:a onclick="return verificarEliminacionElemento(%{#rn.id});"><!-- openDialog="confirmarEliminacionDialog" -->
							<img id="" class="button" title="Eliminar Regla de negocio"
									src="${pageContext.request.contextPath}/resources/images/icons/eliminar.png" />
						</s:a>
						${blanks}	
						</s:if>					
						
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
			onclick="location.href='${pageContext.request.contextPath}/reglas-negocio/new'">
			<s:text name="Registrar"></s:text>
		</button>
	</div>
	</s:form>	
	<div class = "invisible">
	<!-- EMERGENTE CONFIRMAR ELIMINACIÓN -->
	<sj:dialog id="confirmarEliminacionDialog" title="Confirmación"
		autoOpen="false" minHeight="100" minWidth="400" modal="true"
		draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion"
			name="frmConfirmarEliminacionName" theme="simple">
			<div class="seccion">
				<s:text name="MSG11"></s:text>
			</div>
			<br />
			<div align="center">
				<input id="btnConfirmarEliminacion" type="button" onclick=""
					value="Aceptar" /> <input type="button"
					onclick="cancelarConfirmarEliminacion();" value="Cancelar" />
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
	</div>
</body>
</html>
</jsp:root>

