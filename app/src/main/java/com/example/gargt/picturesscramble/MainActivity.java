package com.example.gargt.picturesscramble;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gargt.picturesscramble.adapter.DataAdapter;
import com.example.gargt.picturesscramble.application.PicturesScrambleApplication;
import com.example.gargt.picturesscramble.network.NetworkFactory;
import com.example.gargt.picturesscramble.network.VolleyHelper;
import com.example.gargt.picturesscramble.utils.AppConstants;
import com.example.gargt.picturesscramble.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gargt on 12/9/16.
 */

public class MainActivity extends AppCompatActivity implements DataAdapter.DataAdpaterInterface {
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private NetworkImageView flipImageView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initViews();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(AppConstants.CUSTOM_EVENT_NAME));
        startNetworkRequest();


    }

    private void startNetworkRequest() {
        Handler handler = new Handler();
        NetworkFactory networkFactory = new NetworkFactory(this, handler);
        networkFactory.executeRequest();
    }

    private void initViews() {
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        flipImageView = (NetworkImageView) findViewById(R.id.flip_image);
        showProgressBar();
        adapter = new DataAdapter(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setLayoutManager(layoutManager);


    }

    private void showProgressBar() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        flipImageView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        if (pDialog != null) {
            pDialog.hide();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatemente
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final ArrayList<String> originalUrlList = intent.getStringArrayListExtra(AppConstants.URL_LIST);
            loadData(originalUrlList, context);
        }
    };

    private void loadData(final ArrayList<String> originalUrlList, Context context) {
        if (originalUrlList != null && originalUrlList.size() > 0) {
            Collections.shuffle(originalUrlList);
        }
        adapter.setDataList(false, originalUrlList, null, flipImageView, adapter, recyclerView, null, this);
        adapter.resetData();
        pDialog.hide();
        recyclerView.setAdapter(adapter);
        Toast.makeText(PicturesScrambleApplication.getAppContext(), R.string.shuffle_msg, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> shuffleUrlList = new ArrayList<String>();
                for (int i = 0; i< originalUrlList.size(); i++) {
                    shuffleUrlList.add(originalUrlList.get(i));
                }
                Collections.shuffle(shuffleUrlList);
                ImageLoader imageLoader =  VolleyHelper.getInstance(PicturesScrambleApplication.getAppContext()).getImageLoader();
                flipImageView.setVisibility(View.VISIBLE);
                flipImageView.setImageUrl(shuffleUrlList.get(0), imageLoader);
                adapter.setDataList(true, originalUrlList, shuffleUrlList, flipImageView, adapter, recyclerView, shuffleUrlList.get(0), MainActivity.this);
                recyclerView.getAdapter().notifyDataSetChanged();


            }
        }, 15000);
    }

    @Override
    public void showAlertDialog(int count) {
        //Toast.makeText(PicturesScrambleApplication.getAppContext(), "Show Dialog", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //String countMessage = String.valueOf(count)
        alertDialogBuilder.setMessage("Congrats, You have succesfully arranged the pictures, Your total attempt is "  + count +  " out of 36. " + " Do you want to try again?");
        alertDialogBuilder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showProgressBar();
                        startNetworkRequest();
                    }
                });

        alertDialogBuilder.setNegativeButton(
                R.string.exit,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        return;
                    }
                });

        alertDialogBuilder.setNeutralButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
