package com.baidu.aip.robotexample.vis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.SurfaceView;

import com.baidu.aip.robotexample.RobotApplication;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import aip.baidu.com.robotsdk.CameraConfig;
import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.camera.AbstractCamera;
import aip.baidu.com.robotsdk.face.FaceInfos;
import aip.baidu.com.robotsdk.network.model.NamespaceGroup;
import aip.baidu.com.robotsdk.network.model.card.UserCard;
import aip.baidu.com.robotsdk.utils.Base64Utils;

public class VisViewModel extends FaceListener {

    private static final String TAG = "VisViewModel";

    private CameraConfig cameraConfig;
    private WeakReference<VisDelegate> delegateRef;

    public VisViewModel(VisDelegate delegate) {
        delegateRef = new WeakReference<>(delegate);
        cameraConfig = new CameraConfig(2, AbstractCamera.TYPE_INTERNAL_FRONT, "0");

        RobotSDKEngine.getInstance().addDirectiveListener(NamespaceGroup.SCREEN, this);
    }

    public void startFaceLogin(SurfaceView surfaceView) {
        RobotSDKEngine.getInstance().startFaceRecognize(surfaceView, RobotSDKEngine.TASK_FACE_LOGIN, RobotApplication.CAMERA_CONFIG);
        RobotSDKEngine.getInstance().registerFaceListener(this);
    }

    public void startfaceRecog(SurfaceView surfaceView) {
        RobotSDKEngine.getInstance().startFaceRecognize(surfaceView, RobotSDKEngine.TASK_FACE_RECOGNIZE, RobotApplication.CAMERA_CONFIG);
        RobotSDKEngine.getInstance().registerFaceListener(this);
    }

    public void startFaceRecognizeOffline(SurfaceView surfaceView) {

    }

    public void updateFaceLibFully() {
        int ret = RobotSDKEngine.getInstance().recordAllFaceFeature();
        delegateRef.get().printLog("update face lib fully: " + ret);
    }

    public void updateFaceLibIncrementally() {
        int ret = RobotSDKEngine.getInstance().recordDeltaFaceFeature();
        delegateRef.get().printLog("update face lib incrementally: " + ret);
    }

    public void stop() {
        RobotSDKEngine.getInstance().stopTracking();
    }

    @Override
    public void onNewFace(FaceInfos faceInfos, Bitmap bitmap) {
        delegateRef.get().printLog("onNewFace: " + faceInfos.faceInfo.face_id);
//        delegateRef.get().showImg(bitmap);
    }

    @Override
    public void onFaceTracker(FaceInfos faceInfos) {

    }

    @Override
    public void onFaceResult(int i, String s) {
        delegateRef.get().printLog("onFaceResult: " + i + "; result: " + s);
    }

    @Override
    public void onFaceOut(int i) {
        delegateRef.get().printLog("onFaceOut: " + i);
    }

    @Override
    public void onError(int i) {
        delegateRef.get().printLog("onError: " + i);
    }

    @Override
    public void onRenderPerson(UserCard userCard, byte[] bytes) {
        delegateRef.get().printLog("onRenderPerson: " + userCard.toString());
        String str = new String(bytes);
        Log.d(TAG, "onRenderPerson: " + str);
//        byte[] data = Base64Utils.decode(userCard.getImage(), Base64Utils.DEFAULT);
        byte[] data = bytes;
        String ret = null;
        try {
            ret = new String(data, "UTF-8");
            delegateRef.get().printLog("bytes to String: " + ret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//        delegateRef.get().showImg(bm);
    }

    public interface VisDelegate {
        void showImg(Bitmap bitmap);
        void printLog(String text);
        void clearLog();
    }
}
