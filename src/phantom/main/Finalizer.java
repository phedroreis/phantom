package phantom.main;

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
       
        props.setProperty("last", datetime);         
       
        try (FileOutputStream out = new FileOutputStream(updatePathname)) {
            
            props.store(out, "Static copy");
        }
        catch (IOException e) {
            
            e.printStackTrace(System.err);
            
            throw new IOException("Unable to save last backup date-time");
        }
    }
    
    //DELETAR RAWPAGES

}//classe Finalizer
