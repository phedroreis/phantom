package phantom.global;

/**
 * Classe para declarar Strings constantes acessiveis a todos os outros pacotes do projeto.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 27 de agosto de 2024
 */
public enum GlobalStrings {
    
    FORUM_NAME(Constants.FORUM_NAME),//clubeceticismo.com.br
    
    MAIN_PAGE_URL(Constants.MAIN_PAGE_URL),//https://clubeceticismo.com.br
    
    ROOT_URL(Constants.MAIN_PAGE_URL + '/'),//https://clubeceticismo.com.br/
    
    ROOT_DIR(Constants.ROOT_DIR),//./clubeceticismo.com.br/
        
    LOG_DIR(Constants.LOG_DIR),//./log/
    
    CONFIG_DIR(Constants.CONFIG_DIR),//./config/
    
    RAW_PAGES_DIR(Constants.RAW_PAGES_DIR),//./clubeceticismo.com.br/rawpages/
    
    UPDATE_PATHNAME(Constants.UPDATE_PATHNAME),//./log/update.properties
    
    FAILS_PATHNAME(Constants.FAILS_PATHNAME),//./log/fails.properties
    
    CONFIG_PATHNAME(Constants.CONFIG_PATHNAME),//./config/config.properties
    
    APPPHP_PATHNAME(Constants.APPPHP_PATHNAME),    
    
    MEMBERLISTPHP_PATHNAME(Constants.MEMBERLISTPHP_PATHNAME),    

    POSTPHP_PATHNAME(Constants.POSTPHP_PATHNAME), 
    
    POSTINGPHP_PATHNAME(Constants.POSTINGPHP_PATHNAME), 
    
    SEARCHPHP_PATHNAME(Constants.SEARCHPHP_PATHNAME),
    
    UCPPHP_PATHNAME(Constants.UCPPHP_PATHNAME),    
    
    ANCIENT_TIMES("0000-00-00T00:00:00+00:00"),
    
    DISTANT_FUTURE("5000-00-00T00:00:00+00:00");    
    
    private String value;
    
    GlobalStrings(String value) {
        
        this.value = value;
        
    }//construtor   
    
    /**
     *
     * @return
     */
    public String get() {
        
        return value;
        
    }//get
    
    /**
     *
     * @param filename
     * @return
     */
    public static String getHtml(String filename) {
        
        String body = "";
        
        switch (filename) {
            
            case Constants.APPPHP_PATHNAME:               
            case Constants.UCPPHP_PATHNAME:
                
                body = Constants.UCP_BODY;
                break;
                
            case Constants.MEMBERLISTPHP_PATHNAME:
                
                body = Constants.MEMBERLIST_BODY;  
                break;                
                
            case Constants.POSTPHP_PATHNAME:
                
                body = Constants.POST_BODY;
                break;
 
            case Constants.POSTINGPHP_PATHNAME:
                
                body = Constants.POSTING_BODY;   
                break;
                
            case Constants.SEARCHPHP_PATHNAME:
                
                body = Constants.SEARCH_BODY;                
        }
        
        return Constants.HEADER + body + "</body></html>";
    }

    
private static final class Constants {
    
    public static final String HEADER = 
"<!DOCTYPE html>\n<html dir=\"ltr\" lang=\"pt-br\">\n\n<head>\n<meta charset=\"utf-8\" />\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n<link rel=\"shortcut icon\" href=\"./favicon.png\" />\n\n" +
"<link href=\"./assets/css/font-awesome.min.css?assets_version=26\" rel=\"stylesheet\">\n" +
"<link href=\"./styles/basic_aqua/theme/stylesheet.css?assets_version=26\" rel=\"stylesheet\">\n" +
"<title>AVISO</title>\n<style>\n    h1, h2 { text-align: center; margin-top: 8%;  }\n</style>\n" +
"</head>\n\n<body>\n\n<header>\n<a id=\"logo\" class=\"logo\" href=\"./clubeceticismo.com.br.html\" title=\"Aviso\">\n" +
"<img src=\"./styles/basic_aqua/theme/images/logo.png\" data-src-hd=\"./styles/basic_aqua/theme/images/logo_hd.png\" alt=\"Clube Ceticismo\"/>\n</a>\n</header>\n<body>";    
    
    public static final String PROTOCOL = "https://";
    
    public static final String FORUM_NAME = "clubeceticismo.com.br"; 
    
    public static final String MAIN_PAGE_URL = PROTOCOL + FORUM_NAME;
    
    public static final String ROOT_DIR = "./" + FORUM_NAME + '/';
    
    public static final String LOG_DIR = "./log/";
    
    public static final String CONFIG_DIR = "./config/";
    
    public static final String RAW_PAGES_DIR = ROOT_DIR + "rawpages/";
     
    public static final String UPDATE_PATHNAME = LOG_DIR + "update.properties";
    
    public static final String FAILS_PATHNAME = LOG_DIR + "download_fails.log";
    
    public static final String CONFIG_PATHNAME = CONFIG_DIR + "config.properties";
    
    public static final String APPPHP_PATHNAME = ROOT_DIR + "_app.html";     
    
    public static final String MEMBERLISTPHP_PATHNAME = ROOT_DIR + "_memberlist.html";    
    
    public static final String POSTPHP_PATHNAME = ROOT_DIR + "_post.html";
    
    public static final String POSTINGPHP_PATHNAME = ROOT_DIR + "_posting.html";
    
    public static final String SEARCHPHP_PATHNAME = ROOT_DIR + "_search.html";
    
    public static final String UCPPHP_PATHNAME = ROOT_DIR + "_ucp.html"; 
    
    private static final String H1 = 
        "<h1>Voc\u00ea est\u00e1 navegando por uma c\u00f3pia est\u00e1tica do f\u00f3rum.</h1>\n";
    
    public static final String MEMBERLIST_BODY =    
        H1 + 
        "<h2>Sem acesso ao banco de dados com a lista de membros.</h2>";  
             
    public static final String POST_BODY =    
        H1 + 
        "<h2>Este tipo de link n\u00e3o pode ser gerado durante o processo de backup.</h2>" +
        "<p>Para corrigir este problema em sua c\u00f3pia est\u00e1tica, execute o programa fix.jar.</p>" +
        "<p>Obs: Se desejado que esse tipo de link aponte para sua respectiva postagem, " +
        "fix.jar dever\u00e1 ser executado ap\u00f3s cada processo de backup.</p>";  
    
    public static final String POSTING_BODY =    
        H1 + 
        "<h2>N\u00e3o \u00e9 poss\u00edvel inserir novas postagens.</h2>";  
    
    public static final String SEARCH_BODY =    
        H1 + 
        "<h2>N\u00e3o \u00e9 poss\u00edvel realizar pesquisas.</h2>";  
               
    public static final String UCP_BODY =    
        H1 + 
        "<h2>Funcionalidade n\u00e3o dispon\u00edvel.</h2>";  
                                     
    
}//classe privada Constants    
    
}//enum GlobalStrings