package hds.aplications.com.mycp.map;


public abstract class MapsConfig {

    private MapsConfig() {
    }

    public static final int TILE_SIZE = 256;
    public static final float SCREEN_RATION = 1.0f;
    public static final float OVERDRAW = 1.5f;
    public static final int GRID_SIZE = 100;
    public static final String TILE_CACHE_ID = "mapcache";

}
