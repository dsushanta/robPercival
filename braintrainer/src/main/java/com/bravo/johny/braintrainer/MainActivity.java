package com.bravo.johny.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView startView, resultImageView;
    RelativeLayout gameLayout;
    TextView scoreView, timerView, problemView, resultTextView;
    Button button0, button1, button2, button3, playAgainButton;
    Random random;

    int locationCorrectAnswer, incorrectAnswer, score=0, numerOfQuestions=0;
    List<Integer> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = findViewById(R.id.startImageView);
        gameLayout = findViewById(R.id.gameLayout);
        scoreView = findViewById(R.id.scoreTextView);
        timerView = findViewById(R.id.timerTextView);
        problemView = findViewById(R.id.problemTextView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButton);
        resultImageView = findViewById(R.id.resultImageView);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public void playAgain(View view) {
        score = 0;
        numerOfQuestions = 0;
        timerView.setText("30");
        scoreView.setText("0/0");
        resultTextView.setVisibility(View.INVISIBLE);
        resultImageView.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);

        generateQuestion();

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                playAgainButton.setVisibility(View.VISIBLE);
                timerView.setText("0");
                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                resultImageView.setVisibility(View.INVISIBLE);
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setTranslationX(-1000f);
                resultTextView.setText("SCORE : "+score+" / "+numerOfQuestions);
                resultTextView.animate().translationX(25f).rotation(720).setDuration(2000);
            }
        }.start();
    }

    public void start(View view) {
        startView.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        playAgain(playAgainButton);
    }

    public void chooseAnswer(View view) {

        if(view.getTag().toString().equalsIgnoreCase(Integer.toString(locationCorrectAnswer))) {
            score++;
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.drawable.right);
        } else {
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.drawable.wrong);
        }
        numerOfQuestions++;
        scoreView.setText(Integer.toString(score)+"/"+Integer.toString(numerOfQuestions));
        generateQuestion();
    }

    public void generateQuestion() {

//        resultImageView.setVisibility(View.INVISIBLE);
        random = new Random();

        int a = random.nextInt(50);
        int b = random.nextInt(50);

        problemView.setText(Integer.toString(a)+" + "+Integer.toString(b));

        answers.clear();
        locationCorrectAnswer = random.nextInt(4);

        for(int i=0; i<4; i++) {
            if(i == locationCorrectAnswer)
                answers.add(a+b);
            else {
                incorrectAnswer = random.nextInt(99);
                while(incorrectAnswer == a+b)
                    incorrectAnswer = random.nextInt(99);
                answers.add(incorrectAnswer);
            }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }
}
