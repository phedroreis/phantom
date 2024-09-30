package phantom.pages;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;

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
        setPageParser(new SectionPageParser());
        
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
private class SectionPageParser extends toolbox.xml.TagParser {
    
    private String topicName;
    private String topicURL;
    private String topicFilename;
    private String topicNumberOfPosts;
    private String topicLastPostTime; 
    
    private Matcher matcher;
    
    @Override
    public void openTagLevel0(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();
        
        String attrValue = t.getAttrMap().get("class");
        
        if (tagName.equals("li") && attrValue != null && attrValue.startsWith("row bg")) 
                
            t.parseInnerScope();

    }//openTagLevel0
    
    @Override
    public void closeTagLevel0 (toolbox.xml.Tag t) {
        
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
        Anula campos para que o proximo objeto Topic nao receba acidentalmente dados deste.
        */
        topicName = null;
        topicURL = null;
        topicFilename = null;
        topicNumberOfPosts = null;
        topicLastPostTime = null;
        
    }//closeTagLevel0    
    
    @Override
    public void openTagLevel1(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        String classValue = map.get("class");
        
        switch (tagName) {
            
            case "a"://a url de um topic localizada em uma tag a

                if (classValue != null && classValue.equals("topictitle")) {
                    
                    topicURL = map.get("href");
                
                    matcher = FILENAME_FINDER.matcher(topicURL);
                    
                    if (matcher.find()) 
                        
                        topicFilename = matcher.group();
                    
                    else 
                        
                        throw new XMLParseException(msg$1 + topicURL);
                 
                    t.notifyClosing();
  
                }//if                 
                
                break;
                
            case "time"://a data-hora da ultima postagem na Section
                
                topicLastPostTime = map.get("datetime"); 
                break;
                
            case "dd": 
                
                if (classValue != null && classValue.equals("posts")) 
                
                    t.notifyClosing();               
            
        }//switch 
        
    }//openTagLevel1
    
    @Override
    public void closeTagLevel1 (toolbox.xml.Tag t) throws XMLParseException {
        
        String content = t.getContent();
        
        switch (t.getTagName()) {
            
            case "a":
                
                topicName = content;
                break;
                
            case "dd":

                matcher = NUMBER_OF_POSTS_FINDER.matcher(content);
                
                if (matcher.find())
                    
                    topicNumberOfPosts = matcher.group();
                
                else
                    
                    throw new XMLParseException(msg$2 + content);
                
        }//switch
        
    }//closeTagLevel1 
    
}//classe privada SectionPageParser  
    
}//classe Section
