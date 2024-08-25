package phantom.main;

import java.io.IOException;
import javax.management.modelmbean.XMLParseException;
import phantom.pages.Page;

/**
 *
 * @author Pedro Reis
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws XMLParseException
     * @throws IOException
     */
    public static void main(String[] args) throws XMLParseException, IOException{
   
        phantom.log.Log.createLogFile();
        
        toolbox.log.Log.exec("phantom.main", "Main", "main");
        
        java.util.LinkedList<Page> headersList;
        java.util.LinkedList<Page> sectionsList = new java.util.LinkedList<>();
        java.util.LinkedList<Page> topicsList = new java.util.LinkedList<>();
        
        phantom.pages.Main main = new phantom.pages.Main();
        
        System.out.println(main);
        headersList = main.download();
        
        main.setLastPostTime(Page.getForumLastPostTime(headersList));
        System.out.println(main);
        
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
        
        toolbox.log.Log.println("Baixando paginas de topicos...");
        for (Page topic : topicsList) {
            
            System.out.println(topic);
            //topic.download();
            
        }
        
        toolbox.log.Log.ret("phantom.main", "Main", "main");
        toolbox.log.Log.closeFile();
    }
    
}
