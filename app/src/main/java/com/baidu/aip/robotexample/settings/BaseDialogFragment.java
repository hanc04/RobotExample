package com.baidu.aip.robotexample.settings;


import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialogFragment extends DialogFragment {

    public static double widthCoefficient = 0.8;
    public static double heightCoefficient = 0.8;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View root = inflater.inflate(getLayout(), container, false);
        setupView(root);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * widthCoefficient),
                    (int) (displayMetrics.heightPixels * heightCoefficient));
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    public abstract int getLayout();

    protected abstract void setupView(View root);
}