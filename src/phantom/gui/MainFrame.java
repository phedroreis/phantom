package phantom.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.WEST;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 **********************************************************************************************************************/
public final class MainFrame extends JFrame {
    
    private static MainFrame mainFrameReference;

    private static final Dimension MIN_SIZE_CENTER_PANEL_HIDDEN = new Dimension(630, 300);
    
    private static final Dimension MIN_SIZE_CENTER_PANEL_VISIBLE = new Dimension(630, 500);
    
    private final ImageIcon ghostIcon;
    
    private final JMenuBar menuBar;
    private final JMenu helpMenu;
    private final JMenuItem aboutItem;
    private final JMenuItem helpItem; 
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;
    
    private final NorthPanel northPanel;
    private final CenterPanel centerPanel;
    private final SouthPanel southPanel;
    private final StatusBar statusBar;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;    

    /*==================================================================================================================
    * Internacionaliza as Strings "hardcoded" na classe
    ==================================================================================================================*/
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.MainFrame", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Help
            msg$2 = rb.getString("msg$2");//About
            msg$3 = rb.getString("msg$3");//Incremental : Public Area
           
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            //Opcoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Help";
            msg$2 = "About";
            msg$3 = "Incremental : Public Area";           

        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static 
    
    /*******************************************************************************************************************
     * 
     * @param theMainFrame 
     ******************************************************************************************************************/
    public static void setMainFrameReference(final MainFrame theMainFrame) {
        
        mainFrameReference = theMainFrame;
        
    }//setMainFrameReference
    
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static JRadioButton getFullBackupRadioButtonReference() {
        
        return mainFrameReference.getFullBackupRadioButton();
    
    }//getFullBackupRadioButtonReference
    
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static JRadioButton getPrivateAreaRadioButtonReference() {
        
        return mainFrameReference.getPrivateAreaRadioButton();
        
    }//getPrivateAreaRadioButtonReference    
    
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static CustomProgressBar getHtmlProgressBarReference() {
        
        return mainFrameReference.getHtmlProgressBar();
        
    }//getHtmlProgressBarReference
    
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static CustomProgressBar getEditProgressBarReference() {
        
        return mainFrameReference.getEditProgressBar();
        
    }//getEditProgressBarReference    
  
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static CustomProgressBar getStaticProgressBarReference() {
        
        return mainFrameReference.getStaticProgressBar();
        
    }//getStaticProgressBarReference  

    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static CustomProgressBar getCssProgressBarReference() {
        
        return mainFrameReference.getCssProgressBar();
        
    }//getCssProgressBarReference 
 
    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    public static Terminal getTerminalReference() {
        
        return mainFrameReference.getTerminal();
        
    }//getTerminalReference    

    /*******************************************************************************************************************
     * 
     * @return 
     ******************************************************************************************************************/
    protected static StatusBar getStatusBarReference() {
        
        return mainFrameReference.statusBar;
        
    }//getStatusBarReference    

    /*******************************************************************************************************************
     * 
     * @param visible 
     ******************************************************************************************************************/
    public static void setCenterPanelVisible(final boolean visible) {
        
        mainFrameReference.centerPanelVisible(visible);
        
    }//setCenterPanelVisible
    
    /*******************************************************************************************************************
     * 
     * @param visible 
     ******************************************************************************************************************/
    public static void setNorthPanelVisible(final boolean visible) {
        
        mainFrameReference.northPanelVisible(visible);
        
    }//setNorthPanelVisible
    
    /*******************************************************************************************************************
     * 
     ******************************************************************************************************************/
    public static void killMainFrame() {

        if (mainFrameReference.isShowing())
            mainFrameReference.dispatchEvent(
                new WindowEvent(mainFrameReference, WindowEvent.WINDOW_CLOSING)
            );

    }//killMainFrame
    
    
    /*******************************************************************************************************************
     * Construtor da classe.
     ******************************************************************************************************************/
    public MainFrame() {
        
        super("Phantom 1.1");
        
        phantom.resources.Resources resources = new phantom.resources.Resources();
        ghostIcon = resources.getImageIcon("ghost.png");
        setIconImage(ghostIcon.getImage());
        
        setSize(MIN_SIZE_CENTER_PANEL_HIDDEN);
        
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new JMenuBar(); 
        
        helpMenu = new JMenu(msg$1);
        
        MenuItemListener menuItemListener = new MenuItemListener();
        
        aboutItem = new JMenuItem(msg$2);
        aboutItem.addActionListener(menuItemListener);
        helpItem = new JMenuItem(msg$1);
        helpItem.addActionListener(menuItemListener);
        
        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);
 
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);  
        layout = new GridBagLayout();
        cons = new GridBagConstraints();        
        
        setLayout(layout);
  
        cons.weightx = 1;
        cons.weighty = 0;
        cons.anchor = WEST;
        cons.fill = HORIZONTAL; 
        northPanel = new NorthPanel();
        addComponent(northPanel, 0, 0, 1, 1);
        
        centerPanel = new CenterPanel();
        addComponent(centerPanel, 1, 0, 1, 1); 
        
        statusBar = new StatusBar();  
        addComponent(statusBar, 3, 0, 1, 1);  
        
        statusBar.showStatus(msg$3);
        
        cons.weighty = 1000;
        cons.fill = BOTH;     
        southPanel = new SouthPanel();  
        addComponent(southPanel, 2, 0, 1, 1); 
        
        centerPanelVisible(false);        
       
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    
                    phantom.main.Finalizer.finalizer();
                }
             
            }
        );        

    }//construtor
    
    /*==================================================================================================================
    * Adiciona um componente ao JFrame utilizando GridBagLayout.
    ==================================================================================================================*/
    private void addComponent(Component c, int row, int column, int width, int height) {
 
        cons.gridy = row;
        cons.gridx = column;
        cons.gridwidth = width;
        cons.gridheight = height;
        layout.setConstraints(c, cons);
        add(c);
        
    }//addComponent  
    
    /*==================================================================================================================
    * Determina se northPanel esta visivel ou nao.
    ==================================================================================================================*/
    private void northPanelVisible(final boolean visible) {
 
        northPanel.setVisible(visible);
 
    }//northPanelVisible   
     
    /*
    * Determina se centerPanel esta visivel ou nao.
    */
    private void centerPanelVisible(final boolean visible) {
 
        northPanel.setVisible(!visible);
        centerPanel.setVisible(visible);
        
        if (visible) {
            
            setMinimumSize(MIN_SIZE_CENTER_PANEL_VISIBLE);
            setPreferredSize(MIN_SIZE_CENTER_PANEL_VISIBLE);            
        }
        else {
            
            setMinimumSize(MIN_SIZE_CENTER_PANEL_HIDDEN);
            setPreferredSize(MIN_SIZE_CENTER_PANEL_HIDDEN);  
        }
        
    }//centerPanelVisible
    

    /*
    * Retorna uma referencia para o terminal da interface.
    * 
    * @return A referencia.
    **/
    private Terminal getTerminal() {
        
        return southPanel.getTerminal();
        
    }//getTerminal

    /*
     * Retorna uma referencia para acessar o estado do botao de radio da interface que indica
     * se o backup sera total.
     * 
     * @return A referencia.
     */
    private JRadioButton getFullBackupRadioButton() {
        
        return northPanel.getFullBackupRadioButton();
        
    }//getFullBackupRadioButton
    
    /*
     * Retorna uma referencia para acessar o botao de radio da interface que indica se 
     * o backup tera como escopo a area privada do forum.
     * 
     * @return A referencia.
     */
    private JRadioButton getPrivateAreaRadioButton() {
        
        return northPanel.getPrivateAreaRadioButton();
        
    }//getPrivateAreaRadioButton  

    /*
     * Retorna uma referencia para a barra de progresso que indica a evolucao do download
     * de paginas do forum.
     * 
     * @return A referencia.
     */
    private CustomProgressBar getHtmlProgressBar() {
        
        return centerPanel.getHtmlProgressBar();
        
    }//getHtmlProgressBar

    private CustomProgressBar getEditProgressBar() {
        
        return centerPanel.getEditProgressBar();
        
    }//getEditProgressBar

    private CustomProgressBar getStaticProgressBar() {
        
        return centerPanel.getStaticProgressBar();
        
    }//getStaticProgressBar

    private CustomProgressBar getCssProgressBar() {
        
        return centerPanel.getCssProgressBar();
        
    }//getCssProgressBar    
    
/*======================================================================================================================
*    
======================================================================================================================*/
private final class MenuItemListener implements ActionListener {
    
    private static final String ABOUT_MSG = 
        "Phantom 1.1\n\nPhantom stands for PHenomenal Advanced Nerd-technology TO Mess around with phpBB.\n\n" +
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
            JOptionPane.showMessageDialog(null, ABOUT_MSG, msg$2, JOptionPane.PLAIN_MESSAGE, ghostIcon);

    }
    
}//classe privada MenuItemListener 

}//classe MainFrame
