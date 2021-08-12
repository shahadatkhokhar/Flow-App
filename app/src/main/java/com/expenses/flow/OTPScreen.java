package com.expenses.flow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class OTPScreen extends AppCompatActivity {
    Button continueButton;
    EditText OTPInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        continueButton = findViewById(R.id.otp_screen_continue_button);
        OTPInput = findViewById(R.id.otp_input);
        continueButton.setOnClickListener(v->{
            String OTPEntered = OTPInput.getText().toString().trim();
            if(OTPEntered.length() == 0){
                OTPInput.setError("Enter OTP");
            }
            else{
//                confirmSignUp(OTPEntered);
                if(GlobalContent.isSignUpcomplete){
                    startActivity(new Intent(this, DetailsEntryScreen.class));
                    finish();
                }
                else{
                    OTPInput.setError("Incorrect OTP");
                }
            }
        });

    }

}