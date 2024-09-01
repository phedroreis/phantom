package phantom.pages;


/***********************************************************************************************************************
 *  * Classe que analisa, coleta, armazena e fornece dados de uma pagina de Topic.
 * 
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 22 de agosto de 2024
 **********************************************************************************************************************/
final class Topic extends Page {

    protected Topic(
        final String name, 
        final String url, 
        final String filename, 
        final String numberOfPosts,
        final String lastPostTime) {

        setPageName(name);
        setPageUrl(url);
        setPageFilename(filename);
        setPageParser(null);
        setNumberOfPages(( (Integer.parseInt(numberOfPosts) + 1) / MaxList.MAX_POSTS_PER_PAGE.get() ) + 1);
        setDateTimeOfLastPostOnThisPage(lastPostTime);

    }//construtor

}//classe Topic
