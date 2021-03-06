package com.expenses.flow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }



//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent intent;
        Handler handler = new Handler();
//        GlobalDBContents.initDB(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        int delayDuration=2000;

        if (currentUser == null) {
            intent = new Intent(this, LoginScreen.class);
            delayDuration=1000;
            Runnable r = () -> {
                startActivity(intent);
                finish();
            };

            handler.postDelayed(r, delayDuration);
        } else {
            updateGlobalContents(currentUser);
            intent = new Intent(this, MainActivity.class);

        }
//        intent = new Intent(this, LoginScreen.class);
//        Runnable r = () -> {
//            startActivity(intent);
//            finish();
//        };
//
//        handler.postDelayed(r, delayDuration);
    }


    public void updateGlobalContents(FirebaseUser user) {
        String userEmail = user.getEmail();
        String userName = user.getDisplayName();
//        String profilePictureUri = String.valueOf(user.getPhotoUrl());
//        Bitmap[] image = {null};
//        new Thread(() -> {
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
//        }).start();

//        GlobalContent.setUserName(userName);
        GlobalContent.setUserEmail(userEmail);

        readFromFirebase();
    }

//    public static void navigateToHomeScreen(){
//        Handler handler = new Handler();
//
//        Intent intent = new Intent(this, LoginScreen.class);
//        Runnable r = () -> {
//            startActivity(intent);
//            finish();
//        };
//
//        handler.postDelayed(r, 2000);
//    }

    public void readFromFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document(GlobalContent.getUserEmail());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("Read", "DocumentSnapshot data: " + document.getData());
                    ArrayList recievedDebitList = (ArrayList) document.get("DebitList");

                    ArrayList<ItemList> tempDebitList = new ArrayList<>();
                    assert recievedDebitList != null;
                    if(!(recievedDebitList.isEmpty())){
                    for(int counter=0;counter<recievedDebitList.size();counter++){
                        HashMap<HashMap<String,String>,HashMap<String, Integer>> item = (HashMap<HashMap<String, String>, HashMap<String, Integer>>) recievedDebitList.get(counter);

                        String itemNameLabel = (String) item.keySet().toArray()[0];
                        Log.d("itemnamelabel",itemNameLabel);
                        String itemAmountLabel = (String) item.keySet().toArray()[1];
                        Log.d("itemAmountLabel",itemAmountLabel);
                        String itemName = String.valueOf(item.get(itemNameLabel));
                        String itemAmount = String.valueOf(item.get(itemAmountLabel));

                        Log.d("item", itemName + ", " + itemAmount);

                        if (!itemName.equalsIgnoreCase("null") && !itemAmount.equalsIgnoreCase("null")) {
                            ItemList debit = new ItemList(itemName, Integer.parseInt(itemAmount));
                            tempDebitList.add(debit);
                        }
                    }
                    }
                    GlobalContent.setDebitList(tempDebitList);

                    ArrayList recievedCreditList = (ArrayList) document.get("CreditList");
                    ArrayList<ItemList> tempCreditList = new ArrayList<>();
                    assert recievedCreditList != null;
                    if(!(recievedCreditList.isEmpty())) {
                        for (int counter = 0; counter < recievedCreditList.size(); counter++) {
                            HashMap<HashMap<String, String>, HashMap<String, Integer>> item = (HashMap<HashMap<String, String>, HashMap<String, Integer>>) recievedCreditList.get(counter);

                            String itemNameLabel = (String) item.keySet().toArray()[0];
                            Log.d("itemnamelabel",itemNameLabel);
                            String itemAmountLabel = (String) item.keySet().toArray()[1];
                            Log.d("itemAmountLAbel",itemAmountLabel);
                            String itemName = String.valueOf(item.get(itemNameLabel));
                            String itemAmount = String.valueOf(item.get(itemAmountLabel));

                            Log.d("item", itemName + ", " + itemAmount);

                            if (!itemName.equalsIgnoreCase("null") && !itemAmount.equalsIgnoreCase("null")) {
                                ItemList credit = new ItemList(itemName, Integer.parseInt(itemAmount));
                                tempCreditList.add(credit);
                            }
                        }
                    }
                    GlobalContent.setCreditList(tempCreditList);



                    if(document.get("TotalCredit") != null) {
                        GlobalContent.setTotalCredit((Long) document.get("TotalCredit"));
                    }else {
                        GlobalContent.setTotalCredit(0L);
                    }

                    if(document.get("TotalDebit") != null) {
                        GlobalContent.setTotalDebit((Long) document.get("TotalDebit"));
                    }else {
                        GlobalContent.setTotalDebit(0L);
                    }
                    if(document.get("Name")!=null){
                        GlobalContent.setUserName(document.get("Name").toString());
                    }
                    String profilePhotoUrl = (String) document.get("ProfileImage");
                    Bitmap[] image = {null};
                    Thread t= new Thread(() -> {
                        try {
                            URL url = new URL(profilePhotoUrl);
                            image[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            if (image[0] != null) {
                                Log.d("Image", "Success");
                                GlobalContent.setProfileImage(image[0]);
                                startActivity(new Intent(this,MainActivity.class));
                                finish();
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

                } else {
                    Log.d("Read", "No such document");
                    Toast.makeText(this,"User details not found                          ",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,LoginScreen.class));
//                    finish();

                }
            } else {
                Log.d("Read", "get failed with ", task.getException());
            }
        });
    }

}