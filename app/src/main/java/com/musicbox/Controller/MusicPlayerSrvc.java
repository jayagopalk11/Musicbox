package com.musicbox.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.musicbox.View.NowPlaying;

import java.io.IOException;

public class MusicPlayerSrvc extends Service {
    public MediaPlayer mp = new MediaPlayer();
    public boolean isPlaying=false;
    public boolean isPaused = false;
    private final IBinder musicBinder = new customLocalBinder();

    public MusicPlayerSrvc() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }


    public class customLocalBinder extends Binder{
        public MusicPlayerSrvc getService(){
            return MusicPlayerSrvc.this;
        }
    }

    public void playMusic(){
        Log.i("flag status: ",String.valueOf(isPlaying));

        if(!isPlaying) {
            if(!isPaused) {

                try {
                    mp.prepare();
                    Log.i("TAG", "mp prepared");
                } catch (IOException x) {
                    Log.i("TAG", "Exception handled" + x.toString());
                }
            }
        }
        mp.start();

        Toast.makeText(getApplication().getApplicationContext(),String.valueOf(mp.getDuration()),Toast.LENGTH_SHORT).show();
        Log.i("TAG", "mp started");
        isPlaying = true;
        NowPlaying.play.setVisibility(View.INVISIBLE);
        NowPlaying.pause.setVisibility(View.VISIBLE);

    }

    public void pauseMusic(){
        Log.i("pause status : ",String.valueOf(mp.isPlaying()));
        if(mp.isPlaying() ) {
            mp.pause();
            Log.i("TAG", "mp paused");
            isPlaying=false;
            isPaused=true;

            NowPlaying.play.setVisibility(View.VISIBLE);
            NowPlaying.pause.setVisibility(View.INVISIBLE);
        }

    }

    public void setMpResourcePath(String path){
        try {
            mp.setDataSource(path);
        } catch (IOException x) {
            Log.i("TAG", "Exception handled" + x.toString());
        }

    }

    public void reset(){
        mp.reset();
    }

    public boolean isPlaying(){
        return isPlaying;
    }

}
