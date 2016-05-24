package Graphics;

import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kroos on 24.05.2016.
 */
public class Sprite2 {

    private SpriteSheet2 sheet;
    private float scale;
    private BufferedImage image;

    public Sprite2(SpriteSheet2 sheet, float scale) {

        this.sheet = sheet;
        this.scale = scale;
        image = sheet.getSprite();
        image = Utils.resize(image, (int) (image.getWidth() * scale), (int) (image.getWidth() * scale));

    }

    public void render(Graphics2D g, float x, float y) {

        g.drawImage(image, (int)x, (int)y, null);

    }

}
