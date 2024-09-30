package phantom.main;

import java.io.File;
import java.util.LinkedList;
import static phantom.global.GlobalConstants.*;

/***********************************************************************************************************************
 * Realiza tarefas de finalizacao do programa. O metodo {@link finalizer() finalizer} sempre executa e eh o ultimo 
 * metodo a ser executado pelo programa.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0
 **********************************************************************************************************************/
public final class Finalizer {
    
    /**
     * Eecutado antes da janela principal ser fechada e encerrar o programa. Fecha o arquivo de log
     * e limpa o diretorio temporario para onde sao baixados os arquivos HTML. Por fim, deleta este 
     * diretorio.
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void finalizer() {
        
        toolbox.log.Log.closeFile();
        
        phantom.gui.MainFrame.setNorthPanelVisible(false);
        
        toolbox.file.SearchFolders searchFolders = new toolbox.file.SearchFolders(".+", false);
        
        try {
            
            File rawPagesDir = new File(RAW_PAGES_DIR);
            
            LinkedList<File> listFiles = searchFolders.search(rawPagesDir);
            
            for (File file : listFiles) file.delete();
            
            rawPagesDir.delete();

        } 
        catch (Exception ex) {}
        
    }//finalizer


}//classe Finalizer
