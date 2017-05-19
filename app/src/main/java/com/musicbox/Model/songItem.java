package com.musicbox.Model;

/**
 * Created by Jai on 4/30/2017.
 */

public class songItem {

    private String title;
    private String artist;
    private String duration;
    private String id;
    private String albumArt;
    private String albumId;
    private String weight;
    private String album;



    public songItem( String id, String title, String artist, String duration, String albumArt, String albumId, String weight, String album) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;

        this.id = id;
        this.albumArt = albumArt;
        this.albumId = albumId;
        this.weight = weight;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
