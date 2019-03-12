package com.baidu.aip.robotexample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    public static void setActivePermissions(Activity activity) {
        boolean isAllGranted = checkPermissionAllGranted(activity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.DISABLE_KEYGUARD
        });

        if (isAllGranted) {
            return;
        }
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.READ_PHONE_STATE
        }, MY_PERMISSION_REQUEST_CODE);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private static boolean checkPermissionAllGranted(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

}
