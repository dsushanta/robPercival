package com.bravo.johny.demoimages;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    Random random = new Random();

    public void changePic(View view) {

        image = findViewById(R.id.imageView);
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
        image.setImageResource(johnny_bravo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        image = findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.d("johny", ""+index);
                }
                image.setImageResource(johnny_bravo);
            }
        });*/
    }
}
