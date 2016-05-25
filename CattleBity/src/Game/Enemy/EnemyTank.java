package Game.Enemy;

import Game.Collision;
import Game.Entity;
import Game.EntityType;
import Game.Player;
import IO.Input;
import Graphics.TextureAtlas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import Graphics.Sprite2;
import Graphics.SpriteSheet2;

public class EnemyTank extends Entity {

    public enum EnemyTankType {

        GREY_NORTH(128,2,14,14),
        GREY_EAST(225,1,14,14),
        GREY_WEST(162,1,14,14),
        GREY_SOUTH(192,1,14,14),
        GREEN_NORTH(0,131,14,14),
        GREEN_EAST(96,129,14,14),
        GREEN_WEST(33,129,14,14),
        GREEN_SOUTH(64,129,14,14),
        RED_NORTH(129,131,14,14),
        RED_EAST(224,129,14,14),
        RED_WEST(162,129,14,14),
        RED_SOUTH(192,129,14,14);

        private int x, y, w, h;

        EnemyTankType(int x, int y, int w, int h){
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
    private Map<EnemyTankType, Sprite2> enemyTankMap;
    private EnemyTankType enemyTankType;
    private float scale;

    public EnemyTank(float x, float y, float scale, TextureAtlas atlas){
        super(EntityType.EnemyTank, x, y);

        this.atlas = atlas;
        this.scale = scale;
        enemyTankType = EnemyTankType.GREY_SOUTH;
        enemyTankMap = new HashMap<EnemyTankType, Sprite2>();
        for (EnemyTankType t : EnemyTankType.values()) {
            SpriteSheet2 sheet = new SpriteSheet2(t.texture(atlas));
            Sprite2 sprite = new Sprite2(sheet, scale);
            enemyTankMap.put(t, sprite);
        }
    }

    @Override
    public void update(Input input, Collision collision, Player player) {

    }

    @Override
    public void render(Graphics2D g) {
        enemyTankMap.get(enemyTankType).render(g, x, y);
    }
}
