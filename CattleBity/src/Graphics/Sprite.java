package Graphics;

import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {

    private SpriteSheet sheet;
    private float scale;
    private BufferedImage image;

    public Sprite(SpriteSheet sheet, float scale) {

        this.sheet = sheet;
        this.scale = scale;
        image = sheet.getSprite(0);
        image = Utils.resize(image, (int) (image.getWidth() * scale), (int) (image.getWidth() * scale));

    }

    public void render(Graphics2D g, float x, float y) {

        g.drawImage(image, (int)x, (int)y, null);

    }

}
