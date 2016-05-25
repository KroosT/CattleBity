package Game.Enemy;

import Graphics.TextureAtlas;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyStrategy {

    private List<EnemyTank> enemyTankList;
    private TextureAtlas atlas;

    public EnemyStrategy(TextureAtlas atlas) {
        this.atlas = atlas;
        enemyTankList = new ArrayList<>();
    }

    public List<EnemyTank> getEnemyTankList() {
        return enemyTankList;
    }

    public void destroyEnemyTank(EnemyTank enemyTank) {
        enemyTankList.remove(enemyTank);
    }

    public void addEnemyTank(float x, float y) {
        enemyTankList.add(new EnemyTank(x, y, 2, atlas));
    }

    public void render(Graphics2D g) {
        for (EnemyTank e : enemyTankList) {
            e.render(g);
        }
    }

}
