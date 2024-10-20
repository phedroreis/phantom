package phantom.pages;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * 
 * @since 1.0
 * 
 * @version 1.0
 **********************************************************************************************************************/
public final class Reader {
    
    private static final int LEXICAL = 0;
    private static final int LAST_POST = 1;
    private static final int CREATION_DATE = 2;
    
    private static final Pattern TOPIC_INDEX_FINDER = Pattern.compile("\\d+");    
    
    private Main main;
    
    /*******************************************************************************************************************
     * 
     * @param orderBy
     * @return 
     * @throws XMLParseException
     * @throws IOException
     ******************************************************************************************************************/
    public TreeSet<Page> readAllPages(final int orderBy) throws Exception {
        
        List<Page> sectionsList = new LinkedList<>();
        List<Page> topicsList = new LinkedList<>();
        
        main = new Main();

        List<Page> headersList = main.read();
       
        for (Page header : headersList) sectionsList.addAll(header.read());  
      
        for (Page section : sectionsList) topicsList.addAll(section.read());
       
        TreeSet<Page> topicsSet;
        
        switch (orderBy) {
            
            case LEXICAL:
                topicsSet = new TreeSet<>(new LexicalComparator());
                break;
            case LAST_POST:
                topicsSet = new TreeSet<>(new LastPostComparator());
                break;
            case CREATION_DATE:
                topicsSet = new TreeSet<>(new CreationDateComparator());
                break;
            default:
                topicsSet = new TreeSet<>(new LexicalComparator());
                            
        }//switch
        
        topicsSet.addAll(topicsList);
        
        return topicsSet;            
        
    }//readAllPages 
    
    /**
     * 
     * @param page
     * @return
     * @throws IllegalArgumentException 
     */
    public static String topicFilenameToUrl(final Page page) 
        throws IllegalArgumentException {
        
        if (!(page instanceof Topic)) throw new IllegalArgumentException();
        
        Matcher matcher = TOPIC_INDEX_FINDER.matcher(page.getPageFilename(0));
        matcher.find();
        
        return ROOT_URL + "viewtopic.php?t=" + matcher.group();  
        
    }//topicFilenameToUrl 
    
/*======================================================================================================================
*    
======================================================================================================================*/
private static final class LexicalComparator implements Comparator<Page> {

    @Override
    public int compare(Page topic, Page otherTopic) {
        
        return toolbox.string.StringTools.compare(
            topic.getPageName().replaceAll("&([a-z]+|#\\d+);", ""), 
            otherTopic.getPageName().replaceAll("&([a-z]+|#\\d+);", "")
        );
    }

}//classe privada LexicalComparator

/*======================================================================================================================
*
======================================================================================================================*/
private static final class LastPostComparator implements Comparator<Page> {

    @Override
    public int compare(Page topic, Page otherTopic) {

        return 
            -topic.getLastPostDateTime().compareTo(
                otherTopic.getLastPostDateTime()
            );
    }

}//LastPostComparator  

/*======================================================================================================================
*
======================================================================================================================*/
private static class CreationDateComparator implements Comparator<Page> {

    private static Matcher matcher;

    @Override
    public int compare(Page topic, Page otherTopic) {

        matcher = TOPIC_INDEX_FINDER.matcher(topic.getPageFilename(0));
        int a;
        matcher.find();
        a = Integer.parseInt(matcher.group());

        matcher = TOPIC_INDEX_FINDER.matcher(otherTopic.getPageFilename(0));
        int b;
        matcher.find();
        b = Integer.parseInt(matcher.group());            

        return b - a;            
    }   
    
}//CreationDateComparator

}//classe Reader
