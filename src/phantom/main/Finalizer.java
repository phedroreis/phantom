package phantom.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import static phantom.global.GlobalConstants.*;

/**
 * Classe com metodos que devem ser executados apos o termino do backup incremental.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
final class Finalizer {
 
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Finalizer", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Unable to save backup's date-time 
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Unable to save backup's date-time";         
        }
        
    }//bloco static    

    /**
     * 
     * @param datetime
     * @throws IOException 
     */
    public static void saveDateTimeOfLastPostFromThisBackup(final String datetime)         
        throws IOException {
        
        Properties props = new Properties();
       
        props.setProperty("last", datetime);         
       
        try (FileOutputStream out = new FileOutputStream(UPDATE_PATHNAME)) {
            
            props.store(out, "Last Successfull Backup");
        }
        catch (IOException e) {
            
            e.printStackTrace(System.err);
            
            toolbox.log.Log.println(e.getMessage());
            
            throw new IOException(msg$1);
        }
        
    }//saveDateTimeOfLastPostOnLastBackup
    
    //DELETAR RAWPAGES

}//classe Finalizer