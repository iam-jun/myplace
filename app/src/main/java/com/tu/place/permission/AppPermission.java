package com.tu.place.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class AppPermission {
    public static final int REQUEST_PERMISSION = 1;
    private static final String[] PERMISSION_LIST = {Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean acceptedPemission(AppCompatActivity activity) {
        for (String s : PERMISSION_LIST) {
            if (ActivityCompat.checkSelfPermission(activity,s) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermission(AppCompatActivity activity){
        activity.requestPermissions(PERMISSION_LIST,REQUEST_PERMISSION);
    }
}
