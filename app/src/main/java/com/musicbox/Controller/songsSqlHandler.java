package com.musicbox.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.musicbox.Model.albumArtistItem;
import com.musicbox.Model.songItem;

import java.util.ArrayList;

/**
 * Created by Jai on 4/30/2017.
 */

public class songsSqlHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1 ;
    private static final String DATABASE_NAME = "MusicBoxPlaylist5.db";
    private static final String TABLE_SONGS = "SONGS_TABLE";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_ALBUMART = "albumArt";
    private static final String COLUMN_ALBUMID = "albumId";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_ALBUM = "album";

    public SQLiteDatabase database = getWritableDatabase();


    public songsSqlHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Query = "CREATE TABLE "+ TABLE_SONGS + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY ," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_ARTIST + " TEXT ," +
                COLUMN_DURATION + " TEXT ," +
                COLUMN_ALBUMART + " TEXT ," +
                COLUMN_ALBUMID + " TEXT ," +
                COLUMN_WEIGHT + " TEXT ," +
                COLUMN_ALBUM + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_SONGS);
        onCreate(sqLiteDatabase);
    }

    public void addSongItem(songItem songItem){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,songItem.getId());
        values.put(COLUMN_TITLE,songItem.getTitle());
        values.put(COLUMN_ARTIST,songItem.getArtist());
        values.put(COLUMN_DURATION,songItem.getDuration());
        values.put(COLUMN_ALBUMART,songItem.getAlbumArt());
        values.put(COLUMN_ALBUMID,songItem.getAlbumId());
        values.put(COLUMN_WEIGHT,songItem.getWeight());
        values.put(COLUMN_ALBUM, songItem.getAlbum());
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow(TABLE_SONGS, null, values);

        db.close();
    }

    public void deleteSongItem(songItem songItem){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SONGS + " WHERE "+ COLUMN_ID + "=\""+ songItem.getId() + "\"" + " AND "+
                COLUMN_TITLE + "=\""+ songItem.getTitle() + "\"" );
    }

    public ArrayList<songItem> getAllSongs(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        //String test = "";
        ArrayList<songItem> listData = new ArrayList<songItem>();
        cursor = db.rawQuery("SELECT * from "+TABLE_SONGS+" where 1",null);
        while (cursor.moveToNext()) {
            songItem temp = new songItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6), cursor.getString(7));

            //test += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+
                    //cursor.getString(3) + " \n";

            listData.add(temp);

        }
        cursor.close();
        return listData;
    }


    public ArrayList<albumArtistItem> getAllAlbums(){
        //public ArrayList<albumArtistItem> getAllAlbums(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        //String test = "";
        ArrayList<albumArtistItem> listData = new ArrayList<>();
        cursor = db.rawQuery("SELECT "+COLUMN_ALBUM+", COUNT(*) AS 'count' , "+COLUMN_ALBUMART+" from "+TABLE_SONGS+" GROUP BY "+COLUMN_ALBUM,null);
        while (cursor.moveToNext()) {
            Log.i("LOGGER0",cursor.getString(0));
            Log.i("LOGGER1",cursor.getString(1));


            albumArtistItem temp = new albumArtistItem(String.valueOf(cursor.getString(0)),String.valueOf(cursor.getString(1)),
                    String.valueOf(cursor.getString(2)));

            //test += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+
                    //cursor.getString(3) + " \n";

            listData.add(temp);

        }
        cursor.close();
        return listData;

    }

    public ArrayList<albumArtistItem> getAllArtists(){
        //public ArrayList<albumArtistItem> getAllAlbums(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        //String test = "";
        ArrayList<albumArtistItem> listData = new ArrayList<>();
        cursor = db.rawQuery("SELECT DISTINCT "+COLUMN_ARTIST+", COUNT(*) AS 'count' , "+COLUMN_ALBUMART+" from "+TABLE_SONGS+" GROUP BY "+COLUMN_ARTIST,null);
        //cursor = db.rawQuery("SELECT COUNT (DISTINCT "+COLUMN_ARTIST+"), "+COLUMN_ARTIST+" , "+COLUMN_ALBUMART+" from "+TABLE_SONGS+" GROUP BY "+COLUMN_ARTIST,null);
        while (cursor.moveToNext()) {

            Log.i("LOGGER11111",cursor.getString(1));
            albumArtistItem temp = new albumArtistItem(String.valueOf(cursor.getString(0)),String.valueOf(cursor.getString(1)),
                    String.valueOf(cursor.getString(2)));

            //test += cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+
            //cursor.getString(3) + " \n";

            listData.add(temp);

        }
        cursor.close();
        return listData;

    }

    public songItem getSongItem(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        songItem mySong = new songItem(null,null,null,null,null,null,null,null);
        Cursor cursor;
        Log.i("SONG ID",id);
        cursor = db.rawQuery("SELECT * from "+TABLE_SONGS+" where "+COLUMN_ID+"=\""+id+"\"",null);
        while(cursor.moveToNext()){
            mySong = new songItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));
        }
        cursor.close();
        return mySong;
    }

    public songItem getFirstSong(){
        SQLiteDatabase db = this.getReadableDatabase();
        songItem mySong = new songItem(null,null,null,null,null,null,null,null);
        Cursor cursor;

        cursor = db.rawQuery("SELECT * from "+TABLE_SONGS+" where 1 LIMIT 1",null);
        while(cursor.moveToNext()){
            mySong = new songItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));
        }
        cursor.close();
        return mySong;
    }
}
