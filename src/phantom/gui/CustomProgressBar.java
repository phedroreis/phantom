package phantom.gui;

import javax.swing.JProgressBar;

/**
 *
 * @author 
 * @since
 * @version
 */
final class CustomProgressBar extends JProgressBar{
    
    private int counter;

    /**
     * 
     */
    public CustomProgressBar() {
        
        super();
        setStringPainted(true);
        setBorder(GUInterface.STANDART_BORDER);
        counter = 0;
        
    }//construtor
    
    /**
     * 
     */
    public void resetCounter() {
        
        counter = 0;
        
    }//resetCounter
    
    /**
     * 
     */
    public void incrementCounter() {
        
        setValue(++counter);
        
    }//incrementCounter
    
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
