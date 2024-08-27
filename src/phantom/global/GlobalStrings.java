package phantom.global;

import java.io.FileNotFoundException;
import java.util.EnumSet;

/**
 *
 * @author Pedro Reis
 */
public enum GlobalStrings {
    
    FORUM_NAME(Constants.FORUM_NAME),
    
    FORUM_URL("http://" + Constants.FORUM_NAME),
    
    FORUM_DIR(Constants.FORUM_DIR),
        
    LOG_DIR(Constants.LOG_DIR),
    
    CONFIG_DIR(Constants.CONFIG_DIR),
    
    INCREMENTAL_DIR(Constants.INCREMENTAL_DIR),
    
    RAW_PAGES_DIR(Constants.RAW_PAGES_DIR),
    
    UPDATE_PATHNAME(Constants.UPDATE_PATHNAME),
    
    CONFIG_PATHNAME(Constants.CONFIG_PATHNAME),
    
    ANCIENT_TIMES("0000-00-00T00:00:00+00:00"),
    
    DISTANT_FUTURE("5000-00-00T00:00:00+00:00");    
    
    private String value;
    
    GlobalStrings(String value) {
        
        this.value = value;
        
    }//construtor   
    
    public String get() {
        
        return value;
        
    }//get
    
private static final class Constants {
    
    public static final String FORUM_NAME = "clubeceticismo.com.br"; 
    
    public static final String FORUM_DIR = "./" + FORUM_NAME;
    
    public static final String LOG_DIR = "./log";
    
    public static final String CONFIG_DIR = "./config";
    
    public static final String INCREMENTAL_DIR = FORUM_DIR + "/incremental";
    
    public static final String RAW_PAGES_DIR = FORUM_DIR + "/rawpages";
     
    public static final String UPDATE_PATHNAME = LOG_DIR + "/update.properties";
    
    public static final String CONFIG_PATHNAME = CONFIG_DIR + "/config.properties";
    
}//classe privada Constants    
    
}//enum GlobalStrings