package phantom.pages;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 22 de agosto de 2024
 **********************************************************************************************************************/
public final class Header extends Page {
    
    /*******************************************************************************************************************
     * 
     * @param url
     * @param filename
     * @param name 
     ******************************************************************************************************************/
    public Header(final String name, final String url, final String filename) {
        
        setName(name);
        setAbsoluteURL(url);
        setFilename(filename);
        setParser(new HeaderPageParser());
        
    }//construtor
    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/
private class HeaderPageParser extends toolbox.xml.TagParser {
    
    private Section section;
    
    @Override
    public void openTagLevel0(toolbox.xml.Tag t) throws XMLParseException {
        
        if (t.getTagName().equals("li")) {
            
            String attrValue = t.getAttrMap().get("class");

            if (attrValue != null && attrValue.startsWith("row forum-")) {
                
                String filename = 
                    "f=" + attrValue.substring(attrValue.indexOf('-') + 1, attrValue.length());
                
                section = new Section(filename);
                
                t.parseInnerScope();
                
            }//if
            
        }//if  
        
    }//openTagLevel0
    
    @Override
    public void closeTagLevel0 (toolbox.xml.Tag t) throws XMLParseException {
        
        Pattern p = Pattern.compile("<span class=\"dfn\">T.picos</span>: <span class=\"value\">(\\d+?)<");
        
        Matcher m = p.matcher(t.getContent());
        
        if (m.find()) 
            
            section.setNumberOfTopics(m.group(1));
        
        else 
            
            throw new XMLParseException("Formato invalido impede localizar n. de topicos de uma secao.");
        
        addPage(section);
        
    }//closeTagLevel0    
    
    @Override
    public void openTagLevel1(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        switch (tagName) {
            
            case "a"://a url de uma section localizada em uma tag a

                String classValue = map.get("class");

                if (classValue != null && classValue.equals("forumtitle")) {
                    
                    String href = map.get("href");
                    
                    section.setAbsoluteURL(href);
                    
                    t.notifyClosing();
  
                }                 
                
                break;
                
            case "time"://a data-hora da ultima postagem na Section
                
                String datetimeValue = map.get("datetime");
                
                section.setLastPostTime(datetimeValue);           
            
        }//switch 
        
    }//openTagLevel1
    
    @Override
    public void closeTagLevel1 (toolbox.xml.Tag t) {

        section.setName(t.getContent()); 
        
    }//closeTagLevel1 
    
}//classe privada HeaderPageParser

    public static void main(String[] args) throws XMLParseException, IOException {
        Header header = 
            new Header(
                "AVISOS E TESTES",
                "./viewforum.php?f=5&amp;sid=91847a0a2d024342c4e80b4055648c1a",
                "f=5"                
            );
        java.util.LinkedList<Page> l = header.download(1);
        for (Page p : l) System.out.println(p);        
    }
    
}//classe Header