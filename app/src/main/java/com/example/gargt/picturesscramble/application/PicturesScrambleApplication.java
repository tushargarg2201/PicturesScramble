package com.example.gargt.picturesscramble.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by gargt on 12/9/16.
 */

public class PicturesScrambleApplication extends Application {
    private static PicturesScrambleApplication mInstance = null;

    public PicturesScrambleApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext() {
        if (mInstance != null) {
            return mInstance.getApplicationContext();
        }
        return null;

    }


}
