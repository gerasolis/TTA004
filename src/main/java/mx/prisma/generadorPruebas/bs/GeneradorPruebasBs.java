package mx.prisma.generadorPruebas.bs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import static java.nio.charset.StandardCharsets.*;

import mx.prisma.bs.ReferenciaEnum;
import mx.prisma.bs.ReferenciaEnum.TipoReferencia;
import mx.prisma.bs.TipoReglaNegocioEnum;
import mx.prisma.bs.TipoReglaNegocioEnum.TipoReglaNegocioENUM;
import mx.prisma.editor.bs.CuBs;
import mx.prisma.editor.bs.TokenBs;
import mx.prisma.editor.bs.TrayectoriaBs;
import mx.prisma.editor.dao.EntradaDAO;
import mx.prisma.editor.dao.ReferenciaParametroDAO;
import mx.prisma.editor.model.Accion;
import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.MensajeParametro;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.editor.model.Paso;
import mx.prisma.editor.model.ReferenciaParametro;
import mx.prisma.editor.model.ReglaNegocio;
import mx.prisma.editor.model.Trayectoria;
import mx.prisma.generadorPruebas.bs.AnalizadorPasosBs.TipoPaso;
import mx.prisma.generadorPruebas.dao.ConfiguracionDAO;
import mx.prisma.generadorPruebas.dao.ValorDesconocidoDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ConfiguracionBaseDatos;
import mx.prisma.generadorPruebas.model.ConfiguracionHttp;
import mx.prisma.generadorPruebas.model.Query;
import mx.prisma.generadorPruebas.model.ValorEntrada;
import mx.prisma.generadorPruebas.model.ValorEntradaTrayectoria;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
/*import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;*/

public class GeneradorPruebasBs {
	private static String prefijoCSV = "csv_";
	private static final String prefijoPeticionJDBC = "PJ"; 
	private static final String prefijoPeticionHTTP = "PH";
	private static final String prefijoControladorIf = "CI";
	private static final String prefijoAsercion = "AS";
	private static final String prefijoContenedorCSV = "CSV";
	private static String casoUsoTesting = "";
	private static boolean caminoIdealIncierto = true; 
	private static Paso pasoIncierto;
	static int i;
	//public  static Trayectoria trayectoriaActual = new Trayectoria();;
	public static String encabezado() {
		String bloque = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n"
				+ "<jmeterTestPlan version=\"1.2\" properties=\"2.6\" jmeter=\"2.11 r1554548\">" + "\n";
		
		return bloque;
	}
	
	public static String planPruebas() {
		String bloque = 
				"<hashTree>" + "\n"
				+ "<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Plan de Pruebas\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"TestPlan.comments\"></stringProp>" + "\n"
				+ "<boolProp name=\"TestPlan.functional_mode\">false</boolProp>" + "\n"
				+ "<boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>" + "\n"
				+ "<elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"Variables definidas por el Usuario\" enabled=\"true\">" + "\n"
		        + "<collectionProp name=\"Arguments.arguments\"/>" + "\n"
		        + "</elementProp>" + "\n"
		        + "<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>" + "\n"
		        + "</TestPlan>" + "\n";
		
		return bloque;
	}
	
	public static String grupoHilos(String claveCasoUso) {
		String bloque = 
				"<hashTree>" + "\n"
				+ "<ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"TEST" + claveCasoUso + "\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>" + "\n"
				+ "<elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Controlador Bucle\" enabled=\"true\">" + "\n"
				+ "<boolProp name=\"LoopController.continue_forever\">false</boolProp>" + "\n"
				+ "<stringProp name=\"LoopController.loops\">1</stringProp>" + "\n"
				+ "</elementProp>" + "\n"
				+ "<stringProp name=\"ThreadGroup.num_threads\">1</stringProp>" + "\n"
				+ "<stringProp name=\"ThreadGroup.ramp_time\">1</stringProp>" + "\n"
				+ "<longProp name=\"ThreadGroup.start_time\">1402974414000</longProp>" + "\n"
				+ "<longProp name=\"ThreadGroup.end_time\">1402974414000</longProp>" + "\n"
				+ "<boolProp name=\"ThreadGroup.scheduler\">false</boolProp>" + "\n"
				+ "<stringProp name=\"ThreadGroup.duration\"></stringProp>" + "\n"
				+ "<stringProp name=\"ThreadGroup.delay\"></stringProp>" + "\n"
				+ "</ThreadGroup>" + "\n";
		return bloque;
	}
	
	public static String cookieManager() {
		String bloque = 
				"<hashTree>" + "\n"
				+ "<CookieManager guiclass=\"CookiePanel\" testclass=\"CookieManager\" testname=\"HTTP Cookie Manager\" enabled=\"true\">" + "\n"
				+ "<collectionProp name=\"CookieManager.cookies\"/>" + "\n"
				+ "<boolProp name=\"CookieManager.clearEachIteration\">false</boolProp>" + "\n"
				+ "</CookieManager>" + "\n"
				+ "<hashTree/>" + "\n";
		
		return bloque;
	}
	
	public static String configuracionJDBC(String urlBaseDatos, String driver, String usuario, String password) {
		String bloque =
				"<JDBCDataSource guiclass=\"TestBeanGUI\" testclass=\"JDBCDataSource\" testname=\"JDBC-Default\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"dataSource\">JDBC Default</stringProp>" + "\n"
				+ "<stringProp name=\"poolMax\">10</stringProp>" + "\n"
				+ "<stringProp name=\"timeout\">10000</stringProp>" + "\n"
				+ "<stringProp name=\"trimInterval\">60000</stringProp>" + "\n"
				+ "<boolProp name=\"autocommit\">true</boolProp>" + "\n"
				+ "<stringProp name=\"transactionIsolation\">DEFAULT</stringProp>" + "\n"
				+ "<boolProp name=\"keepAlive\">true</boolProp>" + "\n"
				+ "<stringProp name=\"connectionAge\">5000</stringProp>" + "\n"
				+ "<stringProp name=\"checkQuery\">Select 1</stringProp>" + "\n"
				+ "<stringProp name=\"dbUrl\">" + urlBaseDatos + "</stringProp>" + "\n"
				+ "<stringProp name=\"driver\">" + driver + "</stringProp>" + "\n"
				+ "<stringProp name=\"username\">" + usuario + "</stringProp>" + "\n"
				+ "<stringProp name=\"password\">"+ password +"</stringProp>" + "\n"
				+ "</JDBCDataSource>" + "\n"
				+ "<hashTree/>" + "\n";
		return bloque;
	}
	
	public static String configuracionHTTP(String ip, String puerto) {
		String bloque = 
				"<ConfigTestElement guiclass=\"HttpDefaultsGui\" testclass=\"ConfigTestElement\" testname=\"HTTP Default\" enabled=\"true\">" + "\n"
				+ "<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">" + "\n"
				+ "<collectionProp name=\"Arguments.arguments\"/>" + "\n"
				+ "</elementProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.domain\">"+ ip +"</stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.port\">"+ puerto +"</stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.response_timeout\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.protocol\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.path\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.implementation\">Java</stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.concurrentPool\">4</stringProp>" + "\n"
				+ "</ConfigTestElement>" + "\n"
				+ "<hashTree/>" + "\n";
				
		return bloque;
	}
	
	public static String peticionHTTP(String id, String url, ArrayList<String> parametros, String metodo, String paso, boolean hijos) {
		String bloque =
				"<HTTPSamplerProxy guiclass=\"HttpTestSampleGui\" testclass=\"HTTPSamplerProxy\" testname=\""+ id +"\" enabled=\"true\">" + "\n"
				+ "<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">" + "\n"
				+ "<collectionProp name=\"Arguments.arguments\">" + "\n";
				
		bloque += parametrosHTTP(id, parametros, metodo);
		//Para la convención de REST para metodos PUYT y DELETE
		if ("PUT".equals(metodo) || "DELETE".equals(metodo)){
			metodo = "POST";
		}
		bloque += 				
				"</collectionProp>" + "\n"
				+ "</elementProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.domain\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.port\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.response_timeout\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.protocol\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.path\">"+ url +"</stringProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.method\">"+ metodo +"</stringProp>" + "\n"
				+ "<boolProp name=\"HTTPSampler.follow_redirects\">false</boolProp>" + "\n"
				+ "<boolProp name=\"HTTPSampler.auto_redirects\">false</boolProp>" + "\n"
				+ "<boolProp name=\"HTTPSampler.use_keepalive\">true</boolProp>" + "\n"
				+ "<boolProp name=\"HTTPSampler.DO_MULTIPART_POST\">false</boolProp>" + "\n"
				+ "<boolProp name=\"HTTPSampler.monitor\">false</boolProp>" + "\n"
				+ "<stringProp name=\"HTTPSampler.embedded_url_re\"></stringProp>" + "\n"
				+ "<stringProp name=\"TestPlan.comments\">"+ paso +"</stringProp>" + "\n"
				+ "</HTTPSamplerProxy>" + "\n";
		if (hijos) {
			bloque += "<hashTree>" + "\n";
		} else {
			bloque += "<hashTree/>" + "\n";
		}
		return bloque;
		
	}
	
	public static String parametrosHTTP(String id, ArrayList<String> parametros, String metodo) {
		String bloque = "";
		
		for (String parametro : parametros) {
			bloque += 
					"<elementProp name=\""+ parametro+"\" elementType=\"HTTPArgument\">" + "\n"
					+ "<boolProp name=\"HTTPArgument.always_encode\">true</boolProp>" + "\n"
					+ "<stringProp name=\"Argument.value\">${"+ prefijoCSV + id + "_" + parametro +"}</stringProp>" + "\n"
					+ "<stringProp name=\"Argument.metadata\">=</stringProp>" + "\n"
					+ "<boolProp name=\"HTTPArgument.use_equals\">true</boolProp>" + "\n"
					+ "<stringProp name=\"Argument.name\">"+ parametro +"</stringProp>" + "\n"
					+ "</elementProp>" + "\n"; 
		}
		if ("PUT".equals(metodo) || "DELETE".equals(metodo)){
			bloque += 
					"<elementProp name=\"_method\" elementType=\"HTTPArgument\">" + "\n"
					+ "<boolProp name=\"HTTPArgument.always_encode\">true</boolProp>" + "\n"
					+ "<stringProp name=\"Argument.value\">"+metodo+"</stringProp>" + "\n"
					+ "<stringProp name=\"Argument.metadata\">=</stringProp>" + "\n"
					+ "<boolProp name=\"HTTPArgument.use_equals\">true</boolProp>" + "\n"
					+ "<stringProp name=\"Argument.name\">_method</stringProp>" + "\n"
					+ "</elementProp>" + "\n"; 
		}
		return bloque;
	}

	public static String contenedorCSV(String id, String idPeticionHTTP, ArrayList<String> parametros, String paso, boolean terminar, String nombre) {
		String bloque =
				"<CSVDataSet guiclass=\"TestBeanGUI\" testclass=\"CSVDataSet\" testname=\""+ id +"\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"filename\">entradas/" + nombre + "</stringProp>" + "\n"
				+ "<stringProp name=\"fileEncoding\"></stringProp>" + "\n"
				+ "<stringProp name=\"variableNames\">" + generarNombres(idPeticionHTTP, parametros) +"</stringProp>" + "\n"
				+ "<stringProp name=\"delimiter\">,</stringProp>" + "\n"
				+ "<boolProp name=\"quotedData\">false</boolProp>" + "\n" 
				+ "<boolProp name=\"recycle\">true</boolProp>" + "\n"
				+ "<boolProp name=\"stopThread\">false</boolProp>" + "\n"
				+ "<stringProp name=\"shareMode\">shareMode.all</stringProp>" + "\n"
				+ "<stringProp name=\"TestPlan.comments\">" + paso + "</stringProp>" + "\n"
				+ "</CSVDataSet>" + "\n"
				+ "<hashTree/>" + "\n";
				if (terminar) {
					bloque += "</hashTree>" + "\n";
				}
		
		return bloque;
	}

	public static String generarNombres(String idPeticionHTTP, ArrayList<String> parametros) {
		String bloque = "";
		for (String parametro : parametros) {
			bloque +=  (prefijoCSV + idPeticionHTTP + "_" + parametro) + ",";
		}
		
		if (bloque != "") {
			bloque = bloque.substring(0, bloque.length() - 1);
		}
		
		return bloque;
	}
	
	public static String peticionJDBC(String id, String query, String paso) {
		String bloque = 
				"<JDBCSampler guiclass=\"TestBeanGUI\" testclass=\"JDBCSampler\" testname=\""+ id +"\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"dataSource\">JDBC Default</stringProp>" + "\n"
				+ "<stringProp name=\"queryType\">Select Statement</stringProp>" + "\n"
				+ "<stringProp name=\"query\">" + query + "</stringProp>" + "\n"
				+ "<stringProp name=\"queryArguments\"></stringProp>" + "\n"
				+ "<stringProp name=\"queryArgumentsTypes\"></stringProp>" + "\n"
				+ "<stringProp name=\"variableNames\">#"+id+"</stringProp>" + "\n"
				+ "<stringProp name=\"resultVariable\"></stringProp>" + "\n"
				+ "<stringProp name=\"queryTimeout\"></stringProp>" + "\n"
				+ "<stringProp name=\"TestPlan.comments\">" + paso + "</stringProp>" + "\n"
				+ "<stringProp name=\"resultSetHandler\">Store as String</stringProp>" + "\n"
				+ "</JDBCSampler>" + "\n"
				+ "<hashTree/>" + "\n";
	
		return bloque;
	}
	
	public static String iniciarControladorIf(String id, String idPeticionJDBC, String paso, String operador) {
		System.out.println("Entro a controladorIF");
		String bloque = 
				"<IfController guiclass=\"IfControllerPanel\" testclass=\"IfController\" testname=\"" + id + "\" enabled=\"true\">" + "\n"
				+ "<stringProp name=\"TestPlan.comments\">"+ paso +"</stringProp>" + "\n"
				+ "<stringProp name=\"IfController.condition\">${#" + idPeticionJDBC + "_#} " +operador+ " 0</stringProp>" + "\n"
				+ "<boolProp name=\"IfController.evaluateAll\">false</boolProp>"+ "\n"
				+ "</IfController>" + "\n"
				+ "<hashTree>" + "\n";
		
		return bloque;
	}
	
	public static String asercion(String id, ArrayList<String> patrones, String paso) {
		String bloque =
				  "<ResponseAssertion guiclass=\"AssertionGui\" testclass=\"ResponseAssertion\" testname=\"" + id + "\" enabled=\"true\"> " + "\n"
				  + "<collectionProp name=\"Asserion.test_strings\">" + "\n"
				  + generarPatrones(patrones) 
				  + "</collectionProp>" + "\n"
				  + "<stringProp name=\"TestPlan.comments\">" + paso + "</stringProp>" + "\n"
				  + "<stringProp name=\"Assertion.test_field\">Assertion.response_data</stringProp>" + "\n"
				  + "<boolProp name=\"Assertion.assume_success\">false</boolProp>" + "\n"
				  + "<intProp name=\"Assertion.test_type\">16</intProp>" + "\n"
				  + "</ResponseAssertion>" + "\n"
				  + "<hashTree/>" + "\n"
				  + "</hashTree>" + "\n";
		
		
		return bloque;
				
	}
	
	public static String generarPatrones(ArrayList<String> patrones) {
		String bloque = "";
		for (String patron : patrones) {
			bloque +=   "<stringProp name=\"873796163\">"+ patron + "</stringProp>\n";
		}
		return bloque;
	}
	
	public static String cerrar(CasoUso casoUso) {
		String bloque = 
					"</hashTree>" + "\n"
					+ "</hashTree>" + "\n"
					+ "</hashTree>" + "\n"
					+ "</hashTree>" + "\n"
					+ "</jmeterTestPlan>" + "\n"; 
		
		return bloque;
	}
	
	public static String terminarControladorIf() {
		System.out.println("Entro a finalizarControladorif");
		return "</hashTree>\n";
	}

	public static String estadisticas() {
		String archivo = "";
		archivo += "<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\"> \n"
				+ " <boolProp name=\"ResultCollector.error_logging\">false</boolProp> \n"
				+ " <objProp> \n"
				+ " <value class=\"SampleSaveConfiguration\">\n"
				+ " <time>true</time>\n"
				+ "<latency>true</latency>"
				+ " <timestamp>true</timestamp>\n"
				+ "<success>true</success>\n"
				+ " <label>true</label>\n"
				+ " <code>true</code>\n"
				+ " <message>true</message>\n"
				+ "<threadName>true</threadName>\n"
				+ "<dataType>true</dataType>\n"
				+ "<encoding>false</encoding>\n"
				+ "<assertions>true</assertions>\n"
				+ "<subresults>true</subresults>\n"
				+ "<responseData>false</responseData>\n"
				+ "<samplerData>false</samplerData>\n"
				+ "<xml>false</xml>\n"
				+ "<fieldNames>false</fieldNames>\n"
				+ "<responseHeaders>false</responseHeaders>\n"
				+ "<requestHeaders>false</requestHeaders>\n"
				+ "<responseDataOnError>false</responseDataOnError>\n"
				+ "<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>\n"
				+ "<assertionsResultsToSave>0</assertionsResultsToSave>\n"
				+ "<bytes>true</bytes>\n"
				+ "</value>\n"
				+ "</objProp>\n"
				+ "<objProp>\n"
				+ "<value class=\"SampleSaveConfiguration\">\n"
				+ "<time>true</time>\n"
				+ "<latency>true</latency>\n"
				+ "<timestamp>true</timestamp>\n"
				+ "<success>true</success>\n"
				+ "<label>true</label>\n"
				+ "<code>true</code>\n"
				+ "<message>true</message>\n"
				+ "<threadName>true</threadName>\n"
				+ "<dataType>true</dataType>\n"
				+ "<encoding>false</encoding>\n"
				+ "<assertions>true</assertions>\n"
				+ "<subresults>true</subresults>\n"
				+ "<responseData>false</responseData>\n"
				+ "<samplerData>false</samplerData>\n"
				+ "<xml>false</xml>\n"
				+ "<fieldNames>false</fieldNames>\n"
				+ "<responseHeaders>false</responseHeaders>\n"
				+ "<requestHeaders>false</requestHeaders>\n"
				+ " <responseDataOnError>false</responseDataOnError>\n"
				+ "<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>\n"
				+ "<assertionsResultsToSave>0</assertionsResultsToSave>\n"
				+ "<bytes>true</bytes>\n"
				+ "<threadCounts>true</threadCounts>\n"
				+ "</value>\n"
				+ "</objProp>\n"
				+ "<stringProp name=\"filename\"></stringProp>\n"
				+ "</ResultCollector>\n"
				+ " <hashTree/>\n"
				+ "<ResultCollector guiclass=\"TableVisualizer\" testclass=\"ResultCollector\" testname=\"View Results in Table\" enabled=\"true\">\n"
				+ "<boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n"
				+ " <objProp>\n"
				+ "<value class=\"SampleSaveConfiguration\">\n"
				+ "<latency>true</latency>\n"
				+ "<timestamp>true</timestamp>\n"
				+ "<success>true</success>\n"
				+ "<label>true</label>\n"
				+ "<code>true</code>\n"
				+ "<message>true</message>\n"
				+ "<threadName>true</threadName>\n"
				+ "<dataType>true</dataType>\n"
				+ "<encoding>false</encoding>\n"
				+ "<assertions>true</assertions>\n"
				+ "<subresults>true</subresults>\n"
				+ "<responseData>false</responseData>\n"
				+ "<samplerData>false</samplerData>\n"
				+ " <xml>false</xml>\n"
				+ "<fieldNames>false</fieldNames>\n"
				+ "<responseHeaders>false</responseHeaders>\n"
				+ "<requestHeaders>false</requestHeaders>\n"
				+ "<responseDataOnError>false</responseDataOnError>\n"
				+ "<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>\n"
				+ "<assertionsResultsToSave>0</assertionsResultsToSave>\n"
				+ "<bytes>true</bytes>\n"
				+ "</value>\n"
				+ "</objProp>\n"
				+ "<objProp>\n"
				+ "<value class=\"SampleSaveConfiguration\">\n"
				+ "<time>true</time>\n"
				+ "<latency>true</latency>\n"
				+ "<timestamp>true</timestamp>\n"
				+ "<success>true</success>\n"
				+ "<label>true</label>\n"
				+ "<code>true</code>\n"
				+ "<message>true</message>\n"
				+ "<threadName>true</threadName>\n"
				+ " <dataType>true</dataType>\n"
				+ "<encoding>false</encoding>\n"
				+ "<assertions>true</assertions>\n"
				+ "<subresults>true</subresults>\n"
				+ " <responseData>false</responseData>\n"
				+ "<samplerData>false</samplerData>\n"
				+ "<xml>false</xml>\n"
				+ "<fieldNames>false</fieldNames>\n"
				+ "<responseHeaders>false</responseHeaders>\n"
				+ "<requestHeaders>false</requestHeaders>\n"
				+ "<responseDataOnError>false</responseDataOnError>\n"
				+ "<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>\n"
				+ "<assertionsResultsToSave>0</assertionsResultsToSave>\n"
				+ "<bytes>true</bytes>\n"
				+ "<threadCounts>true</threadCounts>\n"
				+ "</value>\n"
				+ "</objProp>\n"
				+ "<stringProp name=\"filename\"></stringProp>\n"
				+ "</ResultCollector>\n"
				+ " <hashTree/>\n";
		
		return archivo;
	}

	/*
	 * String peticionJDBC(@paso)
	 * 
	 * Genera el bloque de código para una petición JDBC
	 * con base en un paso.
	 * 
	 * 
	 * @paso: Objeto Paso  con la estructura "Verifica que exista al menos una ENT·X con base en la RN·Verificación de catálogos". 
	 * Este paso debe tener asociado el query que permite validar esta condición.
	 * 
	 * El código mostrado a continuación sirve para inicializar los objetos cuya estrategia es LAZY.
	 * 
	 * 		refParam = new ReferenciaParametroDAO().consultarReferenciaParametro(refParam.getId());

	 */
	public static String peticionJDBC(Paso paso) {

		String bloque = null;
		String id = calcularIdentificador(prefijoPeticionJDBC, paso);
		String queryString = null;
		ReferenciaParametro refParam = null;

		refParam = AnalizadorPasosBs.obtenerPrimerReferencia(paso, ReferenciaEnum.TipoReferencia.REGLANEGOCIO);
		refParam = new ReferenciaParametroDAO().consultarReferenciaParametro(refParam.getId());
		
		for(Query sentencia : refParam.getQueries()) {
			queryString = sentencia.getQuery();
			break;
		}
		
		String redaccionPaso = consultarRedaccion(paso);
		bloque = peticionJDBC(id, queryString, redaccionPaso);
		return bloque;
	}

	public static String iniciarControladorIf(Paso paso, String operador) {
		String bloque = null;
		String id = calcularIdentificador(prefijoControladorIf, paso);
		String idPeticionJDBC = calcularIdentificador(prefijoPeticionJDBC, paso);
		String redaccionPaso = consultarRedaccion(paso);
		
		bloque = iniciarControladorIf(id, idPeticionJDBC, redaccionPaso, operador);
		
		return bloque;
	}

	public static String peticionHTTP(Paso paso, boolean hijos) {

		String bloque;
		String id = null;
		ReferenciaParametro refParam;
		Accion accion;
		String url;
		ArrayList<String> nombresParametros;
		ArrayList<Entrada> entradas;

		String metodo;
		String redaccionPaso;
		
		id = calcularIdentificador(prefijoPeticionHTTP, paso);

		refParam = AnalizadorPasosBs.obtenerPrimerReferencia(paso, ReferenciaEnum.TipoReferencia.ACCION);
		accion = (Accion) refParam.getAccionDestino();
		url = accion.getUrlDestino();
		metodo = accion.getMetodo();
		redaccionPaso = consultarRedaccion(paso);
		
		entradas = new ArrayList<Entrada>();
		entradas.addAll(paso.getTrayectoria().getCasoUso().getEntradas());		
		nombresParametros = obtenerNombresParametros(entradas);
		
		bloque = peticionHTTP(id, url, nombresParametros, metodo, redaccionPaso, hijos);
		return bloque;
	}
	
	public static String peticionHTTP(Paso paso, Paso pasoRN, Entrada entrada, boolean hijos) {
		String bloque;
		String id = null;
		ReferenciaParametro refParam;
		Accion accion;
		String url;
		ArrayList<String> nombresParametros;
		ArrayList<Entrada> entradas;

		String metodo;
		String redaccionPaso;
		
		id = calcularIdentificador(prefijoPeticionHTTP, paso, pasoRN, entrada);

		refParam = AnalizadorPasosBs.obtenerPrimerReferencia(paso, ReferenciaEnum.TipoReferencia.ACCION);
		accion = (Accion) refParam.getAccionDestino();
		url = accion.getUrlDestino();
		metodo = accion.getMetodo();
		redaccionPaso = consultarRedaccion(paso);
		
		entradas = new ArrayList<Entrada>();
		entradas.addAll(paso.getTrayectoria().getCasoUso().getEntradas());		
		nombresParametros = obtenerNombresParametros(entradas);
		
		bloque = peticionHTTP(id, url, nombresParametros, metodo, redaccionPaso, hijos);
		return bloque;
	}
		
	public static String asercion(Paso paso) {
		
		String bloque;
		String id;
		ReferenciaParametro refParamMensaje;
		ReferenciaParametro refParamPantalla;
		ArrayList<String> patrones = new ArrayList<String>();
		String redaccionPaso;

		id = calcularIdentificador(prefijoAsercion, paso);

		refParamMensaje = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.MENSAJE);
		refParamPantalla = AnalizadorPasosBs.obtenerPrimerReferencia(paso, TipoReferencia.PANTALLA);

		if (refParamMensaje != null) {
			patrones.add(calcularPatron(refParamMensaje));

		}
		if (refParamPantalla != null) {
			patrones.add(calcularPatron(refParamPantalla));

		}	
		redaccionPaso = consultarRedaccion(paso);
		
		bloque = asercion(id, patrones, redaccionPaso);
		return bloque;
	}
	
	public static String calcularPatron(ReferenciaParametro refParam) {
		Mensaje mensaje;
		Pantalla pantalla;
		String patron = null;
		refParam = new ReferenciaParametroDAO().consultarReferenciaParametro(refParam.getId());

		switch(ReferenciaEnum.getTipoReferenciaParametro(refParam)) {
		case MENSAJE:
			mensaje = (Mensaje) refParam.getElementoDestino();
			if (!mensaje.getParametros().isEmpty()) {
				patron = mensaje.getRedaccion();
			} else {
				patron = mensaje.getRedaccion();
			}
			for (MensajeParametro mensajeParametro : mensaje.getParametros()) {
				for (ValorMensajeParametro valor : refParam.getValoresMensajeParametro()) {
					if (mensajeParametro.getId().equals(valor.getMensajeParametro().getId())) {
						System.err.println(patron + "<-Patron dentro del if - valor: " + valor.getValor());
						System.err.println("Cadena sustituta ->"+ TokenBs.tokenPARAM + mensajeParametro.getParametro().getId());
						patron = TokenBs.remplazoToken(patron, TokenBs.tokenPARAM
								+ mensajeParametro.getParametro().getNombre(), valor.getValor());
					}
				}
			}
			patron = patron.substring(1,patron.length());
			System.err.println(patron + "<-Patron mensaje");
			break;
		case PANTALLA:
			pantalla = (Pantalla) refParam.getElementoDestino();
			patron = pantalla.getPatron();
			break;
		default:
			break;
			
		}
		patron = org.apache.commons.lang.StringEscapeUtils.escapeHtml(patron);
		return patron;
	}
	

	public static String contenedorCSV(Paso paso, boolean terminar) throws Exception {
		String bloque = null;
		String idCSV;
		String idPeticionHTTP;
		String redaccionPaso;
		ArrayList<Entrada> entradas = new ArrayList<Entrada>();
		ArrayList<String> nombresParametros = null;
		ArrayList<String> valoresParametros = null;
		String nombreCSV;
		String ruta;
		
		idCSV = calcularIdentificador(prefijoContenedorCSV, paso);
		idPeticionHTTP = calcularIdentificador(prefijoPeticionHTTP, paso);
		entradas.addAll(paso.getTrayectoria().getCasoUso().getEntradas());
		
		nombresParametros = obtenerNombresParametros(entradas);
		valoresParametros = obtenerValoresParametros(entradas);
		redaccionPaso = consultarRedaccion(paso);
		nombreCSV = calcularNombreCSV(idCSV);
		ruta = generarRutaCSV(paso);
		
		bloque = contenedorCSV(idCSV, idPeticionHTTP, nombresParametros, redaccionPaso, terminar, nombreCSV);
		
		generarCSV(ruta, nombreCSV, valoresParametros);
		return bloque;
	}
	
	
	public static String contenedorCSV(Paso paso, Paso rn, Entrada entradaInvalida, boolean terminar) throws Exception {

		String bloque = null;
		String idCSV;
		String idPeticionHTTP;
		String redaccionPaso;
		ArrayList<Entrada> entradas = new ArrayList<Entrada>();
		ArrayList<String> nombresParametros = null;
		ArrayList<String> valoresParametros = null;
		String nombreCSV;
		String ruta;
		ReferenciaParametro referenciaParametro;
		
		idCSV = calcularIdentificador(prefijoContenedorCSV, paso, rn, entradaInvalida);
		idPeticionHTTP = calcularIdentificador(prefijoPeticionHTTP, paso, rn, entradaInvalida);
		entradas.addAll(paso.getTrayectoria().getCasoUso().getEntradas());
		referenciaParametro = AnalizadorPasosBs.obtenerPrimerReferencia(rn, ReferenciaEnum.TipoReferencia.REGLANEGOCIO);
		
		nombresParametros = obtenerNombresParametros(entradas);
		valoresParametros = obtenerValoresParametros(entradas, entradaInvalida, (ReglaNegocio) referenciaParametro.getElementoDestino());
		redaccionPaso = consultarRedaccion(paso);
		nombreCSV = calcularNombreCSV(idCSV);
		ruta = generarRutaCSV(paso);
		
		bloque = contenedorCSV(idCSV, idPeticionHTTP, nombresParametros, redaccionPaso, terminar, nombreCSV);
		generarCSV(ruta, nombreCSV, valoresParametros);
		return bloque;
	}
	
	
	private static ArrayList<String> obtenerValoresParametros(
			ArrayList<Entrada> entradas) {
		ArrayList<String> valores = new ArrayList<String>();
		for (Entrada entrada : entradas) {	
			//if(!trayectoriaActual.isAlternativa()){
				entrada = new EntradaDAO().findById(entrada.getId());
				for (ValorEntrada valorEntrada : entrada.getValores()) {
					if (valorEntrada.getValido()) {
						if(valorEntrada.getCorrecto_prueba() || valorEntrada.getAleatoriocorrecto_prueba()){
							String valor = valorEntrada.getValor();
							String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(valor);
							valores.add(valorEscCSV);
						}
					}
				}
			//}
				//else{ //ESTE ES PARA TRAYECTORIAS ALTERNATIVAS.
					/*entrada = new EntradaDAO().findById(entrada.getId());
					for (ValorEntradaTrayectoria valorEntrada : entrada.getValoresEntradaTrayectoria()) {
						//if (valorEntrada.getValido()) {
							String valor = valorEntrada.getValor();
							String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(valor);
							valores.add(valorEscCSV);
						//}
					}*/
				//}
					
		}
		return valores;
	}
	
	
	private static ArrayList<String> obtenerValoresParametros(
			ArrayList<Entrada> entradas, Entrada entradaInvalida, ReglaNegocio reglaNegocio) {
		ArrayList<String> valores = new ArrayList<String>();
		boolean buscarInvalida = false;
		for (Entrada entrada : entradas) {
			boolean buscarIncidencia = false;
			entrada = new EntradaDAO().findById(entrada.getId());
			if (entradaInvalida != null && entrada.getId() == entradaInvalida.getId()) {
				buscarInvalida = true;
			} else {
				buscarInvalida = false;
			}
			
			for (ValorEntrada valorEntrada : entrada.getValores()) {
				if (buscarInvalida) {
					if (!valorEntrada.getValido() && valorEntrada.getReglaNegocio().getId() == reglaNegocio.getId()) {
						String valor = valorEntrada.getValor();
						String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(valor);
						valores.add(valorEscCSV);
					}
				} else {
					if (valorEntrada.getValido()) {
						if(valorEntrada.getCorrecto_prueba() || valorEntrada.getAleatoriocorrecto_prueba()){//editado
							String valor = valorEntrada.getValor();
							String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(valor);
							valores.add(valorEscCSV);
						}
					} else if(new ValorEntradaDAO().consultarValorValido(entrada)==null){
						buscarIncidencia=true;
					}
				}
			}
			if(buscarIncidencia){
				//Vamos a buscar el valorentrada correcto, relacionado con la entrada que tenemos, para meterlo en el csv.
				for (ValorEntrada valorEntrada : entrada.getValores()) {
					System.out.println("//////////////////////////////////");
					System.err.println(valorEntrada.getValor()+" ID: "+valorEntrada.getId());
					if(valorEntrada.getValido()){
						if(valorEntrada.getCorrecto_prueba()){
							String valor = valorEntrada.getValor();
							String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(valor);
							valores.add(valorEscCSV); 
						}
					}
					
				}
				
				
				
				/*if(new ValorDesconocidoDAO().consultarDesconocido(entrada)){
					//Aquí aplicamos el algoritmo para el valor aleatorio.
						
					String ruta=new ValorDesconocidoDAO().obtenerRuta(entrada);
					File fichero_entrada = new File (ruta);
					if (!fichero_entrada.exists()) {
						System.out.println ("No existe el fichero de entrada especificado"); 
					}else {
			            Scanner scan1;
						try {
							scan1 = new Scanner (fichero_entrada);
							ArrayList<String> datosDeEntrada = new ArrayList<String>();
					        int contador = 0;
					        while (scan1.hasNext()){
					               String lineaExtraida = scan1.nextLine();
					               datosDeEntrada.add(lineaExtraida);
					               contador++;
					         }
						     int ran= (int) (Math.random()*contador+0);
						     System.out.println ("Dato en el txt: "+datosDeEntrada.get(ran)); //Este es el dato que obtenemos en el txt.
						     String valorEscCSV = org.apache.commons.lang.StringEscapeUtils.escapeCsv(datosDeEntrada.get(ran));
							 valores.add(valorEscCSV); //Se agrega el valor del txt al csv.
						} catch (FileNotFoundException e) {
							 // TODO Auto-generated catch block
							 e.printStackTrace();
						}
			         }
				}*/
			}
		}
		return valores;
	}
	
	
	public static ArrayList<String> obtenerNombresParametros(
			ArrayList<Entrada> entradas) {
		ArrayList<String> nombres = new ArrayList<String>();
		entradas = ordenarEntradas(entradas);
		for (Entrada entrada : entradas) {
			nombres.add(entrada.getNombreHTML());
		}
		return nombres;
	}

	
	private static String calcularIdentificador(String prefijo,
			Paso paso, Paso rn, Entrada entrada) {
		if (entrada == null) {
			return prefijo + paso.getTrayectoria().getCasoUso().getClave() + paso.getTrayectoria().getCasoUso().getNumero() + "-" + paso.getTrayectoria().getClave() + paso.getNumero() + rn.getTrayectoria().getClave() + rn.getNumero() + i;
		}
		if (entrada.getAtributo()!= null) {
			return prefijo + paso.getTrayectoria().getCasoUso().getClave() + paso.getTrayectoria().getCasoUso().getNumero() + "-" + paso.getTrayectoria().getClave() + paso.getNumero() + rn.getTrayectoria().getClave() + rn.getNumero() + i + entrada.getNombreHTML();
		} else if (entrada.getTerminoGlosario() != null) {
			return prefijo + paso.getTrayectoria().getCasoUso().getClave() + paso.getTrayectoria().getCasoUso().getNumero() +"-" + paso.getTrayectoria().getClave() + paso.getNumero() + rn.getTrayectoria().getClave() + rn.getNumero() + i + entrada.getNombreHTML();
		}
		return null;
	}
	
	
	public static String calcularIdentificador(String prefijo, Paso paso) {
		return prefijo + paso.getTrayectoria().getCasoUso().getClave() + paso.getTrayectoria().getCasoUso().getNumero() + "-" + paso.getTrayectoria().getClave() + paso.getNumero() + i;
	}
	
	
	public static String generarRutaCSV(Paso paso) {
		return casoUsoTesting + "/entradas/";
	}
	
	public static void generarCSV(String ruta, String nombreCSV,
			ArrayList<String> valoresParametros) throws IOException {
		String linea = "";
		
		for(String valor : valoresParametros) {
			linea = linea + valor + ",";
		}
		
		if (!linea.equals("")) {
			linea = linea.substring(0, linea.length() - 1);	
		} else {
			linea = ",";
		}
		crearArchivo(nombreCSV, ruta, linea);
	}

	
	public static String calcularNombreCSV(String id) {
		return  id + ".csv";
	}
	
	
	public static String consultarRedaccion(Paso paso) {
		//return TokenBs.decodificarCadenaSinToken(paso.getRedaccion());
		return paso.getRedaccion();
	}
	
	
	public static ArrayList<Entrada> ordenarEntradas(ArrayList<Entrada> entradas) {
		int longitud = entradas.size();
		Entrada entrada = null;
		for (int i = 0; i < longitud; i++) {
			for (int j = 0; j < longitud; j++) {
				if (entradas.get(i).getId() < entradas.get(j).getId()) {
					entrada = entradas.get(j);
					entradas.set(j, entradas.get(i));
					entradas.set(i, entrada);
				}
			}
		}
		return entradas;
		
	}

	
	public static String probarReglaNegocio(Paso pasoActual, Paso pasoRN) throws Exception {
		String archivo = "";
		ArrayList<Entrada> entradas = new ArrayList<Entrada>();
		ReferenciaParametro refParam;
		ReglaNegocio reglaNegocio;
		refParam = AnalizadorPasosBs.obtenerPrimerReferencia(pasoRN, TipoReferencia.REGLANEGOCIO);
		reglaNegocio = (ReglaNegocio) refParam.getElementoDestino();
		TipoReglaNegocioENUM tipoRN = TipoReglaNegocioEnum.getTipoReglaNegocio(reglaNegocio.getTipoReglaNegocio());
		System.out.println("PasoActual: "+pasoActual.getRedaccion()+", pasoRN: "+pasoRN.getRedaccion());
		if (tipoRN == TipoReglaNegocioENUM.UNICIDAD) {
			System.out.println("Entro a Regla negocio UNICIDAD");

			refParam = new ReferenciaParametroDAO().consultarReferenciaParametro(refParam.getId());
			archivo += GeneradorPruebasBs.peticionJDBC(pasoRN);
			archivo += GeneradorPruebasBs.iniciarControladorIf(
					pasoRN, ">");
			archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, null, true);
			archivo += GeneradorPruebasBs.contenedorCSV(pasoActual, pasoRN, null, false);
			archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs.calcularPasoAlternativo(pasoRN));
			System.out.println("Entra a terminarControladorIf de probarReglaNegocio");

			archivo += GeneradorPruebasBs.terminarControladorIf();			
			pasoIncierto = pasoRN;
			caminoIdealIncierto = true;

		} else {
			entradas.addAll(pasoActual.getTrayectoria().getCasoUso().getEntradas());	
			if (tipoRN == TipoReglaNegocioENUM.DATOCORRECTO) {
				System.out.println("Entro a Regla negocio DATOCORRECTO");

				for (Entrada entrada : entradas) {
					if (entrada.getAtributo() != null && !entrada.getAtributo().getTipoDato().getNombre().equals("Cadena") && !entrada.getAtributo().getTipoDato().getNombre().equals("Archivo") && !entrada.getAtributo().getTipoDato().getNombre().equals("Otro")) { 
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, entrada, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,	pasoRN, entrada, false);
						archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs
						.calcularPasoAlternativo(pasoRN));
					}
				}
			} 
			if (tipoRN == TipoReglaNegocioENUM.LONGITUD) {
				System.out.println("Entro a Regla negocio LONGITUD");

				for (Entrada entrada : entradas) {
					if (entrada.getAtributo() != null && (entrada.getAtributo().getTipoDato().getNombre().equals("Cadena") || entrada.getAtributo().getTipoDato().getNombre().equals("Entero") || entrada.getAtributo().getTipoDato().getNombre().equals("Flotante"))) { 
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, entrada, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,	pasoRN, entrada, false);
						archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs
						.calcularPasoAlternativo(pasoRN));
					}
				}
			}			
			if (tipoRN == TipoReglaNegocioENUM.OBLIGATORIOS) {
				System.out.println("Entro a Regla negocio OBLIGATORIOS");

				for (Entrada entrada : entradas) {
					if (entrada.getAtributo() != null && entrada.getAtributo().isObligatorio()) { 
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, entrada, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,	pasoRN, entrada, false);
						archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs
						.calcularPasoAlternativo(pasoRN));
					}
				}
			}
			if (tipoRN == TipoReglaNegocioENUM.FORMATOCAMPO) {
				System.out.println("Entro a Regla negocio FORMATOCAMPO");

				for (Entrada entrada : entradas) {
					System.out.println("reglaNegocio.getAtributoExpReg().getId(): "+reglaNegocio.getAtributoExpReg().getId());
					System.out.println("entrada.getAtributo().getId(): "+entrada.getAtributo().getId());
					if(reglaNegocio.getAtributoExpReg().getId() == entrada.getAtributo().getId()){
						System.out.println("ENTRA");
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, entrada, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,	pasoRN, entrada, false);
						archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs
						.calcularPasoAlternativo(pasoRN));
					}
				}
				System.out.println("sale for");
			}
			if (tipoRN == TipoReglaNegocioENUM.COMPATRIBUTOS) {
				System.out.println("Entro a Regla negocio COMPATRIBUTOS");

				for (Entrada entrada : entradas) {
					if (entrada.getAtributo() != null && (entrada.getAtributo().getTipoDato().getNombre().equals("Cadena") || entrada.getAtributo().getTipoDato().getNombre().equals("Fecha"))) { 
						//Que pasaría aquí?
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoRN, entrada, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,	pasoRN, entrada, false);
						archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs
						.calcularPasoAlternativo(pasoRN));
					}
				}
			}
		}
				System.out.println("archivo :"+archivo);		
		return archivo;
	}	
	
	public static void crearArchivo(String nombre, String ruta,
			String contenido) throws IOException {
		//System.out.println("Entro a crearArchivo con el archivo en tamaño "+ contenido );
		File file = new File(ruta + nombre);
		file.getParentFile().mkdirs();
		FileWriter writer = new FileWriter(file);
		
		/*if(nombre.contains(".jmx")) {
			contenido = formatoXML(contenido);
		}*/
		
		writer.append(contenido);
		writer.close();
	}
	
	/*public static String formatoXML(String cadena) throws IOException {
		Document document = FileUtil.parseXmlFile(cadena);

        OutputFormat format = new OutputFormat(document);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(document);
        
        return out.toString();
	}*/
	
	public static void generarCasosPrueba(CasoUso casoUso, String rutaPruebas) throws Exception {
		String archivo = "";
		ConfiguracionHttp confHTTP;
		ConfiguracionBaseDatos confJDBC;
		ArrayList<Paso> pasos = new ArrayList<Paso>();
		casoUsoTesting = rutaPruebas + casoUso.getId() + "";
		archivo += GeneradorPruebasBs.encabezado();
		archivo += GeneradorPruebasBs.planPruebas();
		archivo += GeneradorPruebasBs.grupoHilos(casoUso.getClave() + casoUso.getNumero());
		archivo += GeneradorPruebasBs.cookieManager();
		confHTTP = new ConfiguracionDAO().consultarConfiguracionHttpByCasoUso(casoUso);
		System.out.println(confHTTP.getIp()+", "+confHTTP.getPuerto());
		archivo += GeneradorPruebasBs.configuracionHTTP(confHTTP.getIp(), confHTTP.getPuerto().toString());
		confJDBC = new ConfiguracionDAO().consultarConfiguracionBaseDatosByCasoUso(casoUso);
		System.out.println(confJDBC.getUrlBaseDatos()+", "+confJDBC.getDriver()+", "+confJDBC.getUsuario()+", "+confJDBC.getContrasenia());
		archivo += GeneradorPruebasBs.configuracionJDBC(confJDBC.getUrlBaseDatos(), confJDBC.getDriver(), confJDBC.getUsuario(), confJDBC.getContrasenia());
		archivo += GeneradorPruebasBs.estadisticas();
		archivo += prepararPrueba(casoUso);
		Set<Trayectoria> trayectorias=  casoUso.getTrayectorias();
		List<Trayectoria> trayectorias2 = new ArrayList<Trayectoria>();
		int tamano = trayectorias.size();
		/*for(Trayectoria trayectoria : trayectorias){
			trayectorias2.add(trayectoria);
		}*/
		/*for (i=0; i<tamano ; i++){ //Genera todas las r
			trayectoriaActual = trayectorias2.get(i);
			System.out.println("La trayectoria actual es: " + trayectoriaActual.getClave() + "  "  +trayectoriaActual.getId());
			*/for (Trayectoria trayectoria : casoUso.getTrayectorias()) {
				System.out.println(1);
				if (!trayectoria.isAlternativa()) {
					System.out.println(2);
					List<Paso>pasosaux = new ArrayList<Paso>();
					pasosaux = TrayectoriaBs.obtenerPasos(trayectoria.getId());
					pasos.addAll(pasosaux);				//pasos = AnalizadorPasosBs.ordenarPasos(trayectoria);
					System.out.println(3);
					for (Paso paso : pasosaux) {
						System.out.println(4);
						if (paso.getNumero() == 1) {
							System.out.println(5);
							archivo += generarPrueba(paso, pasos);
							System.out.println(6);
						}
					}
				}
				System.out.println("EL TAMAÑO DEL ARCHIVO ES DESPUES DE CADA TRAYECTORIA " + archivo.length());
			}
		//}
		archivo += GeneradorPruebasBs.cerrar(casoUso);
		/*byte ptext[] = archivo.getBytes(ISO_8859_1); 
		String archivo_utf8 = new String(ptext, UTF_8); */
		
				
		crearArchivo(casoUso.getClave() + casoUso.getNumero() + ".jmx", casoUsoTesting + "/", reemplazarUTF8(archivo));
	}
	public static String reemplazarUTF8(String archivo){
		archivo.replaceAll("&aacute;","á");
		archivo = archivo.replaceAll("&eacute;","é");
		archivo = archivo.replaceAll("&iacute;","í");
		archivo = archivo.replaceAll("&oacute;","ó");
		archivo = archivo.replaceAll("&uacute;","ú");
		archivo = archivo.replaceAll("&Aacute;","Á");
		archivo = archivo.replaceAll("&Eacute;","É");
		archivo = archivo.replaceAll("&Iacute;","Í");
		archivo = archivo.replaceAll("&Oacute;","Ó");
		archivo = archivo.replaceAll("&Uacute;","Ú");
		archivo = archivo.replaceAll("&ntilde;","ñ");
		archivo = archivo.replaceAll("&Ntilde;","Ñ");
		return archivo;
	}
	public static String generarCasosPruebaRuta(CasoUso casoUso, String rutaPruebas) throws Exception {
		String archivo = "";
		ConfiguracionHttp confHTTP;
		ConfiguracionBaseDatos confJDBC;
		ArrayList<Paso> pasos = new ArrayList<Paso>();
		casoUsoTesting = rutaPruebas + casoUso.getId() + "";
		archivo += GeneradorPruebasBs.encabezado();
		archivo += GeneradorPruebasBs.planPruebas();
		archivo += GeneradorPruebasBs.grupoHilos(casoUso.getClave() + casoUso.getNumero());
		archivo += GeneradorPruebasBs.cookieManager();
		confHTTP = new ConfiguracionDAO().consultarConfiguracionHttpByCasoUso(casoUso);
		System.out.println(confHTTP.getIp()+", "+confHTTP.getPuerto());
		archivo += GeneradorPruebasBs.configuracionHTTP(confHTTP.getIp(), confHTTP.getPuerto().toString());
		confJDBC = new ConfiguracionDAO().consultarConfiguracionBaseDatosByCasoUso(casoUso);
		System.out.println(confJDBC.getUrlBaseDatos()+", "+confJDBC.getDriver()+", "+confJDBC.getUsuario()+", "+confJDBC.getContrasenia());
		archivo += GeneradorPruebasBs.configuracionJDBC(confJDBC.getUrlBaseDatos(), confJDBC.getDriver(), confJDBC.getUsuario(), confJDBC.getContrasenia());
		archivo += GeneradorPruebasBs.estadisticas();
		archivo += prepararPrueba(casoUso);

		for (Trayectoria trayectoria : casoUso.getTrayectorias()) {
			if (!trayectoria.isAlternativa()) {
				List<Paso>pasosaux = new ArrayList<Paso>();
				pasosaux = TrayectoriaBs.obtenerPasos(trayectoria.getId());
				pasos.addAll(pasosaux);
				//pasos = AnalizadorPasosBs.ordenarPasos(trayectoria);
				for (Paso paso : pasosaux) {
					if (paso.getNumero() == 1) {
						archivo += generarPrueba(paso, pasos);  //NUNCA se usa (?)
					}
				}
			}
		}
		
		archivo += GeneradorPruebasBs.cerrar(casoUso);
		
		crearArchivo(casoUso.getClave() + casoUso.getNumero() + ".jmx", casoUsoTesting + "/", archivo);
		
		return casoUsoTesting + "/" + casoUso.getClave() + casoUso.getNumero() + ".jmx";
	}


	public static String prepararPrueba(CasoUso casoUso) throws Exception {
		String archivo = "";
		TipoPaso tipo;
		ArrayList<Paso> pasos = new ArrayList<Paso>();
		List<CasoUso> casosUso = new ArrayList<CasoUso>();
		casosUso = CuBs.obtenerCaminoPrevioMasCorto(casoUso);
		
		for (CasoUso casoUsoi : casosUso) {
			for (Trayectoria trayectoria : casoUsoi.getTrayectorias()) {
				if (!trayectoria.isAlternativa()) {
					List<Paso>pasosaux = new ArrayList<Paso>();
					pasosaux = TrayectoriaBs.obtenerPasos(trayectoria.getId());
					pasos.addAll(pasosaux);
					for (Paso paso : AnalizadorPasosBs.ordenarPasos(trayectoria)) {
						tipo = AnalizadorPasosBs.calcularTipo(paso);
						if (tipo != null) {
							switch (tipo) {
							case actorOprimeBoton:
								if (paso.getNumero() == 1) {
									archivo += GeneradorPruebasBs.peticionHTTP(paso, false);
								} else {
									archivo += GeneradorPruebasBs.peticionHTTP(paso, true);
									archivo += GeneradorPruebasBs.contenedorCSV(paso, true);
								}
								break;
							case actorSoliciaSeleccionarRegistro:
								archivo += GeneradorPruebasBs.peticionHTTP(paso, true);
								archivo += GeneradorPruebasBs.contenedorCSV(paso, true);
								break;
							default:
								break;
						
							}
						}
					}
				}
			}
		}
		return archivo;
	}

	
	public static String generarPrueba(Paso pasoActual, ArrayList<Paso> pasos) throws Exception {
		String archivo = "";
		TipoPaso tipo;
		Paso siguiente;
		if (pasoActual == null) {
			return archivo;
		}
		siguiente = AnalizadorPasosBs.calcularSiguiente(pasoActual, pasos);
		tipo = AnalizadorPasosBs.calcularTipo(pasoActual);
		if (tipo == null) {
			pasos.remove(pasoActual);
			archivo += generarPrueba(siguiente, pasos);
			return archivo;
		}
		System.out.println("El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId() +  " " + pasoActual.getRedaccion());
		switch (tipo) {
		case actorOprimeBoton:
			if (pasoActual.getNumero() == 1) {
				System.out.println("Entro a case ActorOprimeBoton" + " El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());
				if (siguiente != null
						&& AnalizadorPasosBs.calcularTipo(siguiente) == AnalizadorPasosBs.TipoPaso.sistemaValidaPrecondicion) {
					archivo += GeneradorPruebasBs.peticionJDBC(siguiente);
					archivo += GeneradorPruebasBs.iniciarControladorIf(
							siguiente, ">");
					pasos.remove(siguiente);
					archivo += generarPrueba(pasoActual, pasos);
					System.out.println("Entra a terminarControlador de linea 1011");
					archivo += GeneradorPruebasBs.terminarControladorIf();

					archivo += GeneradorPruebasBs.iniciarControladorIf(
							siguiente, "==");
					archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, true);
					archivo += GeneradorPruebasBs.asercion(AnalizadorPasosBs.calcularPasoAlternativo(siguiente));
					System.out.println("Entra a terminarControlador de linea 1019");
					archivo += GeneradorPruebasBs.terminarControladorIf();
				} else {
					archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, true);
					pasos.remove(pasoActual);
					archivo += generarPrueba(siguiente, pasos);
				}
			} else if (pasoActual.getNumero() > 1) {
				System.out.println("Entro a case ActorOprimeBoton elseif " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());

				if (siguiente != null
						&& AnalizadorPasosBs.calcularTipo(siguiente) == AnalizadorPasosBs.TipoPaso.sistemaValidaReglaNegocio) {
					
					System.out.println("Entro a case ActorOprimeBoton elseif/siguiente no es nulo " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());

					archivo += probarReglaNegocio(pasoActual, siguiente);
					
					pasos.remove(siguiente);
					archivo += generarPrueba(pasoActual, pasos);

				} else {
					System.out.println("Entro a case ActorOprimeBoton elseif/siguiente SI es nulo " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());

					if (!caminoIdealIncierto) { //aquí nunca entra
						archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, true);
						archivo += GeneradorPruebasBs.contenedorCSV(pasoActual,
							false);
					} else {
						if(pasoIncierto != null) {
							System.out.println("Entra a iniciarControlador de linea 1042");
							archivo += GeneradorPruebasBs.iniciarControladorIf(
									pasoIncierto, "==");
							archivo += GeneradorPruebasBs.peticionHTTP(pasoActual, pasoIncierto, null, true);
							archivo += GeneradorPruebasBs.contenedorCSV(pasoActual, pasoIncierto, null, false);
							//archivo += GeneradorPruebasBs.terminarControladorIf();
						}
						System.out.println("El paso es: " + pasoActual.getNumero() + pasoActual.getId() + pasoActual.getRedaccion());
						pasos.remove(pasoActual);
						archivo += generarPrueba(siguiente, pasos);
						System.out.println("Entra a terminarControlador de linea 1050");
						archivo += GeneradorPruebasBs.terminarControladorIf();	
					}
					
				}
			}
			break;
		case sistemaMuestraMensaje:
			System.out.println("Entro a case sistemMuestraMensaje " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());

			archivo += GeneradorPruebasBs.asercion(pasoActual);
			pasos.remove(pasoActual);
			archivo += generarPrueba(siguiente, pasos);

			break;
		case sistemaMuestraPantalla:
			System.out.println("Entro a case sistemaMuestraPantalla " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());

			archivo += GeneradorPruebasBs.asercion(pasoActual);
			pasos.remove(pasoActual);
			archivo += generarPrueba(siguiente, pasos);

			break;
		default:
			System.out.println("Entro a case default " + "El paso es: " + pasoActual.getNumero() + " "  + pasoActual.getId());
			pasos.remove(pasoActual);
			archivo += generarPrueba(siguiente, pasos);
		}
		return archivo;
	}
	
	
	


	
}

