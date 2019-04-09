package com.baidu.aip.robotexample;

import android.app.Application;
import android.content.Context;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.SDKConfig;


public class RobotApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        // SDK配置项
        context = getApplicationContext();
        SDKConfig.Builder builder = new SDKConfig.Builder();
        builder.context(context)
                .sdkType(SDKConfig.SDK_CONVERSATION)
                .apikey(aip.baidu.com.robotsdk.BuildConfig.API_KEY)
                .appid(aip.baidu.com.robotsdk.BuildConfig.APP_ID)
                .secretkey(aip.baidu.com.robotsdk.BuildConfig.SECRET_KEY)
                .wifiSSID("HUAWEI-7713")
                .wifiPWD("93121824")
                .wifiType(SDKConfig.SECURITY_WPA)
                .asrVolumeNeed(true)
                .faceAngle(0)
                .speechServiceType(SDKConfig.SPEECH_TYPE_INTERNAL);
        try {
            RobotSDKEngine.getInstance().initSDK(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
