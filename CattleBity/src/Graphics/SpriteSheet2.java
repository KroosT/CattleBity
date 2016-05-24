package Graphics;

import java.awt.image.BufferedImage;

/**
 * Created by Kroos on 24.05.2016.
 */
public class SpriteSheet2 {

    BufferedImage image;

    public SpriteSheet2(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getSprite() {
        return image;
    }

}

