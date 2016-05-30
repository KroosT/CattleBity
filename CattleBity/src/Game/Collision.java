package Game;

import Game.Enemy.EnemyStrategy;
import Game.Enemy.EnemyTank;
import Game.Level.Level;

import java.awt.*;
import java.util.ArrayList;
import  java.util.List;

public class Collision {

    private static final int TILE_SCALE = 16;

    private List<Point> coords;
    private List<Point> waterCoords;
    private List<Point> iceCoords;
    private List<Point> emptyCoords;
    private List<EnemyTank> enemyTankList;
    private List<Point> infoCoords;
    private List<Point> eagleCoords;
    private Level lvl;
    private boolean iceCol;
    private EnemyStrategy enemyStrategy;
    private List<Point> metalCoords;

    public Collision(List<Point> coords, Level lvl) {
        this.coords = coords;
        this.enemyStrategy = enemyStrategy;
        this.eagleCoords = lvl.getEagleCoords();
        this.waterCoords = lvl.getWaterCoords();
        this.iceCoords = lvl.getIceCoords();
        this.emptyCoords = lvl.getEmptyCoords();
        this.metalCoords = lvl.getMetalCoords();
        infoCoords = lvl.getInfoCoords();
        this.lvl = lvl;
        this.iceCol = false;
    }

    public boolean TankCollision(Player player, float speed, int var) {

        boolean col = false;
        Rectangle playerRect;
        switch (var) {
            case 0:
                playerRect = new Rectangle((int)player.x, (int)(player.y - 6 * speed), 30, 30);
                break;
            case 1:
                playerRect = new Rectangle((int)player.x, (int)(player.y + speed), 30, 30);
                break;
            case 2:
                playerRect = new Rectangle((int)(player.x - 6 * speed), (int)player.y, 30, 30);
                break;
            case 3:
                playerRect = new Rectangle((int)(player.x + speed), (int)player.y, 30, 30);
                break;
            default:
                playerRect = new Rectangle((int)player.x, (int)player.y, 30, 30);
                break;
        }

        for (Point p : coords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : waterCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : metalCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : infoCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : iceCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                player.setSpeed(4);
                iceCol = true;
                break;
            }
        }
        if (iceCol) {
            for (Point p : emptyCoords) {
                Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
                if (playerRect.intersects(pRect)) {
                    player.setSpeed(2);
                    iceCol = false;
                    break;
                }
            }
        }
        for (Point p : eagleCoords) {
            Rectangle eagleRect = new Rectangle(p.x, p.y, 42, 42);
            if (playerRect.intersects(eagleRect))
                col = true;
        }
        return col;
    }

    public boolean SecondPlayerCollision(SecondPlayer secondPlayer, float speed, int var) {

        boolean col = false;
        Rectangle playerRect;
        switch (var) {
            case 0:
                playerRect = new Rectangle((int)secondPlayer.x, (int)(secondPlayer.y - 6 * speed), 30, 30);
                break;
            case 1:
                playerRect = new Rectangle((int)secondPlayer.x, (int)(secondPlayer.y + speed), 30, 30);
                break;
            case 2:
                playerRect = new Rectangle((int)(secondPlayer.x - 6 * speed), (int)secondPlayer.y, 30, 30);
                break;
            case 3:
                playerRect = new Rectangle((int)(secondPlayer.x + speed), (int)secondPlayer.y, 30, 30);
                break;
            default:
                playerRect = new Rectangle((int)secondPlayer.x, (int)secondPlayer.y, 30, 30);
                break;
        }

        for (Point p : coords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : waterCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : metalCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : infoCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : iceCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                secondPlayer.setSpeed(4);
                iceCol = true;
                break;
            }
        }
        if (iceCol) {
            for (Point p : emptyCoords) {
                Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
                if (playerRect.intersects(pRect)) {
                    secondPlayer.setSpeed(2);
                    iceCol = false;
                    break;
                }
            }
        }
        for (Point p : eagleCoords) {
            Rectangle eagleRect = new Rectangle(p.x, p.y, 42, 42);
            if (playerRect.intersects(eagleRect))
                col = true;
        }
        return col;
    }

    public boolean BulletCollision(Bullet bullet, float speed, int var) {

        boolean col = false;
        Rectangle bulletRect;
        switch (var) {
            case 0:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y - speed), 8, 8);
                break;
            case 1:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y + speed), 8, 8);
                break;
            case 2:
                bulletRect = new Rectangle((int)(bullet.x - speed), (int)bullet.y, 8, 8);
                break;
            case 3:
                bulletRect = new Rectangle((int)(bullet.x + speed), (int)bullet.y, 8, 8);
                break;
            default:
                bulletRect = new Rectangle((int)bullet.x, (int)bullet.y, 8, 8);
                break;
        }

        for (Point p : coords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 15, 15);
            if (bulletRect.intersects(pRect)){
                col = true;
                lvl.destroyTile(p.y / TILE_SCALE, p.x / TILE_SCALE);
                lvl.removeCoords(p);
                break;
            }
        }
        for (Point p : eagleCoords) {
            Rectangle eagleRect = new Rectangle(p.x, p.y, 42, 42);
            if (bulletRect.intersects(eagleRect)) {
                col = true;
                lvl.destroyTile(p.y / TILE_SCALE, p.x / TILE_SCALE);
                lvl.removeEagleCoords(p);
                break;
            }
        }
        for (Point p : metalCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (bulletRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : infoCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (bulletRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        return col;
    }

    public boolean EnemyTankEnemyTankCollision() {
        boolean col = false;
        Rectangle rect;
        Rectangle tRect;
        List<EnemyTank> temp = enemyTankList;
        for (EnemyTank e : enemyTankList) {
            temp.remove(e);

            switch (e.getDirection()) {
                case 0:
                    rect = new Rectangle((int)e.x, (int)(e.y - 6 * e.getSpeed()), 30, 30);
                    break;
                case 1:
                    rect = new Rectangle((int)e.x, (int)(e.y + e.getSpeed()), 30, 30);
                    break;
                case 2:
                    rect = new Rectangle((int)(e.x - 6 * e.getSpeed()), (int)e.y, 30, 30);
                    break;
                case 3:
                    rect = new Rectangle((int)(e.x + e.getSpeed()), (int)e.y, 30, 30);
                    break;
                default:
                    rect = new Rectangle((int)e.x, (int)e.y, 30, 30);
                    break;
            }

            temp = enemyTankList;
        }
        return col;
    }

    public boolean EnemyTankPlayerTankCollision(Player player, float speed, int direction) {
        boolean col = false;
        List list = new ArrayList<>();
        Rectangle playerRect;
        switch (direction) {
            case 0:
                playerRect = new Rectangle((int)player.x, (int)(player.y - 12 * speed), 30, 30);
                break;
            case 1:
                playerRect = new Rectangle((int)player.x, (int)(player.y + 2 * speed), 30, 30);
                break;
            case 2:
                playerRect = new Rectangle((int)(player.x - 12 * speed), (int)player.y, 30, 30);
                break;
            case 3:
                playerRect = new Rectangle((int)(player.x + 2 * speed), (int)player.y, 30, 30);
                break;
            default:
                playerRect = new Rectangle((int)player.x, (int)player.y, 30, 30);
                break;
        }

        for (EnemyTank t : enemyTankList) {
            Rectangle tRect;
            switch (t.getDirection()){
                case 0:
                    tRect = new Rectangle((int)t.x, (int)(t.y - 12 * speed), 30, 30);
                    break;
                case 1:
                    tRect = new Rectangle((int)t.x, (int)(t.y + 2 * speed), 30, 30);
                    break;
                case 2:
                    tRect = new Rectangle((int)(t.x - 12 * speed), (int)t.y, 30, 30);
                    break;
                case 3:
                    tRect = new Rectangle((int)(t.x + 2 * speed), (int)t.y, 30, 30);
                    break;
                default:
                    tRect = new Rectangle((int)t.x, (int)t.y, 30, 30);
                    break;
            }
            if (playerRect.intersects(tRect)) {
                list.add(true);
            }
        }
        if (list.contains(true)) {
            col = true;
        }
        return col;
    }

    public boolean EnemyTankSecondPlayerTankCollision(SecondPlayer secondPlayer, float speed, int direction) {
        boolean col = false;
        Rectangle playerRect;
        switch (direction) {
            case 0:
                playerRect = new Rectangle((int)secondPlayer.x, (int)(secondPlayer.y - 6 * speed), 30, 30);
                break;
            case 1:
                playerRect = new Rectangle((int)secondPlayer.x, (int)(secondPlayer.y + speed), 30, 30);
                break;
            case 2:
                playerRect = new Rectangle((int)(secondPlayer.x - 6 * speed), (int)secondPlayer.y, 30, 30);
                break;
            case 3:
                playerRect = new Rectangle((int)(secondPlayer.x + speed), (int)secondPlayer.y, 30, 30);
                break;
            default:
                playerRect = new Rectangle((int)secondPlayer.x, (int)secondPlayer.y, 30, 30);
                break;
        }

        for (EnemyTank t : enemyTankList) {
            Rectangle tRect = new Rectangle((int)t.x, (int)t.y, 30, 30);
            if (playerRect.intersects(tRect)) {
                col = true;
            }
        }

        return col;
    }

    public boolean EnemyTankBulletCollision(Bullet bullet, float speed, int direction) {
        boolean col = false;
        Rectangle bulletRect;
        switch (direction) {
            case 0:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y - speed), 10, 10);
                break;
            case 1:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y + speed), 10, 10);
                break;
            case 2:
                bulletRect = new Rectangle((int)(bullet.x - speed), (int)bullet.y, 10, 10);
                break;
            case 3:
                bulletRect = new Rectangle((int)(bullet.x + speed), (int)bullet.y, 10, 10);
                break;
            default:
                bulletRect = new Rectangle((int)bullet.x, (int)bullet.y, 10, 10);
                break;
        }

        for (EnemyTank t : enemyTankList) {
            Rectangle tRect = new Rectangle((int)t.x, (int)t.y, 30, 30);
            if (bulletRect.intersects(tRect)) {
                col = true;
                enemyStrategy.destroyEnemyTank(t);
                break;
            }
        }

        return col;
    }

    public boolean EnemyTankCollision(EnemyTank enemyTank, float speed, int direction) {
        this.enemyTankList = enemyStrategy.getEnemyTankList();
        boolean col = false;
        Rectangle enemyTankRect;
        switch (direction) {
            case 0:
                enemyTankRect = new Rectangle((int)enemyTank.x, (int)(enemyTank.y - 6 * speed), 30, 30);
                break;
            case 1:
                enemyTankRect = new Rectangle((int)enemyTank.x, (int)(enemyTank.y + speed), 30, 30);
                break;
            case 2:
                enemyTankRect = new Rectangle((int)(enemyTank.x - 6 * speed), (int)enemyTank.y, 30, 30);
                break;
            case 3:
                enemyTankRect = new Rectangle((int)(enemyTank.x + speed), (int)enemyTank.y, 30, 30);
                break;
            default:
                enemyTankRect = new Rectangle((int)enemyTank.x, (int)enemyTank.y, 30, 30);
                break;
        }

        for (Point p : coords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (enemyTankRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : waterCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (enemyTankRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : metalCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (enemyTankRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : infoCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (enemyTankRect.intersects(pRect)){
                col = true;
                break;
            }
        }
        for (Point p : iceCoords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (enemyTankRect.intersects(pRect)){
                enemyTank.setSpeed(4);
                iceCol = true;
                break;
            }
        }
        if (iceCol) {
            for (Point p : emptyCoords) {
                Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
                if (enemyTankRect.intersects(pRect)) {
                    enemyTank.setSpeed(2);
                    iceCol = false;
                    break;
                }
            }
        }
        for (Point p : eagleCoords) {
            Rectangle eagleRect = new Rectangle(p.x, p.y, 42, 42);
            if (enemyTankRect.intersects(eagleRect))
                col = true;
        }
        return col;
    }

    public boolean PlayerTankBulletCollision(Player player, Bullet bullet, float speed, int direction) {
        boolean col = false;
        Rectangle bulletRect;
        switch (direction) {
            case 0:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y - speed), 10, 10);
                break;
            case 1:
                bulletRect = new Rectangle((int)bullet.x, (int)(bullet.y + speed), 10, 10);
                break;
            case 2:
                bulletRect = new Rectangle((int)(bullet.x - speed), (int)bullet.y, 10, 10);
                break;
            case 3:
                bulletRect = new Rectangle((int)(bullet.x + speed), (int)bullet.y, 10, 10);
                break;
            default:
                bulletRect = new Rectangle((int)bullet.x, (int)bullet.y, 10, 10);
                break;
        }

        Rectangle playerRect;
        switch (direction) {
            case 0:
                playerRect = new Rectangle((int)player.x, (int)(player.y - 4 * speed), 30, 30);
                break;
            case 1:
                playerRect = new Rectangle((int)player.x, (int)(player.y + speed), 30, 30);
                break;
            case 2:
                playerRect = new Rectangle((int)(player.x - speed), (int)player.y, 30, 30);
                break;
            case 3:
                playerRect = new Rectangle((int)(player.x + speed), (int)player.y, 30, 30);
                break;
            default:
                playerRect = new Rectangle((int)player.x, (int)player.y, 30, 30);
                break;
        }

        if (playerRect.intersects(bulletRect)) {
            col = true;
            player.destroy();
        }

        return col;
    }

    public void setEnemyStrategy(EnemyStrategy enemyStrategy) {
        this.enemyStrategy = enemyStrategy;
    }
}