package phantom.pages;

import java.util.HashMap;
import static phantom.global.GlobalConstants.*;
import toolbox.html.Tag;
import toolbox.html.TagParser;

/***********************************************************************************************************************
 * Classe que analisa, coleta, armazena e fornece dados de uma pagina principal.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 18 de agosto de 2024
 **********************************************************************************************************************/
final class Main extends Page {
    
    private final MainParser mainParser = new MainParser();
    private final HeaderOrSectionParser headerOrSectionParser = new HeaderOrSectionParser();
    private final HeaderParser headerParser = new HeaderParser();
    private final SectionParser sectionParser = new SectionParser();
    
    
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
        
        setPageParser(mainParser);
        
        setLastPostDateTime(phantom.time.TimeTools.getLastPostDateTime());
        
        setNumberOfPages(1);
 
        toolbox.log.Log.ret("phantom.pages", "Main", "Construtor de Main");
        
    }//construtor
    
/*======================================================================================================================
         Classe privada. O parsing localiza os dados de HEADERS na pagina principal do forum.   
======================================================================================================================*/    
private final class MainParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.getTagId().equals("div")) {

            String classValue = tag.getAttrMap().get("class");

            if (classValue != null && classValue.startsWith("forabg")) {
                tag.notifyClosing();
                return headerOrSectionParser;
            }
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

}//classe privada MainParser

private final class HeaderOrSectionParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {  

        if (tag.getTagId().equals("li")) {

            String classValue = tag.getAttrMap().get("class");

            if (classValue != null && classValue.equals("header")) {

                return headerParser;

            } else if (classValue != null && classValue.startsWith("row forum-")) {

                return sectionParser;

            }

        }

        return null;

    } 
    
}//classe privada HeaderOrSectionParser

private final class HeaderParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.getTagId().equals("a")) {

            HashMap<String, String> map = tag.getAttrMap();

            headerURL = map.get("href");
            headerFilename = "f=" + map.get("data-id");
            tag.notifyClosing();

        }

        return null;

    }

    @Override
    public void closeTag(Tag tag) {

        headerName = tag.getTagContent();

    }

}//classe privada HeaderParser

private final class SectionParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.getTagId().equals("time")) {

            String datetimeValue = tag.getAttrMap().get("datetime");
            
            if (datetimeValue == null) return null;

            if (headerLastPostTime.compareTo(datetimeValue) < 0) headerLastPostTime = datetimeValue;

        }

        return null;
    }
    
}//classe privada SectionParser

}//classe Main
