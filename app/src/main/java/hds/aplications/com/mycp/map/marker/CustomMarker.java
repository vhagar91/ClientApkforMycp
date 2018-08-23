package hds.aplications.com.mycp.map.marker;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


public class CustomMarker extends Marker {

    public final CustomMarkerModel model;
    public Resources resources;
    protected CustomMarker.OnMarkerLongPressListener mOnMarkerLongPressListener;

    public CustomMarker(@NonNull CustomMarkerModel model, @NonNull MapView mapView, @NonNull Resources resources, @NonNull ResourceProxy resourceProxy, @NonNull OnMarkerClickListener listener, @NonNull OnMarkerLongPressListener listenerLongPress) {
        super(mapView, resourceProxy);
        this.model = model;
        this.resources = resources;
        setIcon(resources.getDrawable(model.getDrawableRes()));
        setPosition(new GeoPoint(model.getLocation()));
        setOnMarkerClickListener(listener);
        setmOnMarkerLongPressListener(listenerLongPress);
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM);
    }

    @Override
    public void showInfoWindow() {
        if(this.mInfoWindow != null) {
            boolean markerWidth = false;
            boolean markerHeight = false;
            int markerWidth1 = this.mIcon.getIntrinsicWidth();
            int markerHeight1 = this.mIcon.getIntrinsicHeight();
            int offsetX = (int)(this.mIWAnchorU * (float)markerWidth1) - (int)(this.mAnchorU * (float)markerWidth1);
            int offsetY = (int)(this.mIWAnchorV * (float)markerHeight1) - (int)(this.mAnchorV * (float)markerHeight1);
            this.mInfoWindow.open(this, this.mPosition, offsetX, offsetY + 25);
        }
    }

    @Override
    public boolean onLongPress(MotionEvent event, MapView mapView) {
        boolean result = super.onLongPress(event, mapView);
        if (result)
            getmOnMarkerLongPressListener().onLongPress(event, this, mapView);
        return result;
    }

    public OnMarkerLongPressListener getmOnMarkerLongPressListener() {
        return mOnMarkerLongPressListener;
    }

    public void setmOnMarkerLongPressListener(OnMarkerLongPressListener mOnMarkerLongPressListener) {
        this.mOnMarkerLongPressListener = mOnMarkerLongPressListener;
    }

    public interface OnMarkerLongPressListener {
        boolean onLongPress(MotionEvent event, Marker marker, MapView mapView);
    }

    public MarkerInfoWindow getmInfoWindow(){
        return this.mInfoWindow;
    }
}
