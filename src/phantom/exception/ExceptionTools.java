package phantom.exception;

import java.awt.Component;
import javax.swing.JOptionPane;
import phantom.gui.GUInterface;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class ExceptionTools {
    
    /**
     * 
     * @param parentComponent
     * @param e 
     */
    public static void errMessage(
        final Component parentComponent, 
        final Exception e) {
 
        JOptionPane.showMessageDialog(
            parentComponent, 
            e,
            "", 
            JOptionPane.ERROR_MESSAGE
        );
       
    }//crashMessage    
    
    /**
     * 
     * @param e 
     */
    public static void crash(Exception e) {
        
        e.printStackTrace(System.err);
        
        System.exit(phantom.global.GlobalExceptionsCodes.getExceptionCode(e));  
        
    }//crash
    
    /**
     * 
     * @param parentComponent
     * @param e 
     */
    public static void crashMessage(
        final Component parentComponent, 
        final Exception e) {
        
        errMessage(parentComponent, e);
        
        toolbox.log.Log.println(e.toString());
        
        e.printStackTrace(toolbox.log.Log.getStream());
        
        GUInterface.killMainFrame();
        
        crash(e); 
        
    }//crashMessage

}//classe ExceptionTools
