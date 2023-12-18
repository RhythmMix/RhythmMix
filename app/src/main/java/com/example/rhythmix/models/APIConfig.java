package com.example.rhythmix.models;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIConfig {

    private static final String FILE_NAME = "config.properties";

    public static String getApiKey(Context context) {
        return getProperty(context, "api_key");
    }

    public static String getApiHost(Context context) {
        return getProperty(context, "api_host");
    }

    private static String getProperty(Context context, String key) {
        Properties properties = new Properties();
        try (InputStream inputStream = context.getResources().getAssets().open(FILE_NAME)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key, "");
    }
}
