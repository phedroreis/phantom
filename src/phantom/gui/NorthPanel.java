package phantom.gui;

import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class NorthPanel extends JPanel{
    
    /**
     * 
     */
    public NorthPanel() {
        
        setLayout(new FlowLayout());
        
        add(new NorthLeftPanel());
        
        add(new NorthRightPanel());

    }//construtor

}//classe NorthPanel
