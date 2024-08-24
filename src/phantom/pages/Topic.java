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
        
        setName(name);
        setAbsoluteURL(url);
        setFilename(filename);
        setParser(null);
        setNumberOfPages(( (Integer.parseInt(numberOfPosts) + 1) / getMaxPostsPerPage() ) + 1);
        setLastPostTime(lastPostTime);

    }//construtor


}//classe Topic
