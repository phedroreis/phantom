package phantom.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import phantom.global.GlobalStrings;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
public final class Initializer {
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    private static void createLogDir() throws Exception {
        
        toolbox.file.FileTools.createDirsIfNotExists(LOG_DIR);
        
    }//createLogDir  
    
    /*
     *
     */
    private static void createDirs() throws Exception {
        
        toolbox.log.Log.exec("phantom.main", "Initializer", "createDirs");
        
        EnumSet<GlobalStrings> Dir = 
            EnumSet.range(GlobalStrings.CONFIG_DIR, GlobalStrings.RAW_PAGES_DIR);
        
        for (GlobalStrings dir : Dir) {
            
            String pathname = dir.get();
            
            toolbox.log.Log.println(pathname + " : criado se inexistia");
            
            toolbox.file.FileTools.createDirsIfNotExists(pathname);
        }
        
        toolbox.log.Log.ret("phantom.main", "Initializer", "createDirs");
        
    }//createDirs
    
    /*
    *
    */
    private static void createFilesIfNotExists() {
        
        toolbox.log.Log.exec("phantom.main", "Initializer", "createFilesIfNotExists"); 
        
        EnumSet<GlobalStrings> Php = 
            EnumSet.range(GlobalStrings.UPDATE_PATHNAME, GlobalStrings.HELP_PATHNAME);
        
        phantom.resources.Resources rsc = new phantom.resources.Resources();
        
        for (GlobalStrings php : Php) {
            
            String targetPathname = php.get();
            
            File file = new File(targetPathname);
            
            if (!file.exists()) {
                
                String sourcePathname = file.getName();

                try (PrintWriter p = new PrintWriter(targetPathname, "utf8")) {
                    
                    String contentFile = 
                        toolbox.textfile.TextFileTools.readTextFileFromInputStream(
                            rsc.getResourceAsStream(sourcePathname),
                            "utf8"
                        );                    
                    
                    toolbox.log.Log.println(targetPathname + " : criado");
                    
                    p.println(contentFile);
                    
                    p.close();
                    
                }
                catch (IOException e) {
                    
                    phantom.exception.ExceptionTools.crashMessage(null, e);
                } 
               
            }//if         

        }//for
        
        toolbox.log.Log.ret("phantom.main", "Initializer", "createFilesIfNotExists");  
        
    }//createHtmlFiles
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public static void init() throws Exception {
        
        //Cria, se nao existir ainda, o diretorio onde sera gravado o arquivo de log
        createLogDir();
        
        //Cria arquivo de log. O nome sera a date e hora atual.
        toolbox.log.Log.createLogFile(LOG_DIR);      
        
        //Cria todos os diretorios de trabalho caso ainda nao existam
        createDirs();
        
        //Cria todos os arquivos necessarios caso ainda nao existam
        createFilesIfNotExists();
        
    }//init

}//classe Initializer