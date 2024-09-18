package phantom.time;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static phantom.global.GlobalConstants.*;
import phantom.gui.GUInterface;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class TimeTools {
    
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.TimeTools", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Unable to save backup's date-time 
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Unable to save backup's date-time";         
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
    }//bloco static    

    /**
     * 
     * @param datetime
     * @throws IOException 
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void saveDateTimeOfLastPostFromThisBackup(final String datetime)         
        throws Exception {
        
        toolbox.log.Log.exec("phantom.time", "TimeTools", "saveDateTimeOfLastPostFromThisBackup");
        
        toolbox.log.Log.param(datetime);
        
        toolbox.config.Property property = new toolbox.config.Property(UPDATE_PATHNAME);
        
        try { 
            
            property.load(); 
        } 
        catch (Exception e) {}
        
        String priv = property.getProperty("privatearea", ANCIENT_TIMES);
        
        String pub = property.getProperty("publicarea", ANCIENT_TIMES);
        
        String type;
        
        if (GUInterface.isPrivateAreaBackup()) {
            priv = datetime;
            type = "private";
        }
        else {
            pub = datetime; 
            type = "public";
        }
        
        property.setProperty("privatearea", priv);
        property.setProperty("publicarea", pub);        
               
        try {
            
            property.store("Last Successfull Backup : " + type);
        }
        catch (Exception e) {
            
            toolbox.log.Log.ret("phantom.time", "TimeTools", "saveDateTimeOfLastPostFromThisBackup");  
            
            throw new Exception(msg$1);
        }
        
        toolbox.log.Log.ret("phantom.time", "TimeTools", "saveDateTimeOfLastPostFromThisBackup");
             
    }//saveDateTimeOfLastPostOnLastBackup    
    
     /**
     * 
     * @return
     * @throws java.lang.Exception
     */
	public static String readDateTimeOfLastPostFromLastBackup() throws Exception {

        toolbox.log.Log.exec(
            "phantom.time",
            "TimeTools", 
            "readDateTimeOfLastPostFromLastBackup"
        ); 
        
        toolbox.log.Log.println(
            "Lendo data-hora da postagem mais recente do mais recente backup"
        );
        
        toolbox.config.Property property = new toolbox.config.Property(UPDATE_PATHNAME);
 
        property.load(); 

        String lastPost;
        
        if (GUInterface.isPrivateAreaBackup()) 
            
            lastPost = property.getProperty("privatearea");
        
        else 
            
            lastPost = property.getProperty("publicarea");
        
        
        if (lastPost == null) {
            
            toolbox.log.Log.ret(
                "phantom.time",
                "TimeTools", 
                "readDateTimeOfLastPostFromLastBackup"
            );  
            
            phantom.exception.ExceptionTools.crashMessage(null, new NullPointerException());
        }
        
        toolbox.log.Log.ret(
            "phantom.time",
            "TimeTools", 
            "readDateTimeOfLastPostFromLastBackup",
            lastPost
        );         
        
        return lastPost;
        
    }//readDateTimeOfLastPostFromLastBackup 

}//classe TimeTools
