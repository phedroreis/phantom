package phantom.pages;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import javax.management.modelmbean.XMLParseException;
import static phantom.def.DefStrings.*;

/***********************************************************************************************************************
* Superclasse para as classes que analisam, coletam, armazenam e fornecem
* dados da PAGINA PRINCIPAL, das paginas de HEADERS, SECOES E TOPICOS do forum.
*
* @version 1.0
* @since 1.0 - 11 de agosto de 2024
* @author Pedro Reis
***********************************************************************************************************************/
public abstract class Page {
    

/**
 * Enumera constantes
 */    
protected enum MaxList {

   MAX_SECTIONS_TITLES_PER_PAGE(50),//Nao utilizado nesta versao
   MAX_TOPICS_TITLES_PER_PAGE(50),//Max. de titulos de topicos que podem ser listados em pags. de Secao
   MAX_POSTS_PER_PAGE(50);//Max. de posts que podem ser publicados em uma pag. de Topico
   
   private int max;
   
   MaxList(final int max) { this.max = max; }
   
   public int get() { return max; }
   
}//enum MaxList 
    
    /*==================================================================================================================
    * O nome do header, secao ou topico. Para um objeto da subclasse Main este campo armazenarah "Principal"
    ==================================================================================================================*/
    private String pageName;
    
    /*==================================================================================================================
    * O nome do arquivo onde a pagina do header, secao ou topico sera gravada. Armazenado sem a extensao
    * html e sem o sufixo de indexacao, que sao inseridos posteriormente pelo metodo getPageFilename().
    
    * Nesta versao, o nome do arquivo para gravar a pagina principal eh clubeceticismo.html
    ==================================================================================================================*/
    private String pageFilename;
    
    /*==================================================================================================================
    * A URL para baixar a 1a pagina de determinado header, secao ou topico. Sem sufixo de indexacao que
    * e introduzido pelo metodo getPageUrl().
    *
    * Na da pagina principal, este campo armazenarah a URL de acesso ao proprio forum.
    ==================================================================================================================*/
    private String absoluteURL; 
    
    /*
    *
    */
    private int numberOfPages;
    
    /*==================================================================================================================
    * Customiza o parser do arquivo html para recuperar os dados necessarios para um objeto 
    * de alguma subclasse de Page.
    ==================================================================================================================*/
    private toolbox.xml.TagParser parser;
    
    /*==================================================================================================================
    * Para a pagina principal, pageList retorna uma lista com objetos da subclasse Header, representando
    * os Headers (cabecalhos) do forum.
    * Para uma pagina de Header, armazena a lista de Sections deste Header.
    * Para uma pagina de Section, armazena a lista de Topics deste Section.
    * Para uma pagina de Topic deve armazenar <code>null</code>.
    ==================================================================================================================*/
    private LinkedList<Page> pagesList;
    
    /*
    *
    */
    private Calendar dateTimeOfLastPost;
    
    /*
    *
    */
    private String stringDateTimeOfLastPost; 
    
    /*
    *
    */
    private static Calendar dateTimeOfLastPostFromLastBackup;
    
    /*
    * Determina se o backup sera full ou incremental.
    */
    private static boolean isFullBackup;
    
    /*==================================================================================================================
    *        BLOCO DE INICIALIZACAO cria o objeto pagesList.
    ==================================================================================================================*/
    { 
        pagesList = new LinkedList<>();
        dateTimeOfLastPost = null;
        stringDateTimeOfLastPost = null;
    }
    
    /**
     * 
     * @param datetime
     */
    protected static void setDateTimeOfLastPostFromLastBackup(final String datetime) {
        
        dateTimeOfLastPostFromLastBackup = 
            toolbox.time.Util.htmlDateTimeToCalendar(datetime, -3);
        
    }//setDateTimeOfLastPostOnLastBackup
    
    /**
     * 
     * @param isFull 
     */
    protected static void setBackupMode(final boolean isFull) {
        
        isFullBackup = isFull;
        
    }//setBackupFullIsOn
    
    /**
     * 
     * @param n 
     */
    protected void setNumberOfPages(final int n) {
        
        numberOfPages = n;
        
    }//setNumberOfPages
    
    /*
     * retirar este metodo se continuar private e permitir acesso direto ao campo
     * @return 
     */
    private int getNumberOfPages() {
        
        return numberOfPages;
        
    }//getNumberOfPages
    
    /**
     * 
     * @param datetime 
     */
    public void setDateTimeOfLastPostOnThisPage(final String datetime) {        
        
        stringDateTimeOfLastPost = datetime;
        
        dateTimeOfLastPost = toolbox.time.Util.htmlDateTimeToCalendar(datetime, -3);
        
    }//setDateTimeOfLastPostOnThisPage
    
    /**
     * 
     * @return 
     */
    protected Calendar getDateTimeOfLastPostOnThisPage() {
        
        return dateTimeOfLastPost;
        
    }//getDateTimeOfLastPostOnThisPage 
    
    /**
     * 
     * @return 
     */
    protected String getStringDateTimeOfLastPostOnThisPage() {
        
        return stringDateTimeOfLastPost;
        
    }//getStringDateTimeOfLastPostOnThisPage
    
    /*******************************************************************************************************************
     * Este metodo permite que um objeto Header, Section ou Topic seja adicionado a pagesList.
     * 
     * @param page Objeto com dados da pagina de Header, Section ou Topic.
     ******************************************************************************************************************/
    protected void addPage(final Page page) {
        
        pagesList.add(page);
        
    }//addPage    
    
    /*******************************************************************************************************************
     * Atribui o nome com que o forum designa a pagina de Header, Section ou Topic.
     * Para a pagina principal sera atribuido pelo construtor, o nome Principal.
     * 
     * @param name O nome da pagina.
     ******************************************************************************************************************/
    protected void setPageName(final String name) {
        
        pageName = name;
        
    }//setPageName
    
    /*******************************************************************************************************************
    * O nome do Header, Section ou Topic que lhe foi atribuido no forum. Para a pagina Principal este
    * metodo retornara Principal.
    * 
    * @return O nome da pagina: principal, de Header, Section ou Topic.
    *******************************************************************************************************************/
    protected String getPageName() {
        
        return pageName;
        
    }//getPageName   
    
    /*******************************************************************************************************************
     * Atribui o nome do arquivo, sem a extensao html e nem o sufixo de indexacao.
     * 
     * @param filename O nome do arquivo.
     ******************************************************************************************************************/
    protected void setPageFilename(final String filename) {
        
        pageFilename = filename;
        
    }//setPageFilename
    
    /*******************************************************************************************************************
    * O nome com o qual o arquivo com a pagina principal, de Header, Section ou Topic sera gravado 
    * no disco.
    * 
    * @param pageIndex Uma Section pode se estender por varias paginas, caso contenha muitos Topics. Da
    * mesma forma uma pagina de Topic, se contiver muitos posts, pode se estender por mais de uma pagina.
    * 
    * @return O nome do arquivo com a pagina estatica no disco.
    *******************************************************************************************************************/
    protected String getPageFilename(final int pageIndex) {
        
        if (pageIndex == 0) return pageFilename + ".html";
        
        return pageFilename + "&start=" + (pageIndex * MaxList.MAX_TOPICS_TITLES_PER_PAGE.get()) + ".html";
        
    }//getPageFilename  
    
    /*******************************************************************************************************************
     * O endereço absoluto da pagina no servidor do forum.
     * 
     * @param url A URL do arquivo no servidor.
     ******************************************************************************************************************/
    protected void setPageUrl(final String url) {
        
        absoluteURL = 
            url.replace("./", FORUM_URL.toString()).replace("&amp;", "&").replaceAll("&sid=.*", "");
        
    }//setPageUrl
    
    /*******************************************************************************************************************
    * Retorna o endereço absoluto da pagina.
    * 
    * @param pageIndex Uma Section pode se estender por varias paginas, caso contenha muitos Topics. Da
    * mesma forma uma pagina de Topic, se contiver muitos posts, pode se estender por mais de uma pagina.
    * 
    * @return A url absoluta para obter o arquivo no servidor do forum.
    *******************************************************************************************************************/
    protected String getPageUrl(final int pageIndex) {
        
        if (pageIndex == 0) return absoluteURL;
        
        return absoluteURL + "&start=" + (pageIndex * MaxList.MAX_TOPICS_TITLES_PER_PAGE.get());
        
    }//getPageUrl
        
    /*******************************************************************************************************************
     * Objeto com os metodos que irao analisar a abertura e fechamento de cada tag no arquivo.
     * 
     * @param tagParser O objeto com os metodos que irao analisar a abertura e fechamento de cada tag no arquivo.
     ******************************************************************************************************************/
    protected void setPageParser(final toolbox.xml.TagParser tagParser) {
        
        parser = tagParser;
        
    }//setPageParser    
    
    /*******************************************************************************************************************
     * Baixa a pagina inicial do forum ou uma pagina de um Header, Section ou Topic e retorna o conteudo deste arquivo.
     * 
     * @param indexPage O indice da pagina. A primeira pagina tem indice 0.
     * 
     * @return O conteudo do arquivo.
     * 
     * @throws IOException Em caso de erro de IO.
     ******************************************************************************************************************/
    protected String downloadPage(final int indexPage) throws IOException {
        
        toolbox.log.Log.exec("phantom.pages", "Page", "downloadPage"); 
        
        String indexedUrl = getPageUrl(indexPage);
        String indexedFilename = RAW_PAGES_DIR.toString() + getPageFilename(indexPage);
        
        toolbox.log.Log.println("Baixando: " + indexedUrl);
        toolbox.log.Log.println("para arquivo: " + indexedFilename);
        
        toolbox.net.Util.downloadUrlToPathname(indexedUrl, indexedFilename);
     
        toolbox.textfile.TextFileHandler tfh = new toolbox.textfile.TextFileHandler(indexedFilename);
        
        tfh.read();  
        
        toolbox.log.Log.ret("phantom.pages", "Page", "downloadPage"); 
        
        return tfh.getContent();
        
    }//donwloadPage
    
    /*
    * Retorna <code>true</code> se a pagina referente a este objeto Page nao foi modificada desde o
    * ultimo backup.
    */
    private boolean pageWasntUpdatedSinceLastBackup() {
        
        return (getDateTimeOfLastPostOnThisPage().compareTo(dateTimeOfLastPostFromLastBackup) <= 0); 
        
    }//pageWasUpdatedSinceLastBackup
     
    /*******************************************************************************************************************
     * Baixa a pagina e faz o parsing desta.
     * 
     * 
     * @return A lista de Headers da pag. principal, ou de Sections de um Header, ou de Topics de um 
     * Section.
     * 
     * @throws XMLParseException Caso seja encontrado erro de sintaxe no arquivo durante o parsing, ou
     * se algum dado nao for compativel com o padrao esperado ou nao puder ser obtido com o parsing
     * do arquivo.
     * 
     * @throws IOException Em caso de erro de IO.
     ******************************************************************************************************************/
    protected LinkedList<Page> download() throws XMLParseException, IOException {
        
        if ( (!isFullBackup && pageWasntUpdatedSinceLastBackup()) ) return null;
        
        toolbox.log.Log.exec("phantom.pages", "Page", "download");
        
        int n = getNumberOfPages();
        
        for (int i = 0; i < n; i++) {
            
            String pageContent = downloadPage(i);
            
            if (parser != null) {//Page eh instancia de Main, Header ou Section
            
                toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(pageContent, parser);

                htmlParser.parse();
            }

        }//for
        
        toolbox.log.Log.ret("phantom.pages", "Page", "download");     
        
        if (parser == null) return null;
        
        return pagesList;  
        
    }//download  
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
             
        return String.format(
            "%s%n%s%n%s%n%s%n%s%n", 
            getStringDateTimeOfLastPostOnThisPage() + " GMT-3", 
            getPageName(), 
            getPageUrl(0), 
            getPageFilename(0),
            getNumberOfPages()
        );
        
    }//toString
    
    /**
     * 
     * @param Headerslist
     * 
     * @return 
     */
    protected static String getDateTimeOfLastPostFromThisBackup(final LinkedList<Page> Headerslist) {
        
        toolbox.collection.CollectionsProcessor<Page, Page> cp = 
            new toolbox.collection.CollectionsProcessor<>(Headerslist); 
        
        Page lastUpdateHeader = 
            cp.reduce(
                (result, item) -> {
                    if (result == null) return item;
                    if (((Header)item).compareTo(result) > 0) return item;
                    return result;
                }
            );
        
        return lastUpdateHeader.getStringDateTimeOfLastPostOnThisPage();        
        
    }//getForumLastPostTime
    
}//classe Page
