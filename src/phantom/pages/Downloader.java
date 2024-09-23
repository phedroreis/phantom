package phantom.pages;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;


/**
 *
 * @author 
 * @since
 * @version
 */
public final class Downloader {
    
    private Main main;
   
    private String dateTimeOfLastPostFromLastBackup;
    
    private final phantom.gui.CustomProgressBar htmlProgressBar;
    
    private final phantom.gui.Terminal terminal;
    
    private final JRadioButton privateAreaRadioButton;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    private static String msg$5;
    private static String msg$6;  


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
           
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Downloading main page and getting headers list";
            msg$2 = "Downloading headers and getting sections list";
            msg$3 = "Dowloaging sections and getting topics list";
            msg$4 = "Downloagind topics";
            msg$5 = "Gabarito, the restricted area is close!";
            msg$6 = "Most recent post :"; 
           
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
        terminal = phantom.gui.MainFrame.getTheTerminalReference();
        privateAreaRadioButton = phantom.gui.MainFrame.getPrivateAreaRadioButtonReference();

    }//construtor

    /**
     * 
     * @return 
     */
    public String getDateTimeOfLastPostFromThisBackup() {
        
        return main.getDateTimeOfLastPostOnThisPage();
        
    }//getDateTimeOfLastPostFromThisBackup
    
    /*
    *
    */
    private void printMessages(final String msg1, final String msg2) {
        
        System.out.printf(FORMAT, msg1);
  
        terminal.appendln(msg1 + "...");  
 
        toolbox.log.Log.println("**** " + msg2 + " ****");           
        
    }//printMessages
    
    /*
    *
    */
    private List<Page> downloadPagesList(
        final String msg,
        final String logMsg,
        final List<Page> PagesList
    ) throws Exception {
        
        List<Page> auxList;  
        
        List<Page> mergeList = new LinkedList<>();
        
        printMessages(msg, logMsg);        
        
        htmlProgressBar.setMaximum(Page.getTotalNumberOfPagesInThisPagesList()); 
        htmlProgressBar.resetCounter();        
        Page.resetTotalNumberOfPagesInThisPagesList();
     
        for (Page page : PagesList) {
       
            auxList = page.download();
            
            if (auxList != null) mergeList.addAll(auxList);  
            
        }// 
        
        return mergeList;
        
    }//downloadPagesList

    /**
     * 
     * @throws Exception
     */
    public void downloadAllPages() throws Exception {
        
        toolbox.log.Log.exec("phantom.pages", "Downloader", "downloadAllPages");
        
        Page.setDateTimeOfLastPostFromLastBackup(dateTimeOfLastPostFromLastBackup);
        
        List<Page> headersList;
        List<Page> sectionsList;
        List<Page> topicsList;
        
        Page.resetTotalNumberOfPagesInThisPagesList();
          
        main = new phantom.pages.Main(); 

        printMessages(
            msg$1,
            "Baixando pagina inicial e obtendo lista de cabecalhos"
        );
        
        
        htmlProgressBar.setMaximum(Page.getTotalNumberOfPagesInThisPagesList()); 
        htmlProgressBar.resetCounter();        
        Page.resetTotalNumberOfPagesInThisPagesList();
        
        headersList = main.download();              
        
        if (privateAreaRadioButton.isSelected()) (new PrivateHeaders()).removeNonPrivateHeaders(headersList);
  
        
        if (headersList.isEmpty()) {
            
            main.setDateTimeOfLastPostOnThisPage(dateTimeOfLastPostFromLastBackup);
            
            System.out.println(msg$5 + toolbox.string.StringTools.NEWLINE);//Gabarito, the restricted area is closed!
            
            terminal.appendln(msg$5); 
            
            phantom.exception.ExceptionTools.crashMessage(null, new NullPointerException(msg$5));
        }

        //O header que tiver o post mais recente, tera, obviamente, o post mais recente do forum
        main.setDateTimeOfLastPostOnThisPage(
            Page.getDateTimeOfLastestPostFromThisPageList(headersList)        
        ); 
        
        System.out.println(//Most recently post: 
            msg$6 
            + " " 
            + main.getDateTimeOfLastPostOnThisPage() 
            + toolbox.string.StringTools.NEWLINE
        );
        
        terminal.appendln(msg$6 + " " + main.getDateTimeOfLastPostOnThisPage()); 

        sectionsList = 
            downloadPagesList(
                msg$2,
                "Baixando cabecalhos e obtendo lista de secoes",
                headersList
            );
        
 
        if (sectionsList.isEmpty()) return;        
        topicsList = 
            downloadPagesList(
                msg$3,
                "Baixando secoes e obtendo lista de topicos",
                sectionsList
            );
 
   
        if (topicsList.isEmpty()) return;        
        downloadPagesList(
            msg$4,
            "Baixando topicos",
            topicsList
        );        
        
        toolbox.log.Log.ret("phantom.pages", "Downloader", "downloadAllPages");  
        
    }//downloadAllPages

}//classe Downloader