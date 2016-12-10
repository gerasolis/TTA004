<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Configuración Caso de uso</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casoUsoPrevio.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>	
]]>

</head>
<body>

	<h1>Configuración del Caso de uso  </h1>
	<h3><s:property value="previo.clave + previo.numero + ' ' + previo.nombre"/></h3>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<s:fielderror fieldName ="campos" cssClass="error" theme="jquery" />
	<br />

	<s:form autocomplete="off" id="frmCasoUso" theme="simple"
		action="%{#pageContext.request.contextPath}/configuracion-casos-uso-previos!configurarCasoUso" method="post">
		
		
		<div class="formulario" id="formularioEntradas">
			<div class="tituloFormulario">Configuración de las Entradas</div>
			<p class="instrucciones">Ingrese los valores de los atributos <i>name</i> y <i>value</i> de los input HTML de cada una de las Entradas.</p>
			<div class="seccion" id="seccionEntradas">
				<table id="tablaEntradas">
				<!--  -->
				</table>
			</div>
		</div>
		<div class="formulario" id="formularioAcciones">
			<div class="tituloFormulario">Configuración de las URL</div>
			<p class="instrucciones">Ingrese la URL destino de cada una de las Acciones.</p>
			<div class="seccion" id="seccionURL">
				<!--  -->
			</div>
		</div>
		
		<br />
		<div align="center">
			<input class="boton" type="button"
				onclick="guardarSalir();"
				value="Guardar" />
				
			<input class="boton" type="button"
				onclick="aceptar();"
				value="Finalizar" />

			<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/configuracion-casos-uso-previos!prepararConfiguracion">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarCU}'"
				value="Cancelar" />
		</div>
		
		<s:hidden name="jsonAcciones" id="jsonAcciones" value="%{jsonAcciones}"/>
		<s:hidden name="jsonEntradas" id="jsonEntradas" value="%{jsonEntradas}"/>
		<s:hidden name="jsonImagenesPantallasAcciones" id="jsonImagenesPantallasAcciones" value="%{jsonImagenesPantallasAcciones}"/>
	</s:form>
	<sj:dialog id="pantallaDialog" title="Previsualizar Pantalla" autoOpen="false"
		minHeight="200" minWidth="700" modal="true" draggable="true">
			<div class="marcoImagen" id="marco-pantalla">
				<center>
					<img id="pantalla" src="#" class="imagen" />
				</center>
			</div>
			<div align="center">
				<input type="button" onclick="cerrarPantalla()" value="Aceptar"/> 
			</div>
	</sj:dialog>
</body>
	</html>
</jsp:root>