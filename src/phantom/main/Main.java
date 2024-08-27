package phantom.main;

import java.io.IOException;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author Pedro Reis
 */
public final class Main {

    /**
     * @param args the command line arguments
     * @throws XMLParseException
     * @throws IOException
     */
    public static void main(String[] args) throws XMLParseException, IOException{
   
        toolbox.log.Log.exec("phantom.main", "Main", "main");
        
        Initializer.init();
        
        phantom.pages.Downloader downloader = 
            new phantom.pages.Downloader(Initializer.getDateTimeOfLastPostFromLastBackup());
        
        downloader.downloadAllPages();
        
        Finalizer.setDateTimeOfLastPostFromThisBackup(
            downloader.getDateTimeOfLastPostFromThisBackup()
        );
        
        toolbox.log.Log.ret("phantom.main", "Main", "main");        
        toolbox.log.Log.closeFile();
    }
    
}
