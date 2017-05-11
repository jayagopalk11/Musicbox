package com.musicbox.Model;

/**
 * Created by Jai on 5/10/2017.
 */

public class albumArtistItem {

    String albumName;
    String songsInAlbum;
    String albumArt;

    public albumArtistItem(String albumName, String songsInAlbum, String albumArt) {
        this.albumName = albumName;
        this.songsInAlbum = songsInAlbum;
        this.albumArt = albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongsInAlbum() {
        return songsInAlbum;
    }

    public void setSongsInAlbum(String songsInAlbum) {
        this.songsInAlbum = songsInAlbum;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }
}
