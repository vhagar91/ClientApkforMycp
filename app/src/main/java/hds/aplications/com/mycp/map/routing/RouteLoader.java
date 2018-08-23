package hds.aplications.com.mycp.map.routing;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.List;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.helpers.SAppData;
import hds.aplications.com.mycp.map.downloader.AbstractMap;
import mgleon.common.com.LogUtils;


public class RouteLoader extends AsyncTaskLoader<RouteLoader.Result> {

    public static class Result {
        static final Result INTERNAL_ERROR = new Result(false, true);
        public static final Result NO_ROUTE = new Result(true, false);

        private final List<GeoPoint> mGeoPoints;
        private double distance;
        private final boolean mIsError;
        private final boolean mIsNoRoute;

        public Result(@NonNull List<GeoPoint> geoPoints, double distance) {
            mGeoPoints = geoPoints;
            this.distance = distance;
            mIsError = mIsNoRoute = false;
        }

        private Result(boolean isNoRoute, boolean isError) {
            mGeoPoints = null;
            mIsNoRoute = isNoRoute;
            mIsError = isError;
        }

        public List<GeoPoint> getGeoPoints() {
            return mGeoPoints;
        }

        public double getDistance() {
            return distance;
        }

        public boolean isError() {
            return mIsError;
        }

        public boolean isNoRoute() {
            return mIsNoRoute;
        }
    }

    private static final String TAG = LogUtils.makeLogTag(RouteLoader.class);

    private final Location mStartLocation;
    private final Location mEndLocation;

    @StringDef({EncodingManager.CAR, EncodingManager.FOOT, EncodingManager.BIKE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Vehicle {}
    private String vehicle;

    public RouteLoader(@NonNull Context context, @NonNull Location startLocation, @NonNull Location endLocation, @Vehicle String vehicle) {
        super(context);
        mStartLocation = startLocation;
        mEndLocation = endLocation;
        this.vehicle = vehicle;
    }

    @Override
    public Result loadInBackground() {
        LogUtils.LOGI(TAG, "#loadInBackground; mStartLocation = " + mStartLocation + "; mEndLocation = " + mEndLocation);
        try {
            final File mapsforgeFile = AbstractMap.instance().getMapsforgeFile(getContext());
            String graphHopperLocation = mapsforgeFile.getParent();

            final GraphHopper hopper = new GraphHopper().forMobile();
            //hopper.setInMemory();
            hopper.setCHEnable(false);
            hopper.setOSMFile(mapsforgeFile.getAbsolutePath());
            hopper.setGraphHopperLocation(graphHopperLocation);
            hopper.setEncodingManager(new EncodingManager(EncodingManager.CAR + "," + EncodingManager.FOOT + "," + EncodingManager.BIKE));
            hopper.importOrLoad();
            final GHRequest req = new GHRequest(mStartLocation.getLatitude(), mStartLocation.getLongitude(), mEndLocation.getLatitude(), mEndLocation.getLongitude()).setVehicle(this.vehicle);
            final GHResponse rsp = hopper.route(req);
            if (rsp.hasErrors()) {
                LogUtils.LOGW(TAG, "GHResponse contains errors!");
                List<Throwable> errors = rsp.getErrors();
                for (int i = 0; i < errors.size(); i++) {
                    LogUtils.LOGE(TAG, "Graphhopper error #" + i, errors.get(i));
                }
                return Result.INTERNAL_ERROR;
            }
            else {
                final List<GeoPoint> geoPoints = new LinkedList<>();
                final PointList points = rsp.getPoints();
                double distance = rsp.getDistance();
                SAppData.getInstance().setInstructions(rsp.getInstructions());
                double lati, longi, alti;
                for (int i = 0; i < points.getSize(); i++) {
                    lati = points.getLatitude(i);
                    longi = points.getLongitude(i);
                    alti = points.getElevation(i);
                    geoPoints.add(new GeoPoint(lati, longi, alti));
                }
                return new Result(geoPoints, distance);
            }
        } catch (OutOfMemoryError e) {
            LogUtils.LOGE(TAG, "Graphhoper OOM", e);
            return Result.INTERNAL_ERROR;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        LogUtils.LOGD(TAG, "onStartLoading");
        forceLoad();
    }

    public static boolean isRunning(@NonNull LoaderManager loaderManager) {
        final Loader<Object> loader = loaderManager.getLoader(R.id.loader_find_route);
        return loader != null && loader.isStarted();
    }

}
