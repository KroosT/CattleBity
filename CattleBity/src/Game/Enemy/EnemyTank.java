package Game.Enemy;

import Game.*;
import IO.Input;
import Graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import Graphics.Sprite2;
import Graphics.SpriteSheet2;
import javax.swing.Timer;

import static Game.Enemy.EnemyTank.EnemyTankType.*;

public class EnemyTank extends Entity {

    private static final int SPRITE_SCALE = 16;

    public enum Direction {
        NORTH,
        EAST,
        WEST,
        SOUTH;

        Direction() {
        }
    }

    public enum EnemyTankType {

        GREY_NORTH(128,2,14,13),
        GREY_EAST(225,1,14,14),
        GREY_WEST(162,1,14,14),
        GREY_SOUTH(192,1,14,14),
        GREEN_NORTH(0,131,14,13),
        GREEN_EAST(96,129,14,14),
        GREEN_WEST(33,129,14,14),
        GREEN_SOUTH(64,129,14,14),
        RED_NORTH(129,131,14,13),
        RED_EAST(224,129,14,14),
        RED_WEST(162,129,14,14),
        RED_SOUTH(192,129,14,14);

        private int x, y, w, h;

        EnemyTankType(int x, int y, int w, int h){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        protected BufferedImage texture(TextureAtlas atlas) {
            return atlas.Cut(x, y, w, h);
        }
    }

    private TextureAtlas atlas;
    private Map<EnemyTankType, Sprite2> enemyTankMap;
    private EnemyTankType enemyTankType;
    private float scale;
    private float speed;
    private Bullet bullet;
    private Boom boom;
    private int boomKind;
    private boolean boomState;
    private int currFrame;
    private boolean interval;
    private Timer timer;
    private boolean shotOccured;

    public EnemyTank(float x, float y, float scale, float speed, TextureAtlas atlas){
        super(EntityType.EnemyTank, x, y);

        this.atlas = atlas;
        this.scale = scale;
        this.speed = speed;
        enemyTankType = RED_SOUTH;
        enemyTankMap = new HashMap<EnemyTankType, Sprite2>();
        for (EnemyTankType t : EnemyTankType.values()) {
            SpriteSheet2 sheet = new SpriteSheet2(t.texture(atlas));
            Sprite2 sprite = new Sprite2(sheet, scale);
            enemyTankMap.put(t, sprite);
        }
        interval = true;
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interval = true;
                timer.stop();
            }
        });
    }

    public void shot(Collision collision) {
        if (bullet == null && interval) {
            if (enemyTankType == GREY_NORTH || enemyTankType == RED_NORTH || enemyTankType == GREEN_NORTH) {
                bullet = new Bullet(x + 12, y, 2, 0, atlas);
            } else if (enemyTankType == GREY_EAST || enemyTankType == RED_EAST || enemyTankType == GREEN_EAST) {
                bullet = new Bullet(x + SPRITE_SCALE + 10, y + 12, 2, 1, atlas);
            } else if (enemyTankType == GREY_WEST || enemyTankType == RED_WEST || enemyTankType == GREEN_WEST) {
                bullet = new Bullet(x, y + 12, 2, 2, atlas);
            } else if (enemyTankType == GREY_SOUTH || enemyTankType == RED_SOUTH || enemyTankType == GREEN_SOUTH) {
                bullet = new Bullet(x + 12, y + SPRITE_SCALE + 10, 2, 3, atlas);
            }
            shotOccured = true;
        }
        if (shotOccured) {
            if (bullet.getHeading() == 0) {
                if (bullet.getY() - speed < 0) {
                    boom = new Boom(bullet.getX(), bullet.getY(), atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                } else bullet.setY(bullet.getY() - 2 * speed);
                if (collision.BulletCollision(bullet, 3.2f * speed, 0)) {
                    boom = new Boom(bullet.getX(), bullet.getY(), atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                }
            } else if (bullet.getHeading() == 1) {
                if (bullet.getX() + speed > Game.WIDTH) {
                    boom = new Boom(bullet.getX(), bullet.getY(), atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                } else bullet.setX(bullet.getX() + 2 * speed);
                if (collision.BulletCollision(bullet, speed, 3)) {
                    boom = new Boom(bullet.getX(), bullet.getY() + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                }
            } else if (bullet.getHeading() == 2) {
                if (bullet.getX() - speed < 0) {
                    boom = new Boom(bullet.getX(), bullet.getY(), atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                } else bullet.setX(bullet.getX() - 2 * speed);
                if (collision.BulletCollision(bullet, 3.2f * speed, 2)) {
                    boom = new Boom(bullet.getX() - 10, bullet.getY() + 10, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                }
            } else if (bullet.getHeading() == 3) {
                if (bullet.getY() - speed > Game.HEIGHT) {
                    boom = new Boom(bullet.getX(), bullet.getY(), atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                } else bullet.setY(bullet.getY() + 2 * speed);
                if (collision.BulletCollision(bullet, speed, 1)) {
                    boom = new Boom(bullet.getX(), bullet.getY() + 25, atlas);
                    boom.setDelay(100);
                    boomState = true;
                    boomKind = 0;
                    bullet = null;
                    interval = false;
                    timer.start();
                    shotOccured = false;
                }
            }
        }

        if (boomState) {
            currFrame = boom.update(boomKind);
            if (currFrame == boomKind + 3) {
                boom = null;
                boomState = false;
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public EnemyTankType fromNumericGrey(int direction) {
        if (direction == 0)
            return GREY_NORTH;
        else if (direction == 1)
            return GREY_EAST;
        else if (direction == 2)
            return GREY_WEST;
        else return GREY_SOUTH;
    }

    public EnemyTankType fromNumericGreen(int direction) {
        if (direction == 0)
            return GREEN_NORTH;
        else if (direction == 1)
            return GREEN_EAST;
        else if (direction == 2)
            return GREEN_WEST;
        else return GREEN_SOUTH;
    }

    public EnemyTankType fromNumericRed(int direction) {
        if (direction == 0)
            return RED_NORTH;
        else if (direction == 1)
            return RED_EAST;
        else if (direction == 2)
            return RED_WEST;
        else return RED_SOUTH;
    }

    public void changeDirection() {
        Random random = new Random();
        if (enemyTankType == GREY_NORTH || enemyTankType == GREY_SOUTH || enemyTankType == GREY_WEST || enemyTankType == GREY_EAST)
            enemyTankType = fromNumericGrey(random.nextInt(4));
        else if (enemyTankType == RED_NORTH || enemyTankType == RED_SOUTH || enemyTankType == RED_WEST || enemyTankType == RED_EAST)
            enemyTankType = fromNumericRed(random.nextInt(4));
        else
            enemyTankType = fromNumericGreen(random.nextInt(4));
    }

    public int getDirection() {
        if (enemyTankType == GREEN_NORTH || enemyTankType == GREY_NORTH || enemyTankType == RED_NORTH) {
            return 0;
        } else if (enemyTankType == GREEN_EAST || enemyTankType == GREY_EAST || enemyTankType == RED_EAST) {
            return 3;
        } else if (enemyTankType == GREEN_WEST || enemyTankType == GREY_WEST || enemyTankType == RED_WEST) {
            return 2;
        } else return 1;
    }

    @Override
    public void update(Input input, Collision collision, Player player) {

    }

    @Override
    public void updateSecondPlayer(Input input, Collision collision, SecondPlayer secondPlayer) {}

    @Override
    public void render(Graphics2D g) {
        enemyTankMap.get(enemyTankType).render(g, x, y);
        if (shotOccured) {
            bullet.render(g);
        }
        if (boomState) {
            try {
                g.drawImage(boom.getImage(boomKind), (int) (boom.getX() - 12 - boomKind * 2), (int) (boom.getY() - 20 - boomKind * 2), null);
            } catch (NullPointerException e) {
                boomState = false;
            }
        }
    }
}
