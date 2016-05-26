package Game;

import Display.Display;
import Game.Enemy.EnemyStrategy;
import Game.Enemy.EnemyTank;
import Game.Level.Level;
import Game.Menu.Menu;
import IO.Input;
import utils.Time;
import Graphics.TextureAtlas;

import java.awt.*;

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
    private Collision collision;
    private static int menuChoice;
    private static boolean choiced;
    private EnemyStrategy enemyStrategy;


    public Game() {

        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        menu = new Menu(MENU_FILE_NAME);
        player = new Player(230, 323, 2, 2, atlas);
        secondPlayer = new SecondPlayer(130, 323, 2, 2, atlas);
        level = new Level(atlas);
        collision = new Collision(level.getTilesCoords(), level);
        enemyStrategy = new EnemyStrategy(atlas, collision, player, secondPlayer);
        collision.setEnemyStrategy(enemyStrategy);
        enemyStrategy.addEnemyTank(60, 60);
        enemyStrategy.addEnemyTank(500, 40);
        enemyStrategy.addEnemyTank(180, 60);
        enemyStrategy.addEnemyTank(270, 60);
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
            if (menuChoice == 0) {
                player.update(input, collision, player);
                enemyStrategy.update();
                level.update();
            } else if (menuChoice == 1) {
                player.update(input, collision, player);
                secondPlayer.updateSecondPlayer(input, collision, secondPlayer);
                enemyStrategy.update();
                level.update();
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
}
