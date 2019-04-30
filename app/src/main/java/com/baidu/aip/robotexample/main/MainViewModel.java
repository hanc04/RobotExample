package com.baidu.aip.robotexample.main;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.aip.robotexample.BuildConfig;

import java.lang.ref.WeakReference;
import java.util.List;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.network.interfaces.IDialogListener;
import aip.baidu.com.robotsdk.network.interfaces.IErrorListener;
import aip.baidu.com.robotsdk.network.interfaces.IInstructionListener;
import aip.baidu.com.robotsdk.network.interfaces.IScreenListener;
import aip.baidu.com.robotsdk.network.interfaces.ISpeakerControllerListener;
import aip.baidu.com.robotsdk.network.model.NamespaceGroup;
import aip.baidu.com.robotsdk.network.model.card.ListCard;
import aip.baidu.com.robotsdk.network.model.card.NormalCard;
import aip.baidu.com.robotsdk.network.model.card.SimpleImageCard;
import aip.baidu.com.robotsdk.network.model.card.TextCard;
import aip.baidu.com.robotsdk.network.model.card.UserCard;
import aip.baidu.com.robotsdk.network.model.card.VideoCard;
import aip.baidu.com.robotsdk.network.model.card.WeatherInfoCard;
import aip.baidu.com.robotsdk.speech.SpeechBean;

public class MainViewModel implements RobotSDKEngine.SpeechCallBack, IDialogListener,
        IScreenListener, IErrorListener, IInstructionListener, ISpeakerControllerListener {

    private static final String TAG = "MainViewModel";

    private WeakReference<ConsoleDelegate> delegateRef;
    private boolean isListening;
    private boolean mIsUpdating;

    public MainViewModel(ConsoleDelegate delegate) {
        this.delegateRef = new WeakReference<>(delegate);
        this.isListening = false;
    }

    public boolean isListening() {
        return isListening;
    }

    public void startListening() {
        RobotSDKEngine.getInstance().registerSpeechListener(this);
        RobotSDKEngine.getInstance().addDirectiveListener(NamespaceGroup.DIALOG, this);
        RobotSDKEngine.getInstance().addDirectiveListener(NamespaceGroup.SCREEN, this);
        RobotSDKEngine.getInstance().addDirectiveListener(NamespaceGroup.ERROR, this);

        RobotSDKEngine.getInstance().startListening();
        isListening = true;
    }

    public void stopListening() {
        RobotSDKEngine.getInstance().stopListening();

        RobotSDKEngine.getInstance().unRegisterSpeechListener(this);
        RobotSDKEngine.getInstance().removeDirectiveListener(NamespaceGroup.DIALOG, this);
        RobotSDKEngine.getInstance().removeDirectiveListener(NamespaceGroup.SCREEN, this);
        RobotSDKEngine.getInstance().removeDirectiveListener(NamespaceGroup.ERROR, this);
        isListening = false;
    }

    @Override
    public void onStatus(int i) {
    }

    @Override
    public void onNewSpeech(SpeechBean speechBean) {
        Log.d(TAG, "onNewSpeech: " + speechBean.speech);
        String message = speechBean.speech;
        if (!mIsUpdating) {
            Log.d(TAG, "onNewSpeech: insert");
            delegateRef.get().insertChat(new Message(Message.USER, message));
            mIsUpdating = true;
        } else {
            Log.d(TAG, "onNewSpeech: update");
            delegateRef.get().updateChat(message);
        }
        if (speechBean.isFinal) {
            Log.d(TAG, "onNewSpeech: final status");
            mIsUpdating = false;
        }
    }

    @Override
    public void onWakeUp(String s) {
        delegateRef.get().printLog("onWakeUp: " + s);
    }

    @Override
    public void onWakeAngle(int i) {
        delegateRef.get().printLog("onWakeAngle: " + i);
    }

    @Override
    public void onError(int i) {
        delegateRef.get().printLog("onError:" + i);
    }

    @Override
    public void onConfigObtained(String languageId) {

    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {

    }

    @Override
    public void onVoiceOutput(String s) {
        RobotSDKEngine.getInstance().stopSpeaking();
        if (TextUtils.isEmpty(s)
                || TextUtils.isEmpty(s.trim())
                || "null".equals(s.trim())) {
            s = "";
        }
        RobotSDKEngine.getInstance().speak(s);
    }

    @Override
    public void onTextOutput(String s) {

        s = "[" + BuildConfig.ROBOT_NAME + "]:" + s;
        delegateRef.get().insertChat(new Message(Message.ROBOT, s));
    }

    @Override
    public void onHints(List<String> list) {
        delegateRef.get().printLog("onHints, size = " + list.size());
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onRenderTextCard(TextCard textCard) {
        delegateRef.get().printLog("onRenderTextCard:" + textCard.getUrl() + "\n" + textCard.getContent());
    }

    @Override
    public void onRenderImageCard(NormalCard normalCard) {

    }

    @Override
    public void onRenderNormalList(ListCard<NormalCard> listCard) {
        delegateRef.get().printLog("onRenderNormalList size:" + listCard.getList().size());
    }

    @Override
    public void onRenderSimpleImgList(ListCard<SimpleImageCard> listCard) {
        delegateRef.get().printLog("onRenderSimpleImgList size:" + listCard.getList().size());
    }

    @Override
    public void onRenderVideoList(ListCard<VideoCard> listCard) {
        delegateRef.get().printLog("onRenderVideoList, list size: " + listCard.getList().size());
    }

    @Override
    public void onRenderPerson(UserCard userCard, byte[] bytes) {
        delegateRef.get().printLog("onRenderPerson: " + userCard.getUid());
    }

    @Override
    public void onRenderWeather(WeatherInfoCard weatherInfoCard) {
        delegateRef.get().printLog("onRenderWeather, list size: " + weatherInfoCard.getWeatherInfo().size());
    }

    @Override
    public void onRenderExpression(String s) {
        delegateRef.get().printLog("onRenderExpression:" + s);
    }

    @Override
    public void onInquireEnergy() {
        delegateRef.get().printLog("onInquireEnergy");

    }

    @Override
    public void onShowFeatures() {
        delegateRef.get().printLog("onShowFeatures");
    }

    @Override
    public void onSetMute() {
        delegateRef.get().printLog("onSetMute");
    }

    @Override
    public void onAdjustVolume(String volumeControl, String volumeValue) {
        delegateRef.get().printLog("onAdjustVolume, volumeControl: " + volumeControl + ", volumeValue: " + volumeValue);
    }

    @Override
    public boolean onDirectiveReceived(String s, String s1, String s2) {
        return false;
    }

    public interface ConsoleDelegate {

        void updateChat(String content);
        void insertChat(Message message);
        void clearChat();
        void printLog(String text);
        void clearLog();
    }
}
