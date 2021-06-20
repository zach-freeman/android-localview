package com.sparkwing.localview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

/**
 * Created by zsfree00 on 7/11/16.
 */
public class RequestPermissionUtils {
    private static final String TAG = RequestPermissionUtils.class.getSimpleName();
    public interface RequestPermissionCallback {
        void Granted(int requestCode);

        void Denied(int requestCode);

    }

    public RequestPermissionUtils() {

    }


    public int checkPermission(Context context, String permissionName) {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                permissionName);
        return permissionCheck;
    }

    public void requestPermission(Activity activity,
                                  String permissionName,
                                  RequestPermissionCallback requestPermissionCallback,
                                  int permissionCode) {
        int permissionCheck = checkPermission(activity, permissionName);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            requestPermissionCallback.Granted(permissionCode);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    permissionCode);
        }

    }

    public void onRequestPermissionsResult(RequestPermissionCallback requestPermissionCallback,
                                           int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermissionCallback.Granted(requestCode);

                } else {
                    requestPermissionCallback.Denied(requestCode);
                }
                return;
            }
        }
    }
}
