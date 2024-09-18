package phantom.main;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Finalizer {
    
    public static void finalizer() {
        
        toolbox.log.Log.closeFile();
        
    }//finalizer


}//classe Finalizer
