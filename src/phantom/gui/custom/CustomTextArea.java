package phantom.gui.custom;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JTextArea;
import static phantom.gui.GlobalComponents.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class CustomTextArea extends JTextArea implements Runnable {
    
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
        
    }//bloco static        

    /**
     * 
     */
    public CustomTextArea() {
        
        super(10, 50);
        setEditable(false); 
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
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
     * @param parentWidth 
     */
    public void resize(final int parentWidth) {
        
        setColumns(parentWidth / 10);
        
    }//resize
    
    /**
     * 
     * @param signal 
     */
    public void sendTerminateSignal(final String signal) {
        
        try {
            
            terminateSignalsQueue.put(signal);
            
        } catch (InterruptedException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }
        
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
                    
                    concurrentAppendln(msg$1 + ELAPSED_TIME.toString() + '\n');
                    countTerminateSignals = 0;
                    phantom.main.Initializer.init();
                    NORTH.setVisible(true);   
                    
                }
                
            } catch (InterruptedException | FileNotFoundException e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
                
            }            
        }
        
    }//run

}//classe CustomTextArea
