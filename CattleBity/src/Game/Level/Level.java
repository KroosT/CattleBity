package Game.Level;

import Game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import Graphics.TextureAtlas;
import utils.Utils;
import Game.GameOver;

public class Level {

    private static final int TILE_SCALE = 8;
    private static final int EAGLE_SCALE = 16;
    private static final int TILE_IN_GAME_SCALE = 2;
    private static final int SCALED_TILE_SIZE = TILE_IN_GAME_SCALE * TILE_SCALE;
    public static final int TILES_IN_WIDTH = Game.WIDTH / SCALED_TILE_SIZE;
    public static final int TILES_IN_HEIGHT = Game.HEIGHT / SCALED_TILE_SIZE;
    private Integer[][] tileMap;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCoords;
    private List<Point> tilesCoords;
    private List<Point> waterCoords;
    private List<Point> iceCoords;
    private List<Point> emptyCoords;
    private List<Point> metalCoords;
    private List<Point> infoCoords;
    private List<Point> eagleCoords;
    private boolean winState;

    public Level(TextureAtlas atlas) {
        winState = false;
        tiles = new HashMap<>();
        tiles.put(TileType.BRICK, new Tile(atlas.Cut(32 * TILE_SCALE, 0, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE,
                TileType.BRICK));
        tiles.put(TileType.WATER, new Tile(atlas.Cut(32 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.WATER));
        tiles.put(TileType.ICE, new Tile(atlas.Cut(36 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.ICE));
        tiles.put(TileType.GRASS, new Tile(atlas.Cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.GRASS));
        tiles.put(TileType.METAL, new Tile(atlas.Cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.METAL));
        tiles.put(TileType.EAGLE, new Tile(atlas.Cut(304, 4 * TILE_SCALE, EAGLE_SCALE, EAGLE_SCALE), 3, TileType.EAGLE));
        tiles.put(TileType.EMPTY, new Tile(atlas.Cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.EMPTY));
        tiles.put(TileType.INFO, new Tile(atlas.Cut(368, 214, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.INFO));
        tiles.put(TileType.STAGE, new Tile(atlas.Cut(328, 176, 5 * TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.STAGE));
        tiles.put(TileType.ONE, new Tile(atlas.Cut(336, 184, TILE_SCALE, TILE_SCALE), TILE_IN_GAME_SCALE, TileType.ONE));
        tileMap = Utils.LevelLoader("CattleBity\\res\\level.lvl");
        grassCoords = new ArrayList<>();
        tilesCoords = new ArrayList<>();
        waterCoords = new ArrayList<>();
        iceCoords = new ArrayList<>();
        emptyCoords = new ArrayList<>();
        metalCoords = new ArrayList<>();
        infoCoords = new ArrayList<>();
        eagleCoords = new ArrayList<>();
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
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

    public boolean getWinState() {
        return winState;
    }

    public void setWinState(boolean winState) {
        this.winState = winState;
    }

    public void changeLevel() {

    }

    public void setTileMapPlain() {
        tileMap = Utils.LevelLoader("CattleBity\\res\\plain.lvl");
    }

    public void update(GameOver gameOver) {
        if (eagleCoords.size() == 0) {
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
    }

    public void renderGrass(Graphics2D g) {
        for (Point p : grassCoords) {
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
        }
    }
}
