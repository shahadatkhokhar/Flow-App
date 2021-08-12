package com.expenses.flow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import java.io.IOException;
import java.net.URL;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }



        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent intent;
        Handler handler = new Handler();
        GlobalDBContents.initDB(this);

        if (account == null) {
            intent = new Intent(this, LoginScreen.class);
        } else {
            updateGlobalContents(account);
            intent = new Intent(this, MainActivity.class);

        }
//        intent = new Intent(this, LoginScreen.class);
        Runnable r = () -> {
            startActivity(intent);
            finish();
        };

        handler.postDelayed(r, 2000);
    }


    public void updateGlobalContents(GoogleSignInAccount account) {
        String userEmail = account.getEmail();
        String userName = account.getDisplayName();
        String profilePictureUri = String.valueOf(account.getPhotoUrl());
        Bitmap[] image = {null};
        new Thread(() -> {
            try {
                URL url = new URL(profilePictureUri);
                image[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (image[0] != null) {
                    Log.d("Image", "Success");
                    GlobalContent.setProfileImage(image[0]);

                } else {
                    Log.d("Image", "Failure");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        GlobalContent.setUserName(userName);
        GlobalContent.setUserEmail(userEmail);
//        GlobalContent.setProfileImage(image[0]);
    }

}