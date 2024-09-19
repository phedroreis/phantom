package phantom.main;

import java.io.IOException;
import java.util.Locale;
import static phantom.global.GlobalConstants.*;
import phantom.gui.GUInterface;

/**
 *
 * @author Pedro Reis
 */
public final class Main {
    
    static {
        
        toolbox.config.Property property = new toolbox.config.Property(CONFIG_PATHNAME);       
  
        try {//Le o arquivo de configuracao 
            
            property.load(); 
        }
        catch (IOException e) {}
  
        String lang = property.getProperty("lang", "pt");
        
        String country = property.getProperty("country", "BR");      
        
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
            
            GUInterface.showMainFrame();        
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }
    }
    
}//classe Main
