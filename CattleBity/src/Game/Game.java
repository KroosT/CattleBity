package Game;

import Display.Display;
import Game.Level.Level;
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

    private static final String ATLAS_FILE_NAME = "textureAtlas.png";

    private boolean running;
    private Thread gameThread;
    private Graphics2D graphics;
    private Input input;
    private TextureAtlas atlas;
    private Player player;
    private Level level;


    public Game() {

        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        player = new Player(300, 300, 2, 2, atlas);
        level = new Level(atlas);

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
        player.update(input, level);
        level.update();
    }

    private void render() {
        Display.clear();
        level.render(graphics);
        player.render(graphics);
        level.renderGrass(graphics);
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
}
