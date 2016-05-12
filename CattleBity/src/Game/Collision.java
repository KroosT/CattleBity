package Game;

import java.awt.*;
import  java.util.List;

public class Collision {

    private List<Point> coords;

    public Collision(List<Point> coords) {
        this.coords = coords;
    }

    public boolean MacroCollision(Player player, float speed, int var) {

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
}