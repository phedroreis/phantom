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
        
        String configDir = CONFIG_DIR.get();
        
        String configPathname = CONFIG_PATHNAME.get();
        
  
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
    public static void main(String[] args) {
        
        try {
            
            /*
            Cria diretorios de trabalho do programa, se ainda nao criados, e o arquivo de 
            log para esta execucao do programa. O diretorio de configuracao, se for a 1a
            execucao do programa, foi criado e inicializado no bloco estatico desta classe.
            */
            Initializer.init();

            /*
            Cria objeto para baixar as paginas do forum, inicializado com data-hora da 
            ultima postagem no ultimo backup. Se este for o 1o backup, a data-hora eh
            lida como "ano 0"
            */
            phantom.pages.Downloader downloader = 
                new phantom.pages.Downloader(
                    Initializer.readDateTimeOfLastPostFromLastBackup()
                );

            /*
            Baixa, incrementalmente, paginas do forum (Main, Headers, Sections, Topics)
            */
            downloader.downloadAllPages();
            
            /*
            Salva a data-hora da ultima postagem do forum neste backup.
            */
            Finalizer.saveDateTimeOfLastPostFromThisBackup(
                downloader.getDateTimeOfLastPostFromThisBackup()
            );

            toolbox.log.Log.closeFile();//Fecha o arquivo de log.
        
        }
        catch (IOException | XMLParseException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }
    }
    
}//classe Main
