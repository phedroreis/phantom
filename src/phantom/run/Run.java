package phantom.run;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import phantom.gui.GUInterface;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Run implements Runnable {
    
    private static final String FORMAT = "===== %s =====%n%n"; 
    
    private static String msg$1;
    private static String msg$2;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Run", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");
            msg$2 = rb.getString("msg$2");            
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Forum pages download is concluded!";
            msg$2 = "Editing pages and downloading static files";
            
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static      

    @Override
    public void run() {
        
        try {
            /*
            Cria objeto para baixar as paginas do forum, inicializado com data-hora da
            ultima postagem no ultimo backup. Se este for o 1o backup, a data-hora eh
            lida como "ano 0"
            */
            String dateTimeOfLastPostFromLastBackup = 
                phantom.time.TimeTools.readDateTimeOfLastPostFromLastBackup();
            
            if (GUInterface.isFullBackup()) 
                dateTimeOfLastPostFromLastBackup = phantom.global.GlobalConstants.ANCIENT_TIMES;
               
            phantom.pages.Downloader downloader = 
                new phantom.pages.Downloader(dateTimeOfLastPostFromLastBackup);

            phantom.time.ElapsedTime elapsedTime = new phantom.time.ElapsedTime();
            elapsedTime.start();            
            
            /*
            Baixa, incrementalmente, paginas do forum (Main, Headers, Sections, Topics)
            */
            downloader.downloadAllPages();
                                    
            GUInterface.setDateTimeOfLastPostFromLastBackup(
                downloader.getDateTimeOfLastPostFromThisBackup()            
            );            
            
            GUInterface.terminalConcurrentAppendln(msg$1 + elapsedTime.toString());

            phantom.edit.Editor editor = new phantom.edit.Editor();
           
            System.out.printf(FORMAT, msg$2);    
            
            editor.edit();     
   
        } catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }
        
    }

}//classe Run
