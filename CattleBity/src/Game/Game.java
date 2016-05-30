package Game;

import Display.Display;
import Game.Enemy.EnemyStrategy;
import Game.Level.Level;
import Game.Menu.Menu;
import IO.Input;
import utils.Time;
import Graphics.TextureAtlas;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

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
    private AudioClip constSound;
    private static boolean constSoundIsPlaying;
    private static boolean permanent;
    private File constSoundFile = new File("CattleBity\\res\\Sounds\\const.wav");


    public Game() {

        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        menu = new Menu(MENU_FILE_NAME);
        player = new Player(230, 323, 2, 1, atlas);
        secondPlayer = new SecondPlayer(130, 323, 2, 1, atlas);
        gameOver = new GameOver(atlas);
        level = new Level(atlas, gameOver);
        collision = new Collision(level.getTilesCoords(), level);
        enemyStrategy = new EnemyStrategy(atlas, collision, player, secondPlayer, level);
        collision.setEnemyStrategy(enemyStrategy);
        choiced = false;

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
        if (!choiced){
            menu.setSecondPlayer(secondPlayer);
            menu.update(input, collision, player);
        }
        else {
            if (gameOver.getGameOver()) {
                level.setTileMapPlain();
            } else {
                if (menuChoice == 0) {
                    player.update(input, collision, player);
                    enemyStrategy.update();
                    level.update(gameOver);
                } else if (menuChoice == 1) {
                    player.update(input, collision, player);
                    secondPlayer.updateSecondPlayer(input, collision, secondPlayer);
                    enemyStrategy.update();
                    level.update(gameOver);
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
            if (gameOver.getGameOver() || enemyStrategy.getChangeLevel()) {
                level.render(graphics);
                gameOver.render(graphics);
            } else {
                if (menuChoice == 0) {
                    level.render(graphics);
                    player.render(graphics);
                    enemyStrategy.render(graphics);
                    level.renderGrass(graphics);
                } else if (menuChoice == 1) {
                    level.render(graphics);
                    player.render(graphics);
                    secondPlayer.render(graphics);
                    enemyStrategy.render(graphics);
                    level.renderGrass(graphics);
                }
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

    public static void setConstSoundIsPlaying(boolean state) {
        constSoundIsPlaying = state;
        permanent = state;
    }

    public static void setPermanent(boolean state) {
        permanent = state;
    }
}
