package com.example.gargt.picturesscramble.network;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.gargt.picturesscramble.application.PicturesScrambleApplication;
import com.example.gargt.picturesscramble.cache.LruBitmapCache;

/**
 * Created by gargt on 12/9/16.
 */

public class VolleyHelper extends Application {
    public static final String TAG = PicturesScrambleApplication.class.getSimpleName();

    private static VolleyHelper INSTANCE;
    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;
    private Context context;

    private VolleyHelper(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new VolleyHelper(context);
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.requestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void init() {
        getRequestQueue();
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
