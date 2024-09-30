package phantom.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * 
 * @since 1.0
 * 
 * @version 1.0
 **********************************************************************************************************************/
abstract class Tag {
   
    /*==================================================================================================================
    Localiza /nomeDoScript.php em URLs relativas ou absolutas para scripts PHP
    ==================================================================================================================*/
    protected static final Pattern PHP_SCRIPT = Pattern.compile("/[^/]*?\\.php");
    
    protected static final Pattern VIEWFORUM_ID = Pattern.compile("f=\\d+");//Loc. ID de Header ou Section
    
    protected static final Pattern START_INDEX = Pattern.compile("start=(\\d+)");
    
    protected static final Pattern VIEWTOPIC_ID = Pattern.compile("[^r](t=\\d+)");//Localiza ID de Topic 

    protected static final Pattern VIEWTOPIC_POST = Pattern.compile("#p\\d+");//Loc. ref para post em pag. Topic
    
    protected static final Pattern QUERY = Pattern.compile("\\?.*$");//Localiza queries em arqs. js 
    
    protected static final Pattern FILE_PHP = Pattern.compile("^.+file\\.php\\?(.+?=\\d+)");

    protected static Matcher matcher;  
    
    protected HashMap<String, String> url2staticUrl;
    
    protected phantom.fetch.Fetcher fetcher;
    
    protected void setUrl2staticUrl(final HashMap<String, String> url2staticUrl) {
        
        this.url2staticUrl = url2staticUrl;
        
    }//setUrl2staticUrl
    
    protected void setFetcher(final phantom.fetch.Fetcher fetcher) {
        
        this.fetcher = fetcher;
        
    }//setFetcher

    protected void queue(final String RelativeUrl, final String RelativeFile) throws Exception {
     
        fetcher.queue(new phantom.fetch.Node(RelativeUrl, RelativeFile));
        
    }
    
    protected void url2staticUrl(final String url, final String staticUrl) {
        
        url2staticUrl.put(url + '\"', staticUrl + '\"');
        
    }
    
    protected abstract void parseUrl(
        final String url, 
        final phantom.fetch.Fetcher fetcher,
        final HashMap<String, String> url2staticUrl) throws Exception;

}//classe Tag
