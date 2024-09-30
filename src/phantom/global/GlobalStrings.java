package phantom.global;

/***********************************************************************************************************************
 * Classe para declarar Strings constantes acessiveis a todos os outros pacotes do projeto.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 **********************************************************************************************************************/
public enum GlobalStrings {
    
    FORUM_NAME(Constants.FORUM_NAME),//clubeceticismo.com.br
    
    MAIN_PAGE_URL(Constants.MAIN_PAGE_URL),//https://clubeceticismo.com.br
    
    ROOT_URL(Constants.MAIN_PAGE_URL + '/'),//https://clubeceticismo.com.br/
    
    ROOT_DIR(Constants.ROOT_DIR),//./clubeceticismo.com.br/
        
    LOG_DIR(Constants.LOG_DIR),//./log/
    
    CONFIG_DIR(Constants.CONFIG_DIR),//./config/
    
    RAW_PAGES_DIR(Constants.RAW_PAGES_DIR),//./clubeceticismo.com.br/rawpages/
    
    FAILS_PATHNAME(Constants.FAILS_PATHNAME),//./log/fails.properties    
    
    UPDATE_PATHNAME(Constants.UPDATE_PATHNAME),//./log/update.properties
    
    CONFIG_PATHNAME(Constants.CONFIG_PATHNAME),//./config/config.properties
    
    APPPHP_PATHNAME(Constants.APPPHP_PATHNAME),    
    
    MEMBERLISTPHP_PATHNAME(Constants.MEMBERLISTPHP_PATHNAME),    

    POSTPHP_PATHNAME(Constants.POSTPHP_PATHNAME), 
    
    POSTINGPHP_PATHNAME(Constants.POSTINGPHP_PATHNAME), 
    
    SEARCHPHP_PATHNAME(Constants.SEARCHPHP_PATHNAME),
    
    UCPPHP_PATHNAME(Constants.UCPPHP_PATHNAME),  
    
    HELP_PATHNAME(Constants.HELP_PATHNAME),
    
    ANCIENT_TIMES("0000-00-00T00:00:00+00:00"),
    
    DISTANT_FUTURE("5000-00-00T00:00:00+00:00");    
    
    private String value;
    
    GlobalStrings(String value) {
        
        this.value = value;
        
    }//construtor   
    
    /*******************************************************************************************************************
     *
     * @return
     ******************************************************************************************************************/
    public String get() {
        
        return value;
        
    }//get

    
private static final class Constants {
  
    public static final String PROTOCOL = "https://";
    
    public static final String FORUM_NAME = "clubeceticismo.com.br"; 
    
    public static final String MAIN_PAGE_URL = PROTOCOL + FORUM_NAME;
    
    public static final String ROOT_DIR = "./" + FORUM_NAME + '/';
    
    public static final String LOG_DIR = "./log/";
    
    public static final String CONFIG_DIR = "./config/";
    
    public static final String RAW_PAGES_DIR = ROOT_DIR + "rawpages/";
    
    public static final String FAILS_PATHNAME = LOG_DIR + "download_fails.log";    
     
    public static final String UPDATE_PATHNAME = LOG_DIR + "update.properties";
    
    public static final String CONFIG_PATHNAME = CONFIG_DIR + "config.properties";
    
    public static final String APPPHP_PATHNAME = ROOT_DIR + "_app.html";     
    
    public static final String MEMBERLISTPHP_PATHNAME = ROOT_DIR + "_memberlist.html";    
    
    public static final String POSTPHP_PATHNAME = ROOT_DIR + "_post.html";
    
    public static final String POSTINGPHP_PATHNAME = ROOT_DIR + "_posting.html";
    
    public static final String SEARCHPHP_PATHNAME = ROOT_DIR + "_search.html";
    
    public static final String UCPPHP_PATHNAME = ROOT_DIR + "_ucp.html"; 
    
    public static final String HELP_PATHNAME = ROOT_DIR + "_help.html";                                    
    
}//classe privada Constants    
    
}//enum GlobalStrings
