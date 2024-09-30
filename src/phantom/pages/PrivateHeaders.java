package phantom.pages;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
final class PrivateHeaders {
    
    private final Set<String> headersSet;

    /*******************************************************************************************************************
     * 
     ******************************************************************************************************************/
    protected PrivateHeaders() {
        
        headersSet = new HashSet<>(); 
        
        toolbox.config.Property property = new toolbox.config.Property(CONFIG_PATHNAME);
        
        try {
            property.load(); 
        }
        catch(IOException e) {}
        
        String headersList = property.getProperty("privateheaders", "6,26");
        
        Scanner scanner = new Scanner(headersList);
        
        scanner.useDelimiter(",");
        
        while (scanner.hasNext()) {
            String headerNumber = scanner.next();
            headersSet.add("f=" + headerNumber);
        }

    }//construtor
    
    /*==================================================================================================================
    *
    ==================================================================================================================*/
    private boolean contains(final String headerID) {
        
        return headersSet.contains(headerID);
        
    }//contains
    
    /*******************************************************************************************************************
     * 
     * @param headersList 
     ******************************************************************************************************************/
    protected void removeNonPrivateHeaders(final List<Page> headersList) {
        
        Iterator<Page> i = headersList.iterator();

        while (i.hasNext()) {

            String headerID = i.next().getPageFilename(0).replace(".html", "");

            if (!contains(headerID)) i.remove();

        }
            
    }//removeNonPrivateHeaders

}//classe PrivateHeaders
