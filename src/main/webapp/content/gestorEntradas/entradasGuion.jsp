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
.nogenerable_prueba,.correcto_prueba,.incorrecto_prueba{/*margin-left: 92px;margin-top: -16px;*/}
.nogenerable_guion,.correcto_guion,.incorrecto_guion{/*margin-left: 10px;*//*margin-top: -16px;*/}
.correctoAleatorio_prueba{/*margin-top:-23px;*/}
.correctoAleatorio_guion{margin-left: 10px;margin-top:-23px;}
.incorrectoAleatorio_prueba,.incorrectoAleatorio_guion{margin-top: -23px;}
.incorrectoAleatorio_guion{margin-left: 10px;}
.f2,.p2,.g2{border: 0px;}
</style>

<jsp:text>
<![CDATA[                 
    <script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/cu/js/index.js"></script>	
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>	
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/gestorEntradas/js/entradas2.js"></script>
	<!--<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/gestorEntradas/js/jquery.validate.min.js"></script>-->
	<!--<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/gestorEntradas/js/form-validation-md.min_es.js"></script>-->
    
]]>
</jsp:text>

</head>
<body>
<div id="errorEntradas1"></div>
	<div class="modal" id="modal"><!-- Place at bottom of page --></div>
	<h1>Gestor de Entradas</h1>
	<div id="errorEntradas1"></div>
	<p class="instrucciones">Ingrese los valores que van a utilizar las entradas del caso de uso en la prueba automática y en el guión de pruebas, así como el formato html con el que se identifican.</p>
	<p class="instrucciones">Antes de iniciar, tome en cuenta la siguiente nomenclatura:</p>
	<ul>
		<li><p class="instrucciones"><b>P:</b> Valores que se van a utilizar en la prueba.</p></li> 
		<li><p class="instrucciones"><b>G:</b> Valores que se van a utilizar en el guión de pruebas.</p></li> 
	</ul>
	
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<div class="form-group col-md-12">
		
		<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configurar-entradas!anadirValoresEntradas" enctype="multipart/form-data" method="post">
		<s:hidden name="_method" value="put" />
			<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
				<thead>
					<tr>
						<th><!-- Número del Caso de uso --></th>
						<th style="width: 15%;"><s:text name="colEntradas"/></th>
						<th style="width: 15%;"><s:text name="colNombreHTML"/></th>
						<th style="width: 15%;"><s:text name="colValorAleatorioCorrecto"/></th>
						<th style="width: 20%;"><s:text name="colValorAleatorioIncorrecto"/></th>
						<th style="width: 5%;"><s:text name="colValorCorrectoInsertar"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="listEntradas" var="entrada">
				<div id="errorEntrada-${entrada.id}"></div>
					<tr class="${'filaEntrada'}${entrada.id}">
						<td></td>
						<td><s:property value="%{#entrada.id + ' ' + #entrada.nombre}"/></td>
						<!-- <td><p>Nada</p></td>-->
						<s:iterator value="listEntradas_2" var="entrada_2">
							<!--<s:property value="%{#entrada_2.atributo.id}"/>-->
							<s:if test="%{#entrada_2.atributo.id == #entrada.id}">
								<td style=""><p align="center"><input style="width: 132px;" type="text" name="nombreHTML-${entrada.id}" id="nombreHTML-${entrada.id}" value="${entrada_2.nombreHTML}"/></p><div id="errorHTML-${entrada.id}"></div></td>
							</s:if>
						</s:iterator>
						
						
						<s:set var = "breakLoop" value = "false" />
						<s:set var = "bandera" value = "0" />
						<s:iterator value="cadenasValidas" var="cadena">
							<s:if test="%{#cadena.entrada.atributo.id == #entrada.id}">
								<s:if test="%{#bandera == 0}">
									<td>
										<table border="0" class="validas">
											<tr>
												<td style="width: 64%;border: 0px;"><p align="left"><input type="hidden" value="${cadena.valor}" id="palabrasAleatorias-${cadena.entrada.atributo.id}"/><s:property value="%{#cadena.valor}"/></p></td>
												<td style="border: 0px;"><p align="right">P <input id="aleatorioCorrectoPrueba-${cadena.entrada.atributo.id}" type="radio" name="prueba-${cadena.entrada.atributo.id}"  class="correctoAleatorio_prueba"/></p></td>							
												<td style="border: 0px;"><p align="right">G <input id="aleatorioCorrectoGuion-${cadena.entrada.atributo.id}" type="radio" name="guion-${cadena.entrada.atributo.id}"  class="correctoAleatorio_prueba"/></p></td>
											</tr>
										</table>
									</td>
									<s:set var = "breakLoop" value = "true"/>
									<s:set var = "bandera" value = "1"/>	
								</s:if>						
							</s:if>					
						</s:iterator>
						<s:if test="%{#breakLoop == false}">
								<td>No se puede generar.</td>
						</s:if>	
						<s:set var = "breakLoop2" value = "false" />
						<td>
						
						<select id="selectIncorrectos-${entrada.id}" size="1">
							<s:iterator value="listAleatoriasIncorrectas" var="incorrecto">
								<s:if test="%{#incorrecto.entrada.atributo.id == #entrada.id}">
									<s:if test="%{#incorrecto.valor.length()>25}">
										<option><p align="left"><s:property value="%{#incorrecto.valor.substring(0, 40)}"/></p></option>
										
									</s:if>
									<s:else>
										<option><p align="left"><s:property value="%{#incorrecto.valor}"/></p></option>
									</s:else>
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
							<table>
										<tr>
											<td style="width: 38%;border: 0px;"><input type="hidden" id="palabras-${entrada.id}"/>
						    <div class="upload" id="upload-${entrada.id}"><input type="file" name="vci"  id="files-${entrada.id}" label="File" value=""/>
							<s:hidden name="idAtributo" value="%{#entrada.id}" /></div>	</td>
											<td style="border: 0px;"><p align="right">P <input id="checkbox-${entrada.id}" type="radio" name="prueba-${entrada.id}"  class="correcto_prueba"/></p></td>							
											<td style="border: 0px;"><p align="right">G <input id="checkbox2-${entrada.id}" type="radio"  class="correcto_guion" name="guion-${entrada.id}"/></p></td>
										</tr>
							</table>
						</td>
					</tr>
				</s:iterator>
				</tbody>
			</table>
			<br />
			<br />
			<div align="center">
				<div align="center">
					
					<!--<s:submit class="boton" value="Aceptar"/>-->
					
					<input class="boton" type="submit" value="Aceptar" />
					
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
			<s:hidden id="jsonEntradasTabla2_1" name="jsonEntradasTabla2_1" value="%{jsonEntradasTabla2_1}" />
		</s:form>
	</div>
	
	<s:hidden name="pruebaGenerada" id="pruebaGenerada" value="%{pruebaGenerada}"/>
	<s:hidden name="pruebaGenerada2" id="pruebaGenerada2" value="%{pruebaGenerada2}"/>
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

