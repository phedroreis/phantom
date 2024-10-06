package phantom.pages;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalConstants.*;
import toolbox.html.Tag;
import toolbox.html.TagParser;

/***********************************************************************************************************************
 * Classe que analisa, coleta, armazena e fornece dados de uma pagina de Section.
 * 
 * @author Pedro Reis
 * 
 * @since 1.0 - 22 de agosto de 2024
 * 
 * @version 1.0 
 **********************************************************************************************************************/
final class Section extends Page {
    
    private final DivForumbgParser divForumbgParser = new DivForumbgParser();
    private final UlTopiclistTopicsParser ulTopiclistTopicsParser = new UlTopiclistTopicsParser();
    private final LiRowParser liRowParser = new LiRowParser();
    private final TopicDataParser topicDataParser = new TopicDataParser();
    private final LastPostParser lastPostParser = new LastPostParser();
    
    private String topicName;
    private String topicURL;
    private String topicFilename;
    private String topicNumberOfPosts = "0";
    private String topicLastPostTime = THE_VERY_FIRST_SECOND; 
    
    private static String msg$1;
    private static String msg$2;
    
    /*==================================================================================================================
    * Internacionaliza as Strings "hardcoded" na classe
    ==================================================================================================================*/
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Section", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//
            msg$2 = rb.getString("msg$2");//
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Unexpected topic ID format from URL: ";  
            msg$2 = "Error parsing how many posts has the topic: ";//
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static   
    
    /*******************************************************************************************************************
     * 
     * @param name
     * @param url
     * @param filename 
     * @param numberOfTopics 
     * @param lastPostTime 
     ******************************************************************************************************************/
    protected Section(
        final String name, 
        final String url, 
        final String filename, 
        final int numberOfPages,
        final String lastPostTime) {
        
        toolbox.log.Log.exec("phantom.pages", "Section", "Construtor de Section");
        toolbox.log.Log.param(name, url, filename, numberOfPages, lastPostTime);
        
        setPageName(name);
        setPageUrl(url);
        setPageFilename(filename);
        setNumberOfPages(numberOfPages);
        setLastPostDateTime(lastPostTime);        
        setPageParser(divForumbgParser);
 
        toolbox.log.Log.ret("phantom.pages", "Section", "Construtor de Section");

    }//construtor
    
/*======================================================================================================================
    Classe privada. Obtem dados de Topics em uma pagina de Section do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/   
private final class DivForumbgParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.isClass("forumbg")) return ulTopiclistTopicsParser;
        
        return null;        
    }
    
}//classe privada DivForumbgParser

private final class UlTopiclistTopicsParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.isClass("topics")) return liRowParser;
        
        return null;        
    }
    
    
}//classe privada DivForumbgParser

private final class LiRowParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception { 

        if (tag.isClass("row")) {

                tag.notifyClosing();
                return topicDataParser;
        
        }

        return null; 
        
    }//openTag
    
    @Override
    public void closeTag(Tag tag) {
        
        int numberOfPages = 
            ( Integer.parseInt(topicNumberOfPosts) / Page.MaxList.MAX_POSTS_PER_PAGE.get() ) + 1;

        addPage(
            
            new Topic(
                topicName, 
                topicURL, 
                topicFilename, 
                numberOfPages,
                topicLastPostTime
            ),
            numberOfPages
        );

        /*
        Anula campos para que o proximo objeto Section nao receba acidentalmente dados deste.
        */
        topicName = null;
        topicURL = null;
        topicFilename = null;
        topicNumberOfPosts = "0";
        topicLastPostTime = THE_VERY_FIRST_SECOND;   

    }//closeTag    
    
}//classe privada LiRowParser

private final class TopicDataParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.isClass("topictitle")) {
            
            topicURL = tag.getAttrMap().get("href");

            matcher = TOPIC_FILENAME_FINDER.matcher(topicURL);
            if (matcher.find()) 

                topicFilename = matcher.group(1);

            else 

                throw new XMLParseException(msg$1);

            tag.notifyClosing();
  
        }else if (tag.isClass("posts")) {
            
            tag.notifyClosing();
            
        }else if (tag.isClass("lastpost")) {
            
            return lastPostParser;
        }

        return null;
        
    }//openTag
    
    @Override
    public void closeTag(Tag tag) throws XMLParseException{
        
        if (tag.getTagId().equals("a")) { 
  
            topicName = tag.getTagContent();
                
        }else {
                
            matcher = NUMBER_OF_POSTS_FINDER.matcher(tag.getTagContent());

            if (matcher.find()) 

                topicNumberOfPosts = matcher.group();

            else 

                throw new XMLParseException(msg$2);                
            
        }
        
    }//closeTag
    
}//classe privada TopicDataParser  


private final class LastPostParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) {

        if (tag.getTagId().equals("time")) topicLastPostTime = tag.getAttrMap().get("datetime");

        return null;
    }
    
}//classe privada LastPostParser
    
}//classe Section

