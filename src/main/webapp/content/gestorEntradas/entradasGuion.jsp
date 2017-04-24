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
.nogenerable_prueba,.correcto_prueba,.incorrecto_prueba{margin-left: 92px;margin-top: -16px;}
.nogenerable_guion,.correcto_guion,.incorrecto_guion{margin-left: 10px;margin-top: -16px;}
.correctoAleatorio_prueba{margin-top:-23px;}
.correctoAleatorio_guion{margin-left: 10px;margin-top:-23px;}
.incorrectoAleatorio_prueba,.incorrectoAleatorio_guion{margin-top: -23px;}
.incorrectoAleatorio_guion{margin-left: 10px;}
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

	<div class="form-group col-md-12">
		<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configurar-entradas!anadirValoresEntradas" enctype="multipart/form-data" onsubmit="prepararEnvio()" method="post">
		<s:hidden name="_method" value="put" />
			<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
				<thead>
					<tr>
						<th><!-- Número del Caso de uso --></th>
						<th style="width: 15%;"><s:text name="colEntradas"/></th>
						<th style="width: 15%;"><s:text name="colValorAleatorioCorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioIncorrecto"/></th>
						<th style="width: 5%;"><s:text name="colValorCorrectoInsertar"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="listEntradas" var="entrada">
					<tr class="${'filaEntrada'}${entrada.id}">
						<td></td>
						<td><s:property value="%{#entrada.id + ' ' + #entrada.nombre}"/></td>
						<s:set var = "breakLoop" value = "false" />
						<s:iterator value="cadenasValidas" var="cadena">
							<s:if test="%{#cadena.entrada.atributo.id == #entrada.id}">
								<td><p align="left"><input type="hidden" value="${cadena.valor}" id="palabrasAleatorias-${cadena.entrada.atributo.id}"/><s:property value="%{#cadena.valor}"/></p><p align="right"><input id="aleatorioCorrectoPrueba-${cadena.entrada.atributo.id}" type="radio" name="prueba-${cadena.entrada.atributo.id}"  class="correctoAleatorio_prueba"/><input id="aleatorioCorrectoGuion-${cadena.entrada.atributo.id}" type="radio" name="guion-${cadena.entrada.atributo.id}" class="correctoAleatorio_guion"/></p></td>
								<s:set var = "breakLoop" value = "true"/>							
							</s:if>					
						</s:iterator>
						<s:if test="%{#breakLoop == false}">
								<td>No se puede generar.</td>
						</s:if>	
						<s:set var = "breakLoop2" value = "false" />
						<td>
						
						<select id="selectIncorrectos-${entrada.id}">
							<s:iterator value="listAleatoriasIncorrectas" var="incorrecto">
								<s:if test="%{#incorrecto.entrada.atributo.id == #entrada.id}">
									<option><p align="left"><s:property value="%{#incorrecto.valor}"/></p></option>
									<s:set var = "breakLoop2" value = "true"/>	
								</s:if>
							</s:iterator>
						</select>
						<!--<p align="right"><input id="incorrectoP-${entrada.id}" type="radio" name="prueba-${entrada.id}"  class="incorrectoAleatorio_prueba"/><input id="incorrectoG-${entrada.id}" type="radio" name="guion-${entrada.id}" class="incorrectoAleatorio_guion"/></p>-->
						<s:if test="%{#breakLoop2 == false}">
								<p>No se puede generar.</p>
								<script>
								document.getElementById('selectIncorrectos-<s:property value="%{#entrada.id}"/>').style.display = 'none';
								document.getElementById('incorrectoG-<s:property value="%{#entrada.id}"/>').style.display = 'none';
								document.getElementById('incorrectoP-<s:property value="%{#entrada.id}"/>').style.display = 'none';
								</script>
						</s:if>
						</td>
						<td>
							<input type="hidden" id="palabras-${entrada.id}"/>
						    <div class="upload" id="upload-${entrada.id}"><input type="file" name="vci"  id="files-${entrada.id}" label="File" value=""/>
							<s:hidden name="idAtributo" value="%{#entrada.id}" /></div>	
							<input id="checkbox-${entrada.id}" type="radio" name="prueba-${entrada.id}"  class="correcto_prueba"/><input id="checkbox2-${entrada.id}" type="radio"  class="correcto_guion" name="guion-${entrada.id}"/></td>
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

