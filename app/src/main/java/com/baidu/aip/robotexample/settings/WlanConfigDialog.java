package com.baidu.aip.robotexample.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.aip.robotexample.R;

public class WlanConfigDialog extends BaseDialogFragment {
    public static final String SECURITY_NONE = "0";
    public static final String SECURITY_WEP = "1";
    public static final String SECURITY_WPA = "2";
    public static final String SECURITY_WPA2 = "3";

    private TextView mTitle;
    private EditText mPwdEditText;
    private Spinner mSpinner;

    private Button mCancelBtn;
    private Button mConfirmBtn;

    public static WlanConfigDialog newInstance(String ssid) {

        Bundle args = new Bundle();
        args.putString("ssid", ssid);
        WlanConfigDialog fragment = new WlanConfigDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_wifi_config;
    }

    @Override
    protected void setupView(View root) {
        mTitle = root.findViewById(R.id.title);
        mTitle.setText(getArguments().getString("ssid"));
        mPwdEditText = root.findViewById(R.id.pwdEditText);
        mSpinner = root.findViewById(R.id.secureSpinner);
        String[] secureTypes = {"NONE", "WEP", "WPA", "WPA2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, secureTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mCancelBtn = root.findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mConfirmBtn = root.findViewById(R.id.confirm);
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid = getArguments().getString("ssid");
                String pwd = mPwdEditText.getText().toString();
                int pos = mSpinner.getSelectedItemPosition();
                switch (pos) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
                // TODO: 2019/4/27 set wifi
                // TODO: 2019/4/27 there is no wifi callback
            }
        });

    }
}
