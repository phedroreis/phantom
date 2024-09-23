package phantom.threads;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import static phantom.time.GlobalCrons.*;

/**
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 */
public class ThreadsMonitor implements Runnable {
    
    private static int countTerminateSignals;
    
    private static String dateTimeOfLastPostFromThisBackup;
    
    private static final LinkedBlockingQueue<String> TERMINATE_SIGNALS_QUEUE = 
        new LinkedBlockingQueue<>();
   
    private final phantom.gui.Terminal terminal;
    
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.ThreadsMonitor", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//** BACKUP IS OVER! **
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "BACKUP IS OVER!";         
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static        

    /**
     * 
     * 
     */
    public ThreadsMonitor() { 
   
        terminal = phantom.gui.MainFrame.getTheTerminalReference();

        countTerminateSignals = 0;        

    }//construtor
    
   /**
     * 
     * @param signal 
     * @throws Exception Em caso de InterruptedException 
     */
    public static void sendTerminateSignal(final String signal) throws Exception {
        
        TERMINATE_SIGNALS_QUEUE.put(signal);
  
    }//sendTerminateSignal
    
    /**
     * 
     * @param datetime 
     */
    public static void setDateTimeOfLastPostFromLastBackup(final String datetime) {
        
        dateTimeOfLastPostFromThisBackup = datetime;
        
    }//setDateTimeOfLastPostFromThisBackup
    
    /**
     * 
     */
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void run() {
        
        while (true) {
            
            try {
                
                String terminateSignal = TERMINATE_SIGNALS_QUEUE.take();
                
                terminal.appendln(terminateSignal);
                
                countTerminateSignals++;
                
                if (countTerminateSignals == 2) {
                    
                    terminal.appendln(
                        msg$1 + 
                        BACKUP_ELAPSED_TIME.toString() +
                        toolbox.string.StringTools.NEWLINE
                    );
                    
                    System.out.println(
                        toolbox.string.StringTools.NEWLINE +
                        msg$1 + 
                        toolbox.string.StringTools.NEWLINE    
                    );
                    
                    toolbox.log.Log.println("******** ENCERROU PROCESSO DE BACKUP ********");
                    
                    countTerminateSignals = 0;

                    phantom.gui.MainFrame.setCenterPanelVisible(false);
                    
                    /*
                    Salva a data-hora da ultima postagem do forum neste backup.
                    */
                    phantom.time.TimeTools.saveDateTimeOfLastPostFromThisBackup(
                        
                        dateTimeOfLastPostFromThisBackup
                        
                    );
                    
                    toolbox.log.Log.println("******** SALVOU REGISTRO DE ULTIMO POST ********");
                    
                }
                
            } catch (Exception e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
                
            }            
        }
        
    }//run
    
}//classe ThreadsMonitor
