package com.expenses.flow;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DetailsEntryScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_entry_screen);

        Button continueButton = findViewById(R.id.detail_entry_screen_continue_button);
        EditText nameInput = findViewById(R.id.detail_screen_name_text_input);
        TextView profilePictureSelectButton = findViewById(R.id.details_screen_profile_select_button);
        ImageView profilePicture = findViewById(R.id.detail_screen_profile_image);
        final Bitmap[] image = {null};

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    Log.d("Activity result",uri+"");
                    profilePicture.setImageURI(Uri.parse(String.valueOf(uri)));

                    try {
                        image[0] = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(image[0] !=null){
                        GlobalContent.setProfileImage(image[0]);
                    }

                });

        profilePictureSelectButton.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            mGetContent.launch("image/*");
        });

        continueButton.setOnClickListener(v->{
            String nameEntered = nameInput.getText().toString().trim();

            if(nameEntered.length() == 0){
                nameInput.setError("Enter Name");
            }
            else {
                GlobalContent.setUserName(nameInput.getText().toString().trim());
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

}