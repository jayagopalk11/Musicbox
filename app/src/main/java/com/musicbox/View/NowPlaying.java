package com.musicbox.View;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;


import com.musicbox.Controller.MainActivity;
import com.musicbox.Controller.playlistGenerator;
import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.songItem;
import com.musicbox.R;

import java.util.ArrayList;

public class NowPlaying extends Activity{

    public ArrayList<songItem> allSongsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        songsSqlHandler sqlActivity;
        sqlActivity = new songsSqlHandler(this, null, null, 1);
        allSongsList = sqlActivity.getAllSongs();



    }





}
