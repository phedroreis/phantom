package phantom.main;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import static phantom.global.GlobalConstants.ROOT_DIR;

public class ImageType {

    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) throws Exception {

        toolbox.file.SearchFolders s = new toolbox.file.SearchFolders(".*", false);

        LinkedList<File> list = s.search(new File(ROOT_DIR + "download"));

        for (File file: list) {        

            ImageInputStream iis = ImageIO.createImageInputStream(file);

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            if (imageReaders.hasNext()) {

                ImageReader reader = (ImageReader) imageReaders.next();

                System.out.printf("%s.%s%n", file.getName(), reader.getFormatName());

            }  

        }

    }
    
}
