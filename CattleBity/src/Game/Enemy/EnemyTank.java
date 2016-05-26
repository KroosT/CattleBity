package Game.Enemy;

import Game.*;
import IO.Input;
import Graphics.TextureAtlas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import Graphics.Sprite2;
import Graphics.SpriteSheet2;

import static Game.Enemy.EnemyTank.EnemyTankType.*;
import static Game.Enemy.EnemyTank.Direction.*;

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

        GREY_NORTH(128,2,14,14),
        GREY_EAST(225,1,14,14),
        GREY_WEST(162,1,14,14),
        GREY_SOUTH(192,1,14,14),
        GREEN_NORTH(0,131,14,14),
        GREEN_EAST(96,129,14,14),
        GREEN_WEST(33,129,14,14),
        GREEN_SOUTH(64,129,14,14),
        RED_NORTH(129,131,14,14),
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
    private List<Bullet> bulletListNorth;
    private List<Bullet> bulletListEast;
    private List<Bullet> bulletListWest;
    private List<Bullet> bulletListSouth;
    private Map<Direction, List<Bullet>> bulletMap;
    private Direction direction;
    private Boom boom;
    private int boomKind;
    private boolean boomState;
    private int currFrame;
    private boolean shotOccured;

    public EnemyTank(float x, float y, float scale, float speed, TextureAtlas atlas){
        super(EntityType.EnemyTank, x, y);

        this.atlas = atlas;
        this.scale = scale;
        this.speed = speed;
        enemyTankType = GREY_SOUTH;
        enemyTankMap = new HashMap<EnemyTankType, Sprite2>();
        for (EnemyTankType t : EnemyTankType.values()) {
            SpriteSheet2 sheet = new SpriteSheet2(t.texture(atlas));
            Sprite2 sprite = new Sprite2(sheet, scale);
            enemyTankMap.put(t, sprite);
        }
        bulletListEast = new ArrayList<>();
        bulletListNorth = new ArrayList<>();
        bulletListSouth = new ArrayList<>();
        bulletListWest = new ArrayList<>();
        bulletMap = new HashMap<Direction, List<Bullet>>();
    }

    public void shot() {
        if (enemyTankType == GREY_NORTH || enemyTankType == RED_NORTH || enemyTankType == GREEN_NORTH) {
            bulletListNorth.add(new Bullet(x + 12, y, 2, 0, atlas));
            bulletMap.put(NORTH, bulletListNorth);
        } else if (enemyTankType == GREY_EAST || enemyTankType == RED_EAST || enemyTankType == GREEN_EAST) {
            bulletListEast.add(new Bullet(x + SPRITE_SCALE + 10, y + 12, 2, 1, atlas));
            bulletMap.put(EAST, bulletListEast);
        } else if (enemyTankType == GREY_WEST || enemyTankType == RED_WEST || enemyTankType == GREEN_WEST) {
            bulletListWest.add(new Bullet(x, y + 12, 2, 2, atlas));
            bulletMap.put(WEST, bulletListWest);
        } else if (enemyTankType == GREY_SOUTH || enemyTankType == RED_SOUTH || enemyTankType == GREEN_SOUTH) {
            bulletListSouth.add(new Bullet(x + 12, y + SPRITE_SCALE + 10, 2, 3, atlas));
            bulletMap.put(SOUTH, bulletListSouth);
        }
    }

    public void showShot(Collision collision) {
        shotOccured = true;
        Iterator it = bulletMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            if (pair.getKey() == Direction.values()[0]) {
                List list = (List)(pair.getValue());
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Bullet b = (Bullet) iter.next();
                    if (b.getY() - speed < 0) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY(), atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    } else b.setY(b.getY() - 2 * speed);
                    if (collision.BulletCollision(b, 3.2f * speed, 0)) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY(), atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    }
                }
            } else if (pair.getKey() == Direction.values()[1]) {
                List list = (List)(pair.getValue());
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Bullet b = (Bullet) iter.next();
                    if (b.getX() + speed > Game.WIDTH) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY(), atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    } else b.setX(b.getX() + 2 * speed);
                    if (collision.BulletCollision(b, speed, 3)) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY() + 10, atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    }
                }
            } else if (pair.getKey() == Direction.values()[2]) {
                List list = (List)(pair.getValue());
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Bullet b = (Bullet) iter.next();
                    if (b.getX() - speed < 0) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY(), atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    } else b.setX(b.getX() - 2 * speed);
                    if (collision.BulletCollision(b, 3.2f * speed, 2)) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX() - 10, b.getY() + 10, atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    }
                }
            } else if (pair.getKey() == Direction.values()[3]) {
                List list = (List) (pair.getValue());
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Bullet b = (Bullet) iter.next();
                    if (b.getY() - speed > Game.HEIGHT) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY(), atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    } else b.setY(b.getY() + 2 * speed);
                    if (collision.BulletCollision(b, speed, 1)) {
                        iter.remove();
                        //timerIntervalAfterBoom.start();
                        boom = new Boom(b.getX(), b.getY() + 25, atlas);
                        boom.setDelay(100);
                        boomState = true;
                        boomKind = 0;
                    }
                }
            }
        }
        if (bulletListNorth.isEmpty() && bulletListWest.isEmpty() && bulletListEast.isEmpty() && bulletListSouth.isEmpty())
            shotOccured = false;

        if (boomState) {
            currFrame = boom.update(boomKind);
            if (currFrame == boomKind + 3) {
                boom = null;
                boomState = false;
            }
        }
    }

    public boolean getShotState() {
        return shotOccured;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void changeDirection() {
        switch (enemyTankType) {
            case GREY_SOUTH:
                this.enemyTankType = GREY_EAST;
                break;
            case GREY_NORTH:
                this.enemyTankType = GREY_WEST;
                break;
            case GREY_EAST:
                this.enemyTankType = GREY_NORTH;
                break;
            case GREY_WEST:
                this.enemyTankType = GREY_NORTH;
                break;
        }
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
    public void render(Graphics2D g) {
        enemyTankMap.get(enemyTankType).render(g, x, y);
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
                g.drawImage(boom.getImage(boomKind), (int) (boom.getX() - 12 - boomKind * 2), (int) (boom.getY() - 20 - boomKind * 2), null);
            } catch (NullPointerException e) {
                boomState = false;
            }
        }
    }
}
