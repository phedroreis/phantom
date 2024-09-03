package phantom.edit;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.modelmbean.XMLParseException;
import static phantom.global.GlobalStrings.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class HrefSrcEditor {
    
    private static final String MAIN_PAGE_FILE = FORUM_NAME.get() + ".html";
    
    private static final String FORUM_URL = ROOT_URL.get();
    
    private static HashMap<String, String> urlsMap;
    
    private static phantom.fetch.Fetch fetch;
    
    @SuppressWarnings("UnusedAssignment")
    public void edit() throws IOException, XMLParseException, InterruptedException {
        
        toolbox.file.SearchFolders searchFolders = new toolbox.file.SearchFolders(new PagesFilter());
        
        LinkedList<String> listPages = searchFolders.search(RAW_PAGES_DIR.get());
        
        fetch = new phantom.fetch.Fetch();
        
        Thread downloadThread = new Thread(fetch);
        
        downloadThread.start();
        
        for (String filename : listPages) {
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(filename, "utf8");
            
            textFileHandler.read();
            
            String content = textFileHandler.getContent();
            
            toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(content, new Parser());
            
            urlsMap = new HashMap<>();
            
            htmlParser.parse();
            
            StringBuilder sbContent = new StringBuilder(content); 
            
            content = null; textFileHandler.setContent(content);//Para liberar memoria
                        
            for (String key : urlsMap.keySet()) {
                
                String value = urlsMap.get(key);
                
                toolbox.string.StringTools.replace(sbContent, key, value);
                
            }//for
            
            /*
            URLs absolutas apontando para pag. principal passam a apontar arq. estatico
            da pag. principal.
            */
            toolbox.string.StringTools.replace(
                sbContent, 
                MAIN_PAGE_URL.get() + "\"",
                "./" + MAIN_PAGE_FILE + "\""
            );
            
            textFileHandler.setContent(sbContent.toString());
            
            textFileHandler.write();
            
        }//for
        
        fetch.terminate();
        
    }//edit
    
    public static void main(String[] args) throws IOException, XMLParseException, InterruptedException {
        HrefSrcEditor f = new HrefSrcEditor();
        f.edit();
    }
    
/*======================================================================================================================
           Classe privada para o parsing dos arquivos HTML das paginas do forum. 
======================================================================================================================*/
private static final class Parser extends toolbox.xml.TagParser {
    
    /*
    Localiza /nomeDoScript.php em URLs relativas ou absolutas para scripts PHP
    */
    private static final Pattern PHP_SCRIPT = Pattern.compile("/[^/]*?\\.php");
    
    private static final Pattern VIEWFORUM_ID = Pattern.compile("f=\\d+");//Loc. ID de Header ou Section
    
    private static final Pattern VIEWFORUM_START_INDEX = Pattern.compile("start=(\\d+)");
    
    private static final Pattern VIEWTOPIC_ID = Pattern.compile("t=\\d+");//Localiza ID de Topic 
    
    /*
    Antes de aplicar esta regex, troca start= para xyz= e entao busca por x=. Para que VIEWTOPIC_ID nao 
    seja localizado em start=
    */
    private static final Pattern VIEWTOPIC_START_INDEX = Pattern.compile("xyz=(\\d+)");

    private static final Pattern VIEWTOPIC_POST = Pattern.compile("#p\\d+");//Loc. ref para post em pag. Topic
    
    private static final Pattern QUERY = Pattern.compile("\\?.*$");//Localiza queries em arqs. js    

    private static Matcher matcher;  
    
    /*==================================================================================================================
    * Recebe URLs dos atributos href/src das tags a, img, link, form e script que sejam URLs para arquivos
    * estaticos no servidor do forum ou que apontem para scripts PHP tb no servidor do forum.
    * 
    * URLs PHP serao convertidas, na copia estatica, para apontarem para os arquivos HTML estaticos recebidos
    * por seus respectivos scripts.
    *
    * URLs apontando para arquivos estaticos (nao PHP) no servidor do forum, nao serao alteradas, mas 
    * estes arquivos serao baixados para a estrutura de arquivos da copia estatica local (que sera criada
    * espelhando a propria estrutura do servidor).   
    ==================================================================================================================*/    
    private void editUrlsAndDownloadFiles(final String url) throws InterruptedException {
        
        /*
        Nesse ponto da execucao do programa, paginas geradas por scripts PHP ja foram todas baixadas.
        Mas suas URLs nos arquivos estaticos precisam ser editadas para apontar para as copias estaticas
        locais que foram (arquivos html) que foram geradas por estes scripts PHP.
        */
        matcher = PHP_SCRIPT.matcher(url);
       
        String startIndex;//Captura o indice (start=) da pagina, se houver
        
        String staticUrl = "#";//Ira conter a versao estatica da URL original
    
        if (matcher.find()) {//Eh URL relativa ou absoluta para script PHP
            
            String phpScriptName = matcher.group();

            switch (phpScriptName) {//Cada tipo de URL p/ script PHP eh editado de forma diferente
                
                case "/index.php"://Este script gera a pag. inicial do forum
                    
                    staticUrl = "./" + MAIN_PAGE_FILE;
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");                    
                    
                    break;    
                    
                case "/viewforum.php"://Este script gera pags. de Header ou Section
                    
                    matcher = VIEWFORUM_START_INDEX.matcher(url);//Localiza indice da pag. de Section, se houver
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : ""; 
                    
                    matcher = VIEWFORUM_ID.matcher(url);//Localiza ID do forum na URL
                    staticUrl = matcher.find() ? "./" + matcher.group() + startIndex + ".html" : "#"; 
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");//Mapeia para edicao do arquivo
                    
                    break;     
                    
                case "/viewtopic.php"://Este script gera pags. de Topic
                    
                    /*
                    t= nao pode ser localizada dentro de start= pois bugaria regex VIEWTOPIC_ID
                    Troca start= por xyz= e busca por xyz= na string urlx
                    */
                    String urlx = url.replace("start=", "xyz=");                    
                   
                    matcher = VIEWTOPIC_START_INDEX.matcher(urlx);//Localiza indice pag. de Topic, se houver
                    startIndex = matcher.find() ? "&start=" + matcher.group(1) : "";                    

                    matcher = VIEWTOPIC_POST.matcher(urlx);//Localiza ref. para post, se houver
                    String postID = matcher.find() ? matcher.group() : "";                    
                   
                    matcher = VIEWTOPIC_ID.matcher(urlx);//Localiza ID do topico
                    staticUrl = 
                        matcher.find() ? 
                        "./" + matcher.group() + startIndex + ".html" + postID :
                        "./_post.html" + postID;//Monta URL estatica
                    
                    urlsMap.put(url + "\"", staticUrl + "\"");//Mapeia para edicao do arquivo
                    
                    break;
                    
                case "/file.php"://Este script busca alguns arquivos de avatar de usuario   
                    
                    break;
                    
                case "/app.php":  
                case "/memberlist.php": 
                case "/posting.php":  
                case "/search.php":                    
                case "/ucp.php":    
                    
                    staticUrl = phpScriptName.replace("/", "./_").replace("php", "html");

                    urlsMap.put(url + "\"", staticUrl + "\""); 
                    
            }//switch

        }
        else  {//URL para arq. estatico no servidor do forum (precisa ser baixado para a copia local)
 
            //Retira parametros queries de arquivos a serem baixados
            matcher = QUERY.matcher(url);
            String query = matcher.find() ? matcher.group() : "";
            String editedUrl = url.replace(query, "");//Deleta query da URL que vai baixar o arquivo
            
            /*
            Aponta para arquivo no servidor do forum com URL absoluta, mas nao eh script Php
            */
            if (editedUrl.startsWith("http")) { 
                
                editedUrl = editedUrl.replace(FORUM_URL, "./");//Converte p/ URL relativa
                
                urlsMap.put(url + "\"", editedUrl + query + "\"");//Insere versao rel. da URL na copia estatica  
            }
            
            fetch.queue(editedUrl);//Insere url na fila para baixar
        }   
        
    }//editUrlsAndDownloadFiles
    
   /*==================================================================================================================
    * Busca pelos atributos href e src das tags a, img, link, form e script que apontem para arquivos no
    * servidor do forum. 
    ==================================================================================================================*/    
    @Override
    public void openTag(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();
        
        HashMap<String, String> map = t.getAttrMap();
        
        String url = null;
       
        switch (tagName) {
            
            case "a":
            case "link":
                url = map.get("href");
                break;
                
            case "img":
            case "script":
                url = map.get("src");
                break;
                
            case "form":
                url = map.get("action");
  
        }//switch
        
        /*
        Baixa arquivo e edita URL no atr. href/src se for URL relativa ou absoluta
        apontando para dominio do forum. Ou seja, apenas URL que comecem com 
        https://clubeceticismo.com.br/ ou ./ ou ../ serao baixadas.
        
        Se for relativa para script PHP, passara a apontar, na copia estatica, para o arquivo
        local gerado e enviado por este script.
        
        Se for relativa apontando para arquivo no servidor, o arquivo e baixado e a URL nao eh
        editada nos arquivos estaticos.
        
        Se for aboluta e apontando para servidor do forum, e convertida na copia estatica para 
        URL relativa.
        
        Qualquer outro tipo de URL sera ignorado.
        */
        if ( url != null && (url.matches("(\\.\\.?/|/app\\.php).+") || url.startsWith(FORUM_URL)) ) {
            
            try {
                
                editUrlsAndDownloadFiles(url);
                
            } 
            catch (InterruptedException e) {//Ocorreu excecao na thread que baixa os arquivos
                
                phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            }
        }
        
    }//openTag    
  
}//classe privada Parser

/*======================================================================================================================
 *   Classe privada: seleciona apenas arquivos de paginas HTML do forum que foram geradas por scripts PHP
 *   e baixadas previamente.
 *
 *   Este filtro produzira a lista de arquivos que sera processada no metodo edit() desta classe.
======================================================================================================================*/
private static class PagesFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) throws IOException {
         
        String fileName = file.getFileName().toString();
        
        /*
        Retorna true apenas para arq. de pag. principal, header, section ou topic
        */
        return (fileName.matches("(f|t)=\\d+(&start=\\d+){0,1}\\.html") || fileName.equals(MAIN_PAGE_FILE)) ;
    }
     
}//classe privada PagesFilter   

}//classe HrefSrcEditor