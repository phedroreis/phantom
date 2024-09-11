package phantom.cron;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import static phantom.global.GlobalConstants.ANCIENT_TIMES;
import static phantom.global.GlobalConstants.UPDATE_PATHNAME;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class CronTools {
    
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Cron", 
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
    
     /**
     * 
     * @return
     */
	public static String readDateTimeOfLastPostFromLastBackup() {

        toolbox.log.Log.exec(
            "phantom.main",
            "Initializer", 
            "readDateTimeOfLastPostFromLastBackup"
        ); 
        
        toolbox.log.Log.println(
            "Lendo data-hora da postagem mais recente do mais recente backup"
        );
        
        Properties props = new Properties();
        
        try ( FileInputStream in = new FileInputStream(UPDATE_PATHNAME) ) { 
            
            props.load(in); 
        }
        catch (IOException e) {
            
            toolbox.log.Log.ret(
                "phantom.main",
                "Initializer", 
                "readDateTimeOfLastPostFromLastBackup",
                ANCIENT_TIMES
            );   
            
            return ANCIENT_TIMES;
            
        }
  
        String last = props.getProperty("last");

        if (last == null) {
            
            toolbox.log.Log.ret(
                "phantom.main",
                "Initializer", 
                "readDateTimeOfLastPostFromLastBackup",
                ANCIENT_TIMES
            );  
            
            return ANCIENT_TIMES;
        }
        
        toolbox.log.Log.ret(
            "phantom.main",
            "Initializer", 
            "readDateTimeOfLastPostFromLastBackup",
            last
        );         
        
        return last;
        
    }//readDateTimeOfLastPostFromLastBackup 
    
    


}//classe CronTools
