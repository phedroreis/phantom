package phantom.gui;

import javax.swing.JProgressBar;

/**
 *
 * @author 
 * @since
 * @version
 */
final class CustomProgressBar extends JProgressBar{

    /**
     * 
     */
    public CustomProgressBar() {
        
        super();
        setStringPainted(true);
        setBorder(GUInterface.STANDART_BORDER);
        
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
