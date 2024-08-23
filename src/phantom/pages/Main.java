package phantom.pages;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 18 de agosto de 2024
 **********************************************************************************************************************/
public final class Main extends Page {
    
    /*
     * 
     */
    private static final Pattern HEADER_PATTERN = 
        Pattern.compile("<a.+?href=\"(.+?php\\?(.=\\d+).*?)\".*?>(.+?)</a>");    
    
    /*******************************************************************************************************************
     * Construtor da classe
     ******************************************************************************************************************/
    public Main() {
        
        setName("Principal");
        setFilename(Page.FORUM_NAME);
        setAbsoluteURL("./");
        setParser(new MainPageParser());
        
    }//construtor
 
/*======================================================================================================================
         Classe privada. O parsing localiza os dados de HEADERS na pagina principal do forum.   
======================================================================================================================*/
private class MainPageParser extends toolbox.xml.TagParser {
    
    @Override
    public void openTag(final toolbox.xml.Tag t) {
        
        if (t.getTagName().equals("li")) {
            
            String attrValue = t.getAttrMap().get("class");

            if (attrValue != null && attrValue.equals("header")) t.notifyClosing();
            
        }//if
        
    }//openTag
    
    @Override
    public void closeTag(final toolbox.xml.Tag t) throws XMLParseException {
        
        Matcher m = HEADER_PATTERN.matcher(t.getContent());
        
        if (!m.find()) throw new XMLParseException("Erro ao tentar localizar os dados de um HEADER");
        
        addPage(new Header(m.group(1), m.group(2), m.group(3)));
             
    }//closeTag
    
}//classe privada MainPageParser 

    public static void main(String[] args) throws XMLParseException, IOException {
        Main main = new Main();
        java.util.LinkedList<Page> l = main.download(1);
        for (Page p : l) System.out.println(p);        
    }

}//classe Main
