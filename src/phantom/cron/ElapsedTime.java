package phantom.cron;

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
        
        return String.format(" [%02dh:%02dm:%02ds]", h, m, s);
        
    }//toString

}//classe ElapsedTime
