package phantom.pages;

import java.io.IOException;
import java.util.HashMap;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public final class Header extends Page {
    
    /*******************************************************************************************************************
     * 
     * @param url
     * @param filename
     * @param name 
     ******************************************************************************************************************/
    public Header(final String name, final String url, final String filename) {
        
        setAbsoluteURL(url);
        setFilename(filename);
        setName(name);
        setParser(new HeaderPageParser());
        
    }//construtor
    
/*======================================================================================================================
    Classe privada. Obtem dados de Sections em uma pagina de Header do forum a partir da tag li
    que exibe estes dados na pagina.
======================================================================================================================*/
private class HeaderPageParser implements toolbox.xml.TagParser {
    
    private Section section;  
    
    @Override
    public void openTag(final toolbox.xml.Tag t) {
        
        if (t.getTagName().equals("li")) {
            
            String attrValue = t.getAttrMap().get("class");

            if (attrValue != null && attrValue.startsWith("row forum-")) {
                
                String filename = 
                    "f=" + attrValue.substring(attrValue.indexOf('-') + 1, attrValue.length());
                
                section = new Section(filename);
                
                t.notifyClosing();
            }
            
        }//if        
        
    }//openTag
    
    @Override
    public void closeTag(final toolbox.xml.Tag t) throws XMLParseException {
       
        toolbox.xml.HtmlParser htmlParser = 
            new toolbox.xml.HtmlParser(t.getContent(), new TagScopeParser(section));
        
        htmlParser.parse();
        
        addPage(section);
             
    }//closeTag
    
}//classe privada HeaderPageParser 

/*
   Classe privada. Obtem dados restantes da Section analisando tags no escopo da tag li que exibe os
   dados das Sections de um determinado Header.
*/
private class TagScopeParser implements toolbox.xml.TagParser {
    
    private Section section; 
    
    public TagScopeParser(final Section s) {
        
        section = s;
        
    }//construtor
    
    @Override
    public void openTag(final toolbox.xml.Tag t) {
        
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
        
    }//openTag
    
    @Override
    public void closeTag(final toolbox.xml.Tag t) throws XMLParseException {
        
        /*
        O nome da Section e o escopo da tag a localizada no metodo openTag()
        */
        section.setName(t.getContent());
             
    }//closeTag
    
}//classe privada TagScopeParser 

    public static void main(String[] args) throws XMLParseException, IOException {
        Header header = 
            new Header(
                "./viewforum.php?f=5&amp;sid=91847a0a2d024342c4e80b4055648c1a",
                "f=5",
                "AVISOS E TESTES"
            );
        java.util.LinkedList<Page> l = header.download();
        for (Page p : l) System.out.println(p);        
    }
    
}//classe Header