package com.baidu.aip.robotexample;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainViewModel.ConsoleDelegate, View.OnClickListener {

    private TextView consoleTextView;
    private ScrollView consoleScroll;

    private RecyclerView dialogRecyclerView;
    private DialogAdapter dialogAdapter;

    private Button switchBtn;
    private Button clearBtn;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        PermissionUtil.setActivePermissions(this);
        viewModel = new MainViewModel(this);

        initView();
    }

    private void initView() {
        consoleTextView = findViewById(R.id.tv_console);
        consoleScroll = findViewById(R.id.sv_console);

        dialogRecyclerView = findViewById(R.id.rv_dialog);
        dialogAdapter = new DialogAdapter(this, viewModel.getMessages());
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dialogRecyclerView.setAdapter(dialogAdapter);

        switchBtn = findViewById(R.id.btn_switch);
        clearBtn = findViewById(R.id.btn_clear);
        switchBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
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
                clearDialog();
                clearLog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateDialog(final int pos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogAdapter.notifyDataSetChanged();
                dialogRecyclerView.getLayoutManager().scrollToPosition(pos);
            }
        });
    }

    @Override
    public void clearDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.clearMessages();
                dialogAdapter.notifyDataSetChanged();
                dialogRecyclerView.getLayoutManager().scrollToPosition(0);
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
}
