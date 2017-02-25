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
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
]]>
<style>
	th {text-align: center;}
</style>
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
		<div class="formulario" style="width: 100%;">
			<div class="tituloFormulario">Errores</div>
			<s:iterator value="ListPruebas" var="p">
			<div class="seccion">
				<p class="instrucciones"><s:property value="'Prueba No. '+#p.id+' realizada el: '+#p.fecha"/></p>
				<table class="table tablaGestion" cellspacing="0" width="100%"> 
			     <thead>
					<tr>
						<th><s:text name="CU"/></th>
						<th><s:text name="Pantalla"/></th>
						<th><s:text name="Trayectoria"/></th>
						<th><s:text name="Paso"/></th>
						<th><s:text name="Entrada"/></th>
						<th><s:text name="Dato"/></th>
						<th><s:text name="Mensaje"/></th>
						<th><s:text name="Tipo de error"/></th>
						<th style="width: 10%;"><s:text name="Núm de error"/></th>
						<!--<th style="width: 10%;"><s:text name="Porcentaje"/></th>-->
						<!--<th style="width: 10%;"><s:text name="Porcentaje de todo"/></th>-->
					</tr>
				</thead>
				
				<s:iterator value="ListErrores" var="e">
					<s:if test="%{#p.id == #e.pruebaid.id}">
					  <tbody>
						<tr>
							<td><s:property value="#e.pruebaid.CasoUsoid.clave + ' '+ #e.pruebaid.CasoUsoid.numero + ' '+#e.pruebaid.CasoUsoid.nombre"/></td>
							<s:iterator value="ListPantallas" var="pan">
								<s:if test="%{#pan.numero == #e.pruebaid.CasoUsoid.numero}">
									<td><s:property value="#pan.clave + ' ' + #pan.numero+ ' '+#pan.nombre"/></td>
								</s:if>
							</s:iterator>
							<s:if test="%{#e.pasoid.trayectoria.alternativa == 0}">
								<td><s:property value="#e.pasoid.trayectoria.clave"/></td>
							</s:if>	
							<s:else>
								<td><s:property value="#e.pasoid.trayectoria.clave + ': ' + #e.pasoid.trayectoria.condicion"/></td>
							</s:else>
							<td><s:property value="#e.pasoid.redaccion"/></td>
							<td>
								<ul>
									<s:iterator value="ListEntradas" var="en">
										<li><s:property value="#en.atributo.nombre"/></li>
									</s:iterator>
								</ul>
							</td>
							<td><s:property value="#e.pruebaid.casouso.id"/></td>
							<td><s:property value="#e.mensajeid.clave + ' ' + #e.mensajeid.numero"/></td>
							<!--<s:iterator value="ListMensajes2" var="m">
								<td><s:property value="#m.redaccion"/></td>
								<s:if test="%{#m.id == #e.pruebaid.id}">
									<td><s:property value="#m.redaccion"/></td>
								</s:if>
							</s:iterator>-->
					
							<td><s:property value="#e.tipoError"/></td>
							<td><p align="center"><s:property value="#e.numError"/></p></td>
							<!--<td><s:property value="#e.porcentaje"/></td>-->
							<!--<td><s:property value="#e.porcentajeTodo"/></td>-->
					    </tr>
				     </tbody>
					</s:if>
				</s:iterator>
				
			</table>
			</div>
		  </s:iterator>
			
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