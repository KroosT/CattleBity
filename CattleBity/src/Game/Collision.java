package Game;

import Game.Level.Level;

import java.awt.*;
import  java.util.List;

public class Collision {

    private static final int TILE_SCALE = 16;

    private List<Point> coords;
    private Level lvl;

    public Collision(List<Point> coords, Level lvl) {
        this.coords = coords;
        this.lvl = lvl;
    }

    public boolean TankCollision(Player player, float speed, int var) {

        boolean col = false;
        Rectangle playerRect;
        switch (var) {
            case 0:
                playerRect = new Rectangle((int)player.x, (int)(player.y - speed), 30, 30);
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

        for (Point p : coords) {
            Rectangle pRect = new Rectangle(p.x, p.y, 10, 10);
            if (playerRect.intersects(pRect)){
                col = true;
                break;
            }
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
        return col;
    }
}