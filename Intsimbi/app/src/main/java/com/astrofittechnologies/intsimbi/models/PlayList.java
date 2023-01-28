package com.astrofittechnologies.intsimbi.models;

import java.util.List;

public class PlayList {

    private Song currentlyPlaying;
    private List<Song> playList;

    public PlayList(Song currentlyPlaying, List<Song> playList) {
        this.currentlyPlaying = currentlyPlaying;
        this.playList = playList;
    }

    public PlayList() {
    }

    public Song getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(Song currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    public List<Song> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Song> playList) {
        this.playList = playList;
    }
}
