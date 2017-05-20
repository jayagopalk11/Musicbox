package com.musicbox.Controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.songItem;
import com.musicbox.R;
import com.musicbox.View.NowPlaying;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean songsListed;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingProgress = (ProgressBar) findViewById(R.id.loading);
        loadingProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        loadingProgress.setVisibility(View.VISIBLE);
        loadingProgress.setIndeterminate(true);


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission to read Ext storage denied", Toast.LENGTH_LONG).show();
            Log.i("PERMISSION", "Permission to record denied");
        }


        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        songsListed = false;

        new backgroundWork().execute();
    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void getPlaylistOperation(){

        if(!doesDatabaseExist(this,"MusicBoxPlaylist5.db")) {

            final playlistGenerator playlistGenerator = new playlistGenerator(this);
            while(!songsListed){
                //
            }

        }else{

        }

    }

    public class backgroundWork extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPlaylistOperation();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingProgress.setVisibility(View.INVISIBLE);
            Intent nowPlaying = new Intent(MainActivity.this,NowPlaying.class);
            startActivity(nowPlaying);
            finish();
            super.onPostExecute(aVoid);
        }
    }
}
