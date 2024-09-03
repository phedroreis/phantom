package phantom.exception;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class ExceptionTools {
    
    /**
     * 
     * @param e 
     */
    public static void crash(final Exception e) {
        
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
        
        toolbox.log.Log.println(e.toString());
        
        e.printStackTrace(toolbox.log.Log.getStream());

        JOptionPane.showMessageDialog(
            parentComponent, 
            e,
            "Fatal", 
            JOptionPane.ERROR_MESSAGE
        );
        
        crash(e);
        
    }//crashMessage

}//classe ExceptionTools
