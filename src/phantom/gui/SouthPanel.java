package phantom.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 */
final class SouthPanel extends JPanel {
    
    private final JScrollPane scrollPane;
    
    private final Terminal terminal;
    
    /**
     * 
     */
    protected SouthPanel() {
        
        setLayout(new BorderLayout());
        
        terminal = new Terminal();
        
        scrollPane = new JScrollPane(terminal);
              
        add(scrollPane);       
        
    }//construtor
    
    /**
     * 
     * @return 
     */
    protected Terminal getTerminal() {
        
        return terminal;
        
    }//getTerminal    


}//classe SouthPanel
