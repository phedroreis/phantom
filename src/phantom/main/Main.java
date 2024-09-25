package phantom.main;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 */
public final class Main {
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;    
    
    static {
        
        toolbox.config.Property property = new toolbox.config.Property(CONFIG_PATHNAME);       
  
        try {//Le o arquivo de configuracao 
            
            property.load(); 
        }
        catch (IOException e) {}
  
        String lang = property.getProperty("lang", "pt");
        
        String country = property.getProperty("country", "BR");      
        
        toolbox.locale.Localization.setLocale(new Locale(lang, country));  
        
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle(
                    "phantom.properties.Main", 
                    toolbox.locale.Localization.getLocale()
                );
            
            msg$1 = rb.getString("msg$1");//Welcome to Phantom 1.1
            msg$2 = rb.getString("msg$2");//Last bakcup :
            msg$3 = rb.getString("msg$3");//[local time]            
           
        } 
        catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            //Opcoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Welcome to Phantom 1.1";
            msg$2 = "Last Backup :";
            msg$3 = "[local time]";

        } 
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);//Aborta backup
            
        }            
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
 
        
        try {
            
            /*
            Cria diretorios de trabalho do programa, se ainda nao criados, e o arquivo de 
            log para esta execucao do programa. O diretorio de configuracao, se for a 1a
            execucao do programa, foi criado e inicializado no bloco estatico desta classe.
            */
            phantom.main.Initializer.init();
            
            phantom.gui.MainFrame mainFrame = new phantom.gui.MainFrame();
            
            phantom.gui.MainFrame.setMainFrameReference(mainFrame);
            
            phantom.threads.ThreadsMonitor threadsMonitor = new phantom.threads.ThreadsMonitor();
            
            Thread thread = new Thread(threadsMonitor);
            
            thread.start();
            
            phantom.gui.Terminal terminal = phantom.gui.MainFrame.getTerminalReference();
            
            mainFrame.setVisible(true);
            
            terminal.appendln(msg$1);   
            terminal.appendln(
                msg$2 + " " +
                phantom.time.TimeTools.readDateTimeOfLastBackup() + " " + msg$3 +
                toolbox.string.StringTools.NEWLINE
            );
        }
        catch (Exception e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
            
        }
    }
    
}//classe Main
