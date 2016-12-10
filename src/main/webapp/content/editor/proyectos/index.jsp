<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Proyectos</title>
<![CDATA[
	<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/content/editor/proyectos/js/index.js"></script>
]]>
</head>

<body>
	<h1>Gestionar Proyectos</h1>
	<s:actionmessage theme="jquery" />
	<s:actionerror theme="jquery" />

	<br />

	<s:form autocomplete="off" theme="simple" onsubmit="return false;">
		<div class="form">
			<table id="gestion" class="tablaGestion" cellspacing="0" width="100%">
				<thead>
					<th style="width: 40%;"><s:text name="colProyecto" /></th>
					<th style="width: 40%;"><s:text name="colLiderProyecto" /></th>
					<th style="width: 20%;"><s:text name="colAcciones" /></th>
				</thead>
				<tbody>
					<s:iterator value="listProyectos" var="proyecto">
						<s:iterator value="%{#proyecto.proyecto_colaboradores}"
							var="proy_col">
							<s:if test="%{#proy_col.rol.nombre == 'Líder'}">
								<s:set name="lider" value="#proy_col.colaborador"></s:set>
								<s:set var="breakLoop" value="%{true}" />
							</s:if>
						</s:iterator>
						<tr>
							<td><s:property
									value="%{#proyecto.clave + ' ' + '-' + ' ' + #proyecto.nombre}" /></td>
							<td><s:property
									value="%{#lider.nombre + ' ' + #lider.apellidoPaterno + ' ' + #lider.apellidoMaterno}" /></td>

							<!-- Si es Líder de análisis podrá elegir los colaboradores del proyecto-->
							<td align="center">
								<s:if test="%{#lider.curp == #session.colaboradorCURP}">
									<!-- Elegir colaboradores -->
									${blanks}
									<s:url var="urlElegir"
										value="%{#pageContext.request.contextPath}/proyectos!elegirColaboradores?idSel=%{#proyecto.id}"/>
									<s:a href="%{urlElegir}">
										<img id="" class="button" title="Elegir Colaboradores"
											src="${pageContext.request.contextPath}/resources/images/icons/colaboradores.png" />										
									</s:a> 						
								</s:if>
								<!-- Entrar -->
								${blanks}
								<s:url var="urlEntrar"
										value="%{#pageContext.request.contextPath}/proyectos!entrar?idSel=%{#proyecto.id}" />
								<s:a href="%{urlEntrar}">
										<img id="" class="button" title="Entrar al Proyecto"
											src="${pageContext.request.contextPath}/resources/images/icons/entrar.png" />										
								</s:a> 
								<!-- Descargar documento -->
								${blanks}
								<s:url var="urlDescargar"
										value="%{#pageContext.request.contextPath}/proyectos!descargarDocumento?idSel=%{#proyecto.id}" >
										<s:param name="extension">pdf</s:param>
								</s:url>
								<s:a href="%{urlDescargar}" method="post">
										<img id="" class="button" title="Descargar Documento"
											src="${pageContext.request.contextPath}/resources/images/icons/pdf.png" />										
								</s:a>
								${blanks}
								<s:url var="urlDescargarDocx"
										value="%{#pageContext.request.contextPath}/proyectos!descargarDocumento?idSel=%{#proyecto.id}">
										<s:param name="extension">docx</s:param>
								</s:url>
								<s:a href="%{urlDescargarDocx}" method="post">
										<img id="" class="button" title="Descargar Documento"
											src="${pageContext.request.contextPath}/resources/images/icons/docx.png" />										
								</s:a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<br />
		<br />
	</s:form>
</body>
	</html>
</jsp:root>

