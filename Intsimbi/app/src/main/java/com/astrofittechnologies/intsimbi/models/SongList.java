package com.astrofittechnologies.intsimbi.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongList implements Serializable {

    private List<Song> songs;

    public SongList() {
        songs = new ArrayList<>();
    }

    public SongList(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void addSong (Song song) {
        this.songs.add(song);
    }
}
