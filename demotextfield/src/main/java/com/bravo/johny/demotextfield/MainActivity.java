package com.bravo.johny.demotextfield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void login(View view) {
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.password);

        Log.i("friends","User Name : "+userName.getText().toString());
        Log.i("friends","Password : "+password.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
