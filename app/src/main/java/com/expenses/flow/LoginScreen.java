package com.expenses.flow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    Button loginButton;
    EditText emailTextInput;
    LinearLayout googleButton;
    ImageView githubButton;
    final String[] emailEntered = new String[1];
    int RC_SIGN_IN = 1;

    static Thread t;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    ProgressBar signInSpinner;

    static boolean userExist = false;

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
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        signInSpinner = findViewById(R.id.sign_in_spinner);

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
                signInSpinner.setVisibility(View.VISIBLE);
                googleButton.setVisibility(View.GONE);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e);
                signInSpinner.setVisibility(View.GONE);
                googleButton.setVisibility(View.VISIBLE );
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
                        assert user != null;
                        String userEmail = user.getEmail();
                        String userName = user.getDisplayName();

                        GlobalContent.setUserName(userName);
                        GlobalContent.setUserEmail(userEmail);

//                        try {
////                            t.join();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        Handler handler = new Handler();


                        DocumentReference docIdRef = db.collection("Users").document(GlobalContent.getUserEmail());
                        docIdRef.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot document = task1.getResult();
                                if (document.exists()) {
                                    Log.d("User Login", "Document exists!");
                                    userExist = true;
                                    FirebaseHelper.readFromFirebase();
                                } else {
                                    Log.d("User Login", "Document does not exist!");
                                    userExist = false;
                                    insertDataInFireStore(user);
                                }

                                Log.d("Sign In", "signInWithCredential:Sucess");
                                Toast.makeText(this,"Signed in as " + GlobalContent.getUserEmail() ,Toast.LENGTH_SHORT).show();
                                Runnable r = () -> {
                                    startActivity(new Intent(this, MainActivity.class));
                                    finish();
                                };

                                handler.postDelayed(r, 2000);

                            } else {
                                Log.d("User Login", "Failed with: ", task1.getException());
                                signInSpinner.setVisibility(View.GONE);
                                googleButton.setVisibility(View.VISIBLE);
                            }
                        });





                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Sign In", "signInWithCredential:failure", task.getException());
                        Toast.makeText(this,"Error Loggin in",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void insertDataInFireStore(FirebaseUser FirebaseUser){
        Map<String, Object> user = new HashMap<>();
        mAuth.getCurrentUser();

        String profilePictureUri = String.valueOf(FirebaseUser.getPhotoUrl());
        Bitmap[] userImage = {null};
        t=new Thread(() -> {
            try {
                URL url = new URL(profilePictureUri);
                userImage[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                if (userImage[0] != null) {
                    Log.d("Image", "Success");
                    GlobalContent.setProfileImage(userImage[0]);

                } else {
                    Log.d("Image", "Failure");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StorageReference storageRef = storage.getReference();

        StorageReference profileImg = storageRef.child("ProfileImages/"+GlobalContent.getUserEmail());

        Bitmap image = GlobalContent.getProfileImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = profileImg.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return profileImg.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
//                    String documentId = db.collection("Users").document(GlobalContent.getUserEmail()).getId();
                        Uri downloadUri = task.getResult();
                        Log.d("Downloaduri", downloadUri.toString());
                        user.put("Name", GlobalContent.getUserName());
                        user.put("Email", GlobalContent.getUserEmail());
                        user.put("CreditList", GlobalContent.getCreditList());
                        user.put("DebitList", GlobalContent.getCreditList());
                        user.put("ProfileImage", downloadUri.toString());
                        user.put("TotalDebit", 0);
                        user.put("TotalCredit", 0);
                        GlobalContent.setProfileImageUrl(downloadUri.toString());

// Add a new document with a generated ID

                        db.collection("Users").document(GlobalContent.getUserEmail())
                                .set(user)
                                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot added "))
                                .addOnFailureListener(e -> Log.w("Failure", "Error adding document", e));
                }

            }
        });



    }

}