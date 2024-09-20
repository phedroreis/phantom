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
    private static String msg$4;
    private static String msg$5;
    private static String msg$6; 
    private static String msg$7;    
    
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
            msg$4 = rb.getString("msg$4");//full
            msg$5 = rb.getString("msg$5");//incremental
            msg$6 = rb.getString("msg$6");//private
            msg$7 = rb.getString("msg$7");//public
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Update";
            msg$2 = "Browse";
            msg$3 = "List Topics";
            msg$4 = "Backup full";
            msg$5 = "Backup incremental";
            msg$6 = "Private area"; 
            msg$7 = "Public area";
            
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
            
            int pw = GUInterface.getMainFramePreferredWidth();
            int ph = GUInterface.getMainFramePreferredHeight();            
            
            if (GUInterface.getMainFrameHeight() < ph || GUInterface.getMainFrameWidth() < pw)
                GUInterface.setMainFrameSize(pw, ph);
            GUInterface.setMainFrameMinimumSize(pw, ph);            
            
            for (int i = 0; i < 4; i++) GUInterface.progressBarSetValue(i, 0);
            
            String backupType = 
                (GUInterface.isFullBackup() ? msg$4 : msg$5) + 
                " : " + 
                (GUInterface.isPrivateAreaBackup() ? msg$6 : msg$7);

            GUInterface.terminalConcurrentAppendln(backupType);
            toolbox.log.Log.println("******** INICIOU PROCESSO DE BACKUP ********");
            toolbox.log.Log.println(backupType);
            
            BACKUP_ELAPSED_TIME.start();            
            
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
        
        private final phantom.resources.Resources rs = new phantom.resources.Resources();
        private final ImageIcon ghost = rs.getImageIcon("ghost.png");
        
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