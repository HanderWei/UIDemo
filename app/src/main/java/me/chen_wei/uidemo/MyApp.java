package me.chen_wei.uidemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * author: Chen Wei time: 16/11/23 desc: 描述
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
