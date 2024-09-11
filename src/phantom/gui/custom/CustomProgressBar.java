package phantom.gui.custom;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class CustomProgressBar extends JProgressBar{

    /**
     * 
     */
    public CustomProgressBar() {
        
        super();
        setStringPainted(true);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        
    }//construtor
    
    /**
     * 
     * @param value 
     */
    public void concurrentSetValue(final int value) {
        
        java.awt.EventQueue.invokeLater(() -> {
            setValue(value);
        });  
        
    }//concurrentSetValue    
    
    /**
     * 
     * @param max 
     */
    public void concurrentSetMaximum(final int max) {
        
        java.awt.EventQueue.invokeLater(() -> {
            setMaximum(max);
        }); 
        
    }//concurrentSetMaximum

}//classe CustomProgressBar
