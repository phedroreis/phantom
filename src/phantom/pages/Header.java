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
    
    private static final Pattern PATTERN = 
        Pattern.compile("<span class=\"dfn\">T.picos</span>: <span class=\"value\">(\\d+?)<");
    
    /*******************************************************************************************************************
     * 
     * @param url
     * @param filename
     * @param name 
     ******************************************************************************************************************/
    protected Header(final String name, final String url, final String filename) {
        
        setName(name);
        setAbsoluteURL(url);
        setFilename(filename);
        setParser(new HeaderPageParser());
        setNumberOfPages(1);
        
    }//construtor
    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/
private class HeaderPageParser extends toolbox.xml.TagParser {
    
    private String sectionName;
    private String sectionURL;
    private String sectionFilename;
    private String sectionNumberOfTopics;
    private String sectionLastPostTime;
    
    @Override
    public void openTagLevel0(toolbox.xml.Tag t) throws XMLParseException {
        
        if (t.getTagName().equals("li")) {
            
            String attrValue = t.getAttrMap().get("class");

            if (attrValue != null && attrValue.startsWith("row forum-")) {
                
                sectionFilename = 
                    "f=" + attrValue.substring(attrValue.indexOf('-') + 1, attrValue.length());
                
                t.parseInnerScope();
                
            }//if
            
        }//if  
        
    }//openTagLevel0
    
    @Override
    public void closeTagLevel0 (toolbox.xml.Tag t) throws XMLParseException {
       
        Matcher m = PATTERN.matcher(t.getContent());
        
        if (m.find()) 
            
            sectionNumberOfTopics = m.group(1);
        
        else 
            
            throw new XMLParseException("Formato invalido impede localizar n. de topicos de uma secao.");
        
        addPage(
            new Section(
                sectionName, 
                sectionURL, 
                sectionFilename, 
                sectionNumberOfTopics, 
                sectionLastPostTime
            )
        );
        
    }//closeTagLevel0    
    
    @Override
    public void openTagLevel1(toolbox.xml.Tag t) throws XMLParseException {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        switch (tagName) {
            
            case "a"://a url de uma section localizada em uma tag a

                String classValue = map.get("class");

                if (classValue != null && classValue.equals("forumtitle")) {
                    
                    sectionURL = map.get("href");
                    
                    t.notifyClosing();
  
                }                 
                
                break;
                
            case "time"://a data-hora da ultima postagem na Section
                
                sectionLastPostTime = map.get("datetime");        
            
        }//switch 
        
    }//openTagLevel1
    
    @Override
    public void closeTagLevel1 (toolbox.xml.Tag t) {

        sectionName = t.getContent(); 
        
    }//closeTagLevel1 
    
}//classe privada HeaderPageParser

    public static void main(String[] args) throws XMLParseException, IOException {
        Header header = 
            new Header(
                "AVISOS E TESTES",
                "./viewforum.php?f=5&amp;sid=91847a0a2d024342c4e80b4055648c1a",
                "f=5"                
            );
        java.util.LinkedList<Page> l = header.download();
        for (Page p : l) System.out.println(p);        
    }
    
}//classe Header