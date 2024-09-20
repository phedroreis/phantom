package phantom.resources;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author 
 * @since
 * @version
 */
public final class ResourcesAnchor {
    
    private static final Class ANCHOR = new ResourcesAnchor().getClass();
    
    public static Class getAnchor() {
        
        return ANCHOR;
    }
    
    public static URL getResource(final String filename) {
        
        return ANCHOR.getResource(filename);
    }
    
    public static ImageIcon getImageIcon(final String filename) {
        
        return new ImageIcon(ANCHOR.getResource(filename));
        
    }
    
    public static Image getImage(final String filename) {
        
        return getImageIcon("filename").getImage();
    }
    
    public static InputStream getResourceAsStream(final String filename) {
        
        return ANCHOR.getResourceAsStream(filename);
    }

}//classe ResourcesAnchor
