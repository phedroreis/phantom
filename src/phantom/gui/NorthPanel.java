package phantom.gui;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import static phantom.gui.GUInterface.STANDART_BORDER;

/**
 *
 * @author 
 * @since
 * @version
 */
final class NorthPanel extends JPanel{
    
    private final NorthInnerLeftPanel northLeftPanel;
    
    /**
     * 
     */
    public NorthPanel() {
        
        setLayout(new FlowLayout());
        
        northLeftPanel = new NorthInnerLeftPanel();
        
        add(northLeftPanel);
        
        add(new NorthInnerRightPanel()); 

    }//construtor
    
    /**
     * 
     * @return 
     */
    public boolean isFullBackup() {
        
        return northLeftPanel.isFullBackup();
        
    }//isFullBackup
    
    /**
     * 
     * @return 
     */
    public boolean isPrivateAreaBackup() {
        
        return northLeftPanel.isPrivateAreaBackup();
        
    }//isPrivateAreaBackup

}//classe NorthPanel
