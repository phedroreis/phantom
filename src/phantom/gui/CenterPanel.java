package phantom.gui;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class CenterPanel extends JPanel {
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;
    
    private final JLabel htmlLabel;
    private final CustomProgressBar htmlProgressBar;
    
    private final JLabel editLabel;
    private final CustomProgressBar editProgressBar;
    
    private final JLabel staticLabel;
    private final CustomProgressBar staticProgressBar;
    
    private final JLabel cssLabel;
    private final CustomProgressBar cssProgressBar;
 
   
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.CenterPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Downloading forum pages :
            msg$2 = rb.getString("msg$2");//Editing pages :
            msg$3 = rb.getString("msg$3");//Downloading static files :
            msg$4 = rb.getString("msg$4");//Searching CSS files :
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Downloading forum pages :";
            msg$2 = "Editing forum pages :";
            msg$3 = "Downloading static files :";
            msg$4 = "Searching CSS files :";
        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static    
    
    /**
     * 
     */
    protected CenterPanel() {
        
        setBorder(STANDART_BORDER);
        
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        
        setLayout(layout);
        
        cons.weightx = 1;
        cons.weighty = 0;
        cons.insets = new Insets(0, 4, 0, 0);
        
        htmlLabel = new JLabel(msg$1);
        editLabel = new JLabel(msg$2);
        staticLabel = new JLabel(msg$3);
        cssLabel = new JLabel(msg$4); 
        
        cons.weightx = 1;
        cons.weighty = 0;
        cons.insets = new Insets(0, 4, 0, 0);
        addComponent(htmlLabel, 0, 0, 1, 1); 
        addComponent(editLabel, 1, 0, 1, 1);
        addComponent(staticLabel, 2, 0, 1, 1); 
        addComponent(cssLabel, 3, 0, 1, 1); 
        
        htmlProgressBar = new CustomProgressBar();
        editProgressBar = new CustomProgressBar();
        staticProgressBar = new CustomProgressBar();
        cssProgressBar = new CustomProgressBar();
        
        cons.weightx = 9;
        cons.weighty = 0;
        cons.insets = new Insets(10, 4, 10, 4);
        addComponent(htmlProgressBar, 0, 1, 10, 1);  
        addComponent(editProgressBar, 1, 1, 10, 1);
        addComponent(staticProgressBar, 2, 1, 10, 1);
        addComponent(cssProgressBar, 3, 1, 10, 1);

    }//construtor
    
    /*
    *
    */
    private void addComponent(Component c, int row, int column, int width, int height) {
        
        cons.anchor = WEST;
        cons.fill = HORIZONTAL;    
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
    protected CustomProgressBar getHtmlProgressBar() {
        
        return htmlProgressBar;
        
    }//getHtmlProgressBar

    /**
     * 
     * @return 
     */
    protected CustomProgressBar getEditProgressBar() {
        
        return editProgressBar;
        
    }//getEditProgressBar

    /**
     * 
     * @return 
     */
    protected CustomProgressBar getStaticProgressBar() {
        
        return staticProgressBar;
        
    }//getStaticProgressBar

    /**
     * 
     * @return 
     */
    protected CustomProgressBar getCssProgressBar() {
        
        return cssProgressBar;
        
    }//getCssProgressBar    

}//classe CenterPanel