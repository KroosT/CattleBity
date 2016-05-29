package Game.Level;

public enum TileType {

    EMPTY(0),
    BRICK(1),
    WATER(2),
    ICE(3),
    GRASS(4),
    METAL(5),
    EAGLE(6),
    INFO(7),
    STAGE(8),
    ONE(9),
    TWO(10),
    THREE(11),
    FOUR(12),
    FIVE(13),
    SIX(14),
    SEVEN(15),
    EIGHT(16),
    NINE(17);


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
            case 7:
                return INFO;
            case 8:
                return STAGE;
            case 9:
                return ONE;
            case 10:
                return TWO;
            case 11:
                return THREE;
            case 12:
                return FOUR;
            case 13:
                return FIVE;
            case 14:
                return SIX;
            case 15:
                return SEVEN;
            case 16:
                return EIGHT;
            case 17:
                return NINE;
            default:
                return EMPTY;
        }
    }

}
