package phantom.pages;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    
    private String topicName;
    private String topicURL;
    private String topicFilename;
    private String topicNumberOfPosts = "0";
    private String topicLastPostTime = THE_VERY_FIRST_SECOND; 
    
    private Matcher matcher;    
    
    private static final Pattern FILENAME_FINDER = Pattern.compile("t=\\d+");
    
    private static final Pattern NUMBER_OF_POSTS_FINDER = Pattern.compile("\\d+");
    
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
        final String numberOfTopics,
        final String lastPostTime) {
        
        toolbox.log.Log.exec("phantom.pages", "Section", "Construtor de Section");
        toolbox.log.Log.param(name, url, filename, numberOfTopics, lastPostTime);
        
        setPageName(name);
        setPageUrl(url);
        setPageFilename(filename);
        setPageParser(new SectionParser());
        
        int nTopics = Integer.parseInt(numberOfTopics);
        if (nTopics == 0)
            setNumberOfPages(1);
        else
            setNumberOfPages( ( (nTopics - 1)/ MaxList.MAX_TOPICS_TITLES_PER_PAGE.get() ) + 1 );
        
        setLastPostDateTime(lastPostTime);

        toolbox.log.Log.ret("phantom.pages", "Section", "Construtor de Section");

    }//construtor
    
/*======================================================================================================================
    Classe privada. Obtem dados de Topics em uma pagina de Section do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/   
private final class SectionParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.getTagId().equals("ul") && tag.contains("class", "topiclist topics"))
            
            return new LiRowParser();
        
        return null;
        
    }
    
}//classe privada SectionParser   

private class LiRowParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception { 

        if (tag.getTagId().equals("li")) {

            String classValue = tag.getAttrMap().get("class");

            if (classValue != null && classValue.startsWith("row")) {

                tag.notifyClosing();
                return new LiInnerParser();
            } 
        }

        return null; 
        
    }//openTag
    
    @Override
    public void closeTag(Tag tag) {

        addPage(
            
            new Topic(
                topicName, 
                topicURL, 
                topicFilename, 
                topicNumberOfPosts, 
                topicLastPostTime
            ),
            ( Integer.parseInt(topicNumberOfPosts) / MaxList.MAX_POSTS_PER_PAGE.get() ) + 1 
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


private class LiInnerParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {
         
        HashMap<String, String> map;

        switch (tag.getTagId()) {
            
            case "a":
                
                if (tag.contains("class", "topictitle")) {
                    
                    map = tag.getAttrMap();

                    topicURL = map.get("href");
                    
                    matcher = FILENAME_FINDER.matcher(topicURL);
                    if (matcher.find()) 
                        
                        topicFilename = matcher.group();
                    
                    else 
                        
                        throw new XMLParseException(msg$1);
                    
                    tag.notifyClosing();
                } 
                
                break;
                
            case "dd":
                
                if (tag.contains("class", "posts")) 
                    
                    tag.notifyClosing(); 
                
                else if (tag.contains("class", "lastpost"))
                    
                    return new LastPostParser();
                
                break;            
        }

        return null;
        
    }//openTag
    
    @Override
    public void closeTag(Tag tag) throws XMLParseException{
        
        switch (tag.getTagId()) { 
            
            case "a":
        
                topicName = tag.getTagContent();
                
                break;
                
            case "dd":
                
                matcher = NUMBER_OF_POSTS_FINDER.matcher(tag.getTagContent());
                
                if (matcher.find()) 
                
                    topicNumberOfPosts = matcher.group();
                
                else 

                    throw new XMLParseException(msg$2);                
            
        }
        
    }//closeTag
    
}//classe privada LiInnerParser  


private final class LastPostParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {

        if (tag.getTagId().equals("time")) topicLastPostTime = tag.getAttrMap().get("datetime");

        return null;
    }
    
}//classe privada LastPostParser
    
}//classe Section
