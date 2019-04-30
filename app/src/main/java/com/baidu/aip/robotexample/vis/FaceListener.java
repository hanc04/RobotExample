package com.baidu.aip.robotexample.vis;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.network.interfaces.IScreenListener;
import aip.baidu.com.robotsdk.network.model.card.ListCard;
import aip.baidu.com.robotsdk.network.model.card.NormalCard;
import aip.baidu.com.robotsdk.network.model.card.SimpleImageCard;
import aip.baidu.com.robotsdk.network.model.card.TextCard;
import aip.baidu.com.robotsdk.network.model.card.VideoCard;
import aip.baidu.com.robotsdk.network.model.card.WeatherInfoCard;

public abstract class FaceListener implements IScreenListener, RobotSDKEngine.RecognizeListener {
    @Override
    public void onRenderTextCard(TextCard textCard) {

    }

    @Override
    public void onRenderImageCard(NormalCard normalCard) {

    }

    @Override
    public void onRenderNormalList(ListCard<NormalCard> listCard) {

    }

    @Override
    public void onRenderSimpleImgList(ListCard<SimpleImageCard> listCard) {

    }

    @Override
    public void onRenderVideoList(ListCard<VideoCard> listCard) {

    }

    @Override
    public void onRenderWeather(WeatherInfoCard weatherInfoCard) {

    }

    @Override
    public void onRenderExpression(String s) {

    }

    @Override
    public boolean onDirectiveReceived(String s, String s1, String s2) {
        return false;
    }
}
