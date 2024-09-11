package phantom.run;

import java.io.IOException;
import javax.management.modelmbean.XMLParseException;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class Run implements Runnable {

    public Run() {

    }//construtor

    @Override
    public void run() {
        
        try {
            /*
            Cria objeto para baixar as paginas do forum, inicializado com data-hora da
            ultima postagem no ultimo backup. Se este for o 1o backup, a data-hora eh
            lida como "ano 0"
            */
            phantom.pages.Downloader downloader = 
                new phantom.pages.Downloader(
                    phantom.cron.CronTools.readDateTimeOfLastPostFromLastBackup()
                );

            /*
            Baixa, incrementalmente, paginas do forum (Main, Headers, Sections, Topics)
            */
            downloader.downloadAllPages();
            
            phantom.edit.HrefSrcEditor hrefSrcEditor = new phantom.edit.HrefSrcEditor();
            
            hrefSrcEditor.edit();
      
            /*
            Salva a data-hora da ultima postagem do forum neste backup.
            */
            phantom.cron.CronTools.saveDateTimeOfLastPostFromThisBackup(
                downloader.getDateTimeOfLastPostFromThisBackup()
            );
            
            
            
        } catch (XMLParseException | IOException | InterruptedException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }
    }

}//classe Run
