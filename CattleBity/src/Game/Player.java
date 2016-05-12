package Game;

import IO.Input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import Graphics.Sprite;
import Graphics.TextureAtlas;
import Graphics.SpriteSheet;

public class Player extends Entity {

    public static final int SPRITE_SCALE = 16;
    public static final int SPRITES_PER_HEADING = 1;
    private Bullet bullet;
    private boolean shotOccured = false;

    private enum Heading {
        NORTH(0 * SPRITE_SCALE, 0 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, 1 * SPRITE_SCALE),
        EAST(6 * SPRITE_SCALE, 0 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, 1 * SPRITE_SCALE),
        WEST(2 * SPRITE_SCALE, 0 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, 1 * SPRITE_SCALE),
        SOUTH(4 * SPRITE_SCALE, 0 * SPRITE_SCALE, SPRITES_PER_HEADING * SPRITE_SCALE, 1 * SPRITE_SCALE);

        private int x, y, w, h;

        Heading(int x, int y, int w, int h){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        protected BufferedImage texture(TextureAtlas atlas) {
            return atlas.Cut(x, y, w, h);
        }

        protected int getWidth() {
            return w;
        }

        protected int getHeight() {
            return h;
        }
    }

    private Heading heading;
    private Map<Heading, Sprite> spriteMap;
    private float scale;
    private float speed;
    private TextureAtlas atlas;
    private Heading shotDirection;

    public Player(float x, float y, float scale, float speed, TextureAtlas atlas){
        super(EntityType.Player, x, y);

        this.atlas = atlas;
        this.scale = scale;
        this.speed = speed;
        heading = Heading.NORTH;
        shotDirection = Heading.NORTH;
        spriteMap = new HashMap<Heading, Sprite>();
        for (Heading h : Heading.values()){
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }
    }

    @Override
    public void update(Input input, Collision collision, Player player){
        float newX = x;
        float newY = y;

        if (input.getKey(KeyEvent.VK_UP)) {
            newY -= speed;
            heading = Heading.NORTH;
            if (collision.MacroCollision(player, 3.2f * speed, 0))
                newY += speed;
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            newY += speed;
            heading = Heading.SOUTH;
            if (collision.MacroCollision(player, speed, 1))
                newY -= speed;
        } else if (input.getKey(KeyEvent.VK_LEFT)) {
            newX -= speed;
            heading = Heading.WEST;
            if (collision.MacroCollision(player, 3.2f * speed, 2))
                newX += speed;
        } else if (input.getKey(KeyEvent.VK_RIGHT)) {
            newX += speed;
            heading = Heading.EAST;
            if (collision.MacroCollision(player, speed, 3))
                newX -= speed;
        }

        if (input.getKey(KeyEvent.VK_SPACE)) {
            if (!shotOccured) {
                switch (heading) {
                    case NORTH:
                        bullet = new Bullet(x + 13, y, 1, 0, atlas);
                        shotDirection = Heading.NORTH;
                        break;
                    case EAST:
                        bullet = new Bullet(x + SPRITE_SCALE, y + 13, 1, 1, atlas);
                        shotDirection = Heading.EAST;
                        break;
                    case WEST:
                        bullet = new Bullet(x, y + 13, 1, 2, atlas);
                        shotDirection = Heading.WEST;
                        break;
                    case SOUTH:
                        bullet = new Bullet(x + 13, y + SPRITE_SCALE, 1, 3, atlas);
                        shotDirection = Heading.SOUTH;
                        break;
                }
                shotOccured = true;
            }
        }

        if (newX < 0) {
            newX = 0;
        } else if (newX >= Game.WIDTH - SPRITE_SCALE * scale) {
            newX = Game.WIDTH - SPRITE_SCALE * scale;
        }
        if (newY < 0) {
            newY = 0;
        } else if (newY >= Game.HEIGHT - SPRITE_SCALE * scale) {
            newY = Game.HEIGHT - SPRITE_SCALE * scale;
        }

        if (shotOccured) {
            switch (shotDirection) {
                case NORTH:
                    if (bullet.y - speed < 0) {
                        bullet = null;
                        shotOccured = false;
                    } else bullet.y -= speed;
                    break;
                case EAST:
                    if (bullet.x + speed > Game.WIDTH) {
                        bullet = null;
                        shotOccured = false;
                    } else bullet.x += speed;
                    break;
                case WEST:
                    if (bullet.x - speed < 0) {
                        bullet = null;
                        shotOccured = false;
                    } else bullet.x -= speed;
                    break;
                case SOUTH:
                    if (bullet.y - speed > Game.HEIGHT) {
                        bullet = null;
                        shotOccured = false;
                    } else bullet.y += speed;
                    break;
            }
        }

        x = newX;
        y = newY;
    }

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
        if (shotOccured) {
            bullet.render(g);
        }
    }

    public int getWidth() {
        return heading.getWidth();
    }

    public int getHeight() {
        return heading.getHeight();
    }
}
