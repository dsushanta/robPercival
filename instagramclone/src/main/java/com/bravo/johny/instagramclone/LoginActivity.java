package com.bravo.johny.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText userNameEditText;
    EditText passwordEditText;
    Button logInButton;
    TextView signUpModeTextView;
    RelativeLayout backgroundRelativeLayout;
    ImageView logoImageView;


    public void showUserPhoto() {
        Intent intent = new Intent(getApplicationContext(), UserPhotoActivity.class);
        startActivity(intent);
        finish();
    }

    public void logIn(View view) {

        String userNameEntered = userNameEditText.getText().toString();
        String passwordEntered = passwordEditText.getText().toString();

        if(userNameEntered.matches("") || passwordEntered.matches("")) {
            Toast.makeText(this, "Both User Name and Password are required ...", Toast.LENGTH_SHORT).show();
        } else {

            ParseUser.logInInBackground(userNameEntered, passwordEntered, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null) {
                        Log.i("friends", "Login Successful");
                        showUserPhoto();
                    } else {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userNameEditText = findViewById(R.id.userNameEditTextView);
        passwordEditText = findViewById(R.id.passwordEditTextView);
        logInButton = findViewById(R.id.logInButton);
        signUpModeTextView = findViewById(R.id.signUpModeTextView);
        signUpModeTextView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);
        backgroundRelativeLayout = findViewById(R.id.backgroundRelativeLayout);
        logoImageView = findViewById(R.id.logoImageView);
        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser().getUsername() != null) {
            showUserPhoto();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signUpModeTextView) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(view.getId() == R.id.passwordEditTextView) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                logIn(view);
        }

        return false;
    }
}
