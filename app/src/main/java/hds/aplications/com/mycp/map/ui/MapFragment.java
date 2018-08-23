package hds.aplications.com.mycp.map.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.graphhopper.routing.util.EncodingManager;

import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.map.MapsConfig;
import hds.aplications.com.mycp.map.downloader.AbstractMap;
import hds.aplications.com.mycp.map.marker.CustomMarker;
import hds.aplications.com.mycp.map.marker.CustomMarkerMenuWindow;
import hds.aplications.com.mycp.map.marker.CustomMarkerModel;
import hds.aplications.com.mycp.map.marker.MyLocationOverlayItem;
import hds.aplications.com.mycp.map.routing.InstructionInfo;
import hds.aplications.com.mycp.map.routing.RouteLoader;
import hds.aplications.com.mycp.map.util.MapUtils;
import hds.aplications.com.mycp.map.view.MapViewHds;
import hds.aplications.com.mycp.models.Accommodation;
import mgleon.common.com.LogUtils;


//import pl.droidsonroids.gif.GifImageView;


public class MapFragment extends Fragment {

    /*ATRIBUTES*/

    private static final String TAG = LogUtils.makeLogTag(MapFragment.class);

    private interface SavedState {
        String ZOOM_LVL = "zoom_lvl";
        String LATI = "lati";
        String LONGI = "longi";
        String LOCATION = "location";
        String TARGET = "target";
        String ROUTE = "route";
    }

    private TileCache tileCache;
    private MapViewHds mMapView;
    private DefaultResourceProxyImpl mDefaultResourceProxy;

    CustomMarker markerUserPosition = null;

    private MyLocationOverlayItem originMarker;
    private MyLocationOverlayItem destinationMarker;

    private Location userPosition;
    private Location originPosition;

    ItemizedIconOverlay<MyLocationOverlayItem> mMyLocationOverlayItem;

    private CustomMarkerModel mDestinationPosition;
    private PathOverlay mPathOverlay;
    private ArrayList<GeoPoint> mCurrentRouteGeoPoints;
    private double mCurrentRouteDistance;

    private LocationManager mLocationManager;
    private MyLocationListener mLocationListener;

    private View mOverviewLayout;
    private View mOverviewLayoutDistance;
    private View lastArrow = null;
    private View view;
    private View btnClear;

    private MarkerInfoWindow menuWindow;
    private MarkerInfoWindow actualMarkerInfoWindow = null;

    private String vehicle = EncodingManager.CAR;
    private ImageButton btnCar;
    private ImageButton btnBike;
    private ImageButton btnFoot;
    private ImageButton mInstructionButton;

    private boolean enable = true;

    private Marker.OnMarkerClickListener mOnMarkerClickListener = new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            if (marker instanceof CustomMarker) {
                showInfoWindow(marker);
                final CustomMarker cMarker = (CustomMarker) marker;
                centerMapTo(cMarker.model.getLocation());
                return true;
            }
            return false;
        }
    };

    private CustomMarker.OnMarkerLongPressListener mOnMarkerLongPressListener = new CustomMarker.OnMarkerLongPressListener() {
        @Override
        public boolean onLongPress(MotionEvent event, Marker marker, MapView mapView) {
            return showMenuWindowInMarker(marker, true);
        }
    };

    private Marker.OnMarkerClickListener mOnMarkerUserPosClickListener = new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            return showMenuWindowInMarker(marker, true);
        }
    };

    private CustomMarkerMenuWindow.OnMenuSelectOptionListener onMenuSelectOptionListener = new CustomMarkerMenuWindow.OnMenuSelectOptionListener() {
        @Override
        public boolean onSelectOrigin(CustomMarker customMarker) {
            Location l = new Location("");
            l.setLatitude(customMarker.model.getLocation().getLatitude());
            l.setLongitude(customMarker.model.getLocation().getLongitude());
/*
            final Resources resources = mMapView.getContext().getResources();
            Marker startMarker = new Marker(mMapView);
            startMarker.setPosition(new GeoPoint(l.getLatitude(), l.getLongitude()));
            startMarker.setIcon(resources.getDrawable(R.mipmap.pin_a_red_));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            mMapView.getOverlays().add(startMarker);
            //mapOverlayToPosition.ad
*/
            originPosition = l;
            updateSelectedMarker(false);
            changeOriginOrDestination(originPosition, true);

            return true;
        }

        @Override
        public boolean onSelectDestination(CustomMarker customMarker) {
            mDestinationPosition = customMarker.model;
            updateSelectedMarker(false);
            changeOriginOrDestination(mDestinationPosition.getLocation(), false);
            return true;
        }
    };

    private final LoaderManager.LoaderCallbacks<RouteLoader.Result> mRouteLoadManager = new LoaderManager.LoaderCallbacks<RouteLoader.Result>() {
        @Override
        public Loader<RouteLoader.Result> onCreateLoader(int id, Bundle args) {
            return new RouteLoader(getActivity(), originPosition, mDestinationPosition.getLocation(), vehicle);
        }

        @Override
        public void onLoadFinished(Loader<RouteLoader.Result> loader, RouteLoader.Result data) {
            List<GeoPoint> pointsOfRute = data.getGeoPoints();
            if (pointsOfRute == null || pointsOfRute.size() == 0){
                Toast.makeText(getActivity(), mMapView.getContext().getString(R.string.no_rute), Toast.LENGTH_SHORT).show();
                pointsOfRute = new ArrayList<>();
            }
            mCurrentRouteGeoPoints = new ArrayList<>(pointsOfRute);
            mCurrentRouteDistance = data.getDistance();
            getLoaderManager().destroyLoader(R.id.loader_find_route);
            mMapView.post(new Runnable() {
                @Override
                public void run() {
                    changeStateBtnRoute(true);
                    mInstructionButton.setVisibility(View.VISIBLE);
                    updateCurrentRoute();
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<RouteLoader.Result> loader) {}
    };

    public MapFragmentListener mapFragmentListener;

    private MapOverlayToPosition mapOverlayToPosition;

    /*OVERRIDE METHODS*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rvalue = inflater.inflate(R.layout.fragment_map, container, false);

        tileCache = AndroidUtil.createTileCache(getActivity(), MapsConfig.TILE_CACHE_ID, MapsConfig.TILE_SIZE, MapsConfig.SCREEN_RATION, MapsConfig.OVERDRAW);
        final File mapFile = AbstractMap.instance().getMapsforgeFile(getActivity());
        mMapView = new MapViewHds(getActivity(), tileCache, mapFile.getAbsolutePath());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout forMap = (RelativeLayout) rvalue.findViewById(R.id.layout_map);
        forMap.addView(mMapView, 0, params);

        mMapView.setScrollableAreaLimit(AbstractMap.instance().getExtent());
        return rvalue;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        mMapView.setMultiTouchControls(true);
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setUseDataConnection(false);

        menuWindow = new CustomMarkerMenuWindow(R.layout.marker_menu_window, mMapView, onMenuSelectOptionListener);

        view.findViewById(R.id.img_btn_zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomIn();
            }
        });
        view.findViewById(R.id.img_btn_zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomOut();
            }
        });
        view.findViewById(R.id.btn_get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LOGD(TAG, "My location button clicked; mLocationManager is" + (mLocationManager == null ? " " : " NOT ") + "null");
                if (mLocationManager == null) initLocationManager();
            }
        });

        this.btnCar = (ImageButton)view.findViewById(R.id.fm_car);
        this.btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle = EncodingManager.CAR;
                calculateRoute();
            }
        });
        this.btnBike = (ImageButton)view.findViewById(R.id.fm_bike);
        this.btnBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle = EncodingManager.BIKE;
                calculateRoute();
            }
        });
        this.btnFoot = (ImageButton)view.findViewById(R.id.fm_foot);
        this.btnFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle = EncodingManager.FOOT;
                calculateRoute();
            }
        });

        this.btnClear = view.findViewById(R.id.fm_clear_distance);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDistance();
            }
        });
        btnClear.setVisibility(View.GONE);

        mOverviewLayout = view.findViewById(R.id.layout_route);
        mOverviewLayoutDistance = view.findViewById(R.id.fm_layout_distance);

        this.mInstructionButton = (ImageButton)view.findViewById(R.id.fm_route_instruction);
        this.mInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fmDistanceRoute = ((TextView)view.findViewById(R.id.fm_distance_route)).getText().toString();

                Bundle b = new Bundle();
                b.putString("fmDistanceRoute", fmDistanceRoute);

                mapFragmentListener.onInstructions(b);
            }
        });

        final GeoPoint initialCenter;
        if (savedInstanceState != null) {
            mMapView.getController().setZoom(savedInstanceState.getInt(SavedState.ZOOM_LVL));
            initialCenter = new GeoPoint(savedInstanceState.getDouble(SavedState.LATI), savedInstanceState.getDouble(SavedState.LONGI));
            originPosition = savedInstanceState.getParcelable(SavedState.LOCATION);
            mDestinationPosition = savedInstanceState.getParcelable(SavedState.TARGET);
            mCurrentRouteGeoPoints = savedInstanceState.getParcelableArrayList(SavedState.ROUTE);
            updateUserPosition(originPosition, false, false);
        } else {
            mMapView.getController().setZoom(AbstractMap.instance().getDefaultZoom());
            initialCenter = AbstractMap.instance().getCenterGeoPoint();

            /*originPosition = new Location("");
            originPosition.setLatitude(23.115102);
            originPosition.setLongitude(-82.370338);
            updateUserPosition(originPosition, false, false);*/

        }
        updateSelectedMarker(false);
        updateCurrentRoute();

        mMapView.getController().setCenter(initialCenter);
        mMapView.setCenter(initialCenter);

        mapOverlayToPosition = new MapOverlayToPosition(mMapView.getContext(), this);
        mMapView.getOverlays().add(mapOverlayToPosition);

/**/
        ItemizedIconOverlay.OnItemGestureListener<MyLocationOverlayItem> onUserItemGestureListener = new ItemizedIconOverlay.OnItemGestureListener<MyLocationOverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int i, MyLocationOverlayItem overlayItem) {
                Toast.makeText(getActivity(), overlayItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onItemLongPress(int i, MyLocationOverlayItem overlayItem) {
                return false;
            }
        };
        final List<MyLocationOverlayItem> mMyLocationOverlayItemArray = new ArrayList<>();
        mMyLocationOverlayItem = new ItemizedIconOverlay<>(mMyLocationOverlayItemArray, onUserItemGestureListener, getDefaultResourceProxyImpl());
        mMapView.getOverlays().add(mMyLocationOverlayItem);
/**/

        mMapView.getOverlays().add(AbstractMap.instance().createMarkersCluster(mMapView, getDefaultResourceProxyImpl(), mOnMarkerClickListener, mOnMarkerLongPressListener));

        mMapView.invalidate();

        if (this.mapFragmentListener != null){
            this.mapFragmentListener.onCreate();
        }

        ImageButton img = (ImageButton)view.findViewById(R.id.fm_route_instruction);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyLocationManager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.LOGD(TAG, "onSaveInstanceState; mMapView is" + (mMapView == null ? " " : " NOT ") + "null");
        if (mMapView != null) {
            final IGeoPoint mapCenter = mMapView.getMapCenter();
            outState.putDouble(SavedState.LATI, mapCenter.getLatitude());
            outState.putDouble(SavedState.LONGI, mapCenter.getLongitude());
            outState.putInt(SavedState.ZOOM_LVL, mMapView.getZoomLevel());
        }
        outState.putParcelable(SavedState.LOCATION, originPosition);
        outState.putParcelable(SavedState.TARGET, mDestinationPosition);
        outState.putParcelableArrayList(SavedState.ROUTE, mCurrentRouteGeoPoints);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tileCache != null) tileCache.destroy();
    }

    /*METHODS*/

    public boolean showMenuWindowInMarker(Marker marker, boolean centerMap){
        if (marker instanceof CustomMarker) {
            MarkerInfoWindow infoWindow = ((CustomMarker) marker).getmInfoWindow();

            marker.setInfoWindow(menuWindow);
            showInfoWindow(marker);

            marker.setInfoWindow(infoWindow);

            if (centerMap)centerMapTo(((CustomMarker) marker).model.getLocation());

            return true;
        } else {
            return false;
        }
    }

    public void showInfoWindow(Marker marker){
        if (actualMarkerInfoWindow != null){
            actualMarkerInfoWindow.close();
            actualMarkerInfoWindow = null;
        }

        marker.showInfoWindow();

        if (marker instanceof CustomMarker) {
            actualMarkerInfoWindow = ((CustomMarker) marker).getmInfoWindow();
        }
    }

    public void changeOriginOrDestination(Location location, boolean origin){
        removePathOverlay();

        mMapView.getOverlays().remove(mMyLocationOverlayItem);
        mMyLocationOverlayItem.removeAllItems();

        if (origin){
            originMarker = new MyLocationOverlayItem(new GeoPoint(location.getLatitude(), location.getLongitude()), getResources(), getContext().getString(R.string.start), R.mipmap.origin);
        }else {
            destinationMarker = new MyLocationOverlayItem(new GeoPoint(location.getLatitude(), location.getLongitude()), getResources(), getContext().getString(R.string.finishp), R.mipmap.destination);
        }

        if (originMarker != null)mMyLocationOverlayItem.addItem(originMarker);
        if (destinationMarker != null)mMyLocationOverlayItem.addItem(destinationMarker);

        btnClear.setVisibility(View.VISIBLE);
        mMapView.getOverlays().add(mMyLocationOverlayItem);
    }

    public void clearDistance(){
        mOverviewLayout.setVisibility(View.GONE);
        if (lastArrow != null) lastArrow.setVisibility(View.GONE);
        btnClear.setVisibility(View.GONE);
        originPosition = null;
        mDestinationPosition = null;

        mMapView.getOverlays().remove(mMyLocationOverlayItem);
        removePathOverlay();

        originMarker = null;
        destinationMarker = null;

        mMapView.setCenter(new GeoPoint(mMapView.getMapCenter().getLatitude(), mMapView.getMapCenter().getLongitude()));
    }

    public void removePathOverlay(){
        mOverviewLayoutDistance.setVisibility(View.GONE);
        mInstructionButton.setVisibility(View.GONE);

        if (mPathOverlay != null) {
            mMapView.getOverlays().remove(mPathOverlay);
        }
    }

    public void calculateRoute(){
        View xlastArrow;
        switch (vehicle){
            case EncodingManager.CAR:
                xlastArrow =  view.findViewById(R.id.arrow_fm_car);
                break;
            case EncodingManager.BIKE:
                xlastArrow =  view.findViewById(R.id.arrow_fm_bike);
                break;
            case EncodingManager.FOOT:
                xlastArrow =  view.findViewById(R.id.arrow_fm_foot);
                break;
            default:
                xlastArrow =  view.findViewById(R.id.arrow_fm_car);
        }

        if (lastArrow != null){
            lastArrow.setVisibility(View.INVISIBLE);
        }

        xlastArrow.setVisibility(View.VISIBLE);
        lastArrow = xlastArrow;

        if (mDestinationPosition != null && mDestinationPosition.getLocation() != null && originPosition != null) {
            getLoaderManager().initLoader(R.id.loader_find_route, null, mRouteLoadManager);
            changeStateBtnRoute(false);
            //mInstructionButton.setVisibility(View.GONE);
        }
    }

    public void changeStateBtnRoute(boolean enabled){
        this.btnCar.setEnabled(enabled);
        this.btnBike.setEnabled(enabled);
        this.btnFoot.setEnabled(enabled);
    }

    public CustomMarker addMarker(@NonNull Accommodation accommodation, @DrawableRes int drawableRes){
        CustomMarkerModel model = new CustomMarkerModel(accommodation, drawableRes);
        return AbstractMap.instance().addMarker(model, mMapView, getDefaultResourceProxyImpl(), mOnMarkerClickListener, mOnMarkerLongPressListener);
    }

    private DefaultResourceProxyImpl getDefaultResourceProxyImpl() {
        return mDefaultResourceProxy == null
                ? mDefaultResourceProxy = new DefaultResourceProxyImpl(getActivity())
                : mDefaultResourceProxy;
    }

    private void initLocationManager() {
        LogUtils.LOGD(TAG, "initLocationManager");
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setSpeedRequired(false);

        final String usedProvider = mLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(usedProvider, 0, 1, mLocationListener);
        LogUtils.LOGD(TAG, "Location manager will be use " + usedProvider + " as location provider");
    }

    private void destroyLocationManager() {
        LogUtils.LOGD(TAG, "destroyLocationManager");
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            mLocationManager = null;
        }
    }

    public void updateUserPosition(Location location, boolean moveToCenter, boolean showMenu) {
        if (isDetached()) return;

        if (markerUserPosition != null) {
            mMapView.getOverlays().remove(markerUserPosition);
        }
        if (location != null) {
            userPosition = location;

            final Resources resources = getResources();

            CustomMarkerModel model = new CustomMarkerModel("My position", R.mipmap.pin_a_red_, userPosition);
            markerUserPosition = new CustomMarker(model, mMapView, resources, getDefaultResourceProxyImpl(), mOnMarkerUserPosClickListener, mOnMarkerLongPressListener);

            mMapView.getOverlays().add(markerUserPosition);

            if(showMenu) {
                showMenuWindowInMarker(markerUserPosition, false);
            }
            if (moveToCenter){
                mMapView.setCenter(location);
            }
            else {
                mMapView.setCenter(new GeoPoint(mMapView.getMapCenter().getLatitude(), mMapView.getMapCenter().getLongitude()));
            }
        }
    }

    private void updateSelectedMarker(boolean moveToCenter) {
        /*if (mDestinationPosition != null && moveToCenter) {
            mMapView.setCenter(mDestinationPosition.getLocation());
        }*/

        if (mDestinationPosition != null && originPosition != null && mDestinationPosition.getLocation().getLatitude() != originPosition.getLatitude() && mDestinationPosition.getLocation().getLongitude() != originPosition.getLongitude()) {
            mOverviewLayout.setVisibility(View.VISIBLE);
            //mMarkerImageView.setImageDrawable(mDestinationPosition.getDrawable(getResources()));
            //mMarkerTextView.setText(mDestinationPosition.getTitle());
            updateDistanceToTarget();

            if (moveToCenter)
                mMapView.setCenter(mDestinationPosition.getLocation());
        } else {
            mOverviewLayout.setVisibility(View.GONE);
            mOverviewLayoutDistance.setVisibility(View.GONE);
            mInstructionButton.setVisibility(View.GONE);
        }
    }

    public void centerMapTo(@NonNull Location location){
        mMapView.setCenter(location);
    }

    public void centerToBox(ArrayList<GeoPoint> partialPolyLine){
        BoundingBoxE6 boundingBox = BoundingBoxE6.fromGeoPoints(partialPolyLine);
        if (partialPolyLine.size() > 0)
            mMapView.zoomToBoundingBox(boundingBox);
    }

    public void centerToExtent(){
       mMapView.zoomToBoundingBox(AbstractMap.instance().getExtent());
    }

    private void updateDistanceToTarget() {
       // if (originPosition != null && mDestinationPosition != null && mDestinationPosition.getLocation() != null) {
            //float distance = originPosition.distanceTo(mDestinationPosition.getLocation());
            //mMarkerDescriptionTextView.setText(String.format("Distance: %.2f km", distance / 1000));
       // }
    }

    private void updateCurrentRoute() {
        if (mPathOverlay != null) {
            mMapView.getOverlays().remove(mPathOverlay);
        }
        if (mCurrentRouteGeoPoints != null) {
            ((TextView)view.findViewById(R.id.fm_distance_route)).setText(InstructionInfo.stringDistance(mCurrentRouteDistance));
            mOverviewLayoutDistance.setVisibility(View.VISIBLE);
            mInstructionButton.setVisibility(View.VISIBLE);

            ArrayList<GeoPoint> partialPolyLine = new ArrayList<>();
            partialPolyLine.add(new GeoPoint(originPosition.getLatitude(), originPosition.getLongitude()));
            partialPolyLine.add(new GeoPoint(mDestinationPosition.getLocation().getLatitude(), mDestinationPosition.getLocation().getLongitude()));

            final int color = ContextCompat.getColor(getContext(), R.color.color_back_blue);

            mPathOverlay = new PathOverlay(color, 8.0f, getDefaultResourceProxyImpl());
            for (GeoPoint geoPoint : mCurrentRouteGeoPoints) {
                mPathOverlay.addPoint(geoPoint);
                partialPolyLine.add(geoPoint);
            }

            mMapView.getOverlays().add(mPathOverlay);
            centerToBox(partialPolyLine);

            mMapView.invalidate();
        }
    }

    /*GETTER AND SETTER*/

    public Location getOriginPosition() {
        return originPosition;
    }

    public void setOriginPosition(Location originPosition) {
        this.originPosition = originPosition;
    }

    public Location getUserPosition() {
        return userPosition;
    }

    public MarkerInfoWindow getMenuWindow() {
        return menuWindow;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /*CLASS DECLARATIONS*/

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

            if (!MapUtils.containedInBox(AbstractMap.instance().getExtent(), location)){
                Toast.makeText(getActivity(), getContext().getString(R.string.outSide), Toast.LENGTH_SHORT).show();
                return;
            }

            LogUtils.LOGD(TAG, "onLocationChanged: " + location);
            //originPosition = location;
            updateUserPosition(/*originPosition*/location, true, false);
            destroyLocationManager();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtils.LOGD(TAG, "onStatusChanged: " + provider + "; status: " + status);
        }

        public void onProviderEnabled(String provider) {
            LogUtils.LOGD(TAG, "onProviderEnabled: " + provider);
        }

        public void onProviderDisabled(String provider) {
            LogUtils.LOGD(TAG, "onProviderDisabled: " + provider);
        }

    }
}
