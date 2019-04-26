package com.baidu.aip.robotexample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.aip.robotexample.settings.SettingsFragment;
import com.baidu.aip.robotexample.settings.WifiSettingFragment;

import java.util.ArrayList;
import java.util.List;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MainViewModel.ConsoleDelegate, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView consoleTextView;
    private ScrollView consoleScroll;

    private RecyclerView dialogRecyclerView;
    private DialogAdapter dialogAdapter;

    private FrameLayout frameLayout;
    private Button switchBtn;
    private Button clearBtn;
    private Button settingsBtn;

    private TextView serialView;

    private MainViewModel viewModel;
    private List<Message> mChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        PermissionUtil.setActivePermissions(this);
        mChatList = new ArrayList<>();
        viewModel = new MainViewModel(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        consoleTextView = findViewById(R.id.tv_console);
        consoleScroll = findViewById(R.id.sv_console);
        serialView = findViewById(R.id.serialDeviceIdView);
        frameLayout = findViewById(R.id.setting_container);

        String serialNumber = RobotSDKEngine.getInstance().getSerialNumber();
        Log.d(TAG, "initView: serialNumber: " + serialNumber);

        serialView.setText(serialNumber);

        dialogRecyclerView = findViewById(R.id.rv_dialog);
        dialogAdapter = new DialogAdapter(this, mChatList);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dialogRecyclerView.setAdapter(dialogAdapter);

        switchBtn = findViewById(R.id.btn_switch);
        clearBtn = findViewById(R.id.btn_clear);
        settingsBtn = findViewById(R.id.btn_settings);
        switchBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch:
                if (viewModel.isListening()) {
                    viewModel.stopListening();
                    switchBtn.setBackgroundColor(getResources().getColor(R.color.green));
                    switchBtn.setText(getString(R.string.start));
                } else {
                    viewModel.startListening();
                    switchBtn.setBackgroundColor(getResources().getColor(R.color.red));
                    switchBtn.setText(getString(R.string.stop));
                }
                break;
            case R.id.btn_clear:
                clearChat();
                clearLog();
                break;
            case R.id.btn_settings:
                SettingsFragment settingsFragment = (SettingsFragment)
                        getSupportFragmentManager().findFragmentByTag("settings");
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.setting_container, settingsFragment)
                        .addToBackStack("settings")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        int count = getSupportFragmentManager().getBackStackEntryCount();
                        if (count == 0) {
                            frameLayout.setVisibility(View.GONE);
                        }
                    }
                });
                frameLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateChat(String content) {
        int pos = getLastActiveUserMessagePos();
        if (-1 == pos) { // there's no active user message !
            return;
        }
        mChatList.get(pos).setMessage(content);
        dialogAdapter.notifyItemChanged(pos);

    }

    @Override
    public void insertChat(Message message) {
        mChatList.add(message);
        int pos = mChatList.size() - 1;
        dialogAdapter.notifyItemInserted(pos);
        dialogRecyclerView.smoothScrollToPosition(pos);
    }

    @Override
    public void clearChat() {
        final int size = mChatList.size();
        mChatList.clear();
        dialogAdapter.notifyItemRangeRemoved(0, size);
        dialogRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void printLog(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                consoleTextView.append("\n" + text);
                consoleScroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void clearLog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                consoleTextView.setText("");
            }
        });
    }

    private int getLastActiveUserMessagePos() {
        for (int i = mChatList.size() - 1; i > 0; i--) {
            if (mChatList.get(i).getType() == Message.USER) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.MY_PERMISSION_REQUEST_CODE == requestCode) {
            PermissionUtil.checkPermissionResult(grantResults);
        }
    }
}
