package com.example.gargt.picturesscramble.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.gargt.picturesscramble.application.PicturesScrambleApplication;
import com.example.gargt.picturesscramble.model.Photo;
import com.example.gargt.picturesscramble.model.PhotoSet;
import com.example.gargt.picturesscramble.model.Size;
import com.example.gargt.picturesscramble.model.Sizes;
import com.example.gargt.picturesscramble.parser.PhotoSetJson;
import com.example.gargt.picturesscramble.parser.PhotosJSON;
import com.example.gargt.picturesscramble.utils.AppConstants;
import com.example.gargt.picturesscramble.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gargt on 12/9/16.
 */

public class NetworkFactory {
    private Context mContext;
    private Handler mHandler;

    public NetworkFactory(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    ArrayList<String> mPhotoUrlList = new ArrayList<>();
    public void setUrlList(ArrayList<String> mPhotoUrlList) {
        mPhotoUrlList = mPhotoUrlList;
    }
    public List<String> getPhotoUrlList() {
        return mPhotoUrlList;
    }



    public void executeRequest() {
        String endPointUrl = getUrl();
        if (TextUtils.isEmpty(endPointUrl)) {
            return;
        }
        if (!AppUtils.isNetworkAvailable()) {
            Toast.makeText(PicturesScrambleApplication.getAppContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        mPhotoUrlList.clear();
        final GsonRequest gsonRequest = new GsonRequest(endPointUrl, PhotoSetJson.class, null, new Response.Listener<PhotoSetJson>() {

            @Override
            public void onResponse(PhotoSetJson jsonResponse) {
                  if (jsonResponse != null ) {
                      PhotoSet photoSet = jsonResponse.getPhotoset();
                      if (photoSet != null) {
                          List<Photo> photos = photoSet.getPhoto();
                          if (photos != null && photos.size() > 0 ){
                              int count = 0;
                              for (Photo photo : photos) {
                                  if (count < AppConstants.GRID_SIZE) {
                                      String photo_id = photo.getId();
                                      String photoUrl = getPhotoUrl(AppConstants.flickr_photos_getSizes_base_url, photo_id);
                                      count++;
                                      executePhotosRequest(photoUrl);
                                  }


                              }
                          }
                      }

                  }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PicturesScrambleApplication.getAppContext(), "There is network error in 1st Request", Toast.LENGTH_SHORT).show();
                if(volleyError != null) Log.e("MainActivity", volleyError.getMessage());
            }
        });




        VolleyHelper.getInstance(PicturesScrambleApplication.getAppContext()).addToRequestQueue(gsonRequest);


    }

    private void executePhotosRequest(String endPointUrl) {
        if (!AppUtils.isNetworkAvailable()) {
            Toast.makeText(PicturesScrambleApplication.getAppContext(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        final GsonRequest gsonRequest = new GsonRequest(endPointUrl, PhotosJSON.class, null, new Response.Listener<PhotosJSON>() {

            @Override
            public void onResponse(PhotosJSON jsonResponse) {
                if (jsonResponse != null ) {
                    Sizes photoSize = jsonResponse.getSizes();
                    if (photoSize != null) {
                        List<Size> listSize = photoSize.getSize();
                        if (listSize != null && listSize.size() >0 ){
                            String source = listSize.get(4).getSource();
                            mPhotoUrlList.add(source);
                            if (mPhotoUrlList != null && mPhotoUrlList.size() >= AppConstants.GRID_SIZE) {
                                setUrlList(mPhotoUrlList);
                                Intent intent = new Intent(AppConstants.CUSTOM_EVENT_NAME);
                                intent.putExtra(AppConstants.URL_LIST, mPhotoUrlList);
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                            }

                        }
                    }

                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PicturesScrambleApplication.getAppContext(), "There is network error in Received PhotosRequest", Toast.LENGTH_SHORT).show();
                if(volleyError != null) Log.e("MainActivity", volleyError.getMessage());
            }
        });

        VolleyHelper.getInstance(PicturesScrambleApplication.getAppContext()).addToRequestQueue(gsonRequest);

    }

    private String getPhotoUrl(String flickr_photos_getSizes_base_url, String photo_id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(flickr_photos_getSizes_base_url);
        stringBuilder.append("&format=");
        stringBuilder.append(AppConstants.FLICKR_FORMAT);
        stringBuilder.append("&api_key=");
        stringBuilder.append(AppConstants.FLICKR_API_KEY);
        stringBuilder.append("&photo_id=");
        stringBuilder.append(photo_id);
        stringBuilder.append("&nojsoncallback=1");
        String endPointUrl = stringBuilder.toString();
        return endPointUrl;
    }

    public String  getUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AppConstants.flickr_photosets_getPhotos_base_url);
        stringBuilder.append("&format=");
        stringBuilder.append(AppConstants.FLICKR_FORMAT);
        stringBuilder.append("&api_key=");
        stringBuilder.append(AppConstants.FLICKR_API_KEY);
        stringBuilder.append("&photoset_id=");
        stringBuilder.append(AppConstants.PHOTOSET_ID);
        stringBuilder.append("&nojsoncallback=1");
        String endPointUrl = stringBuilder.toString();
        return endPointUrl;
    }
}

