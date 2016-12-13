package com.example.gargt.picturesscramble.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.gargt.picturesscramble.application.PicturesScrambleApplication;

/**
 * Created by gargt on 12/7/16.
 */

public class AppUtils {

    public static boolean isNetworkAvailable() {
        Context context = PicturesScrambleApplication.getAppContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
