package phantom.pages;

import java.io.IOException;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author 
 * @since
 * @version
 */
public class PageTest {

    public PageTest() {

    }//construtor
    
    public static void main(String[] args) throws XMLParseException, IOException {
        
        java.util.LinkedList<Page> headersList;
        java.util.LinkedList<Page> sectionsList = new java.util.LinkedList<>();
        java.util.LinkedList<Page> topicsList = new java.util.LinkedList<>();
        
        Main main = new Main();
        
        System.out.println(main);
        headersList = main.download();
        
        System.out.println("\n=======================================================================\n");
        
        for (Page header : headersList) {
            
            System.out.println(header);
            sectionsList.addAll(header.download());
        }
        
        System.out.println("\n=======================================================================\n");
        
        for (Page section : sectionsList) {
            
            System.out.println(section);
            topicsList.addAll(section.download());
        }
        
        System.out.println("\n=======================================================================\n");
        
        for (Page topic : topicsList) {
            
            System.out.println(topic);
            topic.download();
            
        }
    }

}//classe PageTest
