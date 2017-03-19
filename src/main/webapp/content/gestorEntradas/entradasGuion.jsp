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
    background: no-repeat url('/prisma/resources/images/icons/subir.png');
    overflow: hidden;
}


div.upload input {
    display: block !important;
    opacity: 0 !important;
    overflow: hidden !important;
    width: 40px !important;
}
.nogenerable_prueba,.correcto_prueba,.incorrecto_prueba{margin-left: 92px;margin-top: -15px;}
.nogenerable_guion,.correcto_guion,.incorrecto_guion{margin-left: 10px;margin-top: -11px;}
</style>

<jsp:text>
<![CDATA[                 
    <script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/cu/js/index.js"></script>	
				<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>	
		<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/gestorEntradas/js/entradas2.js"></script>
    
]]>
</jsp:text>

</head>
<body>
	<div class="modal" id="modal"><!-- Place at bottom of page --></div>
	<h1>Gestor de Entradas</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<div class="form">
		<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configurar-entradas!anadirValoresEntradas" enctype="multipart/form-data" onsubmit="prepararEnvio()" method="post">
		<s:hidden name="_method" value="put" />
			<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
				<thead>
					<tr>
						<th><!-- Número del Caso de uso --></th>
						<th><s:text name="colEntradas"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioCorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioIncorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorCorrectoInsertar"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="listEntradas" var="entrada">
					<tr class="${'filaEntrada'}${entrada.id}">
						<td></td>
						<td><s:property value="%{#entrada.id + ' ' + #entrada.nombre}"/></td>
						<s:hidden name="entradas" value="%{#entrada.nombre}" />
						<td><p align="center">VALOR<s:checkbox name="aleatorioCorrecto_prueba" fieldValue="%{#entrada.id}" value="%{#entrada.id}"  class="correcto_prueba"/><s:checkbox name="aleatorioCorrecto_guion" fieldValue="%{#entrada.id}" value="%{#entrada.id}"  class="correcto_guion"/></p></td>
						<td><p align="center">VALOR<s:checkbox name="aleatorioIncorrecto_prueba" fieldValue="%{#entrada.id}" value="%{#entrada.id}" class="correcto_prueba"/><s:checkbox name="aleatorioIncorrecto_guion" fieldValue="%{#entrada.id}" value="%{#entrada.id}"  class="correcto_guion"/></p></td>
						<td>
							<input type="hidden" id="palabras-${entrada.id}"/>
						    <div class="upload"><input type="file" name="vci"  id="files-${entrada.id}" label="File" value=""/>
							<s:hidden name="idAtributo" value="%{#entrada.id}" /></div>	
							<input id="checkbox-${entrada.id}" type="checkbox"  class="correcto_prueba"/><input id="checkbox2-${entrada.id}" type="checkbox"  class="correcto_guion"/></td>
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
			<s:hidden id="jsonEntradasTabla" name="jsonEntradasTabla" value="%{jsonEntradasTabla}" />
			<input type="text" id="cadena1"/>
		</s:form>
	</div>
	
	<s:hidden name="pruebaGenerada" id="pruebaGenerada" value="%{pruebaGenerada}"/>
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

