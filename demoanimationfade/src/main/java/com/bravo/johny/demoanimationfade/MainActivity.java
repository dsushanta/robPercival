package com.bravo.johny.demoanimationfade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView image1, image2;
    Random random = new Random();

    public void crossFade(View view) {
        Log.i("johny", view.toString());
        image1 = findViewById(R.id.imageView1);
        image2 = findViewById(R.id.imageView2);
        int johnny_bravo;
        int index = random.nextInt(4);
        switch (index) {
            case 0:
                johnny_bravo = R.drawable.johnny_bravo0;
                break;
            case 1:
                johnny_bravo = R.drawable.johnny_bravo1;
                break;
            case 2:
                johnny_bravo = R.drawable.johnny_bravo2;
                break;
            default:
                johnny_bravo = R.drawable.johnny_bravo3;
                Log.d("johny", ""+johnny_bravo);
        }
        image1.animate().alpha(0f).setDuration(2000);
        image2.setImageResource(johnny_bravo);
        image2.animate().alpha(1f).setDuration(2000);
        image2.bringToFront();
    }

    public void crossFade2(View view) {
        Log.i("johny", view.toString());
        image1 = findViewById(R.id.imageView1);
        image2 = findViewById(R.id.imageView2);
        int johnny_bravo;
        int index = random.nextInt(4);
        switch (index) {
            case 0:
                johnny_bravo = R.drawable.johnny_bravo0;
                break;
            case 1:
                johnny_bravo = R.drawable.johnny_bravo1;
                break;
            case 2:
                johnny_bravo = R.drawable.johnny_bravo2;
                break;
            default:
                johnny_bravo = R.drawable.johnny_bravo3;
                Log.d("johny", ""+johnny_bravo);
        }
        image2.animate().alpha(0f).setDuration(2000);
        image1.setImageResource(johnny_bravo);
        image1.animate().alpha(1f).setDuration(2000);
        image1.bringToFront();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
