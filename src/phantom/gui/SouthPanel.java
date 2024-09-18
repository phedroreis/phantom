package phantom.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author 
 * @since
 * @version
 */
final class SouthPanel extends JPanel {
    
    private final JScrollPane scroll;
    
    private final phantom.gui.CustomTextArea terminal;
    
    /**
     * 
     */
    public SouthPanel() {
        
        terminal = new phantom.gui.CustomTextArea();
        
        /*
        Monitor de threads sinaliza quando todas terminarem e reabilita panel com botoes
        */
        terminal.startThreadsMonitor();
        
        scroll = new JScrollPane(terminal);
        
        scroll.setBorder(GUInterface.STANDART_BORDER);
              
        add(scroll);       
        
    }//construtor
    
    /**
     * 
     * @param text 
     */
    public void terminalConcurrentAppendln(final String text) {
        
        terminal.concurrentAppendln(text);
        
    }//terminalConcurrentAppendln
    
    /**
     * 
     * @param signal
     * @throws Exception 
     */
    public void terminalSendTerminateSignal(final String signal) throws Exception {
        
        terminal.sendTerminateSignal(signal);
        
    }//terminalSendTerminateSignal 
    
    /**
     * 
     */
    public void terminalResize() {
        
        terminal.setDimensions(getWidth(), getHeight());
        
    }//terminalResize    

}//classe SouthPanel
