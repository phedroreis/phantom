package phantom.time;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public final class TimeTools {
    
    private static String privateAreaLastPostDateTime;
    
    private static String publicAreaLastPostDateTime;
    
    private static String msg$1;
    
    /*==================================================================================================================
    * Internacionaliza as Strings "hardcoded" na classe
    ==================================================================================================================*/
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
     * @return
     * @throws IOException 
     */
    public static String readLastBackupDateTime() throws IOException {
        
        toolbox.config.Property property = new toolbox.config.Property(UPDATE_PATHNAME);
 
        property.load(); 

        String datetime = property.getProperty("datetime");
        
        if (datetime == null) return " --- ";
            
        return datetime;
      
    }//readLastBackupDateTime

    /**
     * 
     * @return 
     */
    public static String getLastPostDateTime() {
        
        if (phantom.gui.MainFrame.getPrivateAreaRadioButtonReference().isSelected())
        
            return privateAreaLastPostDateTime;
        
        else
            
            return publicAreaLastPostDateTime;
        
    }//getLastPostDateTime

    /**
     * 
     * @param datetime 
     */
    public static void setLastPostDateTime(final String datetime) {
        
        if (phantom.gui.MainFrame.getPrivateAreaRadioButtonReference().isSelected())
        
            privateAreaLastPostDateTime = datetime;
        
        else 
            
            publicAreaLastPostDateTime = datetime;
        
    }//setPrivateAreaLastPostDateTime

     /******************************************************************************************************************
     * 
     * @throws Exception
     ******************************************************************************************************************/
	public static void readLastPostDateTime() throws Exception {

        toolbox.log.Log.exec(
            "phantom.time",
            "TimeTools", 
            "readLastPostDateTime"
        ); 
        
        toolbox.log.Log.println(
            "Lendo data-hora da postagem mais recente do mais recente backup"
        );
        
        toolbox.config.Property property = new toolbox.config.Property(UPDATE_PATHNAME);
 
        property.load();
        
        privateAreaLastPostDateTime = property.getProperty("privatearea", ANCIENT_TIMES);
        
        publicAreaLastPostDateTime = property.getProperty("publicarea", ANCIENT_TIMES);
        
        toolbox.log.Log.ret(
            "phantom.time",
            "TimeTools", 
            "readLastPostDateTime"    
        );         
        
    }//readLastPostDateTime     

    /*******************************************************************************************************************
     * 
     * @throws IOException 
     ******************************************************************************************************************/
    @SuppressWarnings("UseSpecificCatch")
    public static void saveLastPostDateTime() throws Exception {
        
        toolbox.log.Log.exec("phantom.time", "TimeTools", "saveLastPostDateTime");
        
        toolbox.config.Property property = new toolbox.config.Property(UPDATE_PATHNAME);
        
        String type;
        
        if (phantom.gui.MainFrame.getPrivateAreaRadioButtonReference().isSelected())  
            type = "private";
        else 
            type = "public";
  
        
        property.setProperty("privatearea", privateAreaLastPostDateTime);
        property.setProperty("publicarea", publicAreaLastPostDateTime); 
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        property.setProperty("datetime", now.format(formatter));
               
        try {
            
            property.store("Last Successfull Backup : " + type);
        }
        catch (Exception e) {
            
            toolbox.log.Log.ret("phantom.time", "TimeTools", "saveLastPostDateTime");  
            
            throw new Exception(msg$1);
        }
        
        toolbox.log.Log.ret("phantom.time", "TimeTools", "saveLastPostDateTime");
             
    }//saveLastPostDateTime   
    
}//classe TimeTools
