<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Configuración Trayectorias principal y alternativas</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casosUsoPrevios.js"></script>	
]]>

</head>
<body>
	<div class="menuWizard">
	<img id="" src="${pageContext.request.contextPath}/resources/images/wizard/ww3.png" />
	</div>

	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />

	<p class="instrucciones">Para configurar el caso de uso <b><s:property value="casoUso.clave + casoUso.numero + ' ' + casoUso.nombre"/></b> es necesario que configure
	las trayectorias se muestran a continuación.</p>
	<s:form autocomplete="off" id="frmActor" theme="simple"
		action="%{#pageContext.request.contextPath}/configuracion-trayectorias!configurar" method="post">
		<s:hidden name="_method" value="put" />
		<div class="formulario">
			<div class="tituloFormulario">Trayectorias</div>
			<div class="seccion">
				<table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
				<!-- <thead>
					<tr>
						<th><s:text name="colTrayectoria"/></th>
						<th style="width: 20%;"><s:text name="colEstado"/></th>
						<th style="width: 20%;"><s:text name="colAcciones"/></th>
					</tr>
				</thead>
				<tbody>
				<tr>
						<td>Trayectoria principal</td>
						<td>Configurada</td>
						<td align="center">
							<s:url var="urlConfigurarTrayectoria"
									value="%{#pageContext.request.contextPath}/configuracion-trayectoria!prepararConfiguracionTrayectoria">
									<s:param name="idCUPrevio" value="%{#cu.id}"></s:param>
							</s:url>
						<s:a href="%{urlConfigurarTrayectoria}">			
							<img id="" class="button" title="Configurar Caso de uso"
											src="${pageContext.request.contextPath}/resources/images/icons/configurar.png" />
						</s:a>
						</td>
				</tr>
				<tr>
						<td>Trayectoria alternativa A</td>
						<td>No configurada</td>
						<td align="center">
							<s:url var="urlConfigurarTrayectoria"
									value="%{#pageContext.request.contextPath}/configuracion-trayectoria!prepararConfiguracionTrayectoria">
									<s:param name="idCUPrevio" value="%{#cu.id}"></s:param>
							</s:url>
						<s:a href="%{urlConfigurarTrayectoria}">			
							<img id="" class="button" title="Configurar Caso de uso"
											src="${pageContext.request.contextPath}/resources/images/icons/configurar.png" />
						</s:a>
						</td>
				</tr>-->
			 <thead>
					<tr>
						<th><s:text name="colTrayectoria"/></th>
						<th style="width: 20%;"><s:text name="colEstado"/></th>
						<th style="width: 20%;"><s:text name="colAcciones"/></th>
					</tr>
				</thead>
				<tbody>
				<s:iterator value="ListTrayectoria" var="t">
					<tr class="${'filaCU'}${trayectoria.Id}">
						<td><s:property value="'Trayectoria '+ #t.clave"/></td>
						<td><s:property value="#t.Estado"/></td>
						
						<td align="center">
							<s:url var="urlConfigurarTrayectoria"
								value="%{#pageContext.request.contextPath}/configuracion-trayectoria!prepararConfiguracion">
								<s:param name="idTrayectoria" value="%{#t.id}"></s:param>
							</s:url>			
							<s:a href="%{urlConfigurarTrayectoria}">
								<img id="" class="button" title="Configurar Caso de uso"
										src="${pageContext.request.contextPath}/resources/images/icons/configurar.png" />
							</s:a>
						</td>
					  </tr>
				</s:iterator>
				</tbody>
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
		
			<s:submit class="boton" value="Finalizar"/>
			
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