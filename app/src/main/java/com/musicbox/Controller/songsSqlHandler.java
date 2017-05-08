package com.musicbox.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musicbox.Model.songItem;

import java.util.ArrayList;

/**
 * Created by Jai on 4/30/2017.
 */

public class songsSqlHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1 ;
    private static final String DATABASE_NAME = "MusicBoxPlaylist4.db";
    public static final String TABLE_SONGS = "SONGS_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_ALBUMART = "albumArt";
    public static final String COLUMN_ALBUMID = "albumId";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_ALBUM = "album";

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

    /*
    public ArrayList<songItem> getAllAlbums(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        //String test = "";
        ArrayList<songItem> listData = new ArrayList<songItem>();
        cursor = db.rawQuery("SELECT DISTINCT "+COLUMN_ALBUM+" from "+TABLE_SONGS,null);
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
*/
}
