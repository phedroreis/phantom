package phantom.pages;

import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeSet;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Downloader {
    
    private Main main;
   
    private String dateTimeOfLastPostFromLastBackup;
    
    private TreeSet<Page> topicsOrderedList;

    /**
     * 
     * @param lastPostDateTime 
     */
    public Downloader(final String lastPostDateTime) {
        
        dateTimeOfLastPostFromLastBackup = lastPostDateTime;

    }//construtor

    /**
     * 
     * @return 
     */
    public Main getMain() {
        
        return main;
        
    }//getMain

    /**
     * 
     * @return 
     */
    public String getDateTimeOfLastPostFromThisBackup() {
        
        return main.getDateTimeOfLastPostOnThisPage();
        
    }//getDateTimeOfLastPostFromThisBackup

    /**
     * 
     * @return 
     */
    public TreeSet<Page> getTopicsOrderedList() {
        
        return topicsOrderedList;
        
    }//getTopicsOrderedList

    /**
     * 
     * @throws XMLParseException
     * @throws IOException
     */
    public void downloadAllPages() throws XMLParseException, IOException {
        
        Page.setDateTimeOfLastPostFromLastBackup(dateTimeOfLastPostFromLastBackup);
        
        LinkedList<Page> headersList;
        LinkedList<Page> sectionsList = new LinkedList<>();
        LinkedList<Page> topicsList = new LinkedList<>();
        
        LinkedList<Page> auxList;
        
        main = new phantom.pages.Main();
        
        System.out.println(main);
        headersList = main.download();
        
        //O header que tiver o post mais recente, tera, obviamente, o post mais recente do forum
        main.setDateTimeOfLastPostOnThisPage(Page.getDateTimeOfLastestPostFromThisPageList(headersList));
        System.out.println(main);
        
        System.out.println("\n=======================================================================\n");
        
        for (Page header : headersList) {
            
            System.out.println(header);
            
            auxList = header.download();
            
            if (auxList != null) 
                sectionsList.addAll(auxList);
            else
                System.out.println("Nao baixado!\n\n");
        }
        
        System.out.println("\n=======================================================================\n");
        
        for (Page section : sectionsList) {
            
            System.out.println(section);
           
            auxList = section.download();
            
            if (auxList != null) 
                topicsList.addAll(auxList);
            else
                System.out.println("Nao baixado!\n\n");            
        }
        
        System.out.println("\n=======================================================================\n");
        
        toolbox.log.Log.println("Baixando paginas de topicos...");
        for (Page topic : topicsList) {
            
            System.out.println(topic);
            //topic.download();
            
        }        
        
    }

}//classe Downloader
