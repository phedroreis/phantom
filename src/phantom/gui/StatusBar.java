package phantom.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 **********************************************************************************************************************/
final class StatusBar extends JPanel {
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;  
    
    private final JLabel msgLabel;
    private final JLabel statusLabel;


    /*******************************************************************************************************************
     * 
     ******************************************************************************************************************/
    protected StatusBar() {
        
        setBorder(STANDART_BORDER);        
        
        layout = new GridBagLayout();
        cons = new GridBagConstraints();        
        
        setLayout(layout);
  
        cons.weightx = 1;
        cons.weighty = 0;
        cons.anchor = WEST;
        cons.fill = HORIZONTAL; 
        msgLabel = new JLabel(" ");
        addComponent(msgLabel, 0, 0, 5, 1); 
        
        cons.weightx = 0;
        cons.anchor = EAST;
        statusLabel = new JLabel(" ");
        addComponent(statusLabel, 0, 5, 1, 1);         


    }//construtor
    
    /*==================================================================================================================
    *
    ==================================================================================================================*/
    private void addComponent(Component c, int row, int column, int width, int height) {
 
        cons.gridy = row;
        cons.gridx = column;
        cons.gridwidth = width;
        cons.gridheight = height;
        layout.setConstraints(c, cons);
        add(c);
        
    }//addComponent 
    
    /*******************************************************************************************************************
     * 
     * @param msg 
     ******************************************************************************************************************/
    protected void showMsg(final String msg) {
        
        msgLabel.setText(" " + msg);
        
    }//show
    
    /*******************************************************************************************************************
     * 
     ******************************************************************************************************************/
    protected void clearMsg() {
        
        showMsg("");
        
    }//clear
    
    /*******************************************************************************************************************
     * 
     * @param msg 
     ******************************************************************************************************************/
    protected void showStatus(final String msg) {
        
        statusLabel.setText(msg + " ");
        
    }

}//classe StatusBar
