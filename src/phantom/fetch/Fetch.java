package phantom.fetch;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import static phantom.global.GlobalStrings.*;

/**
 *
 * @author Pedro Reis
 * @since 1.0
 * @version 1.0 - 1 de setembro de 2024
 */
public final class Fetch implements Runnable {
    
    private static final String FINISH_FLAG = "*gameover*";
    
    private static final String FORUM_URL = ROOT_URL.get();
    
    private static final String FORUM_DIR = ROOT_DIR.get();
    
    private final Set<String> fails;
    
    private final Set<String> requestedsDownloads;
    
    private final LinkedBlockingQueue<String> downloadQueue;
    
    /*
    *
    */
    private void readFails() {
        
    }//readFails
    
    /*
    *
    */
    private void saveFails() {
        
    }//saveFails

    /**
     * 
     */
    public Fetch() {
        
        fails = new TreeSet<>();
        
        //ler a lista de falhas do backup anterior e inserir em downloadQueue
        readFails();
        
        requestedsDownloads = new TreeSet<>();
        
        downloadQueue = new LinkedBlockingQueue<>();

    }//construtor
    
    /**
     * 
     * @param url
     * @throws InterruptedException 
     */
    public void queue(final String url) throws InterruptedException {
        
        String absoluteUrl = FORUM_URL + url;
        
        if (requestedsDownloads.contains(absoluteUrl)) return;
        
        requestedsDownloads.add(absoluteUrl);
        
        downloadQueue.put(absoluteUrl);
        
    }//queue
    
    /**
     * 
     * @throws InterruptedException 
     */
    public void terminate() throws InterruptedException {
        
        //Gravar a lista de falhas
        saveFails();
 
        downloadQueue.put(FINISH_FLAG);

    }//terminate
    

    /*
    *
    */
    private boolean download(final String url) throws Exception {
        
        String pathname = url.replace(FORUM_URL, FORUM_DIR);
        
        File file = new File(pathname); 
   
        if (file.exists()) return false;  
          
        toolbox.file.FileTools.createDirsIfNotExists(file.getParentFile());
        
        toolbox.net.Util.downloadUrlToPathname(url, pathname);
        
        return true;
        
    }//download

    /**
     * 
     */
    @Override
    public void run() {
        
        String url = "";
        
        while (true) {
            
            try {

                url = downloadQueue.take();
                
                if (url.equals(FINISH_FLAG)) break; 

            } catch (InterruptedException e) { 
                
                //Ver o que o livro faz quando ocorre InterruptedException
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
            }
            
            try {
                
                if (download(url)) System.out.println("BAIXOU :" + url);
                
            } catch (Exception e) {
                
               fails.add(url);
               
               System.out.println("FALHOU :" + url);
               
               File file = new File(url.replace(FORUM_URL, FORUM_DIR));
               
               file.delete();
          
               toolbox.file.FileTools.deleteDirsIfEmpty(file.getParentFile());
            }           
          
        }//while
        
    }//run
 

}//classe Fetch
