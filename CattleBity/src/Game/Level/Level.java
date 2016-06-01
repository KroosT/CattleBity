package Game.Level;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import Game.Player;
import Graphics.TextureAtlas;

import javax.swing.Timer;
import utils.Utils;
import Game.GameOver;

public class Level {

    private static final int TILE_SCALE = 8;
    private static final int EAGLE_SCALE = 16;
    private static final int TILE_IN_GAME_SCALE = 2;
    private static final int SCALED_TILE_SIZE = TILE_IN_GAME_SCALE * TILE_SCALE;
    private Integer[][] tileMap;
    private Integer[][] tileMapTemp;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCoords;
    private List<Point> tilesCoords;
    private List<Point> waterCoords;
    private List<Point> iceCoords;
    private List<Point> emptyCoords;
    private List<Point> metalCoords;
    private List<Point> infoCoords;
    private List<Point> eagleCoords;
    private BufferedImage tanksLeft;
    private BufferedImage playerLives;
    private List<Point> tanksLeftCoords;
    private boolean stageChanging;
    private GameOver gameOver;
    private int currStage;
    private Timer timerChangeStage;
    private TextureAtlas atlas;
    private Player player;
    private BufferedImage one, two, three;

    public Level(TextureAtlas atlas, GameOver gameOver) {
        this.gameOver = gameOver;
        this.currStage = 0;
        this.atlas = atlas;
        this.tiles = new HashMap<>();
        this.tiles.put(TileType.BRICK, new Tile(atlas.Cut(32 * TILE_SCALE, 0, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
                TileType.BRICK));
        this.tiles.put(TileType.WATER, new Tile(atlas.Cut(32 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.WATER));
        this.tiles.put(TileType.ICE, new Tile(atlas.Cut(36 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.ICE));
        this.tiles.put(TileType.GRASS, new Tile(atlas.Cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.GRASS));
        this.tiles.put(TileType.METAL, new Tile(atlas.Cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.METAL));
        this.tiles.put(TileType.EAGLE, new Tile(atlas.Cut(304, 4 * TILE_SCALE, EAGLE_SCALE, EAGLE_SCALE), 3, TileType.EAGLE));
        this.tiles.put(TileType.EMPTY, new Tile(atlas.Cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.EMPTY));
        this.tiles.put(TileType.INFO, new Tile(atlas.Cut(368, 214, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.INFO));
        this.tiles.put(TileType.STAGE, new Tile(atlas.Cut(328, 176, 5 * TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.STAGE));
        this.tiles.put(TileType.ONE, new Tile(atlas.Cut(336, 184, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.ONE));
        this.tiles.put(TileType.TWO, new Tile(atlas.Cut(344, 184, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.TWO));
        this.tiles.put(TileType.THREE, new Tile(atlas.Cut(352, 184, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.THREE));

        grassCoords = new ArrayList<>();
        tilesCoords = new ArrayList<>();
        waterCoords = new ArrayList<>();
        iceCoords = new ArrayList<>();
        emptyCoords = new ArrayList<>();
        metalCoords = new ArrayList<>();
        infoCoords = new ArrayList<>();
        eagleCoords = new ArrayList<>();
        tanksLeftCoords = new ArrayList<>();
        one = Utils.resize(atlas.Cut(337, 184, 8, 8), 16, 16);
        two = Utils.resize(atlas.Cut(345, 184, 8, 8), 16, 16);
        three = Utils.resize(atlas.Cut(353, 184, 8, 8), 16, 16);
        timerChangeStage = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (currStage + 1) {
                    case 1:
                        tileMap = Utils.LevelLoader("CattleBity\\res\\level.lvl");
                        break;
                    case 2:
                        tileMap = Utils.LevelLoader("CattleBity\\res\\level2.lvl");
                        break;
                    case 3:
                        tileMap = Utils.LevelLoader("CattleBity\\res\\level3.lvl");
                        break;
                }
                loadLevel();
                stageChanging = false;
                timerChangeStage.stop();
                currStage++;
            }
        });
        changeLevel();
    }

    private void loadLevel() {
        int x = 16;
        int y = 16;
        tanksLeftCoords.clear();
        for (int i = 0; i < 20; i++) {
            tanksLeftCoords.add(new Point(736 + x, 32 + y));
            if (i % 2 == 0) {
                x += 16;
            } else {
                x -= 16;
                y += 16;
            }
        }
        tanksLeft = Utils.resize(atlas.Cut(320, 193, 8, 8), 16, 16);
        playerLives = Utils.resize(atlas.Cut(376, 134, 19, 19), 38, 38);
        eagleCoords.clear();
        grassCoords.clear();
        waterCoords.clear();
        iceCoords.clear();
        emptyCoords.clear();
        metalCoords.clear();
        infoCoords.clear();
        tilesCoords.clear();
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = this.tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if (tile.getType() == TileType.EAGLE)
                    eagleCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.GRASS)
                    grassCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.WATER)
                    waterCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.ICE)
                    iceCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.EMPTY)
                    emptyCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.METAL)
                    metalCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() == TileType.INFO)
                    infoCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else
                    tilesCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
            }
        }
    }

    public List<Point> getTilesCoords() {
        return tilesCoords;
    }

    public List<Point> getEagleCoords() {
        return eagleCoords;
    }

    public List<Point> getWaterCoords() {
        return waterCoords;
    }

    public List<Point> getIceCoords() {
        return iceCoords;
    }

    public List<Point> getEmptyCoords() {
        return emptyCoords;
    }

    public List<Point> getMetalCoords() {
        return metalCoords;
    }

    public List<Point> getInfoCoords() {
        return infoCoords;
    }

    public void destroyTile(int x, int y) {
        tileMap[x][y] = 0;
    }

    public void removeCoords(Point p) {
        tilesCoords.remove(p);
    }

    public void removeEagleCoords(Point p) {
        eagleCoords.remove(p);
    }

    public void removeTankFromTanksLeft() {
        tanksLeftCoords.remove(tanksLeftCoords.size() - 1);
    }

    public void changeLevel() {
        if (currStage != 0) {
            player.setLives(3);
        }
        if (currStage + 1 == 4) {
            gameOver.setWin(true);
        } else {
            tileMapTemp = Utils.LevelLoader("CattleBity\\res\\changeLevel.lvl");
            switch (currStage + 1) {
                case 1:
                    tileMapTemp[18][27] = 9;
                    break;
                case 2:
                    tileMapTemp[18][27] = 10;
                    break;
                case 3:
                    tileMapTemp[18][27] = 11;
                    break;
            }
            tileMap = tileMapTemp;
            loadLevel();
            stageChanging = true;
            timerChangeStage.start();
        }
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }

    public void setTileMapPlain() {
        tileMap = Utils.LevelLoader("CattleBity\\res\\plain.lvl");
    }

    public void update(GameOver gameOver) {
        if (eagleCoords.size() == 0 && !stageChanging) {
            gameOver.setGameOver(true);
        }
    }

    public void render(Graphics2D g) {
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if (tile.getType() != TileType.GRASS) {
                    tile.render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);
                }
            }
        }
        if (!gameOver.getGameOver() && !stageChanging) {
            for (Point coords : tanksLeftCoords) {
                g.drawImage(tanksLeft, coords.x, coords.y, null);
            }
            g.drawImage(playerLives, 750, 400, null);
            if (player.getLives() == 3) {
                g.drawImage(three, 766, 420, null);
            } else if (player.getLives() == 2) {
                g.drawImage(two, 766, 420, null);
            } else if (player.getLives() == 1) {
                g.drawImage(one, 766, 420, null);
            }
        }
    }

    public void renderGrass(Graphics2D g) {
        for (Point p : grassCoords) {
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
        }
    }

    public boolean getStageChanging() {
        return stageChanging;
    }
}
