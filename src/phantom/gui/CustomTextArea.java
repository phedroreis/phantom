package phantom.gui;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JTextArea;
import static phantom.time.GlobalCrons.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class CustomTextArea extends JTextArea implements Runnable {
    
    private static final int FONT_SIZE = 11;
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
    private static final FontMetrics FM = new Canvas().getFontMetrics(MONO);
    private static final int LINE_HEIGHT = FM.getHeight();
    
    private int countTerminateSignals;
    private final LinkedBlockingQueue<String> terminateSignalsQueue;

    
    private static String msg$1;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.CustomTextArea", 
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
     */
    public CustomTextArea() {

        super(8, 66);
        
        setFont(MONO);
 
        setEditable(false); 
      
        terminateSignalsQueue = new LinkedBlockingQueue<>();
        countTerminateSignals = 0;
        
    }//construtor
    
    /**
     * 
     */
    public void startThreadsMonitor() {
        
        Thread thread = new Thread(this);
        thread.start();
        
    }//monitoring
    
    /**
     * 
     * 
     */ 
    public void setDimensions(final int panelWidth, final int panelHeight) {
        
        if (panelHeight == 0) return; //south panel nao e visivel
        setColumns(panelWidth / (FONT_SIZE - 2));
        setRows((panelHeight / LINE_HEIGHT) - 2);

    }//resize
    
    /**
     * 
     * @param signal 
     * @throws Exception Em caso de InterruptedException 
     */
    public void sendTerminateSignal(final String signal) throws Exception {
        
        terminateSignalsQueue.put(signal);
  
    }//sendTerminateSignal
    
    /**
     * 
     * @param append 
     */
    public void concurrentAppendln(final String append) {
        
        java.awt.EventQueue.invokeLater(() -> {
            append(append + '\n');
        });  
        
    }//concurrentAppendln

    @Override
    public void run() {
        
        while (true) {
            
            try {
                
                String terminateSignal = terminateSignalsQueue.take();
                
                concurrentAppendln(terminateSignal);
                
                countTerminateSignals++;
                
                if (countTerminateSignals == 2) {
                    
                    concurrentAppendln(
                        msg$1 + 
                        BACKUP_ELAPSED_TIME.toString() +
                        '\n'
                    );
                    
                    System.out.println(
                        toolbox.string.StringTools.NEWLINE +
                        msg$1 + 
                        toolbox.string.StringTools.NEWLINE    
                    );
                    
                    toolbox.log.Log.println("******** ENCERROU PROCESSO DE BACKUP ********");
                    
                    countTerminateSignals = 0;

                    GUInterface.northPanelSetVisible(true); 
                    GUInterface.centerPanelSetVisible(false);
                    if (GUInterface.getMainFrameHeight() < 260)
                        GUInterface.mainFrameSetSize(GUInterface.getMainFrameWidth(), 260);
                    /*
                    Salva a data-hora da ultima postagem do forum neste backup.
                    */
                    phantom.time.TimeTools.saveDateTimeOfLastPostFromThisBackup(
                        
                        GUInterface.getDateTimeOfLastPostFromLastBackup()
                        
                    );
                    
                    toolbox.log.Log.println("******** SALVOU REGISTRO DE ULTIMO POST ********");
                    
                }
                
            } catch (Exception e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
                
            }            
        }
        
    }//run

}//classe CustomTextArea
