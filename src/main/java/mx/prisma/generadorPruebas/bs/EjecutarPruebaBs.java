package mx.prisma.generadorPruebas.bs;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.prisma.editor.model.CasoUso;
import mx.prisma.editor.model.Entrada;
import mx.prisma.editor.model.Mensaje;
import mx.prisma.editor.model.Modulo;
import mx.prisma.editor.model.Pantalla;
import mx.prisma.generadorPruebas.dao.ErroresPruebaDAO;
import mx.prisma.generadorPruebas.dao.PruebaDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import mx.prisma.generadorPruebas.model.Prueba;
import mx.prisma.generadorPruebas.model.ValorMensajeParametro;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EjecutarPruebaBs {

	public static void ejecutarPruebaAutomatica(String rutaReporte,CasoUso casoUso,String rutaFolder) {
		try {
			int p_=0;
            String comando = "./jmeter -n -t "+rutaFolder+casoUso.getClave() + casoUso.getNumero()+".jmx -l Test.jtl -e -o "+rutaReporte;
            String[] cmd = { "/bin/sh", "-c", "cd /Users/gerasolis/Downloads/apache-jmeter-3.0/bin; "+comando};
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println("CASO DE USO A PROBAR: "+casoUso.getNombre());
    		System.out.println("*******************************");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                System.out.println(line);  
            } 
            System.out.println("*******************************");
            Prueba px = new Prueba();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		px.setFecha(dateFormat.format(date));
    		px.setCasoUsoid(casoUso);
    		px.setEstado(0);
    		new PruebaDAO().registrarPrueba(px);
    		new PruebaDAO().modificarReporte(px);
    		System.out.println(rutaReporte);
    		FileInputStream inn = new FileInputStream(rutaReporte+"content/js/dashboard.js");
    		BufferedReader in2 = new BufferedReader(new InputStreamReader(inn));  
    		line="";
    		while ((line = in2.readLine()) != null) {  
                if(line.indexOf("#errorsTable") != -1){
                	System.out.println(line); 
                	try{
	                	String lineaErrores = line.substring(line.indexOf("{"), line.indexOf("}]}"))+"}]}";
	                	System.out.println(lineaErrores);
	                	Object lineaErorresObj = lineaErrores;
	                	
						//Ahora convertirlo a json y trabajarlo como tal.
	                	JSONObject jsonObj = JSONObject.fromObject(lineaErorresObj);
	                	JSONArray results = jsonObj.getJSONArray("items");
	                	
	                	for(int i=0;i<results.size();i++){
	                		System.out.println(results.getJSONObject(i).getJSONArray("data"));
	                		JSONArray as = results.getJSONObject(i).getJSONArray("data");
	                		
	                		//Ahora sólo falta insertar las cadenas en la bd.
	                		//UNA TABLA QUE ESTÉ RELACIONADA CON LA TABLA DE CASO DE USO.
	                		
	                		ErroresPrueba e = new ErroresPrueba();
							byte ptext[] = as.getString(0).getBytes(ISO_8859_1); 
							String error = new String(ptext, UTF_8); 
	                		e.setTipoError(error);
	                		//e.setTipoError(as.getString(0));
							e.setNumError(as.getInt(1));
	                		e.setPorcentaje(as.getDouble(2));
	                		e.setPorcentajeTodo(as.getDouble(3));
	                		e.setPruebaid(px);
	                		if (as.getInt(1) != 0){
	                			p_ = 1;
	                		}
                		    new ErroresPruebaDAO().registrarError(e);

                		    //Una vez registrado en la bd, borramos los archivos temporales.
                		    String comando2 = "rm /Users/gerasolis/Downloads/apache-jmeter-3.0/bin/Test.jtl";
                            Process p2 = Runtime.getRuntime().exec(comando2);
	                	}
                	}catch (Exception e){
                		ErroresPrueba x = new ErroresPrueba();
                		x.setTipoError("No hay errores.");
                		x.setPruebaid(px);
                		new ErroresPruebaDAO().registrarError(x);
                		String comando2 = "rm /Users/gerasolis/Downloads/apache-jmeter-3.0/bin/Test.jtl";
                        Process p2 = Runtime.getRuntime().exec(comando2);
                	}
                }
    		}
    		if(p_==1){
    			px.setEstado(1);
    			new PruebaDAO().modificarPrueba(px);
    		}
		} catch (Exception ioe) {
			System.out.println (ioe);
		}		
	}
	
	public static List<ErroresPrueba> consultarErroresxCasoUso(CasoUso casoUso){
		List<ErroresPrueba> listErrores=null;
		listErrores = new ErroresPruebaDAO().consultarErroresxCasoUso(casoUso);
		return listErrores;
	}
	public static List<ErroresPrueba> consultarErrores(){
		List<ErroresPrueba> listErrores=null;
		listErrores = new ErroresPruebaDAO().consultarErrores();
		return listErrores;
	}
	public static List<Pantalla> consultarPantallas(){
		List<Pantalla> listPantallas=null;
		listPantallas = new ErroresPruebaDAO().consultarPantallas();
		return listPantallas;
	}
	public static List<Mensaje> consultarMensajes(){
		List<Mensaje> listMensajes=null;
		listMensajes = new ErroresPruebaDAO().consultarMensajes();
		return listMensajes;
	}

	public static List<ValorMensajeParametro> consultarValorMensajeParametros(){
		List<ValorMensajeParametro> listValorMensajeParametro=null;
		listValorMensajeParametro = new ErroresPruebaDAO().consultarValorMensajeParametros();
		return listValorMensajeParametro;
	}
	public static void modificarError(ErroresPrueba e){
		new ErroresPruebaDAO().modificarError(e);
	}
	/*public static List<ErroresPrueba> consultarErroresCasosUso(Modulo modulo){
		List<ErroresPrueba> listErrores=null;
		listErrores = new ErroresPruebaDAO().consultarErroresCasosUso(modulo);
		return listErrores;
	}*/
	
	public static List<CasoUso> consultarCasosUso(Modulo modulo){
		List<CasoUso> listCasosUso=null;
		listCasosUso = new ErroresPruebaDAO().consultarCasosUso(modulo);
		return listCasosUso;
	}
	
	public static List<Entrada> consultarEntradas(){
		List<Entrada> listEntradas=null;
		listEntradas = new ErroresPruebaDAO().consultarEntradas();
		return listEntradas;
	}
}
