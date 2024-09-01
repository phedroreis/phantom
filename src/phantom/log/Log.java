package phantom.log;

import java.io.FileNotFoundException;
import static phantom.global.GlobalStrings.*;

/**
 * Classe com o metodo para criar e abrir para escrita o arquivo de log.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
public final class Log {
    
   
    /**
     * Cria e abre um novo arquivo de log cujo nome e a data e hora da criacao deste arquivo.
     * 
     * @throws FileNotFoundException Se o arquivo nao puder ser criado.
     */
    public static void createLogFile() throws FileNotFoundException {
        
        toolbox.log.Log.createLogFile(LOG_DIR.get());        
        
    }//createLogFile

}//classe Log
