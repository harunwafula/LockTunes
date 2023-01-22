package com.astrofittechnologies.intsimbi.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.internal.Objects;

import java.io.IOException;
import java.util.Arrays;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener {

    // Static variable reference of single_instance
    // of type Singleton
    private static MediaService single_instance = null;

    // Declaring a variable of type String
    public MediaPlayer mediaPlayer;
    private Boolean isPaused = true;
    private Context context;
    private Boolean isAwake = false;

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

    public void streamSong(String url) throws IOException {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           String test =  Arrays.stream(mediaPlayer.getTrackInfo()).toArray().toString();
            Toast.makeText(this.context, test, Toast.LENGTH_SHORT).show();
        }
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


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    public Boolean getAwake() {
        return isAwake;
    }
}
