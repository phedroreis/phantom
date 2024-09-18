package phantom.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
    private static void createFiles() {
        
        toolbox.log.Log.exec("phantom.main", "Initializer", "createHtmlFiles"); 
        
        EnumSet<GlobalStrings> Php = 
            EnumSet.range(GlobalStrings.APPPHP_PATHNAME, GlobalStrings.UCPPHP_PATHNAME);
        
        for (GlobalStrings php : Php) {
            
            String pathname = php.get();
            
            File file = new File(pathname);
            
            if (!file.exists()) {
                
                try (PrintWriter p = new PrintWriter(pathname, "utf8")) {
                    
                    toolbox.log.Log.println(pathname + " : criado");
                    
                    p.println(GlobalStrings.getHtml(pathname));
                    p.close();
                    
                }
                catch (FileNotFoundException | UnsupportedEncodingException e) {
                    
                    e.printStackTrace(System.err);
                } 
               
            }//if         

        }//for
        
        String pathname = GlobalStrings.UPDATE_PATHNAME.get();
        
        File file = new File(pathname);
        
        if (!file.exists()) {
                
            try (PrintWriter p = new PrintWriter(pathname, "utf8")) {

                toolbox.log.Log.println(pathname + " : criado");

                p.println("privatearea=0000-00-00T00\\:00\\:00+00\\:00");
                p.println("publicarea=0000-00-00T00\\:00\\:00+00\\:00");
                p.close();

            }
            catch (FileNotFoundException | UnsupportedEncodingException e) {

                phantom.exception.ExceptionTools.crash(e);
            } 

        }//if           

        toolbox.log.Log.ret("phantom.main", "Initializer", "createHtmlFiles");  
        
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
        createFiles();
        
    }//init

}//classe Initializer