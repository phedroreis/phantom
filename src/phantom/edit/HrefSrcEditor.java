package phantom.edit;

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
public final class HrefSrcEditor {
    
    private static final String MAIN_PAGE_FILE = FORUM_NAME.get() + ".html";
    
    private static final String FORUM_URL = ROOT_URL.get();
    
    private static HashMap<String, String> urlsMap;
    
    private static phantom.fetch.Fetch fetch;
    
    @SuppressWarnings("UnusedAssignment")
    public void edit() throws IOException, XMLParseException, InterruptedException {
        
        toolbox.file.SearchFolders searchFolders = new toolbox.file.SearchFolders(new PagesFilter());
        
        LinkedList<String> listPages = searchFolders.search(RAW_PAGES_DIR.get());
        
        fetch = new phantom.fetch.Fetch();
        
        Thread downloadThread = new Thread(fetch);
        
        downloadThread.start();
        
        for (String filename : listPages) {
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(filename, "utf8");
            
            textFileHandler.read();
            
            String content = textFileHandler.getContent();
            
            toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(content, new Parser());
            
            urlsMap = new HashMap<>();
            
            htmlParser.parse();
            
            StringBuilder sbContent = new StringBuilder(content); 
            
            content = null; textFileHandler.setContent(content);//Para liberar memoria
                        
            for (String key : urlsMap.keySet()) {
                
                String value = urlsMap.get(key);
                
                toolbox.string.StringTools.replace(sbContent, key, value);
                
            }//for
            
            toolbox.string.StringTools.replace(sbContent, FORUM_URL + "\"", "./" + MAIN_PAGE_FILE + "\"");
            
            textFileHandler.setContent(sbContent.toString());
            
            textFileHandler.write();
            
        }//for
        
        fetch.terminate();
        
    }//edit
    
    public static void main(String[] args) throws IOException, XMLParseException, InterruptedException {
        HrefSrcEditor f = new HrefSrcEditor();
        f.edit();
    }
    
/*======================================================================================================================
           Classe privada para o parsing dos arquivos HTML das paginas do forum. 
======================================================================================================================*/
private static final class Parser extends toolbox.xml.TagParser {
    
    private static final Pattern PHP_SCRIPT = Pattern.compile("^(" + FORUM_URL + "|\\./).+?\\.php");
    
    private static final Pattern VIEWFORUM_ID = Pattern.compile("f=\\d+");
    
    private static final Pattern VIEWTOPIC_ID = Pattern.compile("t=\\d+"); 
    
    private static final Pattern START_INDEX = Pattern.compile("x=(\\d+)");

    private static final Pattern VIEWTOPIC_POST = Pattern.compile("#p\\d+");
    
    private static final Pattern QUERY = Pattern.compile("\\?.*$");    

    private static Matcher matcher;  
    
    /*==================================================================================================================
    *   Marca os atributos href/src que serao editados e baixa os arquivos das URLs destes atributos
    ==================================================================================================================*/    
    private void editUrlsAndDownloadFiles(final String url) throws InterruptedException {
        
        matcher = PHP_SCRIPT.matcher(url);
        
        String urlx; String startIndex;
        String staticUrl = "#";
    
        if (matcher.find()) {
            
            String phpScriptName = matcher.group();

            switch (phpScriptName) {
                
                case "./index.php":
                    
                    staticUrl = "./" + MAIN_PAGE_FILE;
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");                    
                    
                    break;    
                    
                case "./viewforum.php":
                    
                    //t= nao pode ser localizada dentro de start=
                    urlx = url.replace("start=", "x=");
                    
                    matcher = START_INDEX.matcher(urlx);
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : ""; 
                    
                    matcher = VIEWFORUM_ID.matcher(urlx);
                    staticUrl = matcher.find() ? "./" + matcher.group() + startIndex + ".html" : "#"; 
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");
                    
                    break;     
                    
                case "./viewtopic.php":
                    
                    //t= nao pode ser localizada dentro de start=
                    urlx = url.replace("start=", "x=");                    
                   
                    matcher = START_INDEX.matcher(urlx);
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : "";                    

                    matcher = VIEWTOPIC_POST.matcher(urlx);
                    String postID = matcher.find() ? matcher.group() : "";                    
                   
                    matcher = VIEWTOPIC_ID.matcher(urlx);
                    staticUrl = 
                        matcher.find() ? 
                        "./" + matcher.group() + startIndex + ".html" + postID :
                        "./_post.html" + postID;
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");
                    break;
                    
                case "./download/file.php":   
                    
                    break;
                    
                case "/app.php":  
                case "./memberlist.php": 
                case "./posting.php":  
                case "./search.php":                    
                case "./ucp.php":    
                    
                    staticUrl = phpScriptName.replace("/", "/_").replace("php", "html");

                    urlsMap.put(url + "\"", staticUrl + "\"");                     
            }//switch

        }
        else  {
 
            //Retira parametros queries de arquivos a serem baixados
            matcher = QUERY.matcher(url);
            String query = matcher.find() ? matcher.group() : "";
            String editedUrl = url.replace(query, "");//Deleta query da URL que vai baixar o arquivo
            
            /*
            Aponta para arquivo no servidor do forum com URL absoluta, mas nao eh script Php
            */
            if (editedUrl.startsWith("http")) {
                
                /*
                Converte o endereco absoluto em relativo
                http://clubeceticismo.com.br/file --> ./file
                */
                editedUrl = editedUrl.replace(FORUM_URL,"./");
                
                //url troca por ./file+query na pag. html estatica. 
                //A query permanece no atributo href/src do arquivo HTML estatico
                urlsMap.put(url + "\"", editedUrl + query + "\"");
            }
            
            fetch.queue(editedUrl);//Baixa ./file
        }   
        
    }//parseUrl
    
   /*==================================================================================================================
    *              Busca pelas tags de autofechamento a, img, link e script
    ==================================================================================================================*/    
    @Override
    public void openTag(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        String url = null;
       
        switch (tagName) {
            
            case "a":
            case "link":
                url = map.get("href");
                break;
                
            case "img":
            case "script":
                url = map.get("src");
                break;
                
            case "form":
                url = map.get("action");
  
        }//switch
        
        /*
        Baixa arquivo e edita URL no atr. href/src se for URL relativa ou absoluta
        apontando para dominio do forum
        */
        if ( url != null && (url.matches("\\.\\.?/.+") || url.startsWith(FORUM_URL)) ) {
            
            try {
                
                editUrlsAndDownloadFiles(url);
                
            } 
            catch (InterruptedException e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
            }
        }
        
    }//openTag    
  
}//classe privada Parser

/*======================================================================================================================
 *   Classe privada: seleciona apenas arquivos de paginas HTML do forum
======================================================================================================================*/
private static class PagesFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) throws IOException {
         
        String fileName = file.getFileName().toString();
        
        return (fileName.matches("(f|t)=\\d+(&start=\\d+){0,1}\\.html") || fileName.equals(MAIN_PAGE_FILE)) ;
    }
     
}//classe privada PagesFilter   

}//classe HrefSrcEditor