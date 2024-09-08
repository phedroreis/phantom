package phantom.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import javax.management.modelmbean.XMLParseException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.ROOT_DIR;

/**
 *
 * @author Pedro Reis
 */
public class List extends JFrame {
    
    private static final String HEAD =
"<!DOCTYPE html>\n<html lang=\"pt-br\">\n<head>\n<meta charset=\"utf-8\" />\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
"<link rel=\"shortcut icon\" href=\"./favicon.png\" />\n<title>Lista de T\u00f3picos</title>" +
"<link href=\"./assets/css/font-awesome.min.css?assets_version=26\" rel=\"stylesheet\">" +
"<link href=\"./styles/basic_aqua/theme/stylesheet.css?assets_version=26\" rel=\"stylesheet\">" +
"<style>\nh2 { text-align:center; } ul { list-style: none; } li { font-size: 18px; margin: 0 10px 0 10px; } li:nth-child(odd) { background:#AFEEEE; }</style>" +
"</head><body><header id=\"site-description\" class=\"site-description\">\n" +
"<a id=\"logo\" class=\"logo\" href=\"./clubeceticismo.com.br.html\" title=\"Principal\">\n" +
"<img src=\"./styles/basic_aqua/theme/images/logo.png\" alt=\"Clube Ceticismo\"></a><h2>T\u00f3picos de acesso p\u00fablico "; 
    
    
    private final JPanel westPanel;
    private final JPanel eastPanel;
    private final JRadioButton lexical;
    private final JRadioButton lastPost;
    private final JRadioButton creationDate;
    private final ButtonGroup buttonGroup;
    private final JButton jButton;    

    public List() {
        
        super("Lista de T\u00f3picos");
        
        setLayout(new BorderLayout());
        
        westPanel = new JPanel();
        
        westPanel.setLayout(new BorderLayout());
        
        lexical = new JRadioButton("Ordem alfab\u00e9tica", true);
        lastPost = new JRadioButton("Postagem mais recente", false);
        creationDate = new JRadioButton("Criado mais recentemente", false);
 
        buttonGroup = new ButtonGroup();
        
        buttonGroup.add(lexical);
        buttonGroup.add(lastPost);
        buttonGroup.add(creationDate);
        
        westPanel.add(lexical, BorderLayout.NORTH);
        westPanel.add(lastPost, BorderLayout.CENTER);
        westPanel.add(creationDate, BorderLayout.SOUTH); 
        
        jButton = new JButton("Gerar");
        
        jButton.addActionListener(
            
            (ActionEvent e) -> {
                
                int choice;
                
                if (lexical.isSelected()) 
                    choice = 0;
                else if (lastPost.isSelected())
                    choice = 1;
                else
                    choice = 2;
                    
                OrderedList lt = new OrderedList(choice);
                
                Thread thread = new Thread(lt); thread.start();
            }
        );
        
        eastPanel = new JPanel();  
        
        eastPanel.add(jButton);
        
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        
        setSize(330, 90);
        
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }//construtor
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
  
        try {
            Initializer.init();
            
            List listAllTopics = new List();
            
            listAllTopics.setVisible(true);
        } 
        catch (FileNotFoundException e) {
            
            phantom.exception.ExceptionTools.crashMessage(null, e);
        }
        
    }//main
    
    /*
    *
    */
    private class OrderedList implements Runnable {
        
        private final int orderBy;
        
        /*
        *
        */
        public OrderedList(final int choice) {
            
            this.orderBy = choice;

        }//construtor
        
        /*
        *
        */
        @Override
        public void run() {
            
            try {
                
                StringBuilder sb = new StringBuilder(HEAD);
                
                switch (orderBy) {
                    case 0:
                        sb.append("por ordem alfab\u00e9tica");
                        break;
                    case 1:
                        sb.append("por postagem mais recente");
                        break;
                    case 2:
                        sb.append("por data de cria\u00e7\u00e3o");
                }
                
                sb.append("</h2></header><ul>");
                
                phantom.pages.Reader reader = new phantom.pages.Reader();
                
                TreeSet<phantom.pages.Page> topicsSet = reader.readAllPages(orderBy);
                
                for (phantom.pages.Page page : topicsSet) 
                    
                    sb.append("<li><a href=\"").
                       append(phantom.pages.Reader.topicFilenameToUrl(page)).
                       append("\">").
                       append(page.getPageName()).
                       append("</a></li>\n");

                sb.append("</ul></body></html>");
                
                String pathname = ROOT_DIR + "_list.html";
                
                toolbox.textfile.TextFileHandler tfh =
                    new toolbox.textfile.TextFileHandler(pathname, "utf8");
                
                tfh.setContent(sb.toString());
                
                tfh.write();
                
                toolbox.file.FileTools.openWebPage(new File(pathname));
                
            } catch (XMLParseException | IOException | NullPointerException e) {
                
                phantom.exception.ExceptionTools.crashMessage(null, e);
                
            }
            
        }//run
        
    }//classe privada OrderedList
    
}//ListAllTopics