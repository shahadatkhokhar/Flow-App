package com.expenses.flow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginScreen extends AppCompatActivity {
    Button loginButton;
    EditText emailTextInput;
    LinearLayout googleButton;
    ImageView githubButton;
    final String[] emailEntered = new String[1];
    int RC_SIGN_IN = 1;

    static Thread t;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar!=null){
            actionbar.hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        googleButton = findViewById(R.id.google_button);
        githubButton = findViewById(R.id.github_button);

         googleButton.setOnClickListener(v->{
                 Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                 startActivityForResult(signInIntent, RC_SIGN_IN);
         });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Login", "firebaseAuthWithGoogle:" + account.getIdToken());
                    firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e);
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sign IN", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateContentsFromFirebase(user);
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(this,"Logged in as "+ user.getEmail(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign In", "signInWithCredential:failure", task.getException());
                        Toast.makeText(this,"Error Loggin in",Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
////            updateUI(account);
//            updateGlobalContents(account);
//            t.join();
//
//            Toast.makeText(this,"Logged in as "+ account.getEmail(),Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, MainActivity.class));
//
//
//
//        } catch (ApiException | InterruptedException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.e("signInfailed code=", e+"");
//            Toast.makeText(this,"Error Loggin in",Toast.LENGTH_SHORT).show();
////            updateUI(null);
//        }
//    }

//    public void updateGlobalContents(GoogleSignInAccount account){
//        String userEmail = account.getEmail();
//        String userName = account.getDisplayName();
//        String profilePictureUri = String.valueOf(account.getPhotoUrl());
//        Bitmap[] image = {null};
//        t=new Thread(() -> {
//            try {
//                URL url = new URL(profilePictureUri);
//                image[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                if (image[0] != null) {
//                    Log.d("Image", "Success");
//                    GlobalContent.setProfileImage(image[0]);
//
//                } else {
//                    Log.d("Image", "Failure");
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        t.start();
//
//        GlobalContent.setUserName(userName);
//        GlobalContent.setUserEmail(userEmail);
////        GlobalContent.setProfileImage(image[0]);
//    }

    public void updateContentsFromFirebase(FirebaseUser user){
        String userEmail = user.getEmail();
        String userName = user.getDisplayName();
        String profilePictureUri = String.valueOf(user.getPhotoUrl());
        Bitmap[] image = {null};
        t=new Thread(() -> {
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
        });
        t.start();

        GlobalContent.setUserName(userName);
        GlobalContent.setUserEmail(userEmail);
//        GlobalContent.setProfileImage(image[0]);
    }

//    public void signUp(String email) {
//        AuthSignUpOptions options = AuthSignUpOptions.builder()
//                .userAttribute(AuthUserAttributeKey.email(), email)
//                .build();
//        Amplify.Auth.signUp("username", generatePassword(), options,
//                result -> {
//                    Log.i("AuthQuickStart", "Result: " + result.toString());
//                    GlobalContent.isUserRegistered = true;
//                },
//                error -> Log.e("AuthQuickStart", "Sign up failed", error)
//        );
//    }
//    public static String generatePassword() {
//        SecureRandom random = new SecureRandom();
//        String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String ALPHA = "abcdefghijklmnopqrstuvwxyz";
//        String NUMERIC = "0123456789";
//        String SPECIAL_CHARS = "!@#$%^&*_=+-/";
//        String dic = ALPHA+ALPHA_CAPS+NUMERIC+SPECIAL_CHARS;
//        String result = "";
//        for (int i = 0; i < 8; i++) {
//            int index = random.nextInt(dic.length());
//            result += dic.charAt(index);
//        }
//        return result;
//    }
}