package com.baidu.aip.robotexample.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.aip.robotexample.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

public class WifiSettingFragment extends PreferenceFragmentCompat {

    private static final String TAG = "WifiSettingFragment";
    private Context mContext;
    private Preference wifiPref;
    private PreferenceCategory wifiListCate;
    private WifiManager mWifiManager;
    private WifiReceiver mReiceiver;
    private List<ScanResult> mWifiList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_wifi, rootKey);
        wifiPref = findPreference("current_wifi");
        wifiListCate = findPreference("wifi_list_category");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        startListeningWifiInfo();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        stopListeningWifiInfo();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ((FragmentActivity) mContext)
                    .getSupportFragmentManager()
                    .popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startListeningWifiInfo(){
        String wifiServiceName = Context.WIFI_SERVICE;
        mReiceiver = new WifiReceiver();
        mWifiManager = (WifiManager) getActivity().getSystemService(wifiServiceName);
        mContext.registerReceiver(mReiceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    private void stopListeningWifiInfo() {
        mContext.unregisterReceiver(mReiceiver);
        mReiceiver = null;
        mWifiManager = null;
    }

    class WifiReceiver extends BroadcastReceiver {

        public static final String WIFI_AUTH_OPEN = "";
        public static final String WIFI_AUTH_ROAM = "[ESS]";

        @Override
        public void onReceive(Context c, Intent intent) {
            if (mContext != null) {
                wifiListCate.setTitle("附近的 WLAN 网络");
                mWifiList = filterScanResult(mWifiManager.getScanResults());
                wifiListCate.removeAll();
                for (ScanResult result : mWifiList) {
                    Log.d(TAG, "onReceive: signal level:" + result.level);
                    Preference wifiItemPref = getPreference(result);
                    wifiListCate.addPreference(wifiItemPref);
                }
            }
        }

        private Preference getPreference(ScanResult result) {
            Preference wifiItemPref = new Preference(mContext);
            wifiItemPref.setTitle(result.SSID);
            int level = mWifiManager.calculateSignalLevel(result.level, 4);
            String signalDesc = "信号";
            int signalIcon = 0;
            switch (level) {
                case 0:
                    signalDesc += "微弱";
                    if (isEncrypted(result)) {
                        signalIcon = R.mipmap.baseline_signal_wifi_1_bar_lock;
                    } else {
                        signalIcon = R.mipmap.baseline_signal_wifi_1_bar;
                    }
                    break;
                case 1:
                    signalDesc += "差";
                    if (isEncrypted(result)) {
                        signalIcon = R.mipmap.baseline_signal_wifi_2_bar_lock;
                    } else {
                        signalIcon = R.mipmap.baseline_signal_wifi_2_bar;
                    }
                    break;
                case 2:
                    signalDesc += "良";
                    if (isEncrypted(result)) {
                        signalIcon = R.mipmap.baseline_signal_wifi_3_bar_lock;
                    } else {
                        signalIcon = R.mipmap.baseline_signal_wifi_3_bar;
                    }
                    break;
                case 3:
                    signalDesc += "好";
                    if (isEncrypted(result)) {
                        signalIcon = R.mipmap.baseline_signal_wifi_4_bar_lock;
                    } else {
                        signalIcon = R.mipmap.baseline_signal_wifi_4_bar;
                    }
                    break;
                default:
                    break;
            }
            wifiItemPref.setSummary(signalDesc);
            wifiItemPref.setIcon(signalIcon);
            return wifiItemPref;
        }

        private List<ScanResult> filterScanResult(final List<ScanResult> list) {
            LinkedHashMap<String, ScanResult> linkedMap = new LinkedHashMap<>(list.size());
            for (ScanResult rst : list) {
                if ("".equals(rst.SSID)) {
                    continue;
                }
                if (linkedMap.containsKey(rst.SSID)) {
                    if (rst.level > linkedMap.get(rst.SSID).level) {
                        linkedMap.put(rst.SSID, rst);
                    }
                    continue;
                }
                linkedMap.put(rst.SSID, rst);
            }
            list.clear();
            list.addAll(linkedMap.values());
            Collections.sort(list, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult o1, ScanResult o2) {
                    int levelDelta = o1.level - o2.level;
                    if (levelDelta != 0) {
                        return -levelDelta;
                    } else {
                        return -o1.SSID.compareTo(o2.SSID);
                    }
                }
            });
            return list;
        }

        private boolean isEncrypted(ScanResult sr) {
            if (sr.capabilities != null) {
                String capabilities = sr.capabilities.trim();
                if (capabilities != null && (capabilities.equals(WIFI_AUTH_OPEN) || capabilities.equals(WIFI_AUTH_ROAM))) {
                    return false;
                }
            }
            return true;
        }
    }
}
