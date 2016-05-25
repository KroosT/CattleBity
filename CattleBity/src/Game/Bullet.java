package Game;

import Graphics.Sprite2;
import Graphics.SpriteSheet2;
import Graphics.TextureAtlas;
import IO.Input;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Bullet extends Entity{

    private enum Heading {

        NORTH(323, 102, 4, 5),
        EAST(346, 102, 4, 4),
        WEST(330, 102, 4, 4),
        SOUTH(339, 102, 4, 4);

        private int x, y, w, h;

        Heading(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        protected BufferedImage texture(TextureAtlas atlas) {
            return atlas.Cut(x, y, w, h);
        }
    }

    private Heading heading;
    private Map<Heading, Sprite2> spriteMap;

    public Bullet(float x, float y, float scale, int heading, TextureAtlas atlas) {
        super(EntityType.Bullet, x, y);

        this.heading = fromNumberInHeading(heading);
        spriteMap = new HashMap<Heading, Sprite2>();
        for (Heading h : Heading.values()) {
            SpriteSheet2 sheet = new SpriteSheet2(h.texture(atlas));
            Sprite2 sprite = new Sprite2(sheet, scale);
            spriteMap.put(h, sprite);
        }
    }

    @Override
    public void update(Input input, Collision collision, Player player){

    }

    @Override
    public void render(Graphics2D g){
        spriteMap.get(heading).render(g, x, y);
    }

    private Heading fromNumberInHeading(int number) {
        switch (number) {
            case 0:
                 return heading = Heading.NORTH;
            case 1:
                return heading = Heading.EAST;
            case 2:
                return heading = Heading.WEST;
            case 3:
                return heading = Heading.SOUTH;
            default:
                return heading = Heading.NORTH;
        }
    }

}
