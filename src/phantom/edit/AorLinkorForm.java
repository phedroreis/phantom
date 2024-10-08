package phantom.edit;

import java.util.HashMap;
import static phantom.global.GlobalConstants.*;


/***********************************************************************************************************************
 * Realiza o parsing e edita atributos href e src de tags a, link ou form.
 * 
 * @author Pedro Reis 
 * 
 * @since 1.0 - 18 de setembro de 2024
 * 
 * @version 1.0 
 **********************************************************************************************************************/
final class AorLinkorForm extends Tag {
    
    /*******************************************************************************************************************
     * Analisa e edita uma url coletada de atributo href ou src de uma tag a, link ou form.
     * 
     * @param url A url.
     * 
     * @throws Exception Qualquer excecao aqui leva ao abortamento do programa.
     ******************************************************************************************************************/
    @Override
        protected void parseUrl(
        final String url, 
        final phantom.fetch.Fetcher fetcher,
        final HashMap<String, String> url2staticUrl) throws Exception {
        
        if (url == null || !( url.matches("(\\./|/app\\.php)[\\s\\S]+") || url.startsWith(ROOT_URL) )) 
            return;
        
        setFetcher(fetcher);
        setUrl2staticUrl(url2staticUrl); 
        
        /*
        Nesse ponto da execucao do programa, paginas geradas por scripts PHP ja foram todas baixadas,
        exceto as do script file.php.
        Mas suas URLs nos arquivos estaticos precisam ser editadas para apontar para as copias estaticas
        locais que foram (arquivos html) que foram geradas por estes scripts PHP.
        */
        matcher = PHP_SCRIPT.matcher(url);
        
        String staticUrl = "#";//Ira conter a versao estatica da URL original
        
        String urlRelative = url.replace(ROOT_URL, "./");
        
    
        if (matcher.find()) {//Eh URL relativa ou absoluta para script PHP
            
            String phpScript = matcher.group();
            
            String startIndex;//Captura o indice (start=) da pagina, se houver
 
            switch (phpScript) {//Cada tipo de URL p/ script PHP eh editado de forma diferente
                
                case "/index.php"://Este script gera a pag. inicial do forum
                    
                    boolean isPrivateAreaBackup = 
                        phantom.gui.MainFrame.getPrivateAreaRadioButtonReference().isSelected();
                    
                    staticUrl = 
                        "./" + (isPrivateAreaBackup ? FORUM_NAME + ".htm" : MAIN_PAGE_FILE);                    
                    
                    break;    
                    
                case "/viewforum.php"://Este script gera pags. de Header ou Section
                    
                    matcher = START_INDEX.matcher(url);//Localiza indice da pag. de Section, se houver
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : ""; 
                    
                    matcher = VIEWFORUM_ID.matcher(url);//Localiza ID do forum na URL
                    
                    if (matcher.find()) staticUrl = "./" + matcher.group() + startIndex + ".html"; 
                    
                    break;     
                    
                case "/viewtopic.php"://Este script gera pags. de Topic
              
                   
                    matcher = START_INDEX.matcher(urlRelative);//Localiza indice pag. de Topic, se houver
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : "";                    

                    matcher = VIEWTOPIC_POST.matcher(urlRelative);//Localiza ref. para post, se houver
                    String postID = matcher.find() ? matcher.group() : "";                    
                   
                    matcher = VIEWTOPIC_ID.matcher(urlRelative);//Localiza ID do topico
                    
                    if (matcher.find()) 
                        staticUrl = "./" + matcher.group(1) + startIndex + ".html" + postID;
                    else
                        staticUrl = "./_post.html" + postID;//Monta URL estatica
                    
                    break;
                    
                case "/file.php"://Este script busca arquivos 
                   
                    matcher = FILE_PHP.matcher(urlRelative);
                    
                    if (matcher.find()) {

                        staticUrl = 
                            urlRelative.substring(
                                0, 
                                urlRelative.indexOf("file.php?")
                            )
                            + matcher.group(1);//url.replace("file.php?", "").replace("&amp;", "-");
                       
                        queue(urlRelative, staticUrl);
                    }

                    break;
                    
                case "/app.php":  
                case "/memberlist.php": 
                case "/posting.php":  
                case "/search.php":                    
                case "/ucp.php":    
                    
                    staticUrl = phpScript.replace("/", "./_").replace("php", "html");
              
            }//switch
            
            if (!staticUrl.equals("#")) url2staticUrl(url, staticUrl);

        }
        else  {//URL absoluta ou relativa para arq. estatico no servidor do forum 

            /*
            Aponta para arquivo no servidor do forum com URL absoluta, mas nao eh script Php
            Entao deve ser convertida para URL relativa na copia estatica
            */
            if (url.startsWith(ROOT_URL)) url2staticUrl(url, urlRelative);
            
            //Localiza a query da URL, se existir
            matcher = QUERY.matcher(url);
            String query = matcher.find() ? matcher.group() : ""; 
            
            /*
            Nem a URL e nem o nome do arquivo gravado devem incluir a query.
            */
            urlRelative = urlRelative.replace(query, "");             
    
            //Insere url relativa pra baixar excluida de sua query (caso tenha uma)
            queue(urlRelative, urlRelative);
        }   
        
    }//parseUrl

}//classe AorLinkorForm
