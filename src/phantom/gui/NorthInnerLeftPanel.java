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
final class NorthInnerLeftPanel extends JPanel {
    
    private final JRadioButton full;
    private final JRadioButton incremental;
    private final ButtonGroup group;
    
    private final JRadioButton pub;
    private final JRadioButton priv;
    private final ButtonGroup group2;
    
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
                    "phantom.properties.NorthInnerLeftPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Full
            msg$2 = rb.getString("msg$2");//Incremental
            msg$3 = rb.getString("msg$3");//Public Area
            msg$4 = rb.getString("msg$4");//Private Area
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Full";
            msg$2 = "Incremental";
            msg$3 = "Public Area";
            msg$4 = "Private Area";            
        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static

    /**
     * 
     */
    public NorthInnerLeftPanel() {
                
        full = new JRadioButton(msg$1, false);
        incremental = new JRadioButton(msg$2, true);
        group = new ButtonGroup();
        
        group.add(full);
        group.add(incremental);
        
        Box box = Box.createVerticalBox();
        
        box.add(full);
        box.add(incremental);
        
        add(box);     
        
        pub = new JRadioButton(msg$3, true);
        priv = new JRadioButton(msg$4, false);
        group2 = new ButtonGroup();
        
        group2.add(pub);
        group2.add(priv);
        
        Box box2 = Box.createVerticalBox();
        
        box2.add(pub);
        box2.add(priv);
        
        add(box2);         

    }//construtor
    
    /**
     * 
     * @return 
     */
    public boolean isFullBackup() {
        
        return full.isSelected();
        
    }//isFull  
    
     /**
     * 
     * @return 
     */
    public boolean isPrivateAreaBackup() {
        
        return priv.isSelected();
        
    }//isPrivateAreaBackup

}//classe NorthInnerLeftPanel
