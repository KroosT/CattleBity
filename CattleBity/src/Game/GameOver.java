package Game;

import Graphics.TextureAtlas;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameOver {

    private BufferedImage end;
    private boolean gameOver;

    public GameOver(TextureAtlas textureAtlas) {
        end = textureAtlas.Cut(289, 184, 32, 16);
        end = Utils.resize(end, 96, 48);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void render(Graphics2D g) {
        g.drawImage(end, 378, 288, null);
    }
}
