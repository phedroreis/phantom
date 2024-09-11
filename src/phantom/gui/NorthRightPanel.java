package phantom.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import static phantom.global.GlobalConstants.*;
import static phantom.gui.GlobalComponents.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class NorthRightPanel extends JPanel{
    
    private final JButton start;
    private final JButton browse;
    
    private static String msg$1;
    private static String msg$2;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.NorthRightPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Update
            msg$2 = rb.getString("msg$2");//Browse
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Update";
            msg$2 = "Browse";
        }
        
    }//bloco static      

    public NorthRightPanel() {
              
        start = new JButton(msg$1);
        browse = new JButton(msg$2);
        
        add(start);
        add(browse);
        
        start.addActionListener((ActionEvent e) -> {
            
            GlobalComponents.NORTH.setVisible(false);
            HTML_DOWNLOAD_PROGRESS_BAR.setValue(0);
            EDIT_PROGRESS_BAR.setValue(0);
            STATIC_DOWNLOAD_PROGRESS_BAR.setValue(0);
            CSS_PROGRESS_BAR.setValue(0);
            Thread thread = new Thread(new phantom.run.Run());
            ELAPSED_TIME.start();
            thread.start();
            
        });
        
        browse.addActionListener((ActionEvent e) -> {
            
            try {
                
                toolbox.file.FileTools.openWebPage(new File(ROOT_DIR + FORUM_NAME + ".html"));
                
            } catch (IOException ex) {}

        });        

    }//construtor

}//classe NorthRightPanel
