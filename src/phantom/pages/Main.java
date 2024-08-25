package phantom.pages;

import java.io.IOException;
import java.util.HashMap;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 18 de agosto de 2024
 **********************************************************************************************************************/
public final class Main extends Page {
       
    /*******************************************************************************************************************
     * Construtor da classe
     ******************************************************************************************************************/
    public Main() {
        
        toolbox.log.Log.exec("phantom.pages", "Main", "Construtor de Main");
        
        setName("Principal");
        setFilename(Page.FORUM_NAME);
        setAbsoluteURL("./");
        setParser(new MainPageParser());
        setNumberOfPages(1);
 
        toolbox.log.Log.ret("phantom.pages", "Main", "Construtor de Main");
        
    }//construtor
 
/*======================================================================================================================
         Classe privada. O parsing localiza os dados de HEADERS na pagina principal do forum.   
======================================================================================================================*/
private class MainPageParser extends toolbox.xml.TagParser {
    
    private String headerName;
    private String headerURL;
    private String headerFilename;
    private String lastPostTime = null;
    
    @Override
    public void openTagLevel0(final toolbox.xml.Tag t) {
        
        String classValue = t.getAttrMap().get("class");
        
        switch (t.getTagName()) {
     
            case "li":

                if (classValue != null && classValue.equals("header")) 

                   t.parseInnerScope();
           
                break;
                
            case "ul":
                
                if (classValue != null && classValue.equals("topiclist forums")) 

                   t.parseInnerScope();              
                
        }//switch
        
    }//openTagLevel0
    
    @Override
    public void closeTagLevel0(final toolbox.xml.Tag t) throws XMLParseException {
        
        if (t.getTagName().equals("ul")) {
 
            addPage(new Header(headerName, headerURL, headerFilename, lastPostTime));
            
            lastPostTime = null;
        }   
        
    }//closeTagLevel0
    
   @Override
    public void openTagLevel1(final toolbox.xml.Tag t) {
        
        HashMap<String, String> map = t.getAttrMap(); 
        
        switch (t.getTagName()) {
            
            case "a":
  
                headerURL = map.get("href");
                headerFilename = "f=" + map.get("data-id");
                t.notifyClosing();                 
                break;
                
            case "li": 
                
                String classValue = map.get("class");  
                
                if (classValue != null && classValue.startsWith("row forum-")) 
                    
                    t.parseInnerScope();
                
        }//switch
        
    }//openTag
    
    @Override
    public void closeTagLevel1(final toolbox.xml.Tag t) throws XMLParseException {
        
        if (t.getTagName().equals("a")) headerName = t.getContent();
             
    }//closeTagLevel1

    @Override
    public void openTagLevel2(final toolbox.xml.Tag t) throws XMLParseException {
        
        if (t.getTagName().equals("time")) {
            
            String datetimeValue = t.getAttrMap().get("datetime");
            
            if (datetimeValue != null) {
                
                if (lastPostTime == null) 
                    
                    lastPostTime = datetimeValue;
                
                else if (datetimeValue.compareTo(lastPostTime) > 0) lastPostTime = datetimeValue;
            }            
            
        }
             
    }//openTagLevel2
    
}//classe privada MainPageParser 

    public static void main(String[] args) throws XMLParseException, IOException {
        
        phantom.log.Log.createLogFile();
        
        Main main = new Main();
        
        java.util.LinkedList<Page> headersList = main.download();
        
        for (Page header : headersList) System.out.println(header);        
        
        System.out.println(Page.getForumLastPostTime(headersList));        
    }

}//classe Main
