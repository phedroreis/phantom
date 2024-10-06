package phantom.pages;

import java.util.HashMap;
import static phantom.global.GlobalConstants.*;
import toolbox.html.Tag;
import toolbox.html.TagParser;

/***********************************************************************************************************************
 * Classe que analisa, coleta, armazena e fornece dados de uma pagina principal.
 * 
 * @author Pedro Reis
 * @since 1.0 - 18 de agosto de 2024
 * @version 1.0 
 **********************************************************************************************************************/
final class Main extends Page {
    
    private final DivForabgParser divForabgParser = new DivForabgParser();
    private final UlTopiclistForumsParser ulTopiclistForumsParser = new UlTopiclistForumsParser();
    private final LiHeaderParser liHeaderParser = new LiHeaderParser();
    private final LiRowParser liRowParser = new LiRowParser();
    private final DlRowItemParser dlRowItemParser = new DlRowItemParser();
    private final AParser aParser = new AParser();
    private final DdLastPostParser ddLastPostParser = new DdLastPostParser();
    private final TimeParser timeParser = new TimeParser();

    private String headerName;
    private String headerURL;
    private String headerFilename;
    private String headerLastPostTime = THE_VERY_FIRST_SECOND;  
       
    /*******************************************************************************************************************
     * Construtor da classe
     ******************************************************************************************************************/
    public Main() {
        
        toolbox.log.Log.exec("phantom.pages", "Main", "Construtor de Main");
        
        String forumName = FORUM_NAME;
        
        setPageName(forumName);
        
        setPageFilename(forumName);
        
        //O metodo ira converter em https://clubeceticismo.com.br/
        setPageUrl("");
        
        setPageParser(divForabgParser);
        
        setLastPostDateTime(phantom.time.TimeTools.getLastPostDateTime());
        
        setNumberOfPages(1);
 
        toolbox.log.Log.ret("phantom.pages", "Main", "Construtor de Main");
        
    }//construtor
    
/*======================================================================================================================
         Classe privada. O parsing localiza os dados de HEADERS na pagina principal do forum.   
======================================================================================================================*/    
private final class DivForabgParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.isClass("forabg")) {

            tag.notifyClosing();
            return ulTopiclistForumsParser;
        }

        return null;
    }

    @Override
    public void closeTag(Tag tag) throws Exception {

        addPage(new Header(
                headerName,
                headerURL,
                headerFilename,
                headerLastPostTime
            ),
            1
        );
        /*
        Anula campos para que o proximo objeto Header nao receba acidentalmente dados deste.
        */
        headerName = null;
        headerURL = null;
        headerFilename = null;
        headerLastPostTime = THE_VERY_FIRST_SECOND;            
    }

}//classe privada DivForabgParser

private final class UlTopiclistForumsParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.isClass("topiclist")) {
            
            if (tag.isClass("forums"))
                
                return liRowParser;
            
            else 
                
                return liHeaderParser;
        }
        
        return null;
    }
    
}//classe privada UlTopiclistForumsParser




private final class LiHeaderParser extends toolbox.html.TagParser {
    
   @Override
    public TagParser openTag(Tag tag) throws Exception {  

        if (tag.isClass("header")) return dlRowItemParser;

        return null;
    }     
    
}//classe privada LiHeaderParser

private final class DlRowItemParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) {
        
        if (tag.isClass("row-item")) return aParser;
        
        return null;
        
    }
    
}//classe privada DlRowItemParser

private final class AParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) {
        
        if (tag.getTagId().equals("a")) {

            HashMap<String, String> map = tag.getAttrMap();

            headerURL = map.get("href");
            matcher = HEADERSECTION_FILENAME_FINDER.matcher(headerURL);
            if (matcher.find()) headerFilename = matcher.group(); 
            
            tag.notifyClosing();
        
        }

        return null;
    }
    
    @Override
    public void closeTag(Tag tag) {

        headerName = tag.getTagContent();

    }    
    
    
}//classe privada DlRowItemParser



private final class LiRowParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) {
        
        if (tag.isClass("row")) return ddLastPostParser;
            
        return null;    
    }
    
}//classe privada LiRowParser

private final class DdLastPostParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.isClass("lastPost")) return timeParser;

        return null;
    }
    
}//classe privada DdLastPostParser

private final class TimeParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) {  
        
        if (tag.getTagId().equals("time")) {
            
            String datetimeValue = tag.getAttrMap().get("datetime");
            
            if (datetimeValue == null) return null;

            if (headerLastPostTime.compareTo(datetimeValue) < 0) 
                headerLastPostTime = datetimeValue;          
            
        }
        
        return null;
    }
    
}//classe privada TimeParser

}//classe Main
