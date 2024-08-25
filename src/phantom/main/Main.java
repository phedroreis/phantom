package phantom.main;

import java.io.IOException;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author Pedro Reis
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws XMLParseException
     * @throws IOException
     */
    public static void main(String[] args) throws XMLParseException, IOException{
   
        phantom.log.Log.createLogFile();
        
        toolbox.log.Log.exec("phantom.main", "Main", "main");
        
        phantom.pages.Downloader downloader = 
            new phantom.pages.Downloader(
                phantom.incremental.Incremental.isFull(), 
                phantom.incremental.Incremental.getTimeOfLastPostOnLastBackup()
            );
        
        downloader.downloadAllPages();
        
        toolbox.log.Log.ret("phantom.main", "Main", "main");        
        toolbox.log.Log.closeFile();
    }
    
}
