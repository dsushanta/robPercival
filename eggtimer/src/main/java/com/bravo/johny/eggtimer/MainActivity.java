package com.bravo.johny.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView eggImage;
    SeekBar timeLeftSeekBar;
    Button goPauseButton, resetButton;
    TextView timerTextView;
    CountDownTimer timer;
    boolean counterIsActive = false;

    public void updateTimer(int progress) {
        int minutes = progress / 60;
        int seconds = progress % 60;

        String secondsString = Integer.toString(seconds);

        if(seconds <= 9)
            secondsString = "0"+secondsString;

        timerTextView.setText(Integer.toString(minutes)+":"+secondsString);
    }

    public void reset(View view) {
        eggImage.setImageResource(R.drawable.egg);
        timerTextView.setAlpha(1f);
        resetTimer();
    }

    public void resetTimer() {
        counterIsActive = false;
        timerTextView.setText("0:30");
        timeLeftSeekBar.setProgress(30);
        timer.cancel();
        goPauseButton.setText("GO !");
        goPauseButton.setEnabled(true);
        timeLeftSeekBar.setEnabled(true);
    }

    public void finish() {
        counterIsActive = false;
        timerTextView.setText("0:00");
        timer.cancel();
        goPauseButton.setText("GO !");
        goPauseButton.setEnabled(false);
        timerTextView.setAlpha(0f);
        eggImage.setImageResource(R.drawable.broken_egg);
    }

    public void controlTimer(View view) {
        if(counterIsActive == false) {
            timeLeftSeekBar.setEnabled(false);
            play();
            counterIsActive = true;
        }else {
            pause();
        }
    }

    public void pause() {
        goPauseButton.setText("GO !");
        timer.cancel();
        counterIsActive = false;
    }

    public void play() {
        resetButton.setEnabled(true);
        goPauseButton.setText("PAUSE");
        timer = new CountDownTimer(timeLeftSeekBar.getProgress()*1000+100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftSeekBar.setProgress((int)millisUntilFinished/1000);
                updateTimer((int)millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell);
                mPlayer.start();
                finish();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eggImage = findViewById(R.id.eggImageView);
        timeLeftSeekBar = findViewById(R.id.timeLeftSeekBar);
        timerTextView = findViewById(R.id.timerTextView);
        goPauseButton = findViewById(R.id.goPauseButtonView);
        resetButton = findViewById(R.id.resetButtonView);

        timeLeftSeekBar.setMax(600);
        timeLeftSeekBar.setProgress(30);

        timeLeftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
