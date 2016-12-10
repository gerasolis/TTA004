<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0"
	xmlns:s="/struts-tags" xmlns:sj="/struts-jquery-tags">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Entidad</title>

</head>
<body>
	<h1>Consultar Entidad</h1>
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
		</div>
		<div class="formulario">
			<div class="tituloFormulario">${blanks}</div>
			<div class="seccion">
				<h5><s:text name="labelAtributos" /></h5>
				<s:iterator value="atributos" var="atributo">
					<table>
						<tr>
							<td class="definicion">
								<a name="atributo-${atributo.id}"><!-- ${atributo.nombre} --></a>
								<span class="labelIzq consulta"><s:property value="#atributo.nombre"/></span>
								<span class="ui-widget "> 
								${blanks} <s:property value="#atributo.descripcion"/>
								${blanks}
								
								<s:if test="#atributo.tipoDato.nombre == 'Cadena'">
									<s:text name="descripcionCadena">
										<s:param><s:property value="#atributo.longitud"/></s:param>
									</s:text> ${blanks}
								</s:if> <s:elseif test="#atributo.tipoDato.nombre == 'Fecha'">
									<s:text name="descripcionFecha"/> ${blanks}
								</s:elseif> <s:elseif test="#atributo.tipoDato.nombre == 'Archivo'">
									<s:text name="descripcionArchivo">
										<s:param><s:property value="#atributo.formatoArchivo"/></s:param>
										<s:param><s:property value="#atributo.tamanioArchivo"/></s:param>
										<s:param><s:property value="#atributo.unidadTamanio.abreviatura"/></s:param>
									</s:text> ${blanks}
								</s:elseif> <s:elseif test="#atributo.tipoDato.nombre == 'Booleano'">
									<s:text name="descripcionBooleano"/> ${blanks}
								</s:elseif> <s:elseif test="#atributo.tipoDato.nombre == 'Entero'">
									<s:text name="descripcionEntero">
										<s:param><s:property value="#atributo.longitud"/></s:param>
									</s:text> ${blanks}
								</s:elseif> <s:elseif test="#atributo.tipoDato.nombre == 'Flotante'">
									<s:text name="descripcionFlotante">
										<s:param><s:property value="#atributo.longitud"/></s:param>
									</s:text> ${blanks}
								</s:elseif> <s:elseif test="#atributo.tipoDato.nombre == 'Otro'">
									<s:text name="descripcionOtro">
										<s:param><s:property value="#atributo.otroTipoDato"/></s:param>
									</s:text> ${blanks}
								</s:elseif> 
								
								
								<s:if test="#atributo.obligatorio">
									<s:text name="descripcionObligatorio"/> ${blanks}
								</s:if> <s:else>
									<s:text name="descripcionOpcional"/> ${blanks}
								</s:else>
								
								</span>
							</td>
						</tr>
					</table>
					<br/>
				</s:iterator>
				
			</div>
		</div>

		<br />
		<div align="center">
			<input class="boton" type="button"
				onclick="location.href='${urlPrev}'"
				value="Aceptar" />
		</div>
		



</body>
	</html>
</jsp:root>
