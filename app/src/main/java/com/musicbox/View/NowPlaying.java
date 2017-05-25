package com.musicbox.View;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.musicbox.Controller.CircularSeek;
import com.musicbox.Controller.MusicPlayerSrvc;
import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.albumArtistItem;
import com.musicbox.Model.songItem;
import com.musicbox.R;

import java.util.ArrayList;

import static com.musicbox.View.Equaliser.enabler;
import static com.musicbox.View.Equaliser.presetValues;


public class NowPlaying extends Activity{



    public static ArrayList<songItem> allSongsList;
    public static ArrayList<albumArtistItem> allAlbumList;
    public static ArrayList<albumArtistItem> allArtistList;
    public static ArrayList<songItem> albumList;
    public static ArrayList<songItem> artistList;

    public static Boolean songsFlag;
    public static Boolean albumsFlag;
    public static Boolean artistsFlag;

    Intent musicPlayerSrvc;

    public static ImageButton play;
    public static ImageButton pause;
    public ImageButton nextSong;
    public ImageButton prevSong;
    public static TextView songName;
    public static TextView albumName;
    MusicPlayerSrvc playerService;
    Boolean isBound = false;
    public static CircularSeek seeker;
    public MusicPlayerSrvc musicPlayerService;
    public ImageView myImage;
    private boolean success = false;
    public static songItem wItem;
    Runnable run;
    public static TextView runTime;
    public static TextView leftTime;
    public boolean initialLoad;
    public static int mySongIndex;


    Handler seekHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        //wItem = new songItem(null,null,null,null,null,null,null,null);
        songsSqlHandler sqlActivity;
        sqlActivity = new songsSqlHandler(this, null, null, 1);
        allSongsList = sqlActivity.getAllSongs();
        allAlbumList = sqlActivity.getAllAlbums();
        allArtistList = sqlActivity.getAllArtists();
        songName = (TextView)findViewById(R.id.songName);
        albumName = (TextView)findViewById(R.id.albumName);
        play = (ImageButton)findViewById(R.id.playButton);
        pause = (ImageButton)findViewById(R.id.pauseButton);
        nextSong = (ImageButton)findViewById(R.id.nextButton);
        prevSong = (ImageButton)findViewById(R.id.previousButton);
        runTime = (TextView)findViewById(R.id.runTime);
        leftTime = (TextView)findViewById(R.id.leftTime);
        sqlActivity = new songsSqlHandler(this, null, null, 1);
        initialLoad = true;


        SharedPreferences sharedPreferences = getSharedPreferences("MusicBox",Context.MODE_PRIVATE);
        String songId = sharedPreferences.getString("songIndex","");
        Log.i("SHAREPREF>>>>>>>>>>>>",songId);
        if(!songId.equals("")) {

            mySongIndex = getCategoryPos(songId);
            Log.i("song index ",String.valueOf(mySongIndex));
            wItem = allSongsList.get(mySongIndex);
            //wItem = sqlActivity.getSongItem(songId);
        }else{
            mySongIndex = 0;
            wItem = sqlActivity.getFirstSong();
        }
        songName.setText(wItem.getTitle());
        albumName.setText(wItem.getAlbum());


        musicPlayerSrvc = new Intent(this,MusicPlayerSrvc.class);
        startService(musicPlayerSrvc);
        bindService(musicPlayerSrvc ,musicBoxConnection, Context.BIND_AUTO_CREATE);

        myImage = (ImageView) findViewById(R.id.albumArtImg);

        //Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.music_image);

        //Bitmap myBitmap = getCircleBitmap(image);
        //myImage.setImageBitmap(myBitmap);
        drawAlbumArt();
        seeker = (CircularSeek) findViewById(R.id.cis);
        seeker.setStartAngle(30);
        seeker.setSweepAngle(295);
        seeker.setMax(1000);
        seeker.setTouchInSide(true);

        seeker.setOnSeekArcChangeListener(new CircularSeek.OnCircleSeekChangeListener() {
            @Override
            public void onProgressChanged(CircularSeek seeker, int progress, boolean fromUser) {
                //Log.i("Progress ###",String.valueOf(progress));
                if(fromUser) {
                    //if(musicPlayerService.mp.isPlaying()){
                        int curDuration = musicPlayerService.mp.getDuration();
                        Log.i("CurrentDuration",String.valueOf(curDuration));
                        int val = (curDuration/1000)*progress;
                        Log.i("ProgressVal",String.valueOf(val));
                        if(musicPlayerService.mp.isPlaying()) {
                            musicPlayerService.mp.pause();
                            musicPlayerService.mp.seekTo(val);
                            musicPlayerService.mp.start();
                            play.setVisibility(View.INVISIBLE);
                            pause.setVisibility(View.VISIBLE);
                        }else{
                            musicPlayerService.mp.pause();
                            musicPlayerService.mp.seekTo(val);
                            play.setVisibility(View.VISIBLE);
                            pause.setVisibility(View.INVISIBLE);
                        }
                    //}
                }
            }

            @Override
            public void onStartTrackingTouch(CircularSeek seeker) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeek seeker) {
                //Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_SHORT).show();
            }
        });

        run = new Runnable() {
            @Override
            public void run() {
                //Log.i("seekbar", "runnable intialised");
                seekUpdation();
            }
        };

        SharedPreferences sharedPref = this.getSharedPreferences("Music",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("songIndex",wItem.getId());
        editor.apply();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap sbmp;
        int radius = (bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth():bitmap.getHeight());


        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            float smallest = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() / factor), (int)(bitmap.getHeight() / factor), false);
        } else {
            sbmp = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    public void playTrigger(View view){

        SharedPreferences sharedPref = this.getSharedPreferences("MusicBox",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("songIndex",wItem.getId());
        editor.apply();

        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        musicPlayerService.playMusic();

        //Toast.makeText(this,"thread strt",Toast.LENGTH_SHORT).show();

        run = new Runnable() {
            @Override
            public void run() {
                //Log.i("seekbar", "runnable intialised");
                seekUpdation();
            }
        };
        drawAlbumArt();
        seekUpdation();
    }

    public void pauseTrigger(View view){
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        musicPlayerService.pauseMusic();
    }

    public void nextTrigger(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("MusicBox",Context.MODE_PRIVATE);
        String songId = sharedPreferences.getString("songIndex","");
        Log.i("SHAREPREF>>>>>>>>>>>>",songId);
        if(!songId.equals("")) {
            mySongIndex = getCategoryPos(songId);
            //wItem = allSongsList.get(mySongIndex);
            //wItem = sqlActivity.getSongItem(songId);
        }else{
            mySongIndex = 0;
            //wItem = allSongsList.get(mySongIndex);
        }
        if(mySongIndex < allSongsList.size()) {
            mySongIndex += 1;
        }else{

        }
        wItem = allSongsList.get(mySongIndex);
        Log.i("song new index ",allSongsList.get(mySongIndex).getId());
        songName.setText(wItem.getTitle());
        albumName.setText(wItem.getAlbum());
        Log.i("NEW SONG",wItem.getTitle());
        firstPlay();
        playTrigger(view);
    }

    public void prevTrigger(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("MusicBox",Context.MODE_PRIVATE);
        String songId = sharedPreferences.getString("songIndex","");
        Log.i("SHAREPREF>>>>>>>>>>>>",songId);
        if(!songId.equals("")) {
            mySongIndex = getCategoryPos(songId);
            //wItem = allSongsList.get(mySongIndex);
        }else{
            mySongIndex = 0;
            //wItem = allSongsList.get(mySongIndex);
        }

        if(mySongIndex <= 0){

        }else {
            mySongIndex -= 1;
        }
        wItem = allSongsList.get(mySongIndex);
        Log.i("song new index ",allSongsList.get(mySongIndex).getId());
        songName.setText(wItem.getTitle());
        albumName.setText(wItem.getAlbum());
        Log.i("NEW SONG",wItem.getTitle());
        firstPlay();
        playTrigger(view);
    }

    public void seekUpdation(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                float value = ((float) musicPlayerService.mp.getCurrentPosition()/(float) musicPlayerService.mp.getDuration());
                //seeker.setProgress(musicPlayerService.mp.getCurrentPosition());
                //Log.i("seekbar now", String.valueOf(musicPlayerService.mp.getCurrentPosition()));
                //Log.i("seekbar total", String.valueOf(musicPlayerService.mp.getDuration()));
                //Log.i("seekbar div", String.valueOf(value));


                int progress = (int)(value * 1000);
                //Log.i("seekbar", String.valueOf(progress));
                seeker.setProgress(progress);
                runTime.setText(convertMilli(musicPlayerService.mp.getCurrentPosition()));

                leftTime.setText(convertMilli(musicPlayerService.mp.getDuration()-musicPlayerService.mp.getCurrentPosition()));
            }
        });


        if(musicPlayerService.mp.isPlaying()) {
            seekHandler.sendEmptyMessage(0);
            seekHandler.postDelayed(run, 100);
        }
    }

    public String convertMilli(int milli){
        milli = milli / 1000;
        milli = Math.round(milli);

        int mins = milli/60;
        int secs = milli%60;
        String min = "";
        String sec = "";
        if((mins/10)<1){
            min = "0"+String.valueOf(mins);
        }else{
            min = String.valueOf(mins);
        }
        if((secs/10)<1){
            sec = "0"+String.valueOf(secs);
        }else{
            sec = String.valueOf(secs);
        }

        return min+":"+sec;
    }

    ServiceConnection musicBoxConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicPlayerSrvc.customLocalBinder binder = (MusicPlayerSrvc.customLocalBinder) iBinder;
            musicPlayerService = binder.getService();
            isBound = true;

            Log.i("Binder log: ","Successfully binded");
            Log.i("play status: ",String.valueOf(musicPlayerService.isPlaying()));
            if(musicPlayerService.isPlaying()){
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                seeker.setProgress(musicPlayerService.mp.getCurrentPosition());
                //Toast.makeText(getApplication().getApplicationContext(),String.valueOf(musicPlayerService.mp.getDuration()),Toast.LENGTH_SHORT).show();

            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }
            if(musicPlayerService.mp.isPlaying()){
                initialLoad = false;
                drawAlbumArt();
                seekUpdation();
            }
            musicPlayerService.mp.reset();
            firstPlay();

            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if(audio.isWiredHeadsetOn()){
                Toast.makeText(getApplicationContext(),"Headset On",Toast.LENGTH_SHORT).show();
                enabler.setEnabled(true);
                presetValues.setEnabled(true);

            }else{
                Toast.makeText(getApplicationContext(),"Headset Off",Toast.LENGTH_SHORT).show();
                enabler.setEnabled(false);
                presetValues.setEnabled(false);
            }

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

    private void drawAlbumArt(){
        Bitmap image;
        try{
             image = BitmapFactory.decodeFile(wItem.getAlbumArt());
            if((image.getHeight() > 0)&&(image.getWidth()>0)){

            }
        }catch (Exception e){

            image = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.music_image);

        }

        try{
            Bitmap myBitmap = getCircleBitmap(image);
            myImage.setImageBitmap(myBitmap);
        }catch (Exception e){
            Toast.makeText(this,"Unable to set album art",Toast.LENGTH_SHORT).show();
            image = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.music_image);
            Bitmap myBitmap = getCircleBitmap(image);
            myImage.setImageBitmap(myBitmap);
        }
    }

    public void firstPlay(){

        musicPlayerService.mp.pause();

        Log.i("mp stat","mp paused from playlist");
        //musicPlayerService.mp.stop();
        //Log.i("mp stat","mp stopped from playlist");
        musicPlayerService.mp.reset();
        //Log.i("mp stat","mp reset from playlist");
        musicPlayerService.isPaused = false;
        musicPlayerService.isPlaying = false;


        try {
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.valueOf(wItem.getId()));

            musicPlayerService.mp.setDataSource(this, trackUri);
            success = true;
        }catch (Exception e){
            Log.i("Exception","Exception handled"+e.toString());
        }
        if(!success) {
            try {
                Uri trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        Long.valueOf(wItem.getId()));

                musicPlayerService.mp.setDataSource(this, trackUri);
            } catch (Exception e) {
                Log.i("Exception", "Exception handled" + e.toString());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicBoxConnection);

    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayerSrvc = new Intent(this,MusicPlayerSrvc.class);
        bindService(musicPlayerSrvc ,musicBoxConnection, Context.BIND_AUTO_CREATE);
        Log.i("ON RESUME",">>>>>>>>>>>>>>");
        Log.i("LOG STATUS",String.valueOf(initialLoad));
        if(!initialLoad){
            drawAlbumArt();
            seekUpdation();
        }

        initialLoad = false;

    }

    private int getCategoryPos(String id) {
        Log.i("passed ID",id);
        Log.i("Total lenght",String.valueOf(allSongsList.size()));

        for (int i = 0; i < allSongsList.size() ; i++) {
            if (allSongsList.get(i).getId().equals(id)) {
                return i;
            }

        }

        return -1;
    }

    public void openEq(View view){
        Intent eq = new Intent(this,Equaliser.class);
        startActivity(eq);
    }
}
