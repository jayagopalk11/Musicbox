package com.musicbox.View;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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



public class NowPlaying extends Activity{

    public static ArrayList<songItem> allSongsList;
    public static ArrayList<albumArtistItem> allAlbumList;
    public static ArrayList<albumArtistItem> allArtistList;

    Intent musicPlayerSrvc;

    public static ImageButton play;
    public static ImageButton pause;
    public ImageButton nextSong;
    public ImageButton prevSong;
    public static TextView songName;
    public static TextView albumName;
    MusicPlayerSrvc playerService;
    Boolean isBound = false;
    public CircularSeek seeker;
    public MusicPlayerSrvc musicPlayerService;
    public ImageView myImage;
    private boolean success = false;
    public static songItem wItem;
    Runnable run;
    public static TextView runTime;
    public static TextView leftTime;

    Handler seekHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            /*
            seeker.setProgress(60);

            //seeker.setProgress(musicPlayerService.mp.getCurrentPosition());
            Log.i("seekbar", String.valueOf(musicPlayerService.mp.getCurrentPosition()));
            Log.i("seekbar", "runnable running");
            runTime.setText(convertMilli(musicPlayerService.mp.getCurrentPosition()));

            leftTime.setText(convertMilli(musicPlayerService.mp.getDuration()-musicPlayerService.mp.getCurrentPosition()));
            */
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


        musicPlayerSrvc = new Intent(this,MusicPlayerSrvc.class);
        startService(musicPlayerSrvc);
        bindService(musicPlayerSrvc ,musicBoxConnection, Context.BIND_AUTO_CREATE);

        myImage = (ImageView) findViewById(R.id.albumArtImg);

        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.music_image);

        Bitmap myBitmap = getCircleBitmap(image);
        myImage.setImageBitmap(myBitmap);

        seeker = (CircularSeek) findViewById(R.id.cis);
        seeker.setStartAngle(30);
        seeker.setSweepAngle(295);
        seeker.setTouchInSide(true);

        seeker.setOnSeekArcChangeListener(new CircularSeek.OnCircleSeekChangeListener() {
            @Override
            public void onProgressChanged(CircularSeek seeker, int progress, boolean fromUser) {
                Log.i("Progress ###",String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(CircularSeek seeker) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeek seeker) {
                //Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_SHORT).show();
            }
        });

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
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);
        musicPlayerService.playMusic();

        /*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("seekbar", "runnable intialised");

                    seekUpdation();

            }
        });*/
        Toast.makeText(this,"thread strt",Toast.LENGTH_SHORT).show();

        run = new Runnable() {
            @Override
            public void run() {
                Log.i("seekbar", "runnable intialised");

                seekUpdation();
            }
        };
        seekUpdation();
    }




    public void pauseTrigger(View view){
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
        musicPlayerService.pauseMusic();
    }


    public void seekUpdation(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seeker.setProgress(60);
                float value = ((float) musicPlayerService.mp.getCurrentPosition()/(float) musicPlayerService.mp.getDuration());
                //seeker.setProgress(musicPlayerService.mp.getCurrentPosition());
                Log.i("seekbar now", String.valueOf(musicPlayerService.mp.getCurrentPosition()));
                Log.i("seekbar total", String.valueOf(musicPlayerService.mp.getDuration()));
                Log.i("seekbar div", String.valueOf(value));


                int progress = (int)(value * 100);
                Log.i("seekbar", String.valueOf(progress));
                seeker.setProgress(progress);
                runTime.setText(convertMilli(musicPlayerService.mp.getCurrentPosition()));

                leftTime.setText(convertMilli(musicPlayerService.mp.getDuration()-musicPlayerService.mp.getCurrentPosition()));
            }
        });

        seekHandler.sendEmptyMessage(0);
        seekHandler.postDelayed(run, 1000);
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
                Toast.makeText(getApplication().getApplicationContext(),String.valueOf(musicPlayerService.mp.getDuration()),Toast.LENGTH_SHORT).show();

            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);


                //playFunction();

            }

            //songName.setText(items[songId].items);
            //albumName.setText(items[songId].desc);
            //drawAlbumArt();
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
    /*
    private void drawAlbumArt(){
        Bitmap image = BitmapFactory.decodeFile(items[songId].arr);
        try{
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
        }
    }
        */
    public void playFunction(){
        try {
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.valueOf(wItem.getId()));

            musicPlayerService.mp.setDataSource(getApplicationContext(), trackUri);
            success = true;
        }catch (Exception e){
            Log.i("Exception","Exception handled"+e.toString());
        }
        if(!success) {
            try {
                Uri trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        Long.valueOf(wItem.getId()));

                musicPlayerService.mp.setDataSource(getApplicationContext(), trackUri);
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
    }
}
