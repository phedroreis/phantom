package phantom.pages;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public final class Section extends Page {
    
    private String lastPostTime;
    
    /**
     * 
     * @param filename 
     */
    public Section(final String filename) {
        
        setFilename(filename);

    }//construtor
    
    /**
     * 
     * @param datetime 
     */
    public void setLastPostTime(final String datetime) {
        
        lastPostTime = datetime;
        
    }//setLastPostTime
    
    /**
     * 
     * @return 
     */
    public String getLastPostTime() {
        
        return lastPostTime;
        
    }//getLastPostTime
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        
        return String.format("%s%n%s", getLastPostTime(), super.toString());       
        
    }//toString
    
}//classe Section
