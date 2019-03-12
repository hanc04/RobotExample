package com.baidu.aip.robotexample;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.network.interfaces.IDialogListener;
import aip.baidu.com.robotsdk.network.interfaces.IErrorListener;
import aip.baidu.com.robotsdk.network.interfaces.IScreenListener;
import aip.baidu.com.robotsdk.network.model.NamespaceGroup;
import aip.baidu.com.robotsdk.network.model.card.ListCard;
import aip.baidu.com.robotsdk.network.model.card.NormalCard;
import aip.baidu.com.robotsdk.network.model.card.SimpleImageCard;
import aip.baidu.com.robotsdk.network.model.card.TextCard;
import aip.baidu.com.robotsdk.network.model.card.UserCard;
import aip.baidu.com.robotsdk.network.model.card.VideoCard;
import aip.baidu.com.robotsdk.network.model.card.WeatherInfoCard;
import aip.baidu.com.robotsdk.speech.SpeechBean;

public class MainViewModel implements RobotSDKEngine.SpeechCallBack, IDialogListener, IScreenListener, IErrorListener {

    private WeakReference<ConsoleDelegate> delegateRef;
    private List<DialogAdapter.Message> messages;
    private boolean isListening;
    private boolean isUpdate;
    // 当超时或取消超时限制时设置为true
    private boolean isOverTime = false;

    public MainViewModel(ConsoleDelegate delegate) {
        this.delegateRef = new WeakReference<>(delegate);
        this.isListening = false;
        messages = new ArrayList<>();
    }

    public boolean isListening() {
        return isListening;
    }

    public List<DialogAdapter.Message> getMessages() {
        return messages;
    }

    public int getMessageSize() {
        return messages.size();
    }

    public DialogAdapter.Message getLastMessages() {
        return messages.get(messages.size() - 1);
    }

    public void clearMessages() {
        messages.clear();
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
        int pos;
        if (!isUpdate) {
            pos = getMessageSize() - 1;
            messages.add(new DialogAdapter.Message(DialogAdapter.Message.USER, speechBean.speech));

            isUpdate = true;
            if (speechBean.isFinal) {
                // 不更新
                isUpdate = false;
                // 取消超时机制
                isOverTime = true;

            }
        } else {
            if (getLastMessages().getType() == DialogAdapter.Message.ROBOT) {
                pos = getMessageSize() - 2;
            } else {
                pos = getMessageSize() - 1;
            }
            messages.get(pos).setMessage(speechBean.speech);
            if (speechBean.isFinal) {
                // 不更新
                isUpdate = false;
                // 取消超时机制
                isOverTime = true;
            }
        }
        delegateRef.get().updateDialog(pos);
    }

    @Override
    public void onWakeUp(String s) {

    }

    @Override
    public void onWakeAngle(int i) {

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
//        RobotSDKEngine.getInstance().stopSpeaking();
//        if (TextUtils.isEmpty(s)
//                || TextUtils.isEmpty(s.trim())
//                || "null".equals(s.trim())) {
//            s = "";
//        }
//        RobotSDKEngine.getInstance().speak(s);
    }

    @Override
    public void onTextOutput(String s) {
        messages.add(new DialogAdapter.Message(DialogAdapter.Message.ROBOT, s));
        delegateRef.get().updateDialog(messages.size()-1);
    }

    @Override
    public void onHints(List<String> list) {
        for (String s : list) {
            delegateRef.get().printLog("onHints:" + s);
        }
    }

    @Override
    public void onEnd() {

    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onRenderTextCard(TextCard textCard) {
        delegateRef.get().printLog("onRenderTextCard:" + textCard.getUrl());
        delegateRef.get().printLog("onRenderTextCard:" + textCard.getContent() + "\n");
    }

    @Override
    public void onRenderImageCard(NormalCard normalCard) {

    }

    @Override
    public void onRenderNormalList(ListCard<NormalCard> listCard) {
        for (NormalCard card : listCard.getList()) {
            delegateRef.get().printLog("onRenderNormalList:" + card.getTitle());
            delegateRef.get().printLog("onRenderNormalList:" + card.getContent() + "\n");
        }

    }

    @Override
    public void onRenderSimpleImgList(ListCard<SimpleImageCard> listCard) {
        for (SimpleImageCard card : listCard.getList()) {
            delegateRef.get().printLog("onRenderSimpleImgList:" + card.getImage());
            delegateRef.get().printLog("onRenderSimpleImgList:" + card.getUrl());
        }
    }

    @Override
    public void onRenderVideoList(ListCard<VideoCard> listCard) {

    }

    @Override
    public void onRenderPerson(UserCard userCard, byte[] bytes) {

    }

    @Override
    public void onRenderWeather(WeatherInfoCard weatherInfoCard) {
        for (WeatherInfoCard.WeatherInfo info : weatherInfoCard.getWeatherInfo()) {
            delegateRef.get().printLog("onRenderWeather:" + info.getWeather());
            // developer can log more property here
        }
    }

    @Override
    public void onRenderExpression(String s) {
        delegateRef.get().printLog("onRenderExpression:" + s);
    }

    @Override
    public boolean onDirectiveReceived(String s, String s1, String s2) {
        return false;
    }

    public interface ConsoleDelegate {
        void updateDialog(int position);
        void clearDialog();
        void printLog(String text);
        void clearLog();
    }
}
