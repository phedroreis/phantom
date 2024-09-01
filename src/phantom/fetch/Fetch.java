package phantom.fetch;

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
    
    private static final String FORUM_URL = ROOT_URL.get();
    
    private boolean hasDownloads;
    
    private final Set<String> requestedDownloads;
    
    private final LinkedBlockingQueue<String> downloadQueue;

    /**
     * 
     */
    public Fetch() {
        
        requestedDownloads = new TreeSet<>();
        
        downloadQueue = new LinkedBlockingQueue<>();
        
        hasDownloads = true;

    }//construtor
    
    /**
     * 
     * @param url
     * @throws InterruptedException 
     */
    public void queue(final String url) throws InterruptedException {
        
        String absoluteUrl = FORUM_URL + ((url.charAt(0) == '/') ? url : url.substring(1));
        
        if (requestedDownloads.contains(absoluteUrl)) return;
        
        requestedDownloads.add(absoluteUrl);
        
        downloadQueue.put(absoluteUrl);
        
    }//queue
    
    /**
     * 
     * @throws InterruptedException 
     */
    public void terminate() throws InterruptedException {
 
        downloadQueue.put("*GAMEOVER*");

    }//terminate

    /**
     * 
     */
    @Override
    public void run() {
        
        String url = "";
        
        while (hasDownloads) {
            
            try {

                url = downloadQueue.take();

            } catch (InterruptedException e) { System.exit(1); }
            
            System.out.println("BAIXOU: " + url);
            
            if (url.equals("*GAMEOVER*")) hasDownloads = false;            
            
        }//while
        
    }//run

}//classe Fetch
