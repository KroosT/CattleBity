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
    private boolean firstTime;
    private Player player;
    private SecondPlayer secondPlayer;
    private Level level;
    private boolean interval;
    private Timer timer;
    private Timer respaunTimer;
    private int count;
    private BufferedImage[] beforeAppear;

    public EnemyStrategy(TextureAtlas atlas, Collision collision, Player player, SecondPlayer secondPlayer, Level level) {
        this.atlas = atlas;
        this.collision = collision;
        this.player = player;
        this.secondPlayer = secondPlayer;
        this.level = level;
        enemyTankList = new ArrayList<>();
        firstTime = true;
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
        //beforeAppear[0] = Utils.resize(atlas.Cut(258, 100, 9, 9), 18, 18);
        //beforeAppear[1] = Utils.resize(atlas.Cut(273, 98, 12, 12), 24, 24);
        //beforeAppear[2] = Utils.resize(atlas.Cut(289, 97, 13, 13), 26, 26);
        //beforeAppear[3] = Utils.resize(atlas.Cut(304, 96, 15, 15), 30, 30);
        enemyTankList.add(new EnemyTank(60, 60, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(500, 30, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(180, 35, 2, 1, atlas, player));
        enemyTankList.add(new EnemyTank(270, 60, 2, 1, atlas, player));
        count = 4;
    }

    public List<EnemyTank> getEnemyTankList() {
        return enemyTankList;
    }

    public void destroyEnemyTank(EnemyTank enemyTank) {
        enemyTankList.remove(enemyTank);
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
        if (enemyTankList.size() < 4 && count < 20) {
            respaunTimer.start();
        }
        for (EnemyTank e : enemyTankList) {

            int direction = e.getDirection();
            switch (direction) {
                case 0:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else {
                        e.setY(e.getY() - e.getSpeed());
                    }
                    break;
                case 1:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else {
                        e.setY(e.getY() + e.getSpeed());
                    }
                    break;
                case 2:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else {
                        e.setX(e.getX() - e.getSpeed());
                    }
                    break;
                case 3:
                    if (collision.EnemyTankCollision(e, e.getSpeed(), direction) ||
                            collision.EnemyTankPlayerTankCollision(player, e.getSpeed(), direction) ||
                            collision.EnemyTankSecondPlayerTankCollision(secondPlayer, e.getSpeed(), direction)) {
                        e.changeDirection();
                    }
                    else {
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

    public void render(Graphics2D g) {
        for (EnemyTank e : enemyTankList) {
            e.render(g);
        }
    }

}
