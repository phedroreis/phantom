package phantom.time;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class ElapsedTime {
    
    private long start;
    
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.ElapsedTime", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "[%02dh:%02dm:%02ds]";

        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }
        
        
    }//bloco static     

    /**
     * 
     */
    public void start() {
        
        start = System.currentTimeMillis();

    }//start
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        
        long elapsed = System.currentTimeMillis() - start;
        
        long h = elapsed / 3600000;
        long m = (elapsed % 3600000) / 60000;
        long s = (elapsed - (3600000 * h +  60000 * m)) / 1000;
        
        return String.format(" " + msg$1, h, m, s);
        
    }//toString

}//classe ElapsedTime
