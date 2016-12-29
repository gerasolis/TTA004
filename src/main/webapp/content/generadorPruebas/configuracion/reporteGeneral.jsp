<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Reporte general de errores</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/casosUsoPrevios.js"></script>	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/generadorPruebas/configuracion/js/bootstrap.min.js"></script>	
]]>

</head>
<body>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<h1>Reporte general de errores </h1>
	<p class="instrucciones">A continuación se presentan todos los casos de uso del módulo <b><s:property value="casoUso.clave + casoUso.numero + ' ' + casoUso.nombre"/></b>, 
	así como las pruebas ejecutadas en cada caso de uso y los errores detectados.</p>
	
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	<s:iterator value="ListCasosUso" var="cu">
		<s:if test="%{#cu.reporte == 1}">
			  <div class="panel panel-default">
			    <div class="panel-heading" role="tab" id="headingOne">
			      <h4 class="panel-title">
			        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
			          <s:property value="#cu.clave + #cu.numero + ' ' +#cu.nombre"/>
			        </a>
			      </h4>
			    </div>
			    <div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
			      <div class="panel-body">
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
								<s:if test="%{#cu.id == #e.casoUsoid.id}">
									<tr class="${'filaCU'}${trayectoria.Id}">
										<td><s:property value="#e.tipoError"/></td>
										<td><s:property value="#e.numError"/></td>
										<td><s:property value="#e.porcentaje"/></td>
										<td><s:property value="#e.porcentajeTodo"/></td>
									</tr>
								</s:if>
							</s:iterator>
							</tbody>
						</table>
			      </div>
			    </div>
			  </div>
		  </s:if>	
	 </s:iterator>
	</div>
	<!--<s:form autocomplete="off" id="frmActor" theme="simple"
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
					</tr>
				</s:iterator>
				</tbody>
			</table>
			</div>
		</div>
		<br />
		<div align="center">
			<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/cu">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarCU}'"
				value="Listo" />	
		</div>
	</s:form>-->
</body>
</html>
</jsp:root>