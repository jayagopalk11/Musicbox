package com.musicbox.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musicbox.Model.songItem;

/**
 * Created by Jai on 4/30/2017.
 */

public class songsSqlHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1 ;
    private static final String DATABASE_NAME = "MusicBoxPlaylist.db";
    public static final String TABLE_SONGS = "SONGS_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_ALBUMART = "albumArt";
    public static final String COLUMN_ALBUMID = "albumId";
    public static final String COLUMN_WEIGHT = "weight";

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
                COLUMN_ALBUMID + " TEXT" +
                COLUMN_WEIGHT + " TEXT " +
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
        SQLiteDatabase db = getWritableDatabase();
        db.insertOrThrow(TABLE_SONGS, null, values);

        db.close();
    }

    public void deleteSongItem(songItem songItem){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SONGS + " WHERE "+ COLUMN_ID + "=\""+ songItem.getId() + "\"" + " AND "+
                COLUMN_TITLE + "=\""+ songItem.getTitle() + "\"" );
    }

}
