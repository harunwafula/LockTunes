package com.astrofittechnologies.intsimbi.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.astrofittechnologies.intsimbi.R;
import com.astrofittechnologies.intsimbi.models.Song;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Objects;

import java.io.IOException;
import java.util.Arrays;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    // Static variable reference of single_instance
    // of type Singleton
    private static MediaService single_instance = null;

    public MediaPlayer mediaPlayer;
    private Boolean isPaused = true;
    private Context context;
    private Boolean isAwake = false;


    private ImageView playPause;
    private ImageView currSongDp;
    private TextView currSongName;
    private int currIndex;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private MediaService(Context context)
    {
        mediaPlayer = new MediaPlayer();
        this.context = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static MediaService getInstance(Context context)
    {
        if (single_instance == null)
            single_instance = new MediaService(context);

        return single_instance;
    }

    public void streamSong(Song song, int index) throws IOException {
        String url = song.getUrl();
        currIndex = index;
        setCurrSong(song.getName(), song.getUrl());
        playPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        Glide.with(context)
                .load("https://firebasestorage.googleapis.com/v0/b/locktunes.appspot.com/o/Rectangle%202.png?alt=media&token=4ecfd37e-da85-48e4-b814-5d171f1ffd09")
                .into(currSongDp);
        //if the media was already playing or it has been paused
        if(mediaPlayer.isPlaying() || isPaused ){
            mediaPlayer.reset();

        }
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//           String test =  Arrays.stream(mediaPlayer.getTrackInfo()).toArray().toString();
//            Toast.makeText(this.context, test, Toast.LENGTH_SHORT).show();
//        }
        isAwake = true;


    }

    public void togglePause() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPaused = true;

        }else {
            mediaPlayer.start();
            isPaused = false;
        }

    }

    public int getCurrIndex(){
        return currIndex;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    public Boolean getIsPlaying(){
        return mediaPlayer.isPlaying();
    }

    public Boolean getAwake() {
        return isAwake;
    }

    public void setController(ConstraintLayout layout){
        playPause = layout.findViewById(R.id.play_toggle);
        currSongDp = layout.findViewById(R.id.song_dp_img);
        currSongName = layout.findViewById(R.id.song_name_txt);
    }

    public void setCurrSong (String name, String songDpUrl) {
        currSongName.setText(name);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);

    }
}
