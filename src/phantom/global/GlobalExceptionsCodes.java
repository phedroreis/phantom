package phantom.global;

/***********************************************************************************************************************
 *
 * @author 
 * @since
 * @version
 **********************************************************************************************************************/
public enum GlobalExceptionsCodes {
    
    INTERRUPTED("Interrupted"),
    
    ILLEGAL_ARGUMENT("IllegalArgumet"),
    
    IO("IO"),
    
    FILE_NOT_FOUND("FileNotFound"),
        
    NULL_POINTER("NullPointer"),
    
    MAL_FORMED_URL("MalformedURL"),
    
    URI_SYNTAX("URISyntax"),
    
    XML_PARSE("XMLParse"); 
    

    private String exceptionName;
    

    GlobalExceptionsCodes(final String n) {

        exceptionName = n;

    }//construtor
    
    
    /*******************************************************************************************************************
     * 
     * @param e
     * @return 
     ******************************************************************************************************************/
    public static int getExceptionCode(final Exception e) {
        
        String fullName = e.toString();
        
        String name = 
            fullName.substring(fullName.lastIndexOf(".") + 1, fullName.indexOf("Exception"));        
        
        for (GlobalExceptionsCodes i : GlobalExceptionsCodes.values()) 
            
            if (i.exceptionName.equals(name)) return (i.ordinal() + 1); 
        
        return 255;
        
    }//get
    
    

}//enum GlobalExceptionsCodes
