package phantom.pages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalConstants.*;
import toolbox.html.Tag;
import toolbox.html.TagParser;

/***********************************************************************************************************************
 * Classe que analisa, coleta, armazena e fornece dados de uma pagina de Header.
 * 
 * @author Pedro Reis
 * 
 * @since 1.0 - 22 de agosto de 2024
 * 
 * @version 1.0 
 **********************************************************************************************************************/
final class Header extends Page implements Comparable {
    
    private final DivForabgParser divForabgParser = new DivForabgParser();
    private final LiRowParser liRowParser = new LiRowParser();
    private final UlTopiclistForumsParser ulTopiclistForumsParser = new UlTopiclistForumsParser();
    private final SectionDataParser sectionDataParser = new SectionDataParser();
    
    private String sectionName;
    private String sectionURL;
    private String sectionFilename;
    private String sectionNumberOfTopics = "0";
    private String sectionLastPostTime = THE_VERY_FIRST_SECOND;
    
    private static String msg$1;
    
    /*==================================================================================================================
    * Internacionaliza as Strings "hardcoded" na classe
    ==================================================================================================================*/
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Header", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            //Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Error parsing how many topics has the section:";         
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }  
        
    }//bloco static     
    
    /*******************************************************************************************************************
     * 
     * @param url
     * @param filename
     * @param name 
     * @param lastPostTime 
     ******************************************************************************************************************/
    protected Header(
        final String name, 
        final String url, 
        final String filename,
        final String lastPostTime
    ) {
        
        toolbox.log.Log.exec("phantom.pages", "Header", "Construtor de Header");
        toolbox.log.Log.param(name, url, filename, lastPostTime);
        
        setPageName(name);
        setPageUrl(url);
        setPageFilename(filename);
        setPageParser(divForabgParser);
        setLastPostDateTime(lastPostTime);
        setNumberOfPages(1);
         
        toolbox.log.Log.ret("phantom.pages", "Header", "Construtor de Header");
        
    }//construtor

    /*******************************************************************************************************************
     * 
     * @param t
     * @return 
     ******************************************************************************************************************/
    @Override
    public int compareTo(Object t) {
        
        return getLastPostDateTime().compareTo( ((Header)t).getLastPostDateTime() );
        
    }//compareTo
    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/
private final class DivForabgParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.isClass("forabg")) return ulTopiclistForumsParser;

        return null;
    }

}//classe privada DivForabgParser  

private final class UlTopiclistForumsParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.getTagId().equals("ul") && tag.contains("class", "topiclist forums")) {
            
            return liRowParser;
        }

        return null;
    }
    
}//classe privada UlTopiclisForumsParser
   
private final class LiRowParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception { 

        if (tag.isClass("row")) {
            
            tag.notifyClosing();
            return  sectionDataParser;           
        }

        return null; 
        
    }//openTag

    @Override
    public void closeTag(Tag tag) throws Exception {
        
        int nTopics = Integer.parseInt(sectionNumberOfTopics);  
        int numberOfPages = (nTopics == 0) ? 1 : ( (nTopics - 1)/ Page.MaxList.MAX_TOPICS_TITLES_PER_PAGE.get() ) + 1;        

        addPage(
            new Section(
                sectionName,
                sectionURL,
                sectionFilename,
                numberOfPages,
                sectionLastPostTime
            ),
            numberOfPages            
        );
        /*
        Anula campos para que o proximo objeto Header nao receba acidentalmente dados deste.
        */
        sectionName = null;
        sectionURL = null;
        sectionFilename = null;
        sectionNumberOfTopics = "0";            
        sectionLastPostTime = THE_VERY_FIRST_SECOND;            
    }  
    
}//classe privada LiRowParser

private final class SectionDataParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception { 
    
        if (tag.isClass("forumtitle")) {
            
            sectionURL = tag.getAttrMap().get("href");
            
            matcher = HEADERSECTION_FILENAME_FINDER.matcher(sectionURL);
            
            if (matcher.find()) sectionFilename = matcher.group();
            
            tag.notifyClosing();

        } else if (tag.isClass("forum-statistics")) {
            
            tag.notifyClosing();

        } else if (tag.getTagId().equals("time")) {
            
            sectionLastPostTime = tag.getAttrMap().get("datetime");

        }
        
        return null;
    }
    
    @Override
    public void closeTag(Tag tag) throws XMLParseException {
        
        if (tag.getTagId().equals("a"))
            
            sectionName = tag.getTagContent();
        
        else {
            
            matcher = NUMBER_OF_TOPICS_FINDER.matcher(tag.getTagContent());
            
            if (matcher.find())
                
                sectionNumberOfTopics = matcher.group(1);
            
            else
                
                throw new XMLParseException(msg$1);
        }
            
    }
    
}//classe privada SectionDataParser

    
}//classe Header

