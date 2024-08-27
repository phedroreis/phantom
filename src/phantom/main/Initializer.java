package phantom.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;
import phantom.global.GlobalStrings;
import static phantom.global.GlobalStrings.*;

/**
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
public final class Initializer {
    
    /**
     * 
     * @return
     * 
     * @throws IOException 
     */
	public static String getDateTimeOfLastPostFromLastBackup() throws IOException {
        
        Properties props = new Properties();
        
        String ancientTimes = ANCIENT_TIMES.get();
        
        String updatePathname = UPDATE_PATHNAME.get();
        
        String last;
        
        try (FileInputStream in = new FileInputStream(updatePathname);) { props.load(in); }

  
        last = props.getProperty("last");

        if (last == null) last = props.getProperty("nexttolast");

        if (last == null) last = ancientTimes; 
        
        props.setProperty("nexttolast", last);  

        props.remove("last");        
        
        
        try (FileOutputStream out = new FileOutputStream(updatePathname);) {
            
            props.store(out, "Starting backup...");
            
        }
        catch (IOException e) {
            
            e.printStackTrace(System.err);
            
            throw new IOException("Unable to save last backup date-time");
        }
        
        return last;
        
    }//getDateTimeOfLastPostFromLastBackup 
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    private static void createLogDir() throws FileNotFoundException {

            toolbox.file.FileTools.createDirsIfNotExists(LOG_DIR.get());
        
    }//createLogDir  
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    private static void createDirs() throws FileNotFoundException {
        
        toolbox.log.Log.exec("phantom.main", "Initializer", "createDirs");
        
        EnumSet<GlobalStrings> Dir = EnumSet.range(CONFIG_DIR, RAW_PAGES_DIR);
        
        for (GlobalStrings dir : Dir) {
            
            String pathname = dir.get();
            
            toolbox.log.Log.println( pathname + ": criando se inexistir" );
            
            toolbox.file.FileTools.createDirsIfNotExists(pathname);
        }
        
        toolbox.log.Log.ret("phantom.main", "Initializer", "createDirs");
        
    }//createDirs
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public static void init() throws FileNotFoundException {
        
        createLogDir();
        
        //Cria arquivo de log. O nome sera a date e hora atual.
        phantom.log.Log.createLogFile();        
        
        //Cria todos os diretorios de trabalho caso ainda nao existam
        createDirs();    

        
    }//init

}//classe Initializer
