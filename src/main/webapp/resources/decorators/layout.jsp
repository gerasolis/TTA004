<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:decorator="http://www.opensymphony.com/sitemesh/decorator"
	xmlns="http://www.w3.org/1999/xhtml" xmlns:s="/struts-tags"
	xmlns:sj="/struts-jquery-tags">

	<jsp:directive.page language="java"
		contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" />
	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>PRISMA | <decorator:title default="Bienvenido" /></title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/estilo.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/DataTables-1.10.7/media/css/jquery.dataTables.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/jquery.atwho.css" />
<s:url id="urlRutaContexto" includeParams="none"
	value="%{pageContext.request.contextPath}/resources/template/themes"
	includeContext="true" />
<sj:head debug="false" jqueryui="true" jquerytheme="smoothness-prisma"
	customBasepath="%{urlRutaContexto}" locale="es" />

<decorator:head />
<![CDATA[
	
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/DataTables-1.10.7/media/js/jquery.dataTables.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/menu.js"></script>
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/resources/scripts/dataTable.js"></script>
]]>

</head>
<body>
	<div class="container">
		<div class="banner"><jsp:include
				page="/resources/decorators/banner.jsp" /></div>
		<div class="menuPrincipal">
			<s:if test="#session != null">
				<s:if test="#session.login == true">
					<s:set name="id" value="#session.id"></s:set>
					<s:set var="perfil">/content/<s:property
							value="@mx.prisma.controller.AccessCtrl@getMenu()" />.jsp</s:set>
					<jsp:include page="${perfil}" />
				</s:if>
			</s:if>
		</div>
		<div class="menuSecundario">
			<!--  -->
		</div>

		<div class="areaTrabajo" id="idAreaTrabajo">
				<s:if test="#session.idProyecto != null">
					<table class = "info">
						<tr>
							<td align="right">Proyecto:
							<s:property value="proyecto.clave" /> - <s:property
									value="proyecto.nombre" /></td>
						</tr>
					</table>
				</s:if>
				<s:if test="#session.idModulo != null and modulo!=null">
					<table class = "info">
						<tr>
							<td align="right">Módulo:
							<s:property value="modulo.clave" /> - <s:property
									value="modulo.nombre" /></td>
						</tr>
					</table>
				</s:if>
				<s:if test="#session.idCU != null">
					<table class = "info">
						<tr>
							<td align="right">Caso de uso:
							<s:property value="casoUso.clave + casoUso.numero + ' '" /><s:property
									value="casoUso.nombre" /></td>
						</tr>
					</table>
				</s:if>

			<decorator:body />
		</div>
	</div>
	<input type="text" style="display: none;" id="rutaContexto"
		value="${pageContext.request.contextPath}" />
</body>
	</html>
</jsp:root>

