package phantom.resources;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/***********************************************************************************************************************
 *
 * @author Pedro Reis
 * @since 1.0 - 20 de setembro de 2024
 * @version 1.0
 **********************************************************************************************************************/
public final class Resources implements toolbox.file.ResourcesAnchor {
    
    private static final Class ANCHOR = new Resources().getClass();
    
    @Override
    public Class getAnchor() {
        
        return ANCHOR;
    }
    
    @Override
    public URL getResource(final String filename) {
        
         return ANCHOR.getResource(filename);
    }
    
    @Override
    public ImageIcon getImageIcon(final String filename) {
        
        return new ImageIcon(getResource(filename));
        
    }
    
    @Override
    public Image getImage(final String filename) {
        
        return getImageIcon(filename).getImage();
    }
    
    @Override
    public InputStream getResourceAsStream(final String filename) {
        
        return ANCHOR.getResourceAsStream(filename);
    }

}//classe Resources
