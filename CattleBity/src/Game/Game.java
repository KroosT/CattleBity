package Game;

import Display.Display;
import Game.Enemy.EnemyStrategy;
import Game.Level.Level;
import Game.Menu.Menu;
import Game.Menu.Pause;
import IO.Input;
import utils.Time;
import Graphics.TextureAtlas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 608;
    private static final String TITLE = "CattleBity";
    private static final int CLEAR_COLOR = 0xff000000;
    private static final int NUM_BUFFERS = 3;
    private static final float UPDATE_RATE = 60.0f;
    private static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    private static final long IDLE_TIME = 1;

    private static final String ATLAS_FILE_NAME = "textureAtlas_pros.png";
    private static final String MENU_FILE_NAME = "menu.png";

    private boolean running;
    private Thread gameThread;
    private Graphics2D graphics;
    private Input input;
    private TextureAtlas atlas;
    private Menu menu;
    private Player player;
    private SecondPlayer secondPlayer;
    private Level level;
    private GameOver gameOver;
    private Collision collision;
    private static int menuChoice;
    private static boolean choiced;
    private EnemyStrategy enemyStrategy;
    private Timer respawnPlayerTimer;
    private Pause pause;
    private Timer escAllowTimer;
    private boolean escAllow;
    private Timer backToMenuTimer;
    private boolean backToMenuTimerStart;

    public Game() {

        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        menu = new Menu(MENU_FILE_NAME);
        pause = new Pause();
        player = new Player(230, 323, 2, 1, atlas, pause);
        secondPlayer = new SecondPlayer(130, 323, 2, 1, atlas);
        gameOver = new GameOver(atlas);
        level = new Level(atlas, gameOver);
        collision = new Collision(level);
        enemyStrategy = new EnemyStrategy(atlas, collision, player, secondPlayer, level);
        collision.setEnemyStrategy(enemyStrategy);
        choiced = false;
        escAllow = true;
        backToMenuTimerStart = false;
        respawnPlayerTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setX(415);
                player.setY(540);
                player.setHeading(0);
                enemyStrategy.updatePlayer(player);
                player.setDestroyed(false);
                respawnPlayerTimer.stop();
            }
        });
        escAllowTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escAllow = true;
                escAllowTimer.stop();
            }
        });
        backToMenuTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMenu();
                backToMenuTimer.stop();
            }
        });
    }

    public synchronized void start() {

        if (running)
            return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    public synchronized void stop() {

        if (!running)
            return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cleanup();


    }

    private void update() {
        if (pause.getPauseState()) {
            if (input.getKey(KeyEvent.VK_M)) {
                menu = new Menu(MENU_FILE_NAME);
                pause = new Pause();
                player = new Player(230, 323, 2, 1, atlas, pause);
                secondPlayer = new SecondPlayer(130, 323, 2, 1, atlas);
                gameOver = new GameOver(atlas);
                level = new Level(atlas, gameOver);
                collision = new Collision(level);
                enemyStrategy = new EnemyStrategy(atlas, collision, player, secondPlayer, level);
                collision.setEnemyStrategy(enemyStrategy);
                choiced = false;
                escAllow = true;
                backToMenuTimerStart = false;
                respawnPlayerTimer = new Timer(4000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        player.setX(415);
                        player.setY(540);
                        player.setHeading(0);
                        enemyStrategy.updatePlayer(player);
                        player.setDestroyed(false);
                        respawnPlayerTimer.stop();
                    }
                });
                escAllowTimer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        escAllow = true;
                        escAllowTimer.stop();
                    }
                });
                backToMenuTimer = new Timer(4000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        backToMenuTimer.stop();
                        goToMenu();
                    }
                });
            }
        }
        if (input.getKey(KeyEvent.VK_ESCAPE)) {
            if (escAllow) {
                if (!pause.getPauseState())
                    pause.setPausedState(true);
                else
                    pause.setPausedState(false);
                escAllow = false;
                escAllowTimer.start();
            }
        }
        if (!choiced){
            menu.setSecondPlayer(secondPlayer);
            menu.update(input, collision, player);
        }
        else {
            if (!pause.getPauseState()) {
                if (level.getStageChanging()) {
                    player = new Player(415, 540, 2, 1, atlas, pause);
                    player.setHeading(0);
                    enemyStrategy.updatePlayer(player);
                    collision.CollisionReload();
                } else {
                    if (gameOver.getGameOver() || gameOver.getWinState()) {
                        level.setTileMapPlain();
                        if (!backToMenuTimer.isRunning()) {
                            backToMenuTimer.start();
                        }
                    } else {
                        if (menuChoice == 0) {
                            if (player.isDestroyed() && player.getLives() > 0) {
                                player.setX(760);
                                player.setY(566);
                                respawnPlayerTimer.start();
                                enemyStrategy.update();
                                level.update(gameOver);
                            } else if (player.isDestroyed() && player.getLives() <= 0) {
                                gameOver.setGameOver(true);
                            } else {
                                player.update(input, collision, player);
                                enemyStrategy.update();
                                level.update(gameOver);
                            }
                        } else if (menuChoice == 1) {
                            player.update(input, collision, player);
                            secondPlayer.updateSecondPlayer(input, collision, secondPlayer);
                            enemyStrategy.update();
                            level.update(gameOver);
                        }
                    }
                }
            }
        }
    }

    private void render() {
        Display.clear();
        if (!choiced) {
            menu.render(graphics);
            player.render(graphics);
        }
        else {
            if (!pause.getPauseState()) {
                if (level.getStageChanging()) {
                    level.render(graphics);
                } else {
                    if (gameOver.getGameOver() || gameOver.getWinState()) {
                        level.render(graphics);
                        gameOver.render(graphics);
                    } else {
                        if (menuChoice == 0) {
                            if (player.isDestroyed()) {
                                level.render(graphics);
                                enemyStrategy.render(graphics);
                                level.renderGrass(graphics);
                            } else {
                                level.render(graphics);
                                player.render(graphics);
                                enemyStrategy.render(graphics);
                                level.renderGrass(graphics);
                            }
                        } else if (menuChoice == 1) {
                            level.render(graphics);
                            player.render(graphics);
                            secondPlayer.render(graphics);
                            enemyStrategy.render(graphics);
                            level.renderGrass(graphics);
                        }

                    }

                }
            } else {
                pause.render(graphics);
            }
        }
        Display.swapBuffers();
    }

    private void cleanup() {

        Display.destroyWindow();

    }

    public void run(){

        int fps = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        float delta = 0;

        long lastTime = Time.get();
        while (running) {
            long now = Time.get();
            long elapsed = now - lastTime;
            lastTime = now;

            count += elapsed;

            boolean render = false;
            delta += ( elapsed / UPDATE_INTERVAL );
            while (delta > 1) {
                update();
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
                upd++;
            }
            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " | " + " Fps: " + fps + " | " + " Upd: " + upd + " | " + " Updl: " + updl);
                fps = 0;
                upd = 0;
                updl = 0;
                count = 0;
            }
        }
    }

    public static void setChoiced(boolean choice) {
        choiced = choice;
    }

    public static void setMenuChoice(int choice) {
        menuChoice = choice;
    }

    public void goToMenu() {
        menu = new Menu(MENU_FILE_NAME);
        pause = new Pause();
        player = new Player(230, 323, 2, 1, atlas, pause);
        secondPlayer = new SecondPlayer(130, 323, 2, 1, atlas);
        gameOver = new GameOver(atlas);
        level = new Level(atlas, gameOver);
        collision = new Collision(level);
        enemyStrategy = new EnemyStrategy(atlas, collision, player, secondPlayer, level);
        collision.setEnemyStrategy(enemyStrategy);
        choiced = false;
        escAllow = true;
        respawnPlayerTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setX(415);
                player.setY(540);
                player.setHeading(0);
                enemyStrategy.updatePlayer(player);
                player.setDestroyed(false);
                respawnPlayerTimer.stop();
            }
        });
        escAllowTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escAllow = true;
                escAllowTimer.stop();
            }
        });
        backToMenuTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToMenu();
                backToMenuTimer.stop();
            }
        });
    }

}
