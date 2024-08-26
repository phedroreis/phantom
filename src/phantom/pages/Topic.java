package phantom.pages;


/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public final class Topic extends Page {

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
