package com.baidu.aip.robotexample.vis;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.aip.robotexample.R;

public class VisActivity extends AppCompatActivity implements View.OnClickListener, VisViewModel.VisDelegate {

    private VisViewModel viewModel;

    private SurfaceView cameraSurfaceView;
    private TextView consoleTextView;
    private ScrollView consoleScroll;
    private ImageView faceImageView;
    private Button startFaceLoginBtn;
    private Button startFaceRecognizeOfflineBtn;
    private Button stopBtn;
    private Button updateFullyUBtn;
    private Button updateIncrementallyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vis);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        cameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        faceImageView = findViewById(R.id.faceImageView);
        consoleTextView = findViewById(R.id.tv_console);
        consoleScroll = findViewById(R.id.terminalScroll);
        startFaceLoginBtn = findViewById(R.id.startFaceLoginBtn);
        startFaceRecognizeOfflineBtn = findViewById(R.id.startFaceRecognizeOffline);
        stopBtn = findViewById(R.id.stopRecBtn);
        updateFullyUBtn = findViewById(R.id.fullyUpdateBtn);
        updateIncrementallyBtn = findViewById(R.id.incrementallyUpdateBtn);

        startFaceLoginBtn.setOnClickListener(this);
        startFaceRecognizeOfflineBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        updateFullyUBtn.setOnClickListener(this);
        updateIncrementallyBtn.setOnClickListener(this);

        viewModel = new VisViewModel(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startFaceLoginBtn:
                viewModel.startFaceLogin(cameraSurfaceView);
                break;
            case R.id.startFaceRecognizeOffline:
                viewModel.startFaceRecognizeOffline(cameraSurfaceView);
                break;
            case R.id.stopRecBtn:
                viewModel.stop();
                break;
            case R.id.fullyUpdateBtn:
                viewModel.updateFaceLibFully();
                break;
            case R.id.incrementallyUpdateBtn:
                viewModel.updateFaceLibIncrementally();
                break;
            default:
                break;
        }
    }

    @Override
    public void showImg(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceImageView.setImageBitmap(bitmap);
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            viewModel.stop();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
