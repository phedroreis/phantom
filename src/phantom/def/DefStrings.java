package phantom.def;

import java.io.FileNotFoundException;
import java.util.EnumSet;

/**
 *
 * @author Pedro Reis
 */
public enum DefStrings {
    
    FORUM_NAME(Constants.NAME),
    
    FORUM_URL("http://" + Constants.NAME + "/"),
    
    LOG_DIR("./log/"),
    
    CONFIG_DIR("./config"),
    
    FORUM_DIR("./" + Constants.NAME + "/"),
    
    INCREMENTAL_DIR("./" + Constants.NAME + Constants.INC_DIR + "/"),
    
    RAW_PAGES_DIR("./" + Constants.NAME + Constants.INC_DIR + Constants.RAW_DIR + "/");
    
    
    private String value;
    
    DefStrings(String value) {
        
        this.value = value;
        
    }//construtor   
    
    @Override
    public String toString() {
        
        return value;
        
    }//toString
    
    public static void createDirs() throws FileNotFoundException {
       
        EnumSet<DefStrings> Dir = EnumSet.range(LOG_DIR, RAW_PAGES_DIR);
        
        for (DefStrings dir : Dir) toolbox.file.FileTools.createDirsIfNotExists(dir.toString());
        
    }//createDirs
    
private static final class Constants {
    
    public static final String NAME = "clubeceticismo.com.br"; 
    
    public static final String INC_DIR = "/incremental";
    
    public static final String RAW_DIR = "/rawpages";
    
}//classe privada Constants    
    
}//enum DefStrings
