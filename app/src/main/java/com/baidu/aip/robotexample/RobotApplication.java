package com.baidu.aip.robotexample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.aip.robotexample.utils.PropertiesUtil;

import java.util.Properties;

import aip.baidu.com.robotsdk.CameraConfig;
import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.SDKConfig;
import aip.baidu.com.robotsdk.camera.AbstractCamera;


public class RobotApplication extends Application {
    private static final String TAG = "RobotApplication";

    private static Context context;
    public static volatile boolean mActivated = false;
    public static volatile int mActivateErrorCode = 0;
    public static final CameraConfig CAMERA_CONFIG
            = new CameraConfig(1, AbstractCamera.TYPE_INTERNAL_FRONT, "1");
    public static volatile String mActivateErrorMsg = "";

    private RobotSDKEngine.DeviceActivationCallback mActivateCallback = new RobotSDKEngine.DeviceActivationCallback() {
        @Override
        public void onActivateSuccess() {
            Log.d(TAG, "onActivateSuccess: ");
            mActivated = true;
        }

        @Override
        public void onActivateFailed(int errorCode, String errorMsg) {
            Log.d(TAG, "onActivateFailed: " + errorMsg);
            Log.d(TAG, "onActivateFailed: " + errorCode);
            mActivated = false;
            mActivateErrorCode = errorCode;
            mActivateErrorMsg = "激活失败";
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // SDK配置项
        context = getApplicationContext();

        Properties config = PropertiesUtil.getProperties(this);
        String clientId = config.getProperty("clientId");
        String clientSecret = config.getProperty("clientSecret");

        SDKConfig.Builder builder = new SDKConfig.Builder();
        builder.context(context)
                .sdkType(SDKConfig.SDK_FACE_CONVERSATION)
                .clientid(clientId)
                .clientSecret(clientSecret)
                .cameraType(AbstractCamera.TYPE_INTERNAL_FRONT)
                .faceAngle(0)
                .isFaceAttrDetect(true)
                .faceRecognizeType(SDKConfig.REC_TYPE_OFFLINE)
                .speechServiceType(SDKConfig.SPEECH_TYPE_INTERNAL)
                .asrVolumeNeed(true);
        try {
            RobotSDKEngine.getInstance().registerDeviceActivationCallback(mActivateCallback);
            RobotSDKEngine.getInstance().initSDK(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
