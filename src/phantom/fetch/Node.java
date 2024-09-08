package phantom.fetch;

import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Node {
    
    private final String absoluteUrl;
    
    private final String pathname;
    
    protected static final Node TERMINATE = new Node(null, null);

    /**
     * 
     * @param relativeUrl
     * @param relativePathname 
     */
    public Node(final String relativeUrl, final String relativePathname) {
        
        this.absoluteUrl = ROOT_URL + relativeUrl;
        this.pathname = ROOT_DIR + relativePathname;

    }//construtor

    /**
     * 
     * @return 
     */
    public String getAbsoluteUrl() {
        
        return absoluteUrl;
        
    }//getAbsoluteUrl

    /**
     * 
     * @return 
     */
    public String getPathname() {
        
        return pathname;
        
    }//getAbsolutePathname
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        
        return (getAbsoluteUrl() + " " + getPathname());
        
    }//toString

}//classe Node
