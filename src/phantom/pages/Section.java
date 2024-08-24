package phantom.pages;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public final class Section extends Page {
    
    private static final Pattern FILENAME_FINDER = Pattern.compile("(t=\\d+?)\\D");
    
    private static final Pattern NUMBER_OF_POSTS_FINDER = Pattern.compile("Respostas: <strong>(\\d+?)<");

    /**
     * 
     * @param name
     * @param url
     * @param filename 
     * @param numberOfTopics 
     * @param lastPostTime 
     */
    protected Section(
        final String name, 
        final String url, 
        final String filename, 
        final String numberOfTopics,
        final String lastPostTime) {
        
        setName(name);
        setAbsoluteURL(url);
        setFilename(filename);
        setParser(new SectionPageParser());
        setNumberOfPages( (Integer.parseInt(numberOfTopics) / getMaxTopicsTitlesPerPage()) + 1 );
        setLastPostTime(lastPostTime);

    }//construtor

    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
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
    public void openTagLevel0(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        String attrValue = t.getAttrMap().get("class");
        
        if (tagName.equals("li") && attrValue != null && attrValue.startsWith("row bg")) 
                
            t.parseInnerScope();

    }//openTagLevel0
    
    @Override
    public void closeTagLevel0 (toolbox.xml.Tag t) throws XMLParseException {
        
        addPage(
            new Topic(
                topicName, 
                topicURL, 
                topicFilename, 
                topicNumberOfPosts, 
                topicLastPostTime
            )
        );
        
    }//closeTagLevel0    
    
    @Override
    public void openTagLevel1(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        String classValue = map.get("class");
        
        switch (tagName) {
            
            case "a"://a url de uma section localizada em uma tag a

                if (classValue != null && classValue.equals("topictitle")) {
                    
                    topicURL = map.get("href");
                
                    matcher = FILENAME_FINDER.matcher(topicURL);
                    
                    if (matcher.find()) 
                        
                        topicFilename = matcher.group(1);
                    
                    else 
                        
                        throw new XMLParseException("Formato inesperado de URL de topico.");
                    
                    t.notifyClosing();
  
                }                 
                
                break;
                
            case "time"://a data-hora da ultima postagem na Section
                
                topicLastPostTime = map.get("datetime"); 
                break;
                
            case "span": 
                
                if (classValue != null && classValue.equals("responsive-show left-box")) 
                
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
            case "span":

                matcher = NUMBER_OF_POSTS_FINDER.matcher(content);
                
                if (matcher.find())
                    
                    topicNumberOfPosts = matcher.group(1);
                
                else
                    
                    throw new XMLParseException("Num. de posts do topico nao encontrado");
        }

        
    }//closeTagLevel1 
    
}//classe privada SectionPageParser  

    public static void main(String[] args) throws XMLParseException, IOException {
        Section section = 
            new Section("Ceticismo", "./viewforum.php?f=17", "f=17", "56", "2024-08-11T00:01:02+00:00");
        java.util.LinkedList<Page> l = section.download();
        for (Page p : l) System.out.println(p);        
    }
    
}//classe Section
