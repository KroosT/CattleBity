package Game.Menu;

import Game.*;
import IO.Input;
import utils.ResourceLoader;
import utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static Game.Menu.Menu.Position.*;

public class Menu extends Entity {

    public enum Position {

        UP,
        DOWN;

        Position(){
        }
    }

    private BufferedImage menu;
    private Position position;

    public Menu(String menuName) {
        super(EntityType.Menu, 0, 0);
        menu = ResourceLoader.LoadImage(menuName);
        menu = Utils.resize(menu, 800, 608);
        position = UP;
    }

    @Override
    public void update(Input input, Collision collision, Player player) {

        if (input.getKey(KeyEvent.VK_UP)) {
            if (position == UP)
                return;
            else {
                player.setY(player.getY() - 40);
                position = UP;
            }
        } else if (input.getKey(KeyEvent.VK_DOWN)) {
            if (position == DOWN)
                return;
            else {
                player.setY(player.getY() + 40);
                position = DOWN;
            }
        } else if (input.getKey(KeyEvent.VK_ENTER)) {
            if (position == UP) {
                Game.setChoiced(true);
                Game.setMenuChoice(0);
                player.setX(415);
                player.setY(540);
                player.setHeading(0);
            } else if (position == DOWN) {
                Game.setChoiced(true);
                Game.setMenuChoice(1);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(menu, 0, 0, null);
    }
}
