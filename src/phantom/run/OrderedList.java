package phantom.run;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.TreeSet;
import static phantom.global.GlobalConstants.*;

public final class OrderedList implements Runnable {
    
    private static final String HEAD =
"<!DOCTYPE html>\n<html lang=\"pt-br\">\n<head>\n<meta charset=\"utf-8\" />\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
"<link rel=\"shortcut icon\" href=\"./favicon.png\" />\n<title>Lista de T\u00f3picos</title>" +
"<link href=\"./assets/css/font-awesome.min.css?assets_version=26\" rel=\"stylesheet\">" +
"<link href=\"./styles/basic_aqua/theme/stylesheet.css?assets_version=26\" rel=\"stylesheet\">" +
"<style>\nh2 { text-align:center; } ul { list-style: none; } li { font-size: 18px; margin: 0 10px 0 10px; } li:nth-child(odd) { background:#AFEEEE; }</style>" +
"</head><body><header id=\"site-description\" class=\"site-description\">\n" +
"<a id=\"logo\" class=\"logo\" href=\"./clubeceticismo.com.br.html\" title=\"Principal\">\n" +
"<img src=\"./styles/basic_aqua/theme/images/logo.png\" alt=\"Clube Ceticismo\"></a><h2>T\u00f3picos de acesso p\u00fablico "; 

    private final int orderBy;
    
    /**
     * 
     * @param orderBy 
     */
    public OrderedList(final int orderBy) {
        
        this.orderBy = orderBy;
        
    }//construtor
    
    /*
    *
    */
    @Override
    public void run() {

        try {

            showTopicsList();

        } 
        catch (IllegalArgumentException | NoSuchFileException e) {
            
            phantom.exception.ExceptionTools.errMessage(null, e);
        }
        catch (Exception e) {

            phantom.exception.ExceptionTools.crashMessage(null, e);
        }

    }//run
    
    /**
     * 
     * @throws Exception 
     */
    public void showTopicsList() throws Exception {
        
            StringBuilder htmlSyntax = new StringBuilder(HEAD);
            
            StringBuilder urlSyntax = new StringBuilder();

            htmlSyntax.append("por ordem alfab\u00e9tica");

            htmlSyntax.append("</h2></header><ul>");

            phantom.pages.Reader reader = new phantom.pages.Reader();

            TreeSet<phantom.pages.Page> topicsSet = reader.readAllPages(orderBy);

            for (phantom.pages.Page page : topicsSet) {
                
                String url = phantom.pages.Reader.topicFilenameToUrl(page);
                
                String pageName = page.getPageName();

                htmlSyntax.append("<li><a href=\"").
                    append(url).
                    append("\">").
                    append(pageName).
                    append("</a></li>\n");
                
                urlSyntax.append("[url=").
                    append(url).
                    append("][u]").
                    append(pageName).
                    append("[/u][/url]\n");
                
            }//for
            
            htmlSyntax.append("</ul></body></html>");

            String pathname = ROOT_DIR + "_list.html";

            toolbox.textfile.TextFileHandler tfh =
                new toolbox.textfile.TextFileHandler(pathname, "utf8");

            tfh.setContent(htmlSyntax.toString());

            tfh.write(pathname);

            toolbox.file.FileTools.openWebPage(new File(pathname)); 
            
            pathname = "_list.url";
            
            tfh.setContent(urlSyntax.toString());

            tfh.write(pathname);  
            
    }//showTopicList

}//classe OrderedList
