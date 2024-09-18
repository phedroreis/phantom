package phantom.edit;

import java.util.HashMap;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class Img extends Tag {
    
    @Override
    protected void parseUrl(
        final String url, 
        final phantom.fetch.Fetcher fetcher,
        final HashMap<String, String> url2staticUrl) throws Exception {
        
        if (url == null || !( url.matches("\\./.+") || url.startsWith(ROOT_URL) )) 
            return;
        
        setFetcher(fetcher);
        setUrl2staticUrl(url2staticUrl);     
        
        String staticUrl = "#";//Ira conter a versao estatica da URL original
        
        String urlRelative = url.replace(ROOT_URL, "./");  
        
        matcher = FILE_PHP.matcher(url);
    
        if (matcher.find()) {
            
            staticUrl = 
                urlRelative.substring(0, urlRelative.indexOf("file.php?")) + matcher.group(1);//url.replace("file.php?", "").replace("&amp;", "-");

            queue(urlRelative, staticUrl); 
            
            url2staticUrl(url, staticUrl);
        }
        else {
            
            if (url.startsWith(ROOT_URL)) url2staticUrl(url, urlRelative);
            
            queue(urlRelative, urlRelative);
        }           
        
    }//parseUrl

}//classe Img
