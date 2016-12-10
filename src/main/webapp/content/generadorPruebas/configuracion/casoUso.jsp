<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Configuración Caso de uso x</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casoUso.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/constructores.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/validaciones.js"></script>
		
]]>

</head>
<body>
	<div class="menuWizard">
	<img id="" src="${pageContext.request.contextPath}/resources/images/wizard/w3.png" />
	</div>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<s:fielderror fieldName ="campos" cssClass="error" theme="jquery" />
	<br />
	
	<p class="instrucciones">Ingrese la información solicitada.</p>
	<s:form autocomplete="off" id="frmCasoUso" theme="simple"
		action="%{#pageContext.request.contextPath}/configuracion-caso-uso!configurar" method="post" onsubmit="return prepararEnvio();">
		<s:hidden name="_method" value="put" />
		
		
		<div class="formulario" id="formularioEntradas">
			<div class="tituloFormulario">Configuración de las Entradas</div>
			<div class="seccion">
				<p class="instrucciones">Ingrese los valores de los atributos <i>name</i> y <i>value</i> de los input HTML de cada una de las Entradas. Los valores mostrados en
					<i>value</i> son propuestas generadas a partir de la especificación de los atributos.</p>
			</div>
			<div id="seccionEntradas">
				<table class="seccion" id="tablaEntradas">
				<!--  -->
				</table>
			</div>
		</div>
		<div class="formulario" id="formularioAcciones">
			<div class="tituloFormulario">Configuración de las Acciones</div>
			<div class="seccion">
				<p class="instrucciones">Ingrese la URL destino de cada una de las Acciones.</p>
			</div>
			<div class="seccion" id="seccionURL">
				<!--  -->
			</div>
		</div>
		<div class="formulario" id="formularioReglasNegocio">
			<div class="tituloFormulario">Configuración de las Reglas de negocio</div>
			<div class="seccion">
				<p class="instrucciones">Es esta sección deberá ingresar las queries necesarias para realizar consultas a la base de datos. Las Reglas de negocio
				de unicidad requieren una query que consulte las Entidades cuyo Atributo identificador sea igual al proporcionado en las Entradas. </p>
				<p class="instrucciones"><span class="textoAyuda">P.e. Select * from Persona where CURP = "HESN900909MDFRNT00"; (donde la CURP es el atributo identificador de la entidad Persona).</span></p>
				<p class="instrucciones">En el caso
				de las Reglas de negocio de verificación de catálogos, deberá ingresar una query que consulte todos los registros del catálogo.</p>
				<p class="instrucciones"><span class="textoAyuda">P.e. Select * from Genero; (donde Genero es un catálogo).</span></p>
			</div>
			<div id="seccionReglasNegocio">
				<div class="seccion" id="seccionRN">
					<!--  -->
				</div>
				<table class="seccion" id="tablaReglasNegocio">
				<!--  -->
				</table>
			</div>
		</div>
		<div class="formulario" id="formularioParametros">
			<div class="tituloFormulario">Configuración de los Parámetros</div>
			<div class="seccion">
				<p class="instrucciones">Ingrese el valor que tomará cada uno de los Parámetros de los Mensajes, en caso de que no sea posible determinarlos 
				puede dejar los campos vacíos y el sistema guardará el nombre del Parámetro como su valor para que puedan ser fácilmente identificados dentro del plan de pruebas.</p>
			</div>
			<div class="seccion" id="seccionParametros">
				<!--  -->
			</div>
		</div>
		<div class="formulario" id="formularioPantallas">
			<div class="tituloFormulario">Configuración de las Pantallas</div>
			<div class="seccion">
				<p class="instrucciones">Es necesario que indique una cadena con la que cada Pantalla pueda ser identificada, como el título o algún párrafo que contenga.</p>
			</div>
			<div class="seccion" id="seccionPantallas">
				<table class="seccion" id="tablaPantallas">
				<!--  -->
				</table>
			</div>
		</div>
		<br />
		<div align="center">
			<s:url var="urlAnterior"
				value="%{#pageContext.request.contextPath}/configuracion-casos-uso-previos!prepararConfiguracion">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlAnterior}?idCU=${idCU}'"
				value="Anterior" />
			<input class="boton" type="button"
				onclick="guardarSalir();"
				value="Guardar" />
			<input class="boton" type="button"
				onclick="aceptar();"
				value="Finalizar" />

			<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/cu">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarCU}'"
				value="Cancelar" />
		</div>
		
		<s:hidden name="jsonAcciones" id="jsonAcciones" value="%{jsonAcciones}"/>
		<s:hidden name="jsonEntradas" id="jsonEntradas" value="%{jsonEntradas}"/>
		<s:hidden name="jsonReferenciasReglasNegocio" id="jsonReferenciasReglasNegocio" value="%{jsonReferenciasReglasNegocio}"/>
		<s:hidden name="jsonReferenciasParametrosMensaje" id="jsonReferenciasParametrosMensaje" value="%{jsonReferenciasParametrosMensaje}"/>
		<s:hidden name="jsonPantallas" id="jsonPantallas" value="%{jsonPantallas}"/>
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