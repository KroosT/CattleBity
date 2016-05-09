package Graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage sheet;
    private int spriteCount;
    private int scale;
    private int spritesInWidth;

    public SpriteSheet(BufferedImage sheet, int spriteCount, int scale) {

        this.sheet = sheet;
        this.scale = scale;
        this.spriteCount = spriteCount;
        this.spritesInWidth = sheet.getWidth() / scale;

    }

    public BufferedImage getSprite(int index) {

        index = index % spriteCount;
        int x = index % spritesInWidth * scale;
        int y = index / spritesInWidth * scale;
        return sheet.getSubimage(x, y, scale, scale);

    }

}
