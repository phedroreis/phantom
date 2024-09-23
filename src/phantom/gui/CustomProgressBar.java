package phantom.gui;

import javax.swing.JProgressBar;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 22 de setembro de 2024
 * 
 * @version 1.0
 */
public final class CustomProgressBar extends JProgressBar {
    
    private int counter;

    /**
     * 
     */
    public CustomProgressBar() {
        
        super();
        setStringPainted(true);
        setBorder(STANDART_BORDER);
        counter = 0;
        
    }//construtor
    
    /**
     * 
     */
    public void resetCounter() {
        
        counter = 0;
        java.awt.EventQueue.invokeLater(() -> {
            setValue(0);
        });         
        
    }//resetCounter
    
    /**
     * 
     */
    public void incrementCounter() {
        
        java.awt.EventQueue.invokeLater(() -> {
            setValue(++counter);
        }); 
        
    }//incrementCounter
   
    /**
     * 
     * @param max 
     */
    @Override
    public void setMaximum(final int max) {
        
        java.awt.EventQueue.invokeLater(() -> {
            super.setMaximum(max);
        }); 
        
    }//setMaximum

}//classe CustomProgressBar
