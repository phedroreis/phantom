package phantom.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class NorthLeftPanel extends JPanel {
    
    private final JRadioButton full;
    private final JRadioButton incremental;
    private final ButtonGroup group;
    
    private static String msg$1;
    private static String msg$2;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.NorthLeftPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Full
            msg$2 = rb.getString("msg$2");//Incremental
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Full";
            msg$2 = "Incremental";
        } 
        
    }//bloco static

    /**
     * 
     */
    public NorthLeftPanel() {
                
        full = new JRadioButton(msg$1, false);
        incremental = new JRadioButton(msg$2, true);
        group = new ButtonGroup();
        
        group.add(full);
        group.add(incremental);
        
        Box box = Box.createVerticalBox();
        
        box.add(full);
        box.add(incremental);
        
        add(box);      

    }//construtor
    
    /**
     * 
     * @return 
     */
    public boolean isFull() {
        
        return full.isSelected();
        
    }//isFull  

}//classe NorthLeftPanel
