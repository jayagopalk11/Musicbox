package com.musicbox.Controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.songItem;
import com.musicbox.R;
import com.musicbox.View.NowPlaying;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean songsListed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Toast.makeText(this,String.valueOf(doesDatabaseExist(this,"musics")),Toast.LENGTH_SHORT).show();

        if(!doesDatabaseExist(this,"music")) {
            final playlistGenerator playlistGenerator = new playlistGenerator(this);
            while(!songsListed){
                //
            }

        }else{

        }
        Intent nowPlaying = new Intent(this, NowPlaying.class);
        startActivity(nowPlaying);

    }
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}
