package com.bravo.johny.guessthenumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int count = 0;
    int number;
    public void guess(View view) {

        EditText editText = findViewById(R.id.editText);

        if(editText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a number first", Toast.LENGTH_SHORT).show();
            return;
        }

        int numberEntered = Integer.parseInt(editText.getText().toString());
        if(numberEntered < number) {
            toastAMessage("You Entered a lower number", number, numberEntered);
            count++;
        }
        else if(numberEntered > number) {
            toastAMessage("You Entered a higher number", number, numberEntered);
            count++;
        }
        else {
            toastAMessage("Tada.... You got it after "+(count+1)+" attempts !!", number, numberEntered);
            count = 0;
            number = generateRandomNumber();
            editText.setText("");
        }
    }

    private void toastAMessage(String message, int number, int numberEntered) {
        Log.i("johny", "The number is : "+number);
        Log.i("johny", "Number entered : "+numberEntered);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public int generateRandomNumber() {
        Random random = new Random();
        int number = random.nextInt(100)+1;
        return number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = generateRandomNumber();
    }
}
