package phantom.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class MainFrame extends JFrame {
    
    public static final int PREFERRED_WIDTH = 600;
    public static final int PREFERRED_HEIGHT = 390;
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;   
    
    private final JMenuBar bar;
    private final JMenu helpMenu;
    private final JMenuItem aboutItem;
    private final JMenuItem helpItem;
    
    private final NorthPanel north;
    private final CenterPanel center;
    private final SouthPanel south;
    
    private static String msg$1;
    private static String msg$2;

    private final ImageIcon favicon;
   
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.MainFrame", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Help
            msg$2 = rb.getString("msg$2");//About
           
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            //Opcoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Help";
            msg$2 = "About";

        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static   
    

    /**
     * 
     */
    public MainFrame() {
        
        super("Phantom"); 
        phantom.resources.Resources resources = new phantom.resources.Resources();
        favicon = resources.getImageIcon("favicon.png");
        setIconImage(favicon.getImage());
        setSize(PREFERRED_WIDTH, 130); 
        setMinimumSize(new Dimension(PREFERRED_WIDTH, 130));
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
        bar = new JMenuBar(); 
        
        helpMenu = new JMenu(msg$1);
        
        aboutItem = new JMenuItem(msg$2);
        aboutItem.addActionListener(new MenuItemListener());
        helpItem = new JMenuItem(msg$1);
        helpItem.addActionListener(new MenuItemListener());
        
        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);
 
        bar.add(helpMenu);
        
        setJMenuBar(bar);       
        
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
        
        center.progressBarSetValue(indexBar, value);
        
    }//progressBarSetValue
    
    /**
     * 
     * @param indexBar
     * @param maximum 
     */
    public void progressBarSetMaximum(final int indexBar, final int maximum) {
        
        center.progressBarSetMaximum(indexBar, maximum);
        
    }//progressBarSetMaximum        
    
    /**
     * 
     * @param indexBar
     * @param value 
     */
    public void progressBarConcurrentSetValue(final int indexBar, final int value) {
        
        center.progressBarconcurrentSetValue(indexBar, value);
        
    }//progressBarConcurrentSetValue
    
    /**
     * 
     * @param indexBar
     * @param maximum 
     */
    public void progressBarConcurrentSetMaximum(final int indexBar, final int maximum) {
        
        center.progressBarconcurrentSetMaximum(indexBar, maximum);
        
    }//progressBarConcurrentSetMaximum  
    
    /**
     * 
     * @param indexBar 
     */
    public void progressBarResetCounter(final int indexBar) {
        
        center.progressBarResetCounter(indexBar);
        
    }//progressBarResetCounter
    
    /**
     * 
     * @param indexBar 
     */
    public void progressBarIncrementCounter(final int indexBar) {
        
        center.progressBarIncrementCounter(indexBar);
        
    }//progressBarIncrementCounter
    
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
    
    /**
     * 
     */
    public void terminalResize() {
        
        south.terminalResize();
        
    }//terminalResize

/*
*    
*/    
private final class MenuItemListener implements ActionListener {
    
    private static final String ABOUT_MSG = 
        "Phantom 1.0\n\nPhantom stands for PHenomenal Advanced Nerd-technology TO Mess around with phpBB.\n\n" +
        "Developers Team:\n\nNasa\nMossad\nKGB\nEts\nGabarito\nPedro Reis\n\n" +
        "Phantom is distributed under GPL 3.0 license. Feel free to feel yourself free.";       

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == helpItem) {
            
            try {
                
                toolbox.file.FileTools.openWebPage(new File(HELP_PATHNAME));
                
            } 
            catch (IOException ex) {
                
                phantom.exception.ExceptionTools.errMessage(null, ex);
            }
        } 
        else 
            JOptionPane.showMessageDialog(null, ABOUT_MSG, msg$2, JOptionPane.PLAIN_MESSAGE, favicon);

    }
    
}//classe privada MenuItemListener 

}//classe MainFrame