package phantom.threads;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 */
public final class StartBackup implements Runnable {
    
    private final JRadioButton fullBackupRadioButton;
    
    private final phantom.gui.Terminal terminal;
    
    private static String msg$1;
    private static String msg$2;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.StartBackup", 
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
    
    public StartBackup() {
        
        fullBackupRadioButton = phantom.gui.MainFrame.getFullBackupRadioButtonReference();
        terminal = phantom.gui.MainFrame.getTheTerminalReference();
    }

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
            
            if (fullBackupRadioButton.isSelected()) 
                dateTimeOfLastPostFromLastBackup = phantom.global.GlobalConstants.ANCIENT_TIMES;
               
            phantom.pages.Downloader downloader = 
                new phantom.pages.Downloader(dateTimeOfLastPostFromLastBackup);

            phantom.time.ElapsedTime elapsedTime = new phantom.time.ElapsedTime();
            elapsedTime.start();            
            
            /*
            Baixa, incrementalmente, paginas do forum (Main, Headers, Sections, Topics)
            */
            downloader.downloadAllPages();
                                    
            ThreadsMonitor.setDateTimeOfLastPostFromLastBackup(
                downloader.getDateTimeOfLastPostFromThisBackup()            
            );            
            
            terminal.appendln(msg$1 + elapsedTime.toString());

            phantom.edit.Editor editor = new phantom.edit.Editor();
           
            System.out.printf(FORMAT, msg$2);    
            
            editor.edit();     
   
        } catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }
        
    }

}//classe StartBackup
