package phantom.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class SouthPanel extends JPanel {
    
    private final JScrollPane scroll;

    public SouthPanel() {
        
        /*
        Monitor de threads sinaliza quando todas terminarem e reabilita panel com botoes
        */
        GlobalComponents.TERMINAL.startThreadsMonitor();
        
        scroll = new JScrollPane(GlobalComponents.TERMINAL);
        
        scroll.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
              
        add(scroll);       
        
    }//construtor


}//classe SouthPanel
