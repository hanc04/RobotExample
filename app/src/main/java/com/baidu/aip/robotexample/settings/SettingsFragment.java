package com.baidu.aip.robotexample.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidu.aip.robotexample.MainActivity;
import com.baidu.aip.robotexample.R;

import aip.baidu.com.robotsdk.RobotSDKEngine;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "SettingsFragment";

    private Preference devicePref;
    private Preference wifiPref;
    private Preference volumePref;
    private VolumeDialog volumeDialog;
    private EditTextPreference mapPref;
    private EditTextPreference originSpotPref;
    private SeekBarPreference seekBarPreference;
    private Preference cruiseListPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_setting, rootKey);
        devicePref = findPreference("device_id");
        wifiPref = findPreference("wifi_setting");
        volumePref = findPreference("volume_setting");
        volumePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                volumeDialog = (VolumeDialog) getActivity()
                        .getSupportFragmentManager().findFragmentByTag("volume");
                if (volumeDialog == null) {
                    volumeDialog = new VolumeDialog();
                }
                volumeDialog.show(getActivity().getSupportFragmentManager(), "volume");
                return true;
            }
        });

        mapPref = findPreference("map_setting");
        mapPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                mapPref.setTitle(editText.getText());

            }
        });
        originSpotPref = findPreference("origin_spot_setting");
        originSpotPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                originSpotPref.setTitle(editText.getText());
            }
        });
        cruiseListPref = findPreference("cruise_locations_setting");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        devicePref.setTitle(RobotSDKEngine.getInstance().getSerialNumber());
        wifiPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WifiSettingFragment wifiSettingFragment = (WifiSettingFragment) getActivity()
                        .getSupportFragmentManager().findFragmentByTag("wifi");
                if (wifiSettingFragment == null) {
                    wifiSettingFragment = new WifiSettingFragment();
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.setting_container, wifiSettingFragment)
                        .hide(SettingsFragment.this)
                        .addToBackStack("settings")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                return true;
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}
