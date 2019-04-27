package com.baidu.aip.robotexample.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties getProperties(Context context) {
        Properties config = new Properties();
        try {
            InputStream is = context.getAssets().open("config.properties");
            config.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return config;
    }
}
