package phantom.pages;

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
public final class Header extends Page implements Comparable {
    
    private static final Pattern NUMBER_OF_TOPICS_FINDER = 
        Pattern.compile("<span class=\"dfn\">T.picos</span>: <span class=\"value\">(\\d+?)<");
    
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
        toolbox.log.Log.param(name, url, filename);
        
        setPageName(name);
        setPageUrl(url);
        setPageFilename(filename);
        setPageParser(new HeaderPageParser());
        setDateTimeOfLastPostOnThisPage(lastPostTime);
        setNumberOfPages(1);
        
        toolbox.log.Log.println(this.toString());
        toolbox.log.Log.ret("phantom.pages", "Header", "Construtor de Header");
        
    }//construtor

    /**
     * 
     * @param t
     * @return 
     */
    @Override
    public int compareTo(Object t) {
        
        return getDateTimeOfLastPostOnThisPage().compareTo( ((Header)t).getDateTimeOfLastPostOnThisPage() );
        
    }//compareTo
    
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
    
    private Matcher matcher;
    
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
        
        String tagLiContent = t.getContent();
       
        matcher = NUMBER_OF_TOPICS_FINDER.matcher(tagLiContent);
        
        if (matcher.find()) 
            
            sectionNumberOfTopics = matcher.group(1);
        
        else 
            
            throw new XMLParseException("Error parsing how many topics has the section:\n" + tagLiContent);
        
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
    
}//classe Header