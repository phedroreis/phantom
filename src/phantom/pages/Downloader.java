package phantom.pages;

import java.io.IOException;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Downloader {
    
    private static final String FORMAT = "===== %s %s =====%n%n";
    
    private Main main;
   
    private String dateTimeOfLastPostFromLastBackup;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    private static String msg$5;
    private static String msg$6;  
    private static String msg$7;
    private static String msg$8;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Downloader", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");
            msg$2 = rb.getString("msg$2");
            msg$3 = rb.getString("msg$3");
            msg$4 = rb.getString("msg$4");
            msg$5 = rb.getString("msg$5");
            msg$6 = rb.getString("msg$6");
            msg$7 = rb.getString("msg$7");
            msg$8 = rb.getString("msg$8");
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "main page";
            msg$2 = "headers";
            msg$3 = "sections";
            msg$4 = "topics";
            msg$5 = "Downloading";
            msg$6 = "Parsing";  
            msg$7 = "<<<<NEW POSTS SINCE LAST BACKUP>>>>";
            msg$8 = "static files";
        }
        
    }//bloco static     

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
    public String getDateTimeOfLastPostFromThisBackup() {
        
        return main.getDateTimeOfLastPostOnThisPage();
        
    }//getDateTimeOfLastPostFromThisBackup

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
        
        System.out.printf(FORMAT, msg$5, msg$1);
        
        phantom.gui.GlobalComponents.TERMINAL.append(msg$5 + " " + msg$1 + "...\n");
        headersList = main.download();
        
        //O header que tiver o post mais recente, tera, obviamente, o post mais recente do forum
        main.setDateTimeOfLastPostOnThisPage(Page.getDateTimeOfLastestPostFromThisPageList(headersList));
        
        System.out.println(main);
        
        System.out.printf(FORMAT, msg$6, msg$2);
        
        int count = 0;
        phantom.gui.GlobalComponents.TERMINAL.append(msg$6 + " " + msg$2 + "...\n");        
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setMaximum(headersList.size());
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(0);
        for (Page header : headersList) {
       
            auxList = header.download();
            
            if (auxList != null) {
                sectionsList.addAll(auxList);
                System.out.println(msg$7);
            }
            
            System.out.println(header);       
            phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(++count);
            
        }//for
   
        if (sectionsList.isEmpty()) {
            System.out.printf(FORMAT, msg$5, msg$8);            
            return;
        } 
        System.out.printf(FORMAT, msg$5, msg$3);
        
        count = 0;
        phantom.gui.GlobalComponents.TERMINAL.append(msg$5 + " " + msg$3 + "...\n");
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setMaximum(sectionsList.size());
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(0);
        for (Page section : sectionsList) {
      
            auxList = section.download();
            
            if (auxList != null) {
                topicsList.addAll(auxList);
                System.out.println(section);
            }
                    
            phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(++count); 
            
        }//for    
   
        if (topicsList.isEmpty()) {
            System.out.printf(FORMAT, msg$5, msg$8);            
            return;
        } 
        System.out.printf(FORMAT, msg$5, msg$4);
        
        count = 0;
        phantom.gui.GlobalComponents.TERMINAL.append(msg$5 + " " + msg$4 + "...\n");
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setMaximum(topicsList.size());
        phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(0);
        for (Page topic : topicsList) {
       
            if (topic.download() != null) System.out.println(topic); 
            
            phantom.gui.GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR.setValue(++count);                    
        }//for  
        
        System.out.printf(FORMAT, msg$5, msg$8);
       
    }//downloadAllPages

}//classe Downloader
