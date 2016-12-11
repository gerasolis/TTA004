package mx.prisma.generadorPruebas.bs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import mx.prisma.editor.model.CasoUso;

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
                	//Ahora convertirlo a json y trabajarlo como tal.
                	JSONObject json = (JSONObject) new JSONParser().parse(lineaErrores);
                }
            }
		} catch (Exception ioe) {
			System.out.println (ioe);
		}
		
		
		/*Luego se entra al html y se saca la tabla con su info. Eso se guarda en la bd, y ya después se
		  borran los archivos.*/
		
	}
}
