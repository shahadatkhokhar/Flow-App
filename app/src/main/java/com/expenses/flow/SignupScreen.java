package com.expenses.flow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignupScreen extends AppCompatActivity {
    TextView loginSuggestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
//        loginButton = findViewById(R.id.login_button);
        loginSuggestion = findViewById(R.id.login_suggesstion);
//        loginButton.setOnClickListener(v->{
//            startActivity(new Intent(this,MainActivity.class));
//            finish();
//        });
        loginSuggestion.setOnClickListener(v->{
            startActivity(new Intent(this,LoginScreen.class));
            finish();
        });
    }
}