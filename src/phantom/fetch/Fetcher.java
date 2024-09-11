package phantom.fetch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 1 de setembro de 2024
 */
public final class Fetcher implements Runnable {
    
    private static final Pattern QUERY = Pattern.compile("\\?.*$");//Localiza queries em arqs.
    
    private Matcher matcher;

    private final List<Node> fails;
            
    private final Set<String> requestedsPathnames;
    
    private final LinkedBlockingQueue<Node> downloadQueue;
    
    private int total;
    
    private int count;
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    private static String msg$5;
    private static String msg$6;  
    private static String msg$7;
    
    /*
    * Internacionaliza as Strings "hardcoded" na classe
    */
    static {
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Fetcher", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");
            msg$2 = rb.getString("msg$2");
            msg$3 = rb.getString("msg$3");
            msg$4 = rb.getString("msg$4");
            msg$5 = rb.getString("msg$5");
            msg$6 = rb.getString("msg$6");
            msg$7 = rb.getString("msg$7");
            
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "GOT IT :";
            msg$2 = "FAIL :";
            msg$3 = "Downloading static files...";
            msg$4 = "Searching in CSS files...";
            msg$5 = "Downloading files requested by CSS files...";
            msg$6 = "Saving fails file...";
            msg$7 = "Static files download concluded!";
        }
        
    }//bloco static    
    
    /*
    *
    */
    private void readFails() throws InterruptedException {        
             
        try ( Scanner scanner = new Scanner(new File(FAILS_PATHNAME), "utf8") ) { 
            
            while (scanner.hasNext()) {
                
                String url = scanner.next();
                String pathname = scanner.next();  

                Node node = 
                    new Node(url.replace(ROOT_URL, ""), pathname.replace(ROOT_DIR, ""));
                
                requestedsPathnames.add(pathname);

                downloadQueue.put(node); 
 
                scanner.nextLine(); 
                
            }//while

        }
        catch (Exception e) {}
        
    }//readFails
    
    /*
    *
    */
    private void saveFails() {
        
        try (PrintWriter printWriter = new PrintWriter(FAILS_PATHNAME, "utf8")) {
            
            for (Node node : fails) printWriter.println(node);
 
        } 
        catch (FileNotFoundException | UnsupportedEncodingException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);            
            
        }
        
    }//saveFails

    /**
     *
     * @throws InterruptedException 
     */
    public Fetcher() throws InterruptedException {
        
        fails = new LinkedList<>();
        
        requestedsPathnames = new TreeSet<>();
        
        downloadQueue = new LinkedBlockingQueue<>();
        
        //ler a lista de falhas do backup anterior e inserir em downloadQueue
        readFails(); 
        
        total = 0; count = 0;

    }//construtor
    
    /**
     * Insere um node na fila de download caso ainda nao exista outro com a mesma URL.

     * @param node
     * 
     * @throws InterruptedException Caso ocorra excecao quando a thread estiver dormindo.
     */
    public void queue(final Node node) throws InterruptedException {

        if (new File(node.getPathname()).exists()) return; 

        //if (requestedsUrls.contains(node.getAbsoluteUrl())) return;
        
        if (requestedsPathnames.contains(node.getPathname())) return;

        //requestedsUrls.add(node.getAbsoluteUrl());
        
        requestedsPathnames.add(node.getPathname());
        
        downloadQueue.put(node);

        phantom.gui.GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR.concurrentSetMaximum(++total);
        phantom.gui.GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR.concurrentSetValue(count);

    }//queue
    
    /**
     * 
     * @throws InterruptedException 
     */
    public void terminate() throws InterruptedException {

        downloadQueue.put(Node.TERMINATE);

        phantom.gui.GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR.concurrentSetMaximum(++total);
        phantom.gui.GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR.concurrentSetValue(count);

    }//terminate  
    
    /*
    *
    */
    private void consume() {
        
        Node node = null;
       
        while (true) {
            
            try {

                node = downloadQueue.take();                
  
                phantom.gui.GlobalComponents.STATIC_DOWNLOAD_PROGRESS_BAR.concurrentSetValue(++count);
                  
                if (node == Node.TERMINATE) break; 

            } catch (InterruptedException e) { 
                
                //Ver o que o livro faz quando ocorre InterruptedException
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
            }
            
            String pathname = node.getPathname();
            
            File file = new File(pathname);
            
            try {
                
                toolbox.file.FileTools.createDirsIfNotExists(file.getParentFile());  
                
                toolbox.net.NetTools.downloadUrlToPathname(node.getAbsoluteUrl(), pathname);                
                
                System.out.println(msg$1 + pathname);
                
            } catch (IOException e) {
                
               fails.add(node);
               
               System.out.println(msg$2 + pathname);
               
               file.delete();
          
               toolbox.file.FileTools.deleteDirsIfEmpty(file.getParentFile());
            }           
          
        }//while   
        
    }//consume
    
    /*
    *
    */
    private void searchInCssFiles() {
        
       LinkedList<String> cssFileList = null;
        
        toolbox.file.SearchFolders searchFolders = 
            new toolbox.file.SearchFolders("^.+?\\.css", true);
        
        try {
            
            cssFileList = searchFolders.search(ROOT_DIR);
            
        } 
        catch (IOException | SecurityException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }
        
        toolbox.regex.Regex urlRegex = new toolbox.regex.Regex("url\\((\"|')(.+?)(\"|')\\)");  

        int countCssFiles = 0;
        phantom.gui.GlobalComponents.CSS_PROGRESS_BAR.concurrentSetMaximum(cssFileList.size());
        phantom.gui.GlobalComponents.CSS_PROGRESS_BAR.concurrentSetValue(0);        
        
        for (String pathname: cssFileList) {
            
            toolbox.textfile.TextFileHandler textFileHandler;
            
            try {
                
                textFileHandler = new toolbox.textfile.TextFileHandler(pathname, "utf8");
                
                textFileHandler.read();
 
                urlRegex.setTarget(textFileHandler.getContent());
          
                while ( urlRegex.find() != null ) {
                    
                    String download = urlRegex.group(2);
                     
                    //Localiza a query do arquivo, se existir
                    matcher = QUERY.matcher(download);
                    String query = matcher.find() ? matcher.group() : "";  
                    
                    download = download.replace(query, "");
                 
                    String relativePathToDownload = 
                        new File(pathname).getParent().replace("\\", "/") + '/' + download;
                    
                    relativePathToDownload = relativePathToDownload.replace(ROOT_DIR, "./");
                    
                    Node node = new Node(relativePathToDownload, relativePathToDownload);
                    
                    try {
                        
                        queue(node);
                        
                    } catch (InterruptedException e) {
                        
                        phantom.exception.ExceptionTools.crashMessage(null, e);
                    }
                    
                }//while

            }
            catch (IOException | IllegalCharsetNameException | UnsupportedCharsetException e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);  
                
            }//try-catch
            
            phantom.gui.GlobalComponents.CSS_PROGRESS_BAR.concurrentSetValue(++countCssFiles);             
  
        }//for
        
        try {
            
            terminate();
            
        } catch (InterruptedException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }        
        
    }//searchInCssFiles

    /**
     * 
     */
    @Override
    public void run() {
        
        phantom.gui.GlobalComponents.TERMINAL.concurrentAppendln(msg$3); 
        
        consume();
        
        phantom.gui.GlobalComponents.TERMINAL.concurrentAppendln(msg$4); 
        
        searchInCssFiles();
        
        phantom.gui.GlobalComponents.TERMINAL.concurrentAppendln(msg$5);
        
        consume(); 
        
        phantom.gui.GlobalComponents.TERMINAL.concurrentAppendln(msg$6);
        
        saveFails();//Gravar a lista de falhas  
        
        phantom.gui.GlobalComponents.TERMINAL.sendTerminateSignal(msg$7);    
 
    }//run      

}//classe Fetcher