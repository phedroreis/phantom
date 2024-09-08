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
    
    //private final Set<String> requestedsUrls;
    
    private final Set<String> requestedsPathnames;
    
    private final LinkedBlockingQueue<Node> downloadQueue;
    
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

                //requestedsUrls.add(url);
                
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
  
        //requestedsUrls = new TreeSet<>();
        
        requestedsPathnames = new TreeSet<>();
        
        downloadQueue = new LinkedBlockingQueue<>();
        
        //ler a lista de falhas do backup anterior e inserir em downloadQueue
        readFails();        

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
        
    }//queue
    
    /**
     * 
     * @throws InterruptedException 
     */
    public void terminate() throws InterruptedException {

        downloadQueue.put(Node.TERMINATE);

    }//terminate  
    
    /*
    *
    */
    private void consume() {
        
        Node node = null;
        
        while (true) {
            
            try {

                node = downloadQueue.take();
                
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
                
                System.out.println("BAIXOU :" + pathname);
                
            } catch (IOException e) {
                
               fails.add(node);
               
               System.out.println("FALHOU :" + pathname);
               
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
    @SuppressWarnings("null")
    public void run() {
        
        consume();
        
        searchInCssFiles();
        
        consume(); 
        
        saveFails();//Gravar a lista de falhas        
 
    }//run      

}//classe Fetcher