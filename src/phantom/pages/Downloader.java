package phantom.pages;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;


/**
 *
 * @author Pedro Reis
 * 
 * @since 1.0 
 * 
 * @version 1.0
 */
public final class Downloader {
    
    private Main main;
   
    private String dateTimeOfLastPostFromLastBackup;
    
    private final phantom.gui.CustomProgressBar htmlProgressBar;
    
    private final phantom.gui.Terminal terminal;
    
    private final JRadioButton privateAreaRadioButton;
    
    private int totalPagesInPagesList;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    private static String msg$5;
    private static String msg$6;  
    private static String msg$7;     
    private static String msg$8;
    private static String msg$9;  
    private static String msg$10; 

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
            msg$9 = rb.getString("msg$9");
            msg$10 = rb.getString("msg$10");              
           
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Downloading main page and getting headers list";
            msg$2 = "Downloading headers and getting sections list";
            msg$3 = "Dowloaging sections and getting topics list";
            msg$4 = "Downloagind topics";
            msg$5 = "Gabarito, the restricted area is close!";
            msg$6 = "Most recent post :"; 
            msg$7 = "No new posts since the last backup";  
            msg$8 = "%,d headers (%,d pages)";
            msg$9 = "%,d sections (%,d pages)";
            msg$10 = "%,d topics (%,d pages)";             
            
           
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static     

    /**
     * 
     * @param lastPostDateTime 
     */
    public Downloader(final String lastPostDateTime) {
        
        dateTimeOfLastPostFromLastBackup = lastPostDateTime;
 
        htmlProgressBar = phantom.gui.MainFrame.getHtmlProgressBarReference();
        terminal = phantom.gui.MainFrame.getTerminalReference();
        privateAreaRadioButton = phantom.gui.MainFrame.getPrivateAreaRadioButtonReference();
        
        totalPagesInPagesList = 1;

    }//construtor
    
    /*
    *
    */
    private void printMessages(final String msg) {
        
        System.out.printf(FORMAT, msg);
  
        terminal.appendln(msg);  
 
        toolbox.log.Log.println("**** " + msg + " ****");           
        
    }//printMessages
    
    /*
    *
    */
    private List<Page> downloadPagesList(
        final String msg,
        final List<Page> PagesList
    ) throws Exception {
        
        List<Page> auxList;  
        
        List<Page> mergeList = new LinkedList<>();
        
        printMessages(msg);        
 
        htmlProgressBar.setMaximum(totalPagesInPagesList);
        htmlProgressBar.resetCounter();  
        totalPagesInPagesList = 0;
     
        for (Page page : PagesList) {
       
            auxList = page.download(dateTimeOfLastPostFromLastBackup);

            if (auxList != null) {
                
                totalPagesInPagesList += page.getTotalNumberOfPagesInPagesList();
                mergeList.addAll(auxList);
            }  
            
        }//for 
        
        return mergeList;
        
    }//downloadPagesList

    /**
     * 
     * @throws Exception
     */
    public void downloadAllPages() throws Exception {
        
        toolbox.log.Log.exec("phantom.pages", "Downloader", "downloadAllPages");
        
        List<Page> headersList;
        List<Page> sectionsList;
        List<Page> topicsList;
                          
        main = new phantom.pages.Main(); 

        printMessages(msg$1);        
        
        htmlProgressBar.setMaximum(totalPagesInPagesList); 
                          
        headersList = main.download(dateTimeOfLastPostFromLastBackup);              
        
        if (privateAreaRadioButton.isSelected()) (new PrivateHeaders()).removeNonPrivateHeaders(headersList);
  
        
        if (headersList.isEmpty()) {
            
            System.out.println(msg$5 + toolbox.string.StringTools.NEWLINE);//Gabarito, the restricted area is closed!
            
            terminal.appendln(msg$5); 
            
            phantom.exception.ExceptionTools.crashMessage(null, new NullPointerException(msg$5));
        }

        //O header que tiver o post mais recente, tera, obviamente, o post mais recente do forum
        main.setDateTimeOfLastPostOnThisPage(
            Page.getDateTimeOfLastestPostFromThisPageList(headersList)        
        ); 
        
        printMessages(msg$6 + " " + main.getDateTimeOfLastPostOnThisPage());
        
        phantom.threads.ThreadsMonitor.setDateTimeOfLastPostFromLastBackup(
            main.getDateTimeOfLastPostOnThisPage()
        );
        
        totalPagesInPagesList = main.getTotalNumberOfPagesInPagesList();
        
        String headersInfo = 
            String.format(toolbox.locale.Localization.getLocale(), msg$8, headersList.size(), totalPagesInPagesList);

        terminal.appendln(headersInfo);
   
        sectionsList = downloadPagesList(msg$2, headersList);        
 
        if (sectionsList.isEmpty()) {
            
            printMessages(msg$7);
            return;
            
        } 
        
        String sectionsInfo = 
            String.format(toolbox.locale.Localization.getLocale(), msg$9, sectionsList.size(), totalPagesInPagesList);

        terminal.appendln(sectionsInfo);
        
        topicsList = downloadPagesList(msg$3, sectionsList); 
        
        String topicsInfo = 
            String.format(toolbox.locale.Localization.getLocale(), msg$10, topicsList.size(), totalPagesInPagesList);

        terminal.appendln(topicsInfo);
   
        downloadPagesList(msg$4, topicsList);        
        
        toolbox.log.Log.ret("phantom.pages", "Downloader", "downloadAllPages");  
        
    }//downloadAllPages

}//classe Downloader