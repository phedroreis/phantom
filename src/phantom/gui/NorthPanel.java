package phantom.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;
import static phantom.time.GlobalCrons.*;

/**
 *
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 */
final class NorthPanel extends JPanel {
    
    private final JRadioButton full;
    private final JRadioButton incremental;
    private final ButtonGroup group1;
    
    private final JRadioButton pub;
    private final JRadioButton priv;
    private final ButtonGroup group2;
    
    private final JButton start;
    private final JButton browse;
    private final JButton listTopics;  
    
    private final ImageIcon favicon16;
    private final ImageIcon download;    
    private final ImageIcon list;  
    
    private StatusPanel statusBar;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;  
    private static String msg$5; 
    private static String msg$6; 
    private static String msg$7; 
    private static String msg$8;
    private static String msg$9;
    private static String msg$10;
    private static String msg$11;  
    private static String msg$12; 
    private static String msg$13; 
    private static String msg$14;    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.NorthPanel", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Full
            msg$2 = rb.getString("msg$2");//Incremental
            msg$3 = rb.getString("msg$3");//Public Area
            msg$4 = rb.getString("msg$4");//Private Area
            msg$5 = rb.getString("msg$5");//Update
            msg$6 = rb.getString("msg$6");//Browse
            msg$7 = rb.getString("msg$7");//List Topics
            msg$8 = rb.getString("msg$8");//Start backup
            msg$9 = rb.getString("msg$9");//Open web page
            msg$10 = rb.getString("msg$10");//Show topics list
            msg$11 = rb.getString("msg$11");//Select full backup
            msg$12 = rb.getString("msg$12");//Select incremental backup
            msg$13 = rb.getString("msg$13");//Select public area
            msg$14 = rb.getString("msg$14");//Select private area           
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Full";
            msg$2 = "Incremental";
            msg$3 = "Public Area";
            msg$4 = "Private Area";  
            msg$5 = "Update";
            msg$6 = "Browse";
            msg$7 = "List Topics";
            msg$8 = "Start backup";
            msg$9 = "Open web page";
            msg$10 = "Show topics list";
            msg$11 = "Select full backup";  
            msg$12 = "Select incremental backup";
            msg$13 = "Select public area";
            msg$14 = "Select private area";            
        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }        
        
    }//bloco static

    /**
     * 
     */
    @SuppressWarnings("UseSpecificCatch")
    public NorthPanel() {
        
        setBorder(STANDART_BORDER);
                 
        full = new JRadioButton(msg$1, false);
        incremental = new JRadioButton(msg$2, true);
        group1 = new ButtonGroup();
        
        group1.add(full);
        group1.add(incremental);
        
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

        Box box3 = Box.createHorizontalBox();
        
        start = new JButton(msg$5);
        browse = new JButton(msg$6);
        listTopics = new JButton(msg$7);
        
        phantom.resources.Resources resources = new phantom.resources.Resources();
       
        download = resources.getImageIcon("download.png");
        start.setIcon(download);  
        
        favicon16 = resources.getImageIcon("favicon16.png");
        browse.setIcon(favicon16);       
                
        list = resources.getImageIcon("list.png");
        listTopics.setIcon(list);        
        
        MouseAdapter showStatus = new ShowMsg();
        
        start.addMouseListener(showStatus);
        browse.addMouseListener(showStatus);
        listTopics.addMouseListener(showStatus); 
        full.addMouseListener(showStatus);
        incremental.addMouseListener(showStatus);
        pub.addMouseListener(showStatus);
        priv.addMouseListener(showStatus);
        
        start.addActionListener(new StartButtonActionListener());
        browse.addActionListener(new BrowseButtonActionListener());
        listTopics.addActionListener(new ListTopicsButtonActionListener());
        RadioButtonActionListener listener = new RadioButtonActionListener();
        full.addActionListener(listener);
        incremental.addActionListener(listener);
        pub.addActionListener(listener);
        priv.addActionListener(listener);
        
        box3.add(start);
        box3.add(browse);
        box3.add(listTopics);  

        add(box3);

    }//construtor
    
    private void showStatus() {
        
        MainFrame mainFrame = (MainFrame)getTopLevelAncestor();        
        boolean isFullBackup = mainFrame.getFullBackupRadioButton().isSelected();
        boolean isPrivateAreaBackup = mainFrame.getPrivateAreaRadioButton().isSelected();

        String backupType = 
            (isFullBackup ? msg$1 : msg$2) + " : " + (isPrivateAreaBackup ? msg$4 : msg$3);  
        
        statusBar.showStatus(backupType);
    }    
    
    /*
    *
    */
    protected void addStatusBar(final StatusPanel statusBar) {
        
        this.statusBar = statusBar;
        showStatus();
        
    }//addStatusBar

    /**
     * 
     * @return 
     */
    protected JRadioButton getFullBackupRadioButton() {
        
        return full;
        
    }//getFullBackupRadioButton
    
    /**
    * 
    * @return 
    */
    protected JRadioButton getPrivateAreaRadioButton() {
        
        return priv;
        
    }//getPrivateAreaRadioButton
    
/*
*
*/
private class ShowMsg extends MouseAdapter {

    @Override
    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();

        if (source == start)                 
            statusBar.showMsg(msg$8 + " [" + (full.isSelected() ? msg$1 : msg$2) + "]");
        else if (source == browse)
            statusBar.showMsg(msg$9 + " : " + (pub.isSelected() ? msg$3 : msg$4));
        else if (source == listTopics) 
            statusBar.showMsg(msg$10); 
        else if (source == full)
            statusBar.showMsg(msg$11);
        else if (source == incremental)
            statusBar.showMsg(msg$12);
        else if (source == pub)
            statusBar.showMsg(msg$13);
        else 
            statusBar.showMsg(msg$14);

    }

    @Override
    public void mouseExited(MouseEvent e) {

        statusBar.clearMsg();

    }

}//classe privada ShowMsg

/*
*
*/
private final class StartButtonActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        MainFrame mainFrame = (MainFrame)getTopLevelAncestor();
        mainFrame.getHtmlProgressBar().resetCounter();
        mainFrame.getEditProgressBar().resetCounter();
        mainFrame.getStaticProgressBar().resetCounter();
        mainFrame.getCssProgressBar().resetCounter();

        mainFrame.centerPanelVisible(true);

        boolean isFullBackup = mainFrame.getFullBackupRadioButton().isSelected();
        boolean isPrivateAreaBackup = mainFrame.getPrivateAreaRadioButton().isSelected();

        String backupType = 
            (isFullBackup ? msg$1 : msg$2) + " : " + (isPrivateAreaBackup ? msg$4 : msg$3);

        Terminal terminal = mainFrame.getTerminal();

        terminal.appendln(backupType);
        toolbox.log.Log.println("******** INICIOU PROCESSO DE BACKUP ********");
        toolbox.log.Log.println(backupType);

        BACKUP_ELAPSED_TIME.start();            

        Thread thread = new Thread(new phantom.threads.StartBackup());
        thread.start();
    }
    
}//classe privada StartButtonActionListener

/*
*
*/
private final class BrowseButtonActionListener implements ActionListener {

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void actionPerformed(ActionEvent ae) {

        try {
            
            MainFrame mainFrame = (MainFrame)getTopLevelAncestor();
            
            boolean isPrivateAreaBackup = mainFrame.getPrivateAreaRadioButton().isSelected();               

            String ext = isPrivateAreaBackup ? ".htm" : ".html"; 

            toolbox.file.FileTools.openWebPage(new File(ROOT_DIR + FORUM_NAME + ext));

        } 
        catch (IllegalArgumentException | NoSuchFileException ex) {

            phantom.exception.ExceptionTools.errMessage(null, ex);
        }
        catch (Exception ex) {

            phantom.exception.ExceptionTools.crashMessage(null, ex);

        }  

    }
    
}//classe privada BrowseButtonActionListener

/*
*
*/
private final class ListTopicsButtonActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        Thread thread = new Thread(new phantom.threads.BuildOrderedList(0));
        thread.start();
    }
    
}//classe privada ListTopicsButtonActionListener   

/*
*
*/
private final class RadioButtonActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        showStatus();
        
    }
    
    
}//classe privada RadioButtonActionListener
 

}//classe NorthPanel