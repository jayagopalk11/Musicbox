package com.musicbox.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.musicbox.Controller.MainActivity;
import com.musicbox.Controller.MusicPlayerSrvc;
import com.musicbox.Controller.playlistGenerator;
import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.songItem;
import com.musicbox.R;

import java.util.ArrayList;

public class NowPlaying extends Activity{

    public static ArrayList<songItem> allSongsList;
    public static ArrayList<songItem> allAlbumList;
    private ImageButton play;
    private ImageButton pause;
    private ImageButton nextSong;
    private ImageButton prevSong;
    MusicPlayerSrvc playerService;
    Boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        songsSqlHandler sqlActivity;
        sqlActivity = new songsSqlHandler(this, null, null, 1);
        allSongsList = sqlActivity.getAllSongs();
        //allAlbumList = sqlActivity.getAllAlbums();
        play = (ImageButton)findViewById(R.id.playButton);
        pause = (ImageButton)findViewById(R.id.pauseButton);
        nextSong = (ImageButton)findViewById(R.id.nextButton);
        prevSong = (ImageButton)findViewById(R.id.previousButton);

        Intent musicPlayerSrvc = new Intent(this,MusicPlayerSrvc.class);
        bindService(musicPlayerSrvc,musicBoxConnection, Context.BIND_AUTO_CREATE);

    }

    public void playTrigger(View view){
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
    }

    public void pauseTrigger(View view){
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
    }

    private ServiceConnection musicBoxConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerSrvc.customLocalBinder binder = (MusicPlayerSrvc.customLocalBinder) iBinder;
            playerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    public void openLibrary(View view){
        Intent lib = new Intent(this,MusicLibrary.class);
        startActivity(lib);
    }

}
