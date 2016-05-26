package Game;

import Graphics.TextureAtlas;
import IO.Input;
import utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Game.Boom.Frames.*;

public class Boom extends Entity {

    public enum Frames {
        FIRST(257, 130, 15, 13),
        SECOND(272, 129, 16, 16),
        THIRD(287, 128, 17, 17),
        FOURTH(305, 130, 30, 30),
        FIFTH(335, 128, 33, 33);

        private int x, y, w, h;

        Frames(int x, int y, int w, int h) {
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
    private Frames[] bigboom = {FIRST, SECOND, THIRD, FOURTH, FIFTH, FOURTH, THIRD, SECOND, FIRST, FIRST};
    private Frames[] smallboom = {FIRST, SECOND, FIRST, FIRST};
    private long startTime;
    private long delay;
    private int currentFrame;



    public Boom(float x, float y, TextureAtlas atlas) {
        super(EntityType.Boom, x, y);
        this.atlas = atlas;
    }

    public void setDelay(long d) {
        delay = d;
    }

    public int update(int boomKind) {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if (elapsed >= delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        switch (boomKind) {
            case 0:
                if (currentFrame == smallboom.length)
                    currentFrame = 0;
                break;
            case 6:
                if (currentFrame == bigboom.length)
                    currentFrame = 0;
                break;
        }
        return currentFrame;
    }

    public BufferedImage getImage(int boomKind) {
        switch (boomKind) {
            case 0:
                return Utils.resize(smallboom[currentFrame].texture(atlas), smallboom[currentFrame].w * 2, smallboom[currentFrame].h * 2);
            case 6:
                return Utils.resize(bigboom[currentFrame].texture(atlas), bigboom[currentFrame].w * 2, bigboom[currentFrame].h * 2);
            default:
                return Utils.resize(smallboom[currentFrame].texture(atlas), smallboom[currentFrame].w * 2, smallboom[currentFrame].h * 2);
        }
    }

    @Override
    public void update(Input input, Collision collision, Player player) {

    }

    @Override
    public void updateSecondPlayer(Input input, Collision collision, SecondPlayer secondPlayer) {}

    @Override
    public void render(Graphics2D g) {
    }
}
