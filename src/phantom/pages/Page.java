package phantom.pages;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import javax.management.modelmbean.XMLParseException;

/***********************************************************************************************************************
* Superclasse para as classes que analisam, coletam, armazenam e fornecem
* dados da PAGINA PRINCIPAL, das paginas de HEADERS, SECOES E TOPICOS do forum.
*
* @version 1.0
* @since 1.0 - 11 de agosto de 2024
* @author Pedro Reis
***********************************************************************************************************************/
public abstract class Page {
    
    /*******************************************************************************************************************
     * O nome do forum e tambem o nome da pasta com as paginas estaticas e o nome do arquivo com a 
     * pagina inicial.
     ******************************************************************************************************************/
    public static final String FORUM_NAME = "clubeceticismo";
    
    /*******************************************************************************************************************
    *   URL da pagina principal do forum 
    *******************************************************************************************************************/
    public static final String FORUM_URL = "http://" + FORUM_NAME + ".com.br/";
    
    /*==================================================================================================================
    * Num. max. de nomes de secoes que podem ser listados em paginas de HEADERS. 
    * Este valor depende de configuracao especifica que pode ser alterada a qualquer momento pelos 
    * adms. do forum.
    ==================================================================================================================*/
    private static final int MAX_SECTIONS_NAMES_PER_PAGE = 50;     
    
    /*==================================================================================================================
    * Num. max. de titulos de topicos que podem ser listados em paginas de SECAO.
    * Este valor depende de configuracao especifica que pode ser alterada a qualquer momento pelos 
    * adms. do forum.
    ==================================================================================================================*/
    private static final int MAX_TOPICS_TITLES_PER_PAGE = 50;
    
    /*==================================================================================================================
    * Num. max. de posts que podem ser publicados em cada pagina de topico.
    * Este valor depende de configuracao especifica que pode ser alterada a qualquer momento pelos 
    * adms. do forum.
    ==================================================================================================================*/
    private static final int MAX_POSTS_PER_PAGE = 50;    
    
    /*==================================================================================================================
    * O nome do header, secao ou topico. Para um objeto da subclasse Main este
    * campo armazenarah "Principal"
    ==================================================================================================================*/
    private String name;
    
    /*==================================================================================================================
    * O nome do arquivo onde a pagina do header, secao ou topico sera gravada. Armazenado sem a extensao
    * html e sem o sufixo de indexacao, que sao inseridos posteriormente pelo metodo getFilename().
    
    * Nesta versao, o nome do arquivo para gravar a pagina principal eh clubeceticismo.html
    ==================================================================================================================*/
    private String filename;
    
    /*==================================================================================================================
    * A URL para baixar a 1a pagina de determinado header, secao ou topico. Sem sufixo de indexacao que
    * e introduzido pelo metodo getAbsoluteURL().
    *
    * Na da pagina principal este campo armazenarah a URL de acesso ao proprio forum.
    ==================================================================================================================*/
    private String absoluteURL; 
    
    /*
    *
    */
    private int numberOfPages;
    
    /*==================================================================================================================
    * Customiza a parser do arquivo html para recuperar os dados necessarios para um objeto 
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
    private Calendar lastPostTime;
    
    /*
    *
    */
    private String lastPostTimeStr;    
    
    /*==================================================================================================================
    *        BLOCO DE INICIALIZACAO cria o objeto pagesList.
    ==================================================================================================================*/
    { 
        pagesList = new LinkedList<>();
        lastPostTime = null;
        lastPostTimeStr = null;
    }
    
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
    protected void setLastPostTime(final String datetime) {        
        
        lastPostTimeStr = datetime + " GMT-3";
        
        lastPostTime = Calendar.getInstance();
         
        lastPostTime.set(Calendar.YEAR, Integer.parseInt(datetime.substring(0, 4)));
        
        lastPostTime.set(Calendar.MONTH, Integer.parseInt(datetime.substring(5, 7)));
        
        lastPostTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datetime.substring(8, 10)));
        
        lastPostTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(datetime.substring(11, 13)));
        
        lastPostTime.set(Calendar.MINUTE, Integer.parseInt(datetime.substring(14, 16)));
        
        lastPostTime.set(Calendar.SECOND, Integer.parseInt(datetime.substring(17, 19)));   
        
        lastPostTime.set(Calendar.ZONE_OFFSET, -10800000);
        
        lastPostTime.add(Calendar.MILLISECOND, -10800000);
        
    }//setLastPostTime
    
    /**
     * 
     * @return 
     */
    protected Calendar getLastPostTime() {
        
        return lastPostTime;
        
    }//getLastPostTime  
    
    /**
     * 
     * @return 
     */
    protected String getLastPostTimeStr() {
        
        return lastPostTimeStr;
        
    }//getLastPostTimeStr
    
    /*******************************************************************************************************************
     * Este metodo permite que um objeto Header, Section ou Topic seja adicionado a pagesList.
     * 
     * @param page Objeto com dados da pagina de Header, Section ou Topic.
     ******************************************************************************************************************/
    protected void addPage(final Page page) {
        
        pagesList.add(page);
        
    }//addPage
    
   
    /*******************************************************************************************************************
     * Retorna num. max. de links para topicos que podem estar listados em uma pagina de Section.
     * 
     * @return O num. max. de links para topicos que podem estar listados em uma pagina de Section.
     ******************************************************************************************************************/
    protected static int getMaxTopicsTitlesPerPage() {
        
        return MAX_TOPICS_TITLES_PER_PAGE;
        
    }//getMaxTopicsTitlesPerPage
    
    /*******************************************************************************************************************
     * Retorna num. max. de links para Sections que podem estar listados em uma pagina de Header.
     * 
     * @return num. max. de links para Sections que podem estar listados em uma pagina de Header.
     ******************************************************************************************************************/
    protected static int getMaxSectionsNamesPerPage() {
        
        return MAX_SECTIONS_NAMES_PER_PAGE;
        
    }//getMaxSectionsNamesPerPage   
    
    /*******************************************************************************************************************
     * Retorna o num. max. de posts que podem ser publicados em uma pagina de Topic.
     * 
     * @return num. max. de posts que podem ser publicados em uma pagina de Topic.
     ******************************************************************************************************************/
    protected static int getMaxPostsPerPage() {
        
        return MAX_POSTS_PER_PAGE;
        
    }//getMaxPostsPerPage
    
   
    /*******************************************************************************************************************
    * O nome do Header, Section ou Topic que lhe foi atribuido no forum. Para a pagina Principal este
    * metodo retornara Principal.
    * 
    * @return O nome da pagina: principal, de Header, Section ou Topic.
    *******************************************************************************************************************/
    protected String getName() {
        
        return name;
        
    }//getName
    
    /*******************************************************************************************************************
     * Atribui o nome com que o forum designa a pagina de Header, Section ou Topic.
     * Para a pagina principal sera atribuido pelo construtor, o nome Principal.
     * 
     * @param n O nome da pagina.
     ******************************************************************************************************************/
    protected void setName(final String n) {
        
        name = n;
        
    }//setName
    
    /*******************************************************************************************************************
    * O nome com o qual o arquivo com a pagina principal, de Header, Section ou Topic sera gravado 
    * no disco.
    * 
    * @param pageIndex Uma Section pode se estender por varias paginas, caso contenha muitos Topics. Da
    * mesma forma uma pagina de Topic, se contiver muitos posts, pode se estender por mais de uma pagina.
    * 
    * @return O nome do arquivo com a pagina estatica no disco.
    *******************************************************************************************************************/
    protected String getFilename(final int pageIndex) {
        
        if (pageIndex == 0) return filename + ".html";
        
        return filename + "&start=" + (pageIndex * MAX_TOPICS_TITLES_PER_PAGE) + ".html";
        
    }//getFilename
    
    /*******************************************************************************************************************
     * Atribui o nome do arquivo, sem a extensao html e nem o sufixo de indexacao.
     * 
     * @param f O nome do arquivo.
     ******************************************************************************************************************/
    protected void setFilename(final String f) {
        
        filename = f;
        
    }//setFilename
    
    /*******************************************************************************************************************
    * Retorna o endereço absoluto da pagina.
    * 
    * @param pageIndex Uma Section pode se estender por varias paginas, caso contenha muitos Topics. Da
    * mesma forma uma pagina de Topic, se contiver muitos posts, pode se estender por mais de uma pagina.
    * 
    * @return A url absoluta para obter o arquivo no servidor do forum.
    *******************************************************************************************************************/
    protected String getAbsoluteURL(final int pageIndex) {
        
        if (pageIndex == 0) return absoluteURL;
        
        return absoluteURL + "&start=" + (pageIndex * MAX_TOPICS_TITLES_PER_PAGE);
        
    }//getAbsoluteURL

    /*******************************************************************************************************************
     * O endereço absoluto da pagina no servidor do forum.
     * 
     * @param url A URL do arquivo no servidor.
     ******************************************************************************************************************/
    protected void setAbsoluteURL(final String url) {
        
        absoluteURL = url.replace("./", FORUM_URL).replace("&amp;", "&").replaceAll("&sid=.*", "");
        
    }//setAbsoluteURL 
    
    /*******************************************************************************************************************
     * Baixa a pagina inicial do forum ou uma pagina inicial de um Header, Section ou Topic e retorna o
     * conteudo deste arquivo.
     * 
     * @param indexPage
     * 
     * @return O conteudo do arquivo.
     * 
     * @throws IOException Em caso de erro de IO.
     ******************************************************************************************************************/
    protected String downloadPage(final int indexPage) throws IOException {
        
        toolbox.net.Util.downloadUrlToPathname(getAbsoluteURL(indexPage), getFilename(indexPage));
     
        toolbox.textfile.TextFileHandler tfh = 
            new toolbox.textfile.TextFileHandler(getFilename(indexPage));
        
        tfh.read();  
        
        return tfh.getContent();
        
    }//donwloadPage
    
    /*******************************************************************************************************************
     * Seta o objeto com os metodos que irao analisar a abertura e fechamento de cada tag no arquivo.
     * 
     * @param parser O objeto com os metodos que irao analisar a abertura e fechamento de cada tag no arquivo.
     ******************************************************************************************************************/
    protected void setParser(final toolbox.xml.TagParser parser) {
        
        this.parser = parser;
        
    }//setParser
    
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
        
        int n = getNumberOfPages();
        
        for (int i = 0; i < n; i++) {
            
            if (parser == null)//Page eh instancia de Topic
                
                downloadPage(i);
               
            else {//Page eh instancia de Main, Header ou Section
            
                toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(downloadPage(i), parser);

                htmlParser.parse();
            }

        }
        
        if (parser == null) return null;
        
        return pagesList;  
        
    }//download  
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        
        String lastPost = lastPostTimeStr == null ? "" : getLastPostTimeStr();
        
        return String.format("%s%n%s%n%s%n%s%n", lastPost, getName(), getAbsoluteURL(0), getFilename(0));
        
    }//toString
    
}//classe Page
