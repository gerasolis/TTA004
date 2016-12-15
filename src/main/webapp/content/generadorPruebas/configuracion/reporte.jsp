<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Reporte de errores</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casosUsoPrevios.js"></script>	
]]>

</head>
<body>
	
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<h1>Reporte de errores </h1>
	<p class="instrucciones">A continuación se presentan los errores generados en la prueba del <b><s:property value="casoUso.clave + casoUso.numero + ' ' + casoUso.nombre"/></b>.</p>
	<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configuracion-trayectorias!configurar" method="post">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Errores</div>
			<div class="seccion">
				<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
			 <thead>
					<tr>
						<th><s:text name="Tipo de error"/></th>
						<th style="width: 20%;"><s:text name="Núm de error"/></th>
						<th style="width: 20%;"><s:text name="Porcentaje"/></th>
						<th style="width: 20%;"><s:text name="Porcentaje de todo"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="ListErrores" var="e">
					<tr class="${'filaCU'}${trayectoria.Id}">
						<td><s:property value="#e.tipoError"/></td>
						<td><s:property value="#e.numError"/></td>
						<td><s:property value="#e.porcentaje"/></td>
						<td><s:property value="#e.porcentajeTodo"/></td>
						<!--
						<td align="center">
							<s:url var="urlConfigurarTrayectoria"
								value="%{#pageContext.request.contextPath}/configuracion-trayectoria!prepararConfiguracion">
								<s:param name="idTrayectoria" value="%{#t.id}"></s:param>
							</s:url>			
							<s:a href="%{urlConfigurarTrayectoria}">
								<img id="" class="button" title="Configurar Caso de uso"
										src="${pageContext.request.contextPath}/resources/images/icons/configurar.png" />
							</s:a>
						</td>-->
					  </tr>
				</s:iterator>
				</tbody>
			</table>
			</div>
		</div>
		
		<br />
		<div align="center">
			<!-- 
			<s:url var="urlAnterior"
				value="%{#pageContext.request.contextPath}/configuracion-casos-uso-previos!prepararConfiguracion">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlAnterior}?idCU=${idCU}'"
				value="Anterior" />
		
			<s:submit class="boton" value="Finalizar"/>
			-->
			<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/cu">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarCU}'"
				value="Listo" />
				
		</div>
	</s:form>
	
</body>
	</html>
</jsp:root>