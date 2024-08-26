package phantom.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Manipulador {

	public static void writeProp() throws IOException {
        Properties props = new Properties();
        
        try (
            
            FileOutputStream out = new FileOutputStream("./dados.properties");
            
        ) {
            
            props.setProperty("lang", "pt");
            
            props.store(out, "");

        }
 

	}
    
	public static void readProp() throws IOException {
        Properties props = new Properties();
        
        try (
            
            FileInputStream in = new FileInputStream("./dados.properties");
            
        ) {
            
            props.load(in);
            
            String lang = props.getProperty("lang");
            
            System.out.println(lang);
        }
 
    }

	public static void  main(String args[]) throws IOException {
		
		readProp();
        
        

	}
}
