package phantom.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import javax.management.modelmbean.XMLParseException;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Fix extends JFrame {
    
    private TreeMap<String, String> idMapToFilename;
    
    private TreeMap<String, String> oldUrlMapToNewUrl;
    
    private final JProgressBar fileProcessingProgressBar;
    
    private final JProgressBar replacingProgressBar;
   
    
    /**
     * 
     */
    public Fix() {
        
        super("Fix");
        
        phantom.resources.Resources rsc = new phantom.resources.Resources();
        setIconImage(rsc.getImage("favicon.png"));    
        
        setSize(350, 80);
        
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(2, 1));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
                  
        fileProcessingProgressBar = new JProgressBar();
        
        fileProcessingProgressBar.setStringPainted(true);
          
        replacingProgressBar = new JProgressBar();
        
        replacingProgressBar.setStringPainted(true);
        
        add(fileProcessingProgressBar, BorderLayout.NORTH);
        
        add(replacingProgressBar, BorderLayout.SOUTH);
        
        idMapToFilename = new TreeMap<>();
        
        oldUrlMapToNewUrl = new TreeMap<>();     
        
    }//construtor
    
    /*
    *
    */
    private void fix() throws IOException, XMLParseException {
        
        toolbox.file.SearchFolders searchFolders = 
            new toolbox.file.SearchFolders(new TopicsFilter());
        
        LinkedList<String> FileList = searchFolders.search(ROOT_DIR);
        
        fileProcessingProgressBar.setMaximum(FileList.size());
        fileProcessingProgressBar.setValue(0);
        int count = 0;
        
        setTitle("Coletando dados...");
        for (String pathname : FileList) {
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(pathname);
            
            textFileHandler.read();
            
            String contentFile = textFileHandler.getContent();
            
            toolbox.xml.HtmlParser topicParser = 
                new toolbox.xml.HtmlParser(
                    contentFile, 
                    new Parser((new File(pathname)).getName())
                );
            
            topicParser.parse();    
            
            fileProcessingProgressBar.setValue(++count);
            
        }//for
        
        searchFolders = new toolbox.file.SearchFolders(new PagesFilter());
        
        FileList = searchFolders.search(ROOT_DIR);
        
        fileProcessingProgressBar.setMaximum(FileList.size());
        fileProcessingProgressBar.setValue(0);
        count = 0;        

        setTitle("Editando arquivos...");    
        for (String pathname : FileList) {
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(pathname);
            
            textFileHandler.read();
            
            String contentFile = textFileHandler.getContent();
            
            toolbox.regex.Regex regex = 
                new toolbox.regex.Regex("href=\"\\./_post\\.html(#p\\d+)\"");
            
            regex.setTarget(contentFile);
            
            String url;
            
            while ( (url = regex.find()) != null) {
                
                String ref = regex.group(1);
                
                oldUrlMapToNewUrl.put(url, "href=\"./" + idMapToFilename.get(ref) + ref + "\"");
                
            }//while
            

            System.out.println(pathname);
            
            StringBuilder sb = new StringBuilder(contentFile);
            
            Set<String> keySet = oldUrlMapToNewUrl.keySet();
            
            replacingProgressBar.setMaximum(keySet.size());
            replacingProgressBar.setValue(0);
            int replaces = 0;  
      
            for (String href : keySet) {
                
                toolbox.string.StringTools.replace(sb, href, oldUrlMapToNewUrl.get(href));
                
                //contentFile = contentFile.replace(href, oldUrlMapToNewUrl.get(href));
                
                //System.out.println("Troca: " + href + " -> " + oldUrlMapToNewUrl.get(href));
                
                replacingProgressBar.setValue(++replaces);
                
            }//for
            
            textFileHandler.setContent(sb.toString());
            
            textFileHandler.write();     
          
            fileProcessingProgressBar.setValue(++count);
            
        }//for   
        
        System.out.println("*** TERMINOU ***");   
        
    }//fix
    
    /**
     * 
     * @param args
     * @throws IOException
     * @throws XMLParseException 
     */
    public static void main(String[] args) throws IOException, XMLParseException {
        
        Fix fix = new Fix();
        
        fix.setVisible(true);
        
        fix.fix();

    }//main
    
/*======================================================================================================================
           Classe privada para o parsing dos arquivos HTML das topicos do forum. 
======================================================================================================================*/
private final class Parser extends toolbox.xml.TagParser {
    
    private final String filename;
    
    /*
    *
    */
    public Parser(final String filename) {
        
        this.filename = filename;
        
    }//construtor
    
   /*==================================================================================================================
    *  
    ==================================================================================================================*/    
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void openTag(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();        

        HashMap<String, String> map = t.getAttrMap();
       
        try {
            
            if (tagName.equals("div")) {
                
                String classValue = map.get("class");
                
                if ( (classValue != null) && (classValue.startsWith("post has-profile bg")) ) {
                    
                    String idValue = map.get("id");
                    
                    if (idValue == null || !idValue.matches("p\\d+"))
                        throw new XMLParseException("Parsing error: " + idValue);
                    
                    idValue = '#' + idValue;
                    
                    idMapToFilename.put(idValue, filename);
                }
            }
            
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }//try-catch
        
    }//openTag

}//classe privada Parser    
    
/*======================================================================================================================
 *  Seleciona apenas arquivos de paginas de topicos.
======================================================================================================================*/
private static class TopicsFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) {
         
        String fileName = file.getFileName().toString();
        
        /*
        Retorna true apenas para arq. de pag. principal, header, section ou topic
        */
        return 
            (fileName.matches("t=\\d+(&start=\\d+){0,1}\\.html") || fileName.equals(MAIN_PAGE_FILE));
    }
     
}//classe privada PagesFilter   

/*======================================================================================================================
 *  Seleciona arquivos de paginas do forum. 
======================================================================================================================*/
private static class PagesFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) {
         
        String fileName = file.getFileName().toString();
        
        /*
        Retorna true apenas para arq. de pag. principal, header, section ou topic
        */
        return 
            (fileName.matches("(f|t)=\\d+(&start=\\d+){0,1}\\.html") || fileName.equals(MAIN_PAGE_FILE));
    }
     
}//classe privada PagesFilter  

}//classe Fix
