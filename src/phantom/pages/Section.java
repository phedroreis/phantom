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
    
    private int numberOfTopics;

    /**
     * 
     * @param filename 
     */
    public Section(final String filename) {
        
        setFilename(filename);
        setParser(new SectionPageParser());

    }//construtor
    
    public void setNumberOfTopics(final String n) {
        
        numberOfTopics = Integer.parseInt(n);
        
    }//setNumberOftopics
    
    public int getNumberOfTopics() {
        
        return numberOfTopics;
        
    }//getNumberOfTopics
    
    public int getNumberOfPages() {
        
        return (getNumberOfTopics() / getMaxTopicsTitlesPerPage()) + 1;
        
    }//getNumberOfPages
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
         
        return String.format("%s%n%s%n%s", numberOfTopics, getLastPostTime(), super.toString());       
        
    }//toString    
    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/
private class SectionPageParser extends toolbox.xml.TagParser {
    
    private Topic topic;
    
    @Override
    public void openTagLevel0(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        String attrValue = t.getAttrMap().get("class");
        
        if (tagName.equals("li")) {

            if (attrValue != null && attrValue.startsWith("row bg")) {
                
                topic = new Topic();
                
                t.parseInnerScope();
                
            }//if
            
        }//if        
        
    }//openTagLevel0
    
    @Override
    public void closeTagLevel0 (toolbox.xml.Tag t) throws XMLParseException {
        
        addPage(topic);
        
    }//closeTagLevel0    
    
    @Override
    public void openTagLevel1(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        switch (tagName) {
            
            case "a"://a url de uma section localizada em uma tag a

                String classValue = map.get("class");

                if (classValue != null && classValue.equals("topictitle")) {
                    
                    String href = map.get("href");
                    
                    topic.setAbsoluteURL(href);
                    
                    Pattern p = Pattern.compile("(t=\\d+?)\\D");
                    
                    Matcher m = p.matcher(href);
                    
                    if (m.find()) 
                        topic.setFilename(m.group(1));
                    else 
                        throw new XMLParseException("Formato inesperado de URL de topico.");
                    
                    t.notifyClosing();
  
                }                 
                
                break;
                
            case "time"://a data-hora da ultima postagem na Section
                
                String datetimeValue = map.get("datetime");
                
                topic.setLastPostTime(datetimeValue);           
            
        }//switch 
        
    }//openTagLevel1
    
    @Override
    public void closeTagLevel1 (toolbox.xml.Tag t) {

        topic.setName(t.getContent()); 
        
    }//closeTagLevel1 
    
}//classe privada SectionPageParser  

    public static void main(String[] args) throws XMLParseException, IOException {
        Section section = new Section("f=17");
        section.setAbsoluteURL("./viewforum.php?f=17");
        section.setName("Ceticismo");
        section.setNumberOfTopics("56");
        System.out.println("num. de topicos" + section.getNumberOfTopics());
        java.util.LinkedList<Page> l = section.download(section.getNumberOfPages());
        for (Page p : l) System.out.println(p);        
    }
    
}//classe Section
