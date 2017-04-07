<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Configuración Guion de Pruebas</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casosUsoPrevios.js"></script>	
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/desconocido.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/guionPruebas/js/verbos.js"></script>
]]>

</head>
<body>
	
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<h1>Gestión de instrucciones para verbos no conocidos</h1>
	
	<p class="instrucciones">Los siguientes verbos (registrados como "Otros") pueden ser sinónimos de algunos verbos del catálogo</p>
	<p class="instrucciones">Por favor, verifique si el verbo coincide con alguno de los ya registrados o bien, ingrese la instrucción correspondiente en un archivo .txt.</p>
	<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configuracion-verbo!anadirInstruccion" enctype="multipart/form-data" method="post">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Configuración de los Verbos</div>
			<div class="seccion">
				<s:iterator value="otros" var="t">
					<p><s:property value="%{sinonimo + ' es sinónimo de ' + similar}"/></p>
					<p class="instrucciones">Instrucción de ${similar} es: ${instruccion[0]}</p>
					<p class="instrucciones">
					<!-- <input type="file" id="pic" name="pic" accept="file_extension"/>-->
					 
					 <!--<s:url var="idAtributo" value="%{#t.id}"></s:url>-->
					 <!-- <input type="text" name="entrada" id="entrada" value="${idAtributo}"/>-->
					 <s:hidden name="idPaso" value="%{idPaso}" />
					 <s:hidden name="idSinonimo" value="%{idSinonimo}" />
					</p>
					<div class="pregunta">
						<div class="genericFont seccion">
								<br />
								<div class="obligatorio">
									<s:text name="labelCorrectoSinonimo" />
								</div>
								<br />
								<div class="radiobotones">
									<s:radio id="esCorrectoVerbo" name="esCorrectoVerbo"
										list="#{'1':'Sí','2':'No'}" cssErrorClass="input-error"
										cssClass="genericFont"
										onchange="verificarObservaciones('verbo')" />
									<div class="seccion">
										<s:fielderror fieldName="esCorrectoVerbo" cssClass="error"
											theme="jquery" />
									</div>
								</div>
						</div>
						<div id="divObservacionesVerbo" style="display: none">
							<table class="seccion">
								<tr>
									<td class="label obligatorio"><s:text
											name="labelInstruccion" /></td>
									<td><s:file name="upload" label="File"/></td>
								</tr>
							</table>
						</div>
					</div>
				</s:iterator>
			</div>
		</div>
		
		<br />
		<div align="center">
			
			<s:submit class="boton" value="Siguiente"/>
			
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
	</s:form>
	
</body>
	</html>
</jsp:root>