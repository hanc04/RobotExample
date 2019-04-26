package com.baidu.aip.robotexample.settings;

import android.app.Dialog;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class VolumeDialog extends DialogFragment implements View.OnClickListener {

    private Button addVolumeBtn;
    private Button subVolumeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_volume_adjust, null);
        addVolumeBtn = view.findViewById(R.id.volumeAddBtn);
        addVolumeBtn.setOnClickListener(this);
        subVolumeBtn = view.findViewById(R.id.volumeSubBtn);
        subVolumeBtn.setOnClickListener(this);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton("Cancel", null)
                .create();
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
