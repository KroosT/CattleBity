package Game.Menu;

import utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kroos on 01.06.2016.
 */
public class Pause {

    private boolean isPaused;
    private BufferedImage pauseScreen;

    public Pause() {
        pauseScreen = ResourceLoader.LoadImage("pauseScreen.png");
    }

    public boolean getPauseState() {
        return isPaused;
    }

    public void setPausedState(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void render(Graphics2D g) {
        g.drawImage(pauseScreen, 0, 0, null);
    }
}
