package com.astrofittechnologies.intsimbi.models;

public class Song {

    private String name;
    private String artist;
    private String url;

    public Song(String name, String artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
    }

    public Song(){
        this.name = "";
        this.artist = "";
        this.url = "";
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
