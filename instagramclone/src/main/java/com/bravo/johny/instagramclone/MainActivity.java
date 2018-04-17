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
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText userNameEditText;
    EditText passwordEditText;
    Button signUpButton;
    TextView changeModeTextView;
    RelativeLayout backgroundRelativeLayout;
    ImageView logoImageView;

    Boolean signUpModeActive = true;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {

        String userNameEntered = userNameEditText.getText().toString();
        String passwordEntered = passwordEditText.getText().toString();

        if(userNameEntered.matches("") && passwordEntered.matches("")) {
            Toast.makeText(this, "Both User Name and Password are required ...", Toast.LENGTH_SHORT).show();
        } else {

            if(signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(userNameEntered);
                user.setPassword(passwordEntered);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("friends", "Sign Up Successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(userNameEntered, passwordEntered, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null) {
                            Log.i("friends", "Login Successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEditText = findViewById(R.id.userNameEditTextView);
        passwordEditText = findViewById(R.id.passwordEditTextView);
        signUpButton = findViewById(R.id.signUpButton);
        changeModeTextView = findViewById(R.id.changeModeTextView);
        changeModeTextView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);
        backgroundRelativeLayout = findViewById(R.id.backgroundRelativeLayout);
        logoImageView = findViewById(R.id.logoImageView);
        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null)
            showUserList();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.changeModeTextView) {
            if(signUpModeActive) {
                signUpModeActive = false;
                signUpButton.setText("LOG IN");
                changeModeTextView.setText("or, SignUp");
            } else {
                signUpModeActive = true;
                signUpButton.setText("SIGN UP");
                changeModeTextView.setText("or, Login");
            }
        } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(view.getId() == R.id.passwordEditTextView) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                signUp(view);
        }

        return false;
    }
}
