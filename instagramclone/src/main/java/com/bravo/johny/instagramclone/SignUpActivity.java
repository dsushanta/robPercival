package com.bravo.johny.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    EditText userNameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    EditText givenNameEditText;
    EditText familyNameEditText;
    EditText eMailEditText;
    EditText cityEditText;
    Button signUpButton;
    Button cancelButton;
    Button resetButton;
    RelativeLayout signUpBackgroundLayout;

    Boolean matchPasswords = false;

    public void showUserPhoto() {
        Intent intent = new Intent(getApplicationContext(), UserPhotoActivity.class);
        startActivity(intent);
    }

    private Boolean allFieldsHaveValues() {

        String userNameEntered = userNameEditText.getText().toString();
        String passwordEntered = passwordEditText.getText().toString();
        String confirmPasswordEntered = confirmPasswordEditText.getText().toString();
        String givenNameEntered = givenNameEditText.getText().toString();
        String familyNameEntered = familyNameEditText.getText().toString();
        String eMailEntered = eMailEditText.getText().toString();
        String cityEntered = cityEditText.getText().toString();

        Boolean userNameEmpty = userNameEntered.matches("");
        Boolean passwordEmpty = passwordEntered.matches("");
        Boolean confirmPasswordEmpty = confirmPasswordEntered.matches("");
        Boolean givenNameEmpty = givenNameEntered.matches("");
        Boolean familyNameEmpty = familyNameEntered.matches("");
        Boolean eMailEmpty = eMailEntered.matches("");
        Boolean cityEmpty = cityEntered.matches("");

        if(userNameEmpty || passwordEmpty || confirmPasswordEmpty || givenNameEmpty || familyNameEmpty || eMailEmpty || cityEmpty) {
            return false;
        } else {
            return true;
        }
    }

    public void signUp() {

        String userNameEntered = userNameEditText.getText().toString();
        String passwordEntered = passwordEditText.getText().toString();
        String givenNameEntered = givenNameEditText.getText().toString();
        String familyNameEntered = familyNameEditText.getText().toString();
        String eMailEntered = eMailEditText.getText().toString();
        String cityEntered = cityEditText.getText().toString();

        if(allFieldsHaveValues() && matchPasswords) {

            signUpButton.setEnabled(true);

            ParseUser user = new ParseUser();

            user.setUsername(userNameEntered);
            user.setPassword(passwordEntered);
            user.put("givenname", givenNameEntered);
            user.put("familyname", familyNameEntered);
            user.put("city", cityEntered);
            user.setEmail(eMailEntered);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("friends", "Sign Up Successful");
                        showUserPhoto();
                    } else {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        signUpButton.setEnabled(false);
                    }
                }
            });

        } else {
            Toast.makeText(this, "Please fill all the details ...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        userNameEditText = findViewById(R.id.userNameEditText);
        userNameEditText.setOnFocusChangeListener(this);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.setOnFocusChangeListener(this);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        confirmPasswordEditText.setOnFocusChangeListener(this);
        givenNameEditText = findViewById(R.id.givenNameEditText);
        givenNameEditText.setOnFocusChangeListener(this);
        familyNameEditText = findViewById(R.id.familyNameEditText);
        familyNameEditText.setOnFocusChangeListener(this);
        eMailEditText = findViewById(R.id.eMailEditText);
        eMailEditText.setOnFocusChangeListener(this);
        cityEditText = findViewById(R.id.cityEditText);
        cityEditText.setOnFocusChangeListener(this);
        signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(this);
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        signUpBackgroundLayout = findViewById(R.id.signUpBackgroundRelativeLayout);
        signUpBackgroundLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signUp) {
            signUp();
        } else if(v.getId() == R.id.cancelButton) {
            signUpButton.setEnabled(false);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if(v.getId() == R.id.resetButton) {
            userNameEditText.setText("");
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            givenNameEditText.setText("");
            familyNameEditText.setText("");
            eMailEditText.setText("");
            cityEditText.setText("");
            signUpButton.setEnabled(false);
        }  else if (v.getId() == R.id.signUpBackgroundRelativeLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus == false) {
            if(v.getId() == R.id.confirmPasswordEditText) {
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (password.equalsIgnoreCase(confirmPassword) && password.matches("") == false) {
                    matchPasswords = true;
                } else {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match ...", Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                    signUpButton.setEnabled(false);
                }
            } else if(v.onCheckIsTextEditor()) {
                if(allFieldsHaveValues() && matchPasswords) {
                    signUpButton.setEnabled(true);
                } else {
                    signUpButton.setEnabled(false);
                }
            }
        }
    }
}
