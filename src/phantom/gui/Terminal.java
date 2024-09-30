package phantom.gui;

import java.awt.Font;
import javax.swing.JTextArea;

/***********************************************************************************************************************
 * Terminal nao editavel na janela de interface principal para saidas do programa.
 * 
 * @author Pedro Reis
 * 
 * @since 1.1 - 21 de setembro de 2024
 * 
 * @version 1.0
 **********************************************************************************************************************/
public final class Terminal extends JTextArea {
    
    private static final int FONT_SIZE = 11;
    private static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);

    /*******************************************************************************************************************
     * Construtor da classe, cria  um terminal nao editavel para saidas do programa. 
     ******************************************************************************************************************/
    protected Terminal() {

        super();
        
        setFont(MONO);
 
        setEditable(false); 
        
    }//construtor 
    
    /*******************************************************************************************************************
     * Implementa escrita "thread-safe" no terminal da janela de interface principal. Escreve uma
     * <code>String</code> seguida de quebra de linha. 
     * 
     * @param append A <code>String</code>.
     ******************************************************************************************************************/
    public void appendln(final String append) {
        
        java.awt.EventQueue.invokeLater(() -> {
            append(append + toolbox.string.StringTools.NEWLINE);
        });  
        
    }//appendln
    
}//classe Terminal
