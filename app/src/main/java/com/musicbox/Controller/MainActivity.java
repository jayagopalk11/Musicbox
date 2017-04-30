package com.musicbox.Controller;

import android.Manifest;
import android.content.ContentResolver;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        songsSqlHandler sqlActivity;
        sqlActivity = new songsSqlHandler(this, null, null, 1);



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



        ArrayList<songItem> songList = new ArrayList<songItem>();

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int a = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int b = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int c = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int d = musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_RINGTONE);
            int e = musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_NOTIFICATION);
            int f = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int g = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            Log.i("ALBUM ID: ",String.valueOf(f));


            do{
                Log.i("log ","inside do loop");
                long id = musicCursor.getLong(b);
                Log.i("log ","long value");
                Log.i("log ",String.valueOf(id));
                Log.i("position of duration :",String.valueOf(g));
                String title = musicCursor.getString(a);
                String artist = musicCursor.getString(c);
                String albumId = musicCursor.getString(f);
                String ringtone = musicCursor.getString(d);
                String notific = musicCursor.getString(e);
                //Log.i("duration",musicCursor.getString(g).toString());
                String duration =  milliToMinutes((musicCursor.getString(g))!=null?musicCursor.getString(g):"0");

                //String duration = milliToMinutes("10000");
                String albumPath = "";
                if(ringtone.contains("0") && notific.contains("0")){


                    //album art retriever
                    Uri myUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                    Cursor myCursor = getContentResolver().query(myUri,new String[] {MediaStore.Audio.Albums._ID,
                                    MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {musicCursor.getString(f)},
                            null);
                    Log.i("query stat: ","queried");
                    if(myCursor!=null && myCursor.moveToFirst()){

                        int x = myCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                        //do {

                        albumPath = myCursor.getString(x);
                        //}while(myCursor.moveToNext());
                    }
                    myCursor.close();
                    //end of album art retriever


                    Log.i("ringtone type: ",ringtone.getClass().getName());
                    Log.i("Song List: ", String.valueOf(id) + " - " + title + " - " + artist);
                    Log.i("Is ringtone: ",ringtone);

                    songList.add(new songItem(title,artist,duration,String.valueOf(id),albumPath,albumId,"0"));
                    Log.i("Songs count: ", String.valueOf(songList.size()));
                    try {
                        sqlActivity.addSongItem(new songItem(title, artist, duration, String.valueOf(id), albumPath,albumId,"0"));
                    }catch (Exception x){

                    }


                }
            }while(musicCursor.moveToNext());
        }
        musicCursor.close();
        musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        musicCursor = musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int a = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int b = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int c = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int d = musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_RINGTONE);
            int e = musicCursor.getColumnIndex(MediaStore.Audio.Media.IS_NOTIFICATION);
            int f = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int g = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            Log.i("ALBUM ID: ",String.valueOf(f));

            do{
                long id = musicCursor.getLong(b);
                String title = musicCursor.getString(a);
                String artist = musicCursor.getString(c);
                String duration = milliToMinutes(musicCursor.getString(g)!=null?musicCursor.getString(g):"0");
                String albumId = musicCursor.getString(f);
                String ringtone = musicCursor.getString(d)!=null?musicCursor.getString(d):"1";
                String notific = musicCursor.getString(e);
                String albumPath = "";

                if(ringtone.contains("0")&&notific.contains("0")) {

                    //album art retriever
                    Uri myUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
                    Cursor myCursor = getContentResolver().query(myUri,new String[] {MediaStore.Audio.Albums._ID,
                                    MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {musicCursor.getString(f)},
                            null);
                    Log.i("query stat: ","queried");
                    if(myCursor!=null && myCursor.moveToFirst()){

                        int x = myCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                        albumPath = myCursor.getString(x);

                    }
                    myCursor.close();
                    //end of album art retriever



                    Log.i("Song List: ", String.valueOf(id) + " - " + title + " - " + artist);
                    songList.add(new songItem(title,artist,duration,String.valueOf(id),albumPath,albumId,"0"));
                    Log.i("Songs count: ", String.valueOf(songList.size()));
                    try {
                        sqlActivity.addSongItem(new songItem(title, artist, duration, String.valueOf(id), albumPath,albumId,"0"));
                    }catch (Exception x){

                    }
                }
            }while(musicCursor.moveToNext());
        }
        musicCursor.close();

        Log.i("RESULT COMING UP: ",songList.toString());


    }


    private String milliToMinutes(String duration){
        Log.i("duration is ",duration);
        long length = Long.parseLong(duration);
        length = length/1000;
        String seconds;
        String mins;

        if((length%60)<10){
            seconds = "0"+String.valueOf(length%60);
        }else{
            seconds = String.valueOf(length%60);
        }

        if((length/60)<10){
            mins = "0"+String.valueOf(length/60);
        }else{
            mins = String.valueOf(length/60);
        }

        return mins+":"+seconds;
    }
}
