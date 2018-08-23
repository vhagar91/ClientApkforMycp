package hds.aplications.com.mycp.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import hds.aplications.com.mycp.R;
import mgleon.common.com.PermissionsManager;

/**
 * Created by mgleon on 15/07/2017.
 */

public class PermissionsAdapter {

    private static PermissionsManager.Listener listenerPermissions = new PermissionsManager.Listener() {
        @Override
        public void hasAllPermissions() {}

        @Override
        public void notPermission(String permission) {}
    };
    private static String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            /*Manifest.permission.GET_ACCOUNTS,*/
            /*Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,*/
            Manifest.permission.READ_SYNC_STATS,
            Manifest.permission.READ_SYNC_SETTINGS,
            Manifest.permission.WRITE_SYNC_SETTINGS
    };

    public static void checkPermissions(@NonNull Activity activity){
        PermissionsManager permissionsManager = new PermissionsManager(listenerPermissions, activity, permissions);
        permissionsManager.checkPermissions();
    }

    public static void onRequestPermissionsResult(@NonNull Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode) {
            case PermissionsManager.PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the                 // camera-related task you need to do.
                } else {
                    // permission denied, boo! Disable the                 // functionality that depends on this permission.             }
                    Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.permission_denied), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
        }
    }
}
