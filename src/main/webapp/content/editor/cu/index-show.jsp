<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Casos de uso</title>

</head>
<body>
	<h1>Consultar Caso de uso</h1>
	<h3><s:property value="%{model.clave + ' ' + model.numero + ' ' + model.nombre}" /></h3>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />
	<br />
	<div class="formulario">
		<div class="tituloFormulario">${blanks}</div>
		<div class="seccion">
			<h4><s:property value="%{model.clave + ' ' + model.numero + ' ' + model.nombre}" /></h4>
			<h5>
				<s:text name="labelResumen"></s:text>
			</h5>
			<p class="instrucciones">
				<s:text name="model.descripcion"></s:text>
			</p>
			<br />
			<table class="tablaDescripcion">
				<tr>
					<td colspan="2" class="encabezadoTabla">Información general
						del Caso de uso</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelEstado" /></td>
					<td class="ui-widget inputFormulario">${model.estadoElemento.nombre}</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelActores" /></td>
					<td class="ui-widget inputFormulario">${model.redaccionActores}</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelEntradas" /></td>
					<td class="ui-widget">${model.redaccionEntradas}</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelSalidas" /></td>
					<td class="ui-widget">${model.redaccionSalidas}</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelReglasNegocio" /></td>
					<td class="ui-widget">${model.redaccionReglasNegocio}</td>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelPrecondiciones"></s:text>
					</td>
					<s:if test="existenPrecondiciones">
						<td class="ui-widget">
							<ul>
								<s:iterator value="model.postprecondiciones"
									var="postprecondicion">
									<s:if test="%{#postprecondicion.precondicion}">
										<li>${postprecondicion.redaccion}</li>
									</s:if>
								</s:iterator>
							</ul>
						</td>
					</s:if>
					<s:else>
						<td class="ui-widget"><s:text name="labelSinInformacion" /></td>
					</s:else>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelPostcondiciones"></s:text>
					</td>
					<s:if test="existenPostcondiciones">
						<td class="ui-widget">
							<ul>
								<s:iterator value="model.postprecondiciones"
									var="postprecondicion">
									<s:if test="!#postprecondicion.precondicion">
										<li>${postprecondicion.redaccion}</li>
									</s:if>
								</s:iterator>
							</ul>
						</td>
					</s:if>
					<s:else>
						<td class="ui-widget"><s:text name="labelSinInformacion" /></td>
					</s:else>
				</tr>
				<tr>
					<td class="label consulta"><s:text name="labelTipo"></s:text>
					</td>
					<td>
						<s:if test="model.extendidoDe.size > 0">
							<s:text name="labelSecundario"/>
							<ul>
								<s:iterator value="model.extendidoDe" var="extension">
									<li><s:property value="%{#extension.casoUsoOrigen.clave + #extension.casoUsoOrigen.numero + ' ' + #extension.casoUsoOrigen.nombre}"/></li>
								</s:iterator>
							</ul>
						</s:if> <s:else>
							<s:text name="labelPrimario" />
						</s:else>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="formulario">
		<div class="tituloFormulario">
			${blanks}
		</div>
		<div class="seccion">

			<h5>
				<s:text name="labelTrayectorias"></s:text>
			</h5>
			<s:if test="model.trayectorias.size == 0">
				<p class="instrucciones">
					<s:text name="labelSinTrayectorias" />
				</p>
			</s:if>
			<s:else>
				<s:iterator value="model.trayectorias" var="tray">
					<s:iterator value="ListPasos" var="lpt">
						<s:iterator value="lpt" var="lptito">
							<s:if test="#lptito.trayectoria.id == #tray.id">
								<s:set var="ListPasosActual" value="%{#lpt}" />
							</s:if>
						</s:iterator>
					</s:iterator>
					<!--<s:property value="'IMPRIME' + ListPasosActual"/>-->
							<s:if test="!#tray.alternativa">
								<h6>
									<a name="trayectoria-${tray.id}"><!--  --></a>
									<s:text name="labelTrayectoriaPrincipal"/><s:property value="' ' + #tray.clave"/>
								</h6>
								<!-- AQUÍ -->
								<ol>
									<s:iterator value="ListPasosActual" var="paso">
										<a name="paso-${#paso.id}"></a>
										<li class="ui-widget">
										<s:if test="#paso.realizaActor">
												<img
													src="${pageContext.request.contextPath}/resources/images/icons/actor.png" />
											</s:if> <s:else>
												<img
													src="${pageContext.request.contextPath}/resources/images/icons/uc.png" />
											</s:else> <s:if test="#paso.verbo.nombre == 'Otro'">
									${blanks} ${paso.otroVerbo} ${blanks} ${paso.redaccion}
								</s:if> <s:else>
									${blanks} ${paso.verbo.nombre} ${blanks} ${paso.redaccion}
								</s:else></li>
									</s:iterator>
									<s:if test="#tray.finCasoUso">
										<li class="ui-widget" style="list-style-type: square"><s:text
												name="labelfinCasoUso" /></li>
									</s:if>
								</ol>
		
								<s:set var="breakLoop" value="%{true}" />
							</s:if>
				</s:iterator>
				
				<s:iterator value="model.trayectorias" var="tray">
				<s:iterator value="ListPasos" var="lpt">
						<s:iterator value="lpt" var="lptito">
							<s:if test="#lptito.trayectoria.id == #tray.id">
								<s:set var="ListPasosActual" value="%{#lpt}" />
								<!--<s:property value="'IMPRIME: ' + ListPasosActual"/>-->
								<!--<s:text name="idAtributo" value="#lpt" />-->
							</s:if>
						</s:iterator>
					</s:iterator>
					<s:if test="#tray.alternativa">
						<h6>
							<a name="trayectoria-${tray.id}"><!--  --></a>
							<s:text name="labelTrayectoriaAlternativa" />
							<s:property value="' ' + #tray.clave" />
						</h6>
						<div>
						<span class="ui-widget " >
						<span class="labelIzq consulta"><s:text name="labelCondicion"/></span>
						${blanks}<s:property value="#tray.condicion"/>
						</span>
						
						</div>
						<!-- AQUÍ -->
						<ol>
							<s:iterator value="ListPasosActual" var="paso">
								<li class="ui-widget"><s:if test="#paso.realizaActor">
										<img
											src="${pageContext.request.contextPath}/resources/images/icons/actor.png" />
									</s:if> <s:else>
										<img
											src="${pageContext.request.contextPath}/resources/images/icons/uc.png" />
									</s:else> <s:if test="#paso.verbo.nombre == 'Otro'">
							${blanks} ${paso.otroVerbo} ${blanks} ${paso.redaccion}
						</s:if> <s:else>
							${blanks} ${paso.verbo.nombre} ${blanks} ${paso.redaccion}
						</s:else></li>
							</s:iterator>
							<s:if test="#tray.finCasoUso">
								<li class="ui-widget" style="list-style-type: square"><s:text
										name="labelfinCasoUso" /></li>
							</s:if>
						</ol>
						<s:set var="breakLoop" value="%{true}" />
					</s:if>
				</s:iterator>
			</s:else>
		</div>
	</div>
	<s:if test="model.extiende.size > 0">
		<div class="formulario">
			<div class="tituloFormulario">
				${blanks}
			</div>
			<div class="seccion">
				<h5>
					<s:text name="labelExtensiones"></s:text>
				</h5>
				<s:iterator value="model.extiende" var="extension">
					<table>
						<tr>
							<td><span class="labelIzq consulta"><s:text
										name="labelCausa" /></span> <span class="ui-widget">${blanks}
									${extension.causa}</span> <br /> <span class="labelIzq consulta"><s:text
										name="labelRegion" /></span> <span class="ui-widget">${blanks}
									${extension.region}</span> <br /> <span class="labelIzq consulta"><s:text
										name="labelCasoUsoExtiende" /></span> <span class="ui-widget">
									${blanks} 
										<s:property value="%{#extension.casoUsoDestino.clave + #extension.casoUsoDestino.numero + ' ' + #extension.casoUsoDestino.nombre}"/>
							</span></td>
						</tr>
					</table>
				</s:iterator>
			</div>
		</div>
	</s:if>


	<br />
	<div align="center">
		<input class="boton" type="button"
			onclick="location.href='${urlPrev}'" value="Aceptar" />
	</div>
</body>
	</html>
</jsp:root>