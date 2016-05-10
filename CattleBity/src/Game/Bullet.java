package Game;

import Game.Level.Level;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;
import IO.Input;
import com.sun.scenario.effect.impl.prism.PrImage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Bullet extends Entity{

    private static final int SPRITES_PER_HEADING = 1;
    private static final int SPRITE_SCALE = 16;

    private enum Heading {

        NORTH(20 * SPRITE_SCALE, 6 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE),
        EAST(23 * SPRITE_SCALE, 6 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE),
        WEST(21 * SPRITE_SCALE, 6 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE),
        SOUTH(22 * SPRITE_SCALE, 6 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE);

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
    private Map<Heading, Sprite> spriteMap;
    private float speed;
    private float scale;

    public Bullet(float x, float y, float scale, float speed, TextureAtlas atlas) {
        super(EntityType.Bullet, x, y);

        heading = Heading.NORTH;
        spriteMap = new HashMap<Heading, Sprite>();
        for (Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }
    }

    @Override
    public void update(Input input, Level lvl){

    }

    @Override
    public void render(Graphics2D g){
        spriteMap.get(heading).render(g, x, y);
    }


}
