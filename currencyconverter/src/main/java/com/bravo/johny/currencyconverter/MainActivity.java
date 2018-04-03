package com.bravo.johny.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void convert(View view) {
        EditText et = findViewById(R.id.editText);
        String dollar = et.getText().toString();
        Double inr = Double.parseDouble(dollar) * 67;
        Toast.makeText(this, "Rs "+inr, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
