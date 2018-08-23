package hds.aplications.com.mycp.map.view;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.IMapTileProviderCallback;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import hds.aplications.com.mycp.map.MapsConfig;


public class MapViewHds extends MapView {

    public MapViewHds(@NonNull Context context,
                      @NonNull TileCache tileCache,
                      @NonNull String offlineMapFilePath) {
        super(context, MapsConfig.TILE_SIZE, new DefaultResourceProxyImpl(context), new MapsforgeTileProvider(context, tileCache, offlineMapFilePath), null);
    }

    public void setCenter(@NonNull Location location) {
        setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
    }

    public void setCenter(@NonNull GeoPoint geoPoint) {
        final GeoPoint aGeoPoint = geoPoint;
        post(new Runnable() {
            @Override
            public void run() {
                IMapController controller = getController();
                controller.animateTo(aGeoPoint);
            }
        });
    }

    static private class MapsforgeTileProvider extends MapTileProviderArray implements IMapTileProviderCallback {
        private MapsforgeTileProvider(@NonNull Context context,
                                      @NonNull TileCache tileCache,
                                      @NonNull String offlineMapFilePath) {
            super(TileSourceFactory.getTileSource("MapquestOSM"), new SimpleRegisterReceiver(context));
            final MapsforgeOSMDroidTileProviderBase mapsforgeProvider = new MapsforgeOSMDroidTileProviderBase();
            final MapsforgeOSMTileSource tileSourceMf = new MapsforgeOSMTileSource(context, "mapsforge", offlineMapFilePath, tileCache);
            mapsforgeProvider.setTileSource(tileSourceMf);
            mTileProviderList.add(mapsforgeProvider);
        }
    }

}
