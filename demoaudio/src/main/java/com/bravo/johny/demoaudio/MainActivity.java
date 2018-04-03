package com.bravo.johny.demoaudio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    AudioManager audioManager;
    boolean playing;


    public void playpause(View view) {
        if(playing) {
            mPlayer.pause();
            playing = false;
        } else {
            mPlayer.start();
            playing = true;
        }
    }

    public void stop(View view) {
        mPlayer.seekTo(0);
        mPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playing = false;

        mPlayer = MediaPlayer.create(this, R.raw.pintu);
        /*mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(this, Uri.parse("http://192.168.0.2:8080/pintu.mp3"));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeControl = findViewById(R.id.volume);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(curVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar progress = findViewById(R.id.progress);
        progress.setMax(mPlayer.getDuration());

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progress.setProgress(mPlayer.getCurrentPosition());
            }
        }, 0, 100);
    }
}
