package mx.prisma.generadorPruebas.bs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mx.prisma.editor.model.CasoUso;
import mx.prisma.generadorPruebas.dao.ErroresPruebaDAO;
import mx.prisma.generadorPruebas.dao.ValorEntradaDAO;
import mx.prisma.generadorPruebas.model.ErroresPrueba;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EjecutarPruebaBs {

	public static void ejecutarPruebaAutomatica(String rutaReporte,CasoUso casoUso,String rutaFolder) {
		try {
			
            String comando = "./jmeter -n -t "+rutaFolder+casoUso.getClave() + casoUso.getNumero()+".jmx -l Test.jtl -e -o "+rutaReporte;
            String[] cmd = { "/bin/sh", "-c", "cd /Users/gerasolis/Downloads/apache-jmeter-3.0/bin; "+comando};
            Process p = Runtime.getRuntime().exec(cmd);
       
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                System.out.println(line);  
            } 
    		FileInputStream inn = new FileInputStream(rutaReporte+"content/js/dashboard.js");
    		BufferedReader in2 = new BufferedReader(new InputStreamReader(inn));  
    		line="";
    		while ((line = in2.readLine()) != null) {  
                if(line.indexOf("#errorsTable") != -1){
                	System.out.println(line); 
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
                		
                		e.setTipoError(as.getString(0));
                		e.setNumError(as.getInt(1));
                		e.setPorcentaje(as.getDouble(2));
                		e.setPorcentajeTodo(as.getDouble(3));
                		e.setCasoUsoid(casoUso);
                		if(i==0){
	                		if(new ErroresPruebaDAO().obtenerErrores(e)!=null){
	                			new ErroresPruebaDAO().eliminarErrores(e);
	                		}
                		}
                		new ErroresPruebaDAO().registrarError(e);
                		new ErroresPruebaDAO().modificarReporte(e);
                		/*List<String> list = new ArrayList<String>();
                		for (int n=0; n<as.size(); n++) {
                		    list.add( as.getString(n));
                		}
                		for(String x : list){
                			System.out.println(x);
                			
                		}*/	
                		//Una vez registrado en la bd, borramos los archivos temporales.
                	}
                }
    		}
		} catch (Exception ioe) {
			System.out.println (ioe);
		}		
	}
}
