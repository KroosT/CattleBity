package Game.Enemy;

import Game.Collision;
import Game.Player;
import Game.SecondPlayer;
import Graphics.TextureAtlas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyStrategy {

    private List<EnemyTank> enemyTankList;
    private TextureAtlas atlas;
    private Collision collision;
    private Timer timerInterval;
    private boolean firstTime;
    private Player player;
    private SecondPlayer secondPlayer;


    public EnemyStrategy(TextureAtlas atlas, Collision collision, Player player, SecondPlayer secondPlayer) {
        this.atlas = atlas;
        this.collision = collision;
        this.player = player;
        this.secondPlayer = secondPlayer;
        enemyTankList = new ArrayList<>();
        firstTime = true;
    }

    public List<EnemyTank> getEnemyTankList() {
        return enemyTankList;
    }

    public void destroyEnemyTank(EnemyTank enemyTank) {
        enemyTankList.remove(enemyTank);
    }

    public void addEnemyTank(float x, float y) {
        enemyTankList.add(new EnemyTank(x, y, 2, 2, atlas));
    }

    public void update() {
        for (EnemyTank e : enemyTankList) {
            int direction = e.getDirection();
            switch (direction) {
                case 0:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction) ||
                            collision.EnemyTankSecondPlayerTankCollision(secondPlayer, e.getSpeed(), direction)) {
                        e.changeDirection();
                    } else e.setY(e.getY() - e.getSpeed());
                    break;
                case 1:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction) ||
                            collision.EnemyTankSecondPlayerTankCollision(secondPlayer, e.getSpeed(), direction)) {
                        e.changeDirection();
                    } else e.setY(e.getY() + e.getSpeed());
                    break;
                case 2:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction) ||
                            collision.EnemyTankSecondPlayerTankCollision(secondPlayer, e.getSpeed(), direction)) {
                        e.changeDirection();
                    } else e.setX(e.getX() - e.getSpeed());
                    break;
                case 3:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction) ||
                            collision.EnemyTankSecondPlayerTankCollision(secondPlayer, e.getSpeed(), direction)) {
                        e.changeDirection();
                    } else e.setX(e.getX() + e.getSpeed());
                    break;
            }
            e.shot(collision);
        }
    }

    public void render(Graphics2D g) {
        for (EnemyTank e : enemyTankList) {
            e.render(g);
        }
    }

}
