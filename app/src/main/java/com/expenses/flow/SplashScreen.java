package com.expenses.flow;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }
        Intent intent = new Intent(this,MainActivity.class);
        Handler handler = new Handler();
        Runnable r = () -> {
            startActivity(intent);
            finish();
        };

        handler.postDelayed(r, 2000);

    }
}