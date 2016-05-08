package Game;

import Display.Display;
import IO.Input;
import utils.Time;
import Graphics.TextureAtlas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Game implements Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "CattleBity";
    private static final int CLEAR_COLOR = 0xff0ff00f;
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

    //temp
    private float x = 350;
    private float y = 250;
    private float x1 = 350;
    private float y1 = 250;
    private float radius = 50;
    private float speed = 3;
    //end

    public Game() {

        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);

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

        if (input.getKey(KeyEvent.VK_UP)) {
            if (y <= 0) {
                return;
            } else {
                y -= speed;
            }
        }
        if (input.getKey(KeyEvent.VK_DOWN)) {
            if (y + radius * 2 >= HEIGHT) {
                return;
            } else {
                y += speed;
            }
        }
        if (input.getKey(KeyEvent.VK_LEFT)) {
            if (x <= 0) {
                return;
            } else {
                x -= speed;
            }
        }
        if (input.getKey(KeyEvent.VK_RIGHT)) {
            if (x + radius * 2 >= WIDTH) {
                return;
            } else {
                x += speed;
            }
        }
        if (input.getKey(KeyEvent.VK_W)) {
            y1 -= speed;
        }
        if (input.getKey(KeyEvent.VK_S)) {
            y1 += speed;
        }
        if (input.getKey(KeyEvent.VK_A)) {
            x1 -= speed;
        }
        if (input.getKey(KeyEvent.VK_D)) {
            x1 += speed;
        }

    }

    private void render() {
        Display.clear();
        graphics.setColor(Color.WHITE);
        graphics.drawImage(atlas.Cut(0, 0, 32, 32), 300, 300, null);
        //graphics.fillOval((int)x, (int)y, (int)(radius * 2), (int)(radius * 2));
        //graphics.setColor(Color.BLUE);
        //graphics.fillOval((int)x1, (int)y1, (int)(radius * 2), (int)(radius * 2));
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
                Display.setTitle(TITLE + " | " + " Fps: " + fps + " | " + " Upd: " + upd + " Updl: " + updl);
                fps = 0;
                upd = 0;
                updl = 0;
                count = 0;
            }
        }

    }
}
