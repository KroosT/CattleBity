package Game;

import IO.Input;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

import Graphics.Sprite;
import Graphics.TextureAtlas;
import Graphics.SpriteSheet;

import static Game.Player.Heading.*;

public class Player extends Entity {

    public static final int SPRITE_SCALE = 16;
    public static final int SPRITES_PER_HEADING = 1;
    private boolean shotOccured = false;

    public enum Heading {
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
    private Map<Heading, List<Bullet>> bulletMap;
    private List<Bullet> bulletListNorth;
    private List<Bullet> bulletListEast;
    private List<Bullet> bulletListWest;
    private List<Bullet> bulletListSouth;
    private boolean interval;
    private boolean intervalAfterBoom;
    private Timer timerInterval;
    private Timer timerIntervalAfterBoom;
    private Boom boom;
    private boolean boomState;
    private int currFrame;
    private int boomKind;

    public Player(float x, float y, float scale, float speed, TextureAtlas atlas){
        super(EntityType.Player, x, y);

        this.atlas = atlas;
        this.scale = scale;
        this.speed = speed;
        heading = EAST;
        spriteMap = new HashMap<Heading, Sprite>();
        for (Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h, sprite);
        }
        bulletListNorth = new ArrayList<Bullet>();
        bulletListEast = new ArrayList<Bullet>();
        bulletListWest = new ArrayList<Bullet>();
        bulletListSouth = new ArrayList<Bullet>();
        bulletMap = new HashMap<Heading, List<Bullet>>();
        interval = true;
        intervalAfterBoom = true;
        timerInterval = new Timer(700, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                interval = true;
                timerInterval.stop();
            }
        });
        timerIntervalAfterBoom = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                intervalAfterBoom = true;
                timerIntervalAfterBoom.stop();
            }
        });
    }

    @Override
    public void update(Input input, Collision collision, Player player){
        float newX = x;
        float newY = y;

        if (input.getKey(KeyEvent.VK_UP)) {
            newY -= speed;
            heading = NORTH;
            if (collision.TankCollision(player, 3.2f * speed, 0) || collision.EnemyTankPlayerTankCollision(player, speed, 0))
                newY += speed;
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            newY += speed;
            heading = SOUTH;
            if (collision.TankCollision(player, speed, 1) || collision.EnemyTankPlayerTankCollision(player, speed, 1))
                newY -= speed;
        } else if (input.getKey(KeyEvent.VK_LEFT)) {
            newX -= speed;
            heading = WEST;
            if (collision.TankCollision(player, 3.2f * speed, 2) || collision.EnemyTankPlayerTankCollision(player, speed, 2))
                newX += speed;
        } else if (input.getKey(KeyEvent.VK_RIGHT)) {
            newX += speed;
            heading = EAST;
            if (collision.TankCollision(player, speed, 3) || collision.EnemyTankPlayerTankCollision(player, speed, 3))
                newX -= speed;
        }

        if (input.getKey(KeyEvent.VK_SPACE)) {
            if (interval && intervalAfterBoom) {
                timerInterval.start();
                switch (heading) {
                    case NORTH:
                        Bullet bulNorth = new Bullet(x + 12, y, 2, 0, atlas);
                        bulletListNorth.add(bulNorth);
                        bulletMap.put(heading, bulletListNorth);
                        break;
                    case EAST:
                        Bullet bulEast = new Bullet(x + SPRITE_SCALE + 10, y + 12, 2, 1, atlas);
                        bulletListEast.add(bulEast);
                        bulletMap.put(heading, bulletListEast);
                        break;
                    case WEST:
                        Bullet bulWest = new Bullet(x, y + 12, 2, 2, atlas);
                        bulletListWest.add(bulWest);
                        bulletMap.put(heading, bulletListWest);
                        break;
                    case SOUTH:
                        Bullet bulSouth = new Bullet(x + 12, y + SPRITE_SCALE + 10, 2, 3, atlas);
                        bulletListSouth.add(bulSouth);
                        bulletMap.put(heading, bulletListSouth);
                        break;
                }
                shotOccured = true;
                interval = false;
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
            Iterator it = bulletMap.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                if (pair.getKey() == Heading.values()[0]) {
                    List list = (List)(pair.getValue());
                    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                        Bullet b = (Bullet) iter.next();
                        if (b.y - speed < 0) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                            boomKind = 0;
                        } else b.y -= speed;
                        if (collision.BulletCollision(b, 3.2f * speed, 0)) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                            boomKind = 0;
                        } else if (collision.EnemyTankBulletCollision(b, 3.2f * speed, 0)) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                            boomKind = 6;
                        }
                    }
                } else if (pair.getKey() == Heading.values()[1]) {
                    List list = (List)(pair.getValue());
                    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                        Bullet b = (Bullet) iter.next();
                        if (b.x + speed > Game.WIDTH) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        } else b.x += speed;
                        if (collision.BulletCollision(b, speed, 3)) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y + 10, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        }
                    }
                } else if (pair.getKey() == Heading.values()[2]) {
                    List list = (List)(pair.getValue());
                    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                        Bullet b = (Bullet) iter.next();
                        if (b.x - speed < 0) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        } else b.x -= speed;
                        if (collision.BulletCollision(b, 3.2f * speed, 2)) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x - 10, b.y + 10, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        }
                    }
                } else if (pair.getKey() == Heading.values()[3]) {
                    List list = (List)(pair.getValue());
                    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                        Bullet b = (Bullet) iter.next();
                        if (b.y - speed > Game.HEIGHT) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        } else b.y += speed;
                        if (collision.BulletCollision(b, speed, 1)) {
                            iter.remove();
                            timerIntervalAfterBoom.start();
                            boom = new Boom(b.x, b.y + 25, atlas);
                            boom.setDelay(100);
                            boomState = true;
                        }
                    }
                }

            }
            if (bulletListNorth.isEmpty() && bulletListWest.isEmpty() && bulletListEast.isEmpty() && bulletListSouth.isEmpty())
                shotOccured = false;
        }

        x = newX;
        y = newY;
        if (boomState) {
            currFrame = boom.update(boomKind);
            if (currFrame == boomKind + 3) {
                boom = null;
                boomState = false;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        spriteMap.get(heading).render(g, x, y);
        if (shotOccured) {
            Iterator it = bulletMap.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                List list = (List)(pair.getValue());
                for (Object b : list)
                    ((Bullet) (b)).render(g);
            }
        }
        if (boomState) {
            try {
                g.drawImage(boom.getImage(boomKind), (int) (boom.x - 12 - boomKind * 2), (int) (boom.y - 20 - boomKind * 2), null);
            } catch (NullPointerException e) {
                boomState = false;
            }
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeading(int heading) {
        switch (heading) {
            case 0:
                this.heading = NORTH;
                break;
            case 1:
                this.heading = EAST;
                break;
            case 2:
                this.heading = WEST;
                break;
            case 3:
                this.heading = SOUTH;
                break;
        }
    }

}
