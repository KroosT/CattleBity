package Game.Level;

import Game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import Graphics.TextureAtlas;
import utils.Utils;

public class Level {

    private static final int TILE_SCALE = 8;
    private static final int TILE_IN_GAME_SCALE = 2;
    private static final int SCALED_TILE_SIZE = TILE_IN_GAME_SCALE * TILE_SCALE;
    public static final int TILES_IN_WIDTH = Game.WIDTH / SCALED_TILE_SIZE;
    public static final int TILES_IN_HEIGHT = Game.HEIGHT / SCALED_TILE_SIZE;
    private Integer[][] tileMap;
    private Map<TileType, Tile> tiles;
    private List<Point> grassCoords;
    private List<Point> tilesCoords;

    public Level(TextureAtlas atlas) {
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
        tiles.put(TileType.EMPTY, new Tile(atlas.Cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE),
                TILE_IN_GAME_SCALE, TileType.EMPTY));
        tileMap = Utils.LevelLoader("CattleBity\\res\\level.lvl");
        grassCoords = new ArrayList<>();
        tilesCoords = new ArrayList<>();
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if (tile.getType() == TileType.GRASS)
                    grassCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
                else if (tile.getType() != TileType.EMPTY)
                    tilesCoords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
            }
        }
    }

    public List<Point> getTilesCoords() {
        return tilesCoords;
    }

    public void update() {

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
