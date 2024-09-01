package phantom.fetch;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalStrings.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Fetch {
    
       private static final String F_NAME = FORUM_NAME.get();
    
    private static HashMap<String, String> urlsMap;
    
    public void fetch() throws IOException, XMLParseException {
        
        toolbox.file.SearchFolders searchFolders = new toolbox.file.SearchFolders(new PagesFilter());
        
        LinkedList<String> listPages = searchFolders.search(RAW_PAGES_DIR.get());
        
        for (String filename : listPages) {
            
            System.out.println("        [" + filename +"]");
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(filename, "utf8");
            
            textFileHandler.read();
            
            String content = textFileHandler.getContent();
            
            toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(content, new Parser());
            
            urlsMap = new HashMap<>();
            
            htmlParser.parse();
            
            content = content.replace(ROOT_URL.get(), F_NAME + ".html");
            
            for (String key : urlsMap.keySet()) {
                
                String value = urlsMap.get(key);
                
                if (filename.endsWith("f=19.html")) System.out.println(key + " --> " + value);
                
                content = content.replace(key, value);
            }
            
            textFileHandler.setContent(content);
            
            textFileHandler.write();
            
        }
        
    }//fetch
    
    public static void main(String[] args) throws IOException, XMLParseException {
        Fetch f = new Fetch();
        f.fetch();
    }
    
/*======================================================================================================================
    Classe privada. 
======================================================================================================================*/
private static final class Parser extends toolbox.xml.TagParser {
    
    private static final String FORUM_URL = ROOT_URL.get();
 
    private static final Pattern PHP_SCRIPT = Pattern.compile("^\\./.+?\\.php");
    
    private static final Pattern VIEWFORUM_ID = Pattern.compile("f=\\d+");
    
    private static final Pattern VIEWTOPIC_ID = Pattern.compile("t=\\d+"); 
    
    private static final Pattern VIEWTOPIC_START = Pattern.compile("x=(\\d+)");

    private static final Pattern VIEWTOPIC_POST = Pattern.compile("#p\\d+");    

    private static Matcher matcher;    
    
    /*==================================================================================================================
    *
    ==================================================================================================================*/
    private void queue(final String url) {
        
        String absoluteUrl = url.replace("./", FORUM_URL + '/');
   
        //System.out.println(absoluteUrl);
        
    }//queue
    
    /*==================================================================================================================
    *
    ==================================================================================================================*/    
    private void parseUrl(final String url) {
        
        matcher = PHP_SCRIPT.matcher(url);
        
        String staticUrl = "#";
    
        if (matcher.find()) {

            switch (matcher.group()) {
                
                case "./index.php":
                    
                    staticUrl = F_NAME + ".html";
                    break;    
                    
                case "./viewforum.php":
                    
                    
                    matcher = VIEWFORUM_ID.matcher(url);
                    if (matcher.find()) 
                        staticUrl = matcher.group() + ".html";
                    else 
                        staticUrl = "#";
                    
                    break;     
                    
                case "./viewtopic.php":
                    
                    String urlx = url.replace("start=", "x=");
                    
                   
                    matcher = VIEWTOPIC_START.matcher(urlx);
                    String startIndex;
                    if (matcher.find())
                        startIndex = "&start=" + matcher.group(1);
                    else 
                        startIndex = ""; 
                    

                    matcher = VIEWTOPIC_POST.matcher(urlx);
                    String postID; 
                    if (matcher.find()) 
                        postID = matcher.group();
                    else
                        postID = "";                    
                   
                    matcher = VIEWTOPIC_ID.matcher(urlx);
                    if (matcher.find()) 
                        staticUrl = matcher.group() + startIndex + ".html" + postID;
                    else 
                        staticUrl = "_postwarn.html" + postID;

                    break;
                    
                case "./ucp.php":
                case "./app.php":    
                    
                    staticUrl = "_warn.html";
                    
                    break;

                case "./search.php":
                    
                    staticUrl = "_searchwarn.html"; 
                    
                    break;
                    
                case "./posting.php":
                    
                    staticUrl = "_postingwarn.html";   
                    
                    break;
                    
                case "memberlist.php":
                    
                    staticUrl = "_memberwarn.html"; 
                    
                    break;
                    
                case "./download/file.php":
                    
                    staticUrl = url;
                              
            }//switch
            
            urlsMap.put(url + "\"", "./" + staticUrl + "\"");
        }
        else  queue(url.replaceAll("\\?.+", ""));   
        
    }//parseUrl
    
   /*==================================================================================================================
    *
    ==================================================================================================================*/    
    @Override
    public void openTag(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        String url = null;
       
        switch (tagName) {
            
            case "a" :
            case "link" :
                //System.out.println("----> " + tagName);
                url = map.get("href");
                break;
                
            case "img":
            case "script":
                //System.out.println("----> " + tagName);
                url = map.get("src");

  
        }//switch
        
        if ( url != null && url.startsWith("./") ) parseUrl(url);  
        
    }//openTag
    
  
}//classe privada Parser

/*======================================================================================================================
 * Classe privada implementa o filtro padrao.
======================================================================================================================*/
private static class PagesFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) throws IOException {
         
        String fileName = file.getFileName().toString();
        
        return (fileName.matches("(f|t)=\\d+\\.html") || fileName.equals(FORUM_NAME.get() + ".html")) ;
    }
     
}//classe PagesFilter   

}//classe Fetch
