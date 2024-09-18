package phantom.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import static phantom.global.GlobalConstants.*;
import static phantom.time.GlobalCrons.*;

/**
 *
 * @author 
 * @since
 * @version
 */
final class NorthInnerRightPanel extends JPanel {
    
    private final JButton start;
    private final JButton browse;
    private final JButton listTopics;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;    
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.NorthInnerRightPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Update
            msg$2 = rb.getString("msg$2");//Browse
            msg$3 = rb.getString("msg$3");//List Topics
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Update";
            msg$2 = "Browse";
            msg$3 = "List Topics";
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static      

    @SuppressWarnings("UseSpecificCatch")
    public NorthInnerRightPanel() {
              
        start = new JButton(msg$1);
        browse = new JButton(msg$2);
        listTopics = new JButton(msg$3);
        
        Boo boo = new Boo();
        
        start.addMouseListener(boo);
        browse.addMouseListener(boo);
        listTopics.addMouseListener(boo);    
        
        add(start);
        add(browse);
        add(listTopics);
               
        start.addActionListener((ActionEvent e) -> {
            
            GUInterface.northPanelSetVisible(false);
            GUInterface.centerPanelSetVisible(true);
            GUInterface.southPanelSetVisible(true);
            if (GUInterface.getMainFrameHeight() < 380)
                GUInterface.mainFrameSetSize(GUInterface.getMainFrameWidth(), 380);            
            
            for (int i = 0; i < 4; i++) GUInterface.progressBarSetValue(i, 0);

            BACKUP_ELAPSED_TIME.start();
            
            toolbox.log.Log.println("******** INICIOU PROCESSO DE BACKUP ********");
            toolbox.log.Log.println("Backup total: " + GUInterface.isFullBackup());
            toolbox.log.Log.println("Backup area restrita: " + GUInterface.isPrivateAreaBackup());  
            
            Thread thread = new Thread(new phantom.run.Run());
            thread.start();
            
        });
        
        browse.addActionListener((ActionEvent e) -> {
            
            try {
                
                String ext = GUInterface.isPrivateAreaBackup() ? ".htm" : ".html"; 
                
                toolbox.file.FileTools.openWebPage(new File(ROOT_DIR + FORUM_NAME + ext));
                
            } 
            catch (IllegalArgumentException | NoSuchFileException ex) {
                
                phantom.exception.ExceptionTools.errMessage(null, ex);
            }
            catch (Exception ex) {
                
                phantom.exception.ExceptionTools.crashMessage(null, ex);
                
            }

        });  
        
        listTopics.addActionListener((ActionEvent e) -> {
            
            Thread thread = new Thread(new phantom.run.OrderedList(0));
            thread.start();
            
        });         

    }//construtor
    
    /**
     * Fantasminha assombra os botoes da interface
     */
    private class Boo extends MouseAdapter {
        
        ImageIcon ghost = new ImageIcon(getClass().getResource("ghost.png"));
        
        @Override
        public void mouseEntered(MouseEvent e) {        
            if (e.getSource() == start)                 
                start.setIcon(ghost);
            else if (e.getSource() == browse)
                browse.setIcon(ghost);
            else listTopics.setIcon(ghost);   
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == start)                 
                start.setIcon(null);
            else if (e.getSource() == browse)
                browse.setIcon(null);
            else listTopics.setIcon(null);
        }
        
    }//classe privada Boo

}//classe NorthInnerRightPanel
