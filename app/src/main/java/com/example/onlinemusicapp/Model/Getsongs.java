package com.example.onlinemusicapp.Model;

public class Getsongs {

    String songscategory,songTitle,artist,album_art,songDuration,songlink,mkey;

    public Getsongs(String songscategory, String songTitle, String artist, String album_art, String songDuration, String songlink) {

        if (songTitle.trim().equals("")){
            songTitle="No Title";
        }

        this.songscategory = songscategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songlink = songlink;
    }

    public Getsongs() {
    }

    public String getsongscategory() {
        return songscategory;
    }

    public void setSongscategory(String songscategory) {
        this.songscategory = songscategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getsongDuration() {
        return songDuration;
    }

    public void setsongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSonglink() {
        return songlink;
    }

    public void setSonglink(String songlink) {
        this.songlink = songlink;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
