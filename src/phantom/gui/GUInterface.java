package phantom.gui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class GUInterface {
    
    private static String dateTimeOfLastPostFromLastBackup;
    
    protected static final Border STANDART_BORDER = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    
    private static final MainFrame MAIN_FRAME = new MainFrame();

    public static void setDateTimeOfLastPostFromLastBackup(final String datetime) {

        dateTimeOfLastPostFromLastBackup = datetime;

    }//setDateTimeOfLastPostFromLastBackup 
    
    public static String getDateTimeOfLastPostFromLastBackup() {

        return dateTimeOfLastPostFromLastBackup;

    }//getDateTimeOfLastPostFromLastBackup     


    public static void showMainFrame() {
        toolbox.log.Log.exec("phantom.gui", "GUInterface", "showMainFrame");
        MAIN_FRAME.setVisible(true);
        toolbox.log.Log.println("Abriu janela de interface");
        toolbox.log.Log.ret("phantom.gui", "GUInterface", "showMainFrame");  
        
    }//showMainFrame
    
    public static void killMainFrame() {
       
        if (MAIN_FRAME.isShowing()) 
            MAIN_FRAME.dispatchEvent(new WindowEvent(MAIN_FRAME, WindowEvent.WINDOW_CLOSING));
       
    }//killMainFrame
    
    public static int getMainFrameHeight() {
        
        return MAIN_FRAME.getHeight();
        
    }//getMainFrameHeight
    
    public static int getMainFrameWidth() {
        
        return MAIN_FRAME.getWidth();
        
    }//getMainFrameWidth
    
    public static int getMainFramePreferredWidth() {
        
        return MainFrame.PREFERRED_WIDTH;
        
    }//getMainFramePreferredWidth    
    
    public static int getMainFramePreferredHeight() {
        
        return MainFrame.PREFERRED_HEIGHT;
        
    }//getMainFramePreferredHeight
    
    public static void setMainFrameMinimumSize(final int width, final int height) {
        
        MAIN_FRAME.setMinimumSize(
            new Dimension(MainFrame.PREFERRED_WIDTH, MainFrame.PREFERRED_HEIGHT)
        );
        
    }//setMainFrameMinimumSize
    
    public static void setMainFrameSize(final int width, final int height) {
                
        MAIN_FRAME.setSize(width, height);
        MAIN_FRAME.terminalResize();
        
    }//mainFrameSetSize
    
    public static boolean isFullBackup() {
        
        return MAIN_FRAME.isFullBackup();
        
    }  
    
    public static boolean isPrivateAreaBackup() {
        
        return MAIN_FRAME.isPrivateAreaBackup();
        
    }  
    
    public static void northPanelSetVisible(final boolean isVisible) {
        
        MAIN_FRAME.northPanelSetVisible(isVisible);
        
    }

    public static void centerPanelSetVisible(final boolean isVisible) {
        
        MAIN_FRAME.centerPanelSetVisible(isVisible);
        
    }

    public static void southPanelSetVisible(final boolean isVisible) {
        
        MAIN_FRAME.southPanelSetVisible(isVisible);
        
    }      
   
    public static void progressBarSetValue(final int indexBar, final int value) {
        
        MAIN_FRAME.progressBarSetValue(indexBar, value);
    }
    
    public static void progressBarConcurrentSetValue(final int indexBar, final int value) {
        
        MAIN_FRAME.progressBarConcurrentSetValue(indexBar, value);
        
    }
    
    public static void progressBarSetMaximum(final int indexBar, final int max) {
        
        MAIN_FRAME.progressBarSetMaximum(indexBar, max);
        
    }
    
    public static void progressBarConcurrentSetMaximum(final int indexBar, final int max) {
        
        MAIN_FRAME.progressBarConcurrentSetMaximum(indexBar, max);
        
    }  
    
    public static void terminalConcurrentAppendln(final String text) {
        
        MAIN_FRAME.terminalConcurrentAppendln(text);
        
    }
    
    public static void terminalSendTerminateSignal(final String signal) throws Exception {
        
        MAIN_FRAME.terminalSendTerminateSignal(signal);
        
    }
    
    public static void terminalResize() {
        
        MAIN_FRAME.terminalResize();
        
    }
 
}//classe GUInterface
