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
 * Classe que analisa, coleta, armazena e fornece dados de uma pagina de Header.
 * 
 * @author Pedro Reis
 * 
 * @since 1.0 - 22 de agosto de 2024
 * 
 * @version 1.0 
 **********************************************************************************************************************/
final class Header extends Page implements Comparable {
    
    private String sectionName;
    private String sectionURL;
    private String sectionFilename;
    private String sectionNumberOfTopics = "0";
    private String sectionLastPostTime = THE_VERY_FIRST_SECOND;
    
    private Matcher matcher;    
    
    private static final Pattern NUMBER_OF_TOPICS_FINDER = 
        Pattern.compile("<span class=\"dfn\">T.picos</span>: <span class=\"value\">(\\d+)");
    
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
            
            msg$1 = rb.getString("msg$1");//Unable to save backup's date-time 
            
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
        setPageParser(new HeaderParser());
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
private final class HeaderParser extends toolbox.html.TagParser {
    
    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        if (tag.getTagId().equals("ul") && tag.contains("class", "topiclist forums"))
            
            return new LiRowParser();
        
        return null;
        
    }
    
}//classe privada HeaderParser    
    
private final class LiRowParser extends toolbox.html.TagParser {

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
        
        int nTopics = Integer.parseInt(sectionNumberOfTopics);

        addPage(
            
            new Section(
                sectionName, 
                sectionURL, 
                sectionFilename, 
                sectionNumberOfTopics, 
                sectionLastPostTime
            ),
            nTopics == 0 ? 1 : ( (nTopics - 1)/ MaxList.MAX_TOPICS_TITLES_PER_PAGE.get() ) + 1 
        );

        /*
        Anula campos para que o proximo objeto Section nao receba acidentalmente dados deste.
        */
        sectionName = null;
        sectionURL = null;
        sectionFilename = null;
        sectionNumberOfTopics = "0";
        sectionLastPostTime = THE_VERY_FIRST_SECOND;   

    }//closeTag
    
}//classe privada LiRowParser

private final class LiInnerParser extends toolbox.html.TagParser {

    @Override
    public TagParser openTag(Tag tag) throws Exception {
        
        String tagId = tag.getTagId();
        
        HashMap<String, String> map;

        switch (tagId) {
            
            case "a":
                
                if (tag.contains("class", "forumtitle")) {
                    
                    map = tag.getAttrMap();

                    sectionURL = map.get("href");
                    sectionFilename = "f=" + map.get("data-id");
                    tag.notifyClosing();
                } 
                
                break;
                
            case "div":
                
                if (tag.contains("class", "forum-statistics")) tag.notifyClosing(); 
                
                break;
                
            case "time":
                
                map = tag.getAttrMap();
                
                sectionLastPostTime = map.get("datetime");    
                
                break;
            
        }

        return null;
        
    }//openTag
    
    @Override
    public void closeTag(Tag tag) throws XMLParseException{
        
        String tagId = tag.getTagId();
        
        switch (tagId) {
            
            case "a":      
                
                sectionName = tag.getTagContent();
                
                break;
                
            case "div":
                
                matcher = NUMBER_OF_TOPICS_FINDER.matcher(tag.getTagContent());
                
                if (matcher.find())
                    sectionNumberOfTopics = matcher.group(1);  
                else
                    throw new XMLParseException(msg$1);
        }
        
    }//closeTag    
    
}//classe privada ForumTitleParser
    
    
}//classe Header
