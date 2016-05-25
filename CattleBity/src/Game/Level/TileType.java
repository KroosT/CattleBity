package Game.Level;

public enum TileType {

    EMPTY(0),
    BRICK(1),
    WATER(2),
    ICE(3),
    GRASS(4),
    METAL(5),
    EAGLE(6);


    private int n;

    TileType(int n) {
        this.n = n;
    }

    public int numeric() {
        return n;
    }

    public static TileType fromNumeric(int n) {
        switch (n) {
            case 1:
                return BRICK;
            case 2:
                return WATER;
            case 3:
                return ICE;
            case 4:
                return GRASS;
            case 5:
                return METAL;
            case 6:
                return EAGLE;
            default:
                return EMPTY;
        }
    }

}
