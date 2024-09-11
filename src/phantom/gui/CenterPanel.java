package phantom.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class CenterPanel extends JPanel {
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;
   
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
        
    }//bloco static    
    
    /**
     * 
     */
    public CenterPanel() {
        
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        
        setLayout(layout);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setOpaque(true);
        
        JLabel htmlLabel = new JLabel(msg$1);
        JLabel editionLabel = new JLabel(msg$2);
        JLabel staticLabel = new JLabel(msg$3);
        JLabel cssLabel = new JLabel(msg$4);
        
        
        cons.weightx = 1;
        cons.weighty = 1;
        cons.insets = new Insets(0, 4, 0, 0);        
        addComponent(htmlLabel,    0, 0, 1, 1);
        addComponent(editionLabel, 1, 0, 1, 1);
        addComponent(staticLabel,  2, 0, 1, 1);
        addComponent(cssLabel,     3, 0, 1, 1);        
        
        cons.weightx = 9;
        cons.weighty = 1;
        cons.insets = new Insets(10, 4, 10, 4);
        addComponent(GlobalComponents.HTML_DOWNLOAD_PROGRESS_BAR, 0, 1, 10, 1);
        addComponent(GlobalComponents.EDIT_PROGRESS_BAR, 1, 1, 10, 1);
        addComponent(GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR,  2, 1, 10, 1);
        addComponent(GlobalComponents.CSS_PROGRESS_BAR,  3, 1, 10, 1);        
        

    }//construtor
    
    /*
    *
    */
    private void addComponent(Component c, int row, int column, int width, int height) {
        
        cons.anchor = WEST;
        cons.fill = BOTH;    
        cons.gridy = row;
        cons.gridx = column;
        cons.gridwidth = width;
        cons.gridheight = height;
        layout.setConstraints(c, cons);
        add(c);
        
    }

}//classe CenterPanel
