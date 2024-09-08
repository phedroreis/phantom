package phantom.pages;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Reader {
    
    public static final int LEXICAL = 0;
    public static final int LAST_POST = 1;
    public static final int CREATION_DATE = 2;
    
    private static final Pattern PATTERN = Pattern.compile("\\d+");    
    
    private Main main;
    
    /**
     * 
     * @param orderBy
     * @return 
     * @throws XMLParseException
     * @throws IOException
     */
    public TreeSet<Page> readAllPages(final int orderBy) 
        throws XMLParseException, IOException, NullPointerException {
        
        LinkedList<Page> sectionsList = new LinkedList<>();
        LinkedList<Page> topicsList = new LinkedList<>();
        
        main = new phantom.pages.Main();

        LinkedList<Page> headersList = main.read();
        
        for (Page header : headersList) sectionsList.addAll(header.read());           
        
        for (Page section : sectionsList) topicsList.addAll(section.read());
       
        TreeSet<Page> topicsSet;
        
        switch (orderBy) {
            
            case 0:
                topicsSet = new TreeSet<>(new LexicalComparator());
                break;
            case 1:
                topicsSet = new TreeSet<>(new LastPostComparator());
                break;
            case 2:
                topicsSet = new TreeSet<>(new CreationDateComparator());
                break;
            default:
                topicsSet = new TreeSet<>(new LexicalComparator());
                            
        }//switch
        
        topicsSet.addAll(topicsList);
        
        return topicsSet;            
        
    }//readAllPages 
    
    public static String topicFilenameToUrl(final Page page) 
        throws IllegalArgumentException {
        
        if (!(page instanceof Topic)) throw new IllegalArgumentException();
        
        Matcher matcher = PATTERN.matcher(page.getPageFilename(0));
        matcher.find();
        
        return ROOT_URL + "viewtopic.php?t=" + matcher.group();  
        
    }//topicFilenameToUrl                                                                                                             
   
    private static class LexicalComparator implements Comparator<Page> {

        @Override
        public int compare(Page topic, Page otherTopic) {
            
            String topicName = toolbox.string.StringTools.normalizeToCompare(topic.getPageName());
            
            String otherTopicName = toolbox.string.StringTools.normalizeToCompare(otherTopic.getPageName());
            
            return topicName.compareTo(otherTopicName);
        }
        
    }
    
    private static class LastPostComparator implements Comparator<Page> {

        @Override
        public int compare(Page topic, Page otherTopic) {
 
            return 
                -topic.getDateTimeOfLastPostOnThisPage().compareTo(
                    otherTopic.getDateTimeOfLastPostOnThisPage()
                );
        }
        
    }  
    
    private static class CreationDateComparator implements Comparator<Page> {

        private static Matcher matcher;

        @Override
        public int compare(Page topic, Page otherTopic) {
            
            matcher = PATTERN.matcher(topic.getPageFilename(0));
            int a;
            matcher.find();
            a = Integer.parseInt(matcher.group());
            
            matcher = PATTERN.matcher(otherTopic.getPageFilename(0));
            int b;
            matcher.find();
            b = Integer.parseInt(matcher.group());            
            
            return b - a;            
        }       
    }
    
    public static void main(String[] args) throws XMLParseException, IOException {
        Reader r = new Reader();
        TreeSet<Page> tree = r.readAllPages(LEXICAL);
        for (Page page : tree) System.out.println(page);
    }

}//classe Reader
