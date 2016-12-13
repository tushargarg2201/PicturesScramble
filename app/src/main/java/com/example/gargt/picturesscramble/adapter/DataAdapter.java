package com.example.gargt.picturesscramble.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gargt.picturesscramble.AndroidVersion;
import com.example.gargt.picturesscramble.R;
import com.example.gargt.picturesscramble.network.VolleyHelper;
import com.example.gargt.picturesscramble.utils.AppConstants;

import java.util.List;

/**
 * Created by gargt on 12/9/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<AndroidVersion> android;
    private Context context;
    private ImageLoader imageLoader;
    private List<String> mOriginaUrlList;
    private List<String> mShuffleUrlList;
    private boolean mIsFlipOn = false;
    private NetworkImageView mFlipNetworkImageView;
    private DataAdapter mDataAdapter;
    private RecyclerView mRecyclerView;
    private String mShuffleUrl;
    private int mShuffleCount = 0;
    private DataAdpaterInterface mDataAdapterListener;
    private int mTriedCount = 0;
    private boolean mNotSelected = false;


    public DataAdapter(Context context) {
        this.context = context;
        imageLoader =  VolleyHelper.getInstance(context).getImageLoader();
    }

    public void setDataList(boolean isFlipOn, List<String> originalUrlList, List<String> shuffleUrlList, NetworkImageView flipNetworkImageView, DataAdapter dataAdapter, RecyclerView recyclerView, String shuffleUrl, DataAdpaterInterface dataAdpaterListener) {
        mIsFlipOn = isFlipOn;
        mOriginaUrlList = originalUrlList;
        mShuffleUrlList = shuffleUrlList;
        mFlipNetworkImageView = flipNetworkImageView;
        mDataAdapter = dataAdapter;
        mRecyclerView = recyclerView;
        mShuffleUrl = shuffleUrl;
        mDataAdapterListener = dataAdpaterListener;


    }

    public void resetData() {
        mShuffleCount = 0;
        mIsFlipOn = false;
        mTriedCount = 0;
        mNotSelected = false;
    }

    public interface DataAdpaterInterface {
        public void showAlertDialog(int count);

    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataAdapter.ViewHolder viewHolder, final int position) {
        if (mIsFlipOn) {
            if(mShuffleCount <= 0) {
                viewHolder.thumbNailNetworkImageView.setImageUrl(AppConstants.local_url, imageLoader);
            }

            if (mShuffleCount > 0 && mShuffleCount <= AppConstants.GRID_SIZE && !mNotSelected) {
                viewHolder.thumbNailNetworkImageView.setImageUrl(mOriginaUrlList.get(position), imageLoader);
                mOriginaUrlList.set(position, "-1");
            }
            if (!mOriginaUrlList.get(position).equals("-1") && mNotSelected) {
                viewHolder.thumbNailNetworkImageView.setImageUrl(AppConstants.local_url, imageLoader);
                mNotSelected = false;
            }

            if (mShuffleCount >= AppConstants.GRID_SIZE) {
                mIsFlipOn = false;
                if (mDataAdapterListener != null) {
                    mDataAdapterListener.showAlertDialog(mTriedCount);
                }
                return;
            }

            if (mIsFlipOn) {
                viewHolder.thumbNailNetworkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startClick(viewHolder, position);
                        return;
                    }
                });
            }

        } else {
            viewHolder.thumbNailNetworkImageView.setImageUrl(mOriginaUrlList.get(position), imageLoader);
        }
    }



    public void startClick(DataAdapter.ViewHolder viewHolder,  int position) {
        String originalUrl = mOriginaUrlList.get(position);
        if (originalUrl.equals("-1")) {
            return;
        }
        mRecyclerView.setSelected(true);
        if ( !TextUtils.isEmpty(mShuffleUrl) && originalUrl.equals(mShuffleUrl)) {
            viewHolder.thumbNailNetworkImageView.setImageUrl(originalUrl , imageLoader);
            //viewHolder.itemView.setBackgroundColor(Color.CYAN);
            viewHolder.itemView.setFocusable(true);
            mShuffleCount++;
            mTriedCount++;
            mNotSelected = false;
            if (mShuffleCount < AppConstants.GRID_SIZE) {
                mShuffleUrl = mShuffleUrlList.get(mShuffleCount);
                mFlipNetworkImageView.setImageUrl(mShuffleUrlList.get(mShuffleCount), imageLoader);
            }
            mRecyclerView.getAdapter().notifyItemChanged(position);
        } else {
            mNotSelected = true;
            mTriedCount++;
            mRecyclerView.getAdapter().notifyItemChanged(position);
        }
    }


    @Override
    public int getItemCount() {
        if (mOriginaUrlList != null && mOriginaUrlList.size() > 0) {
            return mOriginaUrlList.size();
        }
        return -1;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView thumbNailNetworkImageView;
        public ViewHolder(View view) {
            super(view);
            thumbNailNetworkImageView = (NetworkImageView) view.findViewById(R.id.image);

        }
    }
}