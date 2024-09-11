package phantom.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 */
public final class Main {
    
    static {
        
        Properties props = new Properties();
        
        String configDir = CONFIG_DIR;
        
        String configPathname = CONFIG_PATHNAME;
        
  
        try ( FileInputStream in = new FileInputStream(configPathname) ) { 
            
            props.load(in); 
        }
        catch (IOException e) {            
            
            props.setProperty("lang", "pt");  

            props.setProperty("country", "BR"); 
            
            try {
                
                toolbox.file.FileTools.createDirsIfNotExists(configDir);
                
            } catch (FileNotFoundException e1) {
                
                System.err.println("Can't create " + configDir);
                
                phantom.exception.ExceptionTools.crash(e1);
                
            }

            try (FileOutputStream out = new FileOutputStream(configPathname)) {               
                
                props.store(out, "Config");
                
                toolbox.locale.Localization.setLocale(new Locale("pt", "BR"));
            }
            catch (IOException e2) {
                
                System.err.println("Can't save config data in " + configPathname);  
                
                phantom.exception.ExceptionTools.crash(e2);

            }              
              
        }
  
        String lang = props.getProperty("lang");
        
        String country = props.getProperty("country");      
        
        toolbox.locale.Localization.setLocale(new Locale(lang, country));
        
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
        
        try {
            
            /*
            Cria diretorios de trabalho do programa, se ainda nao criados, e o arquivo de 
            log para esta execucao do programa. O diretorio de configuracao, se for a 1a
            execucao do programa, foi criado e inicializado no bloco estatico desta classe.
            */
            Initializer.init();

            phantom.gui.MainFrame mainFrame = new phantom.gui.MainFrame();
            
            mainFrame.setVisible(true);
        
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }
    }
    
}//classe Main
