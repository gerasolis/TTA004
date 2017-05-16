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
<style>
	th {text-align: center;}
</style>
</head>
<body>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<h1>Reporte general de pruebas </h1>
	<s:iterator value="Modulo" var="m">
	<p class="instrucciones">A continuación se presentan todos los casos de uso del módulo <b><s:property value="#m.clave  + ' - ' +#m.nombre"/></b>, 
	así como las pruebas ejecutadas en cada caso de uso y los errores detectados.</p>
	</s:iterator>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	<s:iterator value="ListCasosUso" var="cu">
		<s:if test="%{#cu.reporte == 1}">
			  <div class="panel panel-default">
			    <div class="panel-heading" role="tab" id="headingOne">
			      <h4 class="panel-title">
			        <s:a role="button" data-toggle="collapse" data-parent="#accordion" href="#cu%{#cu.id}" aria-expanded="true" aria-controls="collapseOne">
			          <s:property value="#cu.clave + #cu.numero + ' ' +#cu.nombre"/>
			        </s:a>
			      </h4>
			    </div>
			    <s:div id="cu%{#cu.id}" class="collapse" role="tabpanel" aria-labelledby="headingOne">
			   		<s:iterator value="ListPruebas" var="p">
			   			<s:if test="%{#cu.id == #p.casoUsoid.id}">
						    <div class="panel-heading" role="tab" id="collapseOne">
						        <s:a role="button" data-toggle="collapse" href="#p%{#p.id}" aria-expanded="true" aria-controls="collapseOne">
						           <s:if test="%{#p.estado == 1}">
						           	<p class="instrucciones"><img src="/prisma/resources/images/icons/wrong.png" title="checked" class="button" id=""/> <s:property value="'  Prueba No. '+#p.id+' realizada el: '+#p.fecha"/></p>
						           </s:if>
						           <s:elseif test="%{#p.estado == 0}">
						           	<p class="instrucciones"><img src="/prisma/resources/images/icons/checked.png" title="checked" class="button" id=""/> <s:property value="'  Prueba No. '+#p.id+' realizada el: '+#p.fecha"/></p>
						           </s:elseif>
						        </s:a>
			   				</div>
							 <s:div id="p%{#p.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
							      <div class="panel-body">
								      <table id="gestion" class="tablaGestion" cellspacing="0" width="100%"> 
										 <thead>
												<tr>
													<th></th>
													<th><s:text name="CU"/></th>
													<th><s:text name="Pantalla"/></th>
													<th><s:text name="Trayectoria"/></th>
													<th><s:text name="Paso"/></th>
													<th><s:text name="Entrada"/></th>
													<th><s:text name="Mensaje"/></th>
													<th><s:text name="Tipo de error"/></th>
													<th style="width: 20%;"><s:text name="Núm de error"/></th>
													<!--<th style="width: 20%;"><s:text name="Porcentaje"/></th>
													<th style="width: 20%;"><s:text name="Porcentaje de todo"/></th>-->
												</tr>
											</thead>
											<s:iterator value="ListErrores" var="e">
							   					<s:if test="%{#p.id == #e.pruebaid.id}">
													<tbody>
															<tr>
																<s:if test="%{#e.numError == 0}">
																	<td><img src="/prisma/resources/images/icons/checked.png" title="checked" class="button" id=""/></td>
																</s:if>
																<s:else>
																	<td><img src="/prisma/resources/images/icons/wrong.png" title="checked" class="button" id=""/></td>
																</s:else>
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
																			<s:if test="%{#en.CasoUso.id == #e.pruebaid.CasoUsoid.id}">
																				<li><s:property value="#en.Atributo.nombre"/></li>
																			</s:if>
																		</s:iterator>
																	</ul>
																</td>
																<td><s:property value="#e.mensajeid.clave + ' ' + #e.mensajeid.numero"/></td>
																<td><s:property value="#e.tipoError"/></td>
																<td><p align="center"><s:property value="#e.numError"/></p></td>
																<!--<td><s:property value="#e.porcentaje"/></td>
																<td><s:property value="#e.porcentajeTodo"/></td>-->
															</tr>
													</tbody>
												</s:if>
											</s:iterator>
										</table>
							      </div>
						    </s:div>
				    	</s:if>
					</s:iterator>
				</s:div>
			  </div>
		  </s:if>	
	 </s:iterator>
	</div>
		<div align="center">
			<s:url var="urlGestionarCU"
				value="%{#pageContext.request.contextPath}/cu">
			</s:url>
			<input class="boton" type="button"
				onclick="location.href='${urlGestionarCU}'"
				value="Aceptar" />
		</div>
	<!--</s:form>-->
</body>
</html>
</jsp:root>