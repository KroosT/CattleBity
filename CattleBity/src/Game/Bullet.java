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

    private static final int SPRITES_PER_HEADING = 1;
    private static final int SPRITE_SCALE = 3;

    private enum Heading {

        NORTH(108 * SPRITE_SCALE - 1, 34 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE + 1),
        EAST(115 * SPRITE_SCALE, 34 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE + 2, SPRITES_PER_HEADING * SPRITE_SCALE + 2),
        WEST(110 * SPRITE_SCALE, 34 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE + 1, SPRITES_PER_HEADING * SPRITE_SCALE + 1),
        SOUTH(113 * SPRITE_SCALE, 34 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE + 2);

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
    private float speed = 2;
    private float scale;

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
