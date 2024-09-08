package phantom.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.Properties;
import phantom.global.GlobalStrings;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
final class Initializer {
    
    /**
     * 
     * @return
     */
	public static String readDateTimeOfLastPostFromLastBackup() {

        toolbox.log.Log.exec(
            "phantom.main",
            "Initializer", 
            "readDateTimeOfLastPostFromLastBackup"
        ); 
        
        toolbox.log.Log.println(
            "Lendo data-hora da postagem mais recente do mais recente backup"
        );
        
        Properties props = new Properties();
        
        try ( FileInputStream in = new FileInputStream(UPDATE_PATHNAME) ) { 
            
            props.load(in); 
        }
        catch (IOException e) {
            
            toolbox.log.Log.ret(
                "phantom.main",
                "Initializer", 
                "readDateTimeOfLastPostFromLastBackup",
                ANCIENT_TIMES
            );   
            
            return ANCIENT_TIMES;
            
        }
  
        String last = props.getProperty("last");

        if (last == null) {
            
            toolbox.log.Log.ret(
                "phantom.main",
                "Initializer", 
                "readDateTimeOfLastPostFromLastBackup",
                ANCIENT_TIMES
            );  
            
            return ANCIENT_TIMES;
        }
        
        toolbox.log.Log.ret(
            "phantom.main",
            "Initializer", 
            "readDateTimeOfLastPostFromLastBackup",
            last
        );         
        
        return last;
        
    }//readDateTimeOfLastPostFromLastBackup 
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    private static void createLogDir() throws FileNotFoundException {
        
        toolbox.file.FileTools.createDirsIfNotExists(LOG_DIR);
        
    }//createLogDir  
    
    /*
     *
     */
    private static void createDirs() throws FileNotFoundException {
        
        toolbox.log.Log.exec("phantom.main", "Initializer", "createDirs");
        
        EnumSet<GlobalStrings> Dir = 
            EnumSet.range(GlobalStrings.CONFIG_DIR, GlobalStrings.RAW_PAGES_DIR);
        
        for (GlobalStrings dir : Dir) {
            
            String pathname = dir.get();
            
            toolbox.log.Log.println(pathname + ": criando se inexistir");
            
            toolbox.file.FileTools.createDirsIfNotExists(pathname);
        }
        
        toolbox.log.Log.ret("phantom.main", "Initializer", "createDirs");
        
    }//createDirs
    
    /*
    *
    */
    private static void createHtmlFiles() {
        
        EnumSet<GlobalStrings> Php = 
            EnumSet.range(GlobalStrings.APPPHP_PATHNAME, GlobalStrings.UCPPHP_PATHNAME);
        
        for (GlobalStrings php : Php) {
            
            String pathname = php.get();
            
            File file = new File(pathname);
            
            if (!file.exists()) {
                
                try (PrintWriter p = new PrintWriter(pathname, "utf8")) {
                    
                    p.println(GlobalStrings.getHtml(pathname));
                    p.close();
                    
                }
                catch (FileNotFoundException | UnsupportedEncodingException e) {} 
               
            }//if         

        }//for
        
    }//createHtmlFiles
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public static void init() throws FileNotFoundException {
        
        //Cria, se nao existir ainda, o diretorio onde sera gravado o arquivo de log
        createLogDir();
        
        //Cria arquivo de log. O nome sera a date e hora atual.
        toolbox.log.Log.createLogFile(LOG_DIR);      
        
        //Cria todos os diretorios de trabalho caso ainda nao existam
        createDirs();
        
        createHtmlFiles();
        
    }//init

}//classe Initializer