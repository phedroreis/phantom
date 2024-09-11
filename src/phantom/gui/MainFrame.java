package phantom.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class MainFrame extends JFrame {
    
    private final CenterPanel center;
    private final SouthPanel south;

    /**
     * 
     */
    public MainFrame() {
        
        setSize(600, 500); 
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        center = new phantom.gui.CenterPanel();
        south = new phantom.gui.SouthPanel();        
        
        add(GlobalComponents.NORTH, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);        
                
        addComponentListener(
            
            new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent componentEvent) {
                    GlobalComponents.TERMINAL.resize(getWidth());
                }
            }
            
        );
        
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    toolbox.log.Log.closeFile();//Fecha o arquivo de log.
                }
            }
        );

    }//construtor

}//classe MainFrame
