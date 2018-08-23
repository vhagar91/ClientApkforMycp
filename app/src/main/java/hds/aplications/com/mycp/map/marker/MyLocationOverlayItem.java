package hds.aplications.com.mycp.map.marker;

import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;


public class MyLocationOverlayItem extends OverlayItem {

    public MyLocationOverlayItem(@NonNull GeoPoint aGeoPoint, @NonNull Resources resources, String titleResourceId, @DrawableRes int drawableResourceId) {
        super(titleResourceId, null, aGeoPoint);
        setMarker(resources.getDrawable(drawableResourceId));
        setMarkerHotspot(HotspotPlace.BOTTOM_CENTER);
    }

}
