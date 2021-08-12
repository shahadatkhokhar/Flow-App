package com.expenses.flow;

import static com.expenses.flow.GlobalContent.getTotalCreditAmount;
import static com.expenses.flow.GlobalContent.getTotalDebitAmount;
import static com.expenses.flow.GlobalContent.setDebitList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.room.Room;

import com.expenses.flow.database.UserDetails;
import com.expenses.flow.database.UserDetailsDB;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GlobalDBContents {

    static UserDetailsDB db;

//    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
//        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
//        image.compress(compressFormat, quality, byteArrayOS);
//        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
//    }
//
//    public static Bitmap decodeBase64(String input) {
//        byte[] decodedBytes = Base64.decode(input, 0);
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }

    public static void insertInDB() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.name = GlobalContent.getUserName();
        userDetails.email = GlobalContent.getUserEmail();
        userDetails.totalCreditAmount = GlobalContent.getTotalCreditAmount();
        userDetails.totalDebitAmount = GlobalContent.getTotalDebitAmount();
//        userDetails.profileImage = encodeToBase64(GlobalContent.getProfileImage(), Bitmap.CompressFormat.JPEG, 100);
        userDetails.CreditList.addAll(GlobalContent.getCreditList());
        userDetails.DebitList.addAll(GlobalContent.getDebitList());

        try{
            new Thread(() -> {
                db.UserDetailsDao().insert(userDetails);
            }).start();
        }
        catch (Exception e){
            throw e;
        }

    }

    public static void initDB(Context context){
        db = Room.databaseBuilder(context,
                UserDetailsDB.class, "User1").build();
    }

    public static void updateCreditListInDb(String email, ArrayList<ItemList> creditList){
        GlobalContent.setCreditList(creditList);
        new Thread(()->{
            db.UserDetailsDao().updateCreditListInDB(email, creditList);
            db.UserDetailsDao().updateTotalCredit(email, getTotalCreditAmount());
        }).start();
    }

    public static void updateDebitListInDb(String email, ArrayList<ItemList> debitList){
        setDebitList(debitList);
        new Thread(()->{
            db.UserDetailsDao().updateDebitListInDB(email, debitList);
            db.UserDetailsDao().updateTotalDebit(email, getTotalDebitAmount());
        }).start();
    }

    public static void readFromDB(String email){
        UserDetails[] userDetails = new UserDetails[1];
        new Thread(() -> {
            userDetails[0] = db.UserDetailsDao().getUserDetails(email);
//            String username = userDetails[0].name;
//            if(username!=null)
//            {
//                Log.d("username",username);
//            }
//            else{
//                Log.d("username","not found");
//            }
        }).start();
        String userName = userDetails[0].name;
        String userEmail = userDetails[0].email;
        int totalDebit = userDetails[0].totalDebitAmount;
        int totalCredit = userDetails[0].totalCreditAmount;
//        Bitmap profileImage = userDetails[0].profileImage;

        GlobalContent.setUserEmail(userEmail);
        GlobalContent.setUserName(userName);
        GlobalContent.setTotalDebit(totalDebit);
        GlobalContent.setTotalCredit(totalCredit);
        GlobalContent.setDebitList(userDetails[0].DebitList);
        GlobalContent.setCreditList(userDetails[0].CreditList);
//        if(profileImage!=null){
//            GlobalContent.setProfileImage(profileImage);
//        }
    }
    public static void readWriteTest(String email){

            UserDetails userDetails = new UserDetails();
            userDetails.name = GlobalContent.getUserName();
            userDetails.email = GlobalContent.getUserEmail();
            userDetails.totalCreditAmount = GlobalContent.getTotalCreditAmount();
            userDetails.totalDebitAmount = GlobalContent.getTotalDebitAmount();
//            if(GlobalContent.getProfileImage()!=null){
                userDetails.profileImage = GlobalContent.getProfileImage();
//            }
            if(userDetails.CreditList!=null && GlobalContent.getCreditList()!=null) {
                userDetails.CreditList.addAll(GlobalContent.getCreditList());
            }
        if(userDetails.DebitList!=null && GlobalContent.getDebitList()!=null) {
            userDetails.DebitList.addAll(GlobalContent.getDebitList());
        }
            new Thread(() -> {
                try{
                    Log.d("insert","Data written");

                    db.UserDetailsDao().insert(userDetails);
                }catch (Exception e)
                {
                    Log.e("Exception", e + "");
                    UserDetails[] userDetailsRead = new UserDetails[1];
                        userDetailsRead[0] = db.UserDetailsDao().getUserDetails(email);
                        String userName = userDetailsRead[0].name;
                         int totalDebit = userDetailsRead[0].totalDebitAmount;
                         int totalCredit = userDetailsRead[0].totalCreditAmount;
                        String userEmail = userDetailsRead[0].email;

                      Bitmap profileImage = userDetailsRead[0].profileImage;

                    GlobalContent.setUserEmail(userEmail);
                    GlobalContent.setUserName(userName);
                    GlobalContent.setTotalDebit(totalDebit);
                    GlobalContent.setTotalCredit(totalCredit);
                    GlobalContent.setDebitList(userDetailsRead[0].DebitList);
                    GlobalContent.setCreditList(userDetailsRead[0].CreditList);
                    if(profileImage!=null){
                        GlobalContent.setProfileImage(profileImage);
                        Log.d("Profile","Profile Image read");
                    }
                    Log.d("Success","Reading Data");
                }
            }).start();

    }

}
