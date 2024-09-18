package phantom.edit;

import java.util.HashMap;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class Script extends Tag {
    
    @Override
    protected void parseUrl(
        final String url, 
        final phantom.fetch.Fetcher fetcher,
        final HashMap<String, String> url2staticUrl) throws Exception {
        
        if (url == null || !( url.matches("\\./.+") || url.startsWith(ROOT_URL) )) 
            return;
        
        setFetcher(fetcher);
        setUrl2staticUrl(url2staticUrl);
        
        String urlRelative = url.replace(ROOT_URL, "./");  

            
        if (url.startsWith(ROOT_URL)) url2staticUrl(url, urlRelative);
        
        matcher = QUERY.matcher(url);
        String query = matcher.find() ? matcher.group() : ""; 
            
        /*
        Nem a URL e nem o nome do arquivo gravado devem incluir a query.
        */
        urlRelative = urlRelative.replace(query, "");              
            
        queue(urlRelative, urlRelative);
       
    }//parseUrl

}//classe Script
