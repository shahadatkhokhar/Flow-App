package com.expenses.flow;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {
    Button loginButton;
    TextView signupSuggestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        loginButton = findViewById(R.id.login_button);
        signupSuggestion = findViewById(R.id.signup_suggesstion);
        loginButton.setOnClickListener(v->{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        });
        signupSuggestion.setOnClickListener(v->{
            startActivity(new Intent(this,SignupScreen.class));
        });
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
    }
}