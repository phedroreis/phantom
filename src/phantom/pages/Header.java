package phantom.pages;

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
    public Header(final String url, final String filename, final String name) {
        
        setAbsoluteURL(url);
        setFilename(filename);
        setName(name);
        setParser(new HeaderPageParser());
        
    }//construtor
    
/*======================================================================================================================
 Classe privada. O parsing localiza os dados de HEADERS na pagina principal do forum.   
======================================================================================================================*/
private class HeaderPageParser implements toolbox.xml.TagParser {
    
    @Override
    public void openTag(final toolbox.xml.Tag t) {
        
    }
    
    @Override
    public void closeTag(final toolbox.xml.Tag t) throws XMLParseException {
             
    }//closeTag
    
}//classe privada MainPageParser    
    
}//classe Header
