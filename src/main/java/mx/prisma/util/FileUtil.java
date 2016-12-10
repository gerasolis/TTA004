package mx.prisma.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FileUtil {
	private static List<String> fileList;
    /**
     * Zip it
     * @param source folder
     * @param zipFile output ZIP file location
     */
    public static void zipIt(String source, String zipFile){
    	fileList = new ArrayList<String>();
    	generateFileList(source, new File(source));
    	
     byte[] buffer = new byte[1024];
    	
     try{
    		
    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);
    		
    	for(String file : fileList){
    			
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);
               
        	FileInputStream in = 
                       new FileInputStream(source + File.separator + file);
       	   
        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}
               
        	in.close();
    	}
    		
    	zos.closeEntry();
    	//remember close it
    	zos.close();
          
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   }
    
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param source folder
     * @param node file or directory
     */
    public static void generateFileList(String source, File node){

    	//add file only
	if(node.isFile()){
		fileList.add(generateZipEntry(source, node.getAbsoluteFile().toString()));
	}
		
	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(source, new File(node, filename));
		}
	}
 
    }

    /**
     * Format the file path for zip
     * @param source file path
     * @param file file path
     * @return Formatted file path
     */
    private static String generateZipEntry(String source, String file){
    	return file.substring(source.length(), file.length());
    }
    
    
    /**
     * Delete the file or directory
     * @param file file to delete
     */
    public static void delete(File file)
        	throws IOException{
     
        	if(file.isDirectory()){
     
        		//directory is empty, then delete it
        		if(file.list().length==0){
        			
        		   file.delete();
        			
        		}else{
        			
        		   //list all the directory contents
            	   String files[] = file.list();
         
            	   for (String temp : files) {
            	      //construct the file structure
            	      File fileDelete = new File(file, temp);
            		 
            	      //recursive delete
            	     delete(fileDelete);
            	   }
            		
            	   //check the directory again, if empty then delete it
            	   if(file.list().length==0){
               	     file.delete();
            	   }
        		}
        		
        	}else{
        		//if file, then delete it
        		file.delete();
        	}
        }
    
    public static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
