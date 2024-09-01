package phantom.global;

/**
 * Classe para declarar Strings constantes acessiveis a todos os outros pacotes do projeto.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
public enum GlobalStrings {
    
    FORUM_NAME(Constants.FORUM_NAME),
    
    ROOT_URL(Constants.PROTOCOL + Constants.FORUM_NAME),
    
    FORUM_DIR(Constants.FORUM_DIR),
        
    LOG_DIR(Constants.LOG_DIR),
    
    CONFIG_DIR(Constants.CONFIG_DIR),
    
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
    
    public static final String PROTOCOL = "https://";
    
    public static final String FORUM_NAME = "clubeceticismo.com.br"; 
    
    public static final String FORUM_DIR = "./" + FORUM_NAME;
    
    public static final String LOG_DIR = "./log";
    
    public static final String CONFIG_DIR = "./config";
    
   public static final String RAW_PAGES_DIR = FORUM_DIR + "/rawpages";
     
    public static final String UPDATE_PATHNAME = LOG_DIR + "/update.properties";
    
    public static final String CONFIG_PATHNAME = CONFIG_DIR + "/config.properties";
    
}//classe privada Constants    
    
}//enum GlobalStrings