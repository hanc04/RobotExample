package com.baidu.aip.robotexample.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.aip.robotexample.R;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import aip.baidu.com.robotsdk.SDKConfig;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VolumeDialog extends BaseDialogFragment implements View.OnClickListener {

    private Button addVolumeBtn;
    private Button subVolumeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_volume_adjust;
    }

    @Override
    protected void setupView(View root) {
        addVolumeBtn = root.findViewById(R.id.volumeAddBtn);
        addVolumeBtn.setOnClickListener(this);
        subVolumeBtn = root.findViewById(R.id.volumeSubBtn);
        subVolumeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volumeAddBtn:
                RobotSDKEngine.getInstance().adjustVolume(SDKConfig.VOLUME_ADD);
                RobotSDKEngine.getInstance().speak("这是当前音量");
                Toast.makeText(getActivity(), "增加音量", Toast.LENGTH_SHORT).show();
                break;
            case R.id.volumeSubBtn:
                RobotSDKEngine.getInstance().adjustVolume(SDKConfig.VOLUME_SUB);
                RobotSDKEngine.getInstance().speak("这是当前音量");
                Toast.makeText(getActivity(), "减少音量", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
