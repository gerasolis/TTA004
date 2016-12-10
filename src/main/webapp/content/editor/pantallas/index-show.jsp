<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Pantalla</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/pantallas/js/index-show.js"></script>	
]]>

</head>
<body>
	<h1>Consultar Pantalla</h1>
	<h3><s:property value="model.nombre"/></h3>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
		<div class="formulario">
			<div class="tituloFormulario">${blanks}</div>
			<div class="seccion">
				<h4><s:property value="model.nombre"/></h4>
				<p class="instrucciones"><s:property value="model.descripcion"/></p>
			</div>
			<div class="marcoImagen" id="marco-pantalla" style="display: none;">
				<center>
					<img id="pantalla" src="#" class="imagen" />
				</center>
			</div>
		</div>
		
		
		<div class="formulario" id="seccion-acciones" style="display: none;">
			<div class="tituloFormulario">${blanks}</div>
			<div class="seccion" id="acciones">
				<h5><s:text name="labelAcciones" /></h5>
				<s:iterator value="model.acciones" var="accion">
				<table class="tablaConsulta">
						<tr>
							<td>
								<div class="imagenAccion">
									<div class="marcoImagen" id="marco-accion${accion.id}" style="display: none;">
										<center><img src="#" id="accion${accion.id}"/></center>
									</div>
								</div>
								<div class="descripcionAccion">
									<a name="accion-${accion.id}"><!-- accion --></a>
									<span class="labelIzq consulta"><s:property value="%{#accion.nombre}"/></span>
												<span class="ui-widget "> 
												${blanks} <s:property value="%{#accion.descripcion}"/>
												${blanks}
												</span>
									<div>
										<span class="labelIzq consulta"><s:text name="labelTipoAccion" /></span>
										<span class="ui-widget "> 
													${blanks} <s:property value="#accion.tipoAccion.nombre"/>
													${blanks}
										</span>
									</div>
									<div>
										<span class="labelIzq consulta"><s:text name="labelPantallaDestino" /></span>
										<span class="ui-widget "> 
													${blanks}
													<s:if test="#accion.pantallaDestino.id == model.id">
														<s:text name="labelPantallaActual"/>
													</s:if> <s:else>
													<a class="referencia"
														href='${pageContext.request.contextPath}/pantallas/${accion.pantallaDestino.id}'>
															${accion.pantallaDestino.clave}
															${accion.pantallaDestino.numero} ${blanks}
															${accion.pantallaDestino.nombre}</a>
													</s:else> 
													
										</span>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</s:iterator>
			</div>
		</div>

		<br />
		<div align="center">
			<input class="boton" type="button"
				onclick="location.href='${urlPrev}'"
				value="Aceptar" />
		</div>
		
		<s:hidden id="pantallaB64" name="pantallaB64"
			value="%{pantallaB64}" />
		<s:hidden id="jsonAccionesTabla" name="jsonAccionesTabla"
			value="%{jsonAccionesTabla}" />
		<s:hidden id="jsonImagenesAcciones" name="jsonImagenesAcciones"
			value="%{jsonImagenesAcciones}" />



</body>
	</html>
</jsp:root>
