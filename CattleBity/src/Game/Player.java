package Game;

import Game.Menu.Pause;
import IO.Input;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
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
    }

    private Heading heading;
    private int lives;
    private Map<Heading, Sprite> spriteMap;
    private float scale;
    private float speed;
    private TextureAtlas atlas;
    private Bullet bullet;
    private Boom boom;
    private boolean boomState;
    private int currFrame;
    private int boomKind;
    private boolean interval;
    private boolean movePlaying;
    private Timer timer;
    private AudioClip shot;
    private AudioClip littleBoom;
    private AudioClip bigBoom;
    private boolean destroyed;
    private File littleBoomFile = new File("CattleBity\\res\\Sounds\\littleBoom.wav");
    private File bigBoomFile = new File("CattleBity\\res\\Sounds\\BigBoom.wav");
    private File shotFile = new File("CattleBity\\res\\Sounds\\shot.wav");


    public Player(float x, float y, float scale, float speed, TextureAtlas atlas, Pause pause){
        super(EntityType.Player, x, y);

        this.lives = 3;
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
        destroyed = false;
        interval = true;
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interval = true;
                timer.stop();
            }
        });
        try {
            shot = Applet.newAudioClip(shotFile.toURL());
            littleBoom = Applet.newAudioClip(littleBoomFile.toURL());
            bigBoom = Applet.newAudioClip(bigBoomFile.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Input input, Collision collision, Player player){
        float newX = x;
        float newY = y;


        if (input.getKey(KeyEvent.VK_UP)) {
            heading = NORTH;
            if (collision.TankCollision(player, speed, 0) || collision.EnemyTankPlayerTankCollision(player, speed, 0)) {

            } else
                newY -= speed;
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            heading = SOUTH;
            if (collision.TankCollision(player, speed, 1) || collision.EnemyTankPlayerTankCollision(player, speed, 1)) {

            } else
                newY += speed;
        } else if (input.getKey(KeyEvent.VK_LEFT)) {
            heading = WEST;
            if (collision.TankCollision(player, speed, 2) || collision.EnemyTankPlayerTankCollision(player, speed, 2)) {

            } else
                newX -= speed;
        } else if (input.getKey(KeyEvent.VK_RIGHT)) {
            heading = EAST;
            if (collision.TankCollision(player, speed, 3) || collision.EnemyTankPlayerTankCollision(player, speed, 3)) {

            } else
                newX += speed;
        }

        if (input.getKey(KeyEvent.VK_SPACE)) {

            if (bullet == null && interval) {
                switch (heading) {
                    case NORTH:
                        bullet = new Bullet(x + 12, y, 2, 0, atlas);
                        break;
                    case EAST:
                        bullet = new Bullet(x + SPRITE_SCALE + 10, y + 12, 2, 1, atlas);
                        break;
                    case WEST:
                        bullet = new Bullet(x, y + 12, 2, 2, atlas);
                        break;
                    case SOUTH:
                        bullet = new Bullet(x + 12, y + SPRITE_SCALE + 10, 2, 3, atlas);
                        break;
                }
                shot.stop();
                shot.play();
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
            if (bullet.getHeading() == 0) {
                if (collision.BulletCollision(bullet, 3.2f * speed, 0)) {
                    boom = new Boom(bullet.x, bullet.y, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    littleBoom.stop();
                    littleBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else if (collision.EnemyTankBulletCollision(bullet, 3.2f * speed, 0)) {
                    boom = new Boom(bullet.x, bullet.y, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 6;
                    bigBoom.stop();
                    bigBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else
                    bullet.y -= 2 * speed;
            } else if (bullet.getHeading() == 1) {
                if (collision.BulletCollision(bullet, speed, 3)) {
                    boom = new Boom(bullet.x, bullet.y + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    littleBoom.stop();
                    littleBoom.play();
                    boomKind = 0;
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else if (collision.EnemyTankBulletCollision(bullet, speed, 3)) {
                    boom = new Boom(bullet.x, bullet.y + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 6;
                    bigBoom.stop();
                    bigBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else bullet.x += 2 * speed;
            } else if (bullet.getHeading() == 2) {
                if (collision.BulletCollision(bullet, 3.2f * speed, 2)) {
                    boom = new Boom(bullet.x - 10, bullet.y + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    littleBoom.stop();
                    littleBoom.play();
                    boomKind = 0;
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else if (collision.EnemyTankBulletCollision(bullet, 3.2f * speed, 2)) {
                    boom = new Boom(bullet.x - 10, bullet.y + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 6;
                    bigBoom.stop();
                    bigBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else bullet.x -= 2 * speed;
            } else if (bullet.getHeading() == 3) {
                if (collision.BulletCollision(bullet, speed, 1)) {
                    boom = new Boom(bullet.x, bullet.y + 25, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    littleBoom.stop();
                    littleBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else if (collision.EnemyTankBulletCollision(bullet, speed, 1)) {
                    boom = new Boom(bullet.x, bullet.y + 25, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 6;
                    bigBoom.stop();
                    bigBoom.play();
                    bullet = null;
                    shotOccured = false;
                    interval = false;
                    timer.start();
                } else bullet.y += 2 * speed;
            }
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
            bullet.render(g);
        }
        if (boomState) {
            try {
                g.drawImage(boom.getImage(boomKind), (int) (boom.x - 12 - boomKind * 2), (int) (boom.y - 20 - boomKind * 2), null);
            } catch (NullPointerException e) {
                boomState = false;
            }
        }
    }

    @Override
    public void updateSecondPlayer(Input input, Collision collision, SecondPlayer secondPlayer) {}

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

    public void destroy() {
        destroyed = true;
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
