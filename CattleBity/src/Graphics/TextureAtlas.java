package Graphics;

import utils.ResourceLoader;

import java.awt.image.BufferedImage;

public class TextureAtlas {

    private BufferedImage image;

    public TextureAtlas(String imagename) {
        image = ResourceLoader.LoadImage(imagename);
    }

    public BufferedImage Cut(int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }

}
