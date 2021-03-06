package Game.Enemy;

import Game.Collision;
import Game.Level.Level;
import Game.Player;
import Game.SecondPlayer;
import Graphics.TextureAtlas;
import utils.Time;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyStrategy {

    private List<EnemyTank> enemyTankList;
    private TextureAtlas atlas;
    private Collision collision;
    private Player player;
    private SecondPlayer secondPlayer;
    private Level level;
    private boolean interval;
    private Timer timer;
    private Timer respaunTimer;
    private Timer changeLevelTimer;
    private Timer firstRespawnTimer;
    private int count;

    public EnemyStrategy(TextureAtlas atlas, Collision collision, Player player, SecondPlayer secondPlayer, Level level) {
        this.atlas = atlas;
        this.collision = collision;
        this.player = player;
        this.secondPlayer = secondPlayer;
        this.level = level;
        enemyTankList = new ArrayList<>();
        interval = true;
        timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interval = true;
                timer.stop();
            }
        });
        respaunTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEnemyTank();
                count++;
                respaunTimer.stop();
            }
        });
        changeLevelTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level.changeLevel();
                reloadEnemyStrategy();
                changeLevelTimer.stop();
            }
        });
        firstRespawnTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstRespawnTimer.stop();
            }
        });
        enemyTankList.add(new EnemyTank(60, 60, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(500, 30, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(180, 35, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(270, 60, 2, 1, atlas, player));
        count = 20;
    }

    public List<EnemyTank> getEnemyTankList() {
        return enemyTankList;
    }

    public void destroyEnemyTank(EnemyTank enemyTank) {
        enemyTankList.remove(enemyTank);
        level.removeTankFromTanksLeft();
    }

    public void addEnemyTank() {
        Random random = new Random(System.currentTimeMillis());
        switch (random.nextInt(4)) {
            case 0:
                enemyTankList.add(new EnemyTank(60, 60, 2, 1, atlas, player));
                break;
            case 1:
                enemyTankList.add(new EnemyTank(500, 30, 2, 1, atlas, player));
                break;
            case 2:
                enemyTankList.add(new EnemyTank(180, 35, 2, 1, atlas, player));
                break;
            case 3:
                enemyTankList.add(new EnemyTank(270, 60, 2, 1, atlas, player));
                break;
        }
    }

    public void update() {
        if (level.getStageChanging())
            return;
        if (enemyTankList.size() < 4 && count < 20) {
            respaunTimer.start();
        }
        if (count == 20 && enemyTankList.size() == 0) {
            changeLevelTimer.start();
        }
        for (EnemyTank e : enemyTankList) {

            int direction = e.getDirection();
            switch (direction) {
                case 0:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else if (collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {

                    } else {
                        e.setY(e.getY() - e.getSpeed());
                    }
                    break;
                case 1:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else if (collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {

                    } else {
                        e.setY(e.getY() + e.getSpeed());
                    }
                    break;
                case 2:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else if (collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {

                    } else {
                        e.setX(e.getX() - e.getSpeed());
                    }
                    break;
                case 3:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else if (collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {

                    } else {
                        e.setX(e.getX() + e.getSpeed());
                    }
                    break;
            }
            if (interval) {
                e.changeDirection();
            }
            e.shot(collision);
        }
        interval = false;
        timer.start();
    }

    public void updatePlayer(Player player) {
        this.player = player;
        for (EnemyTank e : enemyTankList) {
            e.updatePlayer(player);
        }
        level.updatePlayer(player);
    }

    public void reloadEnemyStrategy() {
        enemyTankList.clear();
        enemyTankList.add(new EnemyTank(60, 60, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(500, 30, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(180, 35, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(270, 60, 2, 1, atlas, player));
        count = 20;
    }

    public void render(Graphics2D g) {
        for (EnemyTank e : enemyTankList) {
            e.render(g);
        }
    }

}
