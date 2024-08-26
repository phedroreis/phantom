package phantom.log;

import java.io.FileNotFoundException;
import static phantom.def.DefStrings.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Log {
    
   
    /**
     * Cria e abre um novo arquivo de log cujo nome e a data e hora da criacao deste arquivo.
     * 
     * @throws FileNotFoundException Se o arquivo nao puder ser criado.
     */
    public static void createLogFile() throws FileNotFoundException {
        
        toolbox.log.Log.createLogFile(LOG_DIR.toString());        
        
    }//createLogFile

}//classe Log
