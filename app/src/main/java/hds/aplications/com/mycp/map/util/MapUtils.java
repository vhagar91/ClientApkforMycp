package hds.aplications.com.mycp.map.util;

import android.location.Location;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

public class MapUtils {

    public static boolean containedInBox(BoundingBoxE6 boundingBoxE6, Location location){
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

        if(geoPoint.getLatitudeE6() > boundingBoxE6.getLatNorthE6() || geoPoint.getLatitudeE6() < boundingBoxE6.getLatSouthE6() || geoPoint.getLongitudeE6() > boundingBoxE6.getLonEastE6() || geoPoint.getLongitudeE6() < boundingBoxE6.getLonWestE6()){
            return false;
        }

        return true;
    }
}
