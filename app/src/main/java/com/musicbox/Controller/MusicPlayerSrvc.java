package com.musicbox.Controller;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.musicbox.View.NowPlaying;

import java.io.IOException;

public class MusicPlayerSrvc extends Service {
    public MediaPlayer mp = new MediaPlayer();
    public boolean isPlaying=false;
    public boolean isPaused = false;
    private final IBinder musicBinder = new customLocalBinder();
    private Boolean success;

    public MusicPlayerSrvc() {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("ONCOMPLETE","Running");
                mp.pause();
                //mp.stop();
                mp.reset();
                setMpResourcePath();
                isPaused = false;
                isPlaying = false;
                NowPlaying.play.setVisibility(View.VISIBLE);
                NowPlaying.pause.setVisibility(View.INVISIBLE);
                NowPlaying.seeker.setProgress(0);

            }
        });

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
                } catch (Exception x) {
                    Log.i("TAG", "Exception handled" + x.toString());
                }
            }
        }
        mp.start();

        //Toast.makeText(getApplication().getApplicationContext(),String.valueOf(mp.getDuration()),Toast.LENGTH_SHORT).show();
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

    public void setMpResourcePath(){
        try {
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.valueOf(NowPlaying.wItem.getId()));

            mp.setDataSource(this, trackUri);
            success = true;
        }catch (Exception e){
            Log.i("Exception","Exception handled"+e.toString());
        }
        if(!success) {
            try {
                Uri trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        Long.valueOf(NowPlaying.wItem.getId()));

                mp.setDataSource(this, trackUri);
            } catch (Exception e) {
                Log.i("Exception", "Exception handled" + e.toString());
            }
        }

    }



    public void reset(){
        mp.reset();
    }

    public boolean isPlaying(){
        return isPlaying;
    }

}
