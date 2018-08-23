package hds.aplications.com.mycp.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.rule.RenderThemeFuture;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import hds.aplications.com.mycp.map.MapsConfig;
import mgleon.common.com.LogUtils;



public class MapsforgeOSMTileSource implements ITileSource {
    private final Context context;

    private static final float DEFAULT_TEXT_SCALE = 1;
    private static final boolean IS_TRANSPARENT = false;
    private static final String TAG = LogUtils.makeLogTag(MapsforgeOSMTileSource.class);

    private final String name;
    private final String mapFile;
    private final MapFile mapDatabase;

    private final DatabaseRenderer mapGenerator;
    private final XmlRenderTheme xmlRenderTheme;
    private RenderThemeFuture renderThemeFuture;
    private final DisplayModel displayModel;
    private final TileCache tileCache;
    //private final WeakHashMap<Job, Drawable> tileWeakCache;

    private final BoundingBox mapBounds;

    private int zoomBounds;
    private int westTileBounds;
    private int eastTileBounds;
    private int southTileBounds;
    private int northTileBounds;

    public MapsforgeOSMTileSource(@NonNull Context context, @NonNull String name, @NonNull String mapFile, @NonNull TileCache cache) {XmlRenderTheme xmlRenderTheme1;
        this.context = context;
        this.name = name;
        this.mapFile = mapFile;
        this.tileCache = cache;
        //this.tileWeakCache = new WeakHashMap<>();

        mapDatabase = new MapFile(new File(mapFile));
        mapGenerator = new DatabaseRenderer(mapDatabase, AndroidGraphicFactory.INSTANCE, this.tileCache);

        //xmlRenderTheme = new RenderTheme();
        /**/
        try
        {
            //AndroidSvgBitmapStore.clear();
            xmlRenderTheme1 = new AssetsRenderTheme(context, "", "osmarender/osmarender.xml");
            //new ExternalRenderTheme(new File("osmarender/osmarender.xml")); new AssetsRenderTheme(context, "", "osmarender/osmarender.xml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            xmlRenderTheme1 = new RenderTheme();
        }
        xmlRenderTheme = xmlRenderTheme1;
        /**/

        displayModel = new Model().displayModel;
        renderThemeFuture = new RenderThemeFuture(AndroidGraphicFactory.INSTANCE, xmlRenderTheme, displayModel);
        new Thread(renderThemeFuture).run();

        /*
        mapDatabase.closeFile();
        mapDatabase.openFile(new File(mapFile));
        */

        mapBounds = mapDatabase.getMapFileInfo().boundingBox;
        zoomBounds = -1;
    }

    @Override
    public String localizedName(@Nullable ResourceProxy proxy) {
        return name();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return name.hashCode();
    }

    @Override
    public int getTileSizePixels() {
        return MapsConfig.TILE_SIZE;
    }

    @Override
    public int getMaximumZoomLevel() {
        return mapGenerator.getZoomLevelMax();
    }

    @Override
    public int getMinimumZoomLevel() {
        return 6;
    }

    public synchronized Drawable getDrawable(int tileX, int tileY, int zoom) throws LowMemoryException {
        if (tileOutOfBounds(tileX, tileY, zoom)) return null;
        Tile tile = new Tile(tileX, tileY, (byte) zoom, MapsConfig.TILE_SIZE);

        try {
            RendererJob job = new RendererJob(tile, mapDatabase, renderThemeFuture, displayModel, DEFAULT_TEXT_SCALE, IS_TRANSPARENT, false);
            //Drawable rvalue = tileWeakCache.get(job);
            Drawable rvalue = null;

            if ( rvalue == null ) {
                try {
                    TileBitmap bitmap = mapGenerator.executeJob(job);
                    Bitmap tileBitmap = AndroidGraphicFactory.getBitmap(bitmap);
                    rvalue = new ExpirableBitmapDrawable(tileBitmap);
                    //tileWeakCache.put(job, rvalue);
                } catch (OutOfMemoryError e) {
                    LogUtils.LOGE(TAG, "OutOfMemory error during tile creating", e);
                    System.gc();
                }
            }
            return rvalue;
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "Cannot create tile! tileX: " + tileX + "; tileY: " + tileY + "; zoom: " + zoom, e);
            //tileBitmap = Bitmap.createBitmap(MapsConfig.TILE_SIZE, MapsConfig.TILE_SIZE, Bitmap.Config.RGB_565);
            //tileBitmap.eraseColor(Color.YELLOW);
            return null;
        }
        //return new BitmapDrawable(context.getResources(), tileBitmap);
        //return new ExpirableBitmapDrawable(tileBitmap);
    }

    private boolean tileOutOfBounds(int tileX, int tileY, int zoom) {
        if (zoom != zoomBounds) recalculateTileBounds(zoom);

        return (tileX < westTileBounds) || (tileX > eastTileBounds) ||
                (tileY < northTileBounds) || (tileY > southTileBounds);
    }

    /* convert lon/lat to tile x,y from http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames */
    private void recalculateTileBounds(final int zoom) {
        zoomBounds = zoom;
        westTileBounds = lon2XTile(mapBounds.minLongitude, zoomBounds);
        eastTileBounds = lon2XTile(mapBounds.maxLongitude, zoomBounds);
        southTileBounds = lat2YTile(mapBounds.minLatitude, zoomBounds);
        northTileBounds = lat2YTile(mapBounds.maxLatitude, zoomBounds);
    }

    @Override
    public Drawable getDrawable(String arg0) throws LowMemoryException {
        return null;
    }

    @Override
    public Drawable getDrawable(InputStream arg0) throws LowMemoryException {
        return null;
    }

    @Override
    public String getTileRelativeFilenameString(final MapTile tile) {
        return null;
    }

    static private int lon2XTile(final double lon, final int zoom) {
        return (int) Math.floor((lon + 180) / 360 * (1 << zoom));
    }

    static private int lat2YTile(final double lat, final int zoom) {
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
    }
}