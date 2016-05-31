package Game;

import Graphics.TextureAtlas;
import utils.ResourceLoader;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOver {

    private BufferedImage end;
    private BufferedImage win;
    private boolean gameOver;
    private boolean winState;

    public GameOver(TextureAtlas textureAtlas) {
        end = textureAtlas.Cut(289, 184, 32, 16);
        end = Utils.resize(end, 96, 48);
        win = Utils.resize(ResourceLoader.LoadImage("win.png"), 800, 606);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setWin(boolean win) {
        this.winState = win;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getWinState() {
        return winState;
    }

    public void render(Graphics2D g) {
        if (gameOver)
            g.drawImage(end, 378, 288, null);
        if (winState)
            g.drawImage(win, 0, 0, null);
    }
}
