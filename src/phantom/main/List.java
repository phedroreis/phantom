package phantom.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import static phantom.global.GlobalConstants.*;

/**
 *
 * @author Pedro Reis
 */
public class List extends JFrame {
    
    private final JPanel westPanel;
    private final JPanel eastPanel;
    private final JRadioButton lexical;
    private final JRadioButton lastPost;
    private final JRadioButton creationDate;
    private final ButtonGroup buttonGroup;
    private final JButton jButton;  

    public List() {
        
        super("Lista de T\u00f3picos");
           
        setIconImage(phantom.resources.ResourcesAnchor.getImage("favicon.png"));
       
        setLayout(new BorderLayout());
        
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    
                    toolbox.log.Log.closeFile();
                }
             
            }
        );        
        
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
                    
                phantom.run.OrderedList lt = new phantom.run.OrderedList(choice);
                
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
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
  
        try {
            
            //Cria, se nao existir ainda, o diretorio onde sera gravado o arquivo de log
            toolbox.file.FileTools.createDirsIfNotExists(LOG_DIR);
        
            //Cria arquivo de log. O nome sera a date e hora atual.
            toolbox.log.Log.createLogFile(LOG_DIR);    
            
            List listAllTopics = new List();
            
            listAllTopics.setVisible(true);
        } 
        catch (Exception e) {
             
            e.printStackTrace(toolbox.log.Log.getStream());
            
            toolbox.log.Log.closeFile();
            
            phantom.exception.ExceptionTools.crash(e);
        }
        
    }//main
     
}//ListAllTopics