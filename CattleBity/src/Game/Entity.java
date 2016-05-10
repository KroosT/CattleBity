package Game;

import Game.Level.Level;
import IO.Input;

import java.awt.*;

public abstract class Entity {

    public final EntityType type;

    protected float x;
    protected float y;

    protected Entity(EntityType type, float x, float y) {

        this.type = type;
        this.x = x;
        this.y = y;

    }

    public abstract void update(Input input, Level lvl);

    public abstract void render(Graphics2D g);

}
