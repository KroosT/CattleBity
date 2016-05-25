package Game;

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

    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public abstract void update(Input input, Collision collision, Player player);

    public abstract void render(Graphics2D g);

}
