package hds.aplications.com.mycp.map.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;


public class MapOverlayToPosition extends Overlay {
    private MapFragment mapFragment;

    public MapOverlayToPosition(Context ctx, MapFragment mapFragment) {
        super(ctx);
        this.mapFragment = mapFragment;
    }

    @Override
    protected void draw(Canvas c, MapView osmv, boolean shadow) {
           /* actionUp = true;
            savemLastUserPosition = null;*/
    }

    @Override
    public boolean onLongPress(MotionEvent event, MapView mapView) {
        final Projection pj = mapView.getProjection();
        final GeoPoint eventPosition = (GeoPoint) pj.fromPixels((int)event.getX(), (int)event.getY());

        Location l = new Location("");
        l.setLongitude(eventPosition.getLongitude());
        l.setLatitude(eventPosition.getLatitude());

        if(this.mapFragment.getOriginPosition() != null && this.mapFragment.getUserPosition() != null && this.mapFragment.getOriginPosition() == this.mapFragment.getUserPosition()){
            this.mapFragment.setOriginPosition(l);
        }

        this.mapFragment.updateUserPosition(l, true, true);

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        if (!this.mapFragment.isEnable()){
            return true;
        }

        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                this.mapFragment.getMenuWindow().close();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event, mapView);
    }
}
