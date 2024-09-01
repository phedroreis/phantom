package phantom.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalStrings.*;


/**
 *
 * @author Pedro Reis
 */
public final class Main {
    
    static {
        
        Properties props = new Properties();
        
        String configPathname = CONFIG_PATHNAME.get();
        
        try ( FileInputStream in = new FileInputStream(configPathname) ) { 
            
            props.load(in); 
        }
        catch (IOException e) {            
            
            props.setProperty("lang", "pt");  

            props.setProperty("country", "BR"); 
            
            try {
                
                toolbox.file.FileTools.createDirsIfNotExists(CONFIG_DIR.get());
                
            } catch (FileNotFoundException ex) {
                
                System.err.println("Can't create " + CONFIG_DIR.get());
                ex.printStackTrace(System.err);
                System.exit(1);
                
            }

            try (FileOutputStream out = new FileOutputStream(configPathname)) {               
                
                props.store(out, "Config");
                
                toolbox.locale.Localization.setLocale(new Locale("pt", "BR"));
            }
            catch (IOException e2) {
                
                e2.printStackTrace(System.err);

                System.exit(1);

            }              
              
        }
  
        String lang = props.getProperty("lang");
        
        String country = props.getProperty("country");      
        
        toolbox.locale.Localization.setLocale(new Locale(lang, country));
        
    }

    /**
     * @param args the command line arguments
     * @throws XMLParseException
     * @throws IOException
     */
    public static void main(String[] args) throws XMLParseException, IOException{
   
        toolbox.log.Log.exec("phantom.main", "Main", "main");
        
        Initializer.init();
        
        phantom.pages.Downloader downloader = 
            new phantom.pages.Downloader(Initializer.readDateTimeOfLastPostFromLastBackup());
        
        downloader.downloadAllPages();
        
        Finalizer.saveDateTimeOfLastPostFromThisBackup(downloader.getDateTimeOfLastPostFromThisBackup());
        
        toolbox.log.Log.ret("phantom.main", "Main", "main");        
        toolbox.log.Log.closeFile();
    }
    
}//classe Main
