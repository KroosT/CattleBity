package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceLoader {

    private static final String PATH = "CattleBity\\res\\";

    public static BufferedImage LoadImage(String filename) {

        BufferedImage image = null;
        try{
            image = ImageIO.read(new File(PATH + filename));
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;

    }

}
