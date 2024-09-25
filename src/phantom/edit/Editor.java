package phantom.edit;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Editor {

    private static HashMap<String, String> url2staticUrl;
    
    private static phantom.fetch.Fetcher fetcher;
    
    private final phantom.gui.Terminal terminal;
    private final phantom.gui.CustomProgressBar editProgressBar;
    private final JRadioButton privateAreaBackup;

    private static String msg$1;
    private static String msg$2;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Editor", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");
            msg$2 = rb.getString("msg$2");
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Starting forum pages edition";
            msg$2 = "Forum pages edition is concluded!";
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }
        
        
    }//bloco static 
    
    private final String mainPageFile;
    
    /**
     * 
     */
    public Editor() {
        
        terminal = phantom.gui.MainFrame.getTerminalReference();
        privateAreaBackup = phantom.gui.MainFrame.getPrivateAreaRadioButtonReference();
        editProgressBar = phantom.gui.MainFrame.getEditProgressBarReference();
        
        if (privateAreaBackup.isSelected())
            mainPageFile = FORUM_NAME + ".htm";
        else
            mainPageFile = MAIN_PAGE_FILE;
        
    }//construtor
    
    /**
     * 
     * @throws Exception 
     */
    public void edit() throws Exception {
        
        toolbox.log.Log.exec("phantom.edit", "Editor", "edit");
        
        toolbox.file.SearchFolders searchFolders = 
            new toolbox.file.SearchFolders(new Editor.PagesFilter());
        
        LinkedList<String> listPages = searchFolders.search(RAW_PAGES_DIR);
        
        fetcher = new phantom.fetch.Fetcher();
        
        Thread downloadThread = new Thread(fetcher);
        
        downloadThread.start();
        
        toolbox.log.Log.println("Iniciou thread de download de arquivos estaticos");
        
        String MainPageUrl = MAIN_PAGE_URL + "\"";

        terminal.appendln(msg$1);
        
        phantom.time.ElapsedTime elapsedTime = new phantom.time.ElapsedTime();
        elapsedTime.start();
        
        editProgressBar.setMaximum(listPages.size());
        
        toolbox.log.Log.println("Iniciou edicao de arquivos HTML de pags. do forum");        
          
        for (String pathname : listPages) {
            
            toolbox.log.Log.println("Editando: " + pathname);
            
            toolbox.textfile.TextFileHandler textFileHandler = 
                new toolbox.textfile.TextFileHandler(pathname, "utf8");
            
            textFileHandler.read();
            
            String content = textFileHandler.getContent();
            
            toolbox.xml.HtmlParser htmlParser = new toolbox.xml.HtmlParser(content, new Editor.Parser());
            
            url2staticUrl = new HashMap<>();
    
            htmlParser.parse();
            
            StringBuilder sbContent = new StringBuilder(content); 
            
            content = null; textFileHandler.setContent(content);//Para liberar memoria
                        
            for (String key : url2staticUrl.keySet()) {
                
                String value = url2staticUrl.get(key);
                
                toolbox.string.StringTools.replace(sbContent, key, value);
                
            }//for
            
            /*
            URLs absolutas apontando para pag. principal passam a apontar arq. estatico
            da pag. principal.
            */
            toolbox.string.StringTools.replace(sbContent, MainPageUrl, "./" + mainPageFile + '\"');
            
            textFileHandler.setContent(sbContent.toString());
            
            File rawFile = new File(pathname);
            
            textFileHandler.write(ROOT_DIR + rawFile.getName());
            
            rawFile.delete();
            
            editProgressBar.incrementCounter(); 
            
        }//for

        phantom.threads.ThreadsMonitor.sendTerminateSignal(msg$2 + elapsedTime.toString());
         
        toolbox.log.Log.println("Envia sinal para objeto fetcher: lista de downloads finalizada!");
        
        fetcher.terminate();
                       
        toolbox.log.Log.ret("phantom.edit", "Editor", "edit");
        
    }//edit
    
/*======================================================================================================================
           Classe privada para o parsing dos arquivos HTML das paginas do forum. 
======================================================================================================================*/
private static final class Parser extends toolbox.xml.TagParser {
    
    private final Tag aorLink = new AorLinkorForm();
    private final Tag img = new Img();
    private final Tag script = new Script();

    
   /*==================================================================================================================
    * Busca pelos atributos href e src das tags a, img, link, form e script que apontem para arquivos no
    * servidor do forum. 
    ==================================================================================================================*/    
    @Override
    public void openTag(toolbox.xml.Tag t) {
        
        String tagName = t.getTagName();        

        HashMap<String, String> map = t.getAttrMap();
       
        try {
            
            switch (tagName) {

                case "a":
                case "link":
                    aorLink.parseUrl(map.get("href"), fetcher, url2staticUrl); 
                    break;
                    
                case "img":
                    img.parseUrl(map.get("src"), fetcher, url2staticUrl);
                    break;                    

                case "script":
                    script.parseUrl(map.get("src"), fetcher, url2staticUrl); 
                    break;

                case "form":
                    aorLink.parseUrl(map.get("action"), fetcher, url2staticUrl);

            }//switch 
            
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }//try-catch
        
    }//openTag

}//classe privada Parser

/*======================================================================================================================
 *   Classe privada: seleciona apenas arquivos de paginas HTML do forum que foram geradas por scripts PHP
 *   e baixadas previamente.
 *
 *   Este filtro produzira a lista de arquivos que sera processada no metodo edit() desta classe.
======================================================================================================================*/
private class PagesFilter implements DirectoryStream.Filter<Path> {
     
    @Override
    public boolean accept(final Path file) {
         
        String fileName = file.getFileName().toString();
        
        /*
        Retorna true apenas para arq. de pag. principal, header, section ou topic
        */
        return 
            (fileName.matches("(f|t)=\\d+(&start=\\d+){0,1}\\.html") || fileName.equals(mainPageFile));
    }
     
}//classe privada PagesFilter      

}//classe Editor
