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

/**
 *
 * @author 
 * @since
 * @version
 */
final class CenterPanel extends JPanel {
    
    private final GridBagLayout layout;
    private final GridBagConstraints cons;
    
    private final JLabel[] labelArray;
    private final CustomProgressBar[] progressBarArray;
 
   
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
    public CenterPanel() {
        
        layout = new GridBagLayout();
        cons = new GridBagConstraints();
        
        setLayout(layout);
        setBorder(GUInterface.STANDART_BORDER);
        
        labelArray = new JLabel[4];
        
        labelArray[0] = new JLabel(msg$1);
        labelArray[1] = new JLabel(msg$2);
        labelArray[2] = new JLabel(msg$3);
        labelArray[3] = new JLabel(msg$4);        
        
        cons.weightx = 1;
        cons.weighty = 0;
        cons.insets = new Insets(0, 4, 0, 0); 
        for (int i = 0; i < labelArray.length; i++)
            addComponent(labelArray[i], i, 0, 1, 1);
       
        progressBarArray = new CustomProgressBar[4];
        
        for (int i = 0; i < progressBarArray.length; i++)
            progressBarArray[i] = new CustomProgressBar();
 
        
        cons.weightx = 9;
        cons.weighty = 0;
        cons.insets = new Insets(10, 4, 10, 4);
        for (int i = 0; i < 4; i++) 
            addComponent(progressBarArray[i], i, 1, 10, 1);  

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
        
    }
    
    public void setValue(final int indexBar, final int value) {
        
        progressBarArray[indexBar].setValue(value);
        
    }
    
    public void setMaximum(final int indexBar, final int maximum) {
        
        progressBarArray[indexBar].setMaximum(maximum);
        
    }        
    
    public void concurrentSetValue(final int indexBar, final int value) {
        
        progressBarArray[indexBar].concurrentSetValue(value);
        
    }
    
    public void concurrentSetMaximum(final int indexBar, final int maximum) {
        
        progressBarArray[indexBar].concurrentSetMaximum(maximum);
        
    }    

}//classe CenterPanel
