package com.mich.todolist.utilities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.Callable;

/**
 * Created by Michal on 05.03.2018.
 */

public class PermissionsChecker {
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 0;

    private static Callable<Void> onPermissionGrantedCallable;

    public static boolean requestReadExternalStoragePermission(AppCompatActivity activity,
                                                               @NonNull Callable<Void> onPermissinGranted) {
        onPermissionGrantedCallable = onPermissinGranted;

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                onPermissionGrantedCallable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);

            return false;
        }
    }

    public static boolean onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                                    @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                onPermissionGrantedCallable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }
}
