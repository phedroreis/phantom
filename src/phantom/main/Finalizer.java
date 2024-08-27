package phantom.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import static phantom.global.GlobalStrings.UPDATE_PATHNAME;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Finalizer {

    public static void setDateTimeOfLastPostFromThisBackup(final String datetime)         
        throws IOException {
        
        Properties props = new Properties();
        
        String updatePathname = UPDATE_PATHNAME.get();
        
        try (FileInputStream in = new FileInputStream(updatePathname);) { props.load(in); }  
        
        props.setProperty("last", datetime);         
       
        try (FileOutputStream out = new FileOutputStream(updatePathname);) {
            
            props.store(out, "Backup finish");
        }
        catch (IOException e) {
            
            e.printStackTrace(System.err);
            
            throw new IOException("Unable to save last backup date-time");
        }
    }

}//classe Finalizer
