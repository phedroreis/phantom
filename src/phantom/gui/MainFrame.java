package phantom.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author 
 * @since
 * @version
 */
final class MainFrame extends JFrame {
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;   
    
    private final JMenuBar bar;
    private final JMenu propertiesMenu;
    private final JMenu helpMenu;
    private final JMenuItem config;
    private final JMenuItem update;
    //private final JMenuItem about;
    //private final JMenuItem help;
    
    private final NorthPanel north;
    private final CenterPanel center;
    private final SouthPanel south;
    

    /**
     * 
     */
    public MainFrame() {
        
        super("Phantom");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("favicon.png")));
        setSize(600, 130); 
        setMinimumSize(new Dimension(600, 130));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        bar = new JMenuBar();
        setJMenuBar(bar);
        
        propertiesMenu = new JMenu("Properties");
        helpMenu = new JMenu("Help");

        
        config = new JMenuItem("Config");
        update = new JMenuItem("Update");

        
        propertiesMenu.add(config);
        propertiesMenu.add(update);
        
        bar.add(propertiesMenu);
        bar.add(helpMenu);
        
        update.setVisible(false);
        
        /*
        config.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
            }
            
        });*/
        
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        
        setLayout(layout);
  
        cons.weightx = 1;
        cons.weighty = 0;
        cons.anchor = WEST;
        cons.fill = HORIZONTAL;     
        north = new NorthPanel();
        addComponent(north, 0, 0, 1, 1);
        
        
        center = new CenterPanel();
        addComponent(center, 1, 0, 1, 1);
        
        cons.weightx = 1;
        cons.weighty = 1000;
        cons.anchor = WEST;
        cons.fill = BOTH;     
        south = new SouthPanel();  
        addComponent(south, 2, 0, 1, 1);
        
        center.setVisible(false);
        
        south.setVisible(false);
        
        addComponentListener(
            
            new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {

                    south.terminalResize();
                              
                }
                
                @Override
                public void componentShown(ComponentEvent e) {

                    south.terminalResize();
                              
                } 
                
                @Override
                public void componentHidden(ComponentEvent e) {

                    south.terminalResize();
                              
                }
                
            }
            
        );
       
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    
                    phantom.main.Finalizer.finalizer();
                }
             
            }
        );

    }//construtor
    
    /*
    *
    */
    private void addComponent(Component c, int row, int column, int width, int height) {
 
        cons.gridy = row;
        cons.gridx = column;
        cons.gridwidth = width;
        cons.gridheight = height;
        layout.setConstraints(c, cons);
        add(c);
        
    }//addComponent
    
    /**
     * 
     * @return 
     */
    public boolean isFullBackup() {
        
        return north.isFullBackup();
        
    }//isFullBackup 
    
    /**
     * 
     * @return 
     */
    public boolean isPrivateAreaBackup() {
        
        return north.isPrivateAreaBackup();
        
    }//isPrivateAreaBackup
    
    /**
     * 
     * @param isVisible 
     */
    public void northPanelSetVisible(final boolean isVisible) {
        
        north.setVisible(isVisible);
        
    }//northPanelSetVisible
    
     /**
     * 
     * @param isVisible 
     */
    public void centerPanelSetVisible(final boolean isVisible) {
        
        center.setVisible(isVisible);
        
    }//centerhPanelSetVisible
    
     /**
     * 
     * @param isVisible 
     */
    public void southPanelSetVisible(final boolean isVisible) {
        
        south.setVisible(isVisible);
        
    }//southPanelSetVisible
    
    /**
     * 
     * @param indexBar
     * @param value 
     */
    public void progressBarSetValue(final int indexBar, final int value) {
        
        center.setValue(indexBar, value);
        
    }//progressBarSetValue
    
    /**
     * 
     * @param indexBar
     * @param maximum 
     */
    public void progressBarSetMaximum(final int indexBar, final int maximum) {
        
        center.setMaximum(indexBar, maximum);
        
    }//progressBarSetMaximum        
    
    /**
     * 
     * @param indexBar
     * @param value 
     */
    public void progressBarConcurrentSetValue(final int indexBar, final int value) {
        
        center.concurrentSetValue(indexBar, value);
        
    }//progressBarConcurrentSetValue
    
    /**
     * 
     * @param indexBar
     * @param maximum 
     */
    public void progressBarConcurrentSetMaximum(final int indexBar, final int maximum) {
        
        center.concurrentSetMaximum(indexBar, maximum);
        
    }//progressBarConcurrentSetMaximum  
    
    /**
     * 
     * @param text 
     */
    public void terminalConcurrentAppendln(final String text) {
        
        south.terminalConcurrentAppendln(text);
        
    }//terminalConcurrentAppendln
    
    /**
     * 
     * @param signal
     * @throws Exception 
     */
    public void terminalSendTerminateSignal(final String signal) throws Exception {
        
        south.terminalSendTerminateSignal(signal);
        
    }//terminalSendTerminateSignal
    
    public void terminalResize() {
        
        south.terminalResize();
        
    }//terminalResize


}//classe MainFrame
