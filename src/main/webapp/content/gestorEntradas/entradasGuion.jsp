<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Casos de uso</title>
<style>
div.upload {
    background: no-repeat url('/TTA004/resources/images/icons/subir.png');
    overflow: hidden;
}


div.upload input {
    display: block !important;
    opacity: 0 !important;
    overflow: hidden !important;
    width: 40px !important;
}
</style>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/cu/js/index.js"></script>	
]]> 
</head>
<body>
	<div class="modal" id="modal"><!-- Place at bottom of page --></div>
	<h1>Gestionar de Entradas</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
	<div class="form">
		<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configurar-entradas!anadirValoresEntradas" enctype="multipart/form-data" method="post">
		<s:hidden name="_method" value="put" />
			<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
				<thead>
					<tr>
						<th><!-- Número del Caso de uso --></th>
						<th><s:text name="colEntradas"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioCorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioIncorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorNoGenerable"/></th>
						<th style="width: 20%;"><s:text name="colValorCorrectoInsertar"/></th>
						<th style="width: 20%;"><s:text name="colValorCorrectoModificar"/></th>
						<th style="width: 20%;"><s:text name="colValorIncorrecto"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="listEntradas" var="entrada">
					<tr class="${'filaEntrada'}${entrada.id}">
						<td></td>
						<td><s:property value="{#entrada.nombre}"/></td>
						<td></td>
						<td></td>
						<td></td>
						<td><div class="upload"><s:file name="upload" label="File"/><s:hidden name="idAtributo" value="%{#entrada.id}" /></div></td>
						<td><div class="upload"><s:file name="upload" label="File"/><s:hidden name="idAtributo" value="%{#entrada.id}" /></div></td>
						<td><div class="upload"><s:file name="upload" label="File"/><s:hidden name="idAtributo" value="%{#entrada.id}" /></div></td>
					</tr>
				</s:iterator>
				</tbody>
			</table>
		
			<br />
			<br />
			<div align="center">
				<div align="center">
					
					<s:submit class="boton" value="Aceptar"/>
					
					<!--  <input class="boton" type="button"
						onclick="siguiente();"
						value="Siguiente" />-->
						
					<s:url var="urlGestionarCU"
						value="%{#pageContext.request.contextPath}/cu">
					</s:url>
					<input class="boton" type="button"
						onclick="location.href='${urlGestionarCU}'"
						value="Cancelar" />
						
				</div>
			</div>
		</s:form>
	</div>
	
	<s:hidden name="pruebaGenerada" id="pruebaGenerada" value="%{pruebaGenerada}"/>
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
		minHeight="200" minWidth="700" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion" name="frmConfirmarEliminacionName" theme="simple">
				<div class="seccion">
				<s:text name="MSG14"/>
				<div class="" id="elementosReferencias"><!--  --></div>
				</div>
			<br />
			<div align="center">
				<input type="button" onclick="cerrarMensajeReferencias()" value="Aceptar"/> 
			</div>
		</s:form>
	</sj:dialog>	
	
	<!-- EMERGENTE TERMINAR -->
	<sj:dialog id="mensajeTerminarDialog" title="Confirmación" autoOpen="false"
		minHeight="100" minWidth="550" modal="true" draggable="true">
		<s:form autocomplete="off" id="frmConfirmarEliminacion" name="frmConfirmarEliminacionName" theme="simple">
				
				<div class="seccion">
					<div id = "mensajeRestricciones"><!--  --></div>
					<div id="restriccionesTermino"><!--  --></div>
				<br />
			</div>
			<div align="center">
				<input id="btnConfirmarTermino" type="button" value="Aceptar"/> 
				 <input type="button" onclick="cancelarConfirmarTermino();" value="Cancelar" />
			</div>
		</s:form>
	</sj:dialog>
</div>
</body>
</html>
</jsp:root>

